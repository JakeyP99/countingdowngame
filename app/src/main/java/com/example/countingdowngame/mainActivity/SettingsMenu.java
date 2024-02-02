package com.example.countingdowngame.mainActivity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.res.ResourcesCompat;

import com.example.countingdowngame.R;
import com.example.countingdowngame.settings.GeneralSettingsLocalStore;
import com.example.countingdowngame.utils.ButtonUtilsActivity;
import com.example.countingdowngame.wildCards.WildCardProperties;
import com.example.countingdowngame.wildCards.wildCardTypes.WildCardsAdapter;

import io.github.muddz.styleabletoast.StyleableToast;

public class inGameSettings extends ButtonUtilsActivity implements View.OnClickListener {

    //-----------------------------------------------------Initialize---------------------------------------------------//
    private EditText wildcardPerPlayerEditText;
    private EditText totalDrinksEditText;
    private Button button_multiChoice;
    private Button button_nonMultiChoice;

    private Button button_quiz_toggle;
    private Button button_task_toggle;
    private Button button_truth_toggle;
    private Button button_misc_toggle;

    private Drawable buttonHighlightDrawable;
    private Drawable outlineForButton;
    private Button btnProgressToGame;


    private WildCardsAdapter adapter;


    //-----------------------------------------------------On Pause---------------------------------------------------//

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }

    public inGameSettings() {
        // Default constructor with no arguments
    }

    public inGameSettings(WildCardsAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onBackPressed() {
        String wildCardAmountInput = wildcardPerPlayerEditText.getText().toString().trim();
        String totalDrinkAmountInput = totalDrinksEditText.getText().toString().trim();

        boolean isWildCardValid = isValidInput(wildCardAmountInput, 3, 0, 100);
        boolean isTotalDrinkValid = isValidInput(totalDrinkAmountInput, 2, 1, 20);

        if (!isWildCardValid || !isTotalDrinkValid) {
            if (!isWildCardValid) {
                if (wildCardAmountInput.isEmpty()) {
                    wildcardPerPlayerEditText.setText("1");
                } else {
                    StyleableToast.makeText(getApplicationContext(), "Please enter a number between 0 and 100", R.style.newToast).show();
                }
            }

            if (!isTotalDrinkValid) {
                totalDrinksEditText.setText("1");

            }

        }
        savePreferences();
        super.onBackPressed();
    }



    //-----------------------------------------------------On Create---------------------------------------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_ingame);
        initializeViews();
        loadPreferences();
        setButtonListeners();


    }

    //-----------------------------------------------------Initialize Views---------------------------------------------------//

    private void initializeViews() {
        // Initialize views
        buttonHighlightDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.buttonhighlight, null);
        outlineForButton = ResourcesCompat.getDrawable(getResources(), R.drawable.outlineforbutton, null);
        btnProgressToGame = findViewById(R.id.btnContinueToGame);
        button_multiChoice = findViewById(R.id.button_multiChoice);
        button_nonMultiChoice = findViewById(R.id.button_nonMultiChoice);

        button_quiz_toggle = findViewById(R.id.button_quiz_toggle);
        button_task_toggle = findViewById(R.id.button_task_toggle);
        button_truth_toggle = findViewById(R.id.button_truth_toggle);
        button_misc_toggle = findViewById(R.id.button_misc_toggle);


        // Find and set up EditTexts
        wildcardPerPlayerEditText = findViewById(R.id.edittext_wildcard_amount);
        totalDrinksEditText = findViewById(R.id.edittext_drink_amount);

        // Set up TextWatchers for EditTexts
        setupTextWatcher(wildcardPerPlayerEditText, 3, this::isValidWildCardAmount);
        setupTextWatcher(totalDrinksEditText, 2, this::isValidTotalDrinkAmount);
    }

    private void setupTextWatcher(EditText editText, int maxLength, Runnable validationAction) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateInput(editText, maxLength);
                validationAction.run();
            }
        });
    }

    private void validateInput(EditText editText, int maxLength) {
        String input = editText.getText().toString().trim();

        if (input.length() > maxLength) {
            input = input.substring(0, maxLength);
            editText.setText(input);
            editText.setSelection(input.length());
        }
    }

    private boolean isValidInput(String input, int maxLength, int minValue, int maxValue) {
        if (input.length() > maxLength) {
            input = input.substring(0, maxLength);
        }
        if (input.length() < minValue) {
            // Handle the scenario where input is empty or shorter than minValue
            input = ""; // Set input to an empty string or handle as per your logic
        }
        try {
            int value = Integer.parseInt(input);
            return value >= minValue && value <= maxValue;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void isValidTotalDrinkAmount() {
        isValidInput(
                totalDrinksEditText.getText().toString().trim(),
                2, // Max length
                1, // Minimum value
                20 // Maximum value
        );// Handle invalid total drink amount
    }

    private void isValidWildCardAmount() {
        isValidInput(
                wildcardPerPlayerEditText.getText().toString().trim(),
                3, // Max length
                0,  // Minimum value
                100 // Maximum value
        );// Handle invalid wild card amount
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId(); // Store the view ID in a variable

        switch (viewId) {
            case R.id.button_multiChoice:
                toggleButtonChoice(button_multiChoice, button_nonMultiChoice);
                break;

            case R.id.button_nonMultiChoice:
                toggleButtonChoice(button_nonMultiChoice, button_multiChoice);
                break;

            case R.id.button_quiz_toggle:
                toggleWildCardButton(button_quiz_toggle, "Quiz");
                break;

            case R.id.button_task_toggle:
                toggleWildCardButton(button_task_toggle, "Task");
                break;

            case R.id.button_truth_toggle:
                toggleWildCardButton(button_truth_toggle, "Truth");
                break;

            case R.id.button_misc_toggle:
                toggleWildCardButton(button_misc_toggle, "Extra");
                break;
        }

        savePreferences();
    }



    private void setButtonListeners() {
        button_multiChoice.setOnClickListener(this);
        button_nonMultiChoice.setOnClickListener(this);

        button_quiz_toggle.setOnClickListener(this);
        button_task_toggle.setOnClickListener(this);
        button_truth_toggle.setOnClickListener(this);
        button_misc_toggle.setOnClickListener(this);

        btnUtils.setButton(btnProgressToGame, () -> {

            String wildCardAmountInput = wildcardPerPlayerEditText.getText().toString().trim();
            String totalDrinkAmountInput = totalDrinksEditText.getText().toString().trim();

            boolean isWildCardAmountValid = isValidInput(wildCardAmountInput, 3, 0, 100);
            boolean isTotalDrinkAmountValid = isValidInput(totalDrinkAmountInput, 2, 1, 20);

            if (isWildCardAmountValid && isTotalDrinkAmountValid) {
                savePreferences();
                goToMainGameWithExtra(Integer.parseInt(totalDrinkAmountInput));
            } else {
                if (!isWildCardAmountValid) {
                    if (wildCardAmountInput.isEmpty()) {
                        wildcardPerPlayerEditText.setText("0");
                        savePreferences();
                        goToMainGameWithExtra(Integer.parseInt(totalDrinkAmountInput));
                    } else {
                        StyleableToast.makeText(getApplicationContext(), "Please enter a wildcard quantity between 0 and 100", R.style.newToast).show();
                    }
                }

                if (!isTotalDrinkAmountValid) {
                    if (totalDrinkAmountInput.isEmpty()) {
                        totalDrinksEditText.setText("0");
                        savePreferences();
                        goToMainGameWithExtra(Integer.parseInt(totalDrinkAmountInput));
                    } else {
                        StyleableToast.makeText(getApplicationContext(), "Please enter a total drink limit between 1 and 20", R.style.newToast).show();
                    }
                }
            }
        });
    }

    private void toggleWildCardButton(Button button, String targetActivity) {
        Log.d(TAG, "toggleWildCardButton: " + targetActivity + " is toggled");
        boolean isSelected = !button.isSelected();
        button.setSelected(isSelected);
        button.setBackground(isSelected ? buttonHighlightDrawable : outlineForButton);

        toggleWildCardsByActivity(targetActivity);
    }

    private void toggleButtonChoice(Button selectedButton, Button unselectedButton) {
        boolean isSelected = !selectedButton.isSelected();
        selectedButton.setSelected(isSelected);
        unselectedButton.setSelected(!isSelected);

        selectedButton.setBackground(isSelected ? buttonHighlightDrawable : outlineForButton);
        unselectedButton.setBackground(!isSelected ? buttonHighlightDrawable : outlineForButton);
    }

    //-----------------------------------------------------Wild Card Choices---------------------------------------------------//

    public void toggleWildCardsByActivity(String targetActivity) {
        if (adapter != null) {
            WildCardProperties[] wildCards = adapter.getWildCards();

            for (WildCardProperties wildcard : wildCards) {
                if (wildcard.getActivity().equals(targetActivity)) {
                    wildcard.setEnabled(!wildcard.isEnabled());
                }
            }

            // Move the adapter-related operations inside the if block
            adapter.setWildCards(wildCards);
            adapter.notifyDataSetChanged();
            adapter.saveWildCardProbabilitiesToStorage(wildCards);

        } else {
            // Handle the case where the adapter is null
            Log.e("Adapter", "Adapter is null");
        }
    }


    //-----------------------------------------------------Load and Save Preferences---------------------------------------------------//

    // Inside WildCardSettings or any other settings activity
    private void goToMainGameWithExtra(int totalDrinkNumber) {

        GeneralSettingsLocalStore store = GeneralSettingsLocalStore.fromContext(this);
        int wildCardCount = store.playerWildCardCount();
        Log.d(TAG, "Wild Card Count: " + wildCardCount);


        // Retrieve the extras passed from NumberChoice activity
        int startingNumber = getIntent().getIntExtra("startingNumber", 0); // 0 is the default value if the extra is not found
        // Create an Intent to start the main game activity
        Intent intent = new Intent(this, MainActivityGame.class);

        // Pass the extras to the main game activity
        intent.putExtra("startingNumber", startingNumber);
        intent.putExtra("totalDrinkNumber", totalDrinkNumber);

        // Start the main game activity
        startActivity(intent);
    }


    private void loadPreferences() {

        int loadWildCardAmount = GeneralSettingsLocalStore.fromContext(this).playerWildCardCount();
        wildcardPerPlayerEditText.setText(String.valueOf(loadWildCardAmount));

        int loadTotalDrinkAmount = GeneralSettingsLocalStore.fromContext(this).totalDrinkAmount();
        totalDrinksEditText.setText(String.valueOf(loadTotalDrinkAmount));


        boolean multiChoiceSelected = GeneralSettingsLocalStore.fromContext(this).isMultiChoice();
        button_multiChoice.setSelected(multiChoiceSelected);
        button_nonMultiChoice.setSelected(!multiChoiceSelected);

        if (multiChoiceSelected) {
            button_multiChoice.setBackground(buttonHighlightDrawable);
            button_nonMultiChoice.setBackground(outlineForButton);
        } else {
            button_multiChoice.setBackground(outlineForButton);
            button_nonMultiChoice.setBackground(buttonHighlightDrawable);
        }

        // Check if it's the first installation
        if (isFirstInstallation()) {
            // Enable all wild card buttons initially
            toggleWildCardButton(button_quiz_toggle, "Quiz");
            toggleWildCardButton(button_task_toggle, "Task");
            toggleWildCardButton(button_truth_toggle, "Truth");
            toggleWildCardButton(button_misc_toggle, "Extra");
        } else {
            // Load preferences for wild card buttons
            toggleWildCardButton(button_quiz_toggle, "Quiz");
            toggleWildCardButton(button_task_toggle, "Task");
            toggleWildCardButton(button_truth_toggle, "Truth");
            toggleWildCardButton(button_misc_toggle, "Extra");
        }

    }

    private boolean isFirstInstallation() {
        // Implement logic to check if it's the first installation
        // This could involve checking a shared preference, database, or other indicators
        // For simplicity, you can use a shared preference as an example

        // Assuming you have a shared preference key to track first installation
        String firstInstallationKey = "isFirstInstallation";
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean isFirstInstallation = preferences.getBoolean(firstInstallationKey, true);

        // If it's the first installation, update the preference and return true
        if (isFirstInstallation) {
            preferences.edit().putBoolean(firstInstallationKey, false).apply();
            return true;
        }

        return false;
    }

    private void savePreferences() {
        int wildCardAmountSetInSettings = Integer.parseInt(wildcardPerPlayerEditText.getText().toString());
        GeneralSettingsLocalStore.fromContext(this).setPlayerWildCardCount(wildCardAmountSetInSettings);


        int totalDrinkAmountSetInSettings = Integer.parseInt(totalDrinksEditText.getText().toString());
        GeneralSettingsLocalStore.fromContext(this).setTotalDrinkAmount(totalDrinkAmountSetInSettings);


        GeneralSettingsLocalStore store = GeneralSettingsLocalStore.fromContext(this);
        store.setIsMultiChoice(button_multiChoice.isSelected());
    }
}