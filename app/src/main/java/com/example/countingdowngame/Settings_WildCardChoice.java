package com.example.countingdowngame;

import static com.example.countingdowngame.WildCardMode.DELETABLE;
import static com.example.countingdowngame.WildCardMode.NON_DELETABLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class Settings_WildCardChoice extends ButtonUtilsActivity {
    private ListView listViewWildCard;
    private WildCardAdapter deletableAdapter;

    @Override
    public void onBackPressed() {
        startActivity(getSafeIntent(SettingClass.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wildcard_edit);

        listViewWildCard = findViewById(R.id.listView_WildCard);
        ListView listViewWildCardGameMode = findViewById(R.id.listView_GameModeWildCards);

        WildCardProbabilities[][] wildCardArrays = loadWildCardProbabilitiesFromStorage(getApplicationContext());
        WildCardProbabilities[] deletableWildCards = wildCardArrays[0];
        WildCardProbabilities[] nonDeletableWildCards = wildCardArrays[1];

        deletableAdapter = new WildCardAdapter(DELETABLE, this, deletableWildCards);
        WildCardAdapter nonDeletableAdapter = new WildCardAdapter(NON_DELETABLE, this, nonDeletableWildCards);

        listViewWildCard.setAdapter(deletableAdapter);
        listViewWildCardGameMode.setAdapter(nonDeletableAdapter);

        Button btnAddWildCard = findViewById(R.id.btnAddWildCard);
        btnAddWildCard.setOnClickListener(v -> addNewWildCard());
    }

    private void addNewWildCard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Wildcard");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText probabilityInput = new EditText(this);
        probabilityInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        probabilityInput.setHint("Probability (1-100)");
        layout.addView(probabilityInput);

        final EditText textInput = new EditText(this);
        textInput.setInputType(InputType.TYPE_CLASS_TEXT);
        textInput.setHint("Text");
        layout.addView(textInput);

        builder.setView(layout);

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.setPositiveButton("OK", (dialog, which) -> {
            int probability = Integer.parseInt(probabilityInput.getText().toString());
            String text = textInput.getText().toString();

            WildCardProbabilities newWildCard = new WildCardProbabilities(text, probability, true, true);

            WildCardProbabilities[][] probabilitiesArray = loadWildCardProbabilitiesFromStorage(
                    getApplicationContext());
            WildCardProbabilities[] deletableProbabilities = probabilitiesArray[0];

            ArrayList<WildCardProbabilities> wildCardList = new ArrayList<>(Arrays.asList(deletableProbabilities));
            wildCardList.add(newWildCard);

            deletableProbabilities = wildCardList.toArray(new WildCardProbabilities[0]);

            deletableAdapter = new WildCardAdapter(DELETABLE, this, deletableProbabilities);
            listViewWildCard.setAdapter(deletableAdapter);

            saveWildCardProbabilitiesToStorage(DELETABLE, deletableProbabilities);

            // Update the probabilities array in settings
            probabilitiesArray[0] = deletableProbabilities;

            // Refresh the adapter if needed
            deletableAdapter.notifyDataSetChanged();
        });

        builder.show();
    }

    WildCardProbabilities[][] loadWildCardProbabilitiesFromStorage(Context context) {
        SharedPreferences deletablePrefs = context.getSharedPreferences("DeletablePrefs", MODE_PRIVATE);
        SharedPreferences nonDeletablePrefs = context.getSharedPreferences("NonDeletablePrefs", MODE_PRIVATE);

        WildCardProbabilities[] deletableProbabilities = new WildCardProbabilities[]{
                new WildCardProbabilities("Take 1 drink.", 10, true, true),
                new WildCardProbabilities("Take 2 drinks.", 8, true, true),
                new WildCardProbabilities("Take 3 drinks.", 5, true, true),
                new WildCardProbabilities("Finish your drink.", 3, true, true),
                new WildCardProbabilities("Give 1 drink.", 10, true, true),
                new WildCardProbabilities("Give 2 drinks.", 8, true, true),
                new WildCardProbabilities("Give 3 drinks.", 5, true, true),
                new WildCardProbabilities("Choose a player to finish their drink.", 3, true, true),
                new WildCardProbabilities("The player to the left takes a drink.", 10, true, true),
                new WildCardProbabilities("The player to the right takes a drink.", 10, true, true),
                new WildCardProbabilities("The oldest player takes 2 drinks.", 10, true, true),
                new WildCardProbabilities("The youngest player takes 2 drinks.", 10, true, true),
                new WildCardProbabilities("The player who last peed takes 3 drinks.", 10, true, true),
                new WildCardProbabilities("The player with the oldest car takes 2 drinks.", 10, true, true),
                new WildCardProbabilities("Whoever last rode on a train takes 2 drinks.", 10, true, true),
                new WildCardProbabilities("Anyone who is standing takes 4 drinks, why are you standing? Sit down mate.", 10, true, true),
                new WildCardProbabilities("Anyone who is sitting takes 2 drinks.", 10, true, true),
                new WildCardProbabilities("Whoever has the longest hair takes 2 drinks.", 10, true, true),
                new WildCardProbabilities("Whoever is wearing a watch takes 2 drinks.", 10, true, true),
                new WildCardProbabilities("Whoever has a necklace on takes 2 drinks.", 10, true, true),
                new WildCardProbabilities("Double the ending drink (whoever loses must now do double the consequence).", 5, true, true),
                new WildCardProbabilities("Drink for courage then deliver a line from your favourite film making it as dramatic as possible!", 2, true, true),
                new WildCardProbabilities("Give 1 drink for every cheese you can name in 10 seconds.", 2, true, true),
                new WildCardProbabilities("The shortest person at the table must take 4 drinks then give 4 drinks.", 2, true, true),
                new WildCardProbabilities("Bare your biceps and flex for everyone. The players next to you each drink 2 for the view.", 2, true, true),
                new WildCardProbabilities("All females drink 3, and all males drink 3. Equality.", 5, true, true)
        };

        int deletableCount = deletablePrefs.getInt("wild_card_count", deletableProbabilities.length);
        if (deletableCount > deletableProbabilities.length) {
            deletableProbabilities = Arrays.copyOf(deletableProbabilities, deletableCount);
        }

        for (int i = 0; i < deletableCount; i++) {
            WildCardProbabilities p = deletableProbabilities[i];
            boolean enabled;
            String activity;
            int probability;

            if (p != null) {
                enabled = deletablePrefs.getBoolean("wild_card_enabled_" + i, p.isEnabled());
                activity = deletablePrefs.getString("wild_card_activity_" + i, p.getText());
                probability = deletablePrefs.getInt("wild_card_probability_" + i, p.getProbability());
            } else {
                enabled = deletablePrefs.getBoolean("wild_card_enabled_" + i, false);
                activity = deletablePrefs.getString("wild_card_activity_" + i, "");
                probability = deletablePrefs.getInt("wild_card_probability_" + i, 0);
            }


            deletableProbabilities[i] = new WildCardProbabilities(activity, probability, enabled, true);
        }

        WildCardProbabilities[] nonDeletableProbabilities = new WildCardProbabilities[]{
                new WildCardProbabilities("Get a skip button to use on any one of your turns!", 3, true, false)
        };


        for (int i = 0; i < nonDeletableProbabilities.length; i++) {

            WildCardProbabilities p = nonDeletableProbabilities[i];

            boolean enabled = nonDeletablePrefs.getBoolean("wild_card_enabled_" + i, p.isEnabled());
            int probability = nonDeletablePrefs.getInt("wild_card_probability_" + i, p.getProbability());

            nonDeletableProbabilities[i] = new WildCardProbabilities(p.getText(), probability, enabled, false);
        }

        return new WildCardProbabilities[][]{deletableProbabilities, nonDeletableProbabilities};
    }

    public void saveWildCardProbabilitiesToStorage(WildCardMode mode, WildCardProbabilities[] probabilities) {
        switch (mode) {
            case DELETABLE: {
                SharedPreferences deletablePrefs = getSharedPreferences("DeletablePrefs", MODE_PRIVATE);
                SharedPreferences.Editor deletableEditor = deletablePrefs.edit();

                deletableEditor.clear(); // Clear previous data

                for (int i = 0; i < probabilities.length; i++) {
                    WildCardProbabilities probability = probabilities[i];
                    deletableEditor.putBoolean("wild_card_enabled_" + i, probability.isEnabled());
                    deletableEditor.putString("wild_card_activity_" + i, probability.getText());
                    deletableEditor.putInt("wild_card_probability_" + i, probability.getProbability());
                }

                deletableEditor.putInt("wild_card_count", probabilities.length);
                deletableEditor.apply();

                break;
            }
            case NON_DELETABLE: {
                SharedPreferences nonDeletablePrefs = getSharedPreferences("NonDeletablePrefs", MODE_PRIVATE);
                SharedPreferences.Editor nonDeletableEditor = nonDeletablePrefs.edit();

                nonDeletableEditor.clear(); // Clear previous data

                for (int i = 0; i < probabilities.length; i++) {
                    WildCardProbabilities probability = probabilities[i];
                    nonDeletableEditor.putBoolean("wild_card_enabled_" + i, probability.isEnabled());
                    nonDeletableEditor.putInt("wild_card_probability_" + i, probability.getProbability());
                }
                nonDeletableEditor.apply();

                break;
            }
        }
    }
}

