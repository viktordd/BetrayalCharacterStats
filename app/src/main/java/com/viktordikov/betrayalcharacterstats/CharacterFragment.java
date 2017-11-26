package com.viktordikov.betrayalcharacterstats;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
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
import com.viktordikov.betrayalcharacterstats.databinding.FragmentCharacterBinding;

import java.lang.ref.WeakReference;

/**
 * A fragment that launches other parts of the application.
 */
public class CharacterFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {

	public static final String ARG_ID = "position";

	private int mId;
	private CharacterStats m_stats;
	private PinDetails Pins;
	private FragmentCharacterBinding binding;

	private View rootView;

    public int Width = -1;
    public int Height = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_character, container, false);
		rootView = binding.getRoot();

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
		UpdateStats();
        ((MainActivity) getActivity()).sendMessage(mId, m_stats);
	}

	// Called when character images are loaded
	public void SetCharacterImages(AsyncResult result) {
		if (result == null || result.Bitmap == null || result.Pins == null)
			return;

        final MainActivity mainActivity = (MainActivity) getActivity();
        ViewPager vp = mainActivity.getViewPager();
        binding.ivCharacter.setImageBitmap(result.Bitmap);
		Pins = result.Pins;

		binding.pins.ivSpeed.setImageBitmap(Pins.getSpeedPinImg());
		binding.pins.ivSpeed.setOnTouchListener(new PinTouchListener(m_stats.getSpeed(), Pins.getPinPos(PinDetails.SPEED_PINS), vp) {
			@Override
			protected void SetStats(int pos, boolean touchEnd) {
				m_stats.setSpeed(pos);
                mainActivity.sendMessage(mId, m_stats);
			}
		});

		binding.pins.ivMight.setImageBitmap(Pins.getMightPinImg());
		binding.pins.ivMight.setOnTouchListener(new PinTouchListener(m_stats.getMight(), Pins.getPinPos(PinDetails.MIGHT_PINS), vp) {
			@Override
			protected void SetStats(int pos, boolean touchEnd) {
				m_stats.setMight(pos);
				mainActivity.sendMessage(mId, m_stats);
			}
		});

		binding.pins.ivSanity.setImageBitmap(Pins.getSanityPinImg());
		binding.pins.ivSanity.setOnTouchListener(new PinTouchListener(m_stats.getSanity(), Pins.getPinPos(PinDetails.SANITY_PINS), vp) {
			@Override
			protected void SetStats(int pos, boolean touchEnd) {
				m_stats.setSanity(pos);
                mainActivity.sendMessage(mId, m_stats);
			}
		});

		binding.pins.ivKnowledge.setImageBitmap(Pins.getKnowledgePinImg());
		binding.pins.ivKnowledge.setOnTouchListener(new PinTouchListener(m_stats.getKnowledge(), Pins.getPinPos(PinDetails.KNOWLEDGE_PINS), vp) {
			@Override
			protected void SetStats(int pos, boolean touchEnd) {
				m_stats.setKnowledge(pos);
                mainActivity.sendMessage(mId, m_stats);
			}
		});

		UpdateStats();
	}

	private void UpdateStats() {
		if (Pins == null)
			return;

		LayoutParams layoutParams;

		Point speedPos = Pins.getPinPos(PinDetails.SPEED_PINS, m_stats.getSpeed());
		layoutParams = (LayoutParams) binding.pins.ivSpeed.getLayoutParams();
		layoutParams.leftMargin = speedPos.x;
		layoutParams.topMargin = speedPos.y;
        binding.pins.ivSpeed.setLayoutParams(layoutParams);

		Point mightPos = Pins.getPinPos(PinDetails.MIGHT_PINS, m_stats.getMight());
		layoutParams = (LayoutParams)  binding.pins.ivMight.getLayoutParams();
		layoutParams.leftMargin = mightPos.x;
		layoutParams.topMargin = mightPos.y;
        binding.pins.ivMight.setLayoutParams(layoutParams);

		Point sanityPos = Pins.getPinPos(PinDetails.SANITY_PINS, m_stats.getSanity());
		layoutParams = (LayoutParams) binding.pins.ivSanity.getLayoutParams();
		layoutParams.leftMargin = sanityPos.x;
		layoutParams.topMargin = sanityPos.y;
        binding.pins.ivSanity.setLayoutParams(layoutParams);

		Point knowledgePos = Pins.getPinPos(PinDetails.KNOWLEDGE_PINS, m_stats.getKnowledge());
		layoutParams = (LayoutParams) binding.pins.ivKnowledge.getLayoutParams();
		layoutParams.leftMargin = knowledgePos.x;
		layoutParams.topMargin = knowledgePos.y;
        binding.pins.ivKnowledge.setLayoutParams(layoutParams);
	}

	public int GetImageResource() {
		switch (mId) {
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

	public int getCharID() {
		return this.mId;
	}

    public CharacterStats getStats() {
        return this.m_stats;
    }

	@Override
	public void onGlobalLayout() {
		removeOnGlobalLayoutListener();
		Width = rootView.getWidth();
		Height = rootView.getHeight();

		// Load bitmaps asynchronously
		loadBitmaps();
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

	private void loadBitmaps() {
		new ImageLoaderTask(this, Width, Height, getResources()).execute();
	}
}
