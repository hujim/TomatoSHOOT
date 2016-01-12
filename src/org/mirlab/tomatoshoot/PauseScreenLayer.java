package org.mirlab.tomatoshoot;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.instant.CCCallFuncND;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCFadeTo;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
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
import android.view.MotionEvent;

public class PauseScreenLayer extends CCColorLayer {

	CCSprite console_full ;
	Context context = CCDirector.sharedDirector().getActivity();	
	static CCScene scene;
	CGSize winSize;
	public static CCScene scene()
	{
		scene = CCScene.node();
		scene.setTag(2);
		CCColorLayer layer = new PauseScreenLayer(ccColor4B.ccc4(0, 0, 100, 100));
		//scene.setVisible(false);
		scene.addChild(layer);
		
		return scene;
	}
	public PauseScreenLayer(ccColor4B color) {
		super(color);
		this.setIsTouchEnabled(true);
		winSize = CCDirector.sharedDirector().displaySize();
		
		CCLabel title = CCLabel.makeLabel("¼È°±", "DroidSansFallback", 70);
		title.setColor(ccColor3B.ccRED);
		title.setPosition(winSize.width*0.5f, winSize.height*0.8f);
		addChild(title);
		
		console_full = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("console_full.png"));		
		console_full.setPosition(CGPoint.ccp(winSize.width * 0.5f, -winSize.height * 0.084f));	
		console_full.setScale(winSize.height/(2*console_full.getContentSize().height));
		addChild(console_full,1);
		
		CGSize consoleS = console_full.getBoundingBox().size;
		CCSprite resume_btn = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("resume_btn.png"));
		//resume_btn.setPosition(CGPoint.ccp(consoleS.width * 0.6f, consoleS.height * 0.63f));
		//resume_btn.setScale(winSize.height/(10*resume_btn.getContentSize().height));
		CCMenuItem itemResume = CCMenuItemImage.item(resume_btn,resume_btn, this, "click_resume");
		itemResume.setPosition(CGPoint.ccp(consoleS.width * 0.14f, -consoleS.height * 0.55f));
		itemResume.setScale(winSize.height/(10*resume_btn.getContentSize().height));
		//console_full.addChild(resume_btn,1);
		CCSprite info_btn = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("info_btn.png"));
		CCMenuItem itemInfo = CCMenuItemImage.item(info_btn,info_btn, this, "click_info");
		itemInfo.setPosition(CGPoint.ccp(consoleS.width * 0.06f, -consoleS.height * 0.64f));
		itemInfo.setScale(winSize.height/(10*info_btn.getContentSize().height));
		//console_full.addChild(resume_btn,1);
		CCSprite restart_btn = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("restart_btn.png"));
		CCMenuItem itemRestart = CCMenuItemImage.item(restart_btn,restart_btn, this, "click_restart");
		itemRestart.setPosition(CGPoint.ccp(-consoleS.width * 0.14f, -consoleS.height * 0.55f));
		itemRestart.setScale(winSize.height/(10*itemRestart.getContentSize().height));
		CCSprite menu_btn = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("menu_btn.png"));
		CCMenuItem itemMenu = CCMenuItemImage.item(menu_btn,menu_btn, this, "click_menu");
		itemMenu.setPosition(CGPoint.ccp(-consoleS.width * 0.06f, -consoleS.height * 0.64f));
		itemMenu.setScale(winSize.height/(10*itemMenu.getContentSize().height));
		
		
		CCMenu pauseMenu = CCMenu.menu(itemResume,itemInfo,itemRestart,itemMenu);
		//pauseMenu.setPosition(CGPoint.ccp(-winSize.width*0.0f, -winSize.height*0.0f));
		console_full.addChild(pauseMenu,1);
		
		// TODO Auto-generated constructor stub
	}
	public void click_resume(Object sender)
	{
		/*
		((CCNode) sender).runAction(CCSequence.actions(
				CCScaleBy.action(0.1f, 1.3f),
				CCScaleBy.action(0.1f, 1.3f).reverse()
				));*/ 
		unPauseGame();
	}
	public void click_info(Object sender)
	{
		
	}
	public void click_menu(Object sender)
	{
		CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameStartLayer.scene(), tOrientation.kOrientationLeftOver));
		  
	}
	public void click_restart(Object sender)
	{
		/*
		unPauseGame();
		CCDirector.sharedDirector().replaceScene(CCDirector.sharedDirector().getRunningScene());
		SoundEngine.sharedEngine().realesAllSounds();
		SoundEngine.sharedEngine().setSoundVolume(0.5f);
		SoundEngine.sharedEngine().playSound(context, R.raw.mr_reno, true);*/
		CCDirector.sharedDirector().pushScene(volumeAdjustLayer.scene());
	}

	public PauseScreenLayer(ccColor4B color, float w, float h) {
		super(color, w, h);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onEnter()
	{
		super.onEnter();		
	}
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
		// Choose one of the touches to work with
		//CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
		
		unPauseGame();
		
		return true;
	}
	public void unPauseGame()
	{
		//CCScene scene = PauseScreenLayer.scene();
		//CCDirector.sharedDirector().pushScene(CCPageTurnTransition.transition(1, scene, true));
		//CCDirector.sharedDirector().pushScene(scene);
		float heightS = console_full.getBoundingBox().size.height;
		console_full.runAction(CCSequence.actions(
				CCMoveBy.action(0.05f, CGPoint.ccp(0, -heightS*0.4f))
		));
		
		
		this.setIsTouchEnabled(false);
		GameLayerBase parentLayer = ((GameLayerBase) this.getParent());
		parentLayer.setIsTouchEnabled(true);
		parentLayer.startSchedulers();
		
		parentLayer.pauseScreenLayer.runAction(
				CCSequence.actions(
						CCFadeTo.action(0.2f,0),					
						CCCallFuncN.action(parentLayer, "spriteMoveFinished")));
		parentLayer.gamePaused = false;
		
		
		//this.getParent().removeChild(this, true);
		//CCDirector.sharedDirector().resume();
	}

}
