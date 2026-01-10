package com.example.farmwise.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmwise.R;
import com.example.farmwise.models.Finance;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.ViewHolder> {

    private List<Finance> financeList;
    private OnItemClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

    public interface OnItemClickListener {
        void onItemClick(Finance finance);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FinanceAdapter(List<Finance> financeList) {
        this.financeList = financeList;
    }

    public void updateList(List<Finance> newList) {
        financeList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Finance finance = financeList.get(position);
        holder.textType.setText(finance.getType());
        holder.textCategory.setText(finance.getCategory());
        holder.textAmount.setText(String.valueOf(finance.getAmount()));

        if (finance.getDate() != null) {
            holder.textDate.setText(dateFormat.format(finance.getDate().toDate()));
        }

        holder.textAnimalId.setText(finance.getAnimalId());

        if ("Income".equals(finance.getType())) {
            holder.textType.setTextColor(Color.parseColor("#388E3C"));
        } else {
            holder.textType.setTextColor(Color.parseColor("#D32F2F"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(finance);
            }
        });
    }

    @Override
    public int getItemCount() {
        return financeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textType, textCategory, textAmount, textDate, textAnimalId;

        public ViewHolder(View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.textType);
            textCategory = itemView.findViewById(R.id.textCategory);
            textAmount = itemView.findViewById(R.id.textAmount);
            textDate = itemView.findViewById(R.id.textDate);
            textAnimalId = itemView.findViewById(R.id.textAnimalId);
        }
    }
}
