package com.br.vobis.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class Donation : IEntity {
    override var key: DocumentReference? = null
    var author: String? = null
    var name: String? = null
    var description: String? = null
    var validity: Timestamp? = null
    var category: String? = null
    var subCategory: String? = null
    var phone: String? = null
    var status: STATUS = STATUS.WAITING
    var memberResponsible: DocumentReference? = null
    var instituteResponsible: DocumentReference? = null
    var location: LocationVobis? = null
    val attach: MutableList<String> = mutableListOf()
    var active: Boolean = true
    var createdOn: Timestamp = Timestamp.now()
    var updatedOn: Timestamp? = null

    constructor()

    constructor(
            author: String,
            name: String,
            description: String,
            validity: Timestamp?,
            phone: String,
            location: LocationVobis
    ) : this() {
        this.author = author
        this.name = name
        this.description = description
        this.validity = validity
        this.phone = phone
        this.location = location
    }

    fun addAttach(storeLink: String) {
        this.attach.add(storeLink)
    }

    enum class STATUS {
        WAITING,
        PENDENT,
        RESOLVED
    }
}
