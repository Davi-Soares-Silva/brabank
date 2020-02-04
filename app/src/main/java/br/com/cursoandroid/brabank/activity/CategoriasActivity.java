package br.com.cursoandroid.brabank.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.util.List;
import java.util.Random;

import br.com.cursoandroid.brabank.R;
import br.com.cursoandroid.brabank.model.CategoriaDAO;
import br.com.cursoandroid.brabank.model.LancamentoDAO;
import br.com.cursoandroid.brabank.model.entity.Categoria;
import br.com.cursoandroid.brabank.model.entity.Lancamento;

public class CategoriasActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, RadioGroup.OnCheckedChangeListener, SearchView.OnQueryTextListener {
    RadioGroup rgTipoCategoria;
    EditText edtNomeCategoria;
    ListView lstCategorias;
    ArrayAdapter<Categoria> adapterCategorias;
    List<Categoria> categorias;
    Categoria categoriaSelecionada;
    SearchView srvCategorias;
    CategoriaDAO cDAO = new CategoriaDAO(this);
    boolean inserindo = true;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        toolbar = findViewById(R.id.toolbar);
        rgTipoCategoria = findViewById(R.id.rgTipoCategoria);
        edtNomeCategoria = findViewById(R.id.edtNomeCategoria);
        lstCategorias = findViewById(R.id.lstCategorias);
//        srvCategorias = findViewById(R.id.srvCategorias);

        rgTipoCategoria.setOnCheckedChangeListener(this);



        toolbar.setTitle("Nova Categoria");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carregarCategorias();

    }

    private void carregarCategorias() {
//        Pegando a lista de categorias

        if (rgTipoCategoria.getCheckedRadioButtonId() == R.id.rdCategoriaReceita) {
            categorias = cDAO.buscarPorTipo("R");
        } else {
            categorias = cDAO.buscarPorTipo("D");
        }

        adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias);
        lstCategorias.setAdapter(adapterCategorias);

        lstCategorias.setOnItemLongClickListener(this);
    }

    ;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);

//        Recuperamos o item Search do menu
        MenuItem menuItem = menu.findItem(R.id.search);

//        Transformamos em um searchview
        srvCategorias = (SearchView) menuItem.getActionView();

//        Setamos o listener de busca
        srvCategorias.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnMenuSalvar:
                if (inserindo) {
                    salvar();
                } else {
                    if (categoriaSelecionada != null) {
                        editar();
                    } else {
                        Toast.makeText(this, "Erro ao editar contato", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Metodo para inseror a Categoria
    public void salvar() {
        if (validar()) {

//            Criando um random aleatorio para usar como codigo
            Random random = new Random();
            Integer id = random.nextInt();

            String tipo = "";

            if (rgTipoCategoria.getCheckedRadioButtonId() == R.id.rdCategoriaReceita) {
                tipo = "R";
            } else {
                tipo = "D";
            }

            Categoria categoria = new Categoria(id, tipo, edtNomeCategoria.getText().toString());


            if (cDAO.inserir(categoria)) {
                Toast.makeText(this, "Categoria cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                edtNomeCategoria.setText("");
                carregarCategorias();
                inserindo = true;
            } else {
                Toast.makeText(this, "Categoria não pode ser cadastrada", Toast.LENGTH_SHORT).show();
                edtNomeCategoria.setText("");
                carregarCategorias();
            }


        }
    }

    public boolean validar() {

        boolean validado = true;
        String nomeCategoria = edtNomeCategoria.getText().toString().trim();

        if (nomeCategoria.equals("")) {
            validado = false;
            edtNomeCategoria.setError("Campo Obrigatório");
//            Verifica se o tamanho do nome é maior que 4
        } else if (nomeCategoria.length() < 4) {
            edtNomeCategoria.setError("O nome deve ter no mínimo 4 caracterees");
            validado = false;
        }

        return validado;
    }

    public void editar() {
        if (validar()) {
            Integer codigo = categoriaSelecionada.codigo;
            String tipo = "";

            if (rgTipoCategoria.getCheckedRadioButtonId() == R.id.rdCategoriaReceita) {
                tipo = "R";
            } else {
                tipo = "D";
            }

            Categoria categoriaEditada = new Categoria(codigo, tipo, edtNomeCategoria.getText().toString());

            if (cDAO.editar(categoriaEditada)) {
                Toast.makeText(this, "Contato editado com sucesso", Toast.LENGTH_SHORT).show();
                edtNomeCategoria.setText("");
                carregarCategorias();
                inserindo = true;
            } else {
                Toast.makeText(this, "Erro ao editar categoria", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rdCategoriaReceita) {
            categorias = cDAO.buscarPorTipo("R");
        } else if (checkedId == R.id.rdCategoriaDespesa) {
            categorias = cDAO.buscarPorTipo("D");
        }

        adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias);
        lstCategorias.setAdapter(adapterCategorias);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Categoria categoriaSel = categorias.get(i);
        listaOpcoes(categoriaSel);
        return true;
    }

    private void listaOpcoes(final Categoria categoria) {
//        Criando uma variável para a categoria que será alterada ou excluida
        final Categoria categoriaSel = categoria;
//        Lista com os itens que aparecerão no AlertDialog
        String[] opcao = {"Editar", "Excluir", "Cancelar"};

        new AlertDialog.Builder(this).setTitle("Selecione uma opção:").setItems(opcao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int posicao = i;
                switch (posicao) {
                    case 0:

                        edtNomeCategoria.setText(categoriaSel.nome);
                        edtNomeCategoria.setFocusable(true);
                        inserindo = false;
                        categoriaSelecionada = categoriaSel;
                        edtNomeCategoria.requestFocus();
                        edtNomeCategoria.setSelection(categoriaSel.nome.length());
                        break;
                    case 1:
                        new AlertDialog.Builder(CategoriasActivity.this).setTitle("Deseja Realmente Excluir").setMessage("Você ira perder as informações desta categoria").setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LancamentoDAO lDAO = new LancamentoDAO(CategoriasActivity.this);
                                List<Lancamento> lancamentos = lDAO.buscarPorCategoria(categoriaSel);
                                if (lancamentos.isEmpty()) {
                                    if (cDAO.excluir(categoriaSel)) {
                                        Toast.makeText(CategoriasActivity.this, "Categoria " + categoriaSel.nome + " excluida com sucesso", Toast.LENGTH_SHORT).show();
                                        carregarCategorias();
                                    } else {
                                        Toast.makeText(CategoriasActivity.this, "Erro ao excluir categoria", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(CategoriasActivity.this, "Categoria em uso", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Cancelar", null).show();

                        break;
                }
            }
        }).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapterCategorias.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterCategorias.getFilter().filter(newText);

        return true;
    }
}
