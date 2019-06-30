package PMR.ToDoList.data.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(onDelete = CASCADE,entity = ToDoList.class,
        parentColumns = "idToDoList",
        childColumns = "keyToDoList"),
        tableName = "task_table")
public class Task implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    private int idTask; //////////////////////////////////////////////
    private String label;
    private int keyToDoList;
    private int checked;


    //CONSTRUCTEURS

    public Task(int idTask, String label, int keyToDoList, int checked) {
        this.idTask = idTask;
        this.label = label;
        this.keyToDoList = keyToDoList;
        this.checked = checked;
    }

    @Ignore
    protected Task(Parcel in) {
        idTask = in.readInt();
        label = in.readString();
        keyToDoList = in.readInt();
        checked = in.readInt();
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
        dest.writeInt(this.idTask);
        dest.writeString(this.label);
        dest.writeInt(this.keyToDoList);
        dest.writeInt(this.checked);
    }

    //Methodes utiles


    @Override
    public String toString() {
        return "Task{" +
                "idTask=" + idTask +
                ", label='" + label + '\'' +
                ", keyToDoList=" + keyToDoList +
                ", checked=" + checked +
                '}';
    }
}
