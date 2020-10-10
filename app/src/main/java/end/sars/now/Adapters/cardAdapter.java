package end.sars.now.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import end.sars.now.Helpers.cardHelper;
import end.sars.now.R;

public class cardAdapter extends RecyclerView.Adapter<cardAdapter.MyViewHolder> {
    Context context;
    private List<end.sars.now.Helpers.cardHelper> cardHelper;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new MyViewHolder(view);
    }
    public cardAdapter(List<cardHelper> articles, Context context) {
        this.cardHelper = articles;
        this.context = context;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, final int position) {
        final MyViewHolder holder = holders;
        final cardHelper model = cardHelper.get(position);
        holder.title.setText(model.getTitle());
        holder.icon.setImageResource(model.getIcon());
    }

    @Override
    public int getItemCount() {
        return cardHelper.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public MyViewHolder(View v ) {
            super(v);
            title=v.findViewById(R.id.title);
            icon=v.findViewById(R.id.icon);
        }
    }
}
