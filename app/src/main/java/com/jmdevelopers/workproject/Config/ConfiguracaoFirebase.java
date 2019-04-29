package com.jmdevelopers.workproject.Config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {



    private static DatabaseReference firebase;

    //retorna a instancia do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        if ( firebase == null ){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

}
