package br.com.adsnoobs.sqliteappexemplo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ConsultaActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView listViewUsuarios;
    private Button buttonFechar;
    private UsuarioAdapter usuarioAdapter;
    private ArrayList<Usuario> usuarios;
    private Usuario usuarioEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        buttonFechar = (Button)findViewById(R.id.buttonFechar);
        buttonFechar.setOnClickListener(this);

        listViewUsuarios = (ListView)findViewById(R.id.listViewUsuarios);
        listViewUsuarios.setOnItemClickListener(this);

        usuarios = new Usuario(this).getUsuarios();
        usuarioAdapter = new UsuarioAdapter(this,usuarios);
        listViewUsuarios.setAdapter(usuarioAdapter);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuario usuario = usuarios.get(position);
        Intent intent = new Intent(this,CadastroActivity.class);
        intent.putExtra("consulta",usuario.getCodigo());
        usuarioEdicao = usuario;
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (usuarioEdicao != null){
            usuarioEdicao.carregaUsuarioPeloCodigo(usuarioEdicao.getCodigo());
            if (usuarioEdicao.isExcluir())
                usuarios.remove(usuarioEdicao);
            usuarioAdapter.notifyDataSetChanged();
        }
    }
}
