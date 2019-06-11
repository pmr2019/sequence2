package PMR.ToDoList.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import PMR.ToDoList.Model.User;
import PMR.ToDoList.R;

/*
Classe pour la création d'un Adapter pour l'activité Settings, qui utilise un viewHolder
définit plus bas.
Dans cette classe, on créé une interface OnItemClickListener pour pouvoir cliquer
sur chacun des éléments du Recyclerview
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingViewHolder> {

    private ArrayList<User> settings;
    private OnItemClickListener settingListener;

    public SettingsAdapter(ArrayList<User> settings){

        this.settings =settings;
    }

    // INTERFACE ONITEMCLICKLISTENER POUR ECOUTER A L'INTERIEUR

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        settingListener =listener;
    }

    // METHODES A IMPLEMENTER DANS L'ADAPTER
    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting,parent,false);
        SettingViewHolder tdlv=new SettingViewHolder(v, settingListener);
        return tdlv;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        User user = settings.get(position);
        holder.login.setText(user.getLogin());
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    /*
    Classe du View Holder lié à l'adapter ci-dessus.
    On y ajoute un Onclicklistener définit plus haut pour écouter les clics sur
    chacun des items de la liste du recyclerview.
     */
    public class SettingViewHolder extends RecyclerView.ViewHolder{

        public TextView login;
        public ImageView btnDeleteSetting;

        public SettingViewHolder(@NonNull View itemView,  final OnItemClickListener settingListener) {
            super(itemView);
            login=itemView.findViewById(R.id.textSetting);
            btnDeleteSetting=itemView.findViewById(R.id.btnDeleteSetting);

            btnDeleteSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (settingListener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            settingListener.onDeleteClick(position);
                        }
                    }
                }
            });


        }
    }

}
