package com.example.tarefas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarefas.adapter.TarefasAdapter;
import com.example.tarefas.banco.TarefaDbHelper;
import com.example.tarefas.model.Tarefas;

import java.util.ArrayList;

public class Tarefas_item extends AppCompatActivity {

    private TarefaDbHelper dbHelper;
    private ArrayList<Tarefas> listaDeTarefa;
    private TarefasAdapter adapter;
    private Button botaoEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarefas_item);

        dbHelper = new TarefaDbHelper(this);
        listaDeTarefa = new ArrayList<>();
        listaDeTarefa.addAll(dbHelper.obterTodasTarefas());
        adapter = new TarefasAdapter(listaDeTarefa, this);



        ImageButton imageButtonEditar = findViewById(R.id.imageButtonEditar);
        imageButtonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ImageButton", "ImageButton clicado");
                Intent intent = new Intent(Tarefas_item.this, Editar_tarefa.class);
                startActivity(intent);
            }
        });
    }

    private long obterIdDaTarefaSelecionada() {
        int posicaoSelecionada = adapter.getPosicaoItemSelecionado();
        if (posicaoSelecionada != RecyclerView.NO_POSITION) {
            Tarefas tarefaSelecionada = listaDeTarefa.get(posicaoSelecionada);
            return tarefaSelecionada.getId();
        } else {
            return -1;
        }
    }

}