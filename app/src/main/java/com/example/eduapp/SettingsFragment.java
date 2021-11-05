package com.example.eduapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class SettingsFragment extends Fragment {
    private MainViewModel mainViewModel;

    private SharedPreferences kidoozePrefs;
    private String kidoozePrefFile = "com.example.android.kidoozePrefs";

    private boolean bgm = true;
    private boolean soundFX = true;
    private boolean notifications = true;
    private boolean darkMode = false;

    private OnSettingsChangedListener settingsChangedListener;

    ImageButton back_btn;
    Switch bgm_switch;
    Switch soundFX_switch;
    Switch notifications_switch;
    Switch darkMode_switch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel =
                new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Sets Activity as the listener for changing bgm
        settingsChangedListener = (OnSettingsChangedListener) getActivity();

        kidoozePrefs = getContext().getSharedPreferences(kidoozePrefFile, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back_btn = view.findViewById(R.id.guideback_btn);
        bgm_switch = view.findViewById(R.id.bgm_switch);
        soundFX_switch = view.findViewById(R.id.soundfx_switch);
        notifications_switch = view.findViewById(R.id.notif_switch);
        darkMode_switch = view.findViewById(R.id.darkmode_switch);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_navigation_profile);
            }
        });

        bgm_switch.setChecked(kidoozePrefs.getBoolean(getString(R.string.bgm), true));
        soundFX_switch.setChecked(kidoozePrefs.getBoolean(getString(R.string.soundFX), true));
        notifications_switch.setChecked(kidoozePrefs.getBoolean(getString(R.string.notifs), true));
        darkMode_switch.setChecked(kidoozePrefs.getBoolean(getString(R.string.darkMode), false));

        SharedPreferences.Editor preferencesEditor = kidoozePrefs.edit();

        bgm_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferencesEditor.putBoolean(getString(R.string.bgm), isChecked);
                preferencesEditor.apply();
                if (settingsChangedListener != null) {
                    settingsChangedListener.playMusic(isChecked);
                }
            }
        });
        darkMode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferencesEditor.putBoolean(getString(R.string.darkMode), b);
                preferencesEditor.apply();
                if (settingsChangedListener != null) {
                    settingsChangedListener.setDarkMode(b);
                    //reloadFragment();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = kidoozePrefs.edit();

//        preferencesEditor.putBoolean(getString(R.string.bgm), bgm_switch.isChecked());
        preferencesEditor.putBoolean(getString(R.string.soundFX), soundFX_switch.isChecked());
        preferencesEditor.putBoolean(getString(R.string.notifs), notifications_switch.isChecked());
//        preferencesEditor.putBoolean(getString(R.string.darkMode), darkMode_switch.isChecked());
//        preferencesEditor.apply();
    }

    public void reloadFragment() {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.settingsFragment);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

}