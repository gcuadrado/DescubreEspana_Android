package es.iesquevedo.descubreespana;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import es.iesquevedo.descubreespana.databinding.ActivityMainBinding;
import es.iesquevedo.descubreespana.ui.useraccount.UserAccountViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);

        //En el AppBarConfiguration se especifican que fragments serán destinos de primer nivel.
        //Todos aquellos destinos que no sean de primer nivel tendrán una back arrow en la actionbar
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_login, R.id.userAccountFragment)
                .build();
        //Se obtiene el navcontroller pasando la activity y el contenedos de los fragments
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //Se vincula la actionbar de la activity al navcontroller
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        UserAccountViewModel userAccountViewModel = new ViewModelProvider(this).get(UserAccountViewModel.class);


        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        navController.navigate(R.id.navigation_home);
                        break;
                    case R.id.navigation_dashboard:
                        navController.navigate(R.id.navigation_dashboard);
                        break;
                    case R.id.navigation_login:
                        if(userAccountViewModel.getmUsuario().getValue()==null) {
                            navController.navigate(R.id.navigation_login);
                        }else{
                            navController.navigate(R.id.userAccountFragment);
                        }
                        break;
                }
                return true;
            }
        });

    }

}
