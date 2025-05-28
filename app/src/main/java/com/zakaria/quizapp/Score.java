package com.zakaria.quizapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Score extends AppCompatActivity {

    private Button btnLogOut, btnTryAgain;
    private TextView tvScore;
    private ProgressBar progressBar;
    private int score;
    private FirebaseAuth mAuth;
    private LinearLayout leaderboardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mAuth = FirebaseAuth.getInstance();
        btnLogOut = findViewById(R.id.buttonLogOut);
        btnTryAgain = findViewById(R.id.buttonTryAgain);
        tvScore = findViewById(R.id.scoreid);
        progressBar = findViewById(R.id.progressBar);
        leaderboardLayout = findViewById(R.id.leaderboardLayout); // Add this ID to your XML

        // Get score from intent
        Intent i = getIntent();
        score = i.getIntExtra("score", 0);
        int totalQuestions = i.getIntExtra("total", 5);
        int percentage = (score * 100) / totalQuestions;

        // Set score text
        tvScore.setText(String.format("%d%%", percentage));

        // Animate progress bar
        progressBar.setMax(100);
        ObjectAnimator.ofInt(progressBar, "progress", 0, percentage)
                .setDuration(1000)
                .start();

        // Save the current score
        saveScore(percentage);

        // Display leaderboard
        displayLeaderboard();
        // Log out button
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(Score.this, "Merci pour votre participation",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Score.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Try again button
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Score.this, QuizActivity.class));
                finish();
            }
        });
    }
    private void saveScore(int percentage) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String username = currentUser.getDisplayName();
            if (username != null && !username.isEmpty()) {
                SharedPreferences prefs = getSharedPreferences("Leaderboard", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                // Save the score with username as key (this is a simple approach)
                editor.putInt(username + "_score", percentage);
                editor.apply();
            }
        }
    }
    private void displayLeaderboard() {
        SharedPreferences prefs = getSharedPreferences("Leaderboard", MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();

        List<ScoreEntry> scores = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().endsWith("_score") && entry.getValue() instanceof Integer) {
                String username = entry.getKey().replace("_score", "");
                int score = (Integer) entry.getValue();
                scores.add(new ScoreEntry(username, score));
            }
        }

        // Sort in descending order
        Collections.sort(scores, (o1, o2) -> o2.score - o1.score);

        // Display top 3
        leaderboardLayout.removeAllViews();

        TextView title = new TextView(this);
        title.setText("Top Players");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        title.setPadding(0, 20, 0, 10);
        leaderboardLayout.addView(title);

        int count = Math.min(3, scores.size());
        for (int i = 0; i < count; i++) {
            ScoreEntry entry = scores.get(i);
            TextView tv = new TextView(this);
            tv.setText(String.format("%d. %s: %d%%", i+1, entry.username, entry.score));
            tv.setTextSize(16);
            tv.setPadding(0, 5, 0, 5);
            leaderboardLayout.addView(tv);
        }
    }

    private static class ScoreEntry {
        String username;
        int score;

        ScoreEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }
    }
}