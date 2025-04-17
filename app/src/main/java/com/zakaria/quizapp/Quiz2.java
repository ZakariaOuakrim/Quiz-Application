package com.zakaria.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Quiz2 extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button nextButton;
    private RadioButton radioButton;

    String correctResp="L’énergie éolienne";
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz2);

        radioGroup = findViewById(R.id.radioGroup1);
        nextButton = findViewById(R.id.buttonNext);
        Intent intent = getIntent();
       score=intent.getIntExtra("score",0);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(Quiz2.this, "Veuillez choisir une réponse!", Toast.LENGTH_SHORT).show();
                }else{
                    radioButton=findViewById(radioGroup.getCheckedRadioButtonId());
                    if(radioButton.getText().toString().equals(correctResp)){
                        score+=1;
                    }
                    Intent i1=new Intent(Quiz2.this, Quiz3.class);
                    i1.putExtra("score",score);
                    startActivity(i1);
                }
            }
        });


    }
}