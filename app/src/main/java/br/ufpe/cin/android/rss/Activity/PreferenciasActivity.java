package br.ufpe.cin.android.rss.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.ufpe.cin.android.rss.R;
import br.ufpe.cin.android.rss.Fragments.PreferenceScreen;

public class PreferenciasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new PreferenceScreen())
                .commit();
    }
}