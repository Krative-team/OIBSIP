package com.example.quizapp;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    TextView counter, questionText, scoreText;
    Button opt1, opt2, opt3, opt4, nextBtn;
    ArrayList<Question> questions;
    int currentIndex = 0, score = 0;
    boolean answered = false;
    Button[] optionButtons;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_quiz);

        counter = findViewById(R.id.txtCounter);
        questionText = findViewById(R.id.txtQuestion);
        scoreText = findViewById(R.id.txtScore);
        opt1 = findViewById(R.id.btnOpt1);
        opt2 = findViewById(R.id.btnOpt2);
        opt3 = findViewById(R.id.btnOpt3);
        opt4 = findViewById(R.id.btnOpt4);
        nextBtn = findViewById(R.id.btnNext);

        optionButtons = new Button[]{opt1, opt2, opt3, opt4};
        questions = Question.getQuestions();
        // take only 10
        if (questions.size() > 10) questions = new ArrayList<>(questions.subList(0, 10));

        loadQuestion();

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            optionButtons[i].setOnClickListener(v -> checkAnswer(finalI));
        }

        nextBtn.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex < questions.size()) loadQuestion();
            else {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("total", questions.size());
                startActivity(intent);
                finish();
            }
        });
    }

    void loadQuestion() {
        answered = false;
        Question q = questions.get(currentIndex);
        counter.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        questionText.setText(q.questionText);
        scoreText.setText("Score: " + score);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(q.options[i]);
            optionButtons[i].setBackgroundColor(Color.parseColor("#E0E0E0"));
            optionButtons[i].setEnabled(true);
        }
        nextBtn.setEnabled(false);
    }

    void checkAnswer(int selected) {
        if (answered) return;
        answered = true;
        Question q = questions.get(currentIndex);
        if (selected == q.correctIndex) {
            optionButtons[selected].setBackgroundColor(Color.parseColor("#4CAF50")); // green
            score++;
            scoreText.setText("Score: " + score);
        } else {
            optionButtons[selected].setBackgroundColor(Color.parseColor("#F44336")); // red
            optionButtons[q.correctIndex].setBackgroundColor(Color.parseColor("#4CAF50"));
        }
        for (Button btn : optionButtons) btn.setEnabled(false);
        nextBtn.setEnabled(true);
    }
}
