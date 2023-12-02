package algonquin.cst2335.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.myapplication.databinding.FragmentDefinitionsBinding;

public class DefinitionsFragment extends Fragment {

    FragmentDefinitionsBinding binding;

    private RecyclerView definitionsRecyclerView;
    private Adapter definitionsAdapter;
    private MyDatabase myDatabase;
    private String selectedWord;

    private SearchTermDao stDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_definitions, container, false);

//        definitionsRecyclerView = view.findViewById(R.id.definitions_recycler_view);

        definitionsRecyclerView = binding.definitionsRV;
        // Initialize other views and components

        myDatabase = Room.databaseBuilder(requireContext(),
                        MyDatabase.class, "my_database")
                .build();

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedWord = bundle.getString("selectedWord", "");
            if (!selectedWord.isEmpty()) {
//                showDefinitions(selectedWord);
            }
        }

        // Setup RecyclerView and its adapter for displaying definitions
        definitionsAdapter = (Adapter) new DefinitionsAdapter(new ArrayList<word>()); // Initialize with an empty list
        definitionsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        definitionsRecyclerView.setAdapter((RecyclerView.Adapter) definitionsAdapter);

        return view;
    }

//    private void showDefinitions(String selectedWord) {
//        myDatabase.searchTermDao().getDefinitionsForWord(selectedWord).observe(getViewLifecycleOwner(), definitions -> {
//            definitionsAdapter.equals(definitions); // Update RecyclerView with definitions
//        });
//    }

    private void deleteDefinition(word word) {
            stDao.delete(word);
            myDatabase.searchTermDao().delete(word);
        };
    }

    // Other necessary methods

    // Inner Adapter class for RecyclerView
    class DefinitionsAdapter extends RecyclerView.Adapter<DefinitionsAdapter.ViewHolder> {

        private List<word> wordsList;

        public DefinitionsAdapter(List<word> wordsList) {
            this.wordsList = wordsList;
        }

        // ViewHolder class and its methods

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate your item layout and return a new ViewHolder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_definitions, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // Bind data to your ViewHolder components based on position
            word definition = wordsList.get(position);
            // Set data to your ViewHolder components
        }

        @Override
        public int getItemCount() {
            return wordsList.size();
        }

        // ViewHolder class for RecyclerView items
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // Define your ViewHolder components

            public ViewHolder(View itemView) {
                super(itemView);
                // Initialize your ViewHolder components
            }
        }
    }
