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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


public class GameLoadingLayer extends CCColorLayer{
	protected CCBitmapFontAtlas _label;
	protected CGSize winSize;
	CCProgressTimer Loading_progress;
	pitchRecorder volThRecorder;
	Thread volThThread;
	float LoadingNum;
	Context context = CCDirector.sharedDirector().getActivity();
	CCScene scene;
	
	public static CCScene scene()
	{
		CCScene scene = CCScene.node();
		GameLoadingLayer layer = new GameLoadingLayer(ccColor4B.ccc4(0, 0, 0, 255));
		//GameLayerLV07 layer = new GameLayerLV07(ccColor4B.ccc4(255, 255, 255, 255));
		
		//layer.getLabel().setString(message);
		
		scene.addChild(layer);
		
		return scene;
	}
	
	public CCBitmapFontAtlas getLabel()
	{
		return _label;
	}
	
	protected GameLoadingLayer(ccColor4B color)
	{
		super(color);
		
		this.setIsTouchEnabled(false);
		LoadingNum = 0;
		winSize = CCDirector.sharedDirector().displaySize();
		
		SoundEngine.sharedEngine().preloadSound(context, R.raw.this_is_honkstep);
		
		
		_label = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		_label.setColor(ccColor3B.ccBLACK);
		_label.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height * 0.45f));
		addChild(_label,3);
		
		CCSprite Loading_frame = CCSprite.sprite("hp_frame.png");
		Loading_frame.setScale(winSize.height/(4f*Loading_frame.getContentSize().height));
		Loading_frame.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		Loading_frame.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 14, 0, 0));
		addChild(Loading_frame,1);
		Loading_progress = CCProgressTimer.progress("hp_full.png");
		Loading_progress.setType(CCProgressTimer.kCCProgressTimerTypeVerticalBarBT);
		Loading_progress.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		Loading_progress.setScale(winSize.height/(4f*Loading_progress.getContentSize().height));
		Loading_progress.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 14, 0, 0));
		addChild(Loading_progress,2);
		volThRecorder = new pitchRecorder(200000,0,null,3);
		volThThread = new Thread(volThRecorder);
		volThRecorder.setComputeMeanVol(true);
		volThRecorder.setRecording(true);
		volThThread.start();
		this.schedule("update", 0.02f);//update logics 20fps
	}
	
	public void gameStartDone()
	{
		//CCDirector.sharedDirector().replaceScene(GameLayer.scene());
		CCDirector.sharedDirector().end();
	}
	public void clickStart(Object sender)
	{		
		CCMenuItem item = (CCMenuItem) sender;
		//Log.d("DDDDD","testClick"+item.getTag());		
		//SoundEngine.sharedEngine().playEffect(context, R.raw.gunreload);
		// Pew!
		if(item.getTag() == 0 ) //1st weapon
		{
			volThRecorder.setRecording(false);
			try {
				volThThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//volumeAdjustLayer.volThreshold = volThRecorder.getMeanVol();
			GameLayer.volThreshold = volThRecorder.getMeanVol();
			CCDirector.sharedDirector().replaceScene(GameLayer.scene());
		}
		else if(item.getTag() == 1){
			
		}

	}
	public void update(float dt){
				
		Loading_progress.setPercentage(LoadingNum);
		this._label.setString((int)LoadingNum + "%");
		if(LoadingNum == 100){
			//CCDirector.sharedDirector().replaceScene(GameStartLayer.scene());
			CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(1, scene, ccColor3B.ccWHITE));
  		  
			return;
		}
		else if(LoadingNum == 99){
			scene = GameStartLayer.scene();	
			LoadingNum+=1;		
		}
		else
		{
			LoadingNum+=1;			
		}
	}
	@Override
	public void onEnter() {
		super.onEnter();
		
	}
	@Override
	public void onExit() {
		volThRecorder.setRecording(false);
		try {
			volThThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		volumeAdjustLayer.meanVol = volThRecorder.getMeanVol();  //
		GameLayer.meanVol = volThRecorder.getMeanVol();		
		super.onExit();
		
	}
}
