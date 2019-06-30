package fr.syned.sequence1_todolist.activities.recyclerview.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import fr.syned.sequence1_todolist.model.Profile;
import fr.syned.sequence1_todolist.R;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>{

    private List<Profile> mDataset;

    private int removedPosition = 0;
    private Profile removedProfile;

    public ProfileAdapter(List<Profile> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public ProfileAdapter.ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_profile, parent, false);
        return new ProfileAdapter.ProfileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ProfileViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {


        private TextView username;

        public ProfileViewHolder(@NonNull View v) {
            super(v);
            username = v.findViewById(R.id.username);
        }

        public void bind(Profile profile) {
            username.setText(profile.getUsername());
        }

    }

    public void removeItem(RecyclerView.ViewHolder viewHolder) {
        removedPosition = viewHolder.getAdapterPosition();
        removedProfile = mDataset.get(viewHolder.getAdapterPosition());

        mDataset.remove(viewHolder.getAdapterPosition());
        notifyItemRemoved(viewHolder.getAdapterPosition());

        // TODO: Faire en sorte que la snackbar ne masque pas l'EditText et le FloatingActionButton
        Snackbar.make(viewHolder.itemView, removedProfile.getUsername() + " deleted.",Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreLastItem(removedPosition, removedProfile);
            }
        }).show();
    }

    public void restoreLastItem(int position, Profile profile) {
        mDataset.add(position, profile);
        notifyItemInserted(position);
    }
}
