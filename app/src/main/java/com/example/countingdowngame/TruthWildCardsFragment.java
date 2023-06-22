package com.example.countingdowngame;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TruthWildCardsFragment extends Fragment {
    WildCardHeadings[] truthWildCards = {
            new WildCardHeadings("Truth! Have you ever had a crush on a friend's significant other?", 10, true, true),
            new WildCardHeadings("Truth! What is your most regrettable romantic rejection?", 10, true, true),
            new WildCardHeadings("Truth! Have you ever stolen something? If yes, what and why?", 10, true, true),
            new WildCardHeadings("Truth! What is your most crazy sex story?", 10, true, true),
            new WildCardHeadings("Truth! Have you ever engaged in a naughty video chat or phone sex?", 10, true, true),
            new WildCardHeadings("Truth! Have you ever watched adult content while in a public setting?", 10, true, true),
            new WildCardHeadings("Truth! Have you ever had a sensual massage with a happy ending?", 10, true, true),
            new WildCardHeadings("Truth! Have you ever betrayed a friend's trust and never apologized?", 10, true, true),
            new WildCardHeadings("Truth! Have you ever knowingly ruined someone's relationship?", 10, true, true),
            new WildCardHeadings("Truth! Have you ever stolen something valuable from a family member?", 10, true, true),
            new WildCardHeadings("Truth! Have you ever caused someone to lose their job?", 10, true, true),
            new WildCardHeadings("Truth! Have you ever destroyed evidence to cover up your wrongdoing?", 10, true, true),
    };

    private TruthWildCardsAdapter adapter; // Declare adapter as a field in the fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_truth_wildcards, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_WildCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        WildCardType mode = WildCardType.DELETABLE;

        adapter = new TruthWildCardsAdapter(truthWildCards, requireContext(), mode); // Assign the adapter to the field

        recyclerView.setAdapter(adapter);

        Button btnAddWildCard = view.findViewById(R.id.btnAddWildCard);
        btnAddWildCard.setOnClickListener(v -> {
            addNewWildCard();
        });

        Button btnToggleAll = view.findViewById(R.id.btnToggleAll);
        btnToggleAll.setOnClickListener(v -> toggleAllWildCards());

        return view;
    }


    private void toggleAllWildCards() {
        boolean allEnabled = adapter.areAllEnabled();

        for (WildCardHeadings wildcard : adapter.getWildCards()) {
            wildcard.setEnabled(!allEnabled);
        }

        adapter.notifyDataSetChanged();
    }


    private void addNewWildCard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Wildcard");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText textInput = new EditText(requireContext());
        textInput.setInputType(InputType.TYPE_CLASS_TEXT);
        textInput.setHint("Wildcard Title");
        layout.addView(textInput);

        final EditText probabilityInput = new EditText(requireContext());
        probabilityInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        probabilityInput.setHint("Probability (0-9999)");
        layout.addView(probabilityInput);

        builder.setView(layout);

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.setPositiveButton("OK", (dialog, which) -> {
            int probability;
            try {
                probability = Integer.parseInt(probabilityInput.getText().toString());
            } catch (NumberFormatException e) {
                probability = 10; // Invalid input, set to a default value
            }

            String inputText = probabilityInput.getText().toString().trim();
            String text = textInput.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "The wildcard needs some text, please and thanks!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (inputText.length() > 4) {
                Toast.makeText(getContext(), "Please enter a probability with 4 or fewer digits.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (textInput.length() <=0 ) {
                Toast.makeText(getContext(), "The wildcard needs some text, please and thanks!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (textInput.length() > 100 ) {
                Toast.makeText(getContext(), "Sorry, way too big of a wildcard boss man, limited to 100 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add "Truth!" to the start of the wildcard text
            String wildcardText = "Truth! " + text;

            WildCardHeadings newWildCard = new WildCardHeadings(wildcardText, probability, true, true);

            // Add the new wildcard to SharedPreferences
            saveNewWildCard(newWildCard);

            // Update the adapter's dataset by loading the wildcards from SharedPreferences
            loadWildCardsFromSharedPreferences();

            adapter.notifyDataSetChanged(); // Notify the adapter about the data change
        });

        builder.show();
    }


    private void saveNewWildCard(WildCardHeadings wildcard) {
        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Get the current wildcard count
        int count = sharedPreferences.getInt("wildcard_count", 0);

        // Increment the count and save it back
        int newCount = count + 1;
        sharedPreferences.edit().putInt("wildcard_count", newCount).apply();

        // Save the new wildcard data
        sharedPreferences.edit()
                .putString("wildcard_text_" + newCount, wildcard.getText())
                .putInt("wildcard_probability_" + newCount, wildcard.getProbability())
                .apply();
    }

    private void loadWildCardsFromSharedPreferences() {
        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Get the wildcard count
        int count = sharedPreferences.getInt("wildcard_count", 0);

        // Create a new array to store the wildcards
        WildCardHeadings[] newWildCards = new WildCardHeadings[count];

        // Load the wildcard data from SharedPreferences
        for (int i = 0; i < count; i++) {
            String text = sharedPreferences.getString("wildcard_text_" + (i + 1), "");
            int probability = sharedPreferences.getInt("wildcard_probability_" + (i + 1), 10);
            newWildCards[i] = new WildCardHeadings(text, probability, true, true);
        }

        // Update the dataset of the adapter
        adapter.setWildCards(newWildCards);
    }

}
