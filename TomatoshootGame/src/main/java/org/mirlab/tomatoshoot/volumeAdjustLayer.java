package org.mirlab.tomatoshoot;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCShow;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.mirlab.SpeechRecognition.pitchRecorder;

import android.util.Log;
import android.view.MotionEvent;

public class volumeAdjustLayer extends CCColorLayer {

	public CCBitmapFontAtlas _label;
	//public CCBitmapFontAtlas _threslabel;
	protected CGSize winSize;
	CCProgressTimer whiteBar;
	CCProgressTimer yellowBar;
	pitchRecorder pitchRecorder;
	CCMenuItemSprite return_btn;
	CCMenuItemSprite auto_adjust;
	CCSprite thresBtn;
	CCSprite greenBar;
	CCSprite adjustTitle;
	public CCSprite showWord;
	public CCSprite bombWord;
	CCSprite accept;
	CCSprite noise;
	CCSprite BackGround;
	CCSprite recog;
	CCSprite quiet;
	
	boolean thresOpen;
	boolean autoOpen;
	double volNum;
	int tempThres;
	int countSec;
	int auto_time;
	public static float maxVol;
	float yellowPercent;
	float adjustThres;
	public static int meanVol;
	public static int volThreshold = 0;
	public static int firstTime = 1;
	public Thread pitchThread;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		volumeAdjustLayer layer = new volumeAdjustLayer(ccColor4B.ccc4(255,255, 255, 255));
		scene.setTag(4);
		
		scene.addChild(layer);

		return scene;
	}

	protected volumeAdjustLayer(ccColor4B color) {
        super(color);
        //Log.d("","SSS" + this.meanVol);
        this.setIsTouchEnabled(true);
        thresOpen = false;
        countSec = -1;
        auto_time = -1;
//        if(volThreshold == 0)
//                tempThres = (meanVol + meanVol * 4);
//        else
//                tempThres = volThreshold;
        meanVol = GameLayerBase.meanVol;
        
        tempThres = meanVol;
        if(firstTime == 1){
        	maxVol = meanVol * 4;
        	GameLayerBase.maxVol = (int)maxVol;
        	firstTime = 2;
        }
//        if(meanVol > 1000000)
//                maxVol = 20000000;
//        else
//                maxVol = 10000000;
        
        yellowPercent = (meanVol / maxVol) * 100;
        Log.d("@@@@", "@@@@###" + yellowPercent);
        if(yellowPercent > 100)
        	yellowPercent = 100.0f;
		// volNum = 0;
		winSize = CCDirector.sharedDirector().displaySize();

		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("imagesGame.plist");
		
		_label = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		_label.setColor(ccColor3B.ccBLACK);
		_label.setPosition(CGPoint.ccp(winSize.width * 0.7f, winSize.height * 0.2f));
		_label.setVisible(false);
		addChild(_label, 20);
//		
//		_threslabel = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
//		_threslabel.setColor(ccColor3B.ccBLACK);
//		_threslabel.setPosition(CGPoint.ccp(winSize.width / 2.0f,winSize.height * 0.25f));
//		addChild(_threslabel, 0);
		adjustTitle = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_title.png"));
		adjustTitle.setPosition(CGPoint.ccp(winSize.width * 0.7f,winSize.height * 0.9f));
		adjustTitle.setScale(winSize.width / (2.778f * adjustTitle.getContentSize().width));
		addChild(adjustTitle);
		showWord = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_show.png"));
		showWord.setPosition(CGPoint.ccp(winSize.width * 0.7f,winSize.height * 0.75f));
		showWord.setScale(1.5f * winSize.width / (14.54f * showWord.getContentSize().width));
		showWord.setVisible(false);
		addChild(showWord);
		bombWord = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_bomb.png"));
		bombWord.setPosition(CGPoint.ccp(winSize.width * 0.7f,winSize.height * 0.75f));
		bombWord.setScale(1.5f * winSize.width / (14.54f * bombWord.getContentSize().width));
		bombWord.setVisible(false);
		addChild(bombWord);
		accept =  CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_accept.png"));
		accept.setPosition(CGPoint.ccp(winSize.width * 0.7f,winSize.height * 0.6f));
		accept.setScale(winSize.width / (4.44f * accept.getContentSize().width));
		accept.runAction(CCRepeatForever.action(CCSequence.actions(CCFadeIn.action(0.5f),CCFadeOut.action(0.5f))));
		addChild(accept);
		noise =  CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_noise.png"));
		//noise.setPosition(CGPoint.ccp(winSize.width * 0.7f,winSize.height * 0.45f));
		noise.setScale(0.5f * winSize.height / (12.0f * noise.getContentSize().height));
		noise.setVisible(false);
		addChild(noise);
		recog =  CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_recog.png"));
		//recog.setPosition(CGPoint.ccp(winSize.width * 0.7f,winSize.height * 0.45f));
		recog.setScale(0.5f * winSize.height / (12.0f * recog.getContentSize().height));
		recog.setVisible(false);
		addChild(recog);
		quiet =  CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_quiet.png"));
		quiet.setPosition(CGPoint.ccp(winSize.width * 0.7f,winSize.height * 0.45f));
		quiet.setScale(winSize.width / (3.67f * quiet.getContentSize().width));
		quiet.setVisible(false);
		addChild(quiet);
		
		BackGround = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_barBG.png"));
		BackGround.setPosition(CGPoint.ccp(winSize.width * 0.3f,winSize.height / 2.0f));
		BackGround.setScale(winSize.width / (3.375f * BackGround.getContentSize().width));
		addChild(BackGround, 0);
		
		greenBar = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_green.png"));
		greenBar.setScale(5f * winSize.width / (80.0f * greenBar.getContentSize().width));
		greenBar.setPosition(CGPoint.ccp(winSize.width * 0.365f,winSize.height / 2.0f));
		addChild(greenBar, 1);
		whiteBar = CCProgressTimer.progress("Setting_white.png");
		whiteBar.setScale(5f * winSize.width / (80.0f * whiteBar.getContentSize().width));
		whiteBar.setType(CCProgressTimer.kCCProgressTimerTypeVerticalBarBT);
		whiteBar.setPosition(CGPoint.ccp(winSize.width * 0.365f,winSize.height / 2.0f));
		addChild(whiteBar, 3);
		yellowBar = CCProgressTimer.progress("Setting_yellow.png");
		yellowBar.setScale(5f * winSize.width / (80.0f * yellowBar.getContentSize().width));
		yellowBar.setType(CCProgressTimer.kCCProgressTimerTypeVerticalBarBT);
		yellowBar.setPosition(CGPoint.ccp(winSize.width * 0.365f,winSize.height / 2.0f));
		yellowBar.setPercentage(yellowPercent);
		addChild(yellowBar, 2);

		thresBtn = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_scroll.png"));
		thresBtn.setScale(winSize.width / (7.62f * thresBtn.getContentSize().width));
		//Log.d("@@@@","@@@@" + CGRect.minY(greenBar.getBoundingBox()) + "   " + 5 *greenBar.getContentSize().height);
		thresBtn.setPosition(CGPoint.ccp(winSize.width * 0.365f,CGRect.minY(greenBar.getBoundingBox()) + yellowPercent * (0.05f * greenBar.getContentSize().height)));
		addChild(thresBtn, 4);

		return_btn = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_back.png")),
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_back_down.png")), this, "clickReturn");
		return_btn.setScale(winSize.width / (9.63f * return_btn.getContentSize().width));
		return_btn.setTag(0);

		CCMenu returnMenu = CCMenu.menu(return_btn);		
		returnMenu.setColor(ccColor3B.ccc3(255,255,255));
		returnMenu.setPosition(CGPoint.ccp(winSize.width * 0.1f, winSize.height * 0.1f));
		returnMenu.alignItemsVertically(5);
		addChild(returnMenu,5);		
		
		auto_adjust = CCMenuItemSprite.item(CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_autoAdj_up.png")),
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Setting_autoAdj_down.png")), this, "clickReturn");
		auto_adjust.setScale(winSize.width / (5.161f * auto_adjust.getContentSize().width));
		auto_adjust.setTag(1);

		CCMenu autoMenu = CCMenu.menu(auto_adjust);		
		autoMenu.setColor(ccColor3B.ccc3(255,255,255));
		autoMenu.setPosition(CGPoint.ccp(winSize.width * 0.7f, winSize.height * 0.2f));
		autoMenu.alignItemsVertically(5);
		addChild(autoMenu);	
		
		pitchRecorder = new pitchRecorder(meanVol,tempThres, this, 2);
		pitchThread = new Thread(pitchRecorder);
		pitchRecorder.setComputeZCR(true);
		pitchRecorder.setComputePitch(true);
		pitchRecorder.setRecording(true);
		pitchThread.start();
		this.schedule("update", 0.05f);// update logics 20fps

	}

	public void clickReturn(Object sender) {
		CCMenuItem item = (CCMenuItem) sender;
		if (item.getTag() == 0) // 1st weapon
		{
			//CCDirector.sharedDirector().replaceScene(GameStartLayer.scene());
			CCDirector.sharedDirector().popScene();
		}
		else if(item.getTag() == 1){
			countSec = 0;
			auto_time = 3;
			autoOpen = true;
			pitchRecorder.setAutoMean(true);
			pitchRecorder.setComputeMeanVol(true);
			pitchRecorder.setOutputToFile(true);
			pitchRecorder.setComputeZCR(false);
			pitchRecorder.setComputePitch(false);
			_label.setVisible(true);
			setIsTouchEnabled(false);
			quiet.setVisible(true);
			whiteBar.setVisible(false);
			noise.setVisible(false);
			recog.setVisible(false);
			
		}

	}

	public void update(float dt) {

		double volPercent, volNum;
		if(autoOpen){
			volNum = pitchRecorder.getMeanVol();
			volPercent = (volNum / maxVol) * 100;
			yellowBar.setPercentage((float) volPercent);
			thresBtn.setPosition(CGPoint.ccp(winSize.width * 0.365f,CGRect.minY(greenBar.getBoundingBox()) + (float)volPercent * (0.05f * greenBar.getContentSize().height)));
			auto_adjust.setIsEnabled(false);
			auto_adjust.setOpacity(125);
			countSec++;
			if(countSec == 25){
				auto_time--;
				countSec = 0;
			}
			_label.setString(String.valueOf(auto_time) + "s");
		}
		else{
			volNum = pitchRecorder.getVol();
			volPercent = (volNum / maxVol) * 100;
			if(volPercent >= 100)
				volPercent = 99;
			whiteBar.setPercentage((float) volPercent);
			noise.setPosition(CGPoint.ccp(winSize.width * 0.5f,CGRect.minY(greenBar.getBoundingBox()) + (float)volPercent * (0.05f * greenBar.getContentSize().height)));
			recog.setPosition(CGPoint.ccp(winSize.width * 0.5f,CGRect.minY(greenBar.getBoundingBox()) + (float)volPercent * (0.05f * greenBar.getContentSize().height)));
			auto_adjust.setIsEnabled(true);
			auto_adjust.setOpacity(500);
			if(pitchRecorder.getVol() >  pitchRecorder.getMeanVol()){
				recog.runAction(
						CCSequence.actions(
							CCFadeOut.action(0.2f)
						)
				);
				noise.runAction(
						CCSequence.actions(
							CCShow.action()
						)
				);
			}
			else{	
				noise.runAction(
						CCSequence.actions(
							CCFadeOut.action(0.2f)
						)
				);
				recog.runAction(
						CCSequence.actions(
							CCShow.action()
						)
				);
			}
		}
		
		if(auto_time == 0){
			auto_time = -1;
			countSec = -1;
			autoOpen = false;
			pitchRecorder.setAutoMean(false);
			pitchRecorder.setComputeMeanVol(false);
			pitchRecorder.setOutputToFile(false);
			pitchRecorder.setComputeZCR(true);
			pitchRecorder.setComputePitch(true);
			_label.setVisible(false);
			setIsTouchEnabled(true);
			quiet.setVisible(false);
			whiteBar.setVisible(true);
			noise.setVisible(true);
			recog.setVisible(true);
		}
		
	}

	@Override
	public void onEnter() {
		super.onEnter();
		SoundEngine.sharedEngine().mute();		
	}

	@Override
	public void onExit() {
		/*
		pitchRecorder.setRecording(false);
		pitchThread.stop();
		pitchRecorder.setIsEnd(true);*/
		GameLayerBase.meanVol = pitchRecorder.getMeanVol();
		this.meanVol = pitchRecorder.getMeanVol();
		//Log.d("@@@@", "@@@@2" + pitchRecorder.getMeanVol());
		pitchRecorder.setRecording(false);
		pitchRecorder.setIsEnd(true);
		try {
			pitchThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		SoundEngine.sharedEngine().unmute();
		super.onExit();		

	}
	public boolean ccTouchesMoved(MotionEvent e) {
		CGPoint touchLocation = CGPoint.ccp(e.getX(), e.getY());
		touchLocation = CCDirector.sharedDirector().convertToGL(touchLocation);
		float thresY;
		if (thresOpen == true) {
			if (touchLocation.y < CGRect.minY(greenBar.getBoundingBox())) {
				thresY = CGRect.minY(greenBar.getBoundingBox());
			} 
			else if (touchLocation.y > CGRect.maxY(greenBar.getBoundingBox())) {
					thresY = CGRect.maxY(greenBar.getBoundingBox());
			} 
			else{
					thresY = touchLocation.y;
			}
			
			thresBtn.setPosition(CGPoint.ccp(winSize.width * 0.365f, thresY));

			yellowPercent = (thresBtn.getPosition().y - CGRect.minY(greenBar.getBoundingBox()))
							/ CGRect.height(greenBar.getBoundingBox());
			yellowBar.setPercentage(yellowPercent * 100f);
			adjustThres = maxVol * yellowPercent;
			
			
		}
		//Log.d("@@@@", "@@@@x" + touchLocation.x + "@@@@y" + touchLocation.y);
		return true;
	}

	public boolean ccTouchesBegan(MotionEvent e) {
		CGPoint touchLocation = CGPoint.ccp(e.getX(), e.getY());
		touchLocation = CCDirector.sharedDirector().convertToGL(touchLocation);
		if (CGRect.containsPoint(thresBtn.getBoundingBox(), CGPoint.ccp(touchLocation.x, touchLocation.y))){
			thresOpen = true;
		}
		return true;
	}

	public boolean ccTouchesEnded(MotionEvent e) {
		pitchRecorder.setMeanVol((int)adjustThres);
		thresOpen = false;
		//Log.d("@@@@", "@@@@" + adjustThres);
		return true;
	}
}
