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

public class WordDetailsFragment extends Fragment {

    private FragmentDefinitionsBinding binding;
    private TextView wordTextView;
    private WordDefinitionEntity word;
    private ArrayList<WordDefinitionEntity> wordList = new ArrayList<>();
    private String definitions;

    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private WordandDefinitionAdapters adapter;



    public WordDetailsFragment(String definitions) {
        this.definitions = definitions;
    }
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

    public class WordandDefinitionAdapters extends RecyclerView.Adapter<WordandDefinitionAdapters.ViewHolder> {
        private List<WordDefinitionEntity> wordList;

        public WordandDefinitionAdapters(List<WordDefinitionEntity> wordDefinitionsList) {
            this.wordList = wordDefinitionsList;
        }

        @NonNull
        @Override
        public WordandDefinitionAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meaning, parent, false);
            return new WordandDefinitionAdapters.ViewHolder(view);
        }

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

        public int getItemCount() {
            return wordList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView definitionTextView;

            androidx.appcompat.widget.Toolbar definitionsMenu;

            public ViewHolder(View itemView) {
                super(itemView);
                definitionTextView = itemView.findViewById(R.id.meaning_text_view);
                definitionsMenu = itemView.findViewById(R.id.definitionsTB);
            }

            public void bind(String definition) {
                definitionTextView.setText(definition);

            }
        }
    }

}


