package com.ziggyqubert.android.baking_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziggyqubert.android.baking_app.model.Recepie;
import com.ziggyqubert.android.baking_app.utilities.Utilities;

import java.util.ArrayList;

public class SelectRecepieAdapter extends RecyclerView.Adapter<SelectRecepieAdapter.SelectRecipieAdapterViewHolder> {


    private ArrayList<Recepie> recepieList;
    private final SelectRecepieOnClickHandler onSelectRecepieHandeler;

    /**
     * interface for handeling recepie selection
     */
    public interface SelectRecepieOnClickHandler {
        void onSelectRecepie(Recepie selectedRecepie);
    }

    /**
     * constructor
     * @param selectRecepieHandeler
     */
    SelectRecepieAdapter(SelectRecepieOnClickHandler selectRecepieHandeler) {
        recepieList = new ArrayList<>();
        onSelectRecepieHandeler = selectRecepieHandeler;
    }

    /**
     * handels view creation
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public SelectRecipieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.select_recipe_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new SelectRecipieAdapterViewHolder(view);
    }

    /**
     * binding the view to the model
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull SelectRecipieAdapterViewHolder holder, int position) {

        //gets the recepie object
        Recepie recepie = recepieList.get(position);

        //sets teh text for the recepie
        holder.recipieName.setText(recepie.getName());

        //loads the movie poster
        Utilities.displayrecepieImage(recepie, holder.recipieImage);
    }

    @Override
    public int getItemCount() {
        return recepieList.size();
    }

    /**
     * adds nwe recepies to the data
     * @param newRecipies
     */
    public void addContent(ArrayList<Recepie> newRecipies) {
        Log.i(BakingApp.APP_TAG, "Adding content");
        recepieList.addAll(newRecipies);
        notifyDataSetChanged();
    }

    /**
     * view holder for a recepie
     */
    public class SelectRecipieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipieName;
        ImageView recipieImage;

        /**
         * constructor, inits data display properties
         * @param itemView
         */
        SelectRecipieAdapterViewHolder(View itemView) {
            super(itemView);
            Log.i(BakingApp.APP_TAG, "create view");
            recipieName = itemView.findViewById(R.id.recipe_name);
            recipieImage = itemView.findViewById(R.id.recipe_image);
            itemView.setOnClickListener(this);
        }

        /**
         * handels when the recepie is clicked
         * @param view
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            onSelectRecepieHandeler.onSelectRecepie(recepieList.get(adapterPosition));
        }
    }
}
