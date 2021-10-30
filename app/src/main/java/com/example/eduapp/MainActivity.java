package com.example.eduapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements OnSettingsChangedListener{

    BottomNavigationView navView;
    MainViewModel mainViewModel;

    MediaPlayer mediaPlayer;

    // Shared preferences file for settings to play bgm, sound fx, dark mode
    private SharedPreferences kidoozePrefs;
    private String kidoozePrefFile = "com.example.android.kidoozePrefs";

    // Preferences
    private boolean bgm = true;
    private boolean soundFX = true;
    private boolean notifications = true;
    private boolean darkMode = false;

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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Load and read shared preference file
        kidoozePrefs = getSharedPreferences(kidoozePrefFile, MODE_PRIVATE);

        bgm = kidoozePrefs.getBoolean(getString(R.string.bgm), true);
        soundFX = kidoozePrefs.getBoolean(getString(R.string.soundFX), true);
        notifications = kidoozePrefs.getBoolean(getString(R.string.notifs), true);
        darkMode = kidoozePrefs.getBoolean(getString(R.string.darkMode), false);

        // Set the settings
        playMusic(bgm);
        setDarkMode(darkMode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        playMusic(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bgm = kidoozePrefs.getBoolean(getString(R.string.bgm), true);
        playMusic(bgm);
    }

    public void setBottomNavVisibility(boolean visible){
        navView.setVisibility(visible ? View.VISIBLE:View.GONE);
    }

    // Plays bgm if bgm settings is true, overrode from OnSettingsChangedListener interface
    @Override
    public void playMusic(boolean bgm) {
        if (bgm) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kidoozebgm);
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }
    }

    @Override
    public void setDarkMode(boolean darkMode) {
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}