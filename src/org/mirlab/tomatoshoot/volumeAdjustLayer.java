package org.mirlab.tomatoshoot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.camera.CCOrbitCamera;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCTransitionScene.tOrientation;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.mirlab.SpeechRecognition.pitchRecorder;
import org.mirlab.tomatoshoot.R;

import android.util.Log;
import android.view.MotionEvent;
import org.json.JSONObject;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class volumeAdjustLayer extends CCColorLayer {

	public CCBitmapFontAtlas _label;
	public CCBitmapFontAtlas _threslabel;
	protected CGSize winSize;
	CCProgressTimer greenBar;
	CCProgressTimer yellowBar;
	pitchRecorder pitchRecorder;
	CCMenuItemSprite return_btn;
	CCSprite thresBtn;
	CCSprite redBar;
	boolean thresOpen;
	double volNum;
	int tempThres;
	float maxVol = 10000000;
	float yellowPercent;
	float adjustThres;
	public static int meanVol;
	public static int volThreshold = 0;
	public Thread pitchThread;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		volumeAdjustLayer layer = new volumeAdjustLayer(ccColor4B.ccc4(255,255, 255, 255));

		// layer.getLabel().setString(message);

		scene.addChild(layer);

		return scene;
	}

	public CCBitmapFontAtlas getLabel() {
		return _label;
	}

	protected volumeAdjustLayer(ccColor4B color) {
        super(color);
        //Log.d("","SSS" + this.meanVol);
        this.setIsTouchEnabled(true);
        thresOpen = false;
        
        if(volThreshold == 0)
                tempThres = (meanVol + meanVol * 4);
        else
                tempThres = volThreshold;
        
        if(meanVol > 1000000)
                maxVol = 20000000;
        else
                maxVol = 10000000;
        
        yellowPercent = (tempThres / maxVol) * 100;
		// volNum = 0;
		winSize = CCDirector.sharedDirector().displaySize();

		_label = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		_label.setColor(ccColor3B.ccBLACK);
		_label.setPosition(CGPoint.ccp(winSize.width / 2.0f,winSize.height * 0.75f));
		addChild(_label, 0);
		
		_threslabel = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		_threslabel.setColor(ccColor3B.ccBLACK);
		_threslabel.setPosition(CGPoint.ccp(winSize.width / 2.0f,winSize.height * 0.25f));
		addChild(_threslabel, 0);
		
		redBar = CCSprite.sprite("redBar.png");
		redBar.setScale(10.0f * winSize.width / (16.0f * redBar.getContentSize().width));
		redBar.setPosition(CGPoint.ccp(winSize.width / 2.0f,winSize.height / 2.0f));
		redBar.runAction(CCOrbitCamera.action(0.0f, 1, 0, 0, 14, 0, 0));
		addChild(redBar, 1);
		greenBar = CCProgressTimer.progress("greenBar.png");
		greenBar.setScale(10.0f * winSize.width / (16.0f * redBar.getContentSize().width));
		greenBar.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarLR);
		greenBar.setPosition(CGPoint.ccp(winSize.width / 2.0f,winSize.height / 2.0f));
		greenBar.runAction(CCOrbitCamera.action(0.0f, 1, 0, 0, 14, 0, 0));
		addChild(greenBar, 3);
		yellowBar = CCProgressTimer.progress("yellowBar.png");
		yellowBar.setScale(10.0f * winSize.width
				/ (16.0f * redBar.getContentSize().width));
		yellowBar.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarLR);
		yellowBar.setPosition(CGPoint.ccp(winSize.width / 2.0f,winSize.height / 2.0f));
		yellowBar.runAction(CCOrbitCamera.action(0.0f, 1, 0, 0, 14, 0, 0));
		yellowBar.setPercentage(yellowPercent);
		addChild(yellowBar, 2);

		thresBtn = CCSprite.sprite("moveThres_red.png");
		thresBtn.setScale(winSize.width / (20f * thresBtn.getContentSize().width));
		redBar.getBoundingBox();
		//Log.d("","eeee" + yellowPercent * redBar.getContentSize().width);
		//thresBtn.setPosition(CGPoint.ccp(winSize.width / 2.0f,winSize.height / 2.0f));
		thresBtn.setPosition(CGPoint.ccp(CGRect.minX(redBar.getBoundingBox()) 
				+ yellowPercent * redBar.getContentSize().width * 0.1f
				,winSize.height * 0.5f));
		addChild(thresBtn, 4);
		// CCMenuItem menuItemTemp = CCMenuItemImage.item(
		// CCSprite.sprite("moveThres_white.png"),
		// CCSprite.sprite("moveThres_red.png"),
		// this, "radio_btn_released");
		// menuItemTemp.setScale(winSize.width/(20f*menuItemTemp.getContentSize().width));
		// menuItemTemp.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 13, 0, 0));

		// HoldableRadioCCMenu radio_btn =
		// HoldableRadioCCMenu.menu(menuItemTemp);
		// radio_btn.setPosition(CGPoint.ccp(winSize.width * 0.5f,
		// winSize.height * 0.5f));
		// addChild(radio_btn,70);

		return_btn = CCMenuItemSprite.item(CCSprite.sprite("return.png"),
				CCSprite.sprite("return_press.png"), this, "clickReturn");
		return_btn.setScale(winSize.width / (20f * return_btn.getContentSize().width));
		return_btn.setTag(0);

		CCMenu returnMenu = CCMenu.menu(return_btn);		
		returnMenu.setColor(ccColor3B.ccc3(255,255,255));
		returnMenu.setPosition(CGPoint.ccp(winSize.width * 0.9f, winSize.height * 0.2f));
		returnMenu.alignItemsVertically(5);
		addChild(returnMenu,5);		
		
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
			pitchRecorder.setRecording(false);
			pitchThread.stop();
			pitchRecorder.setIsEnd(true);
			GameLayerBase.volThreshold = (int)adjustThres;
			//CCDirector.sharedDirector().replaceScene(GameStartLayer.scene());
			CCDirector.sharedDirector().popScene();
		}

	}

	public void update(float dt) {

		double volPercent, volNum;
		volNum = pitchRecorder.getVol();
		volPercent = (volNum / maxVol) * 100;
		greenBar.setPercentage((float) volPercent);
		//Log.d("@@@@", "((((" + volPercent);
		// this._label.setString((int)LoadingNum + "%");
		// if(LoadingNum == 100){
		// //CCDirector.sharedDirector().replaceScene(GameStartLayer.scene());
		// CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(1,
		// GameStartLayer.scene(), ccColor3B.ccWHITE));
		//  		  
		// return;
		// }
	}

	@Override
	public void onEnter() {
		super.onEnter();
		SoundEngine.sharedEngine().mute();		
	}

	@Override
	public void onExit() {
		SoundEngine.sharedEngine().unmute();
		super.onExit();		

	}
	public boolean ccTouchesMoved(MotionEvent e) {
		CGPoint touchLocation = CGPoint.ccp(e.getX(), e.getY());
		touchLocation = CCDirector.sharedDirector().convertToGL(touchLocation);
		float thresX;
		if (thresOpen == true) {
			redBar.getBoundingBox();
			if (e.getX() < CGRect.minX(redBar.getBoundingBox())) {
				redBar.getBoundingBox();
				thresX = CGRect.minX(redBar.getBoundingBox());
			} else {
				redBar.getBoundingBox();
				if (e.getX() > CGRect.maxX(redBar.getBoundingBox())) {
					redBar.getBoundingBox();
					thresX = CGRect.maxX(redBar.getBoundingBox());
				} else
					thresX = e.getX();
			}
			
			thresBtn.setPosition(CGPoint.ccp(thresX, winSize.height / 2));
			redBar.getBoundingBox();
			redBar.getBoundingBox();
			yellowPercent = (thresBtn.getPosition().x - CGRect.minX(redBar.getBoundingBox()))
							/ CGRect.width(redBar.getBoundingBox());
			yellowBar.setPercentage(yellowPercent * 100f);
			adjustThres = maxVol * yellowPercent;
			
			
		}
		Log.d("@@@@", "@@@@x" + touchLocation.x + "@@@@y" + touchLocation.y);
		return true;
	}

	public boolean ccTouchesBegan(MotionEvent e) {
		if (CGRect.containsPoint(thresBtn.getBoundingBox(), CGPoint.ccp(e.getX(), e.getY()))) {
			// thresBtn = CCSprite.sprite("moveThres_white.png");
			thresOpen = true;
		}
		return true;
	}

	public boolean ccTouchesEnded(MotionEvent e) {
		pitchRecorder.setThres((int)adjustThres);
		thresOpen = false;
		return true;
	}
}
