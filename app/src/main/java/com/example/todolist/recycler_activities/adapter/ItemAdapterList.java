package com.example.todolist.recycler_activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.modele.ListeToDo;

import java.util.List;

/**
 * Définition de la classe ItemAdapterList.
 * Cette classe représente l'adapteur associé au Recycler View de l'activité ChoixList Activity
 */
public class ItemAdapterList extends RecyclerView.Adapter<ItemAdapterList.ItemViewHolder> {

    /* La liste des ToDoLists à afficher */
    private List<ListeToDo> lesListes;
    /* Écouteur d'évènement */
    private onClickListListener onClickListListener;

    /**
     * Constructeur par paramètres
     *
     * @param lesListes           la liste des ToDoLists
     * @param onClickListListener l'écouteur d'évènement associé au contexte
     */
    public ItemAdapterList(List<ListeToDo> lesListes, onClickListListener onClickListListener) {
        this.lesListes = lesListes;
        this.onClickListListener = onClickListListener;
    }

    /**
     * Permet de créer la vue associée à l'item
     *
     * @param parent   le groupe de vues auquel appartiendra la nouvelle vue une fois liée
     * @param viewType le type de vue de la nouvelle vue
     * @return la nouvelle vue
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.liste, parent, false));
    }

    /**
     * Permet d'associer les données de l'item à la vue, et de l'afficher à une position
     *
     * @param holder   la vue associée
     * @param position où afficher la vue
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(lesListes.get(position));
    }

    /**
     * Définit le nombre total d'éléments que l'on souhaite afficher
     *
     * @return le nombre d'éléments à afficher
     */
    @Override
    public int getItemCount() {
        return lesListes.size();
    }

    /**
     * Définition de la classe ItemviewHolder.
     * Cette classe représente un item qui doit être lié à une vue par l'adapteur
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* Le titre de la ToDoList (l'élément à afficher dans le Recycler View) */
        public TextView titreListe;

        /**
         * Constructeur par paramètre
         *
         * @param itemView la vue associée au contexte
         */
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titreListe = itemView.findViewById(R.id.listTitle);
            titreListe.setOnClickListener(this);
        }

        /**
         * Permet de lier les données de l'item à sa vue
         *
         * @param listeToDo Ici, on ajoute le titre de la ToDoList
         */
        public void bind(ListeToDo listeToDo) {
            titreListe.setText(listeToDo.getTitreListeToDo());
        }

        /**
         * Fonction par défaut de l'interface View.OnClickListener, appelée lors du clic sur la vue
         *
         * @param v la vue cliquée
         *          Ici, lors du clic sur une ToDoList, on ouvre l'activité ShowList Activity
         */
        @Override
        public void onClick(View v) {
            onClickListListener.clickList(getAdapterPosition());
        }

    }

    /**
     * Interface permettant de gérer le clic sur un élément de la liste des ToDoLists
     * (Pas d'accès direct au context this depuis cette classe, il faut le passer en paramètre)
     */
    public interface onClickListListener {
        void clickList(int position);
    }
}
