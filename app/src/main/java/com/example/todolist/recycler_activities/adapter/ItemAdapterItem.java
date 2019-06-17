package com.example.todolist.recycler_activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.modele.ItemToDo;

import java.util.List;

/** Définition de la classe ItemAdapterItem.
 * Cette classe représente l'adapteur associé au Recycler View de l'activité ShowList Activity
 */
public class ItemAdapterItem extends RecyclerView.Adapter<ItemAdapterItem.ItemViewHolder>{

    /* La liste des items à afficher */
    private List<ItemToDo> lesItems;
    /* Écouteur d'évènement */
    private onClickItemListener onClickItemListener;

    /** Constructeur par paramètres
     * @param lesItems la liste des items
     */
    public ItemAdapterItem(List<ItemToDo> lesItems, onClickItemListener onClickItemListener) {
        this.lesItems = lesItems;
        this.onClickItemListener = onClickItemListener;
    }

    /** Permet de créer la vue associée à l'item
     * @param parent le groupe de vues auquel appartiendra la nouvelle vue une fois liée
     * @param viewType le type de vue de la nouvelle vue
     * @return la nouvelle vue
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item,parent,false));
    }

    /** Permet d'associer les données de l'item à la vue, et de l'afficher à une position
     * @param holder la vue associée
     * @param position où afficher la vue
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(lesItems.get(position));
    }

    /** Définit le nombre total d'éléments que l'on souhaite afficher
     * @return le nombre d'éléments à afficher
     */
    @Override
    public int getItemCount() {
        return lesItems.size();
    }

    /** Définition de la classe ItemviewHolder.
     * Cette classe représente un item qui doit être lié à une vue par l'adapteur
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        /* La CheckBox associée à l'item */
        public CheckBox description;

        /** Constructeur par paramètre
         * @param itemView la vue associée au contexte
         */
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.checkBox);
            description.setOnClickListener(this);
        }

        /** Permet de lier les données de l'item à sa vue
         * @param item
         * Ici, on ajoute la description de l'item et on met à jour la CheckBox
         */
        public void bind(ItemToDo item) {
            description.setText(item.getDescription());
            description.setChecked(item.isFait());
        }

        /** Fonction par défaut de l'interface View.OnClickListener, appelée lors du clic sur la vue
         * @param v la vue cliquée
         * Ici, lors du clic sur un item, on met à jour la CheckBox en la cochant/décochant et on modifie
         *          le paramètre fait de l'item associé
         */
        @Override
        public void onClick(View v) {
            onClickItemListener.clickItem(getAdapterPosition());
        }

    }

    /**
     * Interface permettant de gérer le clic sur un élément de la liste des item
     * (Pas d'accès direct au context this depuis cette classe, il faut le passer en paramètre)
     */
    public interface onClickItemListener{
        void clickItem(int position);
    }
}
