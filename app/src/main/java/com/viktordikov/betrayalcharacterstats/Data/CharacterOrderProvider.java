package com.viktordikov.betrayalcharacterstats.Data;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.viktordikov.betrayalcharacterstats.R;

import java.util.ArrayList;

public class CharacterOrderProvider extends BaseSettingsProvider {

    private static final String PREFS = "CharacterOrder";
	private static final String ID_LIST = "CharacterOrder_ID_Array";
	private static final String SAVED_TAB_POSITION = "saved_tab_position";
	public static final String CHANGED = "CharacterOrder_Changed";

	public CharacterOrderProvider(FragmentActivity fa) {
		super(fa, PREFS);
	}

	private ArrayList<Integer> getDefaults() {
		int numChars = fragmentActivity.getResources().getInteger(R.integer.numCharacters);
		ArrayList<Integer> defaultIds = new ArrayList<>(numChars);
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

		ArrayList<Integer> ids = new ArrayList<>();
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

	public int getTabPosition() {
		return getPrefs().getInt(SAVED_TAB_POSITION, 0);
	}

	public void setTabPosition(int pos) {
		getEditor().putInt(SAVED_TAB_POSITION, pos);
	}
}
