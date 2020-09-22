package com.example.hw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Switch themeSwitcher;
    private boolean isLightTheme = true;

    private static final String THEME_KEY = "THEME_KEY";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            isLightTheme = savedInstanceState.getBoolean(THEME_KEY);
        }

        setTheme(isLightTheme ? R.style.LightTheme : R.style.DarkTheme);

        setContentView(R.layout.activity_main);
        themeSwitcher = findViewById(R.id.ThemeSwitcher);
        themeSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLightTheme ^= true;
                recreate();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(THEME_KEY, isLightTheme);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isLightTheme = savedInstanceState.getBoolean(THEME_KEY);
    }
}
