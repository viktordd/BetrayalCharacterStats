package com.viktordikov.betrayalcharacterstats;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Spinner;

import com.viktordikov.betrayalcharacterstats.Data.CharacterOrderProvider;

import java.util.ArrayList;

public class CharacterActivity extends FragmentActivity implements ActionBar.TabListener,OnSharedPreferenceChangeListener {

	public static final String SAVED_TAB_POSITION = "saved_tab_position";
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	CharacterPagerAdapter mCharacterPagerAdapter;

	View content;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	public ViewPager getViewPager() {
		return mViewPager;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character);

		content = findViewById(android.R.id.content);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mCharacterPagerAdapter = new CharacterPagerAdapter(getSupportFragmentManager(), this);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.character_pager);
		mViewPager.setAdapter(mCharacterPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				final ActionBar actionBar = getActionBar();
				if (actionBar != null) {
					actionBar.setSelectedNavigationItem(position);
				}
				ViewParent root = content.getParent();
				findAndUpdateSpinner(root, position);
			}

			/**
			 * Searches the view hierarchy excluding the content view for a
			 * possible Spinner in the ActionBar.
			 *
			 * @param root
			 *            The parent of the content view
			 * @param position
			 *            The position that should be selected
			 * @return if the spinner was found and adjusted
			 */
			private boolean findAndUpdateSpinner(Object root, int position) {
				if (root instanceof Spinner) {
					// Found the Spinner
					Spinner spinner = (Spinner) root;
					spinner.setSelection(position);
					return true;
				} else if (root instanceof ViewGroup) {
					ViewGroup group = (ViewGroup) root;
					if (group.getId() != android.R.id.content) {
						// Found a container that isn't the container holding
						// our screen layout
						for (int i = 0; i < group.getChildCount(); i++) {
							if (findAndUpdateSpinner(group.getChildAt(i), position)) {
								// Found and done searching the View tree
								return true;
							}
						}
					}
				}
				// Nothing found
				return false;
			}
		});

		if (actionBar != null)
			AddTitles(actionBar);
		CharacterOrderProvider order = new CharacterOrderProvider(this);
		order.getPrefs().registerOnSharedPreferenceChangeListener(this);
	}

	private void AddTitles(ActionBar actionBar) {
		CharacterOrderProvider order = new CharacterOrderProvider(this);
		ArrayList<Integer> ids = order.getIDs();

		// For each character, add a tab to the action bar.
		for (Integer id : ids) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mCharacterPagerAdapter.getPageTitle(id)).setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_character, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_reset) {
			this.mCharacterPagerAdapter.getCurrentFragment().ResetChar();
			return true;
		}
		if (item.getItemId() == R.id.menu_flip) {
			int id = mCharacterPagerAdapter.getCurrentFragment().getCharID();
			int pos = mViewPager.getCurrentItem();

			int newID = id + (id % 2 == 0 ? 1 : -1);

			CharacterOrderProvider order = new CharacterOrderProvider(this);
			ArrayList<Integer> ids = order.getIDs();
			ids.set(pos, newID);
			order.setIDs(ids);
			order.apply();

			ActionBar actionBar = getActionBar();
			if (actionBar != null) {
				Tab tab = actionBar.getTabAt(pos);
				if (tab != null) {
					tab.setText(mCharacterPagerAdapter.getPageTitle(newID));
				}
			}

			mCharacterPagerAdapter.notifyCurrChanged(ids);

			return true;
		}
		if (item.getItemId() == R.id.menu_reorder) {
			startActivity(new Intent(this, ReorderCharsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
		mViewPager.setCurrentItem(sharedPrefs.getInt(SAVED_TAB_POSITION, 0));
	}

	@Override
	protected void onPause() {
		super.onPause();

		SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
		editor.putInt(SAVED_TAB_POSITION, mViewPager.getCurrentItem());
		editor.apply();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCharacterPagerAdapter = null;
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.removeAllTabs();
		}
		mViewPager.removeAllViews();
		mViewPager = null;
		CharacterOrderProvider order = new CharacterOrderProvider(this);
		order.getPrefs().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
		if (key.startsWith(CharacterOrderProvider.CHANGED)) {

			mCharacterPagerAdapter.notifyDataSetChanged();
			ActionBar actionBar = getActionBar();
			if (actionBar != null) {
				actionBar.removeAllTabs();
				AddTitles(actionBar);
			}

			SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
			editor.putInt(SAVED_TAB_POSITION, 0);
			editor.apply();
		}
	}

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
			return getResources().getStringArray(R.array.titles)[position];
		}
	}
}