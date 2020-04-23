package es.iesquevedo.descubreespana;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.iesquevedo.descubreespana.databinding.ActivityMainBinding;
import es.iesquevedo.descubreespana.utils.GetSharedPreferences;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);

        //En el AppBarConfiguration se especifican que fragments serán destinos de primer nivel.
        //Todos aquellos destinos que no sean de primer nivel tendrán una back arrow en la actionbar
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_listado, R.id.navigation_login, R.id.userAccountFragment)
                .build();
        //Se obtiene el navcontroller pasando la activity y el contenedos de los fragments
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //Se vincula la actionbar de la activity al navcontroller
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //Se borra al inicio para que no haya ningún usuario loggeado
        GetSharedPreferences.getInstance().clearCurrentUser(this);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (navView.getSelectedItemId() != item.getItemId()) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                                navController.navigate(R.id.navigation_home);
                            break;
                        case R.id.navigation_listado:
                            navController.navigate(R.id.navigation_listado);
                            break;
                        case R.id.navigation_login:
                            if (GetSharedPreferences.getInstance().getCurrentUser(getApplicationContext()) == null) {
                                    navController.navigate(R.id.navigation_login);
                            } else {
                                    navController.navigate(R.id.userAccountFragment);
                            }
                            break;
                    }
                }
                return true;
            }
        });

    }



    @Override
    public boolean onSupportNavigateUp() {
        return navController.popBackStack()
                || super.onSupportNavigateUp();
    }
}
