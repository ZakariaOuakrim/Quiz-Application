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

public class Quiz1 extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button nextButton;
    private RadioButton radioButton;

    String correctResp="Le soleil";
    int score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz1);

        radioGroup = findViewById(R.id.radioGroup1);
        nextButton = findViewById(R.id.buttonNext);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(Quiz1.this, "Veuillez choisir une réponse!", Toast.LENGTH_SHORT).show();
                }else{
                    radioButton=findViewById(radioGroup.getCheckedRadioButtonId());
                    if(radioButton.getText().toString().equals(correctResp)){
                        score+=1;
                    }
                    Intent i1=new Intent(Quiz1.this, Quiz2.class);
                    i1.putExtra("score",score);
                    startActivity(i1);
                }
            }
        });


    }
}