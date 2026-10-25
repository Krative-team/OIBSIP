package com.example.todoapp;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    EditText name, email, pass; Button register; DBHelper db;
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_signup);
        name=findViewById(R.id.name); email=findViewById(R.id.email);
        pass=findViewById(R.id.password); register=findViewById(R.id.btnRegister);
        db=new DBHelper(this);
        register.setOnClickListener(v -> {
            if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()){
                Toast.makeText(this,"All fields required",Toast.LENGTH_SHORT).show(); return;
            }
            long res = db.registerUser(name.getText().toString(), email.getText().toString(), pass.getText().toString());
            if(res != -1){ Toast.makeText(this,"Registered! Please login",Toast.LENGTH_SHORT).show(); finish(); }
            else Toast.makeText(this,"Email already exists",Toast.LENGTH_SHORT).show();
        });
    }
}
