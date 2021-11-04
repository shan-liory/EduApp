package com.example.eduapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    MainViewModel mainViewModel;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setTest("something");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navView = (BottomNavigationView) findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        Log.d("LAUNCH","yoyo");
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.mobile_navigation, true)
                        .build();

                navController.navigate(item.getItemId(), null, navOptions);
                return true;
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.d("onDestinationChanged", destination.getId() + " home frag: " + R.id.navigation_home);
                switch (destination.getId()) {
                    case R.id.navigation_home:
                    case R.id.navigation_dashboard:
                    case R.id.navigation_profile:
                    case R.id.navigation_notifications:
                        navView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        navView.setVisibility(View.GONE);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        //top level destinations
        switch (navController.getCurrentDestination().getId()) {
            case R.id.navigation_home:
            case R.id.navigation_dashboard:
            case R.id.navigation_profile:
            case R.id.navigation_notifications:
                super.onBackPressed();
                break;
            default:
                super.onBackPressed();
        }

    }

}
