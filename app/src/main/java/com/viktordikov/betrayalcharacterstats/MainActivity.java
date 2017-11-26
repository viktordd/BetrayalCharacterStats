package com.viktordikov.betrayalcharacterstats;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.viktordikov.betrayalcharacterstats.Data.CharacterOrderProvider;
import com.viktordikov.betrayalcharacterstats.Data.CharacterStats;
import com.viktordikov.betrayalcharacterstats.Data.CharacterStatsProvider;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SAVED_TAB_POSITION = "saved_tab_position";
    private static final String TAG = "MainActivity";

    private CharacterPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    public ViewPager getViewPager() {
        return mViewPager;
    }
    private CastChannel mCastChannel;
    private SessionManager mSessionManager;
    private CastSession mCastSession;
    private final SessionManagerListener mSessionManagerListener = new SessionManagerListenerImpl();

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

        mSectionsPagerAdapter = new CharacterPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setCharTitleByPos(position);
                CharacterFragment ch = mSectionsPagerAdapter.getFragment(position);
                if (ch != null)
                    sendMessage(ch.getCharID(), ch.getStats());
            }
        });
        setCharTitleByPos(0);

        mCastChannel = new CastChannel();
        mSessionManager = CastContext.getSharedInstance(this).getSessionManager();
        mCastSession = mSessionManager.getCurrentCastSession();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int menuId = item.getItemId();

        if (menuId == R.id.menu_reset) {
            this.mSectionsPagerAdapter.getCurrentFragment().ResetChar();
            return true;
        }
        if (menuId == R.id.menu_flip) {
            int charId = mSectionsPagerAdapter.getCurrentFragment().getCharID();
            int pos = mViewPager.getCurrentItem();

            int newId = charId + (charId % 2 == 0 ? 1 : -1);

            CharacterOrderProvider order = new CharacterOrderProvider(this);
            ArrayList<Integer> ids = order.getIDs();
            ids.set(pos, newId);
            order.setIDs(ids);
            order.apply();

            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                ActionBar.Tab tab = actionBar.getTabAt(pos);
                if (tab != null) {
                    tab.setText(mSectionsPagerAdapter.getPageTitle(newId));
                }
            }

            mSectionsPagerAdapter.notifyCurrChanged(ids);

            setCharTitleById(newId);

            CharacterStatsProvider statsProvider = new CharacterStatsProvider(this, newId);
            sendMessage(newId, statsProvider.getStats());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //displayView(item.getItemId());
        return true;
    }

//    public void displayView(int viewId) {
//
//        CharacterFragment fragment = null;
//        String title = getString(R.string.app_name);
//
//        switch (viewId) {
//            case R.id.nav_camera:
//                fragment = getCharacterFragment(0);
//                title  = "0";
//
//                break;
//            case R.id.nav_gallery:
//                fragment = getCharacterFragment(1);
//                title = "1";
//                break;
//
//        }
//
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
//        }
//
//        // set the toolbar title
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(title);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//    }

    public void setCharTitleByPos(int pos){
        CharacterOrderProvider order = new CharacterOrderProvider(this);
        setCharTitleById(order.getIDs().get(pos));
    }

    public void setCharTitleById(int id){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mSectionsPagerAdapter.getPageTitle(id));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        mViewPager.setCurrentItem(sharedPrefs.getInt(SAVED_TAB_POSITION, 0));

        mCastSession = mSessionManager.getCurrentCastSession();
        mSessionManager.addSessionManagerListener(mSessionManagerListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt(SAVED_TAB_POSITION, mViewPager.getCurrentItem());
        editor.apply();

        mSessionManager.removeSessionManagerListener(mSessionManagerListener);
        mCastSession = null;
    }

    public void sendMessage(int charId, CharacterStats stats) {
        if (mCastChannel == null || mCastSession == null) {
            return;
        }
        try {
            JSONObject msg = new JSONObject();
            msg.put("name", "name");
            msg.put("char", getResources().getStringArray(R.array.char_names)[charId]);
            msg.put("speed", stats.getSpeed());
            msg.put("might", stats.getMight());
            msg.put("sanity", stats.getSanity());
            msg.put("knowledge", stats.getKnowledge());

            mCastSession.sendMessage(mCastChannel.getNamespace(), msg.toString())
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status result) {
                            if (!result.isSuccess()) {
                                Log.e(TAG, "Sending message failed");
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception while sending message", e);
        }
    }

    private class SessionManagerListenerImpl implements SessionManagerListener {
        @Override
        public void onSessionStarting(Session session) {

        }

        @Override
        public void onSessionStarted(Session session, String sessionId) {
            invalidateOptionsMenu();

            try {
                mCastSession = mSessionManager.getCurrentCastSession();
                mCastSession.setMessageReceivedCallbacks(mCastChannel.getNamespace(), mCastChannel);
                CharacterFragment ch = mSectionsPagerAdapter.getCurrentFragment();
                sendMessage(ch.getCharID(), ch.getStats());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception while creating channel", e);
            }
        }

        @Override
        public void onSessionStartFailed(Session session, int i) {

        }

        @Override
        public void onSessionEnding(Session session) {

        }

        @Override
        public void onSessionResumed(Session session, boolean wasSuspended) {
            invalidateOptionsMenu();
            mCastSession = mSessionManager.getCurrentCastSession();
            CharacterFragment ch = mSectionsPagerAdapter.getCurrentFragment();
            if (ch != null)
                sendMessage(ch.getCharID(), ch.getStats());
        }

        @Override
        public void onSessionResumeFailed(Session session, int i) {

        }

        @Override
        public void onSessionSuspended(Session session, int i) {

        }

        @Override
        public void onSessionEnded(Session session, int error) {
            mCastSession = null;
        }

        @Override
        public void onSessionResuming(Session session, String s) {

        }
    }
}
