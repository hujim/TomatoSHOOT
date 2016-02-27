package org.mirlab.tomatoshoot;

import java.io.Serializable;
import java.util.LinkedList;

public class TomatoFighter implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1809231771129721629L;
	private int money;	
	private String id;
	
	private int maxWeaponNum = 3;	//	目前武器最多3把
	private int maxPropNum = 6;
	private int maxGameNum = 15;	
	
	private int [] weapons = new int [maxWeaponNum];	//	weapon status : 0 : 尚未開放, 1 : 已開放, 2 : 已選擇
	private int [] props = new int [maxPropNum];		//	prop status : 0 : 尚未擁有, 1 : 已經擁有, 2 : 已選擇
	private int [] games = new int [maxGameNum];		//	game status : 0 : lock, 1 : unlock, 2 : 已選擇
	private int [] weaponsLevel = new int [maxWeaponNum];	//	武器的等級
	private int [] propsNum = new int [maxPropNum];			//	擁有道具的數量

	private int [] selectedFriendLevel = {-1, -1, -1};
	
	private int selectedWeaponIndex = -1;
	private int selectedWeaponLevel = -1;
	private int [] selectedPropIndex = {-1, -1};	//	所選道具的index
	
	private int playerLevel = 1;
	private int curGameLevel = 1;
	private int curGameLoopNum = 0;
	
	LinkedList<Float> quickGameScoreList = new LinkedList<Float>();
	
	//	player
	protected TomatoFighter(String id, int money) {
		this.id = id;
		this.money = money;
		setWeaponStatus(0, 1);
		setGameStatus(0, 1);
		quickGameScoreList.add(0,(float) 0);
		quickGameScoreList.add(1,(float) 0);
		quickGameScoreList.add(2,(float) 0);
		quickGameScoreList.add(3,(float) 0);
		quickGameScoreList.add(4,(float) 0);
		
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
	
	public void setPlayerLevel(int level) {
		playerLevel = level;
	}
	
	public int getPlayerLevel() {
		return playerLevel;
	}
	
	//	about weapon
	public void setWeaponStatus(int index, int status) {
		if (index >= 0 && index < maxWeaponNum) {
			weapons[index] = status;
		}
	}
	
	public int [] getWeaponStatus() {
		return weapons;
	}
	
	public void setWeaponLevel(int index, int level) {
		if (index >= 0 && index < maxWeaponNum) {
			weaponsLevel[index] = level;
		}
	}
	
	public int getWeaponLevel(int index) {
		return weaponsLevel[index];
		
	}
	
	public void setSelectedWeapon(int index, int level) {
		selectedWeaponIndex = index;
		selectedWeaponLevel = level;
	}
	
	public int getSelectedWeapon() {
		return selectedWeaponIndex;
	}	
	
	public int getSelectedWeaponLevel() {
		return selectedWeaponLevel;
	}
	
	//	about prop
	public void setPropStatus(int index, int status) {
		if (index >= 0 && index < maxPropNum) {
			props[index] = status;
		}
	}
	
	public int [] getPropStatus() {
		return props;
	}
	
	public void setPropNum(int index, int num) {
		if (index >= 0 && index < maxPropNum) {
			propsNum[index] = num;
		}
	}
	
	public int getPropNum(int index) {
		return propsNum[index];
	}
	
	public void setSelectedProp(int index, int sIndex) {
		selectedPropIndex[index] = sIndex;
	}
	
	public int [] getSelectedProp() {	// -1 代表沒有選擇
		return selectedPropIndex;
	}
	
	//	about friend
	public void setSelectedFriend(int index, int level) {
		//selectedFriendSprite[index] = friend;
		selectedFriendLevel[index] = level;
	}
	
	public int getSelectedFriendLevel(int index) {
		return selectedFriendLevel[index];
	}
	
	//	about game
	public void setGameStatus(int index, int status) {
		if (index >= 0 && index < maxGameNum) {
			games[index] = status;
		}
	}
	
	public int [] getGameStatus() {
		return games;
	}
	
	public void setCurGameLevel(int level) {
		curGameLevel = level;
	}
	
	public int getCurGameLevel() {
		return curGameLevel;
	}
	
	public void setCurGameLoopNum(int loopNum) {
		curGameLoopNum = loopNum;
	}
	
	public int getCurGameLoopNum() {
		return curGameLoopNum;
	}
	public float getQuickGameLeaderBoard(float score_new) {
		// TODO Auto-generated method stub
		
		float score_tmp = 0;
		int numScores = 5;
		//	get all top five scores to score_tmp;
		for (int i=0; i<numScores; i++)
		{
			score_tmp = quickGameScoreList.get(i);
			if (score_new >= score_tmp)
			{
				quickGameScoreList.add(i,score_new);
				quickGameScoreList.remove(numScores);	// release space after location 0~4
				break;
				
			}
		}
		return score_tmp;
		
	}
}
