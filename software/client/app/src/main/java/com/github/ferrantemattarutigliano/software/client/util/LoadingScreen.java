package com.github.ferrantemattarutigliano.software.client.util;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class LoadingScreen {
    private LinearLayout loadingScreen;
    private ViewGroup viewGroup;
    private boolean isShowing;

    public LoadingScreen(ViewGroup viewGroup) {
        this(viewGroup, "");
    }

    public LoadingScreen(ViewGroup viewGroup, String message) {
        buildLoadingScreen(viewGroup, message);
        this.viewGroup = viewGroup;
    }

    public void show(){
        if(isShowing) return;
        viewGroup.addView(loadingScreen);
        isShowing = true;
    }

    public void hide(){
        if(!isShowing) return;
        viewGroup.removeView(loadingScreen);
        isShowing = false;
    }

    public void changeMessage(@NonNull String message){
        buildLoadingScreen(viewGroup, message);
        if(isShowing) show();
    }

    public boolean isShowing() {
        return isShowing;
    }

    private void buildLoadingScreen(ViewGroup viewGroup, String message){
        LoadingViewFactory loadingViewFactory = new LoadingViewFactory();
        loadingScreen = loadingViewFactory.create(viewGroup.getContext(), message);
    }
}
