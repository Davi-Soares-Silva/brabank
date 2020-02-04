package br.com.cursoandroid.brabank.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import br.com.cursoandroid.brabank.R;
import br.com.cursoandroid.brabank.model.CategoriaDAO;
import br.com.cursoandroid.brabank.model.LancamentoDAO;
import br.com.cursoandroid.brabank.model.entity.Categoria;
import br.com.cursoandroid.brabank.model.entity.Lancamento;

public class NovoLancamentoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {
    Toolbar toolbar;
    RadioGroup rgTipoLancamentoCategorias;
    EditText edtDescricaoNovoLancamento, edtValorNovoLancamento, edtData;
    Spinner spnNovoLancamento;
    List<Categoria> categorias;
    ArrayAdapter<Categoria> adapterCategorias;
    RadioButton rdDespesaNovoLancamento, rdReceitaNovoLancamento;
    Integer opcaoSelecionada = 0;
    DatePickerDialog dateDialog;
    Calendar dataAtual;
    Date dataEscolhida;
    CategoriaDAO cDAO = new CategoriaDAO(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_lancamento);
        toolbar = findViewById(R.id.toolbar);
        spnNovoLancamento = findViewById(R.id.spnNovoLancamento);
        rgTipoLancamentoCategorias = findViewById(R.id.rgTipoLancamentoCategoria);
        edtDescricaoNovoLancamento = findViewById(R.id.edtDescricaoNovoLancamento);
        edtValorNovoLancamento = findViewById(R.id.edtValorNovoLancamento);
        edtData = findViewById(R.id.edtData);
        rdDespesaNovoLancamento = findViewById(R.id.rdDespesaNovoLancamento);
        rdReceitaNovoLancamento = findViewById(R.id.rdReceitaNovoLancamento);

        rgTipoLancamentoCategorias.setOnCheckedChangeListener(this);
        edtData.setOnFocusChangeListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        carreegarSpinner();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnMenuSalvar:
                salvar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        if (validar()) {
            LancamentoDAO lDAO = new LancamentoDAO(this);
            Random random = new Random();
            Integer id = random.nextInt();

            String tipo = "";

            if (rgTipoLancamentoCategorias.getCheckedRadioButtonId() == R.id.rdReceitaNovoLancamento) {
                tipo = "R";
            } else {
                tipo = "D";
            }

            double valor = Double.parseDouble(edtValorNovoLancamento.getText().toString().trim());

            String sdata = edtData.getText().toString();

            Date dataLancamento = new Date();

            try {
                Date data = new SimpleDateFormat("dd/MM/yyyy").parse(sdata);
                dataLancamento = data;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Categoria categoria = categorias.get(spnNovoLancamento.getSelectedItemPosition());

            Lancamento lancamento = new Lancamento(id, tipo, edtDescricaoNovoLancamento.getText().toString(), valor, categoria, dataLancamento);

            if (lDAO.inserir(lancamento)) {
                Toast.makeText(this, "Lançamento Realizado com Sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lançamento não pode ser realizado", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private boolean validar() {

        boolean validado = true;

        if (spnNovoLancamento.getSelectedItem() == null) {
            validado = false;
            new AlertDialog.Builder(this).setTitle("Erro").setMessage("Por Favor, selecione um tipo de categoria.").setPositiveButton("Ok", null);
            Toast.makeText(this, "Algo deu errado", Toast.LENGTH_SHORT).show();
        }

        if (edtValorNovoLancamento.getText().toString().equals("")) {
            validado = false;
            edtValorNovoLancamento.setError("Campo Obrigatório.");
        }

        if (edtDescricaoNovoLancamento.getText().toString().equals("")) {
            validado = false;
            edtDescricaoNovoLancamento.setError("Campo Obrigatório.");
        }

        if (edtDescricaoNovoLancamento.getText().toString().length() <= 3) {
            edtDescricaoNovoLancamento.setError("Deve haver mais que 3 caracteres");
            validado = false;
        }

        String sdata = edtData.getText().toString().trim();

        if (sdata.equals("")) {
            edtData.setError("Campo Obrigatório");
            validado = false;
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                dataEscolhida = sdf.parse(sdata);

                if (dataEscolhida.compareTo(dataAtual.getTime()) > 0) {
                    edtData.setError("Data não pode ser maior que a data atual");
                    validado = false;
                }
            } catch (ParseException e) {
                edtData.setError("Data Invalida");
                validado = false;
                e.printStackTrace();
            }
        }

        return validado;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        opcaoSelecionada = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        opcaoSelecionada = -1;
    }

    public void carreegarSpinner(){
        if (rgTipoLancamentoCategorias.getCheckedRadioButtonId() == R.id.rdDespesaNovoLancamento){
            categorias = cDAO.buscarPorTipo("D");
        }else {
            categorias = cDAO.buscarPorTipo("R");
        }

        adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        spnNovoLancamento.setAdapter(adapterCategorias);
    }


    //    Método responsável por verificar qual radio está selecionado
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.rdReceitaNovoLancamento) {
            categorias = cDAO.buscarPorTipo("R");
        } else if (checkedId == R.id.rdDespesaNovoLancamento) {
            categorias = cDAO.buscarPorTipo("D");
        }

        adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);

//        Liga o spinner ao adapter
        spnNovoLancamento.setAdapter(adapterCategorias);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String data = String.valueOf((dayOfMonth) + " /" + (String.valueOf(month + 1) + " /" + String.valueOf(year)));
            Toast.makeText(NovoLancamentoActivity.this, "Data = " + data, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
//            Pega a data atual e divide em dia, mes, e ano

            dataAtual = Calendar.getInstance();
            Integer dia = dataAtual.get(Calendar.DAY_OF_MONTH);
            Integer mes = dataAtual.get(Calendar.MONTH);
            Integer ano = dataAtual.get(Calendar.YEAR);


//            Instancia o datepicker passando o context, Listener e dia, mes e ano atual

            dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                    if (dayOfMonth < 9) {
                        edtData.setText("0" + dayOfMonth + "/" + month + "/" + year);
                    }

                    if (month < 9) {
                        edtData.setText(dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
                    } else {
                        edtData.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }

                    if (dayOfMonth < 9 && month < 9) {
                        edtData.setText("0" + dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
                    }

                }
            }, ano, mes, dia);

//            Limitando o cakendario com a data de hioje
            dateDialog.getDatePicker().setMaxDate(dataAtual.getTimeInMillis());

            dateDialog.setTitle("Escolha uma data");
            dateDialog.show();
        }
    }
}
