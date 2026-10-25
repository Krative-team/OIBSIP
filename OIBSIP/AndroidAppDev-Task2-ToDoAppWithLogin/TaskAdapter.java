package com.example.todoapp;
import android.graphics.Paint;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.VH> {
    ArrayList<DBHelper.Task> list; DBHelper db; Runnable refresh;
    public TaskAdapter(ArrayList<DBHelper.Task> l, DBHelper db, Runnable r){ list=l; this.db=db; refresh=r; }
    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t){
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_task, p, false));
    }
    @Override public void onBindViewHolder(@NonNull VH h, int pos){
        DBHelper.Task task=list.get(pos);
        h.title.setText(task.title); h.notes.setText(task.notes);
        h.check.setChecked(task.isDone);
        if(task.isDone) h.title.setPaintFlags(h.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else h.title.setPaintFlags(h.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

        h.check.setOnCheckedChangeListener((b, checked)->{
            db.updateTaskStatus(task.id, checked);
            refresh.run();
        });
        h.delete.setOnClickListener(v->{ db.deleteTask(task.id); refresh.run(); });
    }
    @Override public int getItemCount(){ return list.size(); }
    static class VH extends RecyclerView.ViewHolder{
        TextView title, notes; CheckBox check; ImageButton delete;
        VH(View v){ super(v); title=v.findViewById(R.id.txtTitle); notes=v.findViewById(R.id.txtNotes);
            check=v.findViewById(R.id.checkDone); delete=v.findViewById(R.id.btnDelete); }
    }
}
