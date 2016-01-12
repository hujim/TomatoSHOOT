package org.mirlab.tomatoshoot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCParallaxNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCFlipXTransition;
import org.cocos2d.transitions.CCTransitionScene.tOrientation;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.mirlab.SpeechRecognition.pitchRecorder;
import org.mirlab.tomatoshoot.R;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;


import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class GameStartLayer extends CCColorLayer
{
	//protected CCBitmapFontAtlas _label;
	protected CCLabel _label;
	protected CGSize winSize;
	mirfb FB;
	Camera mCamera;
	
	ArrayList<String> IdList = new ArrayList<String>(); 
	ArrayList<String> NameList = new ArrayList<String>(); 
	Bitmap mIcon1 = null;
	URL img_value = null;
	pitchRecorder volThRecorder;
	Thread volThThread;
	CCMenuItemSprite newGame,continueGame,quick,setting,help,about,process_btn,rank_btn,music_btn,store_btn,arrow_btn,youtube_btn,FB_btn;
	boolean processOpen,arrowOpen;
	CCParallaxNode bgParallax;
	boolean isFirstRun;
	
	static String textureFolderPath;
	final int SCREEN_DENSITY;
	Context context = CCDirector.sharedDirector().getActivity();
	TomatoFighter tomatoFighter;
	GameStartLayer layer;
	CCMenu mainMenu;
	
	static CCScene scene;
	public static CCScene scene()
	{
		scene = CCScene.node();
		scene.setTag(0);
		GameStartLayer layer = new GameStartLayer(ccColor4B.ccc4(0, 0, 0, 255));
		layer.setTag(0);
		//layer.getLabel().setString(message);
		
		scene.addChild(layer);
		
		return scene;
	}
	
	public CCLabel getLabel()
	{
		return _label;
	}
	
	@SuppressWarnings("deprecation")
	protected GameStartLayer(ccColor4B color)
	{
		super(color);
		
		this.setIsTouchEnabled(true);
		
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
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("imagesStart.plist");
		
		tomatoFighter = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).tomatoFighter;
		FB = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).FB;
		mCamera = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).mCamera;
		isFirstRun = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).firstLaunch;
		
		winSize = CCDirector.sharedDirector().displaySize();
		
		if(tomatoFighter!=null)
		{
			Log.e("save","tomatoFighter exists");
		}
		else
		{
			Log.e("save","tomatoFighter doesn't exists");
		}
		//_label = CCBitmapFontAtlas.bitmapFontAtlas("Tomato Shoot!", "Arial.fnt");
		_label = CCLabel.makeLabel(" ", "DroidSansFallback",50);
		//∫∆®gµf≠XÆg¿ª°I°I°I
		_label.setColor(ccColor3B.ccORANGE);
		//_label.setScale(1.5f);
		_label.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.8f));
		//addChild(_label,100);
		
		newGame = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_new_1.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_new_2.png")), this, "clickMainMenu");
		newGame.setScale(winSize.height/(7f*newGame.getContentSize().height));
		newGame.setTag(0);
		continueGame = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_continue_1.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_continue_2.png")), this, "clickMainMenu");
		continueGame.setScale(winSize.height/(7f*continueGame.getContentSize().height));
		continueGame.setTag(1);
		quick = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_quick_1.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_quick_2.png")), this, "clickMainMenu");
		quick.setScale(winSize.height/(7f*quick.getContentSize().height));
		quick.setTag(2);
		setting = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_setting_1.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_setting_2.png")), this, "clickMainMenu");
		setting.setScale(winSize.height/(7f*setting.getContentSize().height));
		setting.setTag(3);		
		help = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_help_1.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_help_2.png")), this, "clickMainMenu");
		help.setScale(winSize.height/(7f*help.getContentSize().height));
		help.setTag(4);
		about = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_about_1.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_about_2.png")), this, "clickMainMenu");
		about.setScale(winSize.height/(7f*about.getContentSize().height));
		about.setTag(5);		
		FB_btn = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_fb.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_fb.png")), this, "clickMainMenu");
		FB_btn.setTag(6);
		FB_btn.setScale(winSize.height/(4f*FB_btn.getContentSize().height));		
		/*
		process_btn = CCMenuItemSprite.item(
				CCSprite.sprite("process.png"), 
				CCSprite.sprite("process.png"), this, "clickTool");
		process_btn.setTag(1);
		rank_btn = CCMenuItemSprite.item(
				CCSprite.sprite("ranking.png"), 
				CCSprite.sprite("ranking.png"), this, "clickTool");
		rank_btn.setTag(2);
		rank_btn.setScale(0.6f);
		rank_btn.setVisible(false);
		music_btn = CCMenuItemSprite.item(
				CCSprite.sprite("music.png"), 
				CCSprite.sprite("music.png"), this, "clickTool");
		music_btn.setTag(3);
		music_btn.setScale(0.6f);
		music_btn.setVisible(false);
		store_btn = CCMenuItemSprite.item(
				CCSprite.sprite("store.png"), 
				CCSprite.sprite("store.png"), this, "clickTool");
		store_btn.setTag(4);
		store_btn.setScale(0.6f);
		store_btn.setVisible(false);
		arrow_btn = CCMenuItemSprite.item(
				CCSprite.sprite("arrow.png"), 
				CCSprite.sprite("arrow.png"), this, "clickNet");
		arrow_btn.setTag(5);
		youtube_btn = CCMenuItemSprite.item(
				CCSprite.sprite("youtube.png"), 
				CCSprite.sprite("youtube.png"), this, "clickNet");
		youtube_btn.setTag(6);
		youtube_btn.setScale(0.6f);
		youtube_btn.setVisible(false);
		FB_btn = CCMenuItemSprite.item(
				CCSprite.sprite("FB.png"), 
				CCSprite.sprite("FB.png"), this, "clickNet");
		FB_btn.setTag(7);
		FB_btn.setScale(0.6f);
		FB_btn.setVisible(false);
		*/
		if(isFirstRun)
		{
			mainMenu = CCMenu.menu(newGame,quick,setting,help,about);
		}
		else
		{
			mainMenu = CCMenu.menu(continueGame,quick,setting,help,about);
		}
		mainMenu.setColor(ccColor3B.ccc3(255,255,255));
		mainMenu.setPosition(CGPoint.ccp(winSize.width * 0.7f, winSize.height * 0.4f));
		mainMenu.alignItemsVertically(4);		
		addChild(mainMenu,100);	
		CCMenu fbMenu = CCMenu.menu(FB_btn);		
		fbMenu.setColor(ccColor3B.ccc3(255,255,255));
		fbMenu.setPosition(CGPoint.ccp(winSize.width * 0.9f, winSize.height * 0.12f));
		addChild(fbMenu,100);
		/*
		CCMenu toolMenu = CCMenu.menu(store_btn,music_btn,rank_btn,process_btn);		
		toolMenu.setColor(ccColor3B.ccc3(255,255,255));
		toolMenu.setPosition(CGPoint.ccp(winSize.width * 0.1f, winSize.height * 0.25f));
		toolMenu.alignItemsVertically(5);
		addChild(toolMenu,102);
		
		CCMenu netMenu = CCMenu.menu(FB_btn,youtube_btn,arrow_btn);		
		netMenu.setColor(ccColor3B.ccc3(255,255,255));
		netMenu.setPosition(CGPoint.ccp(winSize.width * 0.9f, winSize.height * 0.22f));
		netMenu.alignItemsVertically(5);
		addChild(netMenu,102);	
		*/
		
		CCSprite title = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("index_title.png"));
		title.setPosition(winSize.width*0.7f, winSize.height*0.9f);
		title.setScale(winSize.height/(4.5f*title.getContentSize().height));		
		addChild(title,4);
		
		CCSprite star = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("star.png"));
		star.setPosition(winSize.width*0.92f, winSize.height*0.88f);
		star.setScale(winSize.height/(10*star.getContentSize().height));
		star.runAction(CCRepeatForever.action(
				CCSequence.actions(
					CCFadeOut.action(1.2f),
					CCFadeIn.action(1.2f)
				)));
		addChild(star,1);
		CCSprite star2 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("star.png"));
		star2.setPosition(winSize.width*0.89f, winSize.height*0.65f);
		star2.setScale(winSize.height/(10*star.getContentSize().height));	
		star2.runAction(CCRepeatForever.action(
				CCSequence.actions(
					CCFadeOut.action(1f),
					CCFadeIn.action(1f)
				)));
        addChild(star2,1);
        CCSprite star3 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("star.png"));
        star3.setPosition(winSize.width*0.65f, winSize.height*0.8f);
        star3.setScale(winSize.height/(11*star.getContentSize().height));	
        star3.runAction(CCRepeatForever.action(
				CCSequence.actions(
					CCFadeOut.action(0.8f),
					CCFadeIn.action(0.8f)
				)));
        addChild(star3,1);
        CCSprite star4 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("star.png"));
        star4.setPosition(winSize.width*0.7f, winSize.height*0.55f);
        star4.setScale(winSize.height/(11*star.getContentSize().height));	
        star4.runAction(CCRepeatForever.action(
				CCSequence.actions(
					CCFadeOut.action(0.6f),
					CCFadeIn.action(0.6f)
				)));
        addChild(star4,1);
        CCSprite star5 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("star.png"));
        star5.setPosition(winSize.width*0.92f, winSize.height*0.42f);
        star5.setScale(winSize.height/(8*star.getContentSize().height));
        star5.runAction(CCRepeatForever.action(
				CCSequence.actions(
					CCFadeOut.action(0.4f),
					CCFadeIn.action(0.4f)
				)));
		addChild(star5,1);
		CCSprite star6 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("star.png"));
		star6.setPosition(winSize.width*0.7f, winSize.height*0.18f);
		star6.setScale(winSize.height/(9*star.getContentSize().height));	
		star6.runAction(CCRepeatForever.action(
				CCSequence.actions(
					CCFadeOut.action(0.5f),
					CCFadeIn.action(0.5f)
				)));
        addChild(star6,1);
        CCSprite star7 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("star.png"));
        star7.setPosition(winSize.width*0.42f, winSize.height*0.23f);
        star7.setScale(winSize.height/(8*star.getContentSize().height));	
        star7.runAction(CCRepeatForever.action(
				CCSequence.actions(
					CCFadeOut.action(0.3f),
					CCFadeIn.action(0.3f)
				)));
        addChild(star7,1);
        
		CCSprite planet = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("planet.png"));
		planet.setPosition(winSize.width*0.2f, winSize.height*0.8f);
		planet.runAction(CCRepeatForever.action(CCRotateBy.action(13, 360)));
		
		
		CCSprite ship = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("ship.png"));
		ship.setScale(winSize.height/(7*ship.getContentSize().height));
		ship.setPosition(ship.getBoundingBox().size.width*1.1f, 0);//200
		ship.setRotation(90);
		ship.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCScaleTo.action(4, 0.1f), 
						CCScaleTo.action(4, 1.0f)
						)));
		ship.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCMoveBy.action(0.1f, CGPoint.ccp(-3,-1)),
						CCMoveBy.action(0.1f, CGPoint.ccp(3,1))
						))
						);
		
		ArrayList<CCSpriteFrame> shipfire_aArr = new ArrayList<CCSpriteFrame>();
		for(int i = 1; i <= 2; i++) {		    
			shipfire_aArr.add(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("shipfire_a_"+i+".png"));
		}
		CCAnimation shipfire_aAnimation = CCAnimation.animation("energy_ball_aAnimation", 0.5f, shipfire_aArr);
		
		CCSprite shipfire = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("shipfire_a_1.png"));
		shipfire.setPosition(ship.getBoundingBox().size.width*2.7f,ship.getBoundingBox().size.height*0.2f);
		shipfire.setAnchorPoint(CGPoint.ccp(0, 0.5f));
		shipfire.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCMoveBy.action(1f, CGPoint.ccp(winSize.width*0.03f,0)),
						CCMoveBy.action(1f, CGPoint.ccp(-winSize.width*0.03f,0))
						)));
		shipfire.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCScaleTo.action(0f, 1.5f),
						CCDelayTime.action(0.5f),
						CCScaleTo.action(0f, 1.0f),
						CCDelayTime.action(0.5f)
						)));
		shipfire.runAction(CCRepeatForever.action(
				CCAnimate.action(shipfire_aAnimation, false)));
		ship.addChild(shipfire);
		
		
		CCNode basePoint = CCNode.node();
		basePoint.setPosition(winSize.width*0.6f, winSize.height*0.5f);
		basePoint.setRotation(60);
		basePoint.addChild(ship);
		basePoint.runAction(CCRepeatForever.action(CCRotateBy.action(8, -360)));
		basePoint.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCMoveBy.action(4f, CGPoint.ccp(-winSize.width*0.3f,winSize.height*0.1f)),
						CCMoveBy.action(4f, CGPoint.ccp(winSize.width*0.3f,-winSize.height*0.1f))
						)));
		
		addChild(basePoint,3);
		 
		addChild(planet,2);
        
        ;
        //addChild(bgParallax);

		processOpen = false;
		arrowOpen = false;
		if(FB.is_login())
			friendMenu();
		/*
		Camera.Parameters parameters = mCamera.getParameters();
    	parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA); 
        mCamera.setParameters(parameters);       
		*/
        playBMG();
    }
	public static void playBMG()
	{
		 SoundEngine.sharedEngine().realesAllSounds();
	     SoundEngine.sharedEngine().setSoundVolume(100f);
		 SoundEngine.sharedEngine().playSound(CCDirector.sharedDirector().getActivity(), R.raw.this_is_honkstep, true);		
		
	}
	
	public void gameStartDone()
	{
		//CCDirector.sharedDirector().replaceScene(GameLayer.scene());
		CCDirector.sharedDirector().end();
	}
	public void clickMainMenu(Object sender)
	{		
		CCMenuItem item = (CCMenuItem) sender;
		
		if(item.getTag() == 0 ) //new game
		{
			((TomatoshootGame)CCDirector.sharedDirector().getActivity()).newFighter();
			//((TomatoshootGame)CCDirector.sharedDirector().getActivity()).firstLaunch = false;
			//CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationRightOver));
			//CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(1, StoreLayer.scene(), tOrientation.kOrientationRightOver));
			//CCDirector.sharedDirector().replaceScene(SetGameItemLayer.scene());
		}
		else if(item.getTag() == 1 )  //continue game
		{
			CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationRightOver));
			
		}
		else if(item.getTag() == 2 )  //quick play
		{
			CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationRightOver));
			
		}
		else if(item.getTag() == 3 )  //setting
		{
			CCDirector.sharedDirector().pushScene(volumeAdjustLayer.scene());
		}
		else if(item.getTag() == 4 )  //help
		{
			CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationRightOver));
			
		}
		else if(item.getTag() == 5 )  //about
		{
			CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationRightOver));
			
		}
		else if(item.getTag() == 6 )  //FB
		{
			DialogListener fbDialog=new DialogListener() {
	            @Override
	            public void onComplete(Bundle values) {
	            	//FB.get_me() is required
	            	//Log.e("fbbbbbbb",FB.get_me().toString());  
	            	IdList = FB.get_friendFieldArray("id");
	            	NameList = FB.get_friendFieldArray("name");
	            	friendMenu();
	            	
	            }

	            @Override
	            public void onFacebookError(FacebookError error) {
	            	//Log.e("fbbbbbbb","login error:" + error.toString() );
	            }

	            @Override
	            public void onError(DialogError e) {
	            	//Log.e("fbbbbbbb","error:"+ e.toString());
		        }

	            @Override
	            public void onCancel() {
	            	//Log.e("fbbbbbbb","login: cancel");
		        }
	        };
	        //Log.e("@@@@", (FB==null)? "null" :"not null");
			FB.login(fbDialog);
		}
	}
	public void clickTool(Object sender){
		CCMenuItem item = (CCMenuItem) sender;
		if(item.getTag() == 1 ) 
		{
			if(processOpen == false){
				item.setRotation(0);
				item.runAction(CCRotateBy.action(0.5f, 360));
				this.store_btn.setVisible(true);
				this.music_btn.setVisible(true);
				this.rank_btn.setVisible(true);
				processOpen = true;
			}
			else{
				item.runAction(CCRotateBy.action(0.5f, -360));
				this.store_btn.setVisible(false);
				this.music_btn.setVisible(false);
				this.rank_btn.setVisible(false);
				processOpen = false;
			}
			
		}
		else if(item.getTag() == 2 ) 
		{
			
		}
		else if(item.getTag() == 3 ) 
		{
			//CCDirector.sharedDirector().replaceScene(volumeAdjustLayer.scene());
			CCDirector.sharedDirector().pushScene(volumeAdjustLayer.scene());
		}
		else if(item.getTag() == 4 ) 
		{
			
		}
	}
	public void clickNet(Object sender){
		CCMenuItem item = (CCMenuItem) sender;
		if(item.getTag() == 5){
			if(arrowOpen == false){
				item.setRotation(0);
				item.runAction(CCRotateBy.action(0.5f,180));
				this.FB_btn.setVisible(true);
				this.youtube_btn.setVisible(true);
				arrowOpen = true;
			}
			else{
				item.runAction(CCRotateBy.action(0.5f,-180));
				this.FB_btn.setVisible(false);
				this.youtube_btn.setVisible(false);
				arrowOpen = false;
			}
		}
		else if(item.getTag() == 6){
			
		}
		else if(item.getTag() == 7){
			_label.setString("facebook");					
			DialogListener fbDialog=new DialogListener() {
	            @Override
	            public void onComplete(Bundle values) {
	            	//FB.get_me() is required
	            	//Log.e("fbbbbbbb",FB.get_me().toString());  
	            	IdList = FB.get_friendFieldArray("id");
	            	NameList = FB.get_friendFieldArray("name");
	            	friendMenu();
	            	
	            }

	            @Override
	            public void onFacebookError(FacebookError error) {
	            	//Log.e("fbbbbbbb","login error:" + error.toString() );
	            }

	            @Override
	            public void onError(DialogError e) {
	            	//Log.e("fbbbbbbb","error:"+ e.toString());
		        }

	            @Override
	            public void onCancel() {
	            	//Log.e("fbbbbbbb","login: cancel");
		        }
	        };
	        //Log.e("@@@@", (FB==null)? "null" :"not null");
			FB.login(fbDialog);
		}
	}
	@SuppressWarnings("deprecation")
	public void friendMenu(){
		IdList = FB.get_friendFieldArray("id");
    	NameList = FB.get_friendFieldArray("name");
		CCMenuItemSprite[] friendItem = new CCMenuItemSprite[5];
		for(int i = 0;i < 5;i++){
			try {
				img_value = new URL("http://graph.facebook.com/" + this.IdList.get(i) + "/picture");
				mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			friendItem[i] = CCMenuItemSprite.item(
					CCSprite.sprite(mIcon1), 
					CCSprite.sprite(mIcon1), this, "clickStart");
			friendItem[i].setTag(i+2);
			
		}
		CCMenu friendMenu = CCMenu.menu(friendItem);		
		friendMenu.setColor(ccColor3B.ccc3(255,255,255));
		friendMenu.setPosition(CGPoint.ccp(winSize.width * 0.31f, 50.0f));
		friendMenu.alignItemsHorizontally(5);
		addChild(friendMenu,102);	
		
	}
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
		//gameOverDone();
		//CCDirector.sharedDirector().replaceScene(GameLayer.scene());
		
		return true;
	}
	@Override
	public void onExit() {
		//CCSpriteFrameCache.sharedSpriteFrameCache().removeSpriteFrames();		
		super.onExit();
		
	}
}
