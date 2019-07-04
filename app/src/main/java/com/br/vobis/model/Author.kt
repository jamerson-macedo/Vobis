package com.br.vobis.model

class Author {
    var id: String? = null
    var name: String? = null
    var telefone: String? = null

    constructor()

    constructor(id: String, name: String, telefone: String) : this() {
        this.name = name
        this.id = id
        this.telefone = telefone
    }
}
