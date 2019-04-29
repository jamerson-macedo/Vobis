package com.jmdevelopers.workproject.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jmdevelopers.workproject.Helper.DataCustom;
import com.jmdevelopers.workproject.Model.Doavel;
import com.jmdevelopers.workproject.R;


public class DoarFragment extends Fragment {
    private EditText nome, telefone, localizacao, tipo, validade, descricao;
    private ImageView foto1, foto2;
    Doavel doavel;

    public DoarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doar, container, false);


        Button cadastrar;
        nome = view.findViewById(R.id.doarnome);
        telefone = view.findViewById(R.id.doartelefone);
        localizacao = view.findViewById(R.id.doarloc);
        tipo = view.findViewById(R.id.doartipo);
        validade = view.findViewById(R.id.doarvalidade);
        descricao = view.findViewById(R.id.doardescricao);
        foto1 = view.findViewById(R.id.doarfoto1);
        foto2 = view.findViewById(R.id.doarfoto2);
        cadastrar = view.findViewById(R.id.doarcadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String textoNome = nome.getText().toString();
                String textotelefone = telefone.getText().toString();
                String textolocalizacao = localizacao.getText().toString();
                String textotipo = tipo.getText().toString();
                String textovalidade = validade.getText().toString();
                String textodescricao = descricao.getText().toString();

                if (textoNome.isEmpty() || textotelefone.isEmpty() || textolocalizacao.isEmpty() || textotipo.isEmpty() || textovalidade.isEmpty() || textodescricao.isEmpty()) {
                    mensagemdeErro("preencha os camopos");
                } else {
                    doavel.setDataPublicada(DataCustom.dataAtual());
                    doavel.setDescricao(textodescricao);
                    doavel.setLocalizacao(textolocalizacao);
                    doavel.setTelefone(textotelefone);
                    doavel.setNome(textoNome);
                    doavel.setTipo(textotipo);
                    doavel.setValidade(textovalidade);
                    doavel.salvar();

                }
            }
        });


        return view;

    }

    private void mensagemdeErro(String erro) {
        Toast.makeText(getActivity(), erro, Toast.LENGTH_LONG).show();

    }
}

