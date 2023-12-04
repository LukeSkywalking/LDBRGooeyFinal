package algonquin.cst2335.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.myapplication.databinding.SavedpageBinding;
import algonquin.cst2335.myapplication.databinding.WordBinding;
/**
 * The SavedPage class represents the activity displaying the list of saved words/definitions.
 * It manages the display of saved words, their definitions, and provides functionality
 * to interact with these saved items.
 */
public class SavedPage extends AppCompatActivity {

    private SavedpageBinding binding;
    private ArrayList<WordDefinitionEntity> wordTermList = new ArrayList<>();

    private AppDatabase appDatabase;
    private WordViewModel wordModel;

    private WordAdapter wordAdapter;
    private RecyclerView recyclerView;
    androidx.appcompat.widget.Toolbar toolbar;
    /**
     * Called when the activity is starting. Sets up the UI, toolbar, database, and adapters.
     *
     * @param savedInstanceState A Bundle containing the data most recently supplied in onSaveInstanceState(Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SavedpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.savePageToolBar);
        setSupportActionBar(toolbar);
        wordModel = new ViewModelProvider(this).get(WordViewModel.class);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dictionaryDB").build();
        WordDefinitionDao wordDAO = appDatabase.wordDefinitionDao();

        if (wordTermList.isEmpty()) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                List<WordDefinitionEntity> allWords = wordDAO.getAllWords();
                runOnUiThread(() -> {
                    wordTermList.addAll(allWords);
                    wordModel.term.postValue((ArrayList<WordDefinitionEntity>) allWords);
                    if (wordAdapter == null) {
                        wordAdapter = new WordAdapter(wordTermList);
                        recyclerView.setAdapter(wordAdapter);
                    } else {
                        wordAdapter.notifyDataSetChanged();
                    }
                });
            });
        }



        recyclerView = binding.wordrecycle;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordAdapter = new WordAdapter(wordTermList);
        recyclerView.setAdapter(wordAdapter);
    }
    /**
     * Custom adapter for displaying saved words in a RecyclerView.
     */
    class WordAdapter extends RecyclerView.Adapter<WordViewHolder> {

        private List<WordDefinitionEntity> wordTermList;

        public WordAdapter(List<WordDefinitionEntity> wordTermList) {
            this.wordTermList = wordTermList;
        }
        /**
         * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position
         * @param viewType The view type of the new View
         * @return A new ViewHolder that holds a View of the given view type
         */
        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            WordBinding wordBinding = WordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);


            return new WordViewHolder(wordBinding.getRoot());
        }
        /**
         * Called by RecyclerView to display the data at the specified position.
         *
         * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position
         * @param position The position of the item within the adapter's data set
         */
        public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
            WordDefinitionEntity wordEntity = wordTermList.get(position);

            if (wordEntity != null) {
                String term = wordEntity.getWord();
                holder.bind(term);

                androidx.appcompat.widget.Toolbar toolbar = holder.wordInfo;

                toolbar.inflateMenu(R.menu.savedpageword);
                toolbar.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.fragmentButton) {
                        try {


                            // Create the WordDetailsFragment with the selected term and position
                            WordDetailsFragment wordDetailFragment = new WordDetailsFragment(wordEntity.getDefinitions());
                            // Perform fragment transaction
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.addToBackStack("yerr");
                            transaction.replace(R.id.fragement, wordDetailFragment); // Check your fragment ID
                            transaction.commit();
                        } catch (Exception e) {
                            Log.e("FragmentTransaction", "Error during fragment transaction: " + e.getMessage());
                            e.printStackTrace();
                        }
                        return true; // Return true to indicate the event was handled
                    }
                    return false; // Return false if the event was not handled
                });
            } else {
                Log.e("WordAdapter", "WordEntity at position " + position + " is null");
            }
        }
        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter
         */
        @Override
        public int getItemCount() {
            return wordTermList.size();
        }
    }
    /**
     * ViewHolder for the RecyclerView items displaying saved words.
     */
    class WordViewHolder extends RecyclerView.ViewHolder {
        TextView termTextView;

        Toolbar wordInfo;

        public WordViewHolder(View itemView) {
            super(itemView);
            termTextView = itemView.findViewById(R.id.wordInRecycle);

            wordInfo = itemView.findViewById(R.id.details);
        }
        /**
         * Binds the word to the ViewHolder item.
         *
         * @param word The word to be bound/displayed
         */
        public void bind(String word) {
            termTextView.setText(word);
        }
    }
    /**
     * Initializes the options menu.
     *
     * @param menu The options menu in which items are placed
     * @return true for the menu to be displayed; if you return false, it will not be shown
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handles options menu item selections.
     *
     * @param item The menu item that was selected
     * @return true if the selection was handled; otherwise, false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.help) {
            // Show help information in a dialog
            showHelpInformation();
            return true;
        }

        return false;
    }
    /**
     * Shows the help information in an AlertDialog when the help menu item is selected.
     */
    private void showHelpInformation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help Information");

        // Display help information in a dialog
        String helpText = "Enter a word into the search bar. Then press search. A list of definitions " +
                "will appear below the search bar. To save a definition, press the icon to the right of the definition. A message " +
                "will show up at the bootom of your screen if you want to undo, click undo if you would like to undo the save." +
                " If you want to view your saved words, click the button at the bottom of the page that says View Saved Terms." +
                " It will bring you to a new page that shows all of the words you saved. You can then scroll through your words and " +
                "click the 'i' icon to the right of the word to view the definitions of all of the words. When in the view page, you" +
                " can click on the delete icon on the right of the definition if you want to delete the definition. To go back press " +
                "the back button that is apart of your device. ";
        builder.setMessage(helpText);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
