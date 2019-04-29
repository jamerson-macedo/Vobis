package com.jmdevelopers.workproject.Model;

import com.google.firebase.database.DatabaseReference;
import com.jmdevelopers.workproject.Config.ConfiguracaoFirebase;

public class Doavel {
    private String nome;
    private String foto;
    private String foto2;

    private String descricao;
    private String validade;
    private String telefone;
    private String tipo;
    private String dataPublicada;
    private String Localizacao;
    public void salvar(){

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("doacao")
                .push()
                .setValue(this);

    }
    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDataPublicada() {
        return dataPublicada;
    }

    public void setDataPublicada(String dataPublicada) {
        this.dataPublicada = dataPublicada;
    }

    public String getLocalizacao() {
        return Localizacao;
    }

    public void setLocalizacao(String localizacao) {
        Localizacao = localizacao;
    }

    public Doavel() {
    }
}
