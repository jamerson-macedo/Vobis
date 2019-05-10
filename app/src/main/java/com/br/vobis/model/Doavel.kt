package com.br.vobis.model

import com.br.vobis.helper.DataCustom
import com.br.vobis.services.DoacaoService
import java.util.*

class Doavel() {

    var nome: String? = null
    var descricao: String? = null
    var validade: String? = null
    var telefone: String? = null
    var tipo: String? = null
    var statusdoacao: String? = null
    var dataPublicada: String? = null
    var localizacao: String? = null
    val fotos: ArrayList<String>? = arrayListOf()

    constructor(nome: String, descricao: String, validade: String, telefone: String, tipo: String, localizacao: String) : this() {
        this.nome = nome
        this.descricao = descricao
        this.validade = validade
        this.telefone = telefone
        this.tipo = tipo
        this.statusdoacao = "Em espera..."
        this.localizacao = localizacao
        this.dataPublicada = DataCustom.dataAtual()
    }

    fun addAttach(storeLink: String) {
        this.fotos?.add(storeLink)
    }

    fun save() {
        DoacaoService().api.push().setValue(this)
    }
}
