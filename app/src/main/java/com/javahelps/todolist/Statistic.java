package com.javahelps.todolist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;


public class Statistic extends Fragment {
    TextView textView1,textView2;
    public Statistic() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView1=view.findViewById(R.id.taskDone);
        textView2=view.findViewById(R.id.taskUndone);
        textView1.setText("Task done: "+String.valueOf(DatabaseHandler.getInstance(getContext()).countDone()));
        textView2.setText("Task undone: "+String.valueOf(DatabaseHandler.getInstance(getContext()).countUndone()));
    }
}