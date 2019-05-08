package com.jmdevelopers.workproject.model

import com.jmdevelopers.workproject.services.DoacaoService
import java.util.*

class Doavel() {

    var nome: String? = null
    var descricao: String? = null
    var validade: String? = null
    var telefone: String? = null
    var tipo: String? = null
    var dataPublicada: String? = null
    var localizacao: String? = null
    val fotos: ArrayList<String>? = arrayListOf()

    constructor(nome: String, descricao: String, validade: String, telefone: String, tipo: String, localizacao: String) : this() {
        this.nome = nome
        this.descricao = descricao
        this.validade = validade
        this.telefone = telefone
        this.tipo = tipo
        this.localizacao = localizacao
    }

    fun salvar() {
        DoacaoService().api.push().setValue(this)
    }
}
