package com.ViktorDikov.BetrayalCharacterStats.Data;


/**
 * Holds Character Stats
 */
public class CharacterStats {
	
	private int Min;
	private int Max;

	private int Speed;
	private int Might;
	private int Sanity;
	private int Knowledge;


	public CharacterStats(int[] constraints) {
		Min = constraints[0];
		Max = constraints[1];
		
		setSpeed(Min);
		setMight(Min);
		setSanity(Min);
		setKnowledge(Min);
	}

	public void Set(int[] vals) {
		setSpeed(vals[0]);
		setMight(vals[1]);
		setSanity(vals[2]);
		setKnowledge(vals[3]);
	}

	public int getMin() {
		return Min;
	}

	public int getMax() {
		return Max;
	}

	public int getSpeed() {
		return Speed;
	}

	public void setSpeed(int speed) {
		if (speed < Min)
			Speed = Min;
		else if (speed > Max)
			Speed = Max;
		else
			Speed = speed;
	}

	public Boolean canIncreaseSpeed() {
		return Speed < Max;
	}

	public void increaseSpeed() {
		if (canIncreaseSpeed())
			Speed++;
		else
			Speed = Max;
	}

	public Boolean canDecreaseSpeed() {
		return Speed > Min;
	}

	public void decreaseSpeed() {
		if (canDecreaseSpeed())
			Speed--;
		else
			Speed = Min;
	}

	public int getMight() {
		return Might;
	}

	public void setMight(int might) {
		if (might < Min)
			Might = Min;
		else if (might > Max)
			Might = Max;
		else
			Might = might;
	}

	public Boolean canIncreaseMight() {
		return Might < Max;
	}

	public void increaseMight() {
		if (canIncreaseMight())
			Might++;
		else
			Might = Max;
	}

	public Boolean canDecreaseMight() {
		return Might > Min;
	}

	public void decreaseMight() {
		if (canDecreaseMight())
			Might--;
		else
			Might = Min;
	}

	public int getSanity() {
		return Sanity;
	}

	public void setSanity(int sanity) {
		if (sanity < Min)
			Sanity = Min;
		else if (sanity > Max)
			Sanity = Max;
		else
			Sanity = sanity;
	}

	public Boolean canIncreaseSanity() {
		return Sanity < Max;
	}

	public void increaseSanity() {
		if (canIncreaseSanity())
			Sanity++;
		else
			Sanity = Max;
	}

	public Boolean canDecreaseSanity() {
		return Sanity > Min;
	}

	public void decreaseSanity() {
		if (canDecreaseSanity())
			Sanity--;
		else
			Sanity = Min;
	}

	public int getKnowledge() {
		return Knowledge;
	}

	public void setKnowledge(int knowledge) {
		if (knowledge < Min)
			Knowledge = Min;
		else if (knowledge > Max)
			Knowledge = Max;
		else
			Knowledge = knowledge;
	}

	public Boolean canIncreaseKnowledge() {
		return Knowledge < Max;
	}

	public void increaseKnowledge() {
		if (canIncreaseKnowledge())
			Knowledge++;
		else
			Knowledge = Max;
	}

	public Boolean canDecreaseKnowledge() {
		return Knowledge > Min;
	}

	public void decreaseKnowledge() {
		if (canDecreaseKnowledge())
			Knowledge--;
		else
			Knowledge = Min;
	}

}
