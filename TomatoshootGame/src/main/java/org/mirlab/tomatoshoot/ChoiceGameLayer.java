package org.mirlab.tomatoshoot;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
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
		
	private CCMenuItemSprite [] btn_game = new CCMenuItemSprite[maxGameNum];
	
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
		scene.setTag(1);		
		scene.addChild(layer);		
				
		return scene;
	}
	
	protected ChoiceGameLayer(ccColor4B color) {
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
		
		fighter = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).tomatoFighter;
		Log.d("btn", "user : " + fighter.getID());
		Log.d("btn", "money : "+ fighter.getMoney());
		
		// 背景圖
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("ChoiceGameLayer.plist");
		CCSprite background = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("choiceGame.png"));
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		//float backgroundScale = winSize.height/(background.getTextureRect().size.height);
		float backgroundScale = winSize.width/(background.getTextureRect().size.width);		
		
		background.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		background.setScale(backgroundScale);
		addChild(background);
		
		// set label
		_label1 = CCLabel.makeLabel(" ", "Arial", 25);
		_label1.setColor(ccColor3B.ccBLACK);
		_label1.setPosition(CGPoint.ccp(winSize.width * 0.30f, winSize.height * 0.14f));		
		addChild(_label1);
		
		// 設定關卡
		games[0] = new item("game1", "番茄訓練場");
		games[1] = new item("game2", "以大欺小");
		games[2] = new item("game3", "生化實驗室");
		games[3] = new item("game4", "蟲軍突擊");
		games[4] = new item("game5", "愛的力量真偉大");
		games[5] = new item("game6", "OH MY GOD");
		games[6] = new item("game7", "一二三稻草人");
		games[7] = new item("game8", "Wake up Tomato");
		games[8] = new item("game9", "蓄勢待發");
		games[9] = new item("game10", "事不過三");
		games[10] = new item("game11", "歡樂年華");
		games[11] = new item("game12", "即刻救援");
		games[12] = new item("game13", "決心");
		games[13] = new item("game14", "內心世界");
		games[14] = new item("game15", "背水一戰");
		
		// Ready按鈕				
		CCMenuItemSprite readyBtn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_ready.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("button_ready.png")), this, "clickMenuBtn");
		readyBtn.setScale(backgroundScale);
		readyBtn.setTag(0);
		
		CCAction action2 = (CCRepeatForever.action(
				CCSequence.actions(
						CCRotateBy.action(0.1f, 20),
						CCRotateBy.action(0.1f, -40),
						CCRotateBy.action(0.1f, 20),
						CCDelayTime.action(2)
				)));
		action2.setTag(101);
				
		readyBtn.runAction(action2);
		
		CCMenu readyMenu = CCMenu.menu(readyBtn);
		readyMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		readyMenu.setPosition(CGPoint.ccp(winSize.width - readyBtn.getBoundingBox().size.width * 0.75f, readyBtn.getBoundingBox().size.height * 1.2f));
		addChild(readyMenu);						
		
		// 關卡按鈕		
		int [] gameStatus = fighter.getGameStatus();
		for (int i = 0;i < maxGameNum;i++) {
			if (gameStatus[i] == 0) {	//	尚未開放
				btn_game[i] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("game_lock.png")),
						CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("game_lock.png")), this, "clickGameBtn");
				CCSprite sprite0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("game_unlock.png"));
				sprite0.setPosition(CGPoint.ccp(btn_game[i].getContentSize().width / 2, btn_game[i].getContentSize().height / 2));
				btn_game[i].addChild(sprite0);
				CCSprite sprite1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("game" + (i+1) + ".png"));
				sprite1.setPosition(CGPoint.ccp(btn_game[i].getContentSize().width / 2, btn_game[i].getContentSize().height / 2));
				btn_game[i].addChild(sprite1);
				
				btn_game[i].setTag(0);	
			} else {	//	已開放
				btn_game[i] = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("game_unlock.png")),
						CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("game_clear.png")), this, "clickGameBtn");
				CCSprite sprite0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("game" + (i+1) + ".png"));
				sprite0.setPosition(CGPoint.ccp(btn_game[i].getContentSize().width / 2, btn_game[i].getContentSize().height / 2));
				btn_game[i].addChild(sprite0);
				
				btn_game[i].setTag(1);	
			}
			btn_game[i].setScale(backgroundScale);
		}
		
		CCMenu gRow0 = CCMenu.menu(btn_game[0], btn_game[1], btn_game[2], btn_game[3], btn_game[4]);
		gRow0.setColor(ccColor3B.ccc3(255, 255, 255));
		gRow0.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f + btn_game[0].getBoundingBox().size.height * 1.2f));
		gRow0.alignItemsHorizontally(btn_game[0].getBoundingBox().size.width * 0.125f);		
		addChild(gRow0);
		
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
		
		gBtnNum = 15;
		
	}
	public void clickMenuBtn(Object sender) {
		CCMenuItemSprite item = (CCMenuItemSprite) sender;
		
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = true;
		fighter.setCurGameLevel(curSelectGame);
		if (item.getTag() == 0)	{	//	click ready button
			//	進到遊戲畫面	
			//item.setIsEnabled(false);
			if (curSelectGame == 1) {			//	跳到第1關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV01.scene(), tOrientation.kOrientationRightOver));
				
			} else if (curSelectGame == 2) {	//	跳到第2關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV02.scene(), tOrientation.kOrientationRightOver));
				
			} else if (curSelectGame == 3) {	//	跳到第3關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV03.scene(), tOrientation.kOrientationRightOver));
				
			} else if (curSelectGame == 4) {	//	跳到第4關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV04.scene(), tOrientation.kOrientationRightOver));
				
			} else if (curSelectGame == 5) {	//	跳到第5關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV05.scene(), tOrientation.kOrientationRightOver));
				
			} else if (curSelectGame == 6) {	//	跳到第6關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV06.scene(), tOrientation.kOrientationRightOver));
			} else if (curSelectGame == 7) {	//	跳到第7關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV07.scene(), tOrientation.kOrientationRightOver));
			} else if (curSelectGame == 8) {	//	跳到第8關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV08.scene(), tOrientation.kOrientationRightOver));
			} else if (curSelectGame == 9) {	//	跳到第9關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV09.scene(), tOrientation.kOrientationRightOver));
			} else if (curSelectGame == 10) {	//	跳到第10關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV10.scene(), tOrientation.kOrientationRightOver));
			} else if (curSelectGame == 11) {	//	跳到第11關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV11.scene(), tOrientation.kOrientationRightOver));
			} else if (curSelectGame == 12) {	//	跳到第12關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV12.scene(), tOrientation.kOrientationRightOver));
			} else if (curSelectGame == 13) {	//	跳到第13關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV13.scene(), tOrientation.kOrientationRightOver));
			} else if (curSelectGame == 14) {	//	跳到第14關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV14.scene(), tOrientation.kOrientationRightOver));
			} else if (curSelectGame == 15) {	//	跳到第15關
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameLayerLV15.scene(), tOrientation.kOrientationRightOver));
			} else if (((TomatoshootGame)CCDirector.sharedDirector().getActivity()).isQuickGame) {
				
			} else {
				//item.setIsEnabled(true);
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = false;
			}			
			
		}
		
	}
	
	public void clickGameBtn(Object sender) {
		CCMenuItemSprite item = (CCMenuItemSprite) sender;
		
		if (item.getTag() == 0) {			//	尚未開放
			for (int i = 0;i < gBtnNum;i++) {
				if (btn_game[i].getTag() == 2) {
					btn_game[i].unselected();
					btn_game[i].setTag(1);					
					curSelectGame = -1;
					break;
				}
			}
			
			_label1.setString("等級不足...");
		} else if (item.getTag() == 1) {	//	已開放，但未選擇			
			for (int i = 0;i < gBtnNum;i++) {
				if (btn_game[i].getTag() == 2) {
					btn_game[i].unselected();
					btn_game[i].setTag(1);
					curSelectGame = -1;
					break;
				}
			}	
			
			for (int i = 0;i < gBtnNum;i++) {
				if (btn_game[i] == item) {
					curSelectGame = i + 1;										
					btn_game[i].selected();
					btn_game[i].setTag(2);
					_label1.setString(games[i].getFunc());
					break;
				}
			}					
		} else if (item.getTag() == 2) {	//	已擁有，已選擇
			item.selected();			
		}
	}
}
