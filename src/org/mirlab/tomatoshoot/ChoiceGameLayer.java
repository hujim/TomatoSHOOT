package org.mirlab.tomatoshoot;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCTransitionScene.tOrientation;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.util.Log;

public class ChoiceGameLayer extends CCColorLayer {
	private int maxGameNum = 15;
	
	private item [] games = new item[maxGameNum];
		
	private CCMenuItemImage [] btn_game = new CCMenuItemImage[maxGameNum];
	
	private int gBtnNum = 0;
	private int curSelectGame = -1;
	
	protected CCLabel _label1;
	
	mirfb FB;
	TomatoFighter fighter;	
	Context context = CCDirector.sharedDirector().getActivity();	
		
	public static CCScene scene()
	{
		CCScene scene = CCScene.node();
				
		CCColorLayer layer = new ChoiceGameLayer(ccColor4B.ccc4(255, 255, 255, 0));
				
		scene.addChild(layer);		
				
		return scene;
	}
	
	protected ChoiceGameLayer(ccColor4B color) {
		super(color);
		
		this.setIsTouchEnabled(true);
		
		fighter = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).tomatoFighter;
		Log.d("btn", "user : " + fighter.getID());
		Log.d("btn", "money : "+ fighter.getMoney());
		
		// 背景圖
		CGSize winSize = CCDirector.sharedDirector().displaySize();			
		CCSprite background = CCSprite.sprite("choiceGame.png");	
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		//float backgroundScale = winSize.height/(background.getTextureRect().size.height);
		float backgroundScale = winSize.width/(background.getTextureRect().size.width);		
		
		background.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		background.setScale(backgroundScale);
		addChild(background);
		
		// set label
		_label1 = CCLabel.makeLabel("Hello World", "Arial", 25);
		_label1.setColor(ccColor3B.ccBLACK);
		_label1.setPosition(CGPoint.ccp(winSize.width * 0.25f, winSize.height * 0.15f));		
		addChild(_label1);
		
		// 設定關卡
		games[0] = new item("game1", "練習準度");
		games[1] = new item("game2", "刀疤番茄");
		games[2] = new item("game3", "聰明藥水");
		games[3] = new item("game4", "無限愛心");
		games[4] = new item("game5", "undefined");
		games[5] = new item("game6", "undefined");
		games[6] = new item("game7", "undefined");
		games[7] = new item("game8", "undefined");
		games[8] = new item("game9", "undefined");
		games[9] = new item("game10", "undefined");
		games[10] = new item("game11", "undefined");
		games[11] = new item("game12", "undefined");
		games[12] = new item("game13", "undefined");
		games[13] = new item("game14", "undefined");
		games[14] = new item("game15", "undefined");
		
		// Ready按鈕				
		CCMenuItemImage readyBtn = CCMenuItemImage.item("button_ready.png", "button_ready.png", this, "clickMenuBtn");
		readyBtn.setScale(backgroundScale);
		readyBtn.setTag(0);
		
		CCMenu readyMenu = CCMenu.menu(readyBtn);
		readyMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		readyMenu.setPosition(CGPoint.ccp(winSize.width - readyBtn.getBoundingBox().size.width * 0.75f, readyBtn.getBoundingBox().size.height * 1.4f));
		addChild(readyMenu);							
		
		// 關卡按鈕		
		int [] gameStatus = fighter.getGameStatus();
		if (gameStatus[0] == 0) {	//	尚未擁有
			btn_game[0] = CCMenuItemImage.item("game1_lock.png", "game1_lock.png", this, "clickGameBtn");
			btn_game[0].setTag(0);	
		} else {	//	已擁有
			btn_game[0] = CCMenuItemImage.item("game1_unlock.png", "game1_clear.png", this, "clickGameBtn");
			btn_game[0].setTag(1);	
		}
		btn_game[0].setScale(backgroundScale);		
				
		if (gameStatus[1] == 0) {	//	尚未擁有
			btn_game[1] = CCMenuItemImage.item("game2_lock.png", "game2_lock.png", this, "clickGameBtn");
			btn_game[1].setTag(0);	
		} else {	//	已擁有
			btn_game[1] = CCMenuItemImage.item("game2_unlock.png", "game2_clear.png", this, "clickGameBtn");
			btn_game[1].setTag(1);	
		}
		btn_game[1].setScale(backgroundScale);
		
		if (gameStatus[2] == 0) {	//	尚未擁有
			btn_game[2] = CCMenuItemImage.item("game3_lock.png", "game3_lock.png", this, "clickGameBtn");
			btn_game[2].setTag(0);	
		} else {	//	已擁有
			btn_game[2] = CCMenuItemImage.item("game3_unlock.png", "game3_clear.png", this, "clickGameBtn");
			btn_game[2].setTag(1);	
		}
		btn_game[2].setScale(backgroundScale);
		
		if (gameStatus[3] == 0) {	//	尚未擁有
			btn_game[3] = CCMenuItemImage.item("game5_lock.png", "game5_lock.png", this, "clickGameBtn");
			btn_game[3].setTag(0);	
		} else {	//	已擁有
			btn_game[3] = CCMenuItemImage.item("game5_unlock.png", "game5_clear.png", this, "clickGameBtn");
			btn_game[3].setTag(1);	
		}
		btn_game[3].setScale(backgroundScale);
				
		if (gameStatus[4] == 0) {	//	尚未擁有
			btn_game[4] = CCMenuItemImage.item("game5_lock.png", "game5_lock.png", this, "clickGameBtn");
			btn_game[4].setTag(0);	
		} else {	//	已擁有
			btn_game[4] = CCMenuItemImage.item("game5_unlock.png", "game5_clear.png", this, "clickGameBtn");
			btn_game[4].setTag(1);	
		}
		btn_game[4].setScale(backgroundScale);
		/*
		if (gameStatus[5] == 0) {	//	尚未擁有
			btn_game[5] = CCMenuItemImage.item("game6_lock.png", "game6_lock.png", this, "clickGameBtn");
			btn_game[5].setTag(0);	
		} else {	//	已擁有
			btn_game[5] = CCMenuItemImage.item("game6_unlock.png", "game6_clear.png", this, "clickGameBtn");
			btn_game[5].setTag(1);	
		}
		btn_game[5].setScale(backgroundScale);
		
		if (gameStatus[6] == 0) {	//	尚未擁有
			btn_game[6] = CCMenuItemImage.item("game7_lock.png", "game7_lock.png", this, "clickGameBtn");
			btn_game[6].setTag(0);	
		} else {	//	已擁有
			btn_game[6] = CCMenuItemImage.item("game7_unlock.png", "game7_clear.png", this, "clickGameBtn");
			btn_game[6].setTag(1);	
		}
		btn_game[6].setScale(backgroundScale);
		
		if (gameStatus[7] == 0) {	//	尚未擁有
			btn_game[7] = CCMenuItemImage.item("game8_lock.png", "game8_lock.png", this, "clickGameBtn");
			btn_game[7].setTag(0);	
		} else {	//	已擁有
			btn_game[7] = CCMenuItemImage.item("game8_unlock.png", "game8_clear.png", this, "clickGameBtn");
			btn_game[7].setTag(1);	
		}
		btn_game[7].setScale(backgroundScale);
		
		if (gameStatus[8] == 0) {	//	尚未擁有
			btn_game[8] = CCMenuItemImage.item("game9_lock.png", "game9_lock.png", this, "clickGameBtn");
			btn_game[8].setTag(0);	
		} else {	//	已擁有
			btn_game[8] = CCMenuItemImage.item("game9_unlock.png", "game9_clear.png", this, "clickGameBtn");
			btn_game[8].setTag(1);	
		}
		btn_game[8].setScale(backgroundScale);
		
		if (gameStatus[9] == 0) {	//	尚未擁有
			btn_game[9] = CCMenuItemImage.item("game10_lock.png", "game10_lock.png", this, "clickGameBtn");
			btn_game[9].setTag(0);	
		} else {	//	已擁有
			btn_game[9] = CCMenuItemImage.item("game10_unlock.png", "game10_clear.png", this, "clickGameBtn");
			btn_game[9].setTag(1);	
		}
		btn_game[9].setScale(backgroundScale);
		
		if (gameStatus[10] == 0) {	//	尚未擁有
			btn_game[10] = CCMenuItemImage.item("game11_lock.png", "game11_lock.png", this, "clickGameBtn");
			btn_game[10].setTag(0);	
		} else {	//	已擁有
			btn_game[10] = CCMenuItemImage.item("game11_unlock.png", "game11_clear.png", this, "clickGameBtn");
			btn_game[10].setTag(1);	
		}
		btn_game[10].setScale(backgroundScale);
		
		if (gameStatus[11] == 0) {	//	尚未擁有
			btn_game[11] = CCMenuItemImage.item("game12_lock.png", "game12_lock.png", this, "clickGameBtn");
			btn_game[11].setTag(0);	
		} else {	//	已擁有
			btn_game[11] = CCMenuItemImage.item("game12_unlock.png", "game12_clear.png", this, "clickGameBtn");
			btn_game[11].setTag(1);	
		}
		btn_game[11].setScale(backgroundScale);
		
		if (gameStatus[12] == 0) {	//	尚未擁有
			btn_game[12] = CCMenuItemImage.item("game13_lock.png", "game13_lock.png", this, "clickGameBtn");
			btn_game[12].setTag(0);	
		} else {	//	已擁有
			btn_game[12] = CCMenuItemImage.item("game13_unlock.png", "game13_clear.png", this, "clickGameBtn");
			btn_game[12].setTag(1);	
		}
		btn_game[12].setScale(backgroundScale);
		
		if (gameStatus[13] == 0) {	//	尚未擁有
			btn_game[13] = CCMenuItemImage.item("game14_lock.png", "game14_lock.png", this, "clickGameBtn");
			btn_game[13].setTag(0);	
		} else {	//	已擁有
			btn_game[13] = CCMenuItemImage.item("game14_unlock.png", "game14_clear.png", this, "clickGameBtn");
			btn_game[13].setTag(1);	
		}
		btn_game[13].setScale(backgroundScale);
		
		if (gameStatus[14] == 0) {	//	尚未擁有
			btn_game[14] = CCMenuItemImage.item("game15_lock.png", "game15_lock.png", this, "clickGameBtn");
			btn_game[14].setTag(0);	
		} else {	//	已擁有
			btn_game[14] = CCMenuItemImage.item("game15_unlock.png", "game15_clear.png", this, "clickGameBtn");
			btn_game[14].setTag(1);	
		}
		btn_game[14].setScale(backgroundScale);
		*/
		
		CCMenu gRow0 = CCMenu.menu(btn_game[0], btn_game[1], btn_game[2], btn_game[3], btn_game[4]);
		gRow0.setColor(ccColor3B.ccc3(255, 255, 255));
		gRow0.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f + btn_game[0].getBoundingBox().size.height * 1.2f));
		gRow0.alignItemsHorizontally(btn_game[0].getBoundingBox().size.width * 0.125f);		
		addChild(gRow0);
		/*
		CCMenu gRow1 = CCMenu.menu(btn_game[5], btn_game[6], btn_game[7], btn_game[8], btn_game[9]);
		gRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		gRow1.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f + btn_game[0].getBoundingBox().size.height * 0.45f));
		gRow1.alignItemsHorizontally(btn_game[0].getBoundingBox().size.width * 0.125f);
		addChild(gRow1);
				
		CCMenu gRow2 = CCMenu.menu(btn_game[10], btn_game[11], btn_game[12], btn_game[13], btn_game[14]);
		gRow2.setColor(ccColor3B.ccc3(255, 255, 255));
		gRow2.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f - btn_game[0].getBoundingBox().size.height * 0.3f));
		gRow2.alignItemsHorizontally(btn_game[0].getBoundingBox().size.width * 0.125f);
		addChild(gRow2);
		*/
		gBtnNum = 5;		
		
		
	}
	
	public void clickMenuBtn(Object sender) {
		CCMenuItemImage item = (CCMenuItemImage) sender;
		
		if (item.getTag() == 0)	{	//	click ready button
			//	進到遊戲畫面	
			item.setIsEnabled(false);
			if (curSelectGame == 1) {			//	跳到第1關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV1.scene(), tOrientation.kOrientationRightOver));
				
			} else if (curSelectGame == 2) {	//	跳到第2關
				
			} else if (curSelectGame == 3) {	//	跳到第3關
				
			} else if (curSelectGame == 4) {	//	跳到第4關
				
			} else if (curSelectGame == 5) {	//	跳到第5關
				
			} else if (curSelectGame == 6) {	//	跳到第6關
				
			} else if (curSelectGame == 7) {	//	跳到第7關
				
			} else if (curSelectGame == 8) {	//	跳到第8關
				
			} else if (curSelectGame == 9) {	//	跳到第9關
				
			} else if (curSelectGame == 10) {	//	跳到第10關
				
			} else if (curSelectGame == 11) {	//	跳到第11關
				
			} else if (curSelectGame == 12) {	//	跳到第12關
				
			} else if (curSelectGame == 13) {	//	跳到第13關
				
			} else if (curSelectGame == 14) {	//	跳到第14關
				
			} else if (curSelectGame == 15) {	//	跳到第15關
				
			}
			SoundEngine.sharedEngine().realesAllSounds();
			SoundEngine.sharedEngine().setSoundVolume(0.5f);
			SoundEngine.sharedEngine().playSound(context, R.raw.mr_reno, true);
			
		}
		
	}
	
	public void clickGameBtn(Object sender) {
		CCMenuItemImage item = (CCMenuItemImage) sender;
		
		if (item.getTag() == 0) {			//	尚未擁有
			_label1.setString("尚未開放");
			
		} else if (item.getTag() == 1) {	//	已擁有，但未選擇
			if (curSelectGame < 0) {
				for (int i = 0;i < gBtnNum;i++) {
					if (btn_game[i] == item) {
						curSelectGame = i + 1;
						_label1.setString("選擇第" + (i+1) + "關");
						break;
					}
				}
				
				item.setTag(2);
				item.selected();				
			}
		} else if (item.getTag() == 2) {	//	已擁有，已選擇
			curSelectGame = -1;
			item.setTag(1);
			item.unselected();			
		}
	}
}
