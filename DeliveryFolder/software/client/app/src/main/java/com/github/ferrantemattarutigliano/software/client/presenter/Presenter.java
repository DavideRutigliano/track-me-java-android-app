package com.github.ferrantemattarutigliano.software.client.presenter;

public abstract class Presenter<T> {
    protected T view;

    public Presenter(T view) {
        this.view = view;
    }
}
