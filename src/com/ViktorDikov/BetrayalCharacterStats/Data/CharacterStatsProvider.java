package com.ViktorDikov.BetrayalCharacterStats.Data;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ViktorDikov.BetrayalCharacterStats.R;

public class CharacterStatsProvider {

	public static final String PREFS = "CharacterStats";

	private int id;
	private String key;
	private FragmentActivity fragmentActivity;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;

	public CharacterStatsProvider(FragmentActivity fa, int id) {
		fragmentActivity = fa;
		this.id = id;
	}

	public SharedPreferences getPrefs() {
		if (sharedPref == null)
			sharedPref = fragmentActivity.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		return sharedPref;
	}

	private String getKey() {
		if (key == null)
			key = fragmentActivity.getResources().getStringArray(R.array.titles)[id];
		return key;
	}

	private SharedPreferences.Editor getEditor() {
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

	public int[] getDefaults() {
		return fragmentActivity.getResources().getIntArray(getDefaultCharStatsResource());
	}

	public CharacterStats getStats() {
		Resources r = fragmentActivity.getResources();
		CharacterStats stats = new CharacterStats(r.getIntArray(R.array.char_constraints));
		int[] defaults = getDefaults();

		String statsStr = getPrefs().getString(getKey(), null);
		if (statsStr != null) {
			String[] statsSplit = TextUtils.split(statsStr, ",");
			if (statsSplit.length == 4) {
				stats.setSpeed(parse(statsSplit[0], defaults[0]));
				stats.setMight(parse(statsSplit[1], defaults[1]));
				stats.setSanity(parse(statsSplit[2], defaults[2]));
				stats.setKnowledge(parse(statsSplit[3], defaults[3]));
				return stats;
			}
		}
		stats.Set(defaults);
		return stats;
	}

	private int parse(String s, int defaultVal) {
		try {
			return Integer.valueOf(s);
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public void setStats(CharacterStats stats) {
		String statsStr = String.format(Locale.ROOT, "%d,%d,%d,%d", stats.getSpeed(), stats.getMight(), stats.getSanity(), stats.getKnowledge());
		getEditor().putString(getKey(), statsStr);
	}

	private int getDefaultCharStatsResource() {
		switch (id) {
			default:
			case 0:
				return R.array.madame;
			case 1:
				return R.array.vivian;
			case 2:
				return R.array.brandon;
			case 3:
				return R.array.peter;
			case 4:
				return R.array.heather;
			case 5:
				return R.array.jenny;
			case 6:
				return R.array.darrin;
			case 7:
				return R.array.ox;
			case 8:
				return R.array.father;
			case 9:
				return R.array.professor;
			case 10:
				return R.array.missy;
			case 11:
				return R.array.zoe;
		}
	}
}
