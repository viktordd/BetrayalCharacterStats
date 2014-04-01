package com.ViktorDikov.BetrayalCharacterStats;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.ViktorDikov.BetrayalCharacterStats.Data.CharacterStats;
import com.ViktorDikov.BetrayalCharacterStats.Data.CharacterStatsProvider;

/**
 * A fragment that launches other parts of the application.
 */
public class CharacterFragment extends Fragment implements OnGlobalLayoutListener {

	public static final String ARG_ID = "position";
	public static final String ARG_ORIENTATION = "orientation";

	private int m_id;
	private CharacterStats m_stats;

	private boolean waitingForGlobalLayout = false;
	private RelativeLayout pins;

	private ImageView character;
	private ImageView speedPin;
	private ImageView mightPin;
	private ImageView sanityPin;
	private ImageView knowledgePin;

	private Button increaseSpeed;
	private Button decreaseSpeed;
	private Button increaseMight;
	private Button decreaseMight;
	private Button increaseSanity;
	private Button decreaseSanity;
	private Button increaseKnowledge;
	private Button decreaseKnowledge;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_character, container, false);

		character = (ImageView) rootView.findViewById(R.id.ivCharacter);
		pins = (RelativeLayout) rootView.findViewById(R.id.ivPins);
		speedPin = (ImageView) rootView.findViewById(R.id.ivSpeed);
		mightPin = (ImageView) rootView.findViewById(R.id.ivMight);
		sanityPin = (ImageView) rootView.findViewById(R.id.ivSanity);
		knowledgePin = (ImageView) rootView.findViewById(R.id.ivKnowledge);

		increaseSpeed = (Button) rootView.findViewById(R.id.increaseSpeed);
		decreaseSpeed = (Button) rootView.findViewById(R.id.decreaseSpeed);
		increaseMight = (Button) rootView.findViewById(R.id.increaseMight);
		decreaseMight = (Button) rootView.findViewById(R.id.decreaseMight);
		increaseSanity = (Button) rootView.findViewById(R.id.increaseSanity);
		decreaseSanity = (Button) rootView.findViewById(R.id.decreaseSanity);
		increaseKnowledge = (Button) rootView.findViewById(R.id.increaseKnowledge);
		decreaseKnowledge = (Button) rootView.findViewById(R.id.decreaseKnowledge);

		increaseSpeed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_stats.increaseSpeed();
				UpdateStats();
			}
		});
		decreaseSpeed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_stats.decreaseSpeed();
				UpdateStats();
			}
		});
		increaseMight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_stats.increaseMight();
				UpdateStats();
			}
		});
		decreaseMight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_stats.decreaseMight();
				UpdateStats();
			}
		});
		increaseSanity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_stats.increaseSanity();
				UpdateStats();
			}
		});
		decreaseSanity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_stats.decreaseSanity();
				UpdateStats();
			}
		});
		increaseKnowledge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_stats.increaseKnowledge();
				UpdateStats();
			}
		});
		decreaseKnowledge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_stats.decreaseKnowledge();
				UpdateStats();
			}
		});

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle args = getArguments();
		m_id = args.getInt(ARG_ID);

		CharacterStatsProvider statsProvider = new CharacterStatsProvider(getActivity(), m_id);
		m_stats = statsProvider.getStats();
		
		pins.getViewTreeObserver().addOnGlobalLayoutListener(this);
		waitingForGlobalLayout = true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onGlobalLayout() {
		if (pins != null) {
			CharacterActivity activity = ((CharacterActivity) getActivity());
			activity.loadBitmaps(GetImageResource(), this, pins.getWidth(), pins.getHeight(), activity.getWindowManager().getDefaultDisplay().getRotation());
			pins.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			waitingForGlobalLayout = false;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		
		CharacterStatsProvider statsProvider = new CharacterStatsProvider(getActivity(), m_id);
		statsProvider.setStats(m_stats);
		statsProvider.apply();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (waitingForGlobalLayout)
			pins.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		pins = null;

		character.setImageBitmap(null);
		speedPin.setImageBitmap(null);
		mightPin.setImageBitmap(null);
		sanityPin.setImageBitmap(null);
		knowledgePin.setImageBitmap(null);

		character = null;
		speedPin = null;
		mightPin = null;
		sanityPin = null;
		knowledgePin = null;

		increaseSpeed = null;
		decreaseSpeed = null;
		increaseMight = null;
		decreaseMight = null;
		increaseSanity = null;
		decreaseSanity = null;
		increaseKnowledge = null;
		decreaseKnowledge = null;
	}
	
	public void ResetChar() {
		CharacterStatsProvider statsProvider = new CharacterStatsProvider(getActivity(), m_id);
		m_stats.Set(statsProvider.getDefaults());
		UpdateStats();
	}

	public void SetCharacterImage(Bitmap bitmap) {
		if (character != null)
			character.setImageBitmap(bitmap);
	}

	public void SetPinImages(PinDetails pins) {
		if (speedPin == null || mightPin == null || sanityPin == null || knowledgePin == null)
			return;
		speedPin.setImageBitmap(pins.getSpeedPinImg());
		mightPin.setImageBitmap(pins.getMightPinImg());
		sanityPin.setImageBitmap(pins.getSanityPinImg());
		knowledgePin.setImageBitmap(pins.getKnowledgePinImg());
		UpdateStats();
	}

	private void UpdateStats() {
		CharacterStats s = m_stats;

		increaseSpeed.setEnabled(s.canIncreaseSpeed());
		decreaseSpeed.setEnabled(s.canDecreaseSpeed());
		increaseMight.setEnabled(s.canIncreaseMight());
		decreaseMight.setEnabled(s.canDecreaseMight());
		increaseSanity.setEnabled(s.canIncreaseSanity());
		decreaseSanity.setEnabled(s.canDecreaseSanity());
		increaseKnowledge.setEnabled(s.canIncreaseKnowledge());
		decreaseKnowledge.setEnabled(s.canDecreaseKnowledge());

		CharacterActivity activity = ((CharacterActivity) getActivity());

		Point speedPos = activity.Pins.getPinPos(0, s.getSpeed());
		LayoutParams speedParams = (LayoutParams) speedPin.getLayoutParams();
		speedParams.leftMargin = speedPos.x;
		speedParams.bottomMargin = speedPos.y;
		speedPin.setLayoutParams(speedParams);

		Point mightPos = activity.Pins.getPinPos(1, s.getMight());
		LayoutParams mightParams = (LayoutParams) mightPin.getLayoutParams();
		mightParams.leftMargin = mightPos.x;
		mightParams.topMargin = mightPos.y;
		mightPin.setLayoutParams(mightParams);

		Point sanityPos = activity.Pins.getPinPos(1, s.getSanity());
		LayoutParams sanityParams = (LayoutParams) sanityPin.getLayoutParams();
		sanityParams.rightMargin = sanityPos.x;
		sanityParams.topMargin = sanityPos.y;
		sanityPin.setLayoutParams(sanityParams);

		Point knowledgePos = activity.Pins.getPinPos(0, s.getKnowledge());
		LayoutParams knowledgeParams = (LayoutParams) knowledgePin.getLayoutParams();
		knowledgeParams.rightMargin = knowledgePos.x;
		knowledgeParams.bottomMargin = knowledgePos.y;
		knowledgePin.setLayoutParams(knowledgeParams);
	}

	private int GetImageResource() {
		switch (m_id) {
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
		return this.m_id;
	}
}
