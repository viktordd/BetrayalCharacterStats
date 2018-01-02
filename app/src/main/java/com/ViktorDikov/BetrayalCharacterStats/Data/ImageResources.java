package com.ViktorDikov.BetrayalCharacterStats.Data;

import com.ViktorDikov.BetrayalCharacterStats.R;

public class ImageResources {

    public static int GetChar(int id) {
        switch (id) {
            default:
            case 0:
                return R.drawable.char_blue_madame_zostra;
            case 1:
                return R.drawable.char_blue_vivian_lopez;
            case 2:
                return R.drawable.char_green_brandon_jaspers;
            case 3:
                return R.drawable.char_green_peter_akimoto;
            case 4:
                return R.drawable.char_purple_heather_granville;
            case 5:
                return R.drawable.char_purple_jenny_leclerc;
            case 6:
                return R.drawable.char_red_darrin_flash_williams;
            case 7:
                return R.drawable.char_red_ox_bellows;
            case 8:
                return R.drawable.char_white_father_rhinehardt;
            case 9:
                return R.drawable.char_white_professor_longfellow;
            case 10:
                return R.drawable.char_yellow_missy_dubourde;
            case 11:
                return R.drawable.char_yellow_zoe_ingstrom;
        }
    }

    public static int GetCharPortrait(int id) {
        switch (id) {
            default:
            case 0:
                return R.drawable.char_blue_madame_zostra_portrait;
            case 1:
                return R.drawable.char_blue_vivian_lopez_portrait;
            case 2:
                return R.drawable.char_green_brandon_jaspers_portrait;
            case 3:
                return R.drawable.char_green_peter_akimoto_portrait;
            case 4:
                return R.drawable.char_purple_heather_granville_portrait;
            case 5:
                return R.drawable.char_purple_jenny_leclerc_portrait;
            case 6:
                return R.drawable.char_red_darrin_flash_williams_portrait;
            case 7:
                return R.drawable.char_red_ox_bellows_portrait;
            case 8:
                return R.drawable.char_white_father_rhinehardt_portrait;
            case 9:
                return R.drawable.char_white_professor_longfellow_portrait;
            case 10:
                return R.drawable.char_yellow_missy_dubourde_portrait;
            case 11:
                return R.drawable.char_yellow_zoe_ingstrom_portrait;
        }
    }
}
