package algonquin.cst2335.myapplication;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.myapplication.databinding.SearchDictionaryBinding;

public class Dictionary_Main extends AppCompatActivity {

    private ArrayList<word> term = new ArrayList<>();

    private List<Definition> definitions = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private SharedPreferences sharedPreferences;
    private MyDatabase myDatabase;
    private algonquin.cst2335.myapplication.Adapter adapter;
    private SearchDictionaryBinding binding;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);


        recyclerView = findViewById(R.id.recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        // Initialize other views and components

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        myDatabase = Room.databaseBuilder(getApplicationContext(),
                        MyDatabase.class, "my_database")
                .build();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        String lastSearchTerm = sharedPreferences.getString("lastSearchTerm", "");
        searchEditText.setText(lastSearchTerm);

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> {
            String searchTerm = searchEditText.getText().toString().trim();
            if (!searchTerm.isEmpty()) {
                searchDefinitions(searchTerm);

            }
        });

        // Setup button to view saved search terms
        Button viewSavedButton = findViewById(R.id.view_saved_button);
//        viewSavedButton.setOnClickListener(v -> showSavedSearchTerms());
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.help) {
                showHelpDialog();
                return true;
            }
            return false;
        });
    }

    private void searchDefinitions(String searchTerm) {
        String stringURL;

        try {
            stringURL = "https://api.dictionaryapi.dev/api/v2/entries/en/" +
                    URLEncoder.encode(searchTerm, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                (response) -> {
                    try {
                        JSONArray wordArray = response.getJSONArray("");
                        JSONObject wordObject = wordArray.getJSONObject(0);
                        String term = wordObject.getString("word");
                        JSONArray meaningsArray = wordObject.getJSONArray("meanings");

                        word wordTerm = new word(term);
                        List<word> wordsList = new ArrayList<>();
                        wordsList.add(wordTerm);

                        List<Definition> definitions = new ArrayList<>();
                        for (int i = 0; i < meaningsArray.length(); i++) {
                            JSONObject meaningObject = meaningsArray.getJSONObject(i);
                            JSONArray definitionsArray = meaningObject.getJSONArray("definitions");

                            for (int j = 0; j < definitionsArray.length(); j++) {
                                JSONObject definitionObject = definitionsArray.getJSONObject(j);
                                String definition = definitionObject.getString("definition");

                                Definition definitionTerm = new Definition(definition, wordTerm.getId());
                                definitions.add(definitionTerm);
                            }
                        }

                        // Store 'wordsList' and 'definitions' or perform other actions with this data
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                    }
                },
                error -> {
                    Toast.makeText(Dictionary_Main.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastSearchTerm", binding.searchEditText.getText().toString());
        editor.apply();
    }



    public class WordViewHolder extends RecyclerView.ViewHolder {

        private TextView meaningTextView;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            meaningTextView = itemView.findViewById(R.id.meaning_text_view); //this is the meaning holder
        }

        public void bind(String meaning) {
            meaningTextView.setText(meaningTextView.getText().toString());
        }
    }


    public class WordAdapter extends RecyclerView.Adapter<WordViewHolder> {

        private List<word> meanings;

        public WordAdapter(List<word> meanings) {
            this.meanings = meanings;
        }

        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meaning, parent, false);
            return new WordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
            Definition meaning = definitions.get(position);
            holder.bind(meaning.getMeaning().toString());
        }

        @Override
        public int getItemCount() {
            return meanings.size();
        }
    }

//    private void showSavedSearchTerms() {
//        // Retrieve saved search terms from the database using Room and update the RecyclerView
//        myDatabase.searchTermDao().getAllSearchTerms().observe(this, word -> {
//            // Update the RecyclerView with saved search terms
//            adapter.setData(word);
//
//        });
//    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help");
        builder.setMessage("Instructions on how to use the interface"); // Replace with your instructions

        builder.setPositiveButton("OK", (dialog, which) -> {
            // Dismiss dialog if needed or perform other actions on OK click
            dialog.dismiss();
        });

        AlertDialog helpDialog = builder.create();
        helpDialog.show();
    }
}

