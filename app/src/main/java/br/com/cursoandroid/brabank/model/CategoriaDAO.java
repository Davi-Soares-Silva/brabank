package br.com.cursoandroid.brabank.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.cursoandroid.brabank.model.entity.Categoria;

public class CategoriaDAO {

    public DBHelper dbHelper;
    public SQLiteDatabase db;

    public CategoriaDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean inserir(Categoria categoria) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tipo", categoria.tipo);
        values.put("nome", categoria.nome);

        return db.insert(DBHelper.TABELA_CATEGORIAS, null, values) > 0;
    }


    public boolean editar(Categoria categoria) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", categoria.nome);
        values.put("tipo", categoria.tipo);

        String [] args = {String.valueOf(categoria.codigo)};

        return db.update(DBHelper.TABELA_CATEGORIAS, values, "codigo = ?", args) > 0;
    }


    public boolean excluir(Categoria categoria) {
        db = dbHelper.getWritableDatabase();
        String[] args = {String.valueOf(categoria.codigo)};
        return db.delete(DBHelper.TABELA_CATEGORIAS, "codigo = ?", args) > 0;

    }

    public Categoria buscarPorCodigo(Integer codigo) {
        db = dbHelper.getReadableDatabase();
        String[] args = {codigo.toString()};
        Cursor cursor = db.query(DBHelper.TABELA_LANCAMENTOS, null, "codigo = ?", args, null, null, null);
        if (cursor.moveToNext()){
            Categoria c = new Categoria(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("nome"))
            );
            return c;
        }
        return null;
    }

    public List listarTodos() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABELA_CATEGORIAS, null, null, null, null, null, "nome ASC");
        return toList(cursor);
    }

    private List<Categoria> toList(Cursor cursor) {
        List<Categoria> categorias = new ArrayList<>();

        while (cursor.moveToNext()) {
            Categoria c = new Categoria(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("nome"))
            );
            categorias.add(c);
        }
        return categorias;
    }


    public List<Categoria> buscarPorTipo(String tipo) {
        List<Categoria> categorias = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String[] args = {tipo};
        Cursor cursor = db.query(DBHelper.TABELA_CATEGORIAS, null, "tipo = ?", args, null, null, "nome ASC");
        while(cursor.moveToNext()) {
            Categoria c = new Categoria(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("nome"))
            );
            categorias.add(c);
        }
        return categorias;
    }


    public List<Categoria> buscarPorNome(String nome){
        List<Categoria> categorias = new ArrayList<>();

        db = dbHelper.getReadableDatabase();
        String[] args = {nome};
        Cursor cursor = db.query(DBHelper.TABELA_CATEGORIAS, null, "nome LIKE ?", args, null, null, "nome ASC");

        while (cursor.moveToNext()){
            Categoria c = new Categoria(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("nome"))
            );
            categorias.add(c);
        }
        return categorias;
    }
}
