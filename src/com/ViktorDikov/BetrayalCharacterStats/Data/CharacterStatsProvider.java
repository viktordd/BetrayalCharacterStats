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
		CharacterStats stats = new CharacterStats(r.getIntArray(R.array.char_constraints), r.getIntArray(getCharStatsArrayResource()), getDefaults());

		stats.setAge(r.getInteger(getCharAgeResource()));
		stats.setHeight(r.getString(getCharHeightResource()));
		stats.setWeight(r.getInteger(getCharWeightResource()));
		stats.setHobbies(r.getString(getCharHobbiesResource()));
		stats.setBirthday(r.getString(getCharBirthdayResource()));

		String statsStr = getPrefs().getString(getKey(), null);
		if (statsStr != null) {
			String[] statsSplit = TextUtils.split(statsStr, ",");
			if (statsSplit.length == 4) {
				stats.setSpeed(parse(statsSplit[0], stats.getSpeedDefault()));
				stats.setMight(parse(statsSplit[1], stats.getMightDefault()));
				stats.setSanity(parse(statsSplit[2], stats.getSanityDefault()));
				stats.setKnowledge(parse(statsSplit[3], stats.getKnowledgeDefault()));
			}
		}
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
				return R.array.defaults_blue_madame_zostra;
			case 1:
				return R.array.defaults_blue_vivian_lopez;
			case 2:
				return R.array.defaults_green_brandon_jaspers;
			case 3:
				return R.array.defaults_green_peter_akimoto;
			case 4:
				return R.array.defaults_purple_heather_granville;
			case 5:
				return R.array.defaults_purple_jenny_leclerc;
			case 6:
				return R.array.defaults_red_darrin_flash_williams;
			case 7:
				return R.array.defaults_red_ox_bellows;
			case 8:
				return R.array.defaults_white_father_rhinehardt;
			case 9:
				return R.array.defaults_white_professor_longfellow;
			case 10:
				return R.array.defaults_yellow_missy_dubourde;
			case 11:
				return R.array.defaults_yellow_zoe_ingstrom;
		}
	}

	private int getCharAgeResource() {
		switch (id) {
			default:
			case 0:
				return R.integer.age_blue_madame_zostra;
			case 1:
				return R.integer.age_blue_vivian_lopez;
			case 2:
				return R.integer.age_green_brandon_jaspers;
			case 3:
				return R.integer.age_green_peter_akimoto;
			case 4:
				return R.integer.age_purple_heather_granville;
			case 5:
				return R.integer.age_purple_jenny_leclerc;
			case 6:
				return R.integer.age_red_darrin_flash_williams;
			case 7:
				return R.integer.age_red_ox_bellows;
			case 8:
				return R.integer.age_white_father_rhinehardt;
			case 9:
				return R.integer.age_white_professor_longfellow;
			case 10:
				return R.integer.age_yellow_missy_dubourde;
			case 11:
				return R.integer.age_yellow_zoe_ingstrom;
		}
	}

	private int getCharHeightResource() {
		switch (id) {
			default:
			case 0:
				return R.string.height_blue_madame_zostra;
			case 1:
				return R.string.height_blue_vivian_lopez;
			case 2:
				return R.string.height_green_brandon_jaspers;
			case 3:
				return R.string.height_green_peter_akimoto;
			case 4:
				return R.string.height_purple_heather_granville;
			case 5:
				return R.string.height_purple_jenny_leclerc;
			case 6:
				return R.string.height_red_darrin_flash_williams;
			case 7:
				return R.string.height_red_ox_bellows;
			case 8:
				return R.string.height_white_father_rhinehardt;
			case 9:
				return R.string.height_white_professor_longfellow;
			case 10:
				return R.string.height_yellow_missy_dubourde;
			case 11:
				return R.string.height_yellow_zoe_ingstrom;
		}
	}

	private int getCharWeightResource() {
		switch (id) {
			default:
			case 0:
				return R.integer.weight_blue_madame_zostra;
			case 1:
				return R.integer.weight_blue_vivian_lopez;
			case 2:
				return R.integer.weight_green_brandon_jaspers;
			case 3:
				return R.integer.weight_green_peter_akimoto;
			case 4:
				return R.integer.weight_purple_heather_granville;
			case 5:
				return R.integer.weight_purple_jenny_leclerc;
			case 6:
				return R.integer.weight_red_darrin_flash_williams;
			case 7:
				return R.integer.weight_red_ox_bellows;
			case 8:
				return R.integer.weight_white_father_rhinehardt;
			case 9:
				return R.integer.weight_white_professor_longfellow;
			case 10:
				return R.integer.weight_yellow_missy_dubourde;
			case 11:
				return R.integer.weight_yellow_zoe_ingstrom;
		}
	}

	private int getCharHobbiesResource() {
		switch (id) {
			default:
			case 0:
				return R.string.hobbies_blue_madame_zostra;
			case 1:
				return R.string.hobbies_blue_vivian_lopez;
			case 2:
				return R.string.hobbies_green_brandon_jaspers;
			case 3:
				return R.string.hobbies_green_peter_akimoto;
			case 4:
				return R.string.hobbies_purple_heather_granville;
			case 5:
				return R.string.hobbies_purple_jenny_leclerc;
			case 6:
				return R.string.hobbies_red_darrin_flash_williams;
			case 7:
				return R.string.hobbies_red_ox_bellows;
			case 8:
				return R.string.hobbies_white_father_rhinehardt;
			case 9:
				return R.string.hobbies_white_professor_longfellow;
			case 10:
				return R.string.hobbies_yellow_missy_dubourde;
			case 11:
				return R.string.hobbies_yellow_zoe_ingstrom;
		}
	}

	private int getCharBirthdayResource() {
		switch (id) {
			default:
			case 0:
				return R.string.birthday_blue_madame_zostra;
			case 1:
				return R.string.birthday_blue_vivian_lopez;
			case 2:
				return R.string.birthday_green_brandon_jaspers;
			case 3:
				return R.string.birthday_green_peter_akimoto;
			case 4:
				return R.string.birthday_purple_heather_granville;
			case 5:
				return R.string.birthday_purple_jenny_leclerc;
			case 6:
				return R.string.birthday_red_darrin_flash_williams;
			case 7:
				return R.string.birthday_red_ox_bellows;
			case 8:
				return R.string.birthday_white_father_rhinehardt;
			case 9:
				return R.string.birthday_white_professor_longfellow;
			case 10:
				return R.string.birthday_yellow_missy_dubourde;
			case 11:
				return R.string.birthday_yellow_zoe_ingstrom;
		}
	}

	private int getCharStatsArrayResource() {
		switch (id) {
			default:
			case 0:
				return R.array.stats_blue_madame_zostra;
			case 1:
				return R.array.stats_blue_vivian_lopez;
			case 2:
				return R.array.stats_green_brandon_jaspers;
			case 3:
				return R.array.stats_green_peter_akimoto;
			case 4:
				return R.array.stats_purple_heather_granville;
			case 5:
				return R.array.stats_purple_jenny_leclerc;
			case 6:
				return R.array.stats_red_darrin_flash_williams;
			case 7:
				return R.array.stats_red_ox_bellows;
			case 8:
				return R.array.stats_white_father_rhinehardt;
			case 9:
				return R.array.stats_white_professor_longfellow;
			case 10:
				return R.array.stats_yellow_missy_dubourde;
			case 11:
				return R.array.stats_yellow_zoe_ingstrom;
		}
	}
}
