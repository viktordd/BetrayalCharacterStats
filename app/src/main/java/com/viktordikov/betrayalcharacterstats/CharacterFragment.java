package com.viktordikov.betrayalcharacterstats;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout.LayoutParams;

import com.viktordikov.betrayalcharacterstats.Data.AsyncResult;
import com.viktordikov.betrayalcharacterstats.Data.CharacterStats;
import com.viktordikov.betrayalcharacterstats.Data.CharacterStatsProvider;
import com.viktordikov.betrayalcharacterstats.Helpers.ImageLoaderTask;
import com.viktordikov.betrayalcharacterstats.Helpers.PinDetails;
import com.viktordikov.betrayalcharacterstats.Helpers.PinTouchListener;
import com.viktordikov.betrayalcharacterstats.databinding.FragmentCharacterBinding;

/**
 * A fragment that launches other parts of the application.
 */
public class CharacterFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {

    public static final String ARG_ID = "position";

    private View rootView;
    private int mId;
    private CharacterStats m_stats;
    private PinDetails Pins;
    private FragmentCharacterBinding binding;

    public int Width = -1;
    public int Height = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_character, container, false);
        rootView = binding.getRoot();

        BottomSheetBehavior.from(binding.bottomSheet);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        mId = args.getInt(ARG_ID);

        CharacterStatsProvider statsProvider = new CharacterStatsProvider(getActivity(), mId);
        m_stats = statsProvider.getStats();

        binding.setCharacterStats(m_stats);
    }

    @Override
    public void onGlobalLayout() {
        removeOnGlobalLayoutListener();
        Width = binding.rlCharacterContainer.getWidth();
        Height = binding.rlCharacterContainer.getHeight();

        Resources r = getResources();

        // Load bitmaps asynchronously
        new ImageLoaderTask(this, Width, Height, r).execute();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void removeOnGlobalLayoutListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            //noinspection deprecation
            rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        CharacterStatsProvider statsProvider = new CharacterStatsProvider(getActivity(), mId);
        statsProvider.setStats(m_stats);
        statsProvider.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (Width < 0)
            removeOnGlobalLayoutListener();
    }

    public void ResetChar() {
        m_stats.ResetToDefaults();
    }

    // Called when character images are loaded
    public void SetCharacterImages(AsyncResult result) {
        if (result == null || result.Bitmap == null || result.Pins == null)
            return;

        final CharactersViewPagerFragment parent = (CharactersViewPagerFragment) getParentFragment();
        ViewPager vp = parent.getViewPager();
        Pins = result.Pins;

        binding.ivCharacter.setImageBitmap(result.Bitmap);

        binding.pins.ivSpeed.setImageBitmap(Pins.getSpeedPinImg());
        binding.pins.ivSpeed.setOnTouchListener(new PinTouchListener(m_stats.getSpeed(), Pins.getPinPos(PinDetails.SPEED_PINS), vp) {
            @Override
            protected void SetStats(int pos, boolean touchEnd) {
                m_stats.setSpeed(pos);
                parent.sendMessage(mId, m_stats);
            }
        });

        binding.pins.ivMight.setImageBitmap(Pins.getMightPinImg());
        binding.pins.ivMight.setOnTouchListener(new PinTouchListener(m_stats.getMight(), Pins.getPinPos(PinDetails.MIGHT_PINS), vp) {
            @Override
            protected void SetStats(int pos, boolean touchEnd) {
                m_stats.setMight(pos);
                parent.sendMessage(mId, m_stats);
            }
        });

        binding.pins.ivSanity.setImageBitmap(Pins.getSanityPinImg());
        binding.pins.ivSanity.setOnTouchListener(new PinTouchListener(m_stats.getSanity(), Pins.getPinPos(PinDetails.SANITY_PINS), vp) {
            @Override
            protected void SetStats(int pos, boolean touchEnd) {
                m_stats.setSanity(pos);
                parent.sendMessage(mId, m_stats);
            }
        });

        binding.pins.ivKnowledge.setImageBitmap(Pins.getKnowledgePinImg());
        binding.pins.ivKnowledge.setOnTouchListener(new PinTouchListener(m_stats.getKnowledge(), Pins.getPinPos(PinDetails.KNOWLEDGE_PINS), vp) {
            @Override
            protected void SetStats(int pos, boolean touchEnd) {
                m_stats.setKnowledge(pos);
                parent.sendMessage(mId, m_stats);
            }
        });

        UpdateStats(BR._all);

        m_stats.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                UpdateStats(i);
            }
        });
    }

    private void UpdateStats(int i) {
        if (Pins == null)
            return;

        LayoutParams layoutParams;

        if (i == BR._all || i == BR.speed) {
            Point speedPos = Pins.getPinPos(PinDetails.SPEED_PINS, m_stats.getSpeed());
            layoutParams = (LayoutParams) binding.pins.ivSpeed.getLayoutParams();
            layoutParams.leftMargin = speedPos.x;
            layoutParams.topMargin = speedPos.y;
            binding.pins.ivSpeed.setLayoutParams(layoutParams);
        }

        if (i == BR._all || i == BR.might) {
            Point mightPos = Pins.getPinPos(PinDetails.MIGHT_PINS, m_stats.getMight());
            layoutParams = (LayoutParams) binding.pins.ivMight.getLayoutParams();
            layoutParams.leftMargin = mightPos.x;
            layoutParams.topMargin = mightPos.y;
            binding.pins.ivMight.setLayoutParams(layoutParams);
        }

        if (i == BR._all || i == BR.sanity) {
            Point sanityPos = Pins.getPinPos(PinDetails.SANITY_PINS, m_stats.getSanity());
            layoutParams = (LayoutParams) binding.pins.ivSanity.getLayoutParams();
            layoutParams.leftMargin = sanityPos.x;
            layoutParams.topMargin = sanityPos.y;
            binding.pins.ivSanity.setLayoutParams(layoutParams);
        }

        if (i == BR._all || i == BR.knowledge) {
            Point knowledgePos = Pins.getPinPos(PinDetails.KNOWLEDGE_PINS, m_stats.getKnowledge());
            layoutParams = (LayoutParams) binding.pins.ivKnowledge.getLayoutParams();
            layoutParams.leftMargin = knowledgePos.x;
            layoutParams.topMargin = knowledgePos.y;
            binding.pins.ivKnowledge.setLayoutParams(layoutParams);
        }

        CharactersViewPagerFragment parent = (CharactersViewPagerFragment) getParentFragment();
        if (parent.getSectionsPagerAdapter().getCurrentFragment() == this)
            parent.sendMessage(mId, m_stats);
    }

    public int getCharID() {
        return this.mId;
    }

    public CharacterStats getStats() {
        return this.m_stats;
    }
}
