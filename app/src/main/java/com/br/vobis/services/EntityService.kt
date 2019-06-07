package com.br.vobis.services

import com.br.vobis.model.IEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot


open class EntityService<T : IEntity>(collection: String) {

    private val firestore = FirebaseFirestore.getInstance()
    val collectionReference = firestore.collection(collection)

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
    }

    fun getAll(): Task<QuerySnapshot> {
        return this.collectionReference.get()
    }

    fun getById(id: String): Task<DocumentSnapshot> {
        return this.collectionReference.document(id).get()
    }

    fun add(data: T): Task<DocumentSnapshot> {
        val newDoc = this.collectionReference.document()
        data.id = newDoc.id
        newDoc.set(data)
        return newDoc.get()
    }

    fun update(id: String, data: T) {
        this.collectionReference.document(id).set(data)
    }

    fun delete(id: String) {
        this.collectionReference.document(id).delete()
    }
}