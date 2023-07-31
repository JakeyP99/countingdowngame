package com.example.countingdowngame.mainActivity;

import static com.example.countingdowngame.mainActivity.SharedMainActivity.reverseTurnOrder;
import static com.example.countingdowngame.mainActivity.SharedMainActivity.splitScreenSetTextViewSizeBasedOnInt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.countingdowngame.R;
import com.example.countingdowngame.game.Game;
import com.example.countingdowngame.game.GameEventType;
import com.example.countingdowngame.game.Player;
import com.example.countingdowngame.stores.PlayerModelLocalStore;
import com.example.countingdowngame.utils.AudioManager;
import com.example.countingdowngame.utils.ButtonUtilsActivity;
import com.example.countingdowngame.wildCards.WildCardHeadings;
import com.example.countingdowngame.wildCards.WildCardType;
import com.example.countingdowngame.wildCards.wildCardTypes.ExtrasWildCardsAdapter;
import com.example.countingdowngame.wildCards.wildCardTypes.ExtrasWildCardsFragment;
import com.example.countingdowngame.wildCards.wildCardTypes.QuizWildCardsAdapter;
import com.example.countingdowngame.wildCards.wildCardTypes.QuizWildCardsFragment;
import com.example.countingdowngame.wildCards.wildCardTypes.TaskWildCardsAdapter;
import com.example.countingdowngame.wildCards.wildCardTypes.TaskWildCardsFragment;
import com.example.countingdowngame.wildCards.wildCardTypes.TruthWildCardsAdapter;
import com.example.countingdowngame.wildCards.wildCardTypes.TruthWildCardsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

;

public class MainActivitySplitScreen extends ButtonUtilsActivity {
    private static final int BACK_PRESS_DELAY = 3000; // 3 seconds
    private final Map<Player, Set<WildCardHeadings>> usedWildCard = new HashMap<>();
    private final Set<WildCardHeadings> usedWildCards = new HashSet<>();
    Button btnGenerate;
    Button btnGeneratePlayer2;
    Button btnContinue;
    Button btnContinuePlayer2;
    TextView wildText;
    TextView wildTextPlayer2;
    ImageButton imageButtonExit;
    ImageButton imageButtonExitPlayer2;
    private TextView numberText;
    private TextView numberTextPlayer2;
    private TextView nextPlayerText;
    private TextView nextPlayerTextPlayer2;
    private Button btnAnswer;
    private Button btnAnswerPlayer2;
    private Button btnAnswerRight;
    private Button btnAnswerRightPlayer2;
    private Button btnAnswerWrong;
    private Button btnAnswerWrongPlayer2;
    private Button btnWild;
    private Button btnWildPlayer2;
    private ImageView playerImage;
    private ImageView playerImagePlayer2;
    private boolean doubleBackToExitPressedOnce = false;
    private Handler shuffleHandler;
    private WildCardHeadings selectedWildCard; // Declare selectedWildCard at a higher level

    @Override
    protected void onResume() {
        super.onResume();
        AudioManager.getInstance().playSound(); // Start playing the sound
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioManager.getInstance().stopSound(); // Stop the sound
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Game.getInstance().endGame(this);
            gotoHomeScreen();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to go to the home screen", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, BACK_PRESS_DELAY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a5_game_start_splitscreen);
        initializeViews();
        setupButtons();
        startGame();
    }

    private void initializeViews() {
        btnGenerate = findViewById(R.id.btnGenerate);
        btnGeneratePlayer2 = findViewById(R.id.btnGeneratePlayer2);

        btnContinue = findViewById(R.id.btnBackWildCard);
        btnContinuePlayer2 = findViewById(R.id.btnBackWildCardPlayer2);

        wildText = findViewById(R.id.wild_textview);
        wildTextPlayer2 = findViewById(R.id.wild_textviewPlayer2);

        imageButtonExit = findViewById(R.id.imageBtnExit);
        imageButtonExitPlayer2 = findViewById(R.id.imageBtnExitPlayer2);

        playerImage = findViewById(R.id.playerImage);
        playerImagePlayer2 = findViewById(R.id.playerImagePlayer2);

        btnWild = findViewById(R.id.btnWild);
        btnWildPlayer2 = findViewById(R.id.btnWildPlayer2);

        numberText = findViewById(R.id.numberText);
        numberTextPlayer2 = findViewById(R.id.numberTextPlayer2);

        nextPlayerText = findViewById(R.id.textView_Number_Turn);
        nextPlayerTextPlayer2 = findViewById(R.id.textView_Number_TurnPlayer2);

        btnAnswer = findViewById(R.id.btnAnswer);
        btnAnswerPlayer2 = findViewById(R.id.btnAnswerPlayer2);

        btnAnswerRight = findViewById(R.id.btnAnswerRight);
        btnAnswerRightPlayer2 = findViewById(R.id.btnAnswerRightPlayer2);

        btnAnswerWrong = findViewById(R.id.btnAnswerWrong);
        btnAnswerWrongPlayer2 = findViewById(R.id.btnAnswerWrongPlayer2);

        shuffleHandler = new Handler();

    }

    private void startGame() {
        int soundResourceId = R.raw.cartoonloop;
        AudioManager.getInstance().initialize(this, soundResourceId);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            throw new RuntimeException("Missing extras");
        }

        int startingNumber = extras.getInt("startingNumber");

        // Load player data from PlayerModel
        List<Player> playerList = PlayerModelLocalStore.fromContext(this).loadSelectedPlayers();
        if (!playerList.isEmpty()) {
            // Set the player list in Game class
            Game.getInstance().setPlayers(this, playerList.size());
            Game.getInstance().setPlayerList(playerList);

            // Set the game object for each player
            for (Player player : playerList) {
                player.setGame(Game.getInstance());
            }
        }

        Game.getInstance().startGame(startingNumber, (e) -> {
            if (e.type == GameEventType.NEXT_PLAYER) {
                renderPlayer();
            }
        });
        renderPlayer();
    }

    //-----------------------------------------------------Buttons---------------------------------------------------//

    private void setupButtons() {
        setPairedViewsInvisible(btnContinue, btnContinuePlayer2, btnAnswer, btnAnswerPlayer2,
                btnAnswerRight, btnAnswerRightPlayer2, btnAnswerWrong, btnAnswerWrongPlayer2);

        btnUtils.setButton(btnAnswer, this::showAnswer);
        btnUtils.setButton(btnAnswerPlayer2, this::showAnswer);

        btnUtils.setButton(btnGenerate, this::ButtonGenerateFunction);
        btnUtils.setButton(btnWild, this::ButtonWildFunction);

        btnUtils.setButton(btnContinue, this::ButtonContinueFunction);
        btnUtils.setButton(btnGeneratePlayer2, this::ButtonGenerateFunction);
        btnUtils.setButton(btnWildPlayer2, this::ButtonWildFunction);
        btnUtils.setButton(btnContinuePlayer2, this::ButtonContinueFunction);

        imageButtonExit.setOnClickListener(view -> {
            Game.getInstance().endGame(this);
            gotoHomeScreen();
        });

        imageButtonExitPlayer2.setOnClickListener(view -> {
            Game.getInstance().endGame(this);
            gotoHomeScreen();
        });
    }

    //-----------------------------------------------------Button Shuffling---------------------------------------------------//

    private void startNumberShuffleAnimation() {
        btnGenerate.setEnabled(false);
        btnWild.setEnabled(false);
        btnGeneratePlayer2.setEnabled(false);
        btnWildPlayer2.setEnabled(false);

        int currentNumber = Game.getInstance().getCurrentNumber();
        final int shuffleDuration = 1500;

        int shuffleInterval = currentNumber >= 1000 ? 50 : 100;

        final Random random = new Random();
        shuffleHandler.postDelayed(new Runnable() {
            int shuffleTime = 0;

            @Override
            public void run() {
                int randomDigit = random.nextInt(currentNumber + 1);
                numberText.setText(String.valueOf(randomDigit));
                numberTextPlayer2.setText(String.valueOf(randomDigit));

                shuffleTime += shuffleInterval;


                if (shuffleTime < shuffleDuration) {
                    shuffleHandler.postDelayed(this, shuffleInterval);
                } else {
                    numberText.setText(String.valueOf(randomDigit));
                    numberTextPlayer2.setText(String.valueOf(randomDigit));

                    Game.getInstance().nextNumber(MainActivitySplitScreen.this, () -> gotoGameEnd(), numberText, numberTextPlayer2);

                    btnGenerate.setEnabled(true);
                    btnGeneratePlayer2.setEnabled(true);

                    btnWild.setEnabled(true);
                    btnWildPlayer2.setEnabled(true);
                }
            }
        }, shuffleInterval);
    }


    //-----------------------------------------------------Render Player---------------------------------------------------//

    private void renderPlayer() {
        Player currentPlayer = Game.getInstance().getCurrentPlayer();
        List<Player> playerList = PlayerModelLocalStore.fromContext(this).loadSelectedPlayers();

        if (!playerList.isEmpty()) {
            String playerName = currentPlayer.getName();
            String playerImageString = currentPlayer.getPhoto();

            nextPlayerText.setText(playerName);
            nextPlayerTextPlayer2.setText(playerName);

            btnWild.setText(currentPlayer.getWildCardAmount() + "\n" + "Wild Cards");
            btnWildPlayer2.setText(currentPlayer.getWildCardAmount() + "\n" + "Wild Cards");

            if (playerImageString != null) {
                byte[] decodedString = Base64.decode(playerImageString, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                playerImage.setImageBitmap(decodedBitmap);
                playerImagePlayer2.setImageBitmap(decodedBitmap);
            }
        }

        setPairedViewsVisible(nextPlayerText, nextPlayerTextPlayer2, numberText, numberTextPlayer2);

        int currentNumber = Game.getInstance().getCurrentNumber();
        numberText.setText(String.valueOf(currentNumber));
        numberTextPlayer2.setText(String.valueOf(currentNumber));

        splitScreenSetTextViewSizeBasedOnInt(numberText, String.valueOf(currentNumber));
        splitScreenSetTextViewSizeBasedOnInt(numberTextPlayer2, String.valueOf(currentNumber));

        SharedMainActivity.setNameSizeBasedOnInt(nextPlayerText, nextPlayerText.getText().toString());
        SharedMainActivity.setNameSizeBasedOnInt(nextPlayerTextPlayer2, nextPlayerTextPlayer2.getText().toString());

        if (currentPlayer.getWildCardAmount() > 0) {
            setPairedViewsVisible(btnWild, btnWildPlayer2);
        } else {
            setPairedViewsInvisible(btnWild, btnWildPlayer2);
        }
    }

    //-----------------------------------------------------Wild Card, and Skip Functionality---------------------------------------------------//

    private void wildCardActivate(Player player) {
        Game.getInstance().getCurrentPlayer().useWildCard();
        WildCardHeadings[] emptyProbabilitiesArray = new WildCardHeadings[0];
        QuizWildCardsAdapter quizAdapter = new QuizWildCardsAdapter(emptyProbabilitiesArray, this, WildCardType.QUIZ);
        TaskWildCardsAdapter taskAdapter = new TaskWildCardsAdapter(emptyProbabilitiesArray, this, WildCardType.TASK);
        TruthWildCardsAdapter truthAdapter = new TruthWildCardsAdapter(emptyProbabilitiesArray, this, WildCardType.TRUTH);
        ExtrasWildCardsAdapter extraAdapter = new ExtrasWildCardsAdapter(emptyProbabilitiesArray, this, WildCardType.EXTRAS);

        WildCardHeadings[] quizProbabilities = quizAdapter.loadWildCardProbabilitiesFromStorage(QuizWildCardsFragment.defaultQuizWildCards);
        WildCardHeadings[] taskProbabilities = taskAdapter.loadWildCardProbabilitiesFromStorage(TaskWildCardsFragment.defaultTaskWildCards);
        WildCardHeadings[] truthProbabilities = truthAdapter.loadWildCardProbabilitiesFromStorage(TruthWildCardsFragment.defaultTruthWildCards);
        WildCardHeadings[] extraProbabilities = extraAdapter.loadWildCardProbabilitiesFromStorage(ExtrasWildCardsFragment.defaultExtrasWildCards);

        List<WildCardHeadings> allProbabilities = new ArrayList<>();
        allProbabilities.addAll(Arrays.asList(quizProbabilities));
        allProbabilities.addAll(Arrays.asList(taskProbabilities));
        allProbabilities.addAll(Arrays.asList(truthProbabilities));
        allProbabilities.addAll(Arrays.asList(extraProbabilities));

        final TextView wildActivityTextView = findViewById(R.id.wild_textview);
        final TextView wildActivityTextViewPlayer2 = findViewById(R.id.wild_textviewPlayer2);

        boolean wildCardsEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("wild_cards_toggle", true);

        String selectedActivity = null;
        Set<WildCardHeadings> usedCards = usedWildCard.getOrDefault(player, new HashSet<>());

        if (wildCardsEnabled) {
            Random random = new Random();
            int typeChance = random.nextInt(4); // Generate a random number from 0 to 3

            WildCardHeadings[] selectedType;
            String wildCardType = ""; // Variable to store the type of wildcard

            switch (typeChance) {
                case 0:
                    selectedType = quizProbabilities;
                    wildCardType = "Quiz";
                    break;
                case 1:
                    selectedType = taskProbabilities;
                    wildCardType = "Task";
                    break;
                case 2:
                    selectedType = truthProbabilities;
                    wildCardType = "Truth";
                    break;
                case 3:
                    selectedType = extraProbabilities;
                    wildCardType = "Extras";
                    break;
                default:
                    selectedType = null; // Set selectedType to null when the case is not 0, 1, 2, or 3
                    wildCardType = "Null";
                    break;
            }

            if (selectedType == quizProbabilities) {
                setPairedViewsVisible(btnAnswer, btnAnswerPlayer2);
                setPairedViewsInvisible(btnContinue, btnContinuePlayer2);

            } else {

                setPairedViewsInvisible(btnAnswer, btnAnswerPlayer2);
                setPairedViewsVisible(btnContinue, btnContinuePlayer2);
            }

            int enabledCardsCount = (int) Arrays.stream(selectedType)
                    .filter(WildCardHeadings::isEnabled)
                    .count();

            Log.d("SelectedTypeEnabledCount", "SelectedType Enabled Count: " + enabledCardsCount);

            if (enabledCardsCount == 0) {
                btnAnswer.setVisibility(View.INVISIBLE);
                btnAnswerPlayer2.setVisibility(View.INVISIBLE);

                wildActivityTextView.setText("No wild cards available");
                wildActivityTextViewPlayer2.setText("No wild cards available");

                return;
            }

            List<WildCardHeadings> unusedCards = Arrays.stream(selectedType)
                    .filter(WildCardHeadings::isEnabled)
                    .filter(c -> !usedWildCards.contains(c))
                    .collect(Collectors.toList());

            // Calculate the total probabilities within the selected type
            int totalTypeProbabilities = unusedCards.stream()
                    .mapToInt(WildCardHeadings::getProbability)
                    .sum();

            // Generate a random number within the total probabilities
            int selectedIndex = random.nextInt(totalTypeProbabilities);

            // Select the wildcard based on the random number and its probability
            WildCardHeadings selectedCard = null;
            int cumulativeProbability = 0;

            for (WildCardHeadings card : unusedCards) {
                cumulativeProbability += card.getProbability();

                if (selectedIndex < cumulativeProbability) {
                    selectedCard = card;
                    break;
                }
            }

            if (selectedCard != null) {
                selectedActivity = selectedCard.getText();
                wildActivityTextView.setText(selectedActivity);
                wildActivityTextViewPlayer2.setText(selectedActivity);

                assert usedCards != null;
                usedCards.add(selectedCard);
                usedWildCards.add(selectedCard);
                usedWildCard.put(player, usedCards);

                selectedWildCard = selectedCard; // Update selectedWildCard to the current selected card

            }
            Log.d("WildCardType", "Type: " + wildCardType); // Log the type of wildcard
        }


        switch (Objects.requireNonNull(selectedActivity)) {
            case "Double the current number!": {
                int currentNumber = Game.getInstance().getCurrentNumber();
                int updatedNumber = Math.min(currentNumber * 2, 999999999);
                Game.getInstance().setCurrentNumber(updatedNumber);
                Game.getInstance().addUpdatedNumber(updatedNumber);
                numberText.setText(String.valueOf(updatedNumber));
                SharedMainActivity.setTextViewSizeBasedOnInt(numberText, String.valueOf(updatedNumber));
                break;
            }
            case "Half the current number!": {
                int currentNumber = Game.getInstance().getCurrentNumber();
                int updatedNumber = Math.max(currentNumber / 2, 1);
                Game.getInstance().setCurrentNumber(updatedNumber);
                Game.getInstance().addUpdatedNumber(updatedNumber);
                numberText.setText(String.valueOf(updatedNumber));
                SharedMainActivity.setTextViewSizeBasedOnInt(numberText, String.valueOf(updatedNumber));
                break;
            }
            case "Reset the number!":
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    throw new RuntimeException("Missing extras");
                }
                int startingNumber = extras.getInt("startingNumber");
                Game.getInstance().setCurrentNumber(startingNumber);
                Game.getInstance().addUpdatedNumber(startingNumber);
                numberText.setText(String.valueOf(startingNumber));
                SharedMainActivity.setTextViewSizeBasedOnInt(numberText, String.valueOf(startingNumber));
                break;
            case "Reverse the turn order!":
                reverseTurnOrder(player);
                break;
            case "Gain a couple more wildcards to use, I gotchya back!":
                Game.getInstance().getCurrentPlayer().gainWildCards(3);
                break;
            case "Lose a couple wildcards :( oh also drink 3 lol!":
                Game.getInstance().getCurrentPlayer().loseWildCards();
                break;
        }
    }


    //-----------------------------------------------------Button Functionality---------------------------------------------------//

    private void ButtonGenerateFunction() {
        setPairedViewsInvisible(wildText, wildTextPlayer2);
        setPairedViewsVisible(numberText, numberTextPlayer2, nextPlayerText, nextPlayerTextPlayer2);
        startNumberShuffleAnimation();
    }

    private void ButtonWildFunction() {
        wildCardActivate(Game.getInstance().getCurrentPlayer());

        if (selectedWildCard != null && selectedWildCard.hasAnswer()) {
            setPairedViewsVisible(btnAnswer, btnAnswerPlayer2);
        } else {
            setPairedViewsInvisible(btnAnswer, btnAnswerPlayer2);
        }

        setPairedViewsInvisible(btnWild, btnWildPlayer2, btnGenerate, btnGeneratePlayer2, numberText, numberTextPlayer2, nextPlayerText, nextPlayerTextPlayer2, playerImage, playerImagePlayer2);

        setPairedViewsVisible(wildText, wildTextPlayer2);
    }

    private void ButtonContinueFunction() {
        setPairedViewsInvisible(btnContinue, btnContinuePlayer2, btnAnswer, btnAnswerPlayer2, btnAnswerRight, btnAnswerRightPlayer2, btnAnswerWrong, btnAnswerWrongPlayer2, wildText, wildTextPlayer2);

        setPairedViewsVisible(btnGenerate, btnGeneratePlayer2, numberText, numberTextPlayer2, playerImage, playerImagePlayer2, nextPlayerText, nextPlayerTextPlayer2);

        Game.getInstance().getCurrentPlayer().useSkip();
    }

    private void quizAnswerView(String string) {
        setPairedViewsVisible(btnContinue, btnContinuePlayer2, wildText, wildTextPlayer2);
        setPairedViewsInvisible(btnWild, btnWildPlayer2, btnGenerate, btnGeneratePlayer2, nextPlayerText, nextPlayerTextPlayer2, numberText, numberTextPlayer2);

        wildText.setText(string);
        wildTextPlayer2.setText(string);
    }


    private void showAnswer() {
        TextView wildActivityTextView = findViewById(R.id.wild_textview);
        TextView wildActivityTextViewPlayer2 = findViewById(R.id.wild_textviewPlayer2);

        setPairedViewsVisible(btnAnswerRight, btnAnswerRightPlayer2, btnAnswerWrong, btnAnswerWrongPlayer2);

        if (selectedWildCard != null) {
            if (selectedWildCard.hasAnswer()) {
                String answer = selectedWildCard.getAnswer();
                wildActivityTextView.setText(answer);
                wildActivityTextViewPlayer2.setText(answer);

                Log.d("Answer", "Quiz WildCard:" + answer);

                setPairedViewsInvisible(btnContinue, btnContinuePlayer2);
                btnUtils.setButton(btnAnswerRight, this::quizAnswerRight);
                btnUtils.setButton(btnAnswerRightPlayer2, this::quizAnswerRight);
                btnUtils.setButton(btnAnswerWrong, this::quizAnswerWrong);
                btnUtils.setButton(btnAnswerWrongPlayer2, this::quizAnswerWrong);

            } else {
                wildActivityTextView.setText("No answer available");
                wildActivityTextViewPlayer2.setText("No answer available");
            }
        }

        setPairedViewsInvisible(btnAnswer, btnAnswerPlayer2);
    }

    private void quizAnswerRight() {
        Game.getInstance().getCurrentPlayer().gainWildCards(1);
        setPairedViewsInvisible(btnAnswerRight, btnAnswerRightPlayer2, btnAnswerWrong, btnAnswerWrongPlayer2);
        quizAnswerView("Since you got it right, give out a drink! \n\n P.S. You get to keep your wildcard too.");
    }


    private void quizAnswerWrong() {
        setPairedViewsInvisible(btnAnswerRight, btnAnswerRightPlayer2, btnAnswerWrong, btnAnswerWrongPlayer2);
        quizAnswerView("Since you got it wrong, take a drink! \n\n P.S. Maybe read a book once in a while.");
    }

    private void setPairedViewsInvisible(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    private void setPairedViewsVisible(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

}
