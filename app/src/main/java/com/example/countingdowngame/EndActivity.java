package com.example.countingdowngame;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class EndActivity extends ButtonUtilsActivity {
    @Override
    public void onBackPressed() {
        startActivity(getSafeIntent(HomeScreen.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a6_end_game);

        final ListView previousNumbersList = findViewById(R.id.previousNumbers);

        // Retrieve the saved state of the switches from shared preferences
        final Button btnPlayAgain = findViewById(R.id.btnplayAgain);
        final Button btnNewPlayer = findViewById(R.id.btnNewPlayer);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(EndActivity.this, R.layout.list_view_end_game, R.id.previousNumbers, Game.getInstance().getPreviousNumbersFormatted());
        previousNumbersList.setAdapter(adapter);

        btnUtils.setButton(btnPlayAgain, NumberChoice.class, () -> {
            Game.getInstance().playAgain();
        });

        btnUtils.setButton(btnNewPlayer, PlayerNumberChoice.class, null);
    }
}
