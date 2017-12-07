package br.com.adsnoobs.sqliteappexemplo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.security.spec.ECField;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonExcluir;
    private Button buttonSalvar;
    private Button buttonCancelar;
    private ImageView imageView;

    private final Usuario usuario = new Usuario(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextNome = (EditText)findViewById(R.id.editTextNome);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextSenha = (EditText)findViewById(R.id.editTextSenha);
        buttonExcluir = (Button)findViewById(R.id.buttonExcluir);
        buttonCancelar = (Button)findViewById(R.id.buttonCancelar);
        buttonSalvar = (Button)findViewById(R.id.buttonSalvar);
        imageView = (ImageView)findViewById(R.id.imageViewAvatar);

        buttonExcluir.setOnClickListener(this);
        buttonSalvar.setOnClickListener(this);
        buttonCancelar.setOnClickListener(this);

        if (getIntent().getExtras() != null){
            setTitle(getString(R.string.titulo_editando));
            int codigo = getIntent().getExtras().getInt("consulta");
            usuario.carregaUsuarioPeloCodigo(codigo);

            if (usuario.getAvatar() != null)
                imageView.setImageBitmap(usuario.getAvatar());
            editTextNome.setText(usuario.getNome().toString());
            editTextEmail.setText(usuario.getEmail().toString());
            editTextSenha.setText(usuario.getSenha().toString());
        }else{
            setTitle(getString(R.string.titulo_incluindo));
        }

        buttonExcluir.setEnabled(true);
        if (usuario.getCodigo() == -1)
            buttonExcluir.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonCancelar : {
                finish();
                break;
            }
            case R.id.buttonExcluir : {
                usuario.excluir();
                finish();
                break;
            }
            case R.id.buttonSalvar :{
                boolean valido = true;
                usuario.setNome(editTextNome.getText().toString().trim());
                usuario.setEmail(editTextEmail.getText().toString().trim().toLowerCase());
                usuario.setSenha(editTextSenha.getText().toString().trim());
                carregaImagem();

                if (usuario.getNome().equals("")){
                    editTextNome.setError(getString(R.string.obrigatorio));
                    valido = false;
                }

                if (usuario.getEmail().equals("")){
                    editTextEmail.setError(getString(R.string.obrigatorio));
                    valido = false;
                }

                if (usuario.getSenha().equals("")){
                    editTextSenha.setError(getString(R.string.obrigatorio));
                    valido = false;
                }

                if (valido){
                    usuario.salvar();
                    finish();
                }
                break;
            }
        }
    }

    private void carregaImagem(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                usuario.setImagem(Auxilio.getImagemBytesFromUrl(usuario.getUrlGravatar()));
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(usuario.getAvatar());
                    }
                });
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
