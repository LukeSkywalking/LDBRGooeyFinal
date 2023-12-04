package algonquin.cst2335.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.myapplication.databinding.FragmentDefinitionsBinding;
/**
 * WordDetailsFragment displays a list of words and their definitions in a fragment view.
 * It utilizes RecyclerView to present the word and its definitions and offers an option to delete words from favorites.
 */
public class WordDetailsFragment extends Fragment {

    private FragmentDefinitionsBinding binding;
    private TextView wordTextView;
    private WordDefinitionEntity word;
    private ArrayList<WordDefinitionEntity> wordList = new ArrayList<>();
    private String definitions;

    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private WordandDefinitionAdapters adapter;


    /**
     * Constructs a WordDetailsFragment with definitions.
     *
     * @param definitions The definitions associated with the word
     */
    public WordDetailsFragment(String definitions) {
        this.definitions = definitions;
    }
    /**
     * Creates and returns the fragment view hierarchy.
     *
     * @param inflater           The LayoutInflater object used to inflate any views in the fragment
     * @param container          The parent view that the fragment UI should be attached to
     * @param savedInstanceState A Bundle object containing the saved state of the fragment
     * @return The root view of the fragment's layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDefinitionsBinding.inflate(inflater, container, false);

        wordTextView = binding.textView2;

        recyclerView = binding.definitionsfragmentRV;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new WordandDefinitionAdapters(wordList);
        recyclerView.setAdapter(adapter);

        appDatabase = Room.databaseBuilder(requireContext(), AppDatabase.class, "dictionaryDB").build();
        WordDefinitionDao wordDAO = appDatabase.wordDefinitionDao();

        if (wordList.isEmpty()) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                List<WordDefinitionEntity> allWords = wordDAO.getAllWords();
                requireActivity().runOnUiThread(() -> {
                    wordList.addAll(allWords);
                    if (adapter == null) {
                        adapter = new WordandDefinitionAdapters(wordList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                });
            });
        }

        return binding.getRoot();
    }
    /**
     * Adapter class for displaying word definitions in RecyclerView.
     */
    public class WordandDefinitionAdapters extends RecyclerView.Adapter<WordandDefinitionAdapters.ViewHolder> {
        private List<WordDefinitionEntity> wordList;
        /**
         * Constructor for WordandDefinitionAdapters.
         *
         * @param wordDefinitionsList The list of word definitions to be displayed
         */
        public WordandDefinitionAdapters(List<WordDefinitionEntity> wordDefinitionsList) {
            this.wordList = wordDefinitionsList;
        }
        /**
         * Creates a new ViewHolder object by inflating the layout for an item view.
         *
         * @param parent   The ViewGroup into which the new View will be added
         * @param viewType The type of the new View
         * @return A new ViewHolder that holds a View of the given view type
         */
        @NonNull
        @Override
        public WordandDefinitionAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meaning, parent, false);
            return new WordandDefinitionAdapters.ViewHolder(view);
        }
        /**
         * Binds the data to the views within the ViewHolder.
         *
         * @param holder   The ViewHolder to bind data to
         * @param position The position of the item within the adapter's data set
         */
        @Override
        public void onBindViewHolder(@NonNull WordandDefinitionAdapters.ViewHolder holder, int position) {
            wordTextView.setText(wordList.get(position).getWord());
            String definition = wordList.get(position).getDefinitions();

            appDatabase = Room.databaseBuilder(requireContext(), AppDatabase.class, "dictionaryDB").build();
            WordDefinitionDao wordDAO = appDatabase.wordDefinitionDao();

            androidx.appcompat.widget.Toolbar toolbar = holder.definitionsMenu;
            toolbar.inflateMenu(R.menu.fragmentrecycle);

            toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setMessage("Do you want to delete this word from your Favourites?")
                                .setTitle("Delete")
                                .setNegativeButton("No", (dialog, which) -> {
                                })
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    WordDefinitionEntity wordToDelete = wordList.get(position);
                                    wordList.remove(wordToDelete);

                                    if (wordToDelete != null) {
                                        adapter.notifyItemRemoved(position);
                                        Executor thread1 = Executors.newSingleThreadExecutor();
                                        thread1.execute(() -> {
                                            wordDAO.deleteWordDefinition(wordToDelete);
                                        });
                                        Log.d("WordToDelete", "Word to Delete: " + wordToDelete.toString());

                                        // Show a Snackbar anchored to the root view of the activity or fragment
                                        Snackbar.make(requireView(), "Word Deleted from Favourites", Snackbar.LENGTH_LONG)
                                                .setAction("Undo", (btn) -> {
                                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                                    thread2.execute(() -> {
                                                        wordDAO.insertWordDefinition(wordToDelete);
                                                    });

                                                    wordList.add(position, wordToDelete);
                                                    adapter.notifyItemInserted(position);
                                                })
                                                .show();
                                    }
                                });
                        builder.create().show();
                        break;
                }
                return false;
            });
            holder.bind(definition);
        }
        /**
         * Gets the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in the data set
         */
        public int getItemCount() {
            return wordList.size();
        }
        /**
         * ViewHolder class for the RecyclerView item.
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView definitionTextView;

            androidx.appcompat.widget.Toolbar definitionsMenu;

            public ViewHolder(View itemView) {
                super(itemView);
                definitionTextView = itemView.findViewById(R.id.meaning_text_view);
                definitionsMenu = itemView.findViewById(R.id.definitionsTB);
            }
            /**
             * Binds a definition to the ViewHolder's TextView.
             *
             * @param definition The definition text to bind
             */
            public void bind(String definition) {
                definitionTextView.setText(definition);

            }
        }
    }

}


