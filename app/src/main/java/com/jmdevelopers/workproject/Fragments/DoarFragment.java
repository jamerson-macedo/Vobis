package com.jmdevelopers.workproject.Fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jmdevelopers.workproject.R;
import com.jmdevelopers.workproject.model.Doavel;


public class DoarFragment extends Fragment {
    private TextInputLayout nome, telefone, localizacao, tipo, validade, descricao;
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
        cadastrar = view.findViewById(R.id.doarcadastrar);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = DoarFragment.this.nome.getEditText().getText().toString();
                String telefone = DoarFragment.this.telefone.getEditText().getText().toString();
                String localizacao = DoarFragment.this.localizacao.getEditText().getText().toString();
                String tipo = DoarFragment.this.tipo.getEditText().getText().toString();
                String validade = DoarFragment.this.validade.getEditText().getText().toString();
                String descricao = DoarFragment.this.descricao.getEditText().getText().toString();

                if (nome.isEmpty() || telefone.isEmpty() || localizacao.isEmpty() || tipo.isEmpty() || validade.isEmpty() || descricao.isEmpty()) {
                    mensagemdeErro("preencha os camopos");
                } else {
                    doavel = new Doavel(nome, descricao, validade, telefone, tipo, localizacao);
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

