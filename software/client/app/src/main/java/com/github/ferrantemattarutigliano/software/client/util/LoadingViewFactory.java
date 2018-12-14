package com.github.ferrantemattarutigliano.software.client.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;

public class LoadingViewFactory {
    public LinearLayout create(Context context){
        return create(context, "");
    }

    public LinearLayout create(Context context, String message){
        LinearLayout layout = buildLayout(context);
        ProgressBar progressBar = buildProgressBar(context);
        TextView textView = buildTextView(context, message);
        assembleParts(layout, progressBar, textView);
        return layout;
    }

    private LinearLayout buildLayout(Context context){
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
        return layout;
    }

    private ProgressBar buildProgressBar(Context context){
        return new ProgressBar(context);
    }

    private TextView buildTextView(Context context, String message){
        TextView textMessage = new TextView(context);
        int textColor = ContextCompat.getColor(context, R.color.colorWhite);
        textMessage.setText(message);
        textMessage.setTextColor(textColor);
        textMessage.setGravity(Gravity.CENTER);
        return textMessage;
    }

    private void assembleParts(ViewGroup layout, ProgressBar progressBar, TextView textView){
        progressBar.setX(layout.getX()/2);
        progressBar.setY(layout.getY()/2);
        textView.setX(progressBar.getX());
        textView.setY(progressBar.getY()+10);

        layout.addView(progressBar);
        layout.addView(textView);
        layout.bringChildToFront(progressBar);
        layout.bringChildToFront(textView);
    }
}
