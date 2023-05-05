package com.example.countingdowngame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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
    // This means you can't go back
    @Override
    public void onBackPressed() {
        // Do nothing
    }

    // This sets the new game.
    static Game gameInstance = new Game();
    private TextView numberText;
    private TextView nextPlayerText;
    private Button btnSkip;
    private Button btnWild;

    private Map<Player, Set<WildCardProbabilities>> usedWildCards = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View wildText = findViewById(R.id.wild_textview);
        final MediaPlayer bop = MediaPlayer.create(this, R.raw.bop);

        numberText = findViewById(R.id.numberText);
        Button btnGenerate = findViewById(R.id.btnGenerate);
        nextPlayerText = findViewById(R.id.textView_Number_Turn);
        btnSkip = findViewById(R.id.btnSkip);
        Button btnWild = findViewById(R.id.btnWild);
        ImageButton imageButtonExit = findViewById(R.id.imageBtnExit);


        //These are the button controls
        ButtonUtils.setButton(btnGenerate, null, this, () -> {
            gameInstance.nextNumber();
            bop.start();
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

        });


        ButtonUtils.setImageButton(imageButtonExit, HomeScreen.class, this, () -> {
            gameInstance.endGame();
            bop.start();
        });

        ButtonUtils.setButton(btnWild, null, this, () -> {
            btnWild.setVisibility(View.INVISIBLE);
            wildText.setVisibility(View.VISIBLE);
            nextPlayerText.setVisibility(View.INVISIBLE);
            btnSkip.setVisibility(View.INVISIBLE);
            numberText.setVisibility(View.INVISIBLE);
            bop.start();

            gameInstance.getCurrentPlayer().useWildCard();
            Player currentPlayer = gameInstance.getCurrentPlayer();
            wildCardActivate(currentPlayer);
        });


        // This sets a new playerEventListener, which is linked to the skip button. So the app knows when that button is clicked, it provides a functionality to go to the next player (we made the functionality below)
        gameInstance.setPlayerEventListener(e -> {
            if (e.type == PlayerEventType.SKIP) {
                gameInstance.nextPlayer();
            }
        });

        //This is the functionality mentioned above. Note getCurrentPlayer is found in Game class. Note gameInstance means that it is set for a brand new game.
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
        nextPlayerText.setText("Player " + (gameInstance.getCurrentPlayer().getName()) + "'s" + " Turn");

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
        final TextView wildActivityTextView = findViewById(R.id.wild_textview);
        player.useWildCard();

        WildCardProbabilities[] activityProbabilities = {
                new WildCardProbabilities("Take 1 drink.", 10),
                new WildCardProbabilities("Take 2 drinks.", 8),
                new WildCardProbabilities("Take 3 drinks.", 5),
                new WildCardProbabilities("Finish your drink.", 3),
                new WildCardProbabilities("Give 1 drink.", 10),
                new WildCardProbabilities("Give 2 drinks.", 8),
                new WildCardProbabilities("Give 3 drinks.", 5),
                new WildCardProbabilities("Choose a player to finish their drink.", 3),
                new WildCardProbabilities("The player to the left takes a drink.", 10),
                new WildCardProbabilities("The player to the right takes a drink.", 10),
                new WildCardProbabilities("The oldest player takes 2 drinks.", 10),
                new WildCardProbabilities("The youngest player takes 2 drinks.", 10),
                new WildCardProbabilities("The player who last peed takes 3 drinks.", 10),
                new WildCardProbabilities("The player with the oldest car takes 2 drinks.", 10),
                new WildCardProbabilities("Whoever last rode on a train takes 2 drinks.", 10),
                new WildCardProbabilities("Anyone who is standing takes 4 drinks, why are you standing? Sit down mate.", 10),
                new WildCardProbabilities("Anyone who is sitting takes 2 drinks.", 10),
                new WildCardProbabilities("Whoever has the longest hair takes 2 drinks.", 10),
                new WildCardProbabilities("Whoever is wearing a watch takes 2 drinks.", 10),
                new WildCardProbabilities("Whoever has a necklace on takes 2 drinks.", 10),
                new WildCardProbabilities("Double the ending drink (whoever loses must now do double the consequence).", 5),
                new WildCardProbabilities("Get a skip button to use on any one of your turns!", 3),
                new WildCardProbabilities("Drink for courage then deliver a line from your favourite film making it as dramatic as possible!", 2),
                new WildCardProbabilities("Give 1 drink for every cheese you can name in 10 seconds.", 2),
                new WildCardProbabilities("The shortest person at the table must take 4 drinks then give 4 drinks.", 2),
                new WildCardProbabilities("Bare your biceps and flex for everyone. The players next to you each drink 2 for the view.", 2),
                new WildCardProbabilities("All females drink 3, and all males drink 3. Equality.", 2),

        };


        Set<WildCardProbabilities> usedCards = usedWildCards.getOrDefault(player, new HashSet<>());

        List<WildCardProbabilities> unusedCards = Arrays.stream(activityProbabilities)
                .filter(c -> !usedCards.contains(c))
                .collect(Collectors.toList());

        if (unusedCards.isEmpty()) {
            usedCards.clear();
        }

        int totalWeight = unusedCards.stream()
                .mapToInt(WildCardProbabilities::getProbability)
                .sum();

        if (totalWeight <= 0) {
            wildActivityTextView.setText("No wild cards available");
            return;
        }

        Random random = new Random();
        String selectedActivity = "";
        boolean foundUnusedCard = false;
        while (!foundUnusedCard) {
            int randomWeight = random.nextInt(totalWeight);
            int weightSoFar = 0;
            for (WildCardProbabilities activityProbability : unusedCards) {
                weightSoFar += activityProbability.getProbability();
                if (randomWeight < weightSoFar) {
                    selectedActivity = activityProbability.getActivity();
                    foundUnusedCard = true;
                    usedCards.add(activityProbability);
                    break;
                }
            }
        }

        if (selectedActivity.equals("Get a skip button to use on any one of your turns!")) {
            if (player.getSkipAmount() == 0) {
                player.skips++;
                btnSkip.setVisibility(View.VISIBLE);
            }

            if (usedCards.contains(activityProbabilities[0])) {
                if (player.getSkipAmount() > 0) {
                    btnSkip.setVisibility(View.VISIBLE);
                } else {
                    btnSkip.setVisibility(View.GONE);
                }
            } else {
                btnSkip.setVisibility(View.GONE);
            }
        } else {
            btnSkip.setVisibility(View.GONE);
        }

        if (player.getWildCardAmount() > 0) {
            btnWild.setVisibility(View.VISIBLE);
        } else {
            btnWild.setVisibility(View.INVISIBLE);
        }


        wildActivityTextView.setText(selectedActivity);

    }
}
