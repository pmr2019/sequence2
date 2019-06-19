package com.example.todopmr.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todopmr.Modele.ListeToDo;
import com.example.todopmr.R;

import java.util.ArrayList;

/*
Cette activité gère l'adapter associé aux listes.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<ListeToDo> items;
    private final ActionListenerListe actionListener;
    private ListViewHolder listeEnCours;

    /*
    Constructeur
     */
    public ListAdapter(ArrayList<ListeToDo> item, ActionListenerListe actionListener) {
        this.items = item;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_presentation, parent, false);
        listeEnCours = new ListViewHolder(itemView);
        return listeEnCours;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bind(items.get(position));
    }

    /*
    Calcul du nombre de listes.
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
    public class ListViewHolder extends RecyclerView.ViewHolder {

        private final TextView titre_liste;
        private final ImageView btn_poubelle;

        /*
        Constructeur
         */
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titre_liste = itemView.findViewById(R.id.title);
            this.btn_poubelle = itemView.findViewById(R.id.supprimer);

            //Lors du clic sur l'icon de suppression
            btn_poubelle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                        //Actualisation de l'affichage
                        actionListener.onClickDeleteButtonListe(items.get(getAdapterPosition()));

                        //Actualisation de l'affichage
                        items.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                }
            });

            //Lors du clic sur la liste
            titre_liste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        //Accès aux items de la liste
                        actionListener.onItemClickedListe(items.get(getAdapterPosition()));
                    }
                }
            });
        }

        public void bind(ListeToDo listeActuelle) {
            titre_liste.setText(listeActuelle.getTitreListeToDo());
            btn_poubelle.setImageResource(R.drawable.poubelle);
        }
    }

    public interface ActionListenerListe {
        void onItemClickedListe(ListeToDo listeClicked);
        void onClickDeleteButtonListe(ListeToDo listeToDelete);
    }
}
