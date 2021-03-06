package main.java.coinslot;

import java.math.BigDecimal;

import main.java.util.Constants;

public class TwentyFiveCentSlot implements ICoinSlot {

	private String coinName;
	
	private BigDecimal coinValue;
	
	private int coinCount;
	
	public TwentyFiveCentSlot(int coinCount) {
		this.coinName = Constants.QUARTER;
		this.coinValue = new BigDecimal(Constants.TWENTY_FIVE_CENTS);
		this.coinCount = coinCount;
	}
	
	public String getCoinName() {
		return coinName;
	}
	
	public BigDecimal getCoinValue() {
		return coinValue;
	}
	
	public void addCoin() {
		coinCount++;
	}
	
	public void removeCoin() {
		coinCount--;
	}

	public int getCoinCount() {
		return coinCount;
	}

}
