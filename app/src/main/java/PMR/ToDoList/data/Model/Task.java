package PMR.ToDoList.data.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = ToDoList.class,
        parentColumns = "idToDoList",
        childColumns = "keyToDoList"),
        tableName = "task_table")
public class Task implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    private int idTask;
    private String label;
    private int checked;
    private int keyToDoList;


    //CONSTRUCTEURS

    public Task(int idTask, String label) {
        this.idTask = idTask;
        this.label = label;
    }

    public Task(int idTask, String label, int checked) {
        this.idTask = idTask;
        this.label = label;
        this.checked = checked;
    }

    protected Task(Parcel in) {
        label = in.readString();
        checked = in.readInt();
        idTask = in.readInt();
    }

    //GETTERS & SETTERS


    public int getIdTask() { return idTask; }

    public void setIdTask(int idTask) { this.idTask = idTask; }

    public String getLabel() { return label; }

    public void setLabel(String label) { this.label = label; }

    public int getChecked() { return checked; }

    public void setChecked(int checked) { this.checked = checked; }

    public int getKeyToDoList() { return keyToDoList; }

    public void setKeyToDoList(int keyToDoList) { this.keyToDoList = keyToDoList; }

    // PARCELABLE IMPLEMENTATION
    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeInt(this.checked);
        dest.writeInt(this.idTask);
    }

    //Methodes utiles

    @Override
    public String toString() {
        return "Task{" +
                "idTask=" + idTask +
                ", label='" + label + '\'' +
                ", checked=" + checked +
                '}';
    }


}
