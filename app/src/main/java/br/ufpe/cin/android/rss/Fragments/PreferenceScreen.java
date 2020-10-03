package br.ufpe.cin.android.rss.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import br.ufpe.cin.android.rss.R;

/**
 * The PreferenceScreen implements the methods needed to implements the fragment.
 */
public class PreferenceScreen extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String link_preference = prefs.getString(getString(R.string.link_prefs_name), getString(R.string.feed_padrao));

        EditTextPreference currentLink = (EditTextPreference) findPreference("link_preference");
        currentLink.setSummary(link_preference);

        currentLink.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);

                return true;
            }
        });
    }
}
