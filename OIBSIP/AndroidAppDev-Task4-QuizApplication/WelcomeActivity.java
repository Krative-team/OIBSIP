package com.example.quizapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_welcome);
        Button start = findViewById(R.id.btnStart);
        start.setOnClickListener(v -> startActivity(new Intent(this, QuizActivity.class)));
    }
}
