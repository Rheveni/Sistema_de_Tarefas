package com.example.tarefas.banco;

import static com.example.tarefas.banco.TarefasContrato.TarefaEntry.TABELA_TAREFAS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tarefas.model.Tarefas;

import java.util.ArrayList;
import java.util.List;

public class TarefaDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ListaTarefas.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABELA_TAREFAS + " (" +
                    TarefasContrato.TarefaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TarefasContrato.TarefaEntry.COLUMN_TITULO + " TEXT," +
                    TarefasContrato.TarefaEntry.COLUMN_DESCRICAO + " TEXT," +
                    TarefasContrato.TarefaEntry.COLUMN_DATA_ENTREGA + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABELA_TAREFAS;

    public TarefaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long inserirTarefa(String titulo, String descricao, String dataEntrega) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TarefasContrato.TarefaEntry.COLUMN_TITULO, titulo);
        values.put(TarefasContrato.TarefaEntry.COLUMN_DESCRICAO, descricao);
        values.put(TarefasContrato.TarefaEntry.COLUMN_DATA_ENTREGA, dataEntrega);

        long newRowId = db.insert(TABELA_TAREFAS, null, values);
        db.close();



        return newRowId;
    }

    public List<Tarefas> obterTodasTarefas() {
        List<Tarefas> tarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] projection = {
                    TarefasContrato.TarefaEntry._ID,
                    TarefasContrato.TarefaEntry.COLUMN_TITULO,
                    TarefasContrato.TarefaEntry.COLUMN_DESCRICAO,
                    TarefasContrato.TarefaEntry.COLUMN_DATA_ENTREGA
            };

            cursor = db.query(
                    TABELA_TAREFAS,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(TarefasContrato.TarefaEntry._ID));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(TarefasContrato.TarefaEntry.COLUMN_TITULO));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow(TarefasContrato.TarefaEntry.COLUMN_DESCRICAO));
                String dataEntrega = cursor.getString(cursor.getColumnIndexOrThrow(TarefasContrato.TarefaEntry.COLUMN_DATA_ENTREGA));
                Tarefas tarefa = new Tarefas((int) id, titulo, descricao, dataEntrega);
                tarefas.add(tarefa);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return tarefas;
    }

    public int atualizarTarefa(Tarefas tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TarefasContrato.TarefaEntry.COLUMN_TITULO, tarefa.getTitulo());
        values.put(TarefasContrato.TarefaEntry.COLUMN_DESCRICAO, tarefa.getDescricao());
        values.put(TarefasContrato.TarefaEntry.COLUMN_DATA_ENTREGA, tarefa.getDataEntrega());

        String selection = TarefasContrato.TarefaEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(tarefa.getId())};

        int rowsAffected = db.update(TarefasContrato.TarefaEntry.TABELA_TAREFAS, values, selection, selectionArgs);

        db.close();
        return rowsAffected;
    }


    public boolean excluirTarefa(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = TarefasContrato.TarefaEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        int rowsDeleted = db.delete(TABELA_TAREFAS, selection, selectionArgs);
        db.close();

        return rowsDeleted > 0;
    }
}
