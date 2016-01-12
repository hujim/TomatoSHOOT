package org.mirlab.tomatoshoot;

import java.net.URL;
import java.util.ArrayList;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCFlipXTransition;
import org.cocos2d.transitions.CCTransitionScene.tOrientation;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

public class SetGameItemLayer extends CCColorLayer {
	protected int curLayer = -1;	//	供切換選單使用
	private int curPage = 0;
	
	private int maxPageNum = 5;
	private int maxWeaponNum = 12;
	private int maxPropNum = 20;
	private int maxFriendNum = 6;
	private int maxWeaponSelectNum = 1;
	private int maxPropSelectNum = 2;
	private int maxFriendSelectNum = 2;
	
	private item [] weapons = new item[maxWeaponNum];
	private item [] props = new item[maxPropNum];	
	
	private CCMenuItemImage [] btn_weapon = new CCMenuItemImage[maxWeaponNum];
	private CCMenuItemImage [] btn_prop = new CCMenuItemImage[maxPropNum];
	private CCMenuItemSprite [] btn_friend = new CCMenuItemSprite[maxFriendNum];
	
	private int wBtnNum = 0;
	private int pBtnNum = 0;
	private int fBtnNum = 0;
	
	private CCLayer [] wPages = new CCLayer[maxPageNum];	
	private CCLayer [] pPages = new CCLayer[maxPageNum];
	private CCLayer [] fPages = new CCLayer[maxPageNum];
	
	private int wPageNum = 0;
	private int pPageNum = 0;
	private int fPageNum = 0;
	
	private int wSelectNum = 0;
	private int pSelectNum = 0;
	private int fSelectNum = 0; 
	
	protected CCLabel _label1;
	protected CCLabel _label2;
	protected CCLabel _label3;
	protected CCLabel _label4;
	
	private CCMenuItemImage curSelectBtn;
	private CCMenu buyMenu;	
	private CCMenu fRow0_0, fRow0_1, fRow0_2;
	
	//private ArrayList<String> IdList = new ArrayList<String>(); 
	//private ArrayList<String> NameList = new ArrayList<String>(); 	
	
	private boolean getFriend = true;
	
	mirfb FB;
	TomatoFighter fighter;	
	ArrayList<String> IdList;
	ArrayList<String> NameList;
	ArrayList<String> FriendList;
	
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
		
		this.setIsTouchEnabled(true);
		
		FB = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).FB;
		fighter = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).tomatoFighter;
		IdList = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).IdList;
		NameList = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).NameList;
		FriendList = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).FriendList;
		
		Log.d("btn", "user : " + fighter.getID());
		Log.d("btn", "money : "+ fighter.getMoney());
		Log.d("fb", "2 : " + FriendList.toString());
		
		// 背景圖
		CGSize winSize = CCDirector.sharedDirector().displaySize();	
		
		//CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("SetGameItemLayer.plist");
		CCSprite background = CCSprite.sprite("weaponList.png");
		//CCSprite background = CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("weaponList.png"));
		//CCSprite background = CCSprite.sprite("weaponListSample.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		//float backgroundScale = winSize.height/(background.getTextureRect().size.height);
		float backgroundScale = winSize.width/(background.getTextureRect().size.width);
		
		background.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		background.setScale(backgroundScale);
		addChild(background);
		
		//	set label
		_label1 = CCLabel.makeLabel("Name", "Arial", 25);
		_label1.setColor(ccColor3B.ccBLACK);
		_label1.setPosition(winSize.width * 3 / 4.0f, winSize.height / 2.0f + 40);		
		addChild(_label1);
		_label2 = CCLabel.makeLabel("Func", "Arial", 25);
		_label2.setColor(ccColor3B.ccBLACK);
		_label2.setPosition(winSize.width * 3 / 4.0f, winSize.height / 2.0f);		
		addChild(_label2);
		_label3 = CCLabel.makeLabel("Price", "Arial", 25);
		_label3.setColor(ccColor3B.ccBLACK);
		_label3.setPosition(winSize.width * 3 / 4.0f, winSize.height / 2.0f - 40);		
		addChild(_label3);
		_label4 = CCLabel.makeLabel("$" + fighter.getMoney(), "Arial", 25);
		_label4.setColor(ccColor3B.ccBLACK);
		_label4.setPosition(winSize.width * 3 / 4.0f, winSize.height / 2.0f + 80);		
		addChild(_label4);
		
		//	設定武器
		weapons[0] = new item("pineapple", 200, "吃起來很酸 !");
		weapons[1] = new item("grape", 400, "散彈式的攻擊 !");
		weapons[2] = new item("corn", 800, "天羅地網般的攻擊 !");
		
		//	設定道具
		props[0] = new item("locked", 10, "方型瞄準靶心");
		props[1] = new item("aim", 20, "圓型瞄準把心");			
		
		// 武器、防具、fb好友支援、上一頁、下一頁、購買、選關選單按鈕
		// Tag number : backward : 0, weapon : 1, prop : 2, fb : 3, next : 4, buy : 5, fight : 6		
		CCMenuItemImage backwardBtn = CCMenuItemImage.item("button_backward.png", "button_backward.png", this, "clickMenuBtn");
		backwardBtn.setScale(backgroundScale);
		backwardBtn.setTag(0);
		
		CCMenu backwardMenu = CCMenu.menu(backwardBtn);
		backwardMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		backwardMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f - backwardBtn.getBoundingBox().size.width * 3.0f / 4.0f, winSize.height * 4.1f / 5.0f));
		addChild(backwardMenu);
					
		
		CCMenuItemImage weaponBtn = CCMenuItemImage.item("button_weapon.png", "button_weapon.png", this, "clickMenuBtn");
		weaponBtn.setScale(backgroundScale);
		weaponBtn.setTag(1);		
		
		CCMenu weaponMenu = CCMenu.menu(weaponBtn);
		weaponMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		weaponMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f - weaponBtn.getBoundingBox().size.width * 1.0f / 4.0f, winSize.height * 0.5f + weaponBtn.getBoundingBox().size.height));
		addChild(weaponMenu);
				
		
		CCMenuItemImage propBtn = CCMenuItemImage.item("button_prop.png", "button_prop.png", this, "clickMenuBtn");
		propBtn.setScale(backgroundScale);
		propBtn.setTag(2);
		
		CCMenu propMenu = CCMenu.menu(propBtn);
		propMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		propMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f));
		addChild(propMenu);
		
		
		CCMenuItemImage fbBtn = CCMenuItemImage.item("button_facebook.png", "button_facebook.png", this, "clickMenuBtn");
		fbBtn.setScale(backgroundScale);
		fbBtn.setTag(3);
		
		CCMenu fbBtnMenu = CCMenu.menu(fbBtn);
		fbBtnMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		fbBtnMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f - fbBtn.getBoundingBox().size.width * 0.25f, winSize.height * 0.5f - fbBtn.getBoundingBox().size.height));		
		addChild(fbBtnMenu);	
				
		
		CCMenuItemImage nextBtn = CCMenuItemImage.item("button_next.png", "button_next.png", this, "clickMenuBtn");
		nextBtn.setScale(backgroundScale);
		nextBtn.setTag(4);
		
		CCMenu nextMenu = CCMenu.menu(nextBtn);
		nextMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		nextMenu.setPosition(CGPoint.ccp(winSize.width * 0.5f - nextBtn.getBoundingBox().size.width * 3.2f / 4.0f, winSize.height * 0.9f / 5.0f));
		addChild(nextMenu);
		
		
		CCMenuItemImage buyBtn = CCMenuItemImage.item("button_buy.png", "button_buy.png", this, "clickMenuBtn");
		buyBtn.setScale(backgroundScale);
		buyBtn.setTag(5);
		
		buyMenu = CCMenu.menu(buyBtn);
		buyMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		buyMenu.setPosition(CGPoint.ccp(winSize.width * 0.75f, buyBtn.getBoundingBox().size.height * 1.0f));
		addChild(buyMenu);
		buyMenu.setVisible(false);
		
		
		CCMenuItemImage fightBtn = CCMenuItemImage.item("button_fight.png", "button_fight.png", this, "clickMenuBtn");
		fightBtn.setScale(backgroundScale);
		fightBtn.setTag(6);
		
		CCMenu fightMenu = CCMenu.menu(fightBtn);
		fightMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		fightMenu.setPosition(CGPoint.ccp(winSize.width - fightBtn.getBoundingBox().size.width * 0.5f, winSize.height - fightBtn.getBoundingBox().size.height * 0.5f));
		addChild(fightMenu);
		
		//	設定武器子選單
		int [] weaponStatus = fighter.getWeaponStatus();
		if (weaponStatus[0] == 0) {	//	尚未擁有
			btn_weapon[0] = CCMenuItemImage.item("weapon_pineapple_disabled.png", "weapon_pineapple_selected.png", this, "clickWeaponBtn");
			btn_weapon[0].setTag(0);	
		} else {	//	已擁有
			btn_weapon[0] = CCMenuItemImage.item("weapon_pineapple_unselected.png", "weapon_pineapple_selected.png", this, "clickWeaponBtn");
			btn_weapon[0].setTag(1);	
		}
		btn_weapon[0].setScale(backgroundScale);		
				
		if (weaponStatus[1] == 0) {	//	尚未擁有
			btn_weapon[1] = CCMenuItemImage.item("weapon_grape_disabled.png", "weapon_grape_selected.png", this, "clickWeaponBtn");
			btn_weapon[1].setTag(0);	
		} else {	//	已擁有
			btn_weapon[1] = CCMenuItemImage.item("weapon_grape_unselected.png", "weapon_grape_selected.png", this, "clickWeaponBtn");
			btn_weapon[1].setTag(1);	
		}
		btn_weapon[1].setScale(backgroundScale);
		
		if (weaponStatus[2] == 0) {	//	尚未擁有
			btn_weapon[2] = CCMenuItemImage.item("weapon_corn_disabled.png", "weapon_corn_selected.png", this, "clickWeaponBtn");
			btn_weapon[2].setTag(0);	
		} else {	//	已擁有
			btn_weapon[2] = CCMenuItemImage.item("weapon_corn_unselected.png", "weapon_corn_selected.png", this, "clickWeaponBtn");
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
		wPageNum = 2;
		
		//	設定道具子選單
		int [] propStatus = fighter.getPropStatus();
		if (propStatus[0] == 0) {	//	尚未擁有
			btn_prop[0] = CCMenuItemImage.item("prop_locked_disabled.png", "prop_locked_selected.png", this, "clickPropBtn");
			btn_prop[0].setTag(0);	
		} else {	//	已擁有
			btn_prop[0] = CCMenuItemImage.item("prop_locked_unselected.png", "prop_aim.png", this, "clickPropBtn");
			btn_prop[0].setTag(1);	
		}
		btn_prop[0].setScale(backgroundScale);	
		
		if (propStatus[1] == 0) {	//	尚未擁有
			btn_prop[1] = CCMenuItemImage.item("prop_aim_disabled.png", "prop_aim_selected.png", this, "clickPropBtn");
			btn_prop[1].setTag(0);	
		} else {	//	已擁有
			btn_prop[1] = CCMenuItemImage.item("prop_aim_unselected.png", "prop_aim_selected.png", this, "clickPropBtn");
			btn_prop[1].setTag(1);	
		}
		btn_prop[1].setScale(backgroundScale);
		
		CCMenu pRow0_0 = CCMenu.menu(btn_prop[0], btn_prop[1]);	//	page 0, row 0
		pRow0_0.setColor(ccColor3B.ccc3(255, 255, 255));
		pRow0_0.setPosition(CGPoint.ccp(btn_prop[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f + btn_prop[0].getBoundingBox().size.height));
		pRow0_0.alignItemsHorizontally(btn_prop[0].getBoundingBox().size.width * 0.25f);
		
		pPages[0] = CCLayer.node();
		pPages[0].addChild(pRow0_0);
		
		pBtnNum = 2;
		pPageNum = 1;				
    	
		//	設定好友選單
		if (FB.is_login()) {
			getFriend = false;
			
			Bitmap mIcon1 = null;
			URL img_value = null;	
											        	    	
			for(int i = 0;i < maxFriendNum;i++){					
				try {						
					img_value = new URL("http://graph.facebook.com/" + this.FriendList.get(i) + "/picture");
					mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());						
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (i < FriendList.size()) {
					btn_friend[i] = CCMenuItemSprite.item(CCSprite.sprite(mIcon1), CCSprite.sprite(mIcon1), this, "clickFacebookBtn");
					btn_friend[i].setTag(0);
					btn_friend[i].setScale(2.0f);
				} else {							
					btn_friend[i] = CCMenuItemSprite.item(CCSprite.sprite("button_nothing.png"), CCSprite.sprite("button_nothing.png"), this, "clickFacebookBtn");
					btn_friend[i].setTag(0);
					btn_friend[i].setScale(2.0f);
					btn_friend[i].setVisible(false);
				}
				
				//Log.d("fb", "btn : " + i + ", width : " + btn_friend[i].getBoundingBox().size.width + ", height : " + btn_friend[i].getBoundingBox().size.height);
			}																			
			
			fRow0_0 = CCMenu.menu(btn_friend[0], btn_friend[1]);	//	page 0, row 0
			fRow0_0.setColor(ccColor3B.ccc3(255, 255, 255));					
			fRow0_0.setPosition(CGPoint.ccp(btn_friend[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f + btn_friend[0].getBoundingBox().size.height));
			//fRow0_0.setPosition(CGPoint.ccp(btn_friend[0].getContentSize().width * 2.0f * 1.85f, winSize.height * 0.5f + btn_friend[0].getContentSize().height * 2.0f));
			fRow0_0.alignItemsHorizontally(btn_friend[0].getBoundingBox().size.width * 0.25f);
			//fRow0_0.alignItemsHorizontally(btn_friend[0].getContentSize().width * 2.0f * 0.25f);
			
			fRow0_1 = CCMenu.menu(btn_friend[2], btn_friend[3]);	//	page 0, row 1
			fRow0_1.setColor(ccColor3B.ccc3(255, 255, 255));
			fRow0_1.setPosition(CGPoint.ccp(btn_friend[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f - btn_friend[0].getBoundingBox().size.height * 0.125f));
			//fRow0_1.setPosition(CGPoint.ccp(btn_friend[0].getContentSize().width * 2.0f * 1.85f, winSize.height * 0.5f + btn_friend[0].getContentSize().height * 2.0f));
			fRow0_1.alignItemsHorizontally(btn_weapon[0].getBoundingBox().size.width * 0.25f);
			//fRow0_1.alignItemsHorizontally(btn_friend[0].getContentSize().width * 2.0f * 0.25f);
			
			fRow0_2 = CCMenu.menu(btn_friend[4], btn_friend[5]);
			fRow0_2.setColor(ccColor3B.ccc3(255, 255, 255));
			fRow0_2.setPosition(CGPoint.ccp(btn_friend[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f - btn_friend[0].getBoundingBox().size.height * 1.25f));
			//fRow0_2.setPosition(CGPoint.ccp(btn_friend[0].getContentSize().width * 2.0f * 1.85f, winSize.height * 0.5f + btn_friend[0].getContentSize().height * 2.0f));
			fRow0_2.alignItemsHorizontally(btn_friend[0].getBoundingBox().size.width * 0.25f);							
			//fRow0_2.alignItemsHorizontally(btn_friend[0].getContentSize().width * 2.0f * 0.25f);
			
			/*
			friendMenu = CCMenu.menu(btn_friend[0], btn_friend[1], btn_friend[2], btn_friend[3]);		
			friendMenu.setColor(ccColor3B.ccc3(255,255,255));
			friendMenu.setPosition(CGPoint.ccp(btn_weapon[0].getContentSize().width * 1.75f, winSize.height * 0.5f));
			friendMenu.alignItemsHorizontally(btn_weapon[0].getContentSize().width * 0.25f);		
			*/
			
			for (int i = 0;i < maxFriendNum;i++) {
				btn_friend[i].setScale(1.5f);
			}
			
			fPages[0] = CCLayer.node();
			//fPages[0].addChild(friendMenu);
			fPages[0].addChild(fRow0_0);
			fPages[0].addChild(fRow0_1);
			fPages[0].addChild(fRow0_2);
			
			fPageNum = 1;
			fBtnNum = 6;
		}

		// Handle sound		
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.pew_pew_lei);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.next);
		//SoundEngine.sharedEngine().setSoundVolume(1f);
		//SoundEngine.sharedEngine().playSound(context, R.raw.background_music_aac, true);
		
		//this.schedule("update");		
	}
	
	public void clickMenuBtn(Object sender) {
		CCMenuItemImage item = (CCMenuItemImage) sender;		
		
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
			
			if (curLayer == 0) {	//	at weapon layer
				//	Nothing				
			} else if (curLayer == 1) {	//	at prop layer				
				removeChild(pPages[curPage], false);
				addChild(wPages[0]);												
			} else if (curLayer == 2) {	//	at facebook layer			
				if (FB.is_login())
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
			
			if (curLayer == 0) {	//	at weapon layer				
				removeChild(wPages[curPage], false);
				addChild(pPages[0]);
			} else if (curLayer == 1) {	//	at prop layer
				//	Nothing
			} else if (curLayer == 2) {	//	at facebook layer				
				if (FB.is_login())
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
				removeChild(wPages[curPage], false);						
			} else if (curLayer == 1) {	//	at prop layer				
				removeChild(pPages[curPage], false);										
			} else if (curLayer == 2) {	//	at facebook layer
				//	Nothing
			}			
			
			if (FB.is_login()) {
				if (getFriend) {		//	抓取facebook朋友資料					
					IdList = ((TomatoStoreView)CCDirector.sharedDirector().getActivity()).IdList;
					NameList = ((TomatoStoreView)CCDirector.sharedDirector().getActivity()).NameList;
					FriendList = ((TomatoStoreView)CCDirector.sharedDirector().getActivity()).FriendList;
					
					Bitmap mIcon1 = null;
					URL img_value = null;	
													        	    	
					for(int i = 0;i < maxFriendNum;i++){					
						try {						
							img_value = new URL("http://graph.facebook.com/" + this.FriendList.get(i) + "/picture");
							mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());						
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						if (i < FriendList.size()) {
							btn_friend[i] = CCMenuItemSprite.item(CCSprite.sprite(mIcon1), CCSprite.sprite(mIcon1), this, "clickFacebookBtn");
							btn_friend[i].setTag(0);
							btn_friend[i].setScale(2.0f);
						} else {							
							btn_friend[i] = CCMenuItemSprite.item(CCSprite.sprite("button_nothing.png"), CCSprite.sprite("button_nothing.png"), this, "clickFacebookBtn");
							btn_friend[i].setTag(0);
							btn_friend[i].setScale(2.0f);
							btn_friend[i].setVisible(false);
						}
						
						//Log.d("fb", "btn : " + i + ", width : " + btn_friend[i].getBoundingBox().size.width + ", height : " + btn_friend[i].getBoundingBox().size.height);
					}
					
					CGSize winSize = CCDirector.sharedDirector().displaySize();														
					
					fRow0_0 = CCMenu.menu(btn_friend[0], btn_friend[1]);	//	page 0, row 0
					fRow0_0.setColor(ccColor3B.ccc3(255, 255, 255));					
					fRow0_0.setPosition(CGPoint.ccp(btn_friend[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f + btn_friend[0].getBoundingBox().size.height));
					//fRow0_0.setPosition(CGPoint.ccp(btn_friend[0].getContentSize().width * 2.0f * 1.85f, winSize.height * 0.5f + btn_friend[0].getContentSize().height * 2.0f));
					fRow0_0.alignItemsHorizontally(btn_friend[0].getBoundingBox().size.width * 0.25f);
					//fRow0_0.alignItemsHorizontally(btn_friend[0].getContentSize().width * 2.0f * 0.25f);
					
					fRow0_1 = CCMenu.menu(btn_friend[2], btn_friend[3]);	//	page 0, row 1
					fRow0_1.setColor(ccColor3B.ccc3(255, 255, 255));
					fRow0_1.setPosition(CGPoint.ccp(btn_friend[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f - btn_friend[0].getBoundingBox().size.height * 0.125f));
					//fRow0_1.setPosition(CGPoint.ccp(btn_friend[0].getContentSize().width * 2.0f * 1.85f, winSize.height * 0.5f + btn_friend[0].getContentSize().height * 2.0f));
					fRow0_1.alignItemsHorizontally(btn_weapon[0].getBoundingBox().size.width * 0.25f);
					//fRow0_1.alignItemsHorizontally(btn_friend[0].getContentSize().width * 2.0f * 0.25f);
					
					fRow0_2 = CCMenu.menu(btn_friend[4], btn_friend[5]);
					fRow0_2.setColor(ccColor3B.ccc3(255, 255, 255));
					fRow0_2.setPosition(CGPoint.ccp(btn_friend[0].getBoundingBox().size.width * 1.85f, winSize.height * 0.5f - btn_friend[0].getBoundingBox().size.height * 1.25f));
					//fRow0_2.setPosition(CGPoint.ccp(btn_friend[0].getContentSize().width * 2.0f * 1.85f, winSize.height * 0.5f + btn_friend[0].getContentSize().height * 2.0f));
					fRow0_2.alignItemsHorizontally(btn_friend[0].getBoundingBox().size.width * 0.25f);							
					//fRow0_2.alignItemsHorizontally(btn_friend[0].getContentSize().width * 2.0f * 0.25f);
					
					/*
					friendMenu = CCMenu.menu(btn_friend[0], btn_friend[1], btn_friend[2], btn_friend[3]);		
					friendMenu.setColor(ccColor3B.ccc3(255,255,255));
					friendMenu.setPosition(CGPoint.ccp(btn_weapon[0].getContentSize().width * 1.75f, winSize.height * 0.5f));
					friendMenu.alignItemsHorizontally(btn_weapon[0].getContentSize().width * 0.25f);		
					*/
					
					for (int i = 0;i < maxFriendNum;i++) {
						btn_friend[i].setScale(1.5f);
					}
					
					fPages[0] = CCLayer.node();
					//fPages[0].addChild(friendMenu);
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
					removeChild(pPages[curPage], false);
					addChild(pPages[curPage+1]);
					curPage += 1;
				}
			}
		} else if (item.getTag() == 5) {		//	Click buy icon
			for (int i = 0;i < wBtnNum;i++) {
				if (curSelectBtn == btn_weapon[i]) {					
					Log.d("btn", "money : " + fighter.getMoney());
					
					btn_weapon[i].setNormalImage("weapon_" + weapons[i].getName() + "_unselected.png");
					btn_weapon[i].setTag(1);
					fighter.addOwnWeapons(i);
					fighter.setMoney(fighter.getMoney() - weapons[i].getPrice());
					
					_label4.setString("$" + fighter.getMoney());
					
					Log.d("btn", "money : " + fighter.getMoney());
					break;
				}
			}
			
			for (int i = 0;i < pBtnNum;i++) {
				if (curSelectBtn == btn_prop[i]) {
					Log.d("btn", "money : " + fighter.getMoney());
					
					btn_prop[i].setNormalImage("prop_" + props[i].getName() + "_unselected.png");
					btn_prop[i].setTag(1);
					fighter.addOwnProps(i);
					fighter.setMoney(fighter.getMoney() - props[i].getPrice());
					
					_label4.setString("$" + fighter.getMoney());
					
					Log.d("btn", "money : " + fighter.getMoney());
					break;
				}
			}
			
			buyMenu.setVisible(false);
		} else if (item.getTag() == 6) {		//	Click fight icon	
			//	轉換到選關畫面
			Log.d("btn", "切換畫面");
			((TomatoshootGame)CCDirector.sharedDirector().getActivity()).saveFighter();
			CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(1, ChoiceGameLayer.scene(), ccColor3B.ccWHITE));
		}
	}
	
	public void clickWeaponBtn(Object sender) {
		CCMenuItemImage item = (CCMenuItemImage) sender;
		
		curSelectBtn = item;
		
		if (item.getTag() == 0) {	//	尚未擁有	
			for (int i = 0;i < wBtnNum;i++) {
				if (item == btn_weapon[i]) {
					_label1.setString(weapons[i].getName());
					_label2.setString(weapons[i].getFunc());
					_label3.setString("" + weapons[i].getPrice());
					
					if (fighter.getMoney() >= weapons[i].getPrice()) {
						buyMenu.setVisible(true);
					} else buyMenu.setVisible(false);
					
					break;
				}
			}			
		} else if (item.getTag() == 1) {	//	已擁有
			buyMenu.setVisible(false);
			
			if (wSelectNum < maxWeaponSelectNum) {
				for (int i = 0;i < wBtnNum;i++) {
					if (item == btn_weapon[i]) {
						_label1.setString(weapons[i].getName());
						_label2.setString(weapons[i].getFunc());
						_label3.setString("" + weapons[i].getPrice());
						item.selected();
						item.setTag(2);
						fighter.setWeaponStatus(i, 2);
						wSelectNum += 1;
						break;
					}
				}			
			}
		} else if (item.getTag() == 2) {	//	已選取
			buyMenu.setVisible(false);
			
			for (int i = 0;i < wBtnNum;i++) {
				if (item == btn_weapon[i]) {
					_label1.setString("Name");
					_label2.setString("Func");
					_label3.setString("Price");
					item.unselected();
					item.setTag(1);
					fighter.setWeaponStatus(i, 1);
					wSelectNum -= 1;
					break;
				}
			}
		}
	}
	
	public void clickPropBtn(Object sender) {
		CCMenuItemImage item = (CCMenuItemImage) sender;
		
		curSelectBtn = item;
		
		if (item.getTag() == 0) {	//	尚未擁有	
			for (int i = 0;i < pBtnNum;i++) {
				if (item == btn_prop[i]) {
					_label1.setString(props[i].getName());
					_label2.setString(props[i].getFunc());
					_label3.setString("" + props[i].getPrice());
										
					if (fighter.getMoney() >= props[i].getPrice()) {
						buyMenu.setVisible(true);
					} else buyMenu.setVisible(false);
					
					break;
				}
			}			
		} else if (item.getTag() == 1) {	//	已擁有
			buyMenu.setVisible(false);
			
			if (pSelectNum < maxPropSelectNum) {
				for (int i = 0;i < pBtnNum;i++) {
					if (item == btn_prop[i]) {
						_label1.setString(props[i].getName());
						_label2.setString(props[i].getFunc());
						_label3.setString("" + props[i].getPrice());
						item.selected();
						item.setTag(2);
						fighter.setPropStatus(i, 2);
						pSelectNum += 1;
						break;
					}
				}
			}
		} else if (item.getTag() == 2) {	//	已選取
			buyMenu.setVisible(false);
			
			for (int i = 0;i < pBtnNum;i++) {
				if (item == btn_prop[i]) {
					_label1.setString("Name");
					_label2.setString("Func");
					_label3.setString("Price");
					item.unselected();
					item.setTag(1);
					fighter.setPropStatus(i, 1);
					pSelectNum -= 1;
					break;
				}
			}
		}
	}
	
	public void clickFacebookBtn(Object sender) {
		CCMenuItemSprite item = (CCMenuItemSprite) sender;
		
		if (item.getTag() == 0) {						
			if (fSelectNum < maxFriendSelectNum) {
				for (int i = 0;i < fBtnNum;i++) {
					if (item == btn_friend[i]) {
						Log.d("fb", "" + i);
						_label1.setString(FriendList.get(i));
						//_label2.setString(weapons[i].getFunc());
						//_label3.setString("" + weapons[i].getPrice());
						//item.selected();
						item.setScale(2.0f);
						item.setTag(1);						
						fSelectNum += 1;
						break;
					}
				}			
			}
		} else if (item.getTag() == 1) {						
			for (int i = 0;i < fBtnNum;i++) {
				if (item == btn_friend[i]) {
					_label1.setString("Name");
					_label2.setString("Func");
					_label3.setString("Price");
					//item.unselected();
					item.setScale(1.5f);
					item.setTag(0);
					fSelectNum -= 1;
					break;
				}
			}
		}
	}
	
	//	weapon index 對照表, no used
	public String indexOfWeapon2String(int index) {		
		if (index == 0) return "corn";
		else if (index == 1) return "grape";
		else if (index == 2) return "pineapple";
		else if (index == 3) return "watermelon";
		else return "what";
	}
	
	//	prop index 對照表, no used
	public String indexOfProp2String(int index) {
		if (index == 0) return "aim";
		else if (index == 1) return "locked";
		else return "what";
	}	
	@Override
	public void onExit() {
		//SoundEngine.sharedEngine().realesAllSounds();
		//SoundEngine.sharedEngine().setSoundVolume(0.5f);
		//SoundEngine.sharedEngine().playSound(context, R.raw.mr_reno, true);
		
		super.onExit();
		
	}
}
