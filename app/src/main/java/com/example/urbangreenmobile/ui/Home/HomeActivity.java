package com.example.urbangreenmobile.ui.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.urbangreenmobile.R;
import com.example.urbangreenmobile.ui.Fornecedor.FornecedorActivity;
import com.example.urbangreenmobile.ui.Insumo.InsumoActivity;
import com.example.urbangreenmobile.ui.Login.LoginActivity;
import com.example.urbangreenmobile.ui.Producao.ProducaoActivity;
import com.example.urbangreenmobile.ui.Produto.ProdutoActivity;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela_principal);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnMenu).setOnClickListener(v -> {
            drawerLayout.openDrawer(navigationView);
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_fornecedores) {
                    Intent intent = new Intent(HomeActivity.this, FornecedorActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                    return true;
                }

                if(id == R.id.nav_estoque){
                    showSubMenuDialog();
                    return true;
                }

                if(id == R.id.nav_producao){
                    Intent intent = new Intent(HomeActivity.this, ProducaoActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                    return true;
                }

                return false;
            }
        });
    }

    private void showSubMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha uma opção")
                .setItems(new CharSequence[]{"Produtos", "Insumos"}, (dialog, which) -> {
                    switch (which) {
                        case 0: // Produtos
                            Intent intentProdutos = new Intent(HomeActivity.this, ProdutoActivity.class);
                            startActivity(intentProdutos);
                            break;
                        case 1: // Insumos
                            Intent intentInsumos = new Intent(HomeActivity.this, InsumoActivity.class);
                            startActivity(intentInsumos);
                    }
                });
        builder.create().show();
    }

    public void sair(View view) {
        Intent in = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(in);
    }
}


