package algonquin.cst2335.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DictionaryActivity extends AppCompatActivity{

    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private Button searchButton;
    private Button viewSavedButton;

    DefinitionsViewModel viewModel;

    private ArrayList<WordDefinitionEntity> wordDefinitionsList = new ArrayList<>();

    private ArrayList<WordDefinitionEntity> favsList = new ArrayList<>();
    private WordDefinitionAdapter adapter;

    private WordDefinitionDao wordDAO;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dictionary);

        queue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);
        viewSavedButton = findViewById(R.id.view_saved_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WordDefinitionAdapter(wordDefinitionsList);
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {
            String searchTerm = searchEditText.getText().toString().trim();
            if (!searchTerm.isEmpty()) {
                String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + searchTerm;

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                        response -> handleResponse(response, searchTerm),
                        error -> Toast.makeText(DictionaryActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

                queue.add(jsonArrayRequest);
            } else {
                Toast.makeText(this, "Enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

        viewSavedButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SavedPage.class);
            startActivity(intent);
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.help) {
            // Show help information in a dialog
            showHelpInformation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHelpInformation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help Information");

        // Display help information in a dialog
        String helpText = "Your help information goes here...";
        builder.setMessage(helpText);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void handleResponse(JSONArray response, String searchTerm) {
        try {
            wordDefinitionsList.clear(); // Clear the existing list

            for (int i = 0; i < response.length(); i++) {
                JSONObject wordObject = response.getJSONObject(i);
                JSONArray meanings = wordObject.getJSONArray("meanings");

                StringBuilder definitionsString = new StringBuilder();

                for (int j = 0; j < meanings.length(); j++) {
                    JSONObject meaning = meanings.getJSONObject(j);
                    JSONArray definitionsArray = meaning.getJSONArray("definitions");

                    for (int k = 0; k < definitionsArray.length(); k++) {
                        JSONObject definitionObject = definitionsArray.getJSONObject(k);
                        String definition = definitionObject.getString("definition");
                        definitionsString.append(definition).append("\n");
                    }
                }
                wordDefinitionsList.add(new WordDefinitionEntity(searchTerm, definitionsString.toString()));
            }

            // Update RecyclerView
            adapter.notifyDataSetChanged(); // Notify adapter of the data change

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(DictionaryActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    public class WordDefinitionAdapter extends RecyclerView.Adapter<WordDefinitionAdapter.ViewHolder> {

        private List<WordDefinitionEntity> wordDefinitionsList;


        public WordDefinitionAdapter(List<WordDefinitionEntity> wordDefinitionsList) {
            this.wordDefinitionsList = wordDefinitionsList;
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

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meaning, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String definition = wordDefinitionsList.get(position).getDefinitions();
            holder.bind(definition);

            appDatabase = Room.databaseBuilder(DictionaryActivity.this,AppDatabase.class,"dictionaryDB").build();
            WordDefinitionDao wordDAO = appDatabase.wordDefinitionDao();
            androidx.appcompat.widget.Toolbar toolbar = holder.definitionsMenu;
            toolbar.inflateMenu(R.menu.definitions_menu);

            toolbar.setOnMenuItemClickListener(item ->{
                switch (item.getItemId()){
                    case R.id.saveDef:
                        AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryActivity.this);
                        builder.setMessage("Do you want to add this Definition to your Favourites?")
                                .setTitle("Add")
                                .setNegativeButton("No", (dialog, which) -> {
                                })
                                .setPositiveButton("Yes", (dialog, which) -> {

                                    WordDefinitionEntity definitionToAdd = wordDefinitionsList.get(position);
                                    favsList.add(definitionToAdd);

                                    if (definitionToAdd != null) {
                                        adapter.notifyItemChanged(position);
                                        Executor thread1 = Executors.newSingleThreadExecutor();
                                        thread1.execute(() -> {
                                            try {
                                                // Ensure that the insertSong method is correctly implemented
                                                long result = wordDAO.insertWordDefinition(definitionToAdd);
                                                Log.d("InsertResult", "Rows affected: " + result);
                                            } catch (Exception e) {
                                                Log.e("InsertError", "Error inserting song", e);
                                            }
                                        });

                                        // Log to check if the song is being added to the list
                                        Log.d("definition", "definition to add: " + definitionToAdd.toString());

                                        Snackbar.make(findViewById(android.R.id.content), "defintion added to favourites", Snackbar.LENGTH_LONG)
                                                .setAction("Undo", (btn) -> {
                                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                                    thread2.execute(() -> {
                                                        // undo the addition from the database
                                                        wordDAO.deleteWordDefinition(definitionToAdd);
                                                    });
                                                    wordDefinitionsList.remove(definitionToAdd);
                                                    adapter.notifyItemChanged(position);
                                                })
                                                .show();
                                    }
                                });
                        builder.create().show();
                }
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return wordDefinitionsList.size();
        }
    }


}
