package com.example.countingdowngame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
    }
    static Game gameInstance = new Game();
    private TextView numberText;
    private TextView nextPlayerText;
    private Button btnSkip;
    private Button btnWild;

    private Map<Player, Set<WildCardProbabilities>> usedWildCard = new HashMap<>();
    private Set<WildCardProbabilities> usedWildCards = new HashSet<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View wildText = findViewById(R.id.wild_textview);

        numberText = findViewById(R.id.numberText);
        Button btnGenerate = findViewById(R.id.btnGenerate);
        nextPlayerText = findViewById(R.id.textView_Number_Turn);
        btnSkip = findViewById(R.id.btnSkip);
        Button btnWild = findViewById(R.id.btnWild);
        Button btnBackWild = findViewById(R.id.btnBackWildCard);
        btnBackWild.setVisibility(View.INVISIBLE);
        ImageButton imageButtonExit = findViewById(R.id.imageBtnExit);



        //These are the button controls
        ButtonUtils.setButton(btnGenerate, null, this, () -> {
            gameInstance.nextNumber();
            wildText.setVisibility(View.INVISIBLE);
            numberText.setVisibility(View.VISIBLE);
            nextPlayerText.setVisibility(View.VISIBLE);
            numberText.setVisibility(View.VISIBLE);
        });

        ButtonUtils.setButton(btnBackWild, null, this, () -> {
            btnBackWild.setVisibility(View.INVISIBLE);
            btnGenerate.setVisibility(View.VISIBLE);
            wildText.setVisibility(View.INVISIBLE);
            numberText.setVisibility(View.VISIBLE);
            nextPlayerText.setVisibility(View.VISIBLE);
            numberText.setVisibility(View.VISIBLE);
        });

        ButtonUtils.setButton(btnSkip, null, this, () -> {
            gameInstance.getCurrentPlayer().useSkip();
            wildText.setVisibility(View.INVISIBLE);
            numberText.setVisibility(View.VISIBLE);
            nextPlayerText.setVisibility(View.VISIBLE);
            numberText.setVisibility(View.VISIBLE);
            btnBackWild.setVisibility(View.INVISIBLE);
            btnGenerate.setVisibility(View.VISIBLE);

        });


        ButtonUtils.setImageButton(imageButtonExit, HomeScreen.class, this, () -> {
            gameInstance.endGame();
        });

        ButtonUtils.setButton(btnWild, null, this, () -> {
            btnWild.setVisibility(View.INVISIBLE);
            wildText.setVisibility(View.VISIBLE);
            btnBackWild.setVisibility(View.VISIBLE);
            btnGenerate.setVisibility(View.INVISIBLE);
            nextPlayerText.setVisibility(View.INVISIBLE);
            numberText.setVisibility(View.INVISIBLE);
            gameInstance.getCurrentPlayer().useWildCard();
            Player currentPlayer = gameInstance.getCurrentPlayer();
            wildCardActivate(currentPlayer);
        });

        gameInstance.setPlayerEventListener(e -> {
            if (e.type == PlayerEventType.SKIP) {
                gameInstance.nextPlayer();
            }
        });

        gameInstance.setGameEventListener(e -> {
            switch (e.type) {
                case NEXT_PLAYER: {
                    renderPlayer();
                    break;
                }

                case GAME_END: {
                    gameInstance.endGame();
                    startActivity(new Intent(MainActivity.this, EndActivity.class));
                    break;
                }

                case NEXT_NUMBER: {
                    numberText.setText(String.valueOf(gameInstance.currentNumber));
                    break;
                }
                case GAME_START: {
                    break;
                }
            }
        });

        gameInstance.startGame(NumberChoice.startingNumber);
        numberText.setText(Integer.toString(gameInstance.currentNumber));
        renderPlayer();
    }

    private void renderPlayer() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> playerNamesSet = preferences.getStringSet("playerNames", null);
        String[] playerNamesArray = playerNamesSet.toArray(new String[0]);
        int currentPlayerIndex = gameInstance.currentPlayerId;

        String currentPlayerName = playerNamesArray[currentPlayerIndex];
        nextPlayerText.setText(currentPlayerName + "'s Turn");

        if (gameInstance.getCurrentPlayer().getSkipAmount() > 0) {
            btnSkip.setVisibility(View.VISIBLE);
        } else {
            btnSkip.setVisibility(View.INVISIBLE);
        }

        if (gameInstance.getCurrentPlayer().getWildCardAmount() > 0) {
            if (btnWild == null) {
                btnWild = findViewById(R.id.btnWild);
            }
            btnWild.setVisibility(View.VISIBLE);
        } else {
            if (btnWild != null) {
                btnWild.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void wildCardActivate(Player player) {
        Settings_WildCardChoice settings = new Settings_WildCardChoice();
        WildCardProbabilities[] activityProbabilities = settings.loadWildCardProbabilitiesFromStorage(getApplicationContext());

        final TextView wildActivityTextView = findViewById(R.id.wild_textview);
        player.useWildCard();

        boolean wildCardsEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("wild_cards_toggle", true);

        String selectedActivity = null;
        Set<WildCardProbabilities> usedCards = usedWildCard.getOrDefault(player, new HashSet<>());
        if (wildCardsEnabled) {
            List<WildCardProbabilities> unusedCards = Arrays.stream(activityProbabilities)
                    .filter(c -> c.isEnabled())
                    .filter(c -> !usedWildCards.contains(c))
                    .collect(Collectors.toList());

            if (unusedCards.isEmpty()) {
                usedCards.clear();
            }

            // Calculate total weight of unused wildcards
            int totalWeight = unusedCards.stream()
                    .mapToInt(WildCardProbabilities::getProbability)
                    .sum();

            if (totalWeight <= 0) {
                wildActivityTextView.setText("No wild cards available");
                return;
            }

            Random random = new Random();
            boolean foundUnusedCard = false;
            while (!foundUnusedCard) {
                int randomWeight = random.nextInt(totalWeight);
                int weightSoFar = 0;
                for (WildCardProbabilities activityProbability : unusedCards) {
                    weightSoFar += activityProbability.getProbability();
                    if (randomWeight < weightSoFar) {
                        // Check if the selected wildcard has already been used by the current player
                        if (!usedCards.contains(activityProbability)) {
                            selectedActivity = activityProbability.getText();
                            foundUnusedCard = true;
                            usedCards.add(activityProbability);
                        }
                        break;
                    }
                }
            }

            // Update the used wildcards for the current player
            usedWildCard.put(player, usedCards);
        }

        if (selectedActivity != null) {
            wildActivityTextView.setText(selectedActivity);
            for (WildCardProbabilities wc : activityProbabilities) {
                if (wc.getText().equals(selectedActivity)) {
                    player.addUsedWildCard(wc);
                    usedCards.add(wc);  // add to usedCards set for this player
                    break;
                }
            }
        }

        if (selectedActivity != null && selectedActivity.equals("Get a skip button to use on any one of your turns!")) {
            if (player.getSkipAmount() == 0) {
                player.skips++;
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        if (player.getWildCardAmount() > 0) {
            btnWild.setVisibility(View.VISIBLE);
        } else {
            btnWild.setVisibility(View.INVISIBLE);
        }
    }

}