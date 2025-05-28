package com.zakaria.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button nextButton;
    private TextView questionText, questionNumberText, timerText;
    private ImageView questionImage;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer countDownTimer;
    private final long COUNTDOWN_IN_MILLIS = 10000; // 5 seconds
    private long timeLeftInMillis;
    private MediaPlayer tickSoundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize views
        radioGroup = findViewById(R.id.radioGroup);
        nextButton = findViewById(R.id.buttonNext);
        questionText = findViewById(R.id.textViewQuestion);
        questionNumberText = findViewById(R.id.textViewQuestionNumber);
        questionImage = findViewById(R.id.imageViewQuestion);
        timerText = findViewById(R.id.timerText);
        tickSoundPlayer = MediaPlayer.create(this, R.raw.clock_ticking); // tick.mp3 in res/raw


        // Initialize questions
        initializeQuestions();

        // Load first question
        loadQuestion(currentQuestionIndex);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                processAnswerAndMoveNext();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                if (tickSoundPlayer != null) {
                    tickSoundPlayer.seekTo(0); // Rewind to start
                    tickSoundPlayer.start();
                }
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateTimerText();
                // Time's up - move to next question automatically
                processAnswerAndMoveNext();
            }
        }.start();
    }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeFormatted = String.format("Time: %02d", seconds);
        timerText.setText(timeFormatted);

        // Change color when time is running out
        if (seconds <= 2) {
            timerText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            timerText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void processAnswerAndMoveNext() {
        // Check if an answer was selected
        if (radioGroup.getCheckedRadioButtonId() != -1) {
            checkAnswer();
        }
        // Else: no answer selected = 0 points

        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            // Reset timer for next question
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            loadQuestion(currentQuestionIndex);
            radioGroup.clearCheck();
        } else {
            // Quiz finished
            Intent resultIntent = new Intent(QuizActivity.this, Score.class);
            resultIntent.putExtra("score", score);
            resultIntent.putExtra("total", questions.size());
            startActivity(resultIntent);
            finish();
        }
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();

        questions.add(new Question(
                1,
                "Quelle est une source d'énergie renouvelable ?",
                new String[]{"Le charbon", "Le pétrole", "Le soleil"},
                "Le soleil",
                R.drawable.renewable_energy
        ));

        questions.add(new Question(
                2,
                "Quelle énergie est produite grâce au vent ?",
                new String[]{"L'énergie éolienne", "L'énergie hydraulique", "L'énergie géothermique"},
                "L'énergie éolienne",
                R.drawable.wind
        ));

        questions.add(new Question(
                3,
                "Quel est le principal avantage des énergies renouvelables ?",
                new String[]{"Elles sont gratuites à produire", "Elles sont inépuisables", "Elles ne nécessitent aucun entretien"},
                "Elles sont inépuisables",
                R.drawable.advantage
        ));

        questions.add(new Question(
                4,
                "Quelle énergie utilise la chaleur de la Terre ?",
                new String[]{"L'énergie solaire", "L'énergie géothermique", "L'énergie marémotrice"},
                "L'énergie géothermique",
                R.drawable.soil
        ));

        questions.add(new Question(
                5,
                "Quel pays est l'un des leaders mondiaux de l'énergie solaire ?",
                new String[]{"L'Arabie Saoudite", "La Chine", "Le Canada"},
                "La Chine",
                R.drawable.sun
        ));
    }

    private void loadQuestion(int index) {
        // Reset timer for new question
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        updateTimerText();

        Question currentQuestion = questions.get(index);

        // Set question number
        questionNumberText.setText("Question " + (index + 1));

        // Set question text
        questionText.setText(currentQuestion.getQuestionText());

        // Set image if available
        if (currentQuestion.getImageResource() != 0) {
            questionImage.setVisibility(View.VISIBLE);
            questionImage.setImageResource(currentQuestion.getImageResource());
        } else {
            questionImage.setVisibility(View.GONE);
        }

        // Clear previous radio buttons
        radioGroup.removeAllViews();

        // Add new radio buttons for options
        for (int i = 0; i < currentQuestion.getOptions().length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(currentQuestion.getOptions()[i]);
            radioButton.setTextSize(16);
            radioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            radioGroup.addView(radioButton);
        }

        // Update button text if last question
        if (index == questions.size() - 1) {
            nextButton.setText("Finish");
        } else {
            nextButton.setText("Next");
        }

        // Start timer for this question
        startTimer();
    }

    private void checkAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton radioButton = findViewById(selectedId);
            Question currentQuestion = questions.get(currentQuestionIndex);
            if (radioButton.getText().toString().equals(currentQuestion.getCorrectAnswer())) {
                score++;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tickSoundPlayer != null) {
            tickSoundPlayer.release();
            tickSoundPlayer = null;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}