package com.example.farmwise.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmwise.R;
import com.example.farmwise.models.Livestock;
import java.util.List;

public class LivestockAdapter extends RecyclerView.Adapter<LivestockAdapter.ViewHolder> {

    private List<Livestock> livestockList;
    private OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(Livestock livestock, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LivestockAdapter(List<Livestock> livestockList) {
        this.livestockList = livestockList;
    }

    public void updateList(List<Livestock> newList) {
        livestockList = newList;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_livestock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Livestock livestock = livestockList.get(position);
        holder.textId.setText(String.valueOf(position + 1));
        holder.textType.setText(livestock.getType());
        holder.textName.setText(livestock.getName());
        holder.textBreed.setText(livestock.getBreed());
        holder.textAge.setText(String.valueOf(livestock.getAge()));
        holder.textHealth.setText(livestock.getHealth());

        if (holder.itemView instanceof com.google.android.material.card.MaterialCardView) {
            com.google.android.material.card.MaterialCardView cardView = (com.google.android.material.card.MaterialCardView) holder.itemView;
            if (selectedPosition == position) {
                cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
            } else {
                cardView.setCardBackgroundColor(android.graphics.Color.WHITE);
            }
        } else {
            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
            } else {
                holder.itemView.setBackgroundColor(android.graphics.Color.WHITE);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(livestock, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return livestockList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textId, textType, textName, textBreed, textAge, textHealth;

        public ViewHolder(View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.textId);
            textType = itemView.findViewById(R.id.textType);
            textName = itemView.findViewById(R.id.textName);
            textBreed = itemView.findViewById(R.id.textBreed);
            textAge = itemView.findViewById(R.id.textAge);
            textHealth = itemView.findViewById(R.id.textHealth);
        }
    }
}
