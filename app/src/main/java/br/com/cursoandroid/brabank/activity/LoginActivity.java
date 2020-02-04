package br.com.cursoandroid.brabank.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import br.com.cursoandroid.brabank.R;
import br.com.cursoandroid.brabank.model.RetrofitConfig;
import br.com.cursoandroid.brabank.model.UsuarioDAO;
import br.com.cursoandroid.brabank.model.api.UsuariosAPI;
import br.com.cursoandroid.brabank.model.entity.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPrimeiroAcesso, btnEntrar;
    EditText edtUsuario, edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnPrimeiroAcesso = findViewById(R.id.btnPrimeiroAcesso);
        btnEntrar = findViewById(R.id.btnEntrar);
        edtSenha = findViewById(R.id.edtSenhaUsuario);
        edtUsuario = findViewById(R.id.edtEmailUsuario);

        btnPrimeiroAcesso.setOnClickListener(this);
        btnEntrar.setOnClickListener(this);

        UsuariosAPI usuariosAPI = RetrofitConfig.getRetrofitInstance().create(UsuariosAPI.class);

        Call<List<Usuario>> call = usuariosAPI.listarTodos();

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> usuarios = response.body();
                Log.i("TESTE", "Deu certo!! code: " + response.code());
                for (Usuario u : usuarios){
                    Log.i("TESTE", "Usuario: " + u.nome);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.i("TESTE", "DEU RUIM");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        UsuarioDAO uDAO = new UsuarioDAO();
        List<Usuario> usuarios = uDAO.listarTodos();

        for (Usuario u : usuarios) {
            Log.i("Teste", "Usuario :" + u.email);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnEntrar:
                //autenticar();
                autenticarAPI();
                break;

            case R.id.btnPrimeiroAcesso:
                startActivity(new Intent(this, CadastroActivity.class));
        }


    }

    private void autenticar() {
        String email, senha;

        email = edtUsuario.getText().toString().trim();
        senha = edtSenha.getText().toString().trim();

        UsuarioDAO uDAO = new UsuarioDAO();
        Usuario u = uDAO.buscarPorEmail(email);

        if (u != null && u.senha.equals(senha)) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            new AlertDialog.Builder(this).setTitle("Erro de Autenticação").setMessage("Usuário e/ou senha inválidos").show();
        }

    }

    private void autenticarAPI(){
        final String email, senha;

        email = edtUsuario.getText().toString().trim();
        senha = edtSenha.getText().toString();

        UsuariosAPI usuariosAPI = RetrofitConfig.getRetrofitInstance().create(UsuariosAPI.class);

        Call<Usuario> call = usuariosAPI.buscarPorEmail(email);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                // Verificando se o usuário foi encontrado
                if(response.code() == 404){
                    Toast.makeText(LoginActivity.this, "Usuario ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }else{
                    if(response.code() == 200){
                        Usuario usuario = response.body();
                        //se encontrado verifica se a senha é valida
                        if(usuario.senha.equals(senha)){
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "erro de autenticação", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }



}
