package org.mirlab.tomatoshoot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
//import java.lang.Math;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.camera.CCOrbitCamera;
import org.cocos2d.actions.grid.CCShaky3D;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.instant.CCCallFuncND;
import org.cocos2d.actions.instant.CCToggleVisibility;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCFadeTo;
import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCProgressFromTo;
import org.cocos2d.actions.interval.CCProgressTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.actions.interval.CCTintBy;
import org.cocos2d.actions.interval.CCTintTo;
import org.cocos2d.events.CCKeyDispatcher;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemFont;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCParallaxNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.opengl.CCDrawingPrimitives;
import org.cocos2d.particlesystem.CCParticleExplosion;
import org.cocos2d.particlesystem.CCParticleFire;
import org.cocos2d.particlesystem.CCParticleFireworks;
import org.cocos2d.particlesystem.CCParticleFlower;
import org.cocos2d.particlesystem.CCParticleSpiral;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.cocos2d.types.ccGridSize;
import org.mirlab.SpeechRecognition.NativeasraInt;
import org.mirlab.SpeechRecognition.pitchRecorder;
import org.mirlab.SpeechRecognition.wordRecRecorder;
import org.mirlab.tomatoshoot.R;
import org.mirlab.tomatoshoot.EnemyBug.Scripts;
import org.mirlab.tomatoshoot.EnemyBug.States;
import org.mirlab.tomatoshoot.EnemyBug.EnemyTypes;
import org.mirlab.tomatoshoot.EnemyBug.PathTypes;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class GameLayerLV15 extends GameLayerBase
{
	
	ArrayList<CCSpriteFrame> fly_bossB_aArr;
	CCAnimation fly_bossB_aAnimation;
	//set level scriptlists
	//ArrayList<ArrayList<Scripts>> scriptLists = new ArrayList<ArrayList<Scripts>>(4);
	Scripts[][] scriptListsA= {
			//	circle
			{Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.RIGHT,Scripts.RIGHT,
				Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP,Scripts.UP,Scripts.UP_LEFT,Scripts.UP_LEFT,Scripts.LEFT,Scripts.LEFT}
	};
	Scripts[][] scriptListsB= {
			// M-word
			{Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,
				Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN},
			// W-word
			{Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,
				Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,
				Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,
				Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT},
			// Z-word 
			{Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,
				Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT},
			// Z'-word
			{Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,
				Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT},
	};
	Scripts[][] scriptListsDepth= {
			{Scripts.TOWARD,Scripts.TOWARD,Scripts.TOWARD,Scripts.TOWARD},
			{Scripts.AWAY,Scripts.AWAY,Scripts.TOWARD,Scripts.TOWARD,Scripts.TOWARD,Scripts.TOWARD},
			{Scripts.TOWARD,Scripts.TOWARD,Scripts.AWAY,Scripts.TOWARD,Scripts.TOWARD,Scripts.AWAY},
			{Scripts.TOWARD,Scripts.AWAY,Scripts.TOWARD,Scripts.AWAY,Scripts.TOWARD,Scripts.TOWARD}
	};
	static CCScene scene;
	public static CCScene scene()
	{
		scene = CCScene.node();
		scene.setTag(2);
		CCColorLayer layer = new GameLayerLV15(ccColor4B.ccc4(0, 0, 0, 0));
		//scene.setVisible(false);
		scene.addChild(layer);
		
		return scene;
	}
	protected GameLayerLV15(ccColor4B color)
	{
		super(color);		

		
		

	}
	@Override
	public void initContents()
	{
		 super.initContents();

		 CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(textureFolderPath+"fly_worm_a.plist");
			fly_worm_aArr = new ArrayList<CCSpriteFrame>();
			for(int i = 1; i <= 6; ++i) {		    
				fly_worm_aArr.add(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("fly_worm_a_"+i+".png"));
			}
			fly_worm_aAnimation = CCAnimation.animation("fly_worm_aAnimation", 0.05f, fly_worm_aArr);
			
			
			
			fly_bossB_aArr = new ArrayList<CCSpriteFrame>();
			for(int i = 1; i <= 2; ++i) {		    
				fly_bossB_aArr.add(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("BOSSB_"+i+".png"));
			}
			fly_bossB_aAnimation = CCAnimation.animation("fly_bossB_aAnimation", 0.05f, fly_bossB_aArr);
			
		 
		 
		 
		float initHP_A = 55;
		float initHP_B = 2000;
		float killCounts_A = initHP_A/shoo_attack_power_base;
		float killCounts_B = initHP_B/shoo_attack_power_base;
		float finalHP_A = (float) (shoo_attack_power_base*(killCounts_A+loop_no*0.5));
		float finalHP_B = (float) (shoo_attack_power_base*(killCounts_B+loop_no*0.5));
		
		addBugs(EnemyTypes.BUGA, PathTypes.B, finalHP_A,1,2);
		//addBugs(EnemyTypes.BUGA, PathTypes.A, finalHP_A,2,4);
		
		//addBugs(EnemyTypes.BUGB, PathTypes.B, finalHP_A,1,2);
		//addBugs(EnemyTypes.BUGB, PathTypes.A, finalHP_A,2,2);
		
		addBugs(EnemyTypes.BUGA, PathTypes.A, finalHP_A,2,2);
		//addBugs(EnemyTypes.BUGC, PathTypes.A, finalHP_A,3,5);


		
		addBugs(EnemyTypes.BOSSB, PathTypes.B, finalHP_B,1,0);
		
	}
	@Override
	protected void addBugs(EnemyTypes etype, PathTypes ptype, float eHP, int ezSPD, int exySPD)
	{
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		Random rand = new Random();
		//EnemyBug target = EnemyBug.sprite("fly_worm.png");
		EnemyBug target = new EnemyBug();
		
		//	change picture of enemies
		if (etype==EnemyTypes.BUGA)
			target.bugSprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("fly_worm_a_1.png"));
		else if(etype==EnemyTypes.BUGB)
			target.bugSprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("bugb.png"));
		else if(etype==EnemyTypes.BUGC)
			target.bugSprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("bugc.png"));
		else 
			target.bugSprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("BOSSB_1.png"));
		
		
		addChild(target.bugSprite,-2);
		target.indicatorSprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("up.png")); //init indicator rect(sprite)
		target.indicatorSprite.setScale(winSize.height/(20f*target.indicatorSprite.getContentSize().height));
		addChild(target.circleProgress,-1);
		
		target.azimuth = rand.nextInt(260)+50;
		target.roll = rand.nextInt(101)+30; //-90<=roll<=90//30~131
		
		if (ptype==PathTypes.A)
			target.scriptLists = scriptListsA;   //point to the level's scripts
		else
			target.scriptLists = scriptListsB;
		target.scriptListsDepth = scriptListsDepth;
		
	
		target.zspeed = ezSPD;
		target.xyspeed = exySPD;
		target.MAXHEALTH = eHP;
		target.health_points = target.MAXHEALTH;
		
		target.distance = rand.nextInt(100)+100;
		//target.distance = rand.nextInt(500);//0~500
		if(maxScale == 0)
		maxScale = winSize.height/(2f*target.bugSprite.getContentSize().height);
		
		target.bugSprite.setScale(maxScale * (target.distance * (0.05f-1)/MAXDISTOFBUG + 1));
		target.bugSprite.setPosition(winSize.width+100,winSize.height+100); //set it off-screen at first
		
		if (etype==EnemyTypes.BUGA){
			CCAction fly_worm_aAction = CCRepeatForever.action(CCAnimate.action(fly_worm_aAnimation, false));
			target.bugSprite.runAction(fly_worm_aAction);
		}
		else{
			CCAction fly_bossB_aAction = CCRepeatForever.action(CCAnimate.action(fly_bossB_aAnimation, false));
			target.bugSprite.runAction(fly_bossB_aAction);
		}
		//target.bugSprite.setTag(0); //alive
		
		_bugs.add(target);
		
		
	}
}
