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

    public interface OnItemClickListener {
        void onItemClick(Livestock livestock);
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

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(livestock);
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
