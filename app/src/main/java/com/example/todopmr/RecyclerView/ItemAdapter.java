package com.example.todopmr.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todopmr.Modele.ItemToDo;
import com.example.todopmr.R;

import java.util.ArrayList;

/*
Cette activité gère l'adapter associé aux items.
 */
public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private final ArrayList<ItemToDo> items;
    private final ActionListenerItem actionListener;

    /*
    Constructeur
    */
    public ItemAdapter(ArrayList<ItemToDo> item, ActionListenerItem actionListener) {
        this.items = item;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_presentation, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ItemViewHolder) holder).bind(items.get(position));
    }

    /*
    Calcul du nombre d'items.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /*
    Actualisation de l'affichage
     */
    public void actualiserAffichage() {
        notifyDataSetChanged();
    }

    /*
    Classe de définition de la vue d'une liste.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView description;
        private final ImageView btn_poubelle;
        private final CheckBox check_fait;

        /*
        Constructeur
         */
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            this.description = itemView.findViewById(R.id.description);
            this.check_fait = itemView.findViewById(R.id.checkbox_fait);
            this.btn_poubelle = itemView.findViewById(R.id.supprimer);

            //Lors du clic sur l'icon de suppression
            btn_poubelle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                        //Actualisation de l'affichage
                        actionListener.onClickDeleteButtonItem(items.get(getAdapterPosition()));

                        //Actualisation de l'affichage
                        items.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                }
            });

            //Lors du clic sur la checkbox de l'item
            check_fait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        //Actualisation des données de l'item
                        actionListener.onClickCheckButtonItem(items.get(getAdapterPosition()));
                    }
                }
            });

            //Lors du clic sur l'item
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        //Affichage des données de l'item
                        actionListener.onItemClickedItem(items.get(getAdapterPosition()));
                    }
                }
            });
        }

        public void bind(ItemToDo itemActuel) {
            description.setText(itemActuel.getDescription());
            Boolean fait = false;
            if (itemActuel.getFait() == 1) {
                fait = true;
            }
            check_fait.setChecked(fait);
            btn_poubelle.setImageResource(R.drawable.poubelle);
        }
    }

    public interface ActionListenerItem {
        void onItemClickedItem(ItemToDo itemClicked);
        void onClickDeleteButtonItem(ItemToDo itemToDelete);
        void onClickCheckButtonItem(ItemToDo itemToCheck);
    }
}
