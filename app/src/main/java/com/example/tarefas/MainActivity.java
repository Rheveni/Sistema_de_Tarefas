package com.example.tarefas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tarefas.banco.TarefaDbHelper;
import com.example.tarefas.adapter.TarefasAdapter;
import com.example.tarefas.databinding.ActivityMainBinding;

import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TarefasAdapter tarefasAdapter;
    private ArrayList<com.example.tarefas.model.Tarefas> Tarefas = new ArrayList<>();
    private RecyclerView recyclerView;
    private TarefasAdapter adapter;
    private TarefaDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        recyclerView = binding.RecyclerViewTarefas;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new TarefaDbHelper(this);

        List<com.example.tarefas.model.Tarefas> Tarefas = dbHelper.obterTodasTarefas();

        adapter = new TarefasAdapter((ArrayList<com.example.tarefas.model.Tarefas>)Tarefas, this);

        recyclerView.setAdapter(adapter);


        Button addBotao = findViewById(R.id.addBotao);
        addBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Adicionar_tarefas.class);
                startActivity(intent);
            }
        });
        EditText tituloEditText = findViewById(R.id.tituloAdd);
        EditText descricaoEditText = findViewById(R.id.descricaoAdd);
        EditText dataEntregaEditText = findViewById(R.id.dataEntrega);

    }
}