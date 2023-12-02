package algonquin.cst2335.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<word> searchTerms;
    private Context context;

    public Adapter(List<word> searchTerms) {
        this.searchTerms = searchTerms;
    }

    public void setData(List<word> searchTerms) {
        this.searchTerms = searchTerms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_dictionary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        word searchTerm = searchTerms.get(position);
        holder.bind(searchTerm);
    }

    @Override
    public int getItemCount() {
        return searchTerms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView termTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            termTextView = itemView.findViewById(R.id.recycler_view);
        }

        public void bind(word word) {
            termTextView.setText(word.getTerm());
            itemView.setOnClickListener(v -> {
                // Handle item click if needed
            });
        }
    }
}
