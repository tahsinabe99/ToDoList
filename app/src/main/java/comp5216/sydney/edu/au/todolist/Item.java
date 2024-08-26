package comp5216.sydney.edu.au.todolist;


public class Item {
    private String name;
    private String deadline;
    private String type;

    public Item(String name, String deadline, String type) {
        this.name=name;
        this.deadline=deadline;
        this.type=type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline=deadline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type=type;
    }
}
