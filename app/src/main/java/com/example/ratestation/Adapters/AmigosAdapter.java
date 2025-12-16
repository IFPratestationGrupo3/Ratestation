package com.example.ratestation.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratestation.R;

import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.ViewHolder> {

    private final List<String> friendsList;

    public AmigosAdapter(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public AmigosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_amigo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmigosAdapter.ViewHolder holder, int position) {
        String friendName = friendsList.get(position);
        if (friendName != null && !friendName.isEmpty()) {
            holder.nombre.setText(friendName);
            // Coge la primera letra, la convierte a mayúsculas y la pone en el círculo
            holder.inicial.setText(String.valueOf(friendName.charAt(0)).toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView inicial;
        TextView nombre;

        public ViewHolder(View view) {
            super(view);
            inicial = view.findViewById(R.id.amigo_initial);
            nombre = view.findViewById(R.id.amigo_nombre);
        }
    }
}
