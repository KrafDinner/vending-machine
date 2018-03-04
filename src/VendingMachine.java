import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;

public class VendingMachine {
	
	private BigDecimal customerBalance;
	
	private String display;
	
	private int twentyFiveCentSlot;
	
	private int tenCentSlot;
	
	private int fiveCentSlot;
	
	private ArrayList<String> coinReturn;
	
	private IProduct productReturn;
	
	private Map<String, MutableInt> inventory;

    public VendingMachine(int twentyFiveCentCoins, int tenCentCoins, int fiveCentCoins) {
    	this.twentyFiveCentSlot = twentyFiveCentCoins;
    	this.tenCentSlot = tenCentCoins;
    	this.fiveCentSlot = fiveCentCoins;
    	this.customerBalance = new BigDecimal("0.0");
    	this.coinReturn = new ArrayList<String>();
    	this.productReturn = null;
    	stockInventory();
    	updateDisplay();
    }

	public String checkDisplay() {
    	String currentDisplay = display;
    	updateDisplay();
    	return currentDisplay;
    }
    
    public void insertCoin(String coin) {
    	// Assign coin value based on weight
    	if (coin.length() == 6) {  // Hopefully a nickel worth 5 cents
    		fiveCentSlot++;
    		customerBalance = customerBalance.add(new BigDecimal("0.05"));
    	} else if (coin.length() == 4) {  // Hopefully a dime worth 10 cents
    		tenCentSlot++;
    		customerBalance = customerBalance.add(new BigDecimal("0.10"));
    	} else if (coin.length() == 7) {  // Hopefully a quarter worth 25 cents
    		twentyFiveCentSlot++;
    		customerBalance = customerBalance.add(new BigDecimal("0.25"));
    	} else {
    		coinReturn.add(coin);
    	}
    	
    	updateDisplay();
    }
    
    public void pressButton(String buttonPressed) {
    	if (buttonPressed.equals("cola")) {
    		dispenseProduct(new Cola());
    	} else if (buttonPressed.equals("chips")) {
    		dispenseProduct(new Chips());
    	} else if (buttonPressed.equals("candy")) {
    		dispenseProduct(new Candy());
    	} else if (buttonPressed.equals("returnChange")) {
    		returnChange();
    		updateDisplay();
    	}
    }

	protected String padPriceWithZero(BigDecimal amount) {
    	String amountStr = String.valueOf(amount);
    	// Check number of decimal places
		int indexOfDecimal = amountStr.indexOf(".");
		String decimalPlaces = amountStr.substring(indexOfDecimal + 1);
		
		// Add a '0' to the end of the display if balance only has one decimal place
		if (decimalPlaces.length() < 2) {
			amountStr += "0";
		}
		
		return amountStr;
    }
    
    private void dispenseProduct(IProduct product) {
    	BigDecimal productPrice = product.getPrice();
    	String productType = product.getType();
    	if (customerBalance.compareTo(productPrice) >= 0 && inventory.get(productType).getValue() > 0) {
    		customerBalance = customerBalance.subtract(productPrice);
    		inventory.get(productType).decrement();
			productReturn = product;
			display = "THANK YOU";
			returnChange();
		} else if (inventory.get(productType).getValue() == 0) {
			display = "SOLD OUT";
		} else {
			display = "PRICE: $" + padPriceWithZero(productPrice);
		}
    }

    private void updateDisplay() {
    	if (customerBalance.compareTo(new BigDecimal("0.0")) > 0) {
    		display = "$" + padPriceWithZero(customerBalance);
    	} else if (!canMakeChange()) {
    		display = "EXACT CHANGE ONLY";
    	} else {
    		display = "INSERT COINS";
    	}
    }

	private void returnChange() {
    	while (customerBalance.compareTo(new BigDecimal("0.25")) >= 0 && twentyFiveCentSlot > 0) {
    		coinReturn.add("quarter");
    		twentyFiveCentSlot--;
    		customerBalance = customerBalance.subtract(new BigDecimal("0.25"));
    	}
    	while (customerBalance.compareTo(new BigDecimal("0.10")) >= 0 && tenCentSlot > 0) {
    		coinReturn.add("dime");
    		tenCentSlot--;
    		customerBalance = customerBalance.subtract(new BigDecimal("0.10"));
    	}
    	while (customerBalance.compareTo(new BigDecimal("0.05")) >= 0 && fiveCentSlot > 0) {
    		coinReturn.add("nickel");
    		fiveCentSlot--;
    		customerBalance = customerBalance.subtract(new BigDecimal("0.05"));
    	}
    }
    
    private void stockInventory() {
    	// Vending Machine will hold 2 of each product
    	inventory = new HashMap<String, MutableInt>();
    	inventory.put("cola", new MutableInt(2));
    	inventory.put("chips", new MutableInt(2));
    	inventory.put("candy", new MutableInt(2));
    }
    
    private boolean canMakeChange() {
    	// As a safety precaution, VendingMachine will need at least two of each coin to make change
    	return twentyFiveCentSlot >= 2 && tenCentSlot >= 2 && fiveCentSlot >= 2;
	}
    
    public IProduct getProductReturn() {
    	return this.productReturn;
    }
    
    public ArrayList<String> getCoinReturn() {
    	return this.coinReturn;
    }
}
