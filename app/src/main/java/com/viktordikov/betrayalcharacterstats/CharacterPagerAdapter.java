package com.viktordikov.betrayalcharacterstats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.viktordikov.betrayalcharacterstats.Data.CharacterOrderProvider;

import java.util.ArrayList;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class CharacterPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment currentFragment;
    private boolean refreshAll;
    private FragmentActivity fragmentActivity;
    private ArrayList<Integer> ids;

    public CharacterFragment getCurrentFragment() {
        return (CharacterFragment) currentFragment;
    }

    public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
        this.currentFragment = ((Fragment) paramObject);
    }

    public CharacterPagerAdapter(FragmentManager fragmentManager, FragmentActivity fa) {
        super(fragmentManager);
        fragmentActivity = fa;

        CharacterOrderProvider order = new CharacterOrderProvider(fragmentActivity);
        ids = order.getIDs();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CharacterFragment();
        Bundle args = new Bundle();
        args.putInt(CharacterFragment.ARG_ID, ids.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        if (refreshAll || currentFragment.equals(object))
            return POSITION_NONE;
        return POSITION_UNCHANGED;
    }

    public void notifyCurrChanged(ArrayList<Integer> ids) {
        this.ids = ids;
        refreshAll = false;
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        CharacterOrderProvider order = new CharacterOrderProvider(fragmentActivity);
        ids = order.getIDs();
        refreshAll = true;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentActivity.getResources().getStringArray(R.array.titles)[position];
    }
}