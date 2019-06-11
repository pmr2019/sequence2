package PMR.ToDoList.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;


public class Task implements Parcelable {
    private String description;
    private Boolean fait;

    public Task(String description, Boolean fait) {
        this.description = description;
        this.fait = fait;
    }

    public Task(String description) {
        this.description = description;
        this.fait = Boolean.FALSE;
    }

    protected Task(Parcel in) {
        description = in.readString();
        byte tmpFait = in.readByte();
        fait = tmpFait == 0 ? null : tmpFait == 1;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFait() {
        return fait;
    }

    public void setFait(Boolean fait) {
        this.fait = fait;
    }

    @Override
    public String toString() {
        return ("Item : "+ this.getDescription() + " - Fait : " +this.getFait().toString());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeByte((byte) (this.fait ? 1 : 0));
    }
}
