package com.pitman2e.mutespeaker;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.pitman2e.mutespeaker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.findViewById(R.id.action_home)
                        .setOnClickListener((v) -> {
                            replaceFragmentHome();
                            ((BottomNavigationItemView) v).setSelected(true);
                        });
        binding.bottomNavigationView.findViewById(R.id.action_log)
                        .setOnClickListener((v) -> {
                            replaceFragmentLog();
                            ((BottomNavigationItemView) v).setSelected(true);
                        });

        replaceFragmentHome();
    }

    private void replaceFragmentHome() {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new PreferencesFragment());
        ft.commit();
    }

    private void replaceFragmentLog() {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new LogFragment());
        ft.commit();
    }
}
