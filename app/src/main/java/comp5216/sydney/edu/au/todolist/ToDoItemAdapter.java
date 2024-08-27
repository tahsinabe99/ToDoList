package comp5216.sydney.edu.au.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {

    private ArrayList<ToDoItem> items;

    public ToDoItemAdapter(Context context, ArrayList<ToDoItem> items) {
        super(context, 0, items);
        this.items=items;
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
        checkBox.setChecked(item.getIsChecked()); // Default state, can be customized if needed
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Perform your action when the checkbox is checked
                Toast.makeText(getContext(), "Task Completed: " + item.getToDoItemName(), Toast.LENGTH_SHORT).show();
                item.setIsChecked(true);
                sortItem();
                this.notifyDataSetChanged();

            } else {
                item.setIsChecked(false);
                sortItem();
                notifyDataSetChanged();

            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

//    public void sortChecked(ToDoItem item){
//        if (items.size()>1){
//            ToDoItem tempItem=item;
//            items.remove(item);
//            items.add(tempItem);
//        }
//    }

    public void sortItem(){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault());
        Collections.sort(items, (item1, item2)->{
            if(item1.getIsChecked()==item2.getIsChecked())
            try{
                Date date1=formatter.parse(item1.getDeadline());
                Date date2=formatter.parse(item2.getDeadline());
                return date1.compareTo(date2);
            }catch (ParseException e){
                Log.i("Parsing Problem", e.toString());
                e.printStackTrace();
                return 0;
            }
            return item1.getIsChecked() ? 1 : -1;

        });

    }

}

