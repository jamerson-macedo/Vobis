package com.br.vobis.model

class Category() : IEntity {
    override var id: String? = null
    lateinit var name: String

    constructor(name: String) : this() {
        this.name = name
    }
}