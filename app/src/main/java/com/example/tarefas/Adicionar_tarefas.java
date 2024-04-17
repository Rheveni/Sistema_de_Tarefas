package com.example.tarefas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tarefas.model.Tarefas;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tarefas.banco.TarefaDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Adicionar_tarefas extends AppCompatActivity {
    private EditText editTextTitulo;
    private EditText editTextDescricao;
    private EditText editTextDataEntrega;
    private Button botaosalvarTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adicionar_tarefas);


        editTextTitulo = findViewById(R.id.tituloAdd);
        editTextDescricao = findViewById(R.id.descricaoAdd);
        editTextDataEntrega = findViewById(R.id.dataEntrega);
        botaosalvarTarefa = findViewById(R.id.botaosalvarTarefa);

        editTextDataEntrega.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    exibirData();
                }
            }
        });

        botaosalvarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camposPreenchidos()) {
                    salvarTarefa();
                    Intent intent = new Intent(Adicionar_tarefas.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Adicionar_tarefas.this, "Por favor, insira o título e a data.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("TAREFA_ID")) {
            int id = intent.getIntExtra("TAREFA_ID", -1);
            if (id == -1) {
                Log.d("addtarefa", "ID não foi passado corretamente");
            }
            String titulo = intent.getStringExtra("TITULO");
            String descricao = intent.getStringExtra("DESCRICAO");
            String dataEntrega = intent.getStringExtra("DATA_ENTREGA");

            editTextTitulo.setText(titulo);
            editTextDescricao.setText(descricao);
            editTextDataEntrega.setText(dataEntrega);
        }

        FloatingActionButton botaoVoltar = findViewById(R.id.botaoVoltar);
        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Adicionar_tarefas.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean camposPreenchidos() {
        return !editTextTitulo.getText().toString().isEmpty() && !editTextDataEntrega.getText().toString().isEmpty();
    }

    private void salvarTarefa() {
        String titulo = editTextTitulo.getText().toString();
        String descricao = editTextDescricao.getText().toString();
        String dataEntrega = editTextDataEntrega.getText().toString();

        TarefaDbHelper dbHelper = new TarefaDbHelper(this);
        Intent intent = getIntent();

        if (intent.hasExtra("TAREFA_ID")) {
            int id = intent.getIntExtra("TAREFA_ID", -1);
            Tarefas tarefa = new Tarefas(id, titulo, descricao, dataEntrega);
            int rowsAffected = dbHelper.atualizarTarefa(tarefa);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Tarefa atualizada com sucesso!", Toast.LENGTH_SHORT).show();

                editTextTitulo.setText("");
                editTextDescricao.setText("");
                editTextDataEntrega.setText("");
            } else {
                Toast.makeText(this, "Falha ao atualizar tarefa.", Toast.LENGTH_SHORT).show();
            }
        } else {
            long newRowId = dbHelper.inserirTarefa(titulo, descricao, dataEntrega);
            if (newRowId != -1) {
                Toast.makeText(this, "Tarefa inserida com sucesso!", Toast.LENGTH_SHORT).show();
                editTextTitulo.setText("");
                editTextDescricao.setText("");
                editTextDataEntrega.setText("");
            } else {
                Toast.makeText(this, "Falha ao inserir tarefa.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void exibirData() {
        Calendar calendario = Calendar.getInstance();
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Adicionar_tarefas.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(anoSelecionado, mesSelecionado, diaSelecionado);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String dataSelecionada = dateFormat.format(calendar.getTime());
                editTextDataEntrega.setText(dataSelecionada);
            }
        }, ano, mes, dia);
        datePickerDialog.show();
    }

}