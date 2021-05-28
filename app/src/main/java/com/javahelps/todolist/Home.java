package com.javahelps.todolist;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Home extends Fragment {
    public static final String TAG = "ListView";
    Toolbar toolbar;
    ListView listView;
    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar=view.findViewById(R.id.addTask);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        listView=view.findViewById(R.id.taskView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: " +position);
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                Task task = (Task) listView.getItemAtPosition(position);
                DatabaseHandler.getInstance(getContext()).updateTask(task);
                //databaseHandler.deleteTask(task.getId());
                initListViewData();
            }
        });
        //

        /*this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printSelectedItems();
            }
        });*/

        initListViewData();
    }
    private void initListViewData()  {
        /*Task tom = new Task("Tom","admin");
        Task jerry = new Task("Jerry","user");
        Task donald = new Task("Donald","guest", false);*/

        Task[] users = DatabaseHandler.getInstance(getContext()).getAllTasks().toArray(new Task[0]);

        // android.R.layout.simple_list_item_checked:
        // ListItem is very simple (Only one CheckedTextView).
        ArrayAdapter<Task> arrayAdapter
                = new ArrayAdapter<Task>(getActivity(), android.R.layout.simple_list_item_checked, users);

        this.listView.setAdapter(arrayAdapter);

        for(int i=0;i< users.length; i++ )  {
            this.listView.setItemChecked(i,users[i].isActive());
        }
    }

    // When user click "Print Selected Items".
    /*public void printSelectedItems()  {

        SparseBooleanArray sp = listView.getCheckedItemPositions();

        StringBuilder sb= new StringBuilder();

        for(int i=0;i<sp.size();i++){
            if(sp.valueAt(i)==true){
                UserAccount user= (UserAccount) listView.getItemAtPosition(i);
                // Or:
                // String s = ((CheckedTextView) listView.getChildAt(i)).getText().toString();
                //
                String s= user.getUserName();
                sb = sb.append(" "+s);
            }
        }
        Toast.makeText(this, "Selected items are: "+sb.toString(), Toast.LENGTH_LONG).show();
    }*/
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionBar:
                //TastyToast.makeText(getActivity(), "Fuck!", TastyToast.LENGTH_LONG, TastyToast.INFO);
                final View dialogView = View.inflate(getActivity(), R.layout.date_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText= (EditText) dialogView.findViewById(R.id.task_input);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Task task=new Task(editText.getText().toString(),timePicker.getCurrentHour().toString()+":"+timePicker.getCurrentMinute().toString());
                        DatabaseHandler.getInstance(getContext()).addTask(task);
                        initListViewData();
                        alertDialog.dismiss();
                    }});
                alertDialog.setView(dialogView);
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}