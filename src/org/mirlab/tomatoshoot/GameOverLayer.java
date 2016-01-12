package org.mirlab.tomatoshoot;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
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
import org.mirlab.tomatoshoot.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class GameOverLayer extends CCColorLayer
{
	protected CCLabel moneyLabel;
	protected CCLabel lvlLabel;
	public static int moneyGot;
	public static boolean win;
	TomatoFighter tomatoFighter;
	static String textureFolderPath;
	final int SCREEN_DENSITY;
	Context context = CCDirector.sharedDirector().getActivity();
	
	public static CCScene scene(int money, boolean won)
	{
		CCScene scene = CCScene.node();
		moneyGot = money;
		win = won;	
		
		GameOverLayer layer = new GameOverLayer(ccColor4B.ccc4(0, 0, 0, 50));		
		scene.addChild(layer);
		
		return scene;
	}	
	protected GameOverLayer(ccColor4B color)
	{
		super(color);
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
		
		tomatoFighter = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).tomatoFighter;
		
		this.setIsTouchEnabled(true);		
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		if(win)
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
		}
		else
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
		}
		moneyLabel = CCLabel.makeLabel(""+moneyGot, "DroidSans", 32);
		moneyLabel.setColor(ccColor3B.ccBLACK);
		moneyLabel.setPosition(winSize.width * 0.7f, winSize.height * 0.6f);
		addChild(moneyLabel,2);
		
		//this.runAction(CCSequence.actions(CCDelayTime.action(3.0f), CCCallFunc.action(this, "gameOverDone")));
	}
	public void clickConfirm(Object sender)
	{		
		CCMenuItem item = (CCMenuItem) sender;
		CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationLeftOver));
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
		
		return true;
	}
}
