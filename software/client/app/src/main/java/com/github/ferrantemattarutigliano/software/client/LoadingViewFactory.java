package com.github.ferrantemattarutigliano.software.client;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingViewFactory {
    public LinearLayout create(Context context, @Nullable String message){
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setClickable(true);
        layout.bringToFront();
        int layoutColor = ContextCompat.getColor(context, R.color.colorSoftBlueTransparent);
        layout.setBackgroundColor(layoutColor);

        ProgressBar progressBar = new ProgressBar(context);
        TextView textMessage = new TextView(context);
        layout.addView(progressBar);
        layout.addView(textMessage);

        progressBar.getLayoutParams().width = 200;
        progressBar.getLayoutParams().height = 200;
        progressBar.invalidate(); //redraw progress bar with changes
        progressBar.setX(layout.getX()/2);
        progressBar.setY(layout.getY()/2);

        textMessage.setText(message);
        int textColor = ContextCompat.getColor(context, R.color.colorWhite);
        textMessage.setTextColor(textColor);
        textMessage.setGravity(Gravity.CENTER);
        textMessage.setX(progressBar.getX());
        textMessage.setY(progressBar.getY()+10);

        layout.bringChildToFront(progressBar);
        layout.bringChildToFront(textMessage);
        return layout;
    }
}
