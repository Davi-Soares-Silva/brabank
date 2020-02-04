package br.com.cursoandroid.brabank.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.cursoandroid.brabank.R;
import br.com.cursoandroid.brabank.model.DBHelper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout =  (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navView);

        toolbar.setTitle("Brabank Toolbar");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.getReadableDatabase();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_item_um:
                startActivity(new Intent(this, NovoLancamentoActivity.class));
                break;
            case R.id.nav_item_dois:
                Toast.makeText(this, "Você clicou no botão dois", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_tres:
                startActivity(new Intent(this, CategoriasActivity.class));
                break;
            case R.id.nav_item_quatro:
                startActivity(new Intent(this, InformacoesActivity.class));
                break;
            case R.id.nav_item_cinco:
                Toast.makeText(this, "Você clicou em Ajuda", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item_seis:
                Toast.makeText(this, "Você clicou no menu Meu Perfil", Toast.LENGTH_SHORT).show();
                break;
            default:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
