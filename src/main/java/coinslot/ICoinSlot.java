package main.java.coinslot;

import java.math.BigDecimal;

public interface ICoinSlot {
	
	public String getCoinName();

	public BigDecimal getCoinValue();
	
	public void addCoin();
	
	public void removeCoin();
	
	public int getCoinCount();

}
