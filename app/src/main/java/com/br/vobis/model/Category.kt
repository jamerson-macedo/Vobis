package com.jedev.vobis_admin.models

import com.br.vobis.model.IEntity
import com.google.firebase.Timestamp

data class Category(var name: String?, var createdOn: Timestamp, var subCategories: MutableList<SubCategory>): IEntity {
    override var id: String? = null

    constructor() : this(null, Timestamp.now(), mutableListOf())

    fun addSubCategory(name: String) {
        this.subCategories.add(SubCategory(name))
    }
}