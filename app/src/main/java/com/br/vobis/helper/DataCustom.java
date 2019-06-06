package com.br.vobis.helper;

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
