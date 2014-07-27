package com.ViktorDikov.BetrayalCharacterStats;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ViktorDikov.BetrayalCharacterStats.CharacterActivity.AsynchResult;
import com.ViktorDikov.BetrayalCharacterStats.Data.CharacterStats;
import com.ViktorDikov.BetrayalCharacterStats.Data.CharacterStatsProvider;

/**
 * A fragment that launches other parts of the application.
 */
public class CharacterFragment extends Fragment {

	public static final String ARG_ID = "position";
	public static final String ARG_ORIENTATION = "orientation";

	private int mId;
	private CharacterStats m_stats;
	private PinDetails Pins;

	private ImageView character;
	private ImageView speedPin;
	private ImageView mightPin;
	private ImageView sanityPin;
	private ImageView knowledgePin;

	private TextView age;
	private TextView height;
	private TextView weight;
	private TextView hobbies;
	private TextView birthday;
	private TextView speed;
	private TextView might;
	private TextView sanity;
	private TextView knowledge;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_character, container, false);

		character = (ImageView) rootView.findViewById(R.id.ivCharacter);
		speedPin = (ImageView) rootView.findViewById(R.id.ivSpeed);
		mightPin = (ImageView) rootView.findViewById(R.id.ivMight);
		sanityPin = (ImageView) rootView.findViewById(R.id.ivSanity);
		knowledgePin = (ImageView) rootView.findViewById(R.id.ivKnowledge);

		age = (TextView) rootView.findViewById(R.id.tvAge);
		height = (TextView) rootView.findViewById(R.id.tvHeight);
		weight = (TextView) rootView.findViewById(R.id.tvWeight);
		hobbies = (TextView) rootView.findViewById(R.id.tvHobbies);
		birthday = (TextView) rootView.findViewById(R.id.tvBirthday);
		speed = (TextView) rootView.findViewById(R.id.tvSpeed);
		might = (TextView) rootView.findViewById(R.id.tvMight);
		sanity = (TextView) rootView.findViewById(R.id.tvSanity);
		knowledge = (TextView) rootView.findViewById(R.id.tvKnowledge);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle args = getArguments();
		mId = args.getInt(ARG_ID);

		CharacterStatsProvider statsProvider = new CharacterStatsProvider(getActivity(), mId);
		m_stats = statsProvider.getStats();

		age.setText(Integer.toString(m_stats.getAge()));
		height.setText(m_stats.getHeight());
		weight.setText(Integer.toString(m_stats.getWeight()));
		hobbies.setText(m_stats.getHobbies());
		birthday.setText(m_stats.getBirthday());
		SetSpeedValue();
		SetMightValue();
		SetSanityValue();
		SetKnowledgeValue();

		// Load bitmaps asynchronously
		((CharacterActivity) getActivity()).loadBitmaps(this);
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

		age = null;
		weight = null;
		height = null;
		hobbies = null;
		birthday = null;
		speed = null;
		might = null;
		sanity = null;
		knowledge = null;
	}

	public void ResetChar() {
		m_stats.ResetToDefaults();
		SetSpeedValue();
		SetMightValue();
		SetSanityValue();
		SetKnowledgeValue();
		UpdateStats();
	}

	private void SetSpeedValue() {
		speed.setText(Integer.toString(m_stats.getSpeedVal()));
		speed.setTextColor(getResources().getColor((m_stats.getSpeed() == m_stats.getSpeedDefault() ? R.color.Green : R.color.White)));

	}

	private void SetMightValue() {
		might.setText(Integer.toString(m_stats.getMightVal()));
		might.setTextColor(getResources().getColor((m_stats.getMight() == m_stats.getMightDefault() ? R.color.Green : R.color.White)));
	}

	private void SetSanityValue() {
		sanity.setText(Integer.toString(m_stats.getSanityVal()));
		sanity.setTextColor(getResources().getColor((m_stats.getSanity() == m_stats.getSanityDefault() ? R.color.Green : R.color.White)));
	}

	private void SetKnowledgeValue() {
		knowledge.setText(Integer.toString(m_stats.getKnowledgeVal()));
		knowledge.setTextColor(getResources().getColor((m_stats.getKnowledge() == m_stats.getKnowledgeDefault() ? R.color.Green : R.color.White)));
	}

	// Called when character images are loaded
	public void SetCharacterImages(AsynchResult result) {
		if (character == null || speedPin == null || mightPin == null || sanityPin == null || knowledgePin == null || result == null || result.Bitmap == null
				|| result.Pins == null)
			return;

		CharacterActivity activity = (CharacterActivity) getActivity();
		ViewPager vp = activity.getViewPager();
		character.setImageBitmap(result.Bitmap);
		Pins = result.Pins;

		speedPin.setImageBitmap(Pins.getSpeedPinImg());
		speedPin.setOnTouchListener(new PinTouchListener(m_stats.getSpeed(), Pins.getPinPos(PinDetails.SPEED_PINS), vp) {
			@Override
			protected void SetStats(int pos) {
				m_stats.setSpeed(pos);
				SetSpeedValue();
			}
		});

		mightPin.setImageBitmap(Pins.getMightPinImg());
		mightPin.setOnTouchListener(new PinTouchListener(m_stats.getMight(), Pins.getPinPos(PinDetails.MIGHT_PINS), vp) {
			@Override
			protected void SetStats(int pos) {
				m_stats.setMight(pos);
				SetMightValue();
			}
		});

		sanityPin.setImageBitmap(Pins.getSanityPinImg());
		sanityPin.setOnTouchListener(new PinTouchListener(m_stats.getSanity(), Pins.getPinPos(PinDetails.SANITY_PINS), vp) {
			@Override
			protected void SetStats(int pos) {
				m_stats.setSanity(pos);
				SetSanityValue();
			}
		});

		knowledgePin.setImageBitmap(Pins.getKnowledgePinImg());
		knowledgePin.setOnTouchListener(new PinTouchListener(m_stats.getKnowledge(), Pins.getPinPos(PinDetails.KNOWLEDGE_PINS), vp) {
			@Override
			protected void SetStats(int pos) {
				m_stats.setKnowledge(pos);
				SetKnowledgeValue();
			}
		});

		UpdateStats();
	}

	private void UpdateStats() {
		if (Pins == null)
			return;

		LayoutParams layoutParams;

		Point speedPos = Pins.getPinPos(PinDetails.SPEED_PINS, m_stats.getSpeed());
		layoutParams = (LayoutParams) speedPin.getLayoutParams();
		layoutParams.leftMargin = speedPos.x;
		layoutParams.topMargin = speedPos.y;
		speedPin.setLayoutParams(layoutParams);

		Point mightPos = Pins.getPinPos(PinDetails.MIGHT_PINS, m_stats.getMight());
		layoutParams = (LayoutParams) mightPin.getLayoutParams();
		layoutParams.leftMargin = mightPos.x;
		layoutParams.topMargin = mightPos.y;
		mightPin.setLayoutParams(layoutParams);

		Point sanityPos = Pins.getPinPos(PinDetails.SANITY_PINS, m_stats.getSanity());
		layoutParams = (LayoutParams) sanityPin.getLayoutParams();
		layoutParams.leftMargin = sanityPos.x;
		layoutParams.topMargin = sanityPos.y;
		sanityPin.setLayoutParams(layoutParams);

		Point knowledgePos = Pins.getPinPos(PinDetails.KNOWLEDGE_PINS, m_stats.getKnowledge());
		layoutParams = (LayoutParams) knowledgePin.getLayoutParams();
		layoutParams.leftMargin = knowledgePos.x;
		layoutParams.topMargin = knowledgePos.y;
		knowledgePin.setLayoutParams(layoutParams);
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
}
