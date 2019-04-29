package com.jmdevelopers.workproject.Helper;

import android.icu.text.SimpleDateFormat;

public class DataCustom {


    public static String dataAtual() {

        long data = System.currentTimeMillis();
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;

    }
}
