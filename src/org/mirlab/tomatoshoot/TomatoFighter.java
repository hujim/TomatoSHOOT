package org.mirlab.tomatoshoot;

import java.io.Serializable;

public class TomatoFighter implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1809231771129721629L;
	private int money;	
	private String id;
	
	private int maxWeaponNum = 12;
	private int maxPropNum = 20;
	//private int maxFriendNum = 5;
	private int maxGameNum = 15;
	
	private int [] weapons = new int [maxWeaponNum];	//	weapon status : 0 : 尚未擁有, 1 : 已擁有, 2 : 已選擇
	private int [] props = new int [maxPropNum];		//	prop status : 0 : 尚未擁有, 1 : 已擁有, 2 : 已選擇
	private int [] games = new int [maxGameNum];		//	game status : 0 : lock, 1 : unlock, 2 : 已選擇
	//private String [] friends = new String [maxFriendNum];
	private int selectedWeaponNum;
	
	protected TomatoFighter(String id, int money) {
		this.id = id;
		this.money = money;
	}
	
	public int getSelectedWeapon(){
		return selectedWeaponNum;
	}
	public void setMoney(int Money) {
		this.money = Money;
	}
	
	public int getMoney() {
		return this.money;
	}
	
	public String getID() {
		return this.id;
	}
	
	public void addOwnWeapons(int index) {
		if (index >= 0 && index < maxWeaponNum) {
			weapons[index] = 1;
		}
	}
	
	public void addOwnProps(int index) {
		if (index >= 0 && index < maxPropNum) {
			props[index] = 1;
		}
	}	
	
	public void addOwnGames(int index) {
		if (index >= 0 && index < maxGameNum) {
			games[index] = 1;
		}
	}
	
	public void setWeaponStatus(int index, int status) {
		if (index >= 0 && index < maxWeaponNum) {
			weapons[index] = status;
			if(status == 2)
			{
				selectedWeaponNum = index;
			}
		}
	}
	
	public void setPropStatus(int index, int status) {
		if (index >= 0 && index < maxPropNum) {
			props[index] = status;
		}
	}
	
	public void setGameStatus(int index, int status) {
		if (index >= 0 && index < maxGameNum) {
			games[index] = status;
		}
	}
	
	public int [] getWeaponStatus() {
		return weapons;
	}
	
	public int [] getPropStatus() {
		return props;
	}

	public int [] getGameStatus() {
		return games;
	}
}
