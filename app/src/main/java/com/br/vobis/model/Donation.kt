package com.br.vobis.model

import com.google.firebase.Timestamp

class Donation : IEntity {

    override var id: String? = null
    var author: String? = null
    var description: String? = null
    var validity: Timestamp? = null
    var category: String? = null
    var subCategory: String? = null
    var phone: String? = null
    var type: String? = null
    var photo: String? = null
    var status: STATUS = STATUS.WAITING
    var location: LocationVobis? = null
    val attach: MutableList<String> = mutableListOf()
    var updatedOn: Timestamp? = Timestamp.now()

    constructor()

    constructor(author: String, description: String, validity: Timestamp, phone: String, type: String, location: LocationVobis) : this() {
        this.author = author
        this.description = description
        this.validity = validity
        this.phone = phone
        this.type = type
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

/*fun addAttachphoto(storeLink: String) {
   //this.photo.add(storeLink)
}*/

