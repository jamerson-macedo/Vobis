package com.br.vobis.model

import com.google.firebase.Timestamp

class Donation : IEntity {

    override var id: String? = null
    var name: String? = null
    var description: String? = null
    var validity: Timestamp? = null
    var category: String? = null
    var subCategory: String? = null
    var phoneAuthor: String? = null
    var status: STATUS = STATUS.WAITING
    var location: Location? = null
    val attach: MutableList<String> = mutableListOf()
    var createdOn: Timestamp? = Timestamp.now()
    var updatedOn: Timestamp? = null

    constructor()

    constructor(name: String, description: String, validity: Timestamp?, phone: String, category: String, subCategory: String, location: Location) : this() {
        this.name = name
        this.description = description
        this.validity = validity
        this.phoneAuthor = phone
        this.category = category
        this.subCategory = subCategory
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
