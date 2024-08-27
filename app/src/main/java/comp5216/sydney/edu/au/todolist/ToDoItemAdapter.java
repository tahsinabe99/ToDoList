package comp5216.sydney.edu.au.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {

    public ToDoItemAdapter(Context context, ArrayList<ToDoItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Getting data item for this position
        ToDoItem item = getItem(position);

        // Checking if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }


        // Lookup view for data population
        TextView tvName =convertView.findViewById(R.id.tvItemName);
        TextView tvDeadline=convertView.findViewById(R.id.tvItemDeadline);
        TextView tvType=convertView.findViewById(R.id.tvItemType);
        CheckBox checkBox=convertView.findViewById(R.id.checkBox);

        // Populate the data into the template view using the data object
        tvName.setText(item.getToDoItemName());
        tvDeadline.setText(item.getDeadline());
        tvType.setText(item.getType());


        checkBox.setOnCheckedChangeListener(null); // Unbind previous listeners to avoid issues
        checkBox.setChecked(false); // Default state, can be customized if needed
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle the checkbox click event
            if (isChecked) {
                // Perform your action when the checkbox is checked
                Toast.makeText(getContext(), "Task Completed: " + item.getToDoItemName(), Toast.LENGTH_SHORT).show();

                // You could also mark the item as completed in the database or perform other actions
            } else {
                // Handle the case when the checkbox is unchecked
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


}

