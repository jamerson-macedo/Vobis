package com.br.vobis.services

import com.br.vobis.model.IEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*


open class EntityService<T: IEntity>(collection: String) {

    private val firestore = FirebaseFirestore.getInstance()
    val collectionReference = firestore.collection(collection)

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
    }

    fun getAll(): CollectionReference {
        return this.collectionReference
    }

    fun getById(key: String): Task<DocumentSnapshot> {
        return this.collectionReference.document(key).get()
    }

    fun getByParam(param: String, data: Any?): Query {
        return this.collectionReference.whereEqualTo(param, data)
    }

    open fun add(data: T): Task<Void> {
        val newDoc = this.collectionReference.document()

        data.key = newDoc
        return newDoc.set(data)
    }

    fun update(key: String, data: T): Task<Void> {
        val doc = this.collectionReference.document(key)

        data.key = doc
        return doc.set(data)
    }

    fun delete(key: String): Task<Void> {
        return this.collectionReference.document(key).delete()
    }
}
