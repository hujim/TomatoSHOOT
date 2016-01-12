package org.mirlab.tomatoshoot;

import java.util.LinkedList;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;

import android.content.Context;

public class SubBtnLayer extends CCLayer {
	protected LinkedList<CCMenuItemImage> _subBtns;	
	private int maxSelectedItemNum = 2;
	private int curSelectedItemNum = 0;	
	private int haveSelectedItemNum = 0;
	private int [] selectedItemIndex = {0, 0, 0, 0, 0, 0, 0, 0, 0};
	
	protected SubBtnLayer(String value_1_1, String value_1_2) {		
		this.setIsTouchEnabled(true);
				
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);		
		//float backgroundScale = winSize.width/(background.getTextureRect().size.width);
		
		CCMenuItemImage btn = CCMenuItemImage.item("button_selected.png", "button_selected.png");		
		btn.setScale(backgroundScale);		
		
		CGSize btnSize = btn.getContentSize();
		btnSize.width = btn.getContentSize().width * backgroundScale;
		btnSize.height = btn.getContentSize().height * backgroundScale;
		
		// 子選單的按鈕
		CCMenuItemImage subBtn_1_1 = CCMenuItemImage.item(value_1_1, value_1_2, this, "clickSubBtn");		
		subBtn_1_1.setScale(backgroundScale);							
		
		_subBtns = new LinkedList<CCMenuItemImage>();
		_subBtns.add(subBtn_1_1);
		
		CCMenu subMenuRow1 = CCMenu.menu(subBtn_1_1);		
		subMenuRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow1.setPosition(CGPoint.ccp(btnSize.width * 1.6f - 20, winSize.height - btnSize.height * 2.5f));
		subMenuRow1.alignItemsHorizontally(20);
		addChild(subMenuRow1);		
	}	

	protected SubBtnLayer(String value_1_1, String value_1_2, String value_2_1, String value_2_2) {		
		this.setIsTouchEnabled(true);
				
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);		
		
		CCMenuItemImage btn = CCMenuItemImage.item("button_selected.png", "button_selected.png");		
		btn.setScale(backgroundScale);		
		
		CGSize btnSize = btn.getContentSize();
		btnSize.width = btn.getContentSize().width * backgroundScale;
		btnSize.height = btn.getContentSize().height * backgroundScale;
		
		// 子選單的按鈕
		CCMenuItemImage subBtn_1_1 = CCMenuItemImage.item(value_1_1, value_1_2, this, "clickSubBtn");		
		subBtn_1_1.setScale(backgroundScale);				
		
		CCMenuItemImage subBtn_1_2 = CCMenuItemImage.item(value_2_1, value_2_2, this, "clickSubBtn");		
		subBtn_1_2.setScale(backgroundScale);		
		
		_subBtns = new LinkedList<CCMenuItemImage>();
		_subBtns.add(subBtn_1_1);
		_subBtns.add(subBtn_1_2);
		
		CCMenu subMenuRow1 = CCMenu.menu(subBtn_1_1, subBtn_1_2);		
		subMenuRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow1.setPosition(CGPoint.ccp(btnSize.width * 2.1f - 10, winSize.height - btnSize.height * 2.5f));
		subMenuRow1.alignItemsHorizontally(20);
		addChild(subMenuRow1);		
	}

	protected SubBtnLayer(String value_1_1, String value_1_2, String value_2_1, String value_2_2, String value_3_1, String value_3_2) {		
		this.setIsTouchEnabled(true);
				
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);		
						
		CCMenuItemImage btn = CCMenuItemImage.item("button_selected.png", "button_selected.png");		
		btn.setScale(backgroundScale);		
		
		CGSize btnSize = btn.getContentSize();
		btnSize.width = btn.getContentSize().width * backgroundScale;
		btnSize.height = btn.getContentSize().height * backgroundScale;
		
		// 子選單的按鈕
		CCMenuItemImage subBtn_1_1 = CCMenuItemImage.item(value_1_1, value_1_2, this, "clickSubBtn");		
		subBtn_1_1.setScale(backgroundScale);				
		
		CCMenuItemImage subBtn_1_2 = CCMenuItemImage.item(value_2_1, value_2_2, this, "clickSubBtn");		
		subBtn_1_2.setScale(backgroundScale);		
				
		CCMenuItemImage subBtn_1_3 = CCMenuItemImage.item(value_3_1, value_3_2, this, "clickSubBtn");		
		subBtn_1_3.setScale(backgroundScale);		
		
		_subBtns = new LinkedList<CCMenuItemImage>();
		_subBtns.add(subBtn_1_1);
		_subBtns.add(subBtn_1_2);
		_subBtns.add(subBtn_1_3);

		CCMenu subMenuRow1 = CCMenu.menu(subBtn_1_1, subBtn_1_2, subBtn_1_3);		
		subMenuRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow1.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 2.5f));
		subMenuRow1.alignItemsHorizontally(20);
		addChild(subMenuRow1);		
	}

	protected SubBtnLayer(String value_1_1, String value_1_2, String value_2_1, String value_2_2, String value_3_1, String value_3_2, String value_4_1, String value_4_2) {
		this.setIsTouchEnabled(true);
				
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);		
		//float backgroundScale = winSize.width/(background.getTextureRect().size.width);
		
		CCMenuItemImage btn = CCMenuItemImage.item("button_selected.png", "button_selected.png");		
		btn.setScale(backgroundScale);		
		
		CGSize btnSize = btn.getContentSize();
		btnSize.width = btn.getContentSize().width * backgroundScale;
		btnSize.height = btn.getContentSize().height * backgroundScale;
		
		// 子選單的按鈕
		CCMenuItemImage subBtn_1_1 = CCMenuItemImage.item(value_1_1, value_1_2, this, "clickSubBtn");		
		subBtn_1_1.setScale(backgroundScale);	
		
		CCMenuItemImage subBtn_1_2 = CCMenuItemImage.item(value_2_1, value_2_2, this, "clickSubBtn");		
		subBtn_1_2.setScale(backgroundScale);
				
		CCMenuItemImage subBtn_1_3 = CCMenuItemImage.item(value_3_1, value_3_2, this, "clickSubBtn");		
		subBtn_1_3.setScale(backgroundScale);
		
		CCMenuItemImage subBtn_2_1 = CCMenuItemImage.item(value_4_1, value_4_2, this, "clickSubBtn");		
		subBtn_2_1.setScale(backgroundScale);
		
		_subBtns = new LinkedList<CCMenuItemImage>();
		_subBtns.add(subBtn_1_1);
		_subBtns.add(subBtn_1_2);
		_subBtns.add(subBtn_1_3);
		_subBtns.add(subBtn_2_1);
		
		CCMenu subMenuRow1 = CCMenu.menu(subBtn_1_1, subBtn_1_2, subBtn_1_3);		
		subMenuRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow1.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 2.5f));
		subMenuRow1.alignItemsHorizontally(20);
		addChild(subMenuRow1);
		
		CCMenu subMenuRow2 = CCMenu.menu(subBtn_2_1);		
		subMenuRow2.setColor(ccColor3B.ccc3(255, 255, 255));		
		
		subMenuRow2.setPosition(CGPoint.ccp(btnSize.width * 1.6f - 20, winSize.height - btnSize.height * 3.57f));
		subMenuRow2.alignItemsHorizontally(20);		
		addChild(subMenuRow2);					
	}

	protected SubBtnLayer(String value_1_1, String value_1_2, String value_2_1, String value_2_2, String value_3_1, String value_3_2, String value_4_1, String value_4_2, //
	String value_5_1, String value_5_2) {
		this.setIsTouchEnabled(true);
				
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);		
		
		CCMenuItemImage btn = CCMenuItemImage.item("button_selected.png", "button_selected.png");		
		btn.setScale(backgroundScale);		
		
		CGSize btnSize = btn.getContentSize();
		btnSize.width = btn.getContentSize().width * backgroundScale;
		btnSize.height = btn.getContentSize().height * backgroundScale;
		
		// 子選單的按鈕
		CCMenuItemImage subBtn_1_1 = CCMenuItemImage.item(value_1_1, value_1_2, this, "clickSubBtn");		
		subBtn_1_1.setScale(backgroundScale);		
		
		CCMenuItemImage subBtn_1_2 = CCMenuItemImage.item(value_2_1, value_2_2, this, "clickSubBtn");		
		subBtn_1_2.setScale(backgroundScale);
				
		CCMenuItemImage subBtn_1_3 = CCMenuItemImage.item(value_3_1, value_3_2, this, "clickSubBtn");		
		subBtn_1_3.setScale(backgroundScale);
		
		CCMenuItemImage subBtn_2_1 = CCMenuItemImage.item(value_4_1, value_4_2, this, "clickSubBtn");		
		subBtn_2_1.setScale(backgroundScale);
		
		CCMenuItemImage subBtn_2_2 = CCMenuItemImage.item(value_5_1, value_5_2, this, "clickSubBtn");
		subBtn_2_2.setScale(backgroundScale);
		
		_subBtns = new LinkedList<CCMenuItemImage>();
		_subBtns.add(subBtn_1_1);
		_subBtns.add(subBtn_1_2);
		_subBtns.add(subBtn_1_3);
		_subBtns.add(subBtn_2_1);
		_subBtns.add(subBtn_2_2);
		
		CCMenu subMenuRow1 = CCMenu.menu(subBtn_1_1, subBtn_1_2, subBtn_1_3);		
		subMenuRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow1.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 2.5f));
		subMenuRow1.alignItemsHorizontally(20);
		addChild(subMenuRow1);
		
		CCMenu subMenuRow2 = CCMenu.menu(subBtn_2_1, subBtn_2_2);		
		subMenuRow2.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow2.setPosition(CGPoint.ccp(btnSize.width * 2.1f - 10, winSize.height - btnSize.height * 3.57f));
		subMenuRow2.alignItemsHorizontally(20);		
		addChild(subMenuRow2);						
	}

	protected SubBtnLayer(String value_1_1, String value_1_2, String value_2_1, String value_2_2, String value_3_1, String value_3_2, String value_4_1, String value_4_2, //
	String value_5_1, String value_5_2, String value_6_1, String value_6_2) {		
		this.setIsTouchEnabled(true);
				
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);		
		
		CCMenuItemImage btn = CCMenuItemImage.item("button_selected.png", "button_selected.png");		
		btn.setScale(backgroundScale);		
		
		CGSize btnSize = btn.getContentSize();
		btnSize.width = btn.getContentSize().width * backgroundScale;
		btnSize.height = btn.getContentSize().height * backgroundScale;
		
		// 子選單的按鈕
		CCMenuItemImage subBtn_1_1 = CCMenuItemImage.item(value_1_1, value_1_2, this, "clickSubBtn");		
		subBtn_1_1.setScale(backgroundScale);	
		
		CCMenuItemImage subBtn_1_2 = CCMenuItemImage.item(value_2_1, value_2_2, this, "clickSubBtn");		
		subBtn_1_2.setScale(backgroundScale);
				
		CCMenuItemImage subBtn_1_3 = CCMenuItemImage.item(value_3_1, value_3_2, this, "clickSubBtn");		
		subBtn_1_3.setScale(backgroundScale);
		
		CCMenuItemImage subBtn_2_1 = CCMenuItemImage.item(value_4_1, value_4_2, this, "clickSubBtn");		
		subBtn_2_1.setScale(backgroundScale);
		
		CCMenuItemImage subBtn_2_2 = CCMenuItemImage.item(value_5_1, value_5_2, this, "clickSubBtn");
		subBtn_2_2.setScale(backgroundScale);

		CCMenuItemImage subBtn_2_3 = CCMenuItemImage.item(value_6_1, value_6_2, this, "clickSubBtn");		
		subBtn_2_3.setScale(backgroundScale);
		
		_subBtns = new LinkedList<CCMenuItemImage>();
		_subBtns.add(subBtn_1_1);
		_subBtns.add(subBtn_1_2);
		_subBtns.add(subBtn_1_3);
		_subBtns.add(subBtn_2_1);
		_subBtns.add(subBtn_2_2);
		_subBtns.add(subBtn_2_3);
		
		CCMenu subMenuRow1 = CCMenu.menu(subBtn_1_1, subBtn_1_2, subBtn_1_3);		
		subMenuRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow1.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 2.5f));
		subMenuRow1.alignItemsHorizontally(20);
		addChild(subMenuRow1);
		
		CCMenu subMenuRow2 = CCMenu.menu(subBtn_2_1, subBtn_2_2, subBtn_2_3);		
		subMenuRow2.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow2.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 3.57f));
		subMenuRow2.alignItemsHorizontally(20);		
		addChild(subMenuRow2);
	}
	
	protected SubBtnLayer(String value_1_1, String value_1_2, String value_2_1, String value_2_2, String value_3_1, String value_3_2, String value_4_1, String value_4_2, //
	String value_5_1, String value_5_2, String value_6_1, String value_6_2, String value_7_1, String value_7_2) {		
		this.setIsTouchEnabled(true);
				
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);		
		
		CCMenuItemImage btn = CCMenuItemImage.item("button_selected.png", "button_selected.png");		
		btn.setScale(backgroundScale);		
		
		CGSize btnSize = btn.getContentSize();
		btnSize.width = btn.getContentSize().width * backgroundScale;
		btnSize.height = btn.getContentSize().height * backgroundScale;
		
		// 子選單的按鈕
		CCMenuItemImage subBtn_1_1 = CCMenuItemImage.item(value_1_1, value_1_2, this, "clickSubBtn");		
		subBtn_1_1.setScale(backgroundScale);		
		
		CCMenuItemImage subBtn_1_2 = CCMenuItemImage.item(value_2_1, value_2_2, this, "clickSubBtn");		
		subBtn_1_2.setScale(backgroundScale);
				
		CCMenuItemImage subBtn_1_3 = CCMenuItemImage.item(value_3_1, value_3_2, this, "clickSubBtn");		
		subBtn_1_3.setScale(backgroundScale);
		
		CCMenuItemImage subBtn_2_1 = CCMenuItemImage.item(value_4_1, value_4_2, this, "clickSubBtn");		
		subBtn_2_1.setScale(backgroundScale);
		
		CCMenuItemImage subBtn_2_2 = CCMenuItemImage.item(value_5_1, value_5_2, this, "clickSubBtn");
		subBtn_2_2.setScale(backgroundScale);

		CCMenuItemImage subBtn_2_3 = CCMenuItemImage.item(value_6_1, value_6_2, this, "clickSubBtn");		
		subBtn_2_3.setScale(backgroundScale);				
		
		CCMenuItemImage subBtn_3_1 = CCMenuItemImage.item(value_7_1, value_7_2, this, "clickSubBtn");		
		subBtn_3_1.setScale(backgroundScale);
		
		_subBtns = new LinkedList<CCMenuItemImage>();
		_subBtns.add(subBtn_1_1);
		_subBtns.add(subBtn_1_2);
		_subBtns.add(subBtn_1_3);
		_subBtns.add(subBtn_2_1);
		_subBtns.add(subBtn_2_2);
		_subBtns.add(subBtn_2_3);
		_subBtns.add(subBtn_3_1);
		
		CCMenu subMenuRow1 = CCMenu.menu(subBtn_1_1, subBtn_1_2, subBtn_1_3);		
		subMenuRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow1.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 2.5f));
		subMenuRow1.alignItemsHorizontally(20);
		addChild(subMenuRow1);
		
		CCMenu subMenuRow2 = CCMenu.menu(subBtn_2_1, subBtn_2_2, subBtn_2_3);		
		subMenuRow2.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow2.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 3.57f));
		subMenuRow2.alignItemsHorizontally(20);		
		addChild(subMenuRow2);
		
		CCMenu subMenuRow3 = CCMenu.menu(subBtn_3_1);		
		subMenuRow3.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow3.setPosition(CGPoint.ccp(btnSize.width * 1.6f - 20, winSize.height - btnSize.height * 4.64f));
		subMenuRow3.alignItemsHorizontally(20);		
		addChild(subMenuRow3);				
	}

	protected SubBtnLayer(String value_1_1, String value_1_2, String value_2_1, String value_2_2, String value_3_1, String value_3_2, String value_4_1, String value_4_2, //
	String value_5_1, String value_5_2, String value_6_1, String value_6_2, String value_7_1, String value_7_2, String value_8_1, String value_8_2) {		
		this.setIsTouchEnabled(true);
				
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);		
		
		CCMenuItemImage btn = CCMenuItemImage.item("button_selected.png", "button_selected.png");		
		btn.setScale(backgroundScale);		
		
		CGSize btnSize = btn.getContentSize();
		btnSize.width = btn.getContentSize().width * backgroundScale;
		btnSize.height = btn.getContentSize().height * backgroundScale;
		
		// 子選單的按鈕
		CCMenuItemImage subBtn_1_1 = CCMenuItemImage.item(value_1_1, value_1_2, this, "clickSubBtn");		
		subBtn_1_1.setScale(backgroundScale);	
		
		CCMenuItemImage subBtn_1_2 = CCMenuItemImage.item(value_2_1, value_2_2, this, "clickSubBtn");		
		subBtn_1_2.setScale(backgroundScale);
				
		CCMenuItemImage subBtn_1_3 = CCMenuItemImage.item(value_3_1, value_3_2, this, "clickSubBtn");		
		subBtn_1_3.setScale(backgroundScale);
		
		CCMenuItemImage subBtn_2_1 = CCMenuItemImage.item(value_4_1, value_4_2, this, "clickSubBtn");		
		subBtn_2_1.setScale(backgroundScale);
		
		CCMenuItemImage subBtn_2_2 = CCMenuItemImage.item(value_5_1, value_5_2, this, "clickSubBtn");
		subBtn_2_2.setScale(backgroundScale);

		CCMenuItemImage subBtn_2_3 = CCMenuItemImage.item(value_6_1, value_6_2, this, "clickSubBtn");		
		subBtn_2_3.setScale(backgroundScale);	
		
		CCMenuItemImage subBtn_3_1 = CCMenuItemImage.item(value_7_1, value_7_2, this, "clickSubBtn");		
		subBtn_3_1.setScale(backgroundScale);	
		
		CCMenuItemImage subBtn_3_2 = CCMenuItemImage.item(value_8_1, value_8_2, this, "clickSubBtn");		
		subBtn_3_2.setScale(backgroundScale);
		
		_subBtns = new LinkedList<CCMenuItemImage>();
		_subBtns.add(subBtn_1_1);
		_subBtns.add(subBtn_1_2);
		_subBtns.add(subBtn_1_3);
		_subBtns.add(subBtn_2_1);
		_subBtns.add(subBtn_2_2);
		_subBtns.add(subBtn_2_3);
		_subBtns.add(subBtn_3_1);
		_subBtns.add(subBtn_3_2);
		
		CCMenu subMenuRow1 = CCMenu.menu(subBtn_1_1, subBtn_1_2, subBtn_1_3);		
		subMenuRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow1.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 2.5f));
		subMenuRow1.alignItemsHorizontally(20);
		addChild(subMenuRow1);
		
		CCMenu subMenuRow2 = CCMenu.menu(subBtn_2_1, subBtn_2_2, subBtn_2_3);		
		subMenuRow2.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow2.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 3.57f));
		subMenuRow2.alignItemsHorizontally(20);		
		addChild(subMenuRow2);
		
		CCMenu subMenuRow3 = CCMenu.menu(subBtn_3_1, subBtn_3_2);		
		subMenuRow3.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow3.setPosition(CGPoint.ccp(btnSize.width * 2.1f - 10, winSize.height - btnSize.height * 4.64f));
		subMenuRow3.alignItemsHorizontally(20);		
		addChild(subMenuRow3);				
	}
		
	protected SubBtnLayer(String value_1_1, String value_1_2, String value_2_1, String value_2_2, String value_3_1, String value_3_2, String value_4_1, String value_4_2, //
	String value_5_1, String value_5_2, String value_6_1, String value_6_2, String value_7_1, String value_7_2, String value_8_1, String value_8_2, String value_9_1, String value_9_2) // 	
	{		
		this.setIsTouchEnabled(true);
				
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);		
		
		CCMenuItemImage btn = CCMenuItemImage.item("button_selected.png", "button_selected.png");		
		btn.setScale(backgroundScale);		
		
		CGSize btnSize = btn.getContentSize();
		btnSize.width = btn.getContentSize().width * backgroundScale;
		btnSize.height = btn.getContentSize().height * backgroundScale;
		
		// 子選單的按鈕
		CCMenuItemImage subBtn_1_1 = CCMenuItemImage.item(value_1_1, value_1_2, this, "clickSubBtn");		
		subBtn_1_1.setScale(backgroundScale);				
		
		CCMenuItemImage subBtn_1_2 = CCMenuItemImage.item(value_2_1, value_2_2, this, "clickSubBtn");		
		subBtn_1_2.setScale(backgroundScale);		
				
		CCMenuItemImage subBtn_1_3 = CCMenuItemImage.item(value_3_1, value_3_2, this, "clickSubBtn");		
		subBtn_1_3.setScale(backgroundScale);		
		
		CCMenuItemImage subBtn_2_1 = CCMenuItemImage.item(value_4_1, value_4_2, this, "clickSubBtn");		
		subBtn_2_1.setScale(backgroundScale);		
		
		CCMenuItemImage subBtn_2_2 = CCMenuItemImage.item(value_5_1, value_5_2, this, "clickSubBtn");
		subBtn_2_2.setScale(backgroundScale);		

		CCMenuItemImage subBtn_2_3 = CCMenuItemImage.item(value_6_1, value_6_2, this, "clickSubBtn");		
		subBtn_2_3.setScale(backgroundScale);		
		
		CCMenuItemImage subBtn_3_1 = CCMenuItemImage.item(value_7_1, value_7_2, this, "clickSubBtn");		
		subBtn_3_1.setScale(backgroundScale);		
		
		CCMenuItemImage subBtn_3_2 = CCMenuItemImage.item(value_8_1, value_8_2, this, "clickSubBtn");		
		subBtn_3_2.setScale(backgroundScale);		
		
		CCMenuItemImage subBtn_3_3 = CCMenuItemImage.item(value_9_1, value_9_2, this, "clickSubBtn");		
		subBtn_3_3.setScale(backgroundScale);		
		
		_subBtns = new LinkedList<CCMenuItemImage>();
		_subBtns.add(subBtn_1_1);
		_subBtns.add(subBtn_1_2);
		_subBtns.add(subBtn_1_3);
		_subBtns.add(subBtn_2_1);
		_subBtns.add(subBtn_2_2);
		_subBtns.add(subBtn_2_3);
		_subBtns.add(subBtn_3_1);
		_subBtns.add(subBtn_3_2);
		_subBtns.add(subBtn_3_3);
		
		CCMenu subMenuRow1 = CCMenu.menu(subBtn_1_1, subBtn_1_2, subBtn_1_3);		
		subMenuRow1.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow1.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 2.5f));
		subMenuRow1.alignItemsHorizontally(20);
		addChild(subMenuRow1);
		
		CCMenu subMenuRow2 = CCMenu.menu(subBtn_2_1, subBtn_2_2, subBtn_2_3);		
		subMenuRow2.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow2.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 3.57f));
		subMenuRow2.alignItemsHorizontally(20);		
		addChild(subMenuRow2);
		
		CCMenu subMenuRow3 = CCMenu.menu(subBtn_3_1, subBtn_3_2, subBtn_3_3);		
		subMenuRow3.setColor(ccColor3B.ccc3(255, 255, 255));
		subMenuRow3.setPosition(CGPoint.ccp(btnSize.width * 2.6f, winSize.height - btnSize.height * 4.64f));
		subMenuRow3.alignItemsHorizontally(20);		
		addChild(subMenuRow3);				
	}
	
	public void clickSubBtn(Object sender) {
		CCMenuItemImage item = (CCMenuItemImage) sender;		
		
		Context context = CCDirector.sharedDirector().getActivity();
		
		if (item.getTag() == 0) {
			SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);
			
			for (CCMenuItemImage subBtn : _subBtns) {
				if (subBtn.getTag() == 1) {
					subBtn.setTag(0);
					subBtn.unselected();
					break;
				}
			}
			
			item.setTag(1);			
			item.selected();
		} else if (item.getTag() == 1) {
			SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);
			item.selected();
		} else if (item.getTag() == 2) {
			SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);
			
			if (this.haveSelectedItemNum < this.maxSelectedItemNum) {
				item.setTag(3);
				item.selected();
				setCurSelectedItemNum(getSelectedItemNum() + 1);		
				selectedItemIndex[_subBtns.indexOf(item)] = 1;
			}
		} else if (item.getTag() == 3) {
			SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);
			
			item.setTag(2);
			item.unselected();
			setCurSelectedItemNum(getSelectedItemNum() - 1);
			selectedItemIndex[_subBtns.indexOf(item)] = 0;
		}
	} 
	
	public void setAllBtnTag(int tag) {
		for (CCMenuItemImage subBtn : _subBtns) {
			subBtn.setTag(tag);			
		}		
	}
	
	public void setAllBtnUnselected() {		
		for (CCMenuItemImage subBtn : _subBtns) {
			subBtn.unselected();		
		}
	}
	
	public void setMaxSelectedItemNum(int MaxSelectedItemNum) {
		this.maxSelectedItemNum = MaxSelectedItemNum;
	}
	
	public int getSelectedItemNum() {		
		return this.curSelectedItemNum;
	}
	
	public void setCurSelectedItemNum(int CurSelectedItemNum) {
		this.curSelectedItemNum = CurSelectedItemNum;		
	}
	
	public void setHaveSelectedItemNum(int haveSelectedItemNum) {		
		this.haveSelectedItemNum = haveSelectedItemNum; 
	}
	
	public int[] getSelectedIndex() {		
		return selectedItemIndex;
	}
}