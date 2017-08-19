package com.viktordikov.betrayalcharacterstats.Data;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.viktordikov.betrayalcharacterstats.R;

public class CharacterOrderProvider {

	public static final String PREFS = "CharacterOrder";
	public static final String ID_LIST = "CharacterOrder_ID_Array";
	public static final String CHANGED = "CharacterOrder_Changed";

	private FragmentActivity fragmentActivity;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;

	public CharacterOrderProvider(FragmentActivity fa) {
		fragmentActivity = fa;
	}

	public SharedPreferences getPrefs() {
		if (sharedPref == null)
			sharedPref = fragmentActivity.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		return sharedPref;
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

	public ArrayList<Integer> getDefaults() {
		int numChars = fragmentActivity.getResources().getInteger(R.integer.numCharacters);
		ArrayList<Integer> defaultIds = new ArrayList<Integer>(numChars);
		for (int i = 0; i < numChars; i++) {
			defaultIds.add(i * 2);
		}
		return defaultIds;
	}

	public ArrayList<Integer> getIDs() {
		String idsStr = getPrefs().getString(ID_LIST, null);
		if (idsStr == null) {
			return getDefaults();
		}

		String[] idsSplit = TextUtils.split(idsStr, ",");

		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < idsSplit.length; i++) {
			if (idsSplit[i].length() > 0) {
				try {
					ids.add(Integer.valueOf(idsSplit[i]));
				} catch (Exception e) {
					ids.add(i * 2);
				}
			}
		}
		return ids;
	}

	public void setIDs(ArrayList<Integer> ids) {
		getEditor().putString(ID_LIST, TextUtils.join(",", ids));
	}

	public void setChanged() {
		getEditor().putLong(CHANGED, System.currentTimeMillis());
	}
}
