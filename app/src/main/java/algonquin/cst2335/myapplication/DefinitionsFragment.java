package algonquin.cst2335.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DefinitionsFragment extends Fragment {

    private TextView meaningTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_definitions, container, false);

        meaningTextView = rootView.findViewById(R.id.definitionsRV);

        // Check if an item is selected from RecyclerView and display its details
        if (getArguments() != null && getArguments().containsKey("selectedDefinition")) {
            String selectedDefinition = getArguments().getString("selectedDefinition");
            displayDefinition(selectedDefinition);
        }

        return rootView;
    }

    private void displayDefinition(String definition) {
        meaningTextView.setText(definition);
    }
}

