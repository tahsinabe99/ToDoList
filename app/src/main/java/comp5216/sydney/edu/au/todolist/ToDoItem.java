package comp5216.sydney.edu.au.todolist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;


@Entity(tableName = "todolist")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "toDoItemID")
    private int toDoItemID;

    @ColumnInfo(name = "toDoItemName")
    private String toDoItemName;

    @ColumnInfo(name="toDoItemDeadline")
    private String deadline;

    public ToDoItem(String toDoItemName, String deadline){
        this.toDoItemName = toDoItemName;
        this.deadline=deadline;
    }

    public int getToDoItemID() {
        return toDoItemID;
    }

    public void setToDoItemID(int toDoItemID) {
        this.toDoItemID = toDoItemID;
    }

    public String getToDoItemName() {
        return toDoItemName;
    }

    public void setToDoItemName(String toDoItemName) {
        this.toDoItemName = toDoItemName;
    }

    public String getToDoItemDeadline() {
        return deadline;
    }

    public void setToDoItemDeadline(String toDoItemNameDeadline) {
        this.deadline = deadline;
    }
}
