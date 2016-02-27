package org.mirlab.tomatoshoot;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.camera.CCOrbitCamera;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.mirlab.SpeechRecognition.pitchRecorder;

import android.content.Context;


public class GameLoadingLayer extends CCColorLayer{
	protected CCBitmapFontAtlas _label;
	protected CGSize winSize;
	CCProgressTimer Loading_progress;
	pitchRecorder volThRecorder;
	Thread volThThread;
	float LoadingNum;
	Context context = CCDirector.sharedDirector().getActivity();
	public static int meanVol;
	
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
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("imagesStart.plist");
		this.setIsTouchEnabled(false);
		LoadingNum = 0;
		winSize = CCDirector.sharedDirector().displaySize();
		
		SoundEngine.sharedEngine().preloadSound(context, R.raw.this_is_honkstep);
		
		
		_label = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		_label.setColor(ccColor3B.ccBLACK);
		_label.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height * 0.45f));
		addChild(_label,3);
		
		CCSprite calibrate =  CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("calibrate.png"));
		calibrate.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.75f));
		calibrate.setScale(winSize.height / (4f * calibrate.getContentSize().height));
		calibrate.runAction(CCRepeatForever.action(CCSequence.actions(CCFadeIn.action(0.2f),CCFadeOut.action(0.2f))));
		addChild(calibrate,1);
		CCSprite quiet =  CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("load_quiet.png"));
		quiet.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.2f));
		quiet.setScale(winSize.height / (4f * quiet.getContentSize().height));
		//quiet.runAction(CCRepeatForever.action(CCSequence.actions(CCFadeIn.action(0.2f),CCFadeOut.action(0.3f))));
		addChild(quiet,1);

		
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
	public void update(float dt){
				
		Loading_progress.setPercentage(LoadingNum);
		this._label.setString((int)LoadingNum + "%");
		if(LoadingNum == 100){
			//CCDirector.sharedDirector().replaceScene(GameStartLayer.scene());
			CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.5f, GameStartLayer.scene(), ccColor3B.ccWHITE));
  		  	this.unschedule("update");
			return;
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
		//volThRecorder.setIsEnd(true);
		try {
			volThThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		volumeAdjustLayer.meanVol = volThRecorder.getMeanVol();  //
		GameLayerBase.meanVol = volThRecorder.getMeanVol();
		GameLayerBase.volThreshold = volThRecorder.getMeanVol() * 5 ;
		GameLayerBase.maxVol = GameLayerBase.meanVol * 4;
		meanVol = volThRecorder.getMeanVol();
		super.onExit();
		
	}
}
