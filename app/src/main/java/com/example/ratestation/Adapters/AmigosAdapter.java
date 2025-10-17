package com.example.ratestation.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.ViewHolder> {

    private List<String> friendsList;

    public AmigosAdapter(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public AmigosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmigosAdapter.ViewHolder holder, int position) {
        holder.textView.setText(friendsList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(android.R.id.text1);
        }
    }
}
