package br.com.cursoandroid.brabank.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Random;

import br.com.cursoandroid.brabank.R;
import br.com.cursoandroid.brabank.model.UsuarioDAO;
import br.com.cursoandroid.brabank.model.entity.Usuario;

public class CadastroActivity extends AppCompatActivity {
    EditText edtNomeUsuario;
    RadioGroup rgSexo;
    EditText edtCpfUsuario;
    EditText edtEmailUsuario;
    EditText edtSenhaUsuario;
    EditText edtConfirmaSenhaUsuario;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        toolbar = findViewById(R.id.toolbar);
        edtNomeUsuario = findViewById(R.id.edtNomeUsuario);
        rgSexo = findViewById(R.id.rgSexo);
        edtCpfUsuario = findViewById(R.id.edtCpfUsuario);
        edtEmailUsuario = findViewById(R.id.edtEmailUsuario);
        edtSenhaUsuario = findViewById(R.id.edtSenhaUsuario);
        edtConfirmaSenhaUsuario = findViewById(R.id.edtConfirmaSenhaUsuario);

        toolbar.setTitle("Novo Usuário");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnMenuSalvar:
                salvar();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void salvar(){
        if (validar()){

            Random random = new Random();
            Integer id = random.nextInt();

            String sexo = "";
            if (rgSexo.getCheckedRadioButtonId() == R.id.rdSexoMasculino){
                sexo = "M";
            }else{
                sexo = "F";
            }


            Usuario usuario = new Usuario(
                    id,
                    edtNomeUsuario.getText().toString(),
                    edtCpfUsuario.getText().toString(),
                    edtEmailUsuario.getText().toString(),
                    edtSenhaUsuario.getText().toString(),
                    sexo
            );

            UsuarioDAO uDAO = new UsuarioDAO();
            if(uDAO.inserir(usuario)){
                Toast.makeText(this, "Usuário Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Algo deu Errado", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public boolean validar(){

        boolean validado = true;

        if (edtNomeUsuario.getText().toString().equals("")){
            validado = false;
            edtNomeUsuario.setError("Campo Obrigatório");
        }
        if (edtCpfUsuario.getText().toString().equals("")){
            validado = false;
            edtCpfUsuario.setError("Campo Obrigatório");
        }
        if (edtEmailUsuario.getText().toString().equals("")){
            validado = false;
            edtEmailUsuario.setError("Campo Obrigatório");
        }
        if(edtSenhaUsuario.getText().toString().equals("")){
            validado = false;
            edtSenhaUsuario.setError("Campo Obrigatório");
        }
        if (edtConfirmaSenhaUsuario.getText().toString().equals("")){
            validado = false;
            edtConfirmaSenhaUsuario.setError("Campo Obrigatório");
        }

        if (validado){
            String senha = edtSenhaUsuario.getText().toString();
            String confSenha = edtConfirmaSenhaUsuario.getText().toString();

            if(!senha.equals(confSenha)){
                edtConfirmaSenhaUsuario.setError("As senhas devem ser iguais");
                validado = false;
            }
        }

        return validado;
    }
}
