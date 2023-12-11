package com.example.countingdowngame.mainActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.countingdowngame.R;
import com.example.countingdowngame.settings.GeneralSettingsLocalStore;
import com.example.countingdowngame.utils.ButtonUtilsActivity;

public class GameModeChoice extends ButtonUtilsActivity {

    private GeneralSettingsLocalStore mGeneralSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_mode_choice);

        mGeneralSettings = GeneralSettingsLocalStore.fromContext(this);

        setupButtonControls();
    }

    private void setupButtonControls() {
        Button btnGameModeClassic = findViewById(R.id.btnGameModeClassic);
        Button btnGameModeQuiz = findViewById(R.id.btnGameModeQuiz);

        btnGameModeClassic.setOnClickListener(view -> setGameMode(true));
        btnGameModeQuiz.setOnClickListener(view -> setGameMode(false));
    }

    private void setGameMode(boolean isClassicMode) {
        mGeneralSettings.setIsGameModeClassic(isClassicMode);
        gotoGameModeClassic();
    }
}
