package br.com.cursoandroid.brabank.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "Brabank";
    public static final String TABELA_CATEGORIAS = "tbl_categorias";
    public static final String TABELA_LANCAMENTOS = "tbl_lancamentos";
    private static final int VERSAO = 5;

    public DBHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA_CATEGORIAS + "(" +
                "codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tipo TEXT NOT NULL," +
                "nome TEXT NOT NULL" +
                ")";

        db.execSQL(sql);
        Log.i("BANCO", "OnCreate foi chamado com sucesso");

        String sqlLancamentos = "CREATE TABLE " + TABELA_LANCAMENTOS + "(" +
                "codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tipo TEXT NOT NULL," +
                "descricao TEXT NOT NULL," +
                "valor REAL NOT NULL," +
                "categoria INTEGER NOT NULL," +
                "data TEXT NOT NULL" +
                ");";

        db.execSQL(sqlLancamentos);
        Log.i("BANCO", "OnCreate chamado com sucesso, tabela de lançamentos criada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABELA_CATEGORIAS;
        String sqlLancamentos = "DROP TABLE IF EXISTS " + TABELA_LANCAMENTOS;
        db.execSQL(sql);
        db.execSQL(sqlLancamentos);

        Log.i("BANCO", "OnUpgrade chamado com sucesso, tabelas dropadas : Versão " + VERSAO);

        onCreate(db);
    }
}
