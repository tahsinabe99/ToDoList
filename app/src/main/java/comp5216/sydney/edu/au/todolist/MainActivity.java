package comp5216.sydney.edu.au.todolist;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.FileUtils;


public class MainActivity extends AppCompatActivity {

    // Define variables
    ListView listView;
    ArrayList<ToDoItem> items;
    EditText addItemEditText;
    ToDoItemDB db;
    ToDoItemDao toDoItemDao;
    ToDoItemAdapter itemAdapter;
    private ActivityResultLauncher<Intent> mLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items=new ArrayList<>();
        // Use "activity_main.xml" as the layout
        setContentView(R.layout.activity_main);
        mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {

                        // Extract name, type, deadline value from result extras
                        String editedItem = result.getData().getExtras().getString("item");
                        String editedItemType = result.getData().getExtras().getString("type");
                        String editedItemDeadline = result.getData().getExtras().getString("deadline");
                        int position = result.getData().getIntExtra("position", -1);
                        Log.d("Position", String.format("%d", position));
                        if (position != -1 && editedItem != null) {
                            ToDoItem newItem=new ToDoItem(editedItem);
                            newItem.setDeadline(editedItemDeadline);
                            newItem.setType(editedItemType);
                            items.set(position, newItem);

                            Log.i("Updated item in list", editedItem + ", position: " + position);

                            // Show a toast for the updated item
                            Toast.makeText(getApplicationContext(), "Updated: " + editedItem, Toast.LENGTH_SHORT).show();

                            // Notify adapter of changes
                            itemAdapter.sortItem();
                            itemAdapter.notifyDataSetChanged();

                            // Save updated list to the database
                            saveItemsToDatabase();
                        }
                    }
                }
        );

        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.lstView);
        addItemEditText = (EditText) findViewById(R.id.txtNewItem);

        // Create an instance of ToDoItemDB and ToDoItemDao
        db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        toDoItemDao = db.toDoItemDao();
        readItemsFromDatabase();

        // Create an adapter for the list view using Android's built-in item layout
        //itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, items);
        itemAdapter=new ToDoItemAdapter(this, items);
        // Connect the listView and the adapter
        listView.setAdapter(itemAdapter);
        checkAndUpdateOverdueItems();

        // Setup listView listeners
        setupListViewListener();
    }

    public void onAddItemClick( View view) {

        Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);

        String toAddString = addItemEditText.getText().toString();
        if (toAddString != null && toAddString.length() > 0) {
            ToDoItem newItem=new ToDoItem(toAddString);
            items.add(newItem); // Add text to list view adapter
//            addItemEditText.setText("");
//            saveItemsToDatabase();
        }
        int newPosition= items.size()-1;

        addItemEditText.setText("");
        //saveItemsToDatabase();

        intent.putExtra("item", toAddString);
        intent.putExtra("position", newPosition);

        mLauncher.launch(intent);

        }

    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId)
            {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                items.remove(position); // Remove item from the ArrayList
                                itemAdapter.sortItem();
                                itemAdapter.notifyDataSetChanged(); // Notify listView adapter to update the list

                                saveItemsToDatabase();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });
                //sortItem();
                builder.create().show();
                return true;
            }
        });

        // Register a request to start an activity for result and register the result callback
//        ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK) {
//                        // Extract name value from result extras
//                        String editedItem = result.getData().getExtras().getString("item");
//                        int position = result.getData().getIntExtra("position", -1);
//                        items.set(position, editedItem);
//                        Log.i("Updated item in list ", editedItem + ", position: " + position);
//
//                        // Make a standard toast that just contains text
//                        Toast.makeText(getApplicationContext(), "Updated: " + editedItem, Toast.LENGTH_SHORT).show();
//                        itemsAdapter.notifyDataSetChanged();
//
//                        //saveItemsToFile();
//                        saveItemsToDatabase();
//                    }
//                }
//        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*Will handle checkbox  function later*/
//                boolean isChecked=listView.isItemChecked(position);
//                System.out.println(position);
//                if (isChecked){
//                    return;
//                }

                //CheckBox checkBox=view.findViewById(android.R.id.text1);
                /*
                * Temporatily setting so clicking on checkbox item does not do anything
                * */
//                if (checkBox !=null & checkBox.isPressed()){
//                    return;
//                }

                ToDoItem updateItem = (ToDoItem) itemAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);

                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("item", updateItem.getToDoItemName());
                    intent.putExtra("position", position);

                    // bring up the second activity
                    mLauncher.launch(intent);
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

//    private void readItemsFromFile(){
//        //retrieve the app's private folder.
//        //this folder cannot be accessed by other apps
//        File filesDir = getFilesDir();
//        //prepare a file to read the data
//        File todoFile = new File(filesDir,"todo.txt");
//        //if file does not exist, create an empty list
//        if(!todoFile.exists()){
//            items = new ArrayList<String>();
//        }else{
//            try{
//                //read data and put it into the ArrayList
//                items = new ArrayList<String>(FileUtils.readLines(todoFile, "UTF-8"));
//            }
//            catch(IOException ex){
//                items = new ArrayList<String>();
//            }
//        }
//    }

//    private void saveItemsToFile(){
//        File filesDir = getFilesDir();
//        //using the same file for reading. Should use define a global string instead.
//        File todoFile = new File(filesDir,"todo.txt");
//        try{
//            //write list to file
//            FileUtils.writeLines(todoFile,items);
//        }
//        catch(IOException ex){
//            ex.printStackTrace();
//        }
//    }

    private void readItemsFromDatabase()
    {
        //Use asynchronous task to run query on the background and wait for result
        try {
            // Run a task specified by a Runnable Object asynchronously.
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    //read items from database
                    List<ToDoItem> itemsFromDB = toDoItemDao.listAll();
                    items = new ArrayList<>();
                    if (itemsFromDB != null && itemsFromDB.size() > 0) {
                        for (ToDoItem item : itemsFromDB) {
                            items.add(item);
                            Log.i("SQLite read item", "ID: " + item.getToDoItemID() + " Name: " + item.getToDoItemName());
                        }
                    }
                    System.out.println("I'll run in a separate thread than the main thread.");
                }
            });
            // Block and wait for the future to complete
            future.get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
            Log.e("readItemsToDatabase", Log.getStackTraceString(ex));

        }
    }

    private void saveItemsToDatabase()
    {
        //Use asynchronous task to run query on the background to avoid locking UI
        try {
            // Run a task specified by a Runnable Object asynchronously.
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    //delete all items and re-insert
                    toDoItemDao.deleteAll();
                    for (ToDoItem todo : items) {
                        toDoItemDao.insert(todo);
                        Log.i("SQLite saved item", todo.getToDoItemName());
                    }
                    System.out.println("I'll run in a separate thread than the main thread.");

                }
            });

            // Block and wait for the future to complete
            future.get();
        }
        catch(Exception ex) {
            Log.e("saveItemsToDatabase", ex.getStackTrace().toString());
            Log.e("saveItemsToDatabase", Log.getStackTraceString(ex));

        }
    }

    private void checkAndUpdateOverdueItems() {
        long currentTime=System.currentTimeMillis();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault());

        for (ToDoItem item:items) {
            try {
                Date deadlineDate=formatter.parse(item.getDeadline());

                if (deadlineDate !=null && deadlineDate.getTime()<currentTime) {
                    item.setDeadline("Overdue");  // Mark the deadline as overdue
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        itemAdapter.notifyDataSetChanged();  // Refresh the list after updating
    }

//    private void sortItem(){
//        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault());
//        Collections.sort(items, (item1, item2)->{
//            if(item1.getIsChecked()==item2.getIsChecked())
//                try{
//                    Date date1=formatter.parse(item1.getDeadline());
//                    Date date2=formatter.parse(item2.getDeadline());
//                    return date1.compareTo(date2);
//                }catch (ParseException e){
//                    Log.i("Parsing Problem", e.toString());
//                    e.printStackTrace();
//                    return 0;
//                }
//            return item1.getIsChecked() ? 1 : -1;
//
//        });

//    }


}