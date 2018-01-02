package com.ViktorDikov.BetrayalCharacterStats.Data;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ViktorDikov.BetrayalCharacterStats.R;

import java.util.Locale;

public class CharacterStatsProvider extends BaseSettingsProvider {

    private static final String PREFS = "CharacterStats";

    private int id;
    private String key;

    public CharacterStatsProvider(FragmentActivity fa, int id) {
        super(fa, PREFS);
        this.id = id;
    }

    private String getKey() {
        if (key == null)
            key = fragmentActivity.getResources().getStringArray(R.array.titles)[id];
        return key;
    }

    private int[] getDefaults() {
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
        if (statsStr == null) {
            return stats;
        }

        String[] statsSplit = TextUtils.split(statsStr, ",");
        if (statsSplit.length != 8) {
            return stats;
        }

        stats.setGymnasium(parse(statsSplit[4]));
        stats.setLarder(parse(statsSplit[5]));
        stats.setChapel(parse(statsSplit[6]));
        stats.setLibrary(parse(statsSplit[7]));
        stats.setSpeed(parse(statsSplit[0], stats.getSpeedDefault()));
        stats.setMight(parse(statsSplit[1], stats.getMightDefault()));
        stats.setSanity(parse(statsSplit[2], stats.getSanityDefault()));
        stats.setKnowledge(parse(statsSplit[3], stats.getKnowledgeDefault()));

        return stats;
    }

    private int parse(String s, int defaultVal) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private boolean parse(String s) {
        try {
            return Boolean.valueOf(s);
        } catch (Exception e) {
            return false;
        }
    }

    public void setStats(CharacterStats stats) {
        String statsStr = String.format(Locale.ROOT, "%d,%d,%d,%d,%b,%b,%b,%b",
                stats.getSpeed(), stats.getMight(), stats.getSanity(), stats.getKnowledge(),
                stats.getGymnasium(), stats.getLarder(), stats.getChapel(), stats.getLibrary());
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
