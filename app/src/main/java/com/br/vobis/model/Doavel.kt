package com.br.vobis.model

import com.google.firebase.Timestamp

class Doavel : IEntity {

    override var id: String? = null
    var nome: String = ""
    var descricao: String = ""
    var validade: String = ""
    var telefone: String = ""
    var tipo: String = ""
    var status: STATUS = STATUS.PENDENTE
    var localizacao: String = ""
    var dataPublicada: Timestamp = Timestamp.now()
    val fotos: MutableList<String> = mutableListOf()

    constructor()

    constructor(nome: String, descricao: String, validade: String, telefone: String, tipo: String, localizacao: String) : this() {
        this.nome = nome
        this.descricao = descricao
        this.validade = validade
        this.telefone = telefone
        this.tipo = tipo
        this.localizacao = localizacao
    }

    fun addAttach(storeLink: String) {
        this.fotos.add(storeLink)
    }

    enum class STATUS {
        PENDENTE,
        RESOLVIDO
    }
}
