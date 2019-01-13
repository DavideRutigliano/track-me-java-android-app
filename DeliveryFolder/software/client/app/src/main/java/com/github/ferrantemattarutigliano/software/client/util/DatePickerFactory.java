package com.github.ferrantemattarutigliano.software.client.util;

import android.content.Context;
import android.widget.DatePicker;

public class DatePickerFactory {
    private DatePicker datePicker;

    public DatePickerFactory() {

    }

    public void create(Context context){
        datePicker = new DatePicker(context);
    }
}
