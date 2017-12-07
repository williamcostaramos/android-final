package br.com.adsnoobs.cadastroUsuario;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by williamramos
 */

public class Usuario {

    private int codigo;
    private String nome;
    private String email;
    private String senha;
    private byte[] imagem;
    private Bitmap avatar;
    private String urlGravatar;
    private boolean excluir;
    private Context context;

    public Usuario(Context context){
        this.context = context;
        codigo = -1;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.urlGravatar = String.format("https://s.gravatar.com/avatar/%s?s=200",Auxilio.md5Hex(this.email));
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
        if (this.imagem != null)
            this.avatar = Auxilio.getImagemBytes(this.imagem);
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getUrlGravatar() {
        return urlGravatar;
    }

    public void setUrlGravatar(String urlGravatar) {
        this.urlGravatar = urlGravatar;
    }

    public boolean isExcluir() {
        return excluir;
    }

    public void setExcluir(boolean excluir) {
        this.excluir = excluir;
    }

    public boolean excluir(){
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        try{
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();

            sqLiteDatabase.delete("usuario","codigo = ?",new String[]{String.valueOf(codigo)});

            excluir = true;

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            sqLiteDatabase.endTransaction();
            return false;
        }finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
            if (dbHelper != null)
                dbHelper.close();
        }
    }

    public boolean salvar(){
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        try{
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getWritableDatabase();
            String sql = "";
            if (codigo == -1){
                sql = "INSERT INTO usuario (nome,email,senha,imagem) VALUES (?,?,?,?)";
            }else{
                sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, imagem = ? WHERE codigo = ?";
            }
            sqLiteDatabase.beginTransaction();
            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindString(1,nome);
            sqLiteStatement.bindString(2,email);
            sqLiteStatement.bindString(3,senha);
            if (imagem != null)
                sqLiteStatement.bindBlob(4,imagem);
            if (codigo != -1)
                sqLiteStatement.bindString(5,String.valueOf(codigo));
            sqLiteStatement.executeInsert();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            sqLiteDatabase.endTransaction();
            return false;
        }finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
            if (dbHelper != null)
                dbHelper.close();
        }
    }

    public ArrayList<Usuario> getUsuarios(){
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try{
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getReadableDatabase();
            cursor = sqLiteDatabase.query("usuario",null,null,null,null,null,null);
            while (cursor.moveToNext()){
                Usuario usuario = new Usuario(context);
                usuario.codigo = cursor.getInt(cursor.getColumnIndex("codigo"));
                usuario.nome = cursor.getString(cursor.getColumnIndex("nome"));
                usuario.senha = cursor.getString(cursor.getColumnIndex("senha"));
                usuario.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                usuario.setImagem(cursor.getBlob(cursor.getColumnIndex("imagem")));
                usuarios.add(usuario);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if ((cursor != null) && (!cursor.isClosed()))
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
            if (dbHelper != null)
                dbHelper.close();
        }
        return usuarios;
    }

    public void carregaUsuarioPeloCodigo(int codigo){
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try{
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getReadableDatabase();
            cursor = sqLiteDatabase.query("usuario",null,"codigo = ?",new String[]{String.valueOf(codigo)},null,null,null);
            excluir = true;
            while (cursor.moveToNext()){
                this.codigo = cursor.getInt(cursor.getColumnIndex("codigo"));
                nome = cursor.getString(cursor.getColumnIndex("nome"));
                senha = cursor.getString(cursor.getColumnIndex("senha"));
                setEmail(cursor.getString(cursor.getColumnIndex("email")));
                setImagem(cursor.getBlob(cursor.getColumnIndex("imagem")));
                excluir = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if ((cursor != null) && (!cursor.isClosed()))
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
            if (dbHelper != null)
                dbHelper.close();
        }
    }
}
