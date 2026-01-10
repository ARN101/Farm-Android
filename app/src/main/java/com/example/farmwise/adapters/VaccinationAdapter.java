package com.example.farmwise.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmwise.R;
import com.example.farmwise.models.Vaccination;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VaccinationAdapter extends RecyclerView.Adapter<VaccinationAdapter.ViewHolder> {

    private List<Vaccination> vaccinationList;
    private OnItemClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

    public interface OnItemClickListener {
        void onItemClick(Vaccination vaccination);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public VaccinationAdapter(List<Vaccination> vaccinationList) {
        this.vaccinationList = vaccinationList;
    }

    public void updateList(List<Vaccination> newList) {
        vaccinationList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vaccination, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vaccination vaccination = vaccinationList.get(position);
        holder.textRecordId.setText(String.valueOf(position + 1));
        holder.textAnimalId.setText(vaccination.getAnimalId());
        holder.textVaccine.setText(vaccination.getVaccineName());

        if (vaccination.getDateGiven() != null) {
            holder.textDateGiven.setText(dateFormat.format(vaccination.getDateGiven().toDate()));
        }

        if (vaccination.getNextDueDate() != null) {
            holder.textNextDue.setText(dateFormat.format(vaccination.getNextDueDate().toDate()));
        }

        holder.textStatus.setText(vaccination.getStatus());

        if ("Overdue".equals(vaccination.getStatus())) {
            holder.textStatus.setTextColor(Color.parseColor("#D32F2F"));
        } else if ("Due Soon".equals(vaccination.getStatus())) {
            holder.textStatus.setTextColor(Color.parseColor("#F57C00"));
        } else {
            holder.textStatus.setTextColor(Color.parseColor("#388E3C"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(vaccination);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vaccinationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textRecordId, textAnimalId, textVaccine, textDateGiven, textNextDue, textStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            textRecordId = itemView.findViewById(R.id.textRecordId);
            textAnimalId = itemView.findViewById(R.id.textAnimalId);
            textVaccine = itemView.findViewById(R.id.textVaccine);
            textDateGiven = itemView.findViewById(R.id.textDateGiven);
            textNextDue = itemView.findViewById(R.id.textNextDue);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
    }
}
