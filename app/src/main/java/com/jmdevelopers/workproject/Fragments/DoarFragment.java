package com.jmdevelopers.workproject.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.jmdevelopers.workproject.Helper.DatePickerFragment;
import com.jmdevelopers.workproject.R;
import com.jmdevelopers.workproject.model.Doavel;

import java.text.DateFormat;
import java.util.Calendar;


public class DoarFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private TextInputLayout nome, telefone, localizacao, tipo, descricao;
    private EditText validade;
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
                String validade = DoarFragment.this.validade.getText().toString();
                String descricao = DoarFragment.this.descricao.getEditText().getText().toString();

                if (nome.isEmpty() || telefone.isEmpty() || localizacao.isEmpty() || tipo.isEmpty() || validade.isEmpty() || descricao.isEmpty()) {
                    mensagemdeErro("preencha os camopos");
                } else {
                    doavel = new Doavel(nome, descricao, validade, telefone, tipo, localizacao);
                    doavel.salvar();

                }
            }
        });

        validade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.setTargetFragment(DoarFragment.this, 0);
                    datePicker.show(getFragmentManager(), "date picker");
                }
            }
        });

        return view;

    }

    private void mensagemdeErro(String erro) {
        Toast.makeText(getActivity(), erro, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String dataescolhida = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        Log.i("dataescoli", dataescolhida);
        validade.setText(dataescolhida);
    }
}

