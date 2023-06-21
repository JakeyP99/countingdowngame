package com.example.countingdowngame;

import android.app.AlertDialog;
import android.os.Bundle;
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

public class TaskWildCardsFragment extends Fragment {
    Settings_WildCard_Probabilities[] taskWildCards = {
            new Settings_WildCard_Probabilities("Task! Take 1 drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Take 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Take 3 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Finish your drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Give 1 drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Give 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Give 3 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Choose a player to finish their drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The player to the left takes a drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The player to the right takes a drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The oldest player takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The youngest player takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The player who last peed takes 3 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The player with the oldest car takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Whoever last rode on a train takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Anyone who is standing takes 4 drinks, why are you standing? Sit down mate.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Anyone who is sitting takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Whoever has the longest hair takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Whoever is wearing a watch takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Whoever has a necklace on takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Double the ending drink (whoever loses must now do double the consequence).", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Drink for courage then deliver a line from your favourite film making it as dramatic as possible!", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Give 1 drink for every cheese you can name in 10 seconds.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The shortest person at the table must take 4 drinks then give 4 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Bare your biceps and flex for everyone. The players next to you each drink 2 for the view.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! All females drink 3, and all males drink 3. Equality.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Do a handstand and give out 3 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Choose someone to drink with their non-dominant hand for the next round.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The next person to laugh must take 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Everyone must stand on one leg while drinking.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Recite a tongue twister three times without making a mistake, if you do take 3 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Drink your whole drink while making eye contact with someone for the entire duration.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The person with the longest fingernails takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The person with the most expensive item on them takes 3 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The person with the most keys takes 3 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The person with the longest beard takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Choose someone to take 2 drinks and perform a breakdance move.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Choose someone to take 2 drinks and recite a poem.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Drink while doing a backbend.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Take a drink while doing a split.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Give a toast to the person who shares your zodiac sign and take a drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The person wearing the most buttons on their shirt takes 3 drinks. Mr Fancy aye.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! The person with the most watches takes 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Perform a dramatic reading of a nursery rhyme. Everyone takes a drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Impersonate your favorite celebrity. Others guess who it is. If they fail, they drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Everyone imitates a farm animal. The last one to start drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Create a new cocktail recipe using three random ingredients. Others taste and rate it. If it's bad, you drink.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Tell a cheesy joke. If others laugh, you're safe. If not, take 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Draw a picture blindfolded. Others rate your masterpiece. If the crowd does not like it, take 2 drinks.", 10, true, true),
            new Settings_WildCard_Probabilities("Task! Name three things that should be illegal but aren't. If others agree, you're safe, if not, you take 3 drinks.", 10, true, true),
    };

    private TaskWildCardsAdapter adapter; // Declare adapter as a field in the fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_truth_wildcards, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_WildCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Settings_WildCard_Mode mode = Settings_WildCard_Mode.DELETABLE;

        adapter = new TaskWildCardsAdapter(taskWildCards, requireContext(), mode); // Assign the adapter to the field

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

        for (Settings_WildCard_Probabilities wildcard : adapter.getWildCards()) {
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

            // Add "Quiz!" to the start of the wildcard text
            String wildcardText = "Task! " + text;

            Settings_WildCard_Probabilities newWildCard = new Settings_WildCard_Probabilities(wildcardText, probability, true, true);

            // Create a new array with increased size
            Settings_WildCard_Probabilities[] newQuizWildCards = new Settings_WildCard_Probabilities[taskWildCards.length + 1];
            System.arraycopy(taskWildCards, 0, newQuizWildCards, 0, taskWildCards.length);
            newQuizWildCards[taskWildCards.length] = newWildCard;

            // Update the quizWildCards array with the new array
            taskWildCards = newQuizWildCards;

            adapter.setWildCards(taskWildCards); // Update the adapter's dataset with the new array
            adapter.notifyDataSetChanged(); // Notify the adapter about the data change
        });

        builder.show();
    }
}
