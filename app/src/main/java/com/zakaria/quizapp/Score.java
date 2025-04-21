package com.zakaria.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Score extends AppCompatActivity {


    Button blogOut, tryAgain;

    TextView tvScore;
    ProgressBar pb;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);

        blogOut=findViewById(R.id.buttonLogOut);
        tryAgain=findViewById(R.id.buttonTryAgain);
        tvScore=findViewById(R.id.scoreid);
        pb=findViewById(R.id.progressBar);

        Intent i = getIntent();
        score=i.getIntExtra("score",0);
        tvScore.setText(score*100/5+"%");
        pb.setProgress(score*100/5);

        blogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Merci pour votre participation",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Score.this,MainActivity.class));
                finish();
            }
        });
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Score.this, Quiz1.class));
                finish();
            }
        });



    }
}