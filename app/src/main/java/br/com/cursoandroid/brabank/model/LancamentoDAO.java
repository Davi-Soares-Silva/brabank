package br.com.cursoandroid.brabank.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.cursoandroid.brabank.model.entity.Categoria;
import br.com.cursoandroid.brabank.model.entity.Lancamento;

public class LancamentoDAO {
    public DBHelper dbHelper;
    public SQLiteDatabase db;
    public Context contexto;

    public LancamentoDAO(Context context) {
        dbHelper = new DBHelper(context);
        contexto = context;
    }

    private static List<Lancamento> lancamentos = new ArrayList<>();

    public boolean inserir(Lancamento lancamento) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tipo", lancamento.tipo);
        values.put("descricao", lancamento.descricao);
        values.put("valor", lancamento.valor);
        values.put("categoria", lancamento.categoria.codigo);
        values.put("data", lancamento.data.getTime());

        return db.insert(DBHelper.TABELA_LANCAMENTOS, null, values) > 0;

    }


    public boolean editar(Lancamento lancamento) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tipo", lancamento.tipo);
        values.put("descricao", lancamento.descricao);
        values.put("valor", lancamento.valor);
        values.put("categoria", lancamento.categoria.toString());
        values.put("data", lancamento.data.getTime());

        String[] args = {String.valueOf(lancamento.codigo)};

        return db.update(DBHelper.TABELA_LANCAMENTOS, values, "codigo = ?", args) > 0;
    }

    public boolean excluir(Lancamento lancamento) {
        db = dbHelper.getWritableDatabase();
        String[] args = {String.valueOf(lancamento.codigo)};

        return db.delete(DBHelper.TABELA_LANCAMENTOS, "codigo = ?", args) > 0;
    }

    public List<Lancamento> listarTodos() {

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABELA_LANCAMENTOS, null, null, null, null, null, null, null);
        return toList(cursor);
    }

    private List<Lancamento> toList(Cursor cursor) {
        List<Lancamento> lancamentos = new ArrayList<>();


        while (cursor.moveToNext()) {

            Long sData = cursor.getLong(cursor.getColumnIndex("data"));
            Date data = new Date();
            data.setTime(sData);
            CategoriaDAO cDAO = new CategoriaDAO(contexto);
            Categoria c = cDAO.buscarPorCodigo(cursor.getColumnIndex("categoria"));

            Lancamento l = new Lancamento(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("descricao")),
                    cursor.getDouble(cursor.getColumnIndex("valor")),
                    c,
                    data
            );
            lancamentos.add(l);
        }
        return lancamentos;
    }

    public Lancamento buscarPorCodigo(Integer codigo) {
        db = dbHelper.getReadableDatabase();
        String[] args = {codigo.toString()};
        Cursor cursor = db.query(DBHelper.TABELA_LANCAMENTOS, null, "codigo = ?", args, null, null, null);

        if (cursor.moveToNext()) {
            Long sData = cursor.getLong(cursor.getColumnIndex("data"));
            Date data = new Date();
            data.setTime(sData);
            CategoriaDAO cDAO = new CategoriaDAO(contexto);
            Categoria c = cDAO.buscarPorCodigo(cursor.getColumnIndex("categoria"));

            Lancamento l = new Lancamento(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("descricao")),
                    cursor.getDouble(cursor.getColumnIndex("valor")),
                    c,
                    data
            );
            return l;
        }
        return null;
    }

    public List<Lancamento> buscarPorTipo(String tipo) {
        List<Lancamento> lancamentos = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String[] args = {tipo};
        Cursor cursor = db.query(DBHelper.TABELA_LANCAMENTOS, null, "tipo = ?", args, null, null, null);
        while (cursor.moveToNext()) {
            Long sData = cursor.getLong(cursor.getColumnIndex("data"));
            Date data = new Date();
            data.setTime(sData);
            CategoriaDAO cDAO = new CategoriaDAO(contexto);
            Categoria c = cDAO.buscarPorCodigo(cursor.getColumnIndex("categoria"));

            Lancamento l = new Lancamento(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("descricao")),
                    cursor.getDouble(cursor.getColumnIndex("valor")),
                    c,
                    data
            );
            lancamentos.add(l);
        }
        return lancamentos;
    }

    public List<Lancamento> buscarPorCategoria(Categoria categoria) {
        List<Lancamento> lancamentos = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String[] args = {categoria.codigo.toString()};
        Cursor cursor = db.query(DBHelper.TABELA_LANCAMENTOS, null, "categoria = ?", args, null, null, null);
        while (cursor.moveToNext()) {
            Long sData = cursor.getLong(cursor.getColumnIndex("data"));
            Date data = new Date();
            data.setTime(sData);
            CategoriaDAO cDAO = new CategoriaDAO(contexto);
            Categoria c = cDAO.buscarPorCodigo(cursor.getColumnIndex("categoria"));

            Lancamento l = new Lancamento(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("descricao")),
                    cursor.getDouble(cursor.getColumnIndex("valor")),
                    c,
                    data
            );
            lancamentos.add(l);
        }
        return lancamentos;
    }
}

