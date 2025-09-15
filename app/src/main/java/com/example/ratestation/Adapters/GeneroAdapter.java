package com.example.ratestation.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratestation.R;

import java.util.List;

public class GeneroAdapter extends RecyclerView.Adapter<GeneroAdapter.GeneroViewHolder> {

    private List<String> generos;
    private OnGeneroClickListener listener;

    public interface OnGeneroClickListener {
        void onGeneroClick(String genero);
    }

    public GeneroAdapter(List<String> generos, OnGeneroClickListener listener) {
        this.generos = generos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GeneroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genero, parent, false);
        return new GeneroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneroViewHolder holder, int position) {
        String genero = generos.get(position);
        holder.btnGenero.setText(genero);
        holder.btnGenero.setOnClickListener(v -> listener.onGeneroClick(genero));
    }

    @Override
    public int getItemCount() {
        return generos.size();
    }

    public static class GeneroViewHolder extends RecyclerView.ViewHolder {
        Button btnGenero;

        public GeneroViewHolder(@NonNull View itemView) {
            super(itemView);
            btnGenero = itemView.findViewById(R.id.btnGenero);
        }
    }
}
