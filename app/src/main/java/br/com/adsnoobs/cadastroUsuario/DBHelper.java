package br.com.adsnoobs.cadastroUsuario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by williamramos
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String NOME = "sqliteappexemplo.db";
    private static int VERSAO = 1;

    public DBHelper(Context context){
        super(context,NOME,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE [usuario] (\n" +
            "[codigo] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "[nome] VARCHAR(60)  NOT NULL,\n" +
            "[email] VARCHAR(60)  NOT NULL,\n" +
            "[senha] VARCHAR(60)  NOT NULL,\n" +
            "[imagem] BLOB  NULL\n" +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
