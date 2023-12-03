package algonquin.cst2335.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.myapplication.databinding.SavedpageBinding;

public class SavedPage extends AppCompatActivity {

    private SavedpageBinding binding;
    private List<WordDefinitionEntity> wordTermList;

    private AppDatabase appDatabase;

    private WordAdapter wordAdapter;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SavedpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"dictionaryDB").build();
        WordDefinitionDao wordDAO = appDatabase.wordDefinitionDao();

        if(wordTermList.isEmpty()){
         Executor thread = Executors.newSingleThreadExecutor();
         thread.execute(()->{
         List<WordDefinitionEntity> allWords = wordDAO.getAllWords();
         runOnUiThread(()->{
             if(wordAdapter == null){
                 wordAdapter = new WordAdapter(wordTermList);
                 recyclerView.setAdapter(wordAdapter);
             }
             else {
                 wordAdapter.notifyDataSetChanged();
             }
         });
         });
        }
        recyclerView = binding.wordrecycle;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wordAdapter);
    }

            class WordViewHolder extends RecyclerView.ViewHolder {
                TextView termTextView;

                public WordViewHolder(View itemView) {
                    super(itemView);
                    termTextView = itemView.findViewById(R.id.wordInRecycle);
                }

                public void bind(String word) {
                    termTextView.setText(word);
                }

            }

    class WordAdapter extends RecyclerView.Adapter<WordViewHolder> {

        private List<WordDefinitionEntity> wordTermList;


        public WordAdapter(List<WordDefinitionEntity> wordTermList) {
            this.wordTermList = wordTermList;
        }

        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word, parent, false);
            return new WordViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
            String term = wordTermList.get(position).getWord();
            holder.bind(term);
        }
        @Override
        public int getItemCount() {
            return wordTermList.size();
        }
    }
//
//    appDatabase = Room.databaseBuilder(SavedPage.this,AppDatabase.class,"dictionaryDB").build();
//    WordDefinitionDao wordDAO = appDatabase.wordDefinitionDao();
//    androidx.appcompat.widget.Toolbar toolbar = holder.definitionsMenu;
//            toolbar.inflateMenu(R.menu.definitions_menu);
//
//            toolbar.setOnMenuItemClickListener(item ->{
//        switch (item.getItemId()){
//            case R.id.saveDef:
//                AlertDialog.Builder builder = new AlertDialog.Builder(SavedPage.this);
//                builder.setMessage("Do you want to add this Definition to your Favourites?")
//                        .setTitle("Add")
//                        .setNegativeButton("No", (dialog, which) -> {
//                        })
//                        .setPositiveButton("Yes", (dialog, which) -> {
//
//                            WordDefinitionEntity definitionToAdd = wordDefinitionsList.get(position);
//                            favsList.add(definitionToAdd);
//
//                            if (definitionToAdd != null) {
//                                adapter.notifyItemChanged(position);
//                                Executor thread1 = Executors.newSingleThreadExecutor();
//                                thread1.execute(() -> {
//                                    try {
//                                        // Ensure that the insertSong method is correctly implemented
//                                        long result = wordDAO.insertWordDefinition(definitionToAdd);
//                                        Log.d("InsertResult", "Rows affected: " + result);
//                                    } catch (Exception e) {
//                                        Log.e("InsertError", "Error inserting song", e);
//                                    }
//                                });
//
//                                // Log to check if the song is being added to the list
//                                Log.d("definition", "definition to add: " + definitionToAdd.toString());
//
//                                Snackbar.make(findViewById(android.R.id.content), "defintion added to favourites", Snackbar.LENGTH_LONG)
//                                        .setAction("Undo", (btn) -> {
//                                            Executor thread2 = Executors.newSingleThreadExecutor();
//                                            thread2.execute(() -> {
//                                                // undo the addition from the database
//                                                wordDAO.deleteWordDefinition(definitionToAdd);
//                                            });
//
//                                            wordDefinitionsList.remove(definitionToAdd);
//                                            adapter.notifyItemChanged(position);
//                                        })
//                                        .show();
//                            }
//                        });
//                builder.create().show();
//        }
//        return false;
//    });







        //    @Override
//    public void onItemClick(String selectedDefinition) {
//        DefinitionsFragment definitionsFragment = new DefinitionsFragment();
//        Bundle args = new Bundle();
//        args.putString("selectedDefinition", selectedDefinition);
//        definitionsFragment.setArguments(args);
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.definitionsRV, definitionsFragment)
//                .addToBackStack(null)
//                .commit();
//    }


}
