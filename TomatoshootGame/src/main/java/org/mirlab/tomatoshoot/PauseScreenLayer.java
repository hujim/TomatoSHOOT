package org.mirlab.tomatoshoot;

import java.io.File;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCFadeTo;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.mirlab.SpeechRecognition.pitchRecorder;

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
		
		CCLabel title = CCLabel.makeLabel("暫停", "DroidSansFallback", 70);
		title.setColor(ccColor3B.ccRED);
		title.setPosition(winSize.width*0.5f, winSize.height*0.8f);
		addChild(title);
		
		console_full = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("console_full.png"));		
		console_full.setPosition(CGPoint.ccp(winSize.width * 0.5f, -winSize.height * 0.084f));	
		console_full.setScale(winSize.height/(2*console_full.getContentSize().height));
		addChild(console_full,1);
		
		CGSize consoleS = console_full.getContentSize();
		CCSprite resume_btn = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("resume_btn.png"));
		//resume_btn.setPosition(CGPoint.ccp(consoleS.width * 0.6f, consoleS.height * 0.63f));
		//resume_btn.setScale(winSize.height/(10*resume_btn.getContentSize().height));
		CCMenuItem itemResume = CCMenuItemImage.item(resume_btn,resume_btn, this, "click_resume");
		itemResume.setPosition(CGPoint.ccp(consoleS.width * 0.0f, -consoleS.height * 0.5f));
		itemResume.setScale(winSize.height/(10*resume_btn.getContentSize().height));
		//console_full.addChild(resume_btn,1);
		CCSprite info_btn = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("info_btn.png"));
		CCMenuItem itemInfo = CCMenuItemImage.item(info_btn,info_btn, this, "click_info");
		itemInfo.setPosition(CGPoint.ccp(consoleS.width * 0.14f, -consoleS.height * 0.71f));
		itemInfo.setScale(winSize.height/(10*info_btn.getContentSize().height));
		//console_full.addChild(resume_btn,1);
		CCSprite setting_btn = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("setting_btn.png"));
		CCMenuItem itemSetting = CCMenuItemImage.item(setting_btn,setting_btn, this, "click_setting");
		itemSetting.setPosition(CGPoint.ccp(consoleS.width * 0.06f, -consoleS.height * 0.71f));
		itemSetting.setScale(winSize.height/(10*itemSetting.getContentSize().height));
		CCSprite menu_btn = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("menu_btn.png"));
		CCMenuItem itemMenu = CCMenuItemImage.item(menu_btn,menu_btn, this, "click_menu");
		itemMenu.setPosition(CGPoint.ccp(-consoleS.width * 0.14f, -consoleS.height * 0.71f));
		itemMenu.setScale(winSize.height/(10*itemMenu.getContentSize().height));
		
		
		CCMenu pauseMenu = CCMenu.menu(itemResume,itemInfo,itemSetting,itemMenu);
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
		CCDirector.sharedDirector().pushScene(HelpLayer.scene());
	}
	public void click_menu(Object sender)
	{
		//CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameStartLayer.scene(), tOrientation.kOrientationLeftOver));
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).leaveGameSessionConfirmAlert();
	}
	public void click_setting(Object sender)
	{
//		GameLayerBase parentLayer = ((GameLayerBase) this.getParent());
//		parentLayer.pitchRecorder.choose = 2;
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
		
		//unPauseGame();
		
		return true;
	}
	public void unPauseGame()
	{
		GameLayerBase parentLayer = ((GameLayerBase) this.getParent());
		
		if(parentLayer.gamePaused == false)
		{
			return;
		}
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = true;
		//CCScene scene = PauseScreenLayer.scene();
		//CCDirector.sharedDirector().pushScene(CCPageTurnTransition.transition(1, scene, true));
		//CCDirector.sharedDirector().pushScene(scene);
		float heightS = console_full.getContentSize().height;
		console_full.runAction(CCSequence.actions(
				CCMoveBy.action(0.05f, CGPoint.ccp(0, -heightS*0.4f))
		));
		
		//parentLayer.pitchRecorder.choose = 1;
		pitchRecorder tempPitchRecorder = new pitchRecorder(GameLayerBase.meanVol,GameLayerBase.volThreshold, parentLayer, 1);
		Thread tempPitchThread = new Thread(tempPitchRecorder);
		tempPitchRecorder.setwaveFileName(new File("/sdcard/recData/output/rec.wav"));
		tempPitchRecorder.setComputeZCR(true);
		tempPitchRecorder.setComputePitch(true);
		tempPitchRecorder.setRecording(true);
		//tempPitchThread.start();
		parentLayer.pitchRecorder = tempPitchRecorder;
		parentLayer.pitchThread = tempPitchThread;
//		parentLayer.pitchThread.start();
//		parentLayer.pitchRecorder.setRecording(true);
		parentLayer.pitchThread.start();
		this.setIsTouchEnabled(false);
		parentLayer.setIsTouchEnabled(true);
		//parentLayer.setIsAccelerometerEnabled(true);
		parentLayer.startSensors();
		parentLayer.startSchedulers();
		
		
		parentLayer.pauseScreenLayer.runAction(
				CCSequence.actions(
						CCFadeTo.action(0.2f,0),					
						CCCallFuncN.action(parentLayer, "setInTransitionFalse"),
						CCCallFuncN.action(parentLayer, "spriteMoveFinished")
						));
		parentLayer.gamePaused = false;
		parentLayer.yellowBar.setPercentage((float)(parentLayer.meanVol * 100f / parentLayer.maxVol));
		
		//this.getParent().removeChild(this, true);
		//CCDirector.sharedDirector().resume();
	}

}
