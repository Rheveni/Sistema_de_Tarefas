package com.example.tarefas.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TarefaDataSource{
    private SQLiteDatabase database;
    private TarefaDbHelper dbHelper;

    public TarefaDataSource(Context context) {
        dbHelper = new TarefaDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long inserirTarefa(String titulo, String descricao, String dataEntrega) {
        ContentValues values = new ContentValues();
        values.put(TarefasContrato.TarefaEntry.COLUMN_TITULO, titulo);
        values.put(TarefasContrato.TarefaEntry.COLUMN_DESCRICAO, descricao);
        values.put(TarefasContrato.TarefaEntry.COLUMN_DATA_ENTREGA, dataEntrega);

        return database.insert(TarefasContrato.TarefaEntry.TABELA_TAREFAS, null, values);
    }
}