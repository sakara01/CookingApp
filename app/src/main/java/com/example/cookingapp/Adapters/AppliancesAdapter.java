package com.example.cookingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingapp.Models.Equipment;
import com.example.cookingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AppliancesAdapter extends RecyclerView.Adapter<AppliancesViewHolder>{
    Context context;
    List<Equipment> list;

    public AppliancesAdapter(Context context, List<Equipment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AppliancesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppliancesViewHolder(LayoutInflater.from(context).inflate(R.layout.list_recipe_appliances, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppliancesViewHolder holder, int position) {
        holder.appliance_name.setText(list.get(position).name);
        holder.appliance_name.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/equipment_100x100/" + list.get(position).image).into(holder.appliance_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class AppliancesViewHolder extends RecyclerView.ViewHolder {
    TextView appliance_name;
    ImageView appliance_image;
    public AppliancesViewHolder(@NonNull View itemView) {
        super(itemView);
        appliance_name = itemView.findViewById(R.id.appliance_name);
        appliance_image = itemView.findViewById(R.id.appliance_image);
    }
}