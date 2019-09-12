package com.br.vobis.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Category(
    var name: String = "",
    var createdOn: Timestamp = Timestamp.now(),
    var subCategories: MutableList<SubCategory> = mutableListOf()
) : IEntity {
    override var key: DocumentReference? = null
}
