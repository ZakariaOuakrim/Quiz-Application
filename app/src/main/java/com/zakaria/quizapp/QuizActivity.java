package com.zakaria.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button nextButton;
    private TextView questionText, questionNumberText;
    private ImageView questionImage;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

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

        // Initialize questions
        initializeQuestions();

        // Load first question
        loadQuestion(currentQuestionIndex);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(QuizActivity.this, "Please select an answer!", Toast.LENGTH_SHORT).show();
                } else {
                    checkAnswer();
                    currentQuestionIndex++;

                    if(currentQuestionIndex < questions.size()) {
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
            }
        });
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();

        // Question 1 (from Quiz1)
        questions.add(new Question(
                1,
                "Quelle est une source d'énergie renouvelable ?",
                new String[]{"Le charbon", "Le pétrole", "Le soleil"},
                "Le soleil",
                R.drawable.renewable_energy
        ));

        // Question 2 (from Quiz2)
        questions.add(new Question(
                2,
                "Quelle énergie est produite grâce au vent ?",
                new String[]{"L'énergie éolienne", "L'énergie hydraulique", "L'énergie géothermique"},
                "L'énergie éolienne",
                R.drawable.wind
        ));

        // Question 3 (from Quiz3)
        questions.add(new Question(
                3,
                "Quel est le principal avantage des énergies renouvelables ?",
                new String[]{"Elles sont gratuites à produire", "Elles sont inépuisables", "Elles ne nécessitent aucun entretien"},
                "Elles sont inépuisables",
                R.drawable.advantage
        ));

        // Question 4 (from Quiz4)
        questions.add(new Question(
                4,
                "Quelle énergie utilise la chaleur de la Terre ?",
                new String[]{"L'énergie solaire", "L'énergie géothermique", "L'énergie marémotrice"},
                "L'énergie géothermique",
                R.drawable.soil
        ));

        // Question 5 (from Quiz5)
        questions.add(new Question(
                5,
                "Quel pays est l'un des leaders mondiaux de l'énergie solaire ?",
                new String[]{"L'Arabie Saoudite", "La Chine", "Le Canada"},
                "La Chine",
                R.drawable.sun
        ));
    }

    private void loadQuestion(int index) {
        Question currentQuestion = questions.get(index);

        // Set question number
        questionNumberText.setText("Question " + currentQuestion.getQuestionNumber());

        // Set question text
        questionText.setText(currentQuestion.getQuestionText());

        // Set image if available
        if(currentQuestion.getImageResource() != 0) {
            questionImage.setVisibility(View.VISIBLE);
            questionImage.setImageResource(currentQuestion.getImageResource());
        } else {
            questionImage.setVisibility(View.GONE);
        }

        // Clear previous radio buttons
        radioGroup.removeAllViews();

        // Add new radio buttons for options
        for(int i = 0; i < currentQuestion.getOptions().length; i++) {
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
        if(index == questions.size() - 1) {
            nextButton.setText("Finish");
        } else {
            nextButton.setText("Next");
        }
    }

    private void checkAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        Question currentQuestion = questions.get(currentQuestionIndex);
        if(radioButton.getText().toString().equals(currentQuestion.getCorrectAnswer())) {
            score++;
        }
    }
}