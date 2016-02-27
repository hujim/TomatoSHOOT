package org.mirlab.tomatoshoot;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCRepeat;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCTransitionScene.tOrientation;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class GameOverLayer extends CCColorLayer
{
	CCNode storyNode;
	
	protected CCLabel moneyLabel;
	protected CCLabel lvlLabel;
	protected CCLabel statusLabel;
	public static int moneyGot;
	public static int result;
	public static int timer;
	public static int kills;
	TomatoFighter tomatoFighter;
	static String textureFolderPath;
	int SCREEN_DENSITY;
	CGSize winSize;
	Context context = CCDirector.sharedDirector().getActivity();
	
	public static CCScene scene(int money, int resulted)
	{
		CCScene scene = CCScene.node();
		moneyGot = money;
		result = resulted;	
		
		GameOverLayer layer = new GameOverLayer(ccColor4B.ccc4(0, 0, 0, 50));		
		scene.addChild(layer);
		scene.setTag(5);
		
		return scene;
	}	
	public static CCScene scene(int scoreI, int killedI, int timeI, int resulted) {
		// TODO Auto-generated method stub
		CCScene scene = CCScene.node();
		moneyGot = scoreI;
		result = resulted;	
		timer = timeI;
		kills = killedI;
		
		GameOverLayer layer = new GameOverLayer(ccColor4B.ccc4(0, 0, 0, 50));		
		scene.addChild(layer);
		
		return scene;		
	}
	protected GameOverLayer(ccColor4B color)
	{
		super(color);
		
	}
	@Override
	public void onEnterTransitionDidFinish()
	{
		super.onEnterTransitionDidFinish();
		tomatoFighter = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).tomatoFighter;
		winSize = CCDirector.sharedDirector().displaySize();
		
		if((tomatoFighter.getCurGameLevel()%15)==0 && result == 1 )
		{
			SoundEngine.sharedEngine().realesAllSounds();
			//SoundEngine.sharedEngine().setSoundVolume(0.5f);
			SoundEngine.sharedEngine().playSound(context, R.raw.tsfhx, false);
			
			runEnding();					
		}
		else
		{
			initContents();			
		}
		
	}
	public void runEnding()
	{
		storyNode = CCColorLayer.node(ccColor4B.ccc4(0, 0, 0, 255));
		this.setIsTouchEnabled(true);
		this.setIsKeyEnabled(false); //later...	
		CCFiniteTimeAction subupAction = CCSpawn.actions(CCFadeIn.action(3), CCMoveBy.action(3, CGPoint.ccp(0,winSize.height*0.5f)));
		CCFiniteTimeAction subupActionOut = CCSpawn.actions(CCFadeOut.action(3), CCMoveBy.action(6, CGPoint.ccp(0,winSize.height*1.0f)));
		CCAction subUpSeq1 = CCSequence.actions(subupAction,subupActionOut);
		int offset = 3;
		if(tomatoFighter.getCurGameLoopNum() > 0)
		{
			offset = 0;
			CCLabel text1 = CCLabel.makeLabel("...再次的....", "Arial", 30);
			text1.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text1.getContentSize().height * 1.f));
			text1.setColor(ccColor3B.ccWHITE);
			storyNode.addChild(text1,3);
			text1.runAction(subUpSeq1);
		}
		CCLabel text2 = CCLabel.makeLabel("番茄戰士，戰勝了蟲蟲大軍", "Arial", 30);
		text2.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text2.getContentSize().height * 1.f));
		text2.setColor(ccColor3B.ccWHITE);
		storyNode.addChild(text2,3);
		CCAction subUpSeq2 = CCSequence.actions(CCDelayTime.action(3-offset),subupAction,subupActionOut);
		text2.runAction(subUpSeq2);
		CCLabel text25 = CCLabel.makeLabel("但，歷史總是不斷的重複自己", "Arial", 30);
		text25.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text25.getContentSize().height * 1.f));
		text25.setColor(ccColor3B.ccWHITE);
		storyNode.addChild(text25,3);
		CCAction subUpSeq25 = CCSequence.actions(CCDelayTime.action(6-offset),subupAction,subupActionOut);
		text25.runAction(subUpSeq25);
		
		CCLabel text3 = CCLabel.makeLabel("...有一天臭蟲帝國又會再次壯大...", "Arial", 30);
		text3.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text3.getContentSize().height * 1.f));
		text3.setColor(ccColor3B.ccWHITE);
		storyNode.addChild(text3,3);
		CCAction subUpSeq3 = CCSequence.actions(CCDelayTime.action(9-offset),subupAction,subupActionOut);
		text3.runAction(subUpSeq3);		
		CCLabel text4 = CCLabel.makeLabel("Click to continue...", "Arial", 30);
		text4.setPosition(CGPoint.ccp(winSize.width - text4.getContentSize().width* 0.5f,  text4.getContentSize().height* 0.5f));
		text4.setColor(ccColor3B.ccWHITE);
		storyNode.addChild(text4,3);
		CCAction subUpSeq4 = CCSequence.actions(CCFadeOut.action(0),CCDelayTime.action(12-offset),CCFadeIn.action(0.5f),CCRepeat.action(CCSequence.actions(CCFadeIn.action(0.5f),CCFadeOut.action(0.5f)), 99));
		text4.runAction(subUpSeq4);
		
		
		
		addChild(storyNode,1);
	}
	public void initContents()
	{
		SCREEN_DENSITY = context.getResources().getDisplayMetrics().densityDpi;
		
		if(SCREEN_DENSITY == DisplayMetrics.DENSITY_HIGH)
		{
			textureFolderPath = "high_images/";
		}
		else if(SCREEN_DENSITY == DisplayMetrics.DENSITY_MEDIUM)
		{
			textureFolderPath = "medium_images/";
		}
		else
		{
			textureFolderPath = "medium_images/";
		}		
		//CCSpriteFrameCache.sharedSpriteFrameCache().removeAllSpriteFrames();
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("imagesOver.plist");
		
		SoundEngine.sharedEngine().realesAllSounds();
		SoundEngine.sharedEngine().setSoundVolume(1f);
	    SoundEngine.sharedEngine().playSound(CCDirector.sharedDirector().getActivity(), R.raw.this_is_honkstep, true);
	      	
		this.setIsTouchEnabled(true);		
		
		
		if(result == 1 )//win
		{
			CCSprite bg = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("win_bg.png"));
			bg.setScale(winSize.height/(1f*bg.getContentSize().height));
			bg.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f));
			addChild(bg,1);	
			
			CCMenuItemSprite confirm_btn = CCMenuItemSprite.item(
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("win_confirm_1.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("win_confirm_2.png")), this, "clickConfirm");
			confirm_btn.setTag(6);
			confirm_btn.setScale(winSize.height/(5.5f*confirm_btn.getContentSize().height));	
			
			CCMenu confirmMenu = CCMenu.menu(confirm_btn);			
			confirmMenu.setColor(ccColor3B.ccc3(255,255,255));
			confirmMenu.setPosition(CGPoint.ccp(winSize.width * 0.8f, winSize.height * 0.2f));
			addChild(confirmMenu,2);
			int tempTotal = moneyGot + tomatoFighter.getMoney();
			moneyLabel = CCLabel.makeLabel(""+moneyGot+" += "+ tempTotal , "DroidSans", 32);
			if(tomatoFighter.getCurGameLevel()+tomatoFighter.getCurGameLoopNum()*15>=tomatoFighter.getPlayerLevel())
			{
				lvlLabel = CCLabel.makeLabel("UP! "+tomatoFighter.getPlayerLevel()+"->"+(tomatoFighter.getPlayerLevel()+1) , "DroidSans", 32);
				tomatoFighter.setPlayerLevel(tomatoFighter.getPlayerLevel()+1);
				String temp = "";
				if(tomatoFighter.getPlayerLevel()<=16) //1~5
				{
					if(tomatoFighter.getPlayerLevel() == 2)
					{
						tomatoFighter.setWeaponStatus(1, 1);
						temp = "New Weapoon!";
					}
					else if(tomatoFighter.getPlayerLevel() == 5)
					{
						tomatoFighter.setWeaponStatus(2, 1);
						temp = "New Weapoon!";
					}
					else if(tomatoFighter.getPlayerLevel() == 7)
					{
						temp = "OhMyGod 大絕招啟動!";
					}
					else if(tomatoFighter.getPlayerLevel() == 16)
					{
						temp = "新道具 解開!";
					}
				}
				
				if(tomatoFighter.getCurGameLevel()<15) //1~14 //enable next level
				{
					statusLabel = CCLabel.makeLabel("Level "+(tomatoFighter.getCurGameLevel()+1)+" Unlocked!"+temp, "DroidSans", 32);
					tomatoFighter.setGameStatus(tomatoFighter.getCurGameLevel(),1);
				}
				else //14 enable next loop
				{
					for(int i=1;i<15;i++)  //close lvl-2~lvl-15
					{
						tomatoFighter.setGameStatus(i, 0);
					}
					statusLabel = CCLabel.makeLabel("破卡! Next Loop "+ (tomatoFighter.getCurGameLoopNum()+1)+" Unlocked!"+temp, "DroidSans", 32);
					tomatoFighter.setCurGameLoopNum(tomatoFighter.getCurGameLoopNum()+1);
				}
					
				
			}
			else
			{
				lvlLabel = CCLabel.makeLabel("Unchanged: "+tomatoFighter.getPlayerLevel() , "DroidSans", 32);	
				statusLabel = CCLabel.makeLabel("*Replay session* no level up", "DroidSans", 32);
			}
			tomatoFighter.setMoney(tempTotal);
			statusLabel.setColor(ccColor3B.ccBLACK);
			statusLabel.setPosition(winSize.width * 0.4f, winSize.height * 0.2f);
			statusLabel.setScale(winSize.height/(12.5f*statusLabel.getBoundingBox().size.height));
			addChild(statusLabel,2);
			
		}
		else if(result == 0) //lose
		{
			CCSprite bg = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("lose_bg.png"));
			bg.setScale(winSize.height/(1f*bg.getContentSize().height));
			bg.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f));
			addChild(bg,1);	
			
			CCMenuItemSprite confirm_btn = CCMenuItemSprite.item(
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("lose_confirm_1.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("lose_confirm_2.png")), this, "clickConfirm");
			confirm_btn.setTag(6);
			confirm_btn.setScale(winSize.height/(5.5f*confirm_btn.getContentSize().height));	
			
			CCMenu confirmMenu = CCMenu.menu(confirm_btn);			
			confirmMenu.setColor(ccColor3B.ccc3(255,255,255));
			confirmMenu.setPosition(CGPoint.ccp(winSize.width * 0.8f, winSize.height * 0.2f));
			addChild(confirmMenu,2);
			int tempTotal = moneyGot/2 + tomatoFighter.getMoney();
			moneyLabel = CCLabel.makeLabel("Half: "+(moneyGot/2)+" += "+ tempTotal , "DroidSans", 32);
			lvlLabel = CCLabel.makeLabel("Unchanged: "+tomatoFighter.getPlayerLevel() , "DroidSans", 32);
			tomatoFighter.setMoney(tempTotal);
			
		}
		else if(result == 2) //quickgame
		{
			CCSprite bg = CCSprite.sprite("quickoverBg.png");
			bg.setScale(winSize.height/(1f*bg.getContentSize().height));
			bg.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f));
			addChild(bg,1);	
			
			/*CCSprite time = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("time.png"));
			time.setScale(winSize.height/(4f*time.getContentSize().height));
			time.setPosition(CGPoint.ccp(winSize.width * 0.4f, winSize.height * 0.6f));
			addChild(time,2);
			CCSprite killed = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("killednumber.png"));
			killed.setScale(winSize.height/(4.5f*killed.getContentSize().height));
			killed.setPosition(CGPoint.ccp(winSize.width * 0.4f, winSize.height * 0.4f));
			addChild(killed,2);
			*/
			
			CCMenuItemSprite confirm_btn = CCMenuItemSprite.item(
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("win_confirm_1.png")), 
					CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("win_confirm_2.png")), this, "clickConfirm");
			confirm_btn.setTag(6);
			confirm_btn.setScale(winSize.height/(5.5f*confirm_btn.getContentSize().height));	
			
			CCMenu confirmMenu = CCMenu.menu(confirm_btn);			
			confirmMenu.setColor(ccColor3B.ccc3(255,255,255));
			confirmMenu.setPosition(CGPoint.ccp(winSize.width * 0.8f, winSize.height * 0.2f));
			addChild(confirmMenu,2);
			
			float highScore = tomatoFighter.getQuickGameLeaderBoard(moneyGot);
			CCLabel highscore = CCLabel.makeLabel("HighScore: "+highScore, "DroidSans", 32);
			highscore.setColor(ccColor3B.ccBLACK);
			highscore.setPosition(winSize.width * 0.4f, winSize.height * 0.15f);
			highscore.setScale(winSize.height/(12.5f*highscore.getBoundingBox().size.height));
			addChild(highscore,2);
			
			moneyLabel = CCLabel.makeLabel(""+timer+" seconds", "DroidSans", 32); //time
			lvlLabel = CCLabel.makeLabel(""+kills, "DroidSans", 32); //killed						
			
			statusLabel = CCLabel.makeLabel("Score: "+ moneyGot, "DroidSans", 32);
			statusLabel.setColor(ccColor3B.ccBLACK);
			statusLabel.setPosition(winSize.width * 0.4f, winSize.height * 0.25f);
			statusLabel.setScale(winSize.height/(12.5f*statusLabel.getBoundingBox().size.height));
			addChild(statusLabel,2);	
			
		}
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).saveFighter();
		moneyLabel.setColor(ccColor3B.ccBLACK);
		moneyLabel.setPosition(winSize.width * 0.7f, winSize.height * 0.6f);
		moneyLabel.setScale(winSize.height/(12.5f*moneyLabel.getBoundingBox().size.height));
		lvlLabel.setColor(ccColor3B.ccBLACK);
		lvlLabel.setPosition(winSize.width * 0.7f, winSize.height * 0.4f);
		lvlLabel.setScale(winSize.height/(12.5f*lvlLabel.getBoundingBox().size.height));
		
		addChild(moneyLabel,2);
		addChild(lvlLabel,2);
		
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = false;
		//this.runAction(CCSequence.actions(CCDelayTime.action(3.0f), CCCallFunc.action(this, "gameOverDone")));
	}
	public void clickConfirm(Object sender)
	{		
		CCMenuItem item = (CCMenuItem) sender;
		item.setIsEnabled(false);
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = true;
		if(result == 2) //quickgame
		{
			CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameStartLayer.scene(), tOrientation.kOrientationLeftOver));
		}
		else
		{
			CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationLeftOver));
		}
	}	
	public void gameOverDone()
	{
		//CCDirector.sharedDirector().replaceScene(GameLayer.scene());
		CCDirector.sharedDirector().end();
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
		//gameOverDone();
		//CCDirector.sharedDirector().replaceScene(SetGameItemLayer.scene());
		removeAllChildren(true);
		initContents();
		
		return true;
	}
	
}
