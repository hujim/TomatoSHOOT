package org.mirlab.tomatoshoot;

import java.util.ArrayList;
import java.util.Random;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemLabel;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCTransitionScene.tOrientation;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

public class SetGameItemLayer extends CCColorLayer {
	protected int curLayer = -1;	//	供切換選單使用
	private int curPage = 0;		//	供切換頁數使用
	
	private int maxPageNum = 5;
	private int maxWeaponNum = 3;	//	目前武器最多3把
	private int maxPropNum = 6;
	private int maxFriendNum = 6;
	private int maxWeaponSelectNum = 1;	//	最多可選擇一把武器
	private int maxPropSelectNum = 2;
	private int maxFriendSelectNum = 3;
	
	private item [] weapons = new item[maxWeaponNum];
	private item [] props = new item[maxPropNum];	
	
	private CCMenuItemSprite [] btn_weapon = new CCMenuItemSprite[maxWeaponNum];
	private CCMenuItemSprite [] btn_prop = new CCMenuItemSprite[maxPropNum];
	private CCMenuItemSprite [] btn_friend = new CCMenuItemSprite[maxFriendNum];
	
	private CCSprite [] levelSprite = new CCSprite[11];		//	武器11個階級的圖案
	protected CCLabel [] propNumLabel = new CCLabel[maxPropNum];	//	道具個數
	
	private int wBtnNum = 0;
	private int pBtnNum = 0;
	private int fBtnNum = 0;
	
	private CCLayer [] wPages = new CCLayer[maxPageNum];	
	private CCLayer [] pPages = new CCLayer[maxPageNum];
	private CCLayer [] fPages = new CCLayer[maxPageNum];
	
	private int wPageNum = 0;
	private int pPageNum = 0;
	private int fPageNum = 0;
	private boolean unOwnedSelected = false;
	
	private int wSelectNum = 0;
	private int pSelectNum = 0;
	private int fSelectNum = 0; 
	
	protected CCLabel _label1;
	protected CCLabel _label2;
	protected CCLabel _label3;
	protected CCLabel _label4;
	
	private CCMenuItemSprite curSelectBtn;		//	目前所選擇的按鈕
	private CCMenuItemSprite buyBtn, levelUpBtn;	//	道具購買按鈕、武器升級按鈕
	private CCMenu buyMenu, levelUpMenu;
	private CCMenuItemLabel loginBtn;  
	private CCMenu loginMenu;
	
	private boolean getFriend = true;
	
	private float backgroundScale;
	
	mirfb FB;
	TomatoFighter fighter;	
	
	CCSprite [] friendSprite = new CCSprite[6];
	JSONArray topNArray;
	ArrayList<String> fNameList = new ArrayList<String>(); 
	ArrayList<String> fLevelList = new ArrayList<String>();
	
	Context context = CCDirector.sharedDirector().getActivity();		
	
	static CCScene scene;
	public static CCScene scene()
	{
		scene = CCScene.node();
		scene.setTag(1);
		CCColorLayer layer = new SetGameItemLayer(ccColor4B.ccc4(255, 255, 255, 0));
				
		scene.addChild(layer);		
				
		return scene;
	}
	
	protected SetGameItemLayer(ccColor4B color) {
		super(color);
		
	}
	
	@Override
	public void onEnterTransitionDidFinish()
	{
		super.onEnterTransitionDidFinish();
		initContents();

		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = false;
	}
	
	public void initContents()
	{
		this.setIsTouchEnabled(true);
		
		FB = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).FB;
		fighter = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).tomatoFighter;
		friendSprite = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).friendSprite;
		topNArray = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).topNArray;
		fNameList = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).fNameList;
		fLevelList = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).fLevelList;
		
		Log.d("btn", "user : " + fighter.getID());
		Log.d("btn", "money : "+ fighter.getMoney());					
		
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		// 背景圖
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("SetGameItemLayer.plist");
		CCSprite background = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList.png"));		
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		//float backgroundScale = winSize.height/(background.getTextureRect().size.height);
		backgroundScale = winSize.width/(background.getTextureRect().size.width);
		
		background.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		background.setScale(backgroundScale);
		addChild(background, 0);
		
		//	set label
		_label1 = CCLabel.makeLabel(" ", "Arial", 25);
		_label1.setColor(ccColor3B.ccBLACK);
		_label1.setPosition(winSize.width * 0.75f, winSize.height * 0.55f);		
		addChild(_label1, 1);
		_label2 = CCLabel.makeLabel(" ", "Arial", 20);
		_label2.setColor(ccColor3B.ccBLACK);
		_label2.setPosition(winSize.width * 0.75f, winSize.height * 0.45f);		
		addChild(_label2, 1);
		_label3 = CCLabel.makeLabel(" ", "Arial", 25);
		_label3.setColor(ccColor3B.ccYELLOW);
		_label3.setPosition(winSize.width * 0.75f, winSize.height * 0.35f);		
		addChild(_label3, 1);
		_label4 = CCLabel.makeLabel("$" + fighter.getMoney(), "Arial", 25);
		_label4.setColor(ccColor3B.ccYELLOW);
		_label4.setPosition(winSize.width * 0.75f, winSize.height * 0.70f);		
		addChild(_label4, 1);
		
		/*
		 * 	設定武器
		 * 0 : 玉米, 1 : 西瓜, 2 : 葡萄
		 */
		int [] levelUpPrice0 = {2500, 5000, 7500, 12000, 15000, 18000, 20000, 25000, 30000, 40000, 0};
		weapons[0] = new item("咻咻玉米", levelUpPrice0, "全體敵方防禦力下降");
		int [] levelUpPrice1 = {1800, 2500, 3500, 4800, 5500, 6500, 10000, 15000, 20000, 27500, 0};
		weapons[1] = new item("嘿唷西瓜", levelUpPrice1, "逼退周遭敵人");
		int [] levelUpPrice2 = {2000, 3000, 4200, 5400, 7000, 9000, 13000, 17000, 23000, 30000, 0};
		weapons[2] = new item("轉轉葡萄", levelUpPrice2, "全體敵方麻痺");
		
		/**
		 * 	設定道具
		**/
		props[0] = new item("維他命C飲料", 1800, "HP + 5");
		props[1] = new item("維他命D飲料", 3500, "HP + 10");
		props[2] = new item("維他命E飲料", 6600, "HP + 20");		
		props[3] = new item("銷魂番茄醬", 8000, "可施放OH MY God技能 (HP - 30)");
		props[4] = new item("營養雞蛋", 2500, "Attack * 1.5");
		props[5] = new item("養身雞蛋", 6000, "Attack * 2.5");
		
		// 武器、防具、fb好友支援、上一頁、下一頁、購買、升級、選關選單按鈕
		// Tag number : backward : 0, weapon : 1, prop : 2, fb : 3, next : 4, buy : 5, level up : 6, fight : 7		
		CCMenuItemSprite backwardBtn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_backward.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_backward.png")), this, "clickMenuBtn");
		backwardBtn.setScale(backgroundScale);
		backwardBtn.setTag(0);
		
		CCMenu backwardMenu = CCMenu.menu(backwardBtn);
		backwardMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		backwardMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f - backwardBtn.getBoundingBox().size.width * 3.0f / 4.0f, winSize.height * 4.1f / 5.0f));
		addChild(backwardMenu);
					
		CCMenuItemSprite weaponBtn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_weapon.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_weapon.png")), this, "clickMenuBtn");
		weaponBtn.setScale(backgroundScale);
		weaponBtn.setTag(1);		
		
		CCMenu weaponMenu = CCMenu.menu(weaponBtn);
		weaponMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		weaponMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f - weaponBtn.getBoundingBox().size.width * 1.0f / 4.0f, winSize.height * 0.5f + weaponBtn.getBoundingBox().size.height));
		addChild(weaponMenu);
				
		CCMenuItemSprite propBtn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_prop.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_prop.png")), this, "clickMenuBtn");
		propBtn.setScale(backgroundScale);
		propBtn.setTag(2);
		
		CCMenu propMenu = CCMenu.menu(propBtn);
		propMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		propMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f));
		addChild(propMenu);
		
		CCMenuItemSprite fbBtn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_facebook.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_facebook.png")), this, "clickMenuBtn");
		fbBtn.setScale(backgroundScale);
		fbBtn.setTag(3);
		
		CCMenu fbBtnMenu = CCMenu.menu(fbBtn);
		fbBtnMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		fbBtnMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f - fbBtn.getBoundingBox().size.width * 0.25f, winSize.height * 0.5f - fbBtn.getBoundingBox().size.height));		
		addChild(fbBtnMenu);	
				
		CCMenuItemSprite nextBtn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_next.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_next.png")), this, "clickMenuBtn");
		nextBtn.setScale(backgroundScale);
		nextBtn.setTag(4);
		
		CCMenu nextMenu = CCMenu.menu(nextBtn);
		nextMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		nextMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f - nextBtn.getBoundingBox().size.width * 3.2f / 4.0f, winSize.height * 0.9f / 5.0f));
		addChild(nextMenu);
		
		buyBtn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_buy_up.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_buy_down.png")), this, "clickMenuBtn");
		buyBtn.setScale(backgroundScale);
		buyBtn.setTag(5);
		
		buyMenu = CCMenu.menu(buyBtn);
		buyMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		buyMenu.setPosition(CGPoint.ccp(winSize.width * 0.75f, buyBtn.getBoundingBox().size.height * 1.0f));
		addChild(buyMenu);
		buyMenu.setVisible(false);
		
		levelUpBtn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_levelup_up.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_levelup_down.png")), this, "clickMenuBtn");
		levelUpBtn.setScale(backgroundScale);
		levelUpBtn.setTag(6);
		
		levelUpMenu = CCMenu.menu(levelUpBtn);
		levelUpMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		levelUpMenu.setPosition(CGPoint.ccp(winSize.width * 0.75f, buyBtn.getBoundingBox().size.height * 1.0f));
		addChild(levelUpMenu);
		levelUpMenu.setVisible(false);
		
		CCMenuItemSprite fightBtn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_fight.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_fight.png")), this, "clickMenuBtn");		
		fightBtn.setScale(backgroundScale);
		fightBtn.setTag(7);
		
		CCAction action2 = (CCRepeatForever.action(
				CCSequence.actions(
						CCRotateBy.action(0.1f, 20),
						CCRotateBy.action(0.1f, -40),
						CCRotateBy.action(0.1f, 20),
						CCDelayTime.action(2)
				)));
		action2.setTag(101);
				
		fightBtn.runAction(action2);
		
		CCMenu fightMenu = CCMenu.menu(fightBtn);
		fightMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		fightMenu.setPosition(CGPoint.ccp(winSize.width - fightBtn.getBoundingBox().size.width * 0.5f, winSize.height - fightBtn.getBoundingBox().size.height * 0.5f));
		addChild(fightMenu);
		
		loginBtn = CCMenuItemLabel.item("登入", this, "clickLoginBtn");
		loginBtn.setTag(0);
		
		loginMenu = CCMenu.menu(loginBtn);
		loginMenu.setColor(ccColor3B.ccBLACK);
		loginMenu.setPosition(CGPoint.ccp(winSize.width * 0.75f, buyBtn.getBoundingBox().size.height * 1.0f));
		addChild(loginMenu);
		loginMenu.setVisible(false);
		
		//	設定武器子選單
		int [] weaponStatus = fighter.getWeaponStatus();
		if (weaponStatus[0] == 0) {	//	尚未開放
			btn_weapon[0] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png")), this, "clickWeaponBtn");
			
			CCSprite sprite0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("corn.png"));
			sprite0.setPosition(CGPoint.ccp(btn_weapon[0].getContentSize().width / 2, btn_weapon[0].getContentSize().height / 2));
			btn_weapon[0].addChild(sprite0);
			CCSprite sprite1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png"));
			sprite1.setPosition(CGPoint.ccp(btn_weapon[0].getContentSize().width / 2, btn_weapon[0].getContentSize().height / 2));
			btn_weapon[0].addChild(sprite1);
			
			btn_weapon[0].setTag(0);
		} else {	//	已開放
			btn_weapon[0] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_select.png")), this, "clickWeaponBtn");
			
			CCSprite sprite0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("corn.png"));
			sprite0.setPosition(CGPoint.ccp(btn_weapon[0].getContentSize().width / 2, btn_weapon[0].getContentSize().height / 2));
			btn_weapon[0].addChild(sprite0);
			
			btn_weapon[0].setTag(1);
		}
		btn_weapon[0].setScale(backgroundScale);		
				
		if (weaponStatus[1] == 0) {	//	尚未開放
			btn_weapon[1] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png")), this, "clickWeaponBtn");
			
			CCSprite sprite0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("watermelon.png"));
			sprite0.setPosition(CGPoint.ccp(btn_weapon[1].getContentSize().width / 2, btn_weapon[1].getContentSize().height / 2));
			btn_weapon[1].addChild(sprite0);
			CCSprite sprite1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png"));
			sprite1.setPosition(CGPoint.ccp(btn_weapon[1].getContentSize().width / 2, btn_weapon[1].getContentSize().height / 2));
			btn_weapon[1].addChild(sprite1);
			
			btn_weapon[1].setTag(0);	
		} else {	//	已開放
			btn_weapon[1] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_select.png")), this, "clickWeaponBtn");
			
			CCSprite sprite0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("watermelon.png"));
			sprite0.setPosition(CGPoint.ccp(btn_weapon[1].getContentSize().width / 2, btn_weapon[1].getContentSize().height / 2));
			btn_weapon[1].addChild(sprite0);
			
			btn_weapon[1].setTag(1);
		}
		btn_weapon[1].setScale(backgroundScale);
		
		if (weaponStatus[2] == 0) {	//	尚未開放
			btn_weapon[2] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png")), this, "clickWeaponBtn");
			
			CCSprite sprite0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("grape.png"));
			sprite0.setPosition(CGPoint.ccp(btn_weapon[2].getContentSize().width / 2, btn_weapon[1].getContentSize().height / 2));
			btn_weapon[2].addChild(sprite0);
			CCSprite sprite1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png"));
			sprite1.setPosition(CGPoint.ccp(btn_weapon[2].getContentSize().width / 2, btn_weapon[2].getContentSize().height / 2));
			btn_weapon[2].addChild(sprite1);
			
			btn_weapon[2].setTag(0);	
		} else {	//	已開放
			btn_weapon[2] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_select.png")), this, "clickWeaponBtn");
			
			CCSprite sprite0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("grape.png"));
			sprite0.setPosition(CGPoint.ccp(btn_weapon[2].getContentSize().width / 2, btn_weapon[2].getContentSize().height / 2));
			btn_weapon[2].addChild(sprite0);
			
			btn_weapon[2].setTag(1);	
		}
		btn_weapon[2].setScale(backgroundScale);
		
		CCMenu wRow0_0 = CCMenu.menu(btn_weapon[0], btn_weapon[1]);	//	page 0, row 0
		wRow0_0.setColor(ccColor3B.ccc3(255, 255, 255));
		wRow0_0.setPosition(CGPoint.ccp(btn_weapon[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f + btn_weapon[0].getBoundingBox().size.height));
		wRow0_0.alignItemsHorizontally(btn_weapon[0].getBoundingBox().size.width * 0.25f);
		
		CCMenu wRow0_1 = CCMenu.menu(btn_weapon[2]);	//	page 0, row 1
		wRow0_1.setColor(ccColor3B.ccc3(255, 255, 255));
		wRow0_1.setPosition(CGPoint.ccp(btn_weapon[0].getBoundingBox().size.width * 1.225f, winSize.height * 0.5f - btn_weapon[0].getBoundingBox().size.height * 0.125f));
		wRow0_1.alignItemsHorizontally(btn_weapon[0].getBoundingBox().size.width * 0.25f);
		
		wPages[0] = CCLayer.node();
		wPages[0].addChild(wRow0_0);		
		wPages[0].addChild(wRow0_1);		
		
		wBtnNum = 3;
		wPageNum = 1;
		
		// 武器階級
		for (int i = 0;i < wBtnNum;i++) {
			if (fighter.getWeaponLevel(i) > 0) {
				levelSprite[fighter.getWeaponLevel(i)] = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("level" + fighter.getWeaponLevel(i) + ".png"));
				levelSprite[fighter.getWeaponLevel(i)].setPosition(CGPoint.ccp(btn_weapon[0].getContentSize().width * 0.85f, btn_weapon[0].getContentSize().height * 0.1f));
				btn_weapon[i].addChild(levelSprite[fighter.getWeaponLevel(i)]);
			}
		}
		
		//	設定道具子選單
		btn_prop[0] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_select.png")), this, "clickPropBtn");
		
		CCSprite sprite0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("vitaminC.png"));
		sprite0.setPosition(CGPoint.ccp(btn_prop[0].getContentSize().width / 2, btn_prop[0].getContentSize().height / 2));
		sprite0.setTag(1);
		btn_prop[0].addChild(sprite0);
		btn_prop[0].setTag(1);
		
		btn_prop[1] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_select.png")), this, "clickPropBtn");
		
		CCSprite sprite1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("vitaminD.png"));
		sprite1.setPosition(CGPoint.ccp(btn_prop[1].getContentSize().width / 2, btn_prop[1].getContentSize().height / 2));
		sprite1.setTag(1);
		btn_prop[1].addChild(sprite1);
		btn_prop[1].setTag(1);
		
		btn_prop[4] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_select.png")), this, "clickPropBtn");
		
		CCSprite sprite4 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("egg.png"));
		sprite4.setPosition(CGPoint.ccp(btn_prop[4].getContentSize().width / 2, btn_prop[4].getContentSize().height / 2));
		sprite4.setTag(1);
		btn_prop[4].addChild(sprite4);
		btn_prop[4].setTag(1);
		
		if (fighter.getPlayerLevel() >= 16) {	//	道具 3、4、6 到第二循環關才開啟
			btn_prop[2] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_select.png")), this, "clickPropBtn");
			
			CCSprite sprite2 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("vitaminE.png"));
			sprite2.setPosition(CGPoint.ccp(btn_prop[2].getContentSize().width / 2, btn_prop[2].getContentSize().height / 2));
			sprite2.setTag(1);
			btn_prop[2].addChild(sprite2);
			btn_prop[2].setTag(1);
			
			btn_prop[3] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_select.png")), this, "clickPropBtn");
			
			CCSprite sprite3 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("ketchup.png"));
			sprite3.setPosition(CGPoint.ccp(btn_prop[3].getContentSize().width / 2, btn_prop[3].getContentSize().height / 2));
			sprite3.setTag(1);
			btn_prop[3].addChild(sprite3);
			btn_prop[3].setTag(1);
			
			btn_prop[5] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_select.png")), this, "clickPropBtn");
			
			CCSprite sprite5 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("niceegg.png"));
			sprite5.setPosition(CGPoint.ccp(btn_prop[5].getContentSize().width / 2, btn_prop[5].getContentSize().height / 2));
			sprite5.setTag(1);
			btn_prop[5].addChild(sprite5);
			btn_prop[5].setTag(1);
		} else {
			btn_prop[2] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), this, "clickPropBtn");
			
			CCSprite sprite2 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("vitaminE.png"));
			sprite2.setPosition(CGPoint.ccp(btn_prop[2].getContentSize().width / 2, btn_prop[2].getContentSize().height / 2));
			sprite2.setTag(1);
			btn_prop[2].addChild(sprite2);
			CCSprite sprite20 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png"));
			sprite20.setPosition(CGPoint.ccp(btn_prop[2].getContentSize().width / 2, btn_prop[2].getContentSize().height / 2));
			sprite20.setTag(1);
			btn_prop[2].addChild(sprite20);
			
			btn_prop[2].setTag(0);
			//btn_prop[2].setIsEnabled(false);
			
			btn_prop[3] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), this, "clickPropBtn");
			
			CCSprite sprite3 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("ketchup.png"));
			sprite3.setPosition(CGPoint.ccp(btn_prop[3].getContentSize().width / 2, btn_prop[3].getContentSize().height / 2));
			sprite3.setTag(1);
			btn_prop[3].addChild(sprite3);
			CCSprite sprite30 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png"));
			sprite30.setPosition(CGPoint.ccp(btn_prop[3].getContentSize().width / 2, btn_prop[3].getContentSize().height / 2));
			sprite30.setTag(1);
			btn_prop[3].addChild(sprite30);
			
			btn_prop[3].setTag(0);	
			//btn_prop[3].setIsEnabled(false);
			
			btn_prop[5] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_reject.png")), this, "clickPropBtn");
			
			CCSprite sprite5 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("niceegg.png"));
			sprite5.setPosition(CGPoint.ccp(btn_prop[5].getContentSize().width / 2, btn_prop[5].getContentSize().height / 2));
			sprite5.setTag(1);
			btn_prop[5].addChild(sprite5);
			CCSprite sprite50 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png"));
			sprite50.setPosition(CGPoint.ccp(btn_prop[5].getContentSize().width / 2, btn_prop[5].getContentSize().height / 2));
			sprite50.setTag(1);
			btn_prop[5].addChild(sprite50);
			
			btn_prop[5].setTag(0);	
			//btn_prop[5].setIsEnabled(false);
		}
		
		for (int i = 0;i < maxPropNum;i++) {
			btn_prop[i].setScale(backgroundScale);
			
			propNumLabel[i] = CCLabel.makeLabel(""+fighter.getPropNum(i), "Arial", 15);
			propNumLabel[i].setColor(ccColor3B.ccBLACK);
			propNumLabel[i].setPosition(btn_prop[i].getBoundingBox().size.width * 0.88f, btn_prop[i].getBoundingBox().size.height * 0.1f);
			btn_prop[i].addChild(propNumLabel[i]);			
		}				
    	
		CCMenu pRow0_0 = CCMenu.menu(btn_prop[0], btn_prop[1]);	//	page 0, row 0
		pRow0_0.setColor(ccColor3B.ccc3(255, 255, 255));
		pRow0_0.setPosition(CGPoint.ccp(btn_prop[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f + btn_prop[0].getBoundingBox().size.height));
		pRow0_0.alignItemsHorizontally(btn_prop[0].getBoundingBox().size.width * 0.25f);
		
		CCMenu pRow0_1 = CCMenu.menu(btn_prop[2], btn_prop[3]);	//	page 0, row 1
		pRow0_1.setColor(ccColor3B.ccc3(255, 255, 255));
		pRow0_1.setPosition(CGPoint.ccp(btn_prop[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f - btn_prop[0].getBoundingBox().size.height * 0.125f));					
		pRow0_1.alignItemsHorizontally(btn_prop[0].getBoundingBox().size.width * 0.25f);					
		
		CCMenu pRow0_2 = CCMenu.menu(btn_prop[4], btn_prop[5]);
		pRow0_2.setColor(ccColor3B.ccc3(255, 255, 255));
		pRow0_2.setPosition(CGPoint.ccp(btn_prop[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f - btn_prop[0].getBoundingBox().size.height * 1.25f));					
		pRow0_2.alignItemsHorizontally(btn_prop[0].getBoundingBox().size.width * 0.25f);		
		
		pPages[0] = CCLayer.node();
		pPages[0].addChild(pRow0_0);
		pPages[0].addChild(pRow0_1);
		pPages[0].addChild(pRow0_2);
		
		pBtnNum = 6;
		pPageNum = 1;
		
		// default畫面
		addChild(wPages[0]);
		curLayer = 0;
		_label1.setString("請選擇一把武器");
		
		// Handle sound		
		//SoundEngine.sharedEngine().preloadEffect(context, R.raw.pew_pew_lei);
		//SoundEngine.sharedEngine().preloadEffect(context, R.raw.next);
		//SoundEngine.sharedEngine().setSoundVolume(1f);
		//SoundEngine.sharedEngine().playSound(context, R.raw.background_music_aac, true);
			
	}
	public void clickMenuBtn(Object sender) {
		CCMenuItemSprite item = (CCMenuItemSprite) sender;		
		
		if (item.getTag() == 0) {			//	Click backward button			
			if (curPage > 0) {								
				if (curLayer == 0) {
					removeChild(wPages[curPage], false);
					addChild(wPages[curPage-1]);
					curPage -= 1;					
				} else if (curLayer == 1) {
					removeChild(pPages[curPage], false);
					addChild(pPages[curPage-1]);
					curPage -= 1;
				}
			}
		} else if (item.getTag() == 1) {		//	Click weapon button
			//SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);
			Log.d("btn", "Press btn 1");					
			
			if (fighter.getSelectedWeapon() > -1) {
				//	顯示上一次選擇的按鈕細節
				for (int i = 0;i < wBtnNum;i++) {
					if (btn_weapon[i].getTag() == 2) {
						levelUpMenu.setVisible(true);
						
						_label1.setString(weapons[i].getName());
						_label2.setString(weapons[i].getFunc());					
						
						if (fighter.getMoney() < weapons[i].getLevelUpPrice()[fighter.getWeaponLevel(i)] || fighter.getWeaponLevel(i) >= 10) {
							levelUpBtn.setIsEnabled(false);
							levelUpMenu.setOpacity(100);
							_label3.setString(" ");
						} else {						
							levelUpBtn.setIsEnabled(true);
							levelUpMenu.setOpacity(800);
							_label3.setString("$" + weapons[i].getLevelUpPrice()[fighter.getWeaponLevel(i)]);
						}
						break;
					}
				}
			} else {
				_label1.setString("請選擇一把武器");
				_label2.setString(" ");
				_label3.setString(" ");
			}
			
			if (curLayer == 0) {	//	at weapon layer
				//	Nothing				
			} else if (curLayer == 1) {	//	at prop layer	
				buyMenu.setVisible(false);
				removeChild(pPages[curPage], false);
				addChild(wPages[0]);												
			} else if (curLayer == 2) {	//	at facebook layer	
				loginMenu.setVisible(false);
				removeChild(fPages[curPage], false);				
				addChild(wPages[0]);				
			} else {	//	at first
				Log.d("btn", "Press btn 1, first");				
				addChild(wPages[0]);
			}
			
			curPage = 0;											
			curLayer = 0;							
		} else if (item.getTag() == 2) {		// Click prop button
			//SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);			
			Log.d("btn", "Press btn 2");
			
			_label1.setString("請選擇道具");
			
			if (curLayer == 0) {	//	at weapon layer					
				levelUpMenu.setVisible(false);
				_label2.setString(" ");
				_label3.setString(" ");
				removeChild(wPages[curPage], false);
				addChild(pPages[0]);
			} else if (curLayer == 1) {	//	at prop layer
				//	Nothing
			} else if (curLayer == 2) {	//	at facebook layer					
				_label2.setString(" ");
				_label3.setString(" ");			
				loginMenu.setVisible(false);
				removeChild(fPages[curPage], false);
				addChild(pPages[0]);				
			} else {	//	at first			
				Log.d("btn", "Press btn 2 at first");				
				addChild(pPages[0]);
			}
			
			curPage = 0;					
			curLayer = 1;			
		} else if (item.getTag() == 3) {		//	Click facebook button
			Log.d("btn", "Press btn 3");									
			
			if (curLayer == 0) {	//	at weapon layer
				levelUpMenu.setVisible(false);
				_label1.setString(" ");
				_label2.setString(" ");
				_label3.setString(" ");
				removeChild(wPages[curPage], false);						
			} else if (curLayer == 1) {	//	at prop layer		
				buyMenu.setVisible(false);
				_label1.setString(" ");
				_label2.setString(" ");
				_label3.setString(" ");
				removeChild(pPages[curPage], false);										
			} else if (curLayer == 2) {	//	at facebook layer
				//	Nothing
			}			
			
			_label1.setString("請選擇三位好友支援大絕招");
			
			if (getFriend) {		//	抓取facebook朋友資料
				if (FB.is_login()) {																												
					for(int i = 0;i < topNArray.length();i++) {																																
						btn_friend[i] = CCMenuItemSprite.item(friendSprite[i], friendSprite[i], this, "clickFacebookBtn");
						btn_friend[i].setTag(0);
						btn_friend[i].setScale(backgroundScale * 2.0f);																		
					}					
				} else {				
					_label2.setString("馬上登入FB跟朋友一起戰鬥!");					
					
					for (int i = 0;i < maxFriendNum;i++) {
						btn_friend[i] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tomato"+(i+1)+".png")), 
								CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tomato"+(i+1)+".png")), this, "clickFacebookBtn");
						btn_friend[i].setTag(0);
						btn_friend[i].setScale(backgroundScale * 2.0f);
					}
				}
				
				CGSize winSize = CCDirector.sharedDirector().displaySize();														
				
				CCMenu fRow0_0 = CCMenu.menu(btn_friend[0], btn_friend[1]);	//	page 0, row 0
				fRow0_0.setColor(ccColor3B.ccc3(255, 255, 255));					
				fRow0_0.setPosition(CGPoint.ccp(btn_friend[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f + btn_friend[0].getBoundingBox().size.height));					
				fRow0_0.alignItemsHorizontally(btn_friend[0].getBoundingBox().size.width * 0.25f);					
				
				CCMenu fRow0_1 = CCMenu.menu(btn_friend[2], btn_friend[3]);	//	page 0, row 1
				fRow0_1.setColor(ccColor3B.ccc3(255, 255, 255));
				fRow0_1.setPosition(CGPoint.ccp(btn_friend[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f - btn_friend[0].getBoundingBox().size.height * 0.125f));					
				fRow0_1.alignItemsHorizontally(btn_weapon[0].getBoundingBox().size.width * 0.25f);					
				
				CCMenu fRow0_2 = CCMenu.menu(btn_friend[4], btn_friend[5]);
				fRow0_2.setColor(ccColor3B.ccc3(255, 255, 255));
				fRow0_2.setPosition(CGPoint.ccp(btn_friend[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f - btn_friend[0].getBoundingBox().size.height * 1.25f));					
				fRow0_2.alignItemsHorizontally(btn_friend[0].getBoundingBox().size.width * 0.25f);																			
				
				fPages[0] = CCLayer.node();					
				fPages[0].addChild(fRow0_0);
				fPages[0].addChild(fRow0_1);
				fPages[0].addChild(fRow0_2);
				
				fPageNum = 1;
				fBtnNum = 6;
				
				getFriend = false;				
			}		
			
			if (curLayer == 0) {	//	at weapon layer						
				addChild(fPages[0]);
			} else if (curLayer == 1) {	//	at prop layer									
				addChild(fPages[0]);							
			} else if (curLayer == 2) {	//	at facebook layer
				//	Nothing
			} else {				
				Log.d("btn", "Press btn 3 at first");				
				addChild(fPages[0]);
			}
			
			curPage = 0;
			curLayer = 2;
		} else if (item.getTag() == 4) {		//	Click next button			
			if (curLayer == 0) {	//	at weapon layer
				if (curPage+1 < wPageNum) {					
					removeChild(wPages[curPage], false);
					addChild(wPages[curPage+1]);
					curPage += 1;					
				}
			} else if (curLayer == 1) {
				if (curPage+1 < pPageNum) {
					buyMenu.setVisible(false);
					levelUpMenu.setVisible(false);
					removeChild(pPages[curPage], false);
					addChild(pPages[curPage+1]);
					curPage += 1;
				}
			}
		} else if (item.getTag() == 5) {		//	Click buy icon
			for (int i = 0;i < pBtnNum;i++) {		//	購買道具
				if (curSelectBtn == btn_prop[i]) {
					Log.d("btn", "money : " + fighter.getMoney());
					
					if (fighter.getPropNum(i) == 0) {
						btn_prop[i].selected();
						btn_prop[i].setTag(2);
						fighter.setPropStatus(i, 2);
						pSelectNum += 1;
					}
					
					fighter.setMoney(fighter.getMoney() - props[i].getPrice());
					fighter.setPropNum(i, fighter.getPropNum(i) + 1);
					
					if (fighter.getMoney() < props[i].getPrice()) {
						buyBtn.setIsEnabled(false);
						buyMenu.setOpacity(100);
						_label3.setString(" ");
					}
					
					btn_prop[i].removeChild(propNumLabel[i], false);
					propNumLabel[i].setString("" + fighter.getPropNum(i));
					propNumLabel[i].setColor(ccColor3B.ccBLACK);
					propNumLabel[i].setPosition(btn_prop[i].getBoundingBox().size.width * 0.88f, btn_prop[i].getBoundingBox().size.height * 0.1f);
					btn_prop[i].addChild(propNumLabel[i]);
					
					_label4.setString("$" + fighter.getMoney());
					
					Log.d("btn", "money : " + fighter.getMoney());
					break;
				}
			}
		} else if (item.getTag() == 6) {		//	Click level up icon
			for (int i = 0;i < wBtnNum;i++) {
				if (curSelectBtn == btn_weapon[i]) {
					Log.d("btn", "weapon level up : $" + weapons[i].getLevelUpPrice()[fighter.getWeaponLevel(i)]);
					
					if (fighter.getWeaponLevel(i) > 0)
						btn_weapon[i].removeChild(levelSprite[fighter.getWeaponLevel(i)], false);
					
					fighter.setMoney(fighter.getMoney() - weapons[i].getLevelUpPrice()[fighter.getWeaponLevel(i)]);
					fighter.setWeaponLevel(i, fighter.getWeaponLevel(i) + 1);
					fighter.setSelectedWeapon(i, fighter.getWeaponLevel(i));
					
					levelSprite[fighter.getWeaponLevel(i)] = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("level" + fighter.getWeaponLevel(i) + ".png"));
					levelSprite[fighter.getWeaponLevel(i)].setPosition(CGPoint.ccp(btn_weapon[0].getContentSize().width * 0.85f, btn_weapon[0].getContentSize().height * 0.1f));
					btn_weapon[i].addChild(levelSprite[fighter.getWeaponLevel(i)]);					
					
					Log.d("btn", "weapon level : " + fighter.getWeaponLevel(i));
					
					if (fighter.getMoney() < weapons[i].getLevelUpPrice()[fighter.getWeaponLevel(i)] || fighter.getWeaponLevel(i) >= 10) {
						levelUpBtn.setIsEnabled(false);
						levelUpMenu.setOpacity(100);
						_label3.setString(" ");
					} else {
						levelUpBtn.setIsEnabled(true);
						levelUpMenu.setOpacity(800);
						_label3.setString("$" + weapons[i].getLevelUpPrice()[fighter.getWeaponLevel(i)]);
					}
					
					_label4.setString("$" + fighter.getMoney());
					
					break;
				}
			}	
		} else if (item.getTag() == 7) {		//	Click fight icon
			//	轉換到選關畫面, 儲存玩家資訊
			Log.d("btn", "切換畫面");
			if (fighter.getSelectedWeapon() != -1 && fSelectNum >= maxFriendSelectNum) {				
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).saveFighter();		
				if(FB.is_login())				
				FB.set_property("level", "" + fighter.getPlayerLevel());
				
				int count = 0;
				for (int i = 0;i < fBtnNum;i++) {
					if (btn_friend[i].getTag() == 1) {						
						int level = 0;
						if(FB.is_login())
						{
							try {
								JSONObject person = topNArray.getJSONObject(i);	// 取得每個人的資料(已經排序過了)
								level = Integer.valueOf(person.getString("value"));
							} catch (JSONException e) {							
								level = 1;
							}
						}
						else
						{
							Random randD = new Random();							
							level = randD.nextInt(6)+1;
							((CCSprite)btn_friend[i].getNormalImage()).setTag(77);
						}
						((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedFriendSprite[count] = (CCSprite)btn_friend[i].getNormalImage();
						//Log.d("selectedfriend", "countlevel"+count+" "+level);
						fighter.setSelectedFriend(count, level);
						count += 1;
					}
				}
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedPropSprite[0] = null;
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedPropSprite[1] = null;
				count = 0;
				for (int i = 0;i < pBtnNum;i++) {					
					if (btn_prop[i].getTag() == 2) {
						((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedPropSprite[count] = (CCSprite)btn_prop[i].getChildByTag(1);
						fighter.setSelectedProp(count, i);
						count += 1;
					}
					else
					{
						
					}
				}
				if(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).isQuickGame)
				{
					((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = true;
					CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLVQuick.scene(), tOrientation.kOrientationRightOver));
				}
				else
				{
					CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(1, ChoiceGameLayer.scene(), ccColor3B.ccWHITE));
				}
			}
			else
			{
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).toastMessage("請選好1把武器和3個FB朋友!");
			}
		}		
	}
	
	public void clickWeaponBtn(Object sender) {
		CCMenuItemSprite item = (CCMenuItemSprite) sender;
		
		Log.d("btn", "all btn status :" + btn_weapon[0].getTag() + " " + btn_weapon[1].getTag() + " " + btn_weapon[2].getTag());
		curSelectBtn = item;
		
		if (item.getTag() == 0) {	//	尚未開放	
			for (int i = 0;i < wBtnNum;i++) {
				if (btn_weapon[i].getTag() == 2) {
					btn_weapon[i].unselected();
					btn_weapon[i].setTag(1);
					fighter.setWeaponStatus(i, 1);
					fighter.setSelectedWeapon(-1, -1);
					wSelectNum -= 1;
					btn_weapon[i].stopAction(100);
					btn_weapon[i].stopAction(101);
					
					break;
				}
			}
			
			_label1.setString(" ");
			_label2.setString("等級不足...");
			_label3.setString(" ");
			levelUpMenu.setVisible(false);
			
		} else if (item.getTag() == 1) {	//	已開放
			levelUpMenu.setVisible(true);
			
			for (int i = 0;i < wBtnNum;i++) {
				if (btn_weapon[i].getTag() == 2) {
					btn_weapon[i].unselected();
					btn_weapon[i].setTag(1);
					fighter.setWeaponStatus(i, 1);
					fighter.setSelectedWeapon(-1, -1);
					wSelectNum -= 1;
					btn_weapon[i].stopAction(100);
					btn_weapon[i].stopAction(101);
					
					break;
				}
			}
				
			for (int i = 0;i < wBtnNum;i++) {
				if (item == btn_weapon[i]) {						
					_label1.setString(weapons[i].getName());
					_label2.setString(weapons[i].getFunc());
					
					btn_weapon[i].selected();
					btn_weapon[i].setTag(2);
					
					fighter.setWeaponStatus(i, 2);
					fighter.setSelectedWeapon(i, fighter.getWeaponLevel(i));
					wSelectNum += 1;
					
					if (fighter.getMoney() > weapons[i].getLevelUpPrice()[fighter.getWeaponLevel(i)] && fighter.getWeaponLevel(i) < 10) {
						_label3.setString("" + weapons[i].getLevelUpPrice()[fighter.getWeaponLevel(i)]);
						levelUpMenu.setOpacity(800);
						levelUpBtn.setIsEnabled(true);
					} else {
						_label3.setString(" ");
						levelUpMenu.setOpacity(100);
						levelUpBtn.setIsEnabled(false);
					}									
					
					CCAction action = CCRepeatForever.action(
							CCSequence.actions(
									CCFadeOut.action(0.1f),
									CCFadeIn.action(0.1f),
									CCFadeOut.action(0.1f),
									CCFadeIn.action(0.1f)															
							));
					action.setTag(100);
					
					//	2011.11.21, yujhe
					CCAction action2 = (CCRepeatForever.action(
							CCSequence.actions(
									CCRotateBy.action(0.1f, 20),
									CCRotateBy.action(0.1f, -40),
									CCRotateBy.action(0.1f, 20),
									CCDelayTime.action(2)
							)));
					action2.setTag(101);
					
					btn_weapon[i].runAction(action);
					btn_weapon[i].runAction(action2);
					
					break;
				}
			}			
			Log.d("btn", "current weapon index : " + fighter.getSelectedWeapon());
			Log.d("btn", "current weapon level : " + fighter.getSelectedWeaponLevel());
		} else if (item.getTag() == 2) {	//	已選取
			//item.selected();
			for (int i = 0;i < wBtnNum;i++) {
				if (item == btn_weapon[i]) {
					_label1.setString(" ");
					_label2.setString(" ");
					_label3.setString(" ");
					item.unselected();
					item.setTag(1);
					fighter.setWeaponStatus(i, 1);
					wSelectNum -= 1;
					btn_weapon[i].stopAction(100);
					btn_weapon[i].stopAction(101);
					break;
				}
			}
			
			levelUpMenu.setVisible(false);
		}
	}
	
	public void clickPropBtn(Object sender) {
		CCMenuItemSprite item = (CCMenuItemSprite) sender;
		
		curSelectBtn = item;
		
		if (item.getTag() == 0) {	//	尚未開放
			_label1.setString(" ");
			_label2.setString("尚未開放...");
			_label3.setString(" ");
			buyMenu.setVisible(false);			
		} else if (item.getTag() == 1) {	//	已開放
			buyMenu.setVisible(true);
			
			if (pSelectNum < maxPropSelectNum) {
				for (int i = 0;i < pBtnNum;i++) {
					if (item == btn_prop[i]) {
						_label1.setString(props[i].getName());
						_label2.setString(props[i].getFunc());
						
						if(unOwnedSelected) //unused
						{
							for(int j = 0;j < pBtnNum;j++)
							{									
								btn_prop[j].setTag(1);
								//fighter.setPropStatus(j, 1);									
								btn_prop[j].stopAction(100);
								btn_prop[j].stopAction(101);
							}								
						}
						if (fighter.getPropNum(i) > 0) {
							item.selected();
							item.setTag(2);
							fighter.setPropStatus(i, 2);
							pSelectNum += 1;
							
							CCAction action = CCRepeatForever.action(
									CCSequence.actions(
											CCFadeOut.action(0.1f),
											CCFadeIn.action(0.1f),
											CCFadeOut.action(0.1f),
											CCFadeIn.action(0.1f)															
									));
							action.setTag(100);
							
							//	2011.11.21, yujhe
							CCAction action2 = (CCRepeatForever.action(
									CCSequence.actions(
											CCRotateBy.action(0.1f, 20),
											CCRotateBy.action(0.1f, -40),
											CCRotateBy.action(0.1f, 20),
											CCDelayTime.action(2)
									)));
							action2.setTag(101);
							
							btn_prop[i].runAction(action);
							btn_prop[i].runAction(action2);
						}
						else
						{							
							////unOwnedSelected = true;
							////item.setTag(3);	//selected but does not own, just blink
							//fighter.setPropStatus(i, 3);							
						}
						
						
						if (fighter.getMoney() > props[i].getPrice()) {
							_label3.setString("" + props[i].getPrice());
							buyMenu.setOpacity(800);
							buyBtn.setIsEnabled(true);
						} else {
							_label3.setString(" ");
							buyMenu.setOpacity(100);
							buyBtn.setIsEnabled(false);
						}
						
						
						break;
					}
				}
			}
		} else if (item.getTag() == 2) {	//	已選取
			buyMenu.setVisible(false);
			
			for (int i = 0;i < pBtnNum;i++) {
				if (item == btn_prop[i]) {
					_label1.setString(" ");
					_label2.setString(" ");
					_label3.setString(" ");
					item.unselected();
					item.setTag(1);
					fighter.setPropStatus(i, 1);
					pSelectNum -= 1;
					btn_prop[i].stopAction(100);
					btn_prop[i].stopAction(101);
					break;
				}
			}
		}else if (item.getTag() == 3) {	//	已選取 but doesn't own
			buyMenu.setVisible(false);
			unOwnedSelected = false;
			for (int i = 0;i < pBtnNum;i++) {
				if (item == btn_prop[i]) {
					_label1.setString(" ");
					_label2.setString(" ");
					_label3.setString(" ");
					//item.unselected();
					item.setTag(1);
					//fighter.setPropStatus(i, 1);
					//pSelectNum -= 1;
					btn_prop[i].stopAction(100);
					btn_prop[i].stopAction(101);
					break;
				}
			}
		}
	}
	
	public void clickFacebookBtn(Object sender) {
		CCMenuItemSprite item = (CCMenuItemSprite) sender;
		
		_label1.setString(" ");
		_label3.setString(" ");
		
		if (item.getTag() == 0) {						
			if (fSelectNum < maxFriendSelectNum) {
				for (int i = 0;i < fBtnNum;i++) {
					if (item == btn_friend[i]) {
						Log.d("fb", "" + i);						
												
						CCSprite sprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("weaponList_lock.png"));
						sprite.setPosition(CGPoint.ccp(btn_friend[0].getContentSize().width / 2, btn_friend[0].getContentSize().height / 2));
						sprite.setTag(99);
						
						btn_friend[i].addChild(sprite);						
						btn_friend[i].setTag(1);
						btn_friend[i].setScale(backgroundScale);
						
						//	2011.11.21, yujhe
						CCAction action2 = (CCRepeatForever.action(
								CCSequence.actions(
										CCRotateBy.action(0.1f, 20),
										CCRotateBy.action(0.1f, -40),
										CCRotateBy.action(0.1f, 20),
										CCDelayTime.action(2)
								)));
						action2.setTag(101);
						
						btn_friend[i].runAction(action2);
						
						if(FB.is_login())
						{
							_label1.setString(fNameList.get(i));
							_label3.setString("Level "+fLevelList.get(i));
						}
						else
						{
							_label1.setString("壞壞朋友番茄");
							_label3.setString("Level 1~5");							
						}
						fSelectNum += 1;
						break;
					}
				}			
			}
		} else if (item.getTag() == 1) {						
			for (int i = 0;i < fBtnNum;i++) {
				if (item == btn_friend[i]) {					
					btn_friend[i].removeChildByTag(99, false);
					btn_friend[i].setTag(0);
					btn_friend[i].setScale(backgroundScale * 2.0f);
					btn_friend[i].stopAction(101);
					fSelectNum -= 1;
					break;
				}
			}
		}
	}

	public void clickLoginBtn(Object sender) {
		CCMenuItemLabel item = (CCMenuItemLabel) sender;
		
		if (item.getTag() == 0) {			
			Log.d("btn", "click login btn");
			if (!FB.is_login()) {
				if(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).isInternetOn)
				{
					Log.d("fb", "login in Set.java@!");
					DialogListener fbDialog = new DialogListener() {
						@Override
						public void onComplete(Bundle values) {	            	
			            	Log.d("fb", "login in TomatoStoreView.java@!");
														
							for(int i = 0;i < topNArray.length();i++) {																
								btn_friend[i].setNormalImage(friendSprite[i]);
								btn_friend[i].setSelectedImage(friendSprite[i]);
								btn_friend[i].setTag(0);
								btn_friend[i].setScale(backgroundScale * 2.0f);														
							}
							loginMenu.setVisible(false);
			            }
						
						@Override
			            public void onFacebookError(FacebookError error) {
			            	Log.d("fb","login error:" + error.toString() );
			            }
						
						@Override
			            public void onError(DialogError e) {
			            	Log.e("fb","error:"+ e.toString());
				        }
						
						@Override
			            public void onCancel() {
			            	Log.e("fb","login: cancel");
				        }
			        };
					FB.login(fbDialog);
				}
				else
				{
					((TomatoshootGame)CCDirector.sharedDirector().getActivity()).toastMessage("請連上網路以登入FB~");
				}
			}
		}
	}
	
	@Override
	public void onExit() {
		//SoundEngine.sharedEngine().realesAllSounds();
		//SoundEngine.sharedEngine().setSoundVolume(0.5f);
		//SoundEngine.sharedEngine().playSound(context, R.raw.mr_reno, true);
		
		super.onExit();		
	}
}
