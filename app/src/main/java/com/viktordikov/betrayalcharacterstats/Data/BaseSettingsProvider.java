package com.viktordikov.betrayalcharacterstats.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

public abstract class BaseSettingsProvider {

	private String sharedPreferencesName;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;
	FragmentActivity fragmentActivity;

	BaseSettingsProvider(FragmentActivity fa, String sharedPreferencesName) {
		fragmentActivity = fa;
		this.sharedPreferencesName = sharedPreferencesName;
	}

	public SharedPreferences getPrefs() {
		if (sharedPref == null)
			sharedPref = fragmentActivity.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
		return sharedPref;
	}

	SharedPreferences.Editor getEditor() {
		if (editor == null)
			editor = getPrefs().edit();
		return editor;
	}

	public void apply() {
		if (editor != null) {
			editor.apply();
			editor = null;
		}
	}
}
