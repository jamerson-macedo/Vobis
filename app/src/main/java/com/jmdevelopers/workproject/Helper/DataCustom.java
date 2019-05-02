package com.jmdevelopers.workproject.Helper;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

public class DataCustom {


    public static String dataAtual() {

        long data = System.currentTimeMillis();
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        Log.i("dataatual",dataString);
        return dataString;

    }
}
