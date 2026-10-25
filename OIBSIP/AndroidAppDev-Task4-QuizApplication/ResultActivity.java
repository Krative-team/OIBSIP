package com.example.quizapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_result);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 10);

        TextView result = findViewById(R.id.txtResult);
        TextView detail = findViewById(R.id.txtDetail);
        Button restart = findViewById(R.id.btnRestart);

        result.setText(score + " / " + total);
        detail.setText("Correct: " + score + "\nIncorrect: " + (total - score) + "\nPercentage: " + (score * 100 / total) + "%");

        restart.setOnClickListener(v -> {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        });
    }
}
