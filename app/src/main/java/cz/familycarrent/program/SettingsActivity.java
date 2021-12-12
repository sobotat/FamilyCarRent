package cz.familycarrent.program;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    Switch sw_ForceDarkMode;
    Boolean forceDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sw_ForceDarkMode = findViewById(R.id.sw_SettingsForceDarkMode);

        sw_ForceDarkMode.setChecked(get_ForceDarkMode());
        sw_ForceDarkMode.setOnClickListener(v -> {
            forceDarkMode = sw_ForceDarkMode.isChecked();
            set_ForceDarkMode(forceDarkMode);
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public boolean get_ForceDarkMode(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getBoolean("forceDarkMode", false);
    }

    public void set_ForceDarkMode(Boolean forceDarkMode){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        preferences.edit().putBoolean("forceDarkMode", forceDarkMode).commit();

        if(forceDarkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}