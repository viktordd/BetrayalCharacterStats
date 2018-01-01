package com.viktordikov.betrayalcharacterstats;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.viktordikov.betrayalcharacterstats.Data.CharacterOrderProvider;
import com.viktordikov.betrayalcharacterstats.Data.ImageResources;
import com.viktordikov.betrayalcharacterstats.Data.SettingsProvider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private Menu menu;
    private int currViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();

        CharacterOrderProvider order = new CharacterOrderProvider(this);
        order.getPrefs().registerOnSharedPreferenceChangeListener(this);
        displayView(R.id.nav_characters, order.getTabPosition(), null);

        addMenuItems();
        initAlwaysOnDisplayToggle();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currViewId != R.id.nav_characters) {
                CharacterOrderProvider order = new CharacterOrderProvider(this);
                int tabPosition = order.getTabPosition();
                displayView(R.id.nav_characters, tabPosition, null);
                setSelectedMenuItem(tabPosition);
                return;
            }

            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CharacterOrderProvider order = new CharacterOrderProvider(this);
        order.getPrefs().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId(), item.getOrder(), item);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.startsWith(CharacterOrderProvider.CHANGED)) {
            addMenuItems();
        }
    }

    public void displayView(int viewId, int pos, MenuItem item) {
        Fragment fragment = null;

        switch (viewId) {
            case R.id.nav_characters:
                if (currViewId == viewId) {
                    CharactersViewPagerFragment chars = (CharactersViewPagerFragment) (getSupportFragmentManager().findFragmentById(R.id.content_frame));
                    chars.setCurrentItem(pos);
                } else {
                    fragment = new CharactersViewPagerFragment();
                    CharacterOrderProvider order = new CharacterOrderProvider(this);
                    order.setTabPosition(pos);
                    order.apply();
                }
                break;
            case R.id.nav_reorder:
                fragment = new ReorderCharsFragment();
                setActionBarTitle(getResources().getText(R.string.a_menu_re_order));
                break;
            case R.id.nav_set_player_name:
                setPlayerName();
                return;
            case R.id.nav_always_on_display:
                return;
            default:
                return;
        }

        currViewId = viewId;

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void addMenuItems() {
        Resources r = getResources();

        menu.removeGroup(R.id.nav_character_group);

        CharacterOrderProvider order = new CharacterOrderProvider(this);
        ArrayList<Integer> iDs = order.getIDs();
        int selected = order.getTabPosition();

        for (int i = 0; i < iDs.size(); i++) {
            Integer id = iDs.get(i);
            String title = r.getStringArray(R.array.titles)[id];
            MenuItem menuItem = menu.add(R.id.nav_character_group, R.id.nav_characters, i, title)
                    .setIcon(R.drawable.ic_char)
//                    .setActionView(R.layout.menu_character)
                    .setChecked(currViewId == R.id.nav_characters && i == selected);

//            View view = menuItem.getActionView();
//
//            ImageView img = view.findViewById(R.id.portrait);
//            img.setImageResource(ImageResources.GetCharPortrait(id));
//
//            TextView text = view.findViewById(R.id.text);
//            text.setText(title);
//
//            ImageView flip = view.findViewById(R.id.flip);
//            flip.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CharactersViewPagerFragment chars = (CharactersViewPagerFragment) (getSupportFragmentManager().findFragmentById(R.id.content_frame));
//                    chars.flipCurrentCharacter();
//                }
//            });
        }

        menu.setGroupCheckable(R.id.nav_character_group, true, true);
    }

    private void initAlwaysOnDisplayToggle() {
        SettingsProvider settings = new SettingsProvider(this);
        Switch alwaysOnToggle = menu.findItem(R.id.nav_always_on_display).getActionView().findViewById(R.id.toggle);

        alwaysOnToggle.setChecked(settings.getAlwaysOnDisplay());
        alwaysOnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAlwaysOnDisplay(true);
            }
        });

        setAlwaysOnDisplay(false);
    }

    private void setAlwaysOnDisplay(boolean save) {
        boolean checked = ((Switch) menu.findItem(R.id.nav_always_on_display).getActionView().findViewById(R.id.toggle)).isChecked();
        findViewById(R.id.drawer_layout).setKeepScreenOn(checked);

        if (save) {
            SettingsProvider settings = new SettingsProvider(this);
            settings.setAlwaysOnDisplay(checked);
            settings.apply();
        }
    }

    private void setPlayerName() {
        final SettingsProvider settings = new SettingsProvider(this);
        final EditText name = new EditText(this);
        name.setText(settings.getName());

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_set_player_name)
                .setMessage(R.string.msg_enter_player_name)
                .setView(name)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        settings.setName(name.getText().toString());
                        settings.apply();

                        if (currViewId == R.id.nav_characters) {
                            CharactersViewPagerFragment chars = (CharactersViewPagerFragment) (getSupportFragmentManager().findFragmentById(R.id.content_frame));
                            CharacterFragment fr = chars.getSectionsPagerAdapter().getCurrentFragment();
                            chars.sendMessage(fr.getCharID(), fr.getStats());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void setActionBarTitle(CharSequence text) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(text);
        }
    }

    public void setSelectedMenuItem(int pos) {
        menu.getItem(pos).setChecked(true);
    }
}
