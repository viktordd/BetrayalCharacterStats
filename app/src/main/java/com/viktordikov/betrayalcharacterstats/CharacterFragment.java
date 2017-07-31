package com.viktordikov.betrayalcharacterstats;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.viktordikov.betrayalcharacterstats.Data.CharacterStats;
import com.viktordikov.betrayalcharacterstats.Data.CharacterStatsProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

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

		character = rootView.findViewById(R.id.ivCharacter);
		speedPin = rootView.findViewById(R.id.ivSpeed);
		mightPin = rootView.findViewById(R.id.ivMight);
		sanityPin = rootView.findViewById(R.id.ivSanity);
		knowledgePin = rootView.findViewById(R.id.ivKnowledge);

		age = rootView.findViewById(R.id.tvAge);
		height = rootView.findViewById(R.id.tvHeight);
		weight = rootView.findViewById(R.id.tvWeight);
		hobbies = rootView.findViewById(R.id.tvHobbies);
		birthday = rootView.findViewById(R.id.tvBirthday);
		speed = rootView.findViewById(R.id.tvSpeed);
		might = rootView.findViewById(R.id.tvMight);
		sanity = rootView.findViewById(R.id.tvSanity);
		knowledge = rootView.findViewById(R.id.tvKnowledge);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle args = getArguments();
		mId = args.getInt(ARG_ID);

		CharacterStatsProvider statsProvider = new CharacterStatsProvider(getActivity(), mId);
		m_stats = statsProvider.getStats();

		age.setText(m_stats.getAgeString());
		height.setText(m_stats.getHeight());
        weight.setText(m_stats.getWeightString());
		hobbies.setText(m_stats.getHobbies());
		birthday.setText(m_stats.getBirthday());
		SetSpeedValue();
		SetMightValue();
		SetSanityValue();
		SetKnowledgeValue();

		// Load bitmaps asynchronously
		loadBitmaps(this);
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
		speed.setText(m_stats.getSpeedString());
		speed.setTextColor(getResources().getColor((m_stats.getSpeed() == m_stats.getSpeedDefault() ? R.color.Green : R.color.White)));

	}

	private void SetMightValue() {
		might.setText(m_stats.getMightString());
		might.setTextColor(getResources().getColor((m_stats.getMight() == m_stats.getMightDefault() ? R.color.Green : R.color.White)));
	}

	private void SetSanityValue() {
		sanity.setText(m_stats.getSanityString());
		sanity.setTextColor(getResources().getColor((m_stats.getSanity() == m_stats.getSanityDefault() ? R.color.Green : R.color.White)));
	}

	private void SetKnowledgeValue() {
		knowledge.setText(m_stats.getKnowledgeString());
		knowledge.setTextColor(getResources().getColor((m_stats.getKnowledge() == m_stats.getKnowledgeDefault() ? R.color.Green : R.color.White)));
	}

	// Called when character images are loaded
	public void SetCharacterImages(AsyncResult result) {
		if (character == null || speedPin == null || mightPin == null || sanityPin == null || knowledgePin == null ||
                result == null || result.Bitmap == null || result.Pins == null)
			return;

		character.setImageBitmap(result.Bitmap);
		Pins = result.Pins;

		speedPin.setImageBitmap(Pins.getSpeedPinImg());
		speedPin.setOnTouchListener(new PinTouchListener(m_stats.getSpeed(), Pins.getPinPos(PinDetails.SPEED_PINS)) {
			@Override
			protected void SetStats(int pos, boolean touchEnd) {
				m_stats.setSpeed(pos);
				SetSpeedValue();
			}
		});

		mightPin.setImageBitmap(Pins.getMightPinImg());
		mightPin.setOnTouchListener(new PinTouchListener(m_stats.getMight(), Pins.getPinPos(PinDetails.MIGHT_PINS)) {
			@Override
			protected void SetStats(int pos, boolean touchEnd) {
				m_stats.setMight(pos);
				SetMightValue();
			}
		});

		sanityPin.setImageBitmap(Pins.getSanityPinImg());
		sanityPin.setOnTouchListener(new PinTouchListener(m_stats.getSanity(), Pins.getPinPos(PinDetails.SANITY_PINS)) {
			@Override
			protected void SetStats(int pos, boolean touchEnd) {
				m_stats.setSanity(pos);
				SetSanityValue();
			}
		});

		knowledgePin.setImageBitmap(Pins.getKnowledgePinImg());
		knowledgePin.setOnTouchListener(new PinTouchListener(m_stats.getKnowledge(), Pins.getPinPos(PinDetails.KNOWLEDGE_PINS)) {
			@Override
			protected void SetStats(int pos, boolean touchEnd) {
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
				return R.mipmap.char_blue_madame_zostra;
			case 1:
				return R.mipmap.char_blue_vivian_lopez;
			case 2:
				return R.mipmap.char_green_brandon_jaspers;
			case 3:
				return R.mipmap.char_green_peter_akimoto;
			case 4:
				return R.mipmap.char_purple_heather_granville;
			case 5:
				return R.mipmap.char_purple_jenny_leclerc;
			case 6:
				return R.mipmap.char_red_darrin_flash_williams;
			case 7:
				return R.mipmap.char_red_ox_bellows;
			case 8:
				return R.mipmap.char_white_father_rhinehardt;
			case 9:
				return R.mipmap.char_white_professor_longfellow;
			case 10:
				return R.mipmap.char_yellow_missy_dubourde;
			case 11:
				return R.mipmap.char_yellow_zoe_ingstrom;
		}
	}

	public int getCharID() {
		return this.mId;
	}

    public int Width = -1;
    public int Height = -1;


    @Override
    public void onGlobalLayout() {
        removeOnGlobalLayoutListener();
        Width = content.getWidth();
        Height = content.getHeight();
        loadWaitingFragments();
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void removeOnGlobalLayoutListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

	private ArrayList<WeakReference<CharacterFragment>> mWaitingFragments;

	private void loadWaitingFragments() {
		if (mWaitingFragments != null) {
			if (mWaitingFragments.size() > 0) {
				for (WeakReference<CharacterFragment> fragRef : mWaitingFragments) {
					if (fragRef.get() != null)
						loadBitmaps(fragRef);
				}
			}
			mWaitingFragments.clear();
		}
		mWaitingFragments = null;
	}

	public void loadBitmaps(CharacterFragment fragment) {
		if (Height < 0) {
			if (mWaitingFragments == null)
				mWaitingFragments = new ArrayList<>();
			mWaitingFragments.add(new WeakReference<>(fragment));
			return;
		}

		loadBitmaps(new WeakReference<>(fragment));
	}

	private void loadBitmaps(WeakReference<CharacterFragment> fragment) {
		int orientation = getResources().getConfiguration().orientation;

		CharacterFragment f = fragment.get();

		TokenWorkerTask task = new TokenWorkerTask(fragment, f.getCharID(), f.GetImageResource(), orientation, Width, Height);
		task.execute();
	}

	private class TokenWorkerTask extends AsyncTask<Integer, Void, AsyncResult> {
		final WeakReference<CharacterFragment> CharFragment;
		final int CharId;
		final int ImgResId;
		final int Orientation;
		final int Width;
		final int Height;

		TokenWorkerTask(WeakReference<CharacterFragment> fragment, int charID, int imgResId, int orientation, int width, int height) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			CharId = charID;
			CharFragment = fragment;
			ImgResId = imgResId;
			Orientation = orientation;
			Width = width;
			Height = height;
		}

		// Decode image in background.
		@Override
		protected AsyncResult doInBackground(Integer... params) {
			Resources r = getResources();

			// Load image
			Bitmap tempImg = BitmapFactory.decodeResource(r, ImgResId);

			double Scale = getScale(tempImg);
			int Density = tempImg.getDensity();

			Bitmap bitmap = scaleImage(Scale, tempImg);

			// Load pins
			PinDetails pins = new PinDetails();
			pins.setSpeedPinImg(scaleImage(r, R.mipmap.pin_speed, Scale));
			pins.setMightPinImg(scaleImage(r, R.mipmap.pin_might, Scale));
			pins.setSanityPinImg(scaleImage(r, R.mipmap.pin_sanity, Scale));
			pins.setKnowledgePinImg(scaleImage(r, R.mipmap.pin_knowledge, Scale));

			pins.FillScaledPinPos(r, CharId, bitmap, Width, Height, (Density / 320.0) * Scale, Orientation);

			return new AsyncResult(bitmap, pins);
		}

		private double getScale(Bitmap b) {
			if (Orientation == Configuration.ORIENTATION_PORTRAIT)
				return Width / (float) b.getWidth();
			else
				// Configuration.ORIENTATION_LANDSCAPE
				return Height / (float) b.getHeight();
		}

		private Bitmap scaleImage(double Scale, Bitmap tempImg) {
			if (Scale == 1.0)
				return tempImg;
			Bitmap scaled = Bitmap.createScaledBitmap(tempImg, (int) (tempImg.getWidth() * Scale), (int) (tempImg.getHeight() * Scale), true);
			tempImg.recycle();
			return scaled;
		}

		private Bitmap scaleImage(Resources r, int resource, double Scale) {
			Bitmap tempImg = BitmapFactory.decodeResource(r, resource);
			return scaleImage(Scale, tempImg);
		}

		// Once complete, see if CharFragment is still around and set bitmaps.
		@Override
		protected void onPostExecute(AsyncResult result) {
			if (result == null || result.Bitmap == null || result.Pins == null || CharFragment == null)
				return;

			CharacterFragment fragment = CharFragment.get();
			if (fragment != null) {
				fragment.SetCharacterImages(result);
			}
		}
	}

	class AsyncResult {
		final Bitmap Bitmap;
		final PinDetails Pins;

		AsyncResult(Bitmap bitmap, PinDetails pins) {
			this.Bitmap = bitmap;
			this.Pins = pins;
		}
	}
}
