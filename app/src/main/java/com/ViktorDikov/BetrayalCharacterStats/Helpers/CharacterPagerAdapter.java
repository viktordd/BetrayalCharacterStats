package com.ViktorDikov.BetrayalCharacterStats.Helpers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ViktorDikov.BetrayalCharacterStats.CharacterFragment;
import com.ViktorDikov.BetrayalCharacterStats.Data.CharacterOrderProvider;
import com.ViktorDikov.BetrayalCharacterStats.R;

import java.lang.ref.WeakReference;
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
    private ArrayList<WeakReference<CharacterFragment>> fragments;

    public CharacterFragment getCurrentFragment() {
        return (CharacterFragment) currentFragment;
    }
    public CharacterFragment getFragment(int position) {
        if (fragments.size() <= position) {
            return null;
        }
        return fragments.get(position).get();
    }
    public ArrayList<CharacterFragment> getAllFragments() {
        ArrayList<CharacterFragment> all = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            CharacterFragment f = fragments.get(i).get();
            if (f != null)
                all.add(f);
        }
        return all;
    }

    public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
        this.currentFragment = ((Fragment) paramObject);
    }

    public CharacterPagerAdapter(FragmentManager fragmentManager, FragmentActivity fa) {
        super(fragmentManager);
        fragmentActivity = fa;

        CharacterOrderProvider order = new CharacterOrderProvider(fragmentActivity);
        ids = order.getIDs();
        fragments = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            fragments.add(new WeakReference<CharacterFragment>(null));
        }
    }

    @Override
    public Fragment getItem(int position) {
        CharacterFragment fragment = new CharacterFragment();
        fragments.set(position, new WeakReference<>(fragment));

        Bundle args = new Bundle();
        args.putInt(CharacterFragment.ARG_ID, ids.get(position));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
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
    public CharSequence getPageTitle(int id) {
        return fragmentActivity.getResources().getStringArray(R.array.titles)[id];
    }
}