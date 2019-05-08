package com.jmdevelopers.workproject.services

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class EntityService(entity: String) {
    // Static attr
    companion object {
        val firebase = FirebaseDatabase.getInstance().reference
    }

    var api: DatabaseReference

    init {
        this.api = firebase.child(entity)
    }
}