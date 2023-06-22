package com.example.countingdowngame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

public class TruthWildCardsAdapter extends WildCardsAdapter {
    public TruthWildCardsAdapter(WildCardHeadings[] truthWildCards, Context context, WildCardType mode) {
        super("TruthPrefs", truthWildCards, context, mode);
    }
    @NonNull
    @Override
    public WildCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_wild_cards, parent, false);
        return new TaskWildCardViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull WildCardViewHolder holder, int position) {
        WildCardHeadings wildcard = wildCards[position];
        holder.bind(wildcard);
    }
    public void setWildCards(WildCardHeadings[] wildCards) {
        this.wildCards = wildCards;
    }
    public boolean areAllEnabled() {
        for (WildCardHeadings wildcard : wildCards) {
            if (!wildcard.isEnabled()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public int getItemCount() {
        return wildCards.length;
    }
    public WildCardHeadings[] getWildCards() {
        return wildCards;
    }

    public class TaskWildCardViewHolder extends WildCardViewHolder {
        public TaskWildCardViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(WildCardHeadings wildcard) {
            textViewTitle.setText(wildcard.getText());
            textViewProbabilities.setText(String.valueOf(wildcard.getProbability()));
            switchEnabled.setChecked(wildcard.isEnabled());

            switchEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
                wildcard.setEnabled(isChecked);
                saveWildCardProbabilitiesToStorage(wildCards);
            });

            editButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Edit Wildcard Properties");
                int blueDarkColor = mContext.getResources().getColor(R.color.bluedark);

                LinearLayout layout = new LinearLayout(mContext);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText textInput = new EditText(mContext);

                if (wildcard.isDeletable()) {
                    textInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    textInput.setText(wildcard.getText());
                    layout.addView(textInput);
                } else {
                    textInput.setEnabled(false);
                }

                final EditText probabilityInput = new EditText(mContext);
                probabilityInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                probabilityInput.setText(String.valueOf(wildcard.getProbability()));
                layout.addView(probabilityInput);

                builder.setView(layout);

                builder.setNegativeButton(Html.fromHtml("<font color='" + blueDarkColor + "'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                ArrayList<WildCardHeadings> wildCardList = new ArrayList<>(Arrays.asList(wildCards));

                if (wildcard.isDeletable()) {
                    builder.setNeutralButton(Html.fromHtml("<font color='" + blueDarkColor + "'>Delete</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wildCardList.remove(getAdapterPosition());
                            wildCards = wildCardList.toArray(new WildCardHeadings[0]);
                            notifyDataSetChanged();
                            saveWildCardProbabilitiesToStorage(wildCards);
                        }
                    });
                }

                builder.setPositiveButton(Html.fromHtml("<font color='" + blueDarkColor + "'>OK</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputText = textInput.getText().toString().trim();
                        if (wildcard.isDeletable() && inputText.isEmpty()) {
                            Toast.makeText(mContext, "The wildcard needs some text, please and thanks!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int probability;
                        try {
                            probability = Integer.parseInt(probabilityInput.getText().toString());
                        } catch (NumberFormatException e) {
                            probability = 0; // Invalid input, set to 0
                        }

                        if (probabilityInput.getText().length() > 4) {
                            Toast.makeText(mContext, "Please enter a probability with 4 or fewer digits.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (textInput.length() > 100) {
                            Toast.makeText(mContext, "Sorry, way too big of a wildcard boss man, limited to 100 characters.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        wildcard.setProbability(probability);

                        if (wildcard.isDeletable()) {
                            wildcard.setText(inputText);
                            textViewTitle.setText(wildcard.getText());
                        }

                        textViewProbabilities.setText(String.valueOf(wildcard.getProbability()));
                        setProbabilitySizeBasedOnString(textViewProbabilities, String.valueOf(wildcard.getProbability()));

                        saveWildCardProbabilitiesToStorage(wildCards);
                    }
                });

                builder.show();
            });
        }

    }
}