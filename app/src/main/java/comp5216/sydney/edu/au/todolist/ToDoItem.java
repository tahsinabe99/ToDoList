package comp5216.sydney.edu.au.todolist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "todolist")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "toDoItemID")
    private int toDoItemID;

    @ColumnInfo(name = "toDoItemName")
    private String toDoItemName;

    @ColumnInfo(name = "deadline")
    private String deadline;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "isChecked")
    private Boolean isChecked;


    public ToDoItem(String toDoItemName){
        this.toDoItemName = toDoItemName;
        this.isChecked=false;
    }

//    public ToDoItem(String toDoItemName, String type, String deadline){
//        this.toDoItemName = toDoItemName;
//        this.type=type;
//        this.deadline=deadline;
//    }

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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDeadline(String deadline) {
         this.deadline=deadline;
    }

    public String getDeadline() {
        return this.deadline;
    }

    public Boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked=isChecked;
    }


}
