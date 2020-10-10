package end.sars.now.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import end.sars.now.Helpers.politiciansHelper;
import end.sars.now.R;

public class politiciansAdapter extends RecyclerView.Adapter<politiciansAdapter.MyViewHolder> implements Filterable {
    Context context;
    private List<end.sars.now.Helpers.politiciansHelper> politiciansHelper;
    public ArrayList<politiciansHelper> arrayListFiltered;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.politicians_item, parent, false);
        return new MyViewHolder(view);
    }
    public politiciansAdapter(List<politiciansHelper> articles, Context context) {
        this.politiciansHelper = articles;
        this.context = context;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, final int position) {
        final MyViewHolder holder = holders;
        final politiciansHelper model = politiciansHelper.get(position);
        holder.email.setText(model.getEmail());
        holder.email.setTextSize(12f);
        holder.location.setTextSize(16f);
        holder.phone.setText(model.getPhoneNo());
        holder.location.setText(model.getState());
        holder.name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return politiciansHelper.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView location,email,name,phone;
        public MyViewHolder(View v ) {
            super(v);
            location=v.findViewById(R.id.location);
            email=v.findViewById(R.id.email);
            name=v.findViewById(R.id.name);
            phone=v.findViewById(R.id.phone);
        }
    }



    public Filter getFilter()
    {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<politiciansHelper> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (politiciansHelper item : arrayListFiltered) {
                    if (item.getName().toLowerCase().contains(filterPattern)||item.getState().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            politiciansHelper.clear();
            politiciansHelper.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
