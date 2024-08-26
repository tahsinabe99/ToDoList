package comp5216.sydney.edu.au.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoItemAdapter extends ArrayAdapter<Item> {

    public ToDoItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tvItemName);
        TextView tvDeadline = convertView.findViewById(R.id.tvItemDeadline);
        TextView tvType = convertView.findViewById(R.id.tvItemType);

        // Populate the data into the template view using the data object
        tvName.setText(item.getName());
        tvDeadline.setText(item.getDeadline());
        tvType.setText(item.getType());

        // Return the completed view to render on screen
        return convertView;
    }
}

