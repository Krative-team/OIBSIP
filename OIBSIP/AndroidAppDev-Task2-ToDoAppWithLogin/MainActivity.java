package com.example.todoapp;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHelper db; int userId; RecyclerView rv; TextView emptyView;
    ArrayList<DBHelper.Task> tasks; TaskAdapter adapter;
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_main);
        db=new DBHelper(this);
        userId=getSharedPreferences("session", MODE_PRIVATE).getInt("user_id", -1);
        rv=findViewById(R.id.recyclerTasks); emptyView=findViewById(R.id.emptyView);
        Button add=findViewById(R.id.btnAddTask); Button logout=findViewById(R.id.btnLogout);

        rv.setLayoutManager(new LinearLayoutManager(this));
        loadTasks();

        add.setOnClickListener(v -> showAddDialog());
        logout.setOnClickListener(v -> {
            getSharedPreferences("session", MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class)); finish();
        });
    }
    void loadTasks(){
        tasks=db.getTasks(userId);
        if(tasks.isEmpty()){ emptyView.setVisibility(View.VISIBLE); rv.setVisibility(View.GONE); }
        else { emptyView.setVisibility(View.GONE); rv.setVisibility(View.VISIBLE); }
        adapter=new TaskAdapter(tasks, db, this::loadTasks);
        rv.setAdapter(adapter);
    }
    void showAddDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        EditText title=view.findViewById(R.id.taskTitle); EditText notes=view.findViewById(R.id.taskNotes);
        new AlertDialog.Builder(this).setTitle("Add Task").setView(view)
            .setPositiveButton("Add", (d,w)->{
                if(!title.getText().toString().isEmpty()){
                    db.addTask(userId, title.getText().toString(), notes.getText().toString());
                    loadTasks();
                }
            }).setNegativeButton("Cancel", null).show();
    }
}
