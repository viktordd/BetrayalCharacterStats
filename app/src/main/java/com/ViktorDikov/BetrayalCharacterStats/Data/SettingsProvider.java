package com.ViktorDikov.BetrayalCharacterStats.Data;

import android.support.v4.app.FragmentActivity;

public class SettingsProvider extends BaseSettingsProvider {

	private static final String PREFS = "Settings";
	private static final String ALWAYS_ON_DISPLAY = "always_on_display";
	private static final String NAME = "name";

	public SettingsProvider(FragmentActivity fa) {
		super(fa, PREFS);
	}

	public boolean getAlwaysOnDisplay() {
		return getPrefs().getBoolean(ALWAYS_ON_DISPLAY, false);
	}

	public void setAlwaysOnDisplay(boolean val) {
		getEditor().putBoolean(ALWAYS_ON_DISPLAY, val);
	}


	public String getName() {
		return getPrefs().getString(NAME, "Player");
	}

	public void setName(String val) {
		getEditor().putString(NAME, val);
	}
}
