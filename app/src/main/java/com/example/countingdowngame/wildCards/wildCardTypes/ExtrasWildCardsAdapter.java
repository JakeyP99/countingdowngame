package com.example.countingdowngame.wildCards.wildCardTypes;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.countingdowngame.wildCards.WildCardProperties;
import com.example.countingdowngame.wildCards.WildCardType;

public class ExtrasWildCardsAdapter extends WildCardsAdapter {
    public ExtrasWildCardsAdapter(WildCardProperties[] extrasWildCards, Context context, WildCardType mode) {
        super("ExtrasPrefs", extrasWildCards, context, mode);
    }

    @NonNull
    @Override
    public WildCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WildCardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}