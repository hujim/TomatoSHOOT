package org.mirlab.tomatoshoot;

public class item {
	private String name;
	private int price;
	private int [] levelUpPrice = new int [11];	//	武器升級的價錢, level 0~10
	private String func;
	
	public item(String name, String func) {
		this.name = new String(name);
		this.price = 0;
		this.func = new String(func);
	}
	
	public item(String name, int price, String func) {
		this.name = new String(name);
		this.price = price;
		this.func = new String(func);
	}
	
	public item(String name, int [] price, String func) {
		this.name = new String(name);
		this.levelUpPrice = price;
		this.func = new String(func);
	}
	
	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int [] getLevelUpPrice() {
		return levelUpPrice;
	}
	
	public String getFunc() {
		return func;
	}
}
