package com.viktordikov.betrayalcharacterstats.Data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.viktordikov.betrayalcharacterstats.BR;

import java.util.Locale;

/**
 * Holds Character Stats
 */
public class CharacterStats extends BaseObservable {

	private int Min;
	private int Max;
	
	private int[] Defaults;

	private int Speed;
	private int Might;
	private int Sanity;
	private int Knowledge;

	private int[] Stats;

	private int Age;
	private String Height;
	private int Weight;
	private String Hobbies;
	private String Birthday;

	public CharacterStats(int[] constraints, int[] stats, int[] defaults) {
		Min = constraints[0];
		Max = constraints[1];
		
		Defaults = defaults;
		
		ResetToDefaults();
		
		Stats = stats;
	}

	public void ResetToDefaults() {
		setSpeed(getSpeedDefault());
		setMight(getMightDefault());
		setSanity(getSanityDefault());
		setKnowledge(getKnowledgeDefault());
	}

	public int getMin() {
		return Min;
	}

	public int getMax() {
		return Max;
	}

	public int getSpeedDefault() {
		return Defaults[0];
	}

	@Bindable
	public int getSpeed() {
		return Speed;
	}

	public int getSpeedVal() {
		return Stats[Speed];
	}

	@Bindable
	public String getSpeedString() {
		return String.format(Locale.ROOT, "%d", getSpeedVal());
	}

	public void setSpeed(int speed) {
		if (speed < Min)
			Speed = Min;
		else if (speed > Max)
			Speed = Max;
		else
			Speed = speed;
		notifyPropertyChanged(BR.speed);
		notifyPropertyChanged(BR.speedString);
	}

	public int getMightDefault() {
		return Defaults[1];
	}

    @Bindable
	public int getMight() {
		return Might;
	}

	public int getMightVal() {
		return Stats[Max + 1 + Might];
	}

	@Bindable
	public String getMightString() {
		return String.format(Locale.ROOT, "%d", getMightVal());
	}

	public void setMight(int might) {
		if (might < Min)
			Might = Min;
		else if (might > Max)
			Might = Max;
		else
			Might = might;
		notifyPropertyChanged(BR.might);
		notifyPropertyChanged(BR.mightString);
	}

	public int getSanityDefault() {
		return Defaults[2];
	}

    @Bindable
	public int getSanity() {
		return Sanity;
	}

	public int getSanityVal() {
		return Stats[2 * (Max + 1) + Sanity];
	}

	@Bindable
	public String getSanityString() {
		return String.format(Locale.ROOT, "%d", getSanityVal());
	}

	public void setSanity(int sanity) {
		if (sanity < Min)
			Sanity = Min;
		else if (sanity > Max)
			Sanity = Max;
		else
			Sanity = sanity;
		notifyPropertyChanged(BR.sanity);
		notifyPropertyChanged(BR.sanityString);
	}

	public int getKnowledgeDefault() {
		return Defaults[3];
	}

    @Bindable
	public int getKnowledge() {
		return Knowledge;
	}

	public int getKnowledgeVal() {
		return Stats[3 * (Max + 1) + Knowledge];
	}

	@Bindable
	public String getKnowledgeString() {
		return String.format(Locale.ROOT, "%d", getKnowledgeVal());
	}

	public void setKnowledge(int knowledge) {
		if (knowledge < Min)
			Knowledge = Min;
		else if (knowledge > Max)
			Knowledge = Max;
		else
			Knowledge = knowledge;
		notifyPropertyChanged(BR.knowledge);
		notifyPropertyChanged(BR.knowledgeString);
	}

	public int getAge() {
		return Age;
	}

	public String getAgeString() {
		return String.format(Locale.ROOT, "%d", Age);
	}

	public void setAge(int age) {
		Age = age;
	}

	public String getHeight() {
		return Height;
	}

	public void setHeight(String height) {
		Height = height;
	}

	public int getWeight() {
		return Weight;
	}

	public String getWeightString() {
		return String.format(Locale.ROOT, "%d", Weight);
	}

	public void setWeight(int weight) {
		Weight = weight;
	}

	public String getHobbies() {
		return Hobbies;
	}

	public void setHobbies(String hobbies) {
		Hobbies = hobbies;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

}
