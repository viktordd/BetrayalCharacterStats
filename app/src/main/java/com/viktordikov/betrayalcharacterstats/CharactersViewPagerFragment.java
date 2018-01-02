package com.viktordikov.betrayalcharacterstats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.viktordikov.betrayalcharacterstats.Data.SettingsProvider;
import com.viktordikov.betrayalcharacterstats.Helpers.CastChannel;
import com.viktordikov.betrayalcharacterstats.Helpers.CharacterPagerAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CharactersViewPagerFragment extends Fragment {

    private static final String TAG = "CharactersFragment";

    private CharacterPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private CastChannel mCastChannel;
    private SessionManager mSessionManager;
    private CastSession mCastSession;
    private final SessionManagerListener mSessionManagerListener = new SessionManagerListenerImpl();

    public CharacterPagerAdapter getSectionsPagerAdapter() {
        return mSectionsPagerAdapter;
    }
    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_characters_view_pager, container, false);
        setHasOptionsMenu(true);

        mSectionsPagerAdapter = new CharacterPagerAdapter(getChildFragmentManager(), getActivity());

        // Set up the ViewPager with the sections adapter.
        mViewPager = rootView.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setCharTitleByPos(position);
                CharacterFragment ch = mSectionsPagerAdapter.getFragment(position);
                if (ch != null)
                    sendMessage(ch.getCharID(), ch.getStats());
                ((MainActivity)getActivity()).setSelectedMenuItem(position);
            }
        });
        setCharTitleByPos(0);

        mCastChannel = new CastChannel();
        mSessionManager = CastContext.getSharedInstance(getActivity()).getSessionManager();
        mCastSession = mSessionManager.getCurrentCastSession();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        CastButtonFactory.setUpMediaRouteButton(getActivity().getApplicationContext(), menu, R.id.media_route_menu_item);
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
            flipCharacter(mViewPager.getCurrentItem());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void flipCharacter(int pos){
        int currPos = mViewPager.getCurrentItem();

        CharacterOrderProvider order = new CharacterOrderProvider(getActivity());
        ArrayList<Integer> ids = order.getIDs();

        int charId = ids.get(pos);
        int newId = charId + (charId % 2 == 0 ? 1 : -1);
        ids.set(pos, newId);

        order.setIDs(ids);
        order.apply();

        mSectionsPagerAdapter.notifyCurrChanged(ids);

        // Fragment will call sendMessage when it initializes.

        MainActivity activity = (MainActivity) getActivity();
        activity.addMenuItems();
        if (pos == currPos) {
            activity.setSelectedMenuItem(pos);
            setCharTitleById(newId);
        }
    }

    public void setCharTitleByPos(int pos){
        CharacterOrderProvider order = new CharacterOrderProvider(getActivity());
        setCharTitleById(order.getIDs().get(pos));
    }

    public void setCharTitleById(int id){
        ((MainActivity)getActivity()).setActionBarTitle(mSectionsPagerAdapter.getPageTitle(id));
    }

    public void setCurrentItem(int pos){
        mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onResume() {
        super.onResume();

        CharacterOrderProvider order = new CharacterOrderProvider(getActivity());
        mViewPager.setCurrentItem(order.getTabPosition());

        mCastSession = mSessionManager.getCurrentCastSession();
        mSessionManager.addSessionManagerListener(mSessionManagerListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        CharacterOrderProvider order = new CharacterOrderProvider(getActivity());
        order.setTabPosition(mViewPager.getCurrentItem());
        order.apply();

        mSessionManager.removeSessionManagerListener(mSessionManagerListener);
        mCastSession = null;
    }

    public void sendMessage(int charId, CharacterStats stats) {
        if (mCastChannel == null || mCastSession == null) {
            return;
        }
        final SettingsProvider settings = new SettingsProvider(getActivity());

        try {
            JSONObject msg = new JSONObject();
            msg.put("name", settings.getName());
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
            getActivity().invalidateOptionsMenu();

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
            getActivity().invalidateOptionsMenu();
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
