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
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.instant.CCCallFuncND;
import org.cocos2d.actions.instant.CCShow;
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
import org.cocos2d.actions.interval.CCScaleBy;
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
import org.cocos2d.particlesystem.CCParticleMeteor;
import org.cocos2d.particlesystem.CCParticleSnow;
import org.cocos2d.particlesystem.CCParticleSpiral;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.particlesystem.CCQuadParticleSystem;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.cocos2d.types.ccColor4F;
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
import android.hardware.Camera;
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

public class GameLayerBase extends CCColorLayer
{
	protected TomatoFighter tomatoFighter;
	Camera mCamera;
	//-----------GameVariables//
	int level_no = 0;
	int loop_no = 0;
	
	int shoo_attack_power = 25;
	int shoo_particle_speed = 100;
	
	float bong_cooldown_time= 3.0f; 
	float energy_cooldown_time= 10.0f; 
	float super_cooldown_time = 6f;
	//--------------------------//
	
	protected ArrayList<EnemyBug> _bugs;
	ArrayList<CGPoint> bug_points = new ArrayList<CGPoint>();
	Vibrator v;
	//protected ArrayList<CCSprite> _projectiles;
	protected int _bugKilled;
	static final float  BUGMOVEUPDATEFREQ = 0.1f;
	static final int  MAXDISTOFBUG = 500;
	static float DISTANCE_RADAR_RATIO = 1f;
	static float ENERGY_BALL_DEFAULT_SCALE = 0.0f;
	static int MAX_ENEMIES_ONSCREEN = 12;
	static Random rand = new Random();
	static String textureFolderPath;
	CGPoint bug_CGPoints[] = new CGPoint[MAX_ENEMIES_ONSCREEN];
	boolean energyFlags[] = {false,false,false,false,false};
	float energyFlagsInt[] = {0,0};
	
	int	bug_CGPoints_size = 0;
	private SensorManager sensorMan;
	public CCBitmapFontAtlas _label;
	public CCBitmapFontAtlas cannon_status;
	public volatile int direction =  0;
	public volatile int inclination;
	float pitch,roll,azimuth;
	float x,y,z;
	float currentPitch = 0;
	int currentEnergyIndex = 0;
	boolean gamePaused = false;
	
	int shoo_selected_idx = 0;
	int bong_selected_idx = 0;
	int energyLevelState = 0;
	float maxScale = 0;
	float maxScaleForProjectile = 0;
	float playerHP;
	//boolean energyBallEnabled = true;
	int energyBallCoolDownTimer = 0;
	int superPowerCoolDownTimer = 0;
	
	
	//CCSpriteSheet fly_worm_aSpriteSheet;
	ArrayList<CCSpriteFrame> fly_worm_aArr;
	CCAnimation fly_worm_aAnimation;
	ArrayList<CCSpriteFrame> energy_ball_aArr;
	CCAnimation energy_ball_aAnimation;
	ArrayList<CCSpriteFrame> explosion_Arr;
	CCAnimation explosion_Animation;
	ArrayList<CCSpriteFrame> fire_Arr;
	CCAnimation fire_Animation;
	
	ArrayList<EnemyBug> bugsToDelete = new ArrayList<EnemyBug>();		
	ArrayList<RealWorldObject> realWorldObjectsToDelete = new ArrayList<RealWorldObject>();
	ArrayList<RealWorldObject> projectiles_Arr = new ArrayList<RealWorldObject>();
	//CCAction fly_worm_aAction;
		
	//CCNode hudNode;
	CCParallaxNode hudNode;
	CGPoint initialHudPos;
	CCSprite hudWindow;
	CCSprite screenRed;
	CCSprite pitchPointer;
	CCSprite energyBall;	
	CCSprite buddhaAngle;
	CCSprite dragonDevil;
	CCSprite pineApple;
	CCSprite FBimage;
	CCParticleSystem	energyEmitter;
	CCParticleSystem smokeEmitter,snowEmitter,fireEmitter,smokeBall;
	PauseScreenLayer pauseScreenLayer;
	CCMenuItem radioItem;
    
	
	RadarCCSprite radarSprite;
	CCSprite cannon;
	CCSprite cannon_flash;
	CCSprite cannon_weapon;
	//CCProgressTimer cannonProgress;
	CCProgressTimer cannonPointer;
	CCProgressTimer hp_progress;
	CCProgressTimer energy_progress;
	CCProgressTimer cooldown_progress;
	CGSize winSize;
	Context context = CCDirector.sharedDirector().getActivity();
	
	public pitchRecorder pitchRecorder;
	public Thread pitchThread;
	public wordRecRecorder wordRecRecorder;
	public Thread wordRecThread;
	public Thread masterThread;
	
	public static int volThreshold;
	public static int meanVol;
	final int SCREEN_DENSITY;
	static CCScene scene;
	public static CCScene scene()
	{
		scene = CCScene.node();
		scene.setTag(2);
		CCColorLayer layer = new GameLayerBase(ccColor4B.ccc4(0, 0, 0, 0));
		//scene.setVisible(false);
		scene.addChild(layer);
		
		return scene;
	}

	protected GameLayerBase(ccColor4B color)
	{
		super(color);
		
		tomatoFighter = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).tomatoFighter;
		mCamera = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).mCamera;
		
		_bugs = new ArrayList<EnemyBug>();
		//_projectiles = new ArrayList<CCSprite>();
		_bugKilled = 0;
		
		playerHP = 100;
		
		winSize = CCDirector.sharedDirector().displaySize();		
		
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
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(textureFolderPath+"energy_ball_a.plist");
		energy_ball_aArr = new ArrayList<CCSpriteFrame>();
		for(int i = 5; i <= 33; i+=2) {		    
			energy_ball_aArr.add(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("energy_ball_a_"+i+".png"));
		}
		energy_ball_aAnimation = CCAnimation.animation("energy_ball_aAnimation", 0.1f, energy_ball_aArr);
		
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("explosion.plist");
		explosion_Arr = new ArrayList<CCSpriteFrame>();
		for(int i = 1; i <= 3; ++i) {		    
			explosion_Arr.add(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("Explosion"+i+".png"));
		}
		explosion_Animation = CCAnimation.animation("explosion_Animation", 0.5f, explosion_Arr);
		
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(textureFolderPath+"animated_fire_a.plist");
		fire_Arr = new ArrayList<CCSpriteFrame>();
		for(int i = 0; i <= 4; ++i) {		    
			fire_Arr.add(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("animated_fire_a_"+i+".png"));
		}
		fire_Animation = CCAnimation.animation("fire_Animation", 0.2f, fire_Arr);
		
				
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("imagesGame.plist");
		
		//CCTextureCache.sharedTextureCache().addImage("bong.png");
		
		// Handle sound
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.flak_exp);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.gunreload);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.flak_shot);
		
		pauseScreenLayer = new PauseScreenLayer(ccColor4B.ccc4(0, 0, 0, 0));
		// create a void node, a parent node
		hudNode = CCParallaxNode.node();
		//hudNode = CCNode.node();
		initialHudPos = hudNode.getPosition();
		addChild(hudNode,50);
		screenRed = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("screen_red.png"));		
		screenRed.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		screenRed.setScaleX(winSize.width/screenRed.getContentSize().width);
		screenRed.setScaleY(winSize.height/screenRed.getContentSize().height);
		screenRed.setOpacity(125);
		addChild(screenRed,-20);//bottom
		screenRed.setVisible(false);
		
		float screenshake_offset = 0.3f;
		//cannon = CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("cannon.png"));	
		cannon = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("cannon.png"));
		//cannon.setPosition(CGPoint.ccp(winSize.width * 0.5f, cannon.getBoundingBox().size.height / 2.0f));		
		hudNode.addChild(cannon,0,1.4f+screenshake_offset, 1.4f+screenshake_offset, winSize.width * 0.5f, cannon.getBoundingBox().size.height / 2.0f);
		/*
		cannonProgress = CCProgressTimer.progress("cannon_energy_mountup.png");
		cannonProgress.setType(CCProgressTimer.kCCProgressTimerTypeVerticalBarBT);
		cannonProgress.setScale(cannon.getContentSize().height/(1.0f*cannonProgress.getContentSize().height));
		cannonProgress.setPosition(CGPoint.ccp(cannon.getPosition().x-cannon.getBoundingBox().size.width*0.7f, cannon.getPosition().y-cannon.getBoundingBox().size.height*0.5f));
		hudNode.addChild(cannonProgress,2,1.4f+screenshake_offset, 1.4f+screenshake_offset,cannon.getPosition().x,cannon.getPosition().y);
		*/
		cannonPointer = CCProgressTimer.progress("cannon_pointer.png");
		cannonPointer.setType(CCProgressTimer.kCCProgressTimerTypeVerticalBarBT);
		cannonPointer.setScale(cannon.getContentSize().height/(1.0f*cannonPointer.getContentSize().height));
		cannonPointer.setPosition(CGPoint.ccp(cannon.getPosition().x-cannon.getBoundingBox().size.width*0.7f, cannon.getPosition().y-cannon.getBoundingBox().size.height*0.5f));
		hudNode.addChild(cannonPointer,1,1.4f+screenshake_offset, 1.4f+screenshake_offset,cannon.getPosition().x,cannon.getPosition().y);
		
		
		
		cannon_flash = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("cannon_flash.png"));		
		//cannon_flash.setPosition(CGPoint.ccp(winSize.width * 0.5f, cannon.getBoundingBox().size.height*1.0f));	
		cannon_flash.setOpacity(0);
		hudNode.addChild(cannon_flash,5,1.4f+screenshake_offset, 1.4f+screenshake_offset, winSize.width * 0.5f, cannon.getBoundingBox().size.height*1.0f);
		
		cannon_weapon = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("corn_cannon.png"));		
		cannon_weapon.setScale(cannon.getContentSize().height/(4.0f*cannon_weapon.getContentSize().height));
		cannon_weapon.setRotation(-45f);
		//cannon_weapon.setPosition(CGPoint.ccp(cannon.getPosition().x-cannon.getBoundingBox().size.width*0.3f, cannon.getPosition().y-cannon.getBoundingBox().size.height*0.01f));
		hudNode.addChild(cannon_weapon,-1,1.4f+screenshake_offset, 1.4f+screenshake_offset, cannon.getPosition().x-cannon.getBoundingBox().size.width*0.27f, cannon.getPosition().y+cannon.getBoundingBox().size.height*0.01f);
		
		
		energyBall = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("energy_ball_a_5.png"));		
		
		energyEmitter = new CCQuadParticleSystem(300);
        energyEmitter.setTexture(CCTextureCache.sharedTextureCache().addImage("fire.png"));
		energyEmitter.setDuration(CCParticleSystem.kCCParticleDurationInfinity);
		energyEmitter.setEmitterMode(CCParticleSystem.kCCParticleModeGravity);
		energyEmitter.setGravity(CGPoint.ccp(0,0));
		energyEmitter.setSpeed(160);
		energyEmitter.setSpeedVar(20);
		energyEmitter.setRadialAccel(-120);
		energyEmitter.setRadialAccelVar(0);
		energyEmitter.setTangentialAccel(30);
		energyEmitter.setTangentialAccelVar(0);
		energyEmitter.setPosition(CGPoint.ccp(winSize.width/2,winSize.height/2));
		energyEmitter.setPosVar(CGPoint.zero());
		energyEmitter.setAngle(90);
		energyEmitter.setAngleVar(360);
		energyEmitter.setLife(6);
		energyEmitter.setLifeVar(1);
		ccColor4F startColor = new ccColor4F(1.0f, 0.8f, 0.0f, 1.0f);
		energyEmitter.setStartColor(startColor);
		ccColor4F startColorVar = new ccColor4F(0.0f, 0.2f, 0.0f, 0.2f);
		energyEmitter.setStartColorVar(startColorVar);
		ccColor4F endColor = new ccColor4F(1.0f, 1.0f, 1.0f, 0.7f);
		energyEmitter.setEndColor(endColor);
		ccColor4F endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.2f);	
		energyEmitter.setEndColorVar(endColorVar);
		energyEmitter.setStartSize(10.0f);
		energyEmitter.setStartSizeVar(5.0f);
		//energyEmitter.setEndSize(CCParticleSystem.kCCParticleStartSizeEqualToEndSize);
		energyEmitter.setEndSize(6f);
		energyEmitter.setEndSizeVar(5f);
		// emits per second
		energyEmitter.setEmissionRate(energyEmitter.getTotalParticles()/energyEmitter.getLife());
		energyEmitter.setBlendAdditive(false);
		//addChild(energyEmitter);
		
		radarSprite = RadarCCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("radar.png"));
		//radarSprite = RadarCCSprite.sprite("radar.png");
		//radarSprite.setPosition(CGPoint.ccp(radarSprite.getBoundingBox().size.width / 2.0f+winSize.width * 0.07f, radarSprite.getBoundingBox().size.height / 2.0f+winSize.height * 0.12f));
		DISTANCE_RADAR_RATIO = radarSprite.getBoundingBox().size.width/(2*MAXDISTOFBUG);
		radarSprite.setRadarPoints(bug_CGPoints, bug_CGPoints_size);
		radarSprite.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 10, 0, 0));
		hudNode.addChild(radarSprite,15, 0.4f+screenshake_offset, 0.4f+screenshake_offset,radarSprite.getBoundingBox().size.width / 2.0f+winSize.width * 0.065f, radarSprite.getBoundingBox().size.height / 2.0f+winSize.height * 0.08f);
		
		CCSprite hp_frame = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("hp_frame.png"));
		hp_frame.setScale(winSize.height/(4f*hp_frame.getContentSize().height));
		hp_frame.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 14, 0, 0));
		hudNode.addChild(hp_frame,15, 0.4f+screenshake_offset, 0.4f+screenshake_offset,hp_frame.getBoundingBox().size.width * 0.0f+winSize.width * 0.14f, hp_frame.getBoundingBox().size.height * 0.0f+winSize.height * 0.72f);
		hp_progress = CCProgressTimer.progress("hp_full.png");
		hp_progress.setType(CCProgressTimer.kCCProgressTimerTypeVerticalBarBT);
		hp_progress.setScale(winSize.height/(4f*hp_progress.getContentSize().height));
		hp_progress.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 14, 0, 0));
		hudNode.addChild(hp_progress,16, 0.4f+screenshake_offset, 0.4f+screenshake_offset,hp_progress.getBoundingBox().size.width * 0.0f+winSize.width * 0.14f, hp_progress.getBoundingBox().size.height * 0.0f+winSize.height * 0.72f);
		
		energy_progress = CCProgressTimer.progress("energy_level.png");
		energy_progress.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarLR);
		energy_progress.setScale(winSize.width/(7.5f*hp_progress.getContentSize().width));
		hudNode.addChild(energy_progress,16, 0.4f+screenshake_offset, 0.4f+screenshake_offset,winSize.width * 0.495f, winSize.height * 0.76f);
		
		cooldown_progress = CCProgressTimer.progress("cooldown_bar.png");
		cooldown_progress.setType(CCProgressTimer.kCCProgressTimerTypeRadialCW);
		cooldown_progress.setScale(winSize.width/(17.0f*cooldown_progress.getContentSize().width));
		cooldown_progress.setPercentage(100);
		hudNode.addChild(cooldown_progress,16, 0.4f+screenshake_offset, 0.4f+screenshake_offset,winSize.width * 0.33f, winSize.height * 0.05f);
		
		
		CCSprite console = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("console_full.png"));
		console.setScale(1.3f);
		hudNode.addChild(console,15, 0.3f+screenshake_offset, 0.3f+screenshake_offset,winSize.width * 0.5f, -winSize.height * 0.084f);
		
		
		CCSprite crossChair = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("crosschair.png"));		
		//crossChair.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		crossChair.setScale(winSize.height/(10f*crossChair.getContentSize().height));
		hudNode.addChild(crossChair,20, 1.4f+screenshake_offset, 1.4f+screenshake_offset,winSize.width / 2.0f, winSize.height / 2.0f);
		     
        hudWindow = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("window.png"));	
		//hudWindow = CCSprite.sprite("window.png");	
		//hudWindow.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		hudWindow.setScaleX(winSize.width/hudWindow.getContentSize().width);
		hudWindow.setScaleY(winSize.height/hudWindow.getContentSize().height);
		//hudWindow.setOpacity(200);
		hudNode.addChild(hudWindow,10,0.3f+screenshake_offset, 0.3f+screenshake_offset, winSize.width / 2.0f, winSize.height / 2.0f);
		
		
		pitchPointer = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("left.png"));		
		pitchPointer.setScale(cannon.getContentSize().height/(6f*pitchPointer.getContentSize().height)); // 1/6cannon size
		//pitchPointer.setPosition(CGPoint.ccp(cannon.getPosition().x+cannon.getBoundingBox().size.width/2, cannon.getPosition().y-cannon.getBoundingBox().size.height/2));				
		hudNode.addChild(pitchPointer,10,1.4f+screenshake_offset, 1.4f+screenshake_offset, cannon.getPosition().x+cannon.getBoundingBox().size.width/2, cannon.getPosition().y-cannon.getBoundingBox().size.height/2);
		
		cannon_status = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		cannon_status.setColor(ccColor3B.ccc3(54, 206, 224));
		//cannon_status.setPosition(winSize.width * 0.2f, winSize.height * 0.4f);
		hudNode.addChild(cannon_status,15,1.4f+screenshake_offset, 1.4f+screenshake_offset,winSize.width * 0.5f, winSize.height * 0.7f);

		_label = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		_label.setColor(ccColor3B.ccBLACK);
		_label.setPosition(winSize.width * 0.2f, winSize.height * 0.5f);
		addChild(_label);
		
		
		
		/*
		HoldableRadioCCMenu radio_btn = HoldableRadioCCMenu.menu(CCMenuItemImage.item(
				CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("radio_btn.png")), 
						CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("radio_btn.png")), this, "radio_btn_released"));		
		*/
		radioItem = CCMenuItemImage.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("radio_btn_up.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("radio_btn_down.png")), 
				this, "radio_btn_released");
		radioItem.setScale(winSize.height/(5f*radioItem.getContentSize().height));	
		radioItem.setRotation(3f);
		radioItem.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 13, 0, 0));
		
		HoldableRadioCCMenu radio_btn = HoldableRadioCCMenu.menu(radioItem);
		radio_btn.setPosition(CGPoint.ccp(winSize.width * 0.87f, winSize.height * 0.17f));
		addChild(radio_btn,70);
		
		
		
		
		//CCSprite weapon1_btn = CCSprite.sprite("corn_cannon_btn.png");		
		//weapon1_btn.setTextureRect(CGRect.make(CGPoint.make(0, 20), weapon1_btn.getContentSize()));
		//weapon1_btn.setPosition(CGPoint.ccp(0f, 50f));
		//weapon1_btn.setScale(winSize.height/(2f*weapon1_btn.getContentSize().height));
		
		//CCSprite weapon2_btn = CCSprite.sprite("grape_cannon_btn.png");		
		//weapon2_btn.setPosition(CGPoint.ccp(winSize.width * 0.7f, winSize.height * 0.01f));
		//weapon2_btn.setScale(winSize.height/(2f*weapon2_btn.getContentSize().height));
		
		/*
		CCMenuItemSprite weapon1 = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("corn_cannon_btn.png")), 
				CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("corn_cannon_btn.png")), this, "clickWeapon");
		weapon1.setTag(0);
		CCMenuItemSprite weapon2 = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("grape_cannon_btn.png")), 
				CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("grape_cannon_btn.png")), this, "clickWeapon");
		weapon2.setTag(1);*/
		
		CCMenuItemSprite weapon1 = CCMenuItemSprite.item(
				CCSprite.sprite("tool_btn_up.png"), 
				CCSprite.sprite("tool_btn_down.png"), this, "clickBong");
		weapon1.setTag(0);
		weapon1.setScale(winSize.width/(9*weapon1.getContentSize().width));
		
		CCMenuItemSprite weapon2 = CCMenuItemSprite.item(
				CCSprite.sprite("tool_btn_up.png"), 
				CCSprite.sprite("tool_btn_down.png"), this, "clickBong");
		weapon2.setTag(1);
		weapon2.setScale(winSize.width/(9*weapon2.getContentSize().width));
		
		CCMenu weaponMenu = CCMenu.menu(weapon1,weapon2);		
		weaponMenu.setColor(ccColor3B.ccc3(255,255,255));
		weaponMenu.setPosition(CGPoint.ccp(winSize.width * 0.9f, winSize.height * 0.7f));
		weaponMenu.alignItemsVertically(5);
		addChild(weaponMenu,100);		
		
		//CCSprite test = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("cannon.png"));
		//test.setPosition(CGPoint.ccp(winSize.width * 0.9f, winSize.height * 0.7f));
		//addChild(test,101);	
		
		//ohMyGod
		smokeBall = CCParticleFire.node(250);
		smokeBall.setTexture(CCTextureCache.sharedTextureCache().addImage("fire.png"));
		fireEmitter = CCParticleFlower.node(500);
		fireEmitter.setTexture(CCTextureCache.sharedTextureCache().addImage("stars_grayscale.png"));
		fireEmitter.setScale(2.0f * (winSize.width/(25.0f * 32)));

		//Haleiluya
		smokeEmitter = CCParticleMeteor.node(150);
		smokeEmitter.setTexture(CCTextureCache.sharedTextureCache().addImage("fire.png"));
		//emitter.setScale(winSize.height/(emitter.getContentSize().height));
		//smokeEmitter.setAutoRemoveOnFinish(true);
		snowEmitter = CCParticleSnow.node(120);
		snowEmitter.setTexture(CCTextureCache.sharedTextureCache().addImage("stars_grayscale.png"));
		//snowEmitter.setScale(3.0f * (winSize.width/(25.0f * 32)));
		
		buddhaAngle = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("buddhaAngle.png"));
		dragonDevil = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("dragonDevil.png"));
		pineApple = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("pineApple.png"));
		FBimage = CCSprite.sprite("test.jpg");
		//CCDirector.sharedDirector().getOpenGLView().setVisibility(1);	
		// Get instance of Vibrator from current Context
		v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);		 
		
		
		sensorMan = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensorMan.registerListener(listener, sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
		sensorMan.registerListener(listener, sensorMan.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);
		
		pitchRecorder = new pitchRecorder(meanVol,volThreshold,this,1);
		pitchThread = new Thread(pitchRecorder);
		pitchRecorder.setComputeZCR(true);
		pitchRecorder.setComputePitch(true);
		pitchRecorder.setRecording(true);
		pitchThread.start();
		
		if(!new File("/sdcard/recData").exists())
	    	   new File("/sdcard/recData").mkdir();
		if(!new File("/sdcard/recData/output").exists())
	    	   new File("/sdcard/recData/output").mkdir();
		CopyFromAssets("chinese.dic","/sdcard/recData/chinese.dic");
		CopyFromAssets("chinese.macb","/sdcard/recData/chinese.macb");
		CopyFromAssets("chinese.vc.prm","/sdcard/recData/chinese.vc.prm");
		CopyFromAssets("output.net","/sdcard/recData/output.net");
		CopyFromAssets("output.netb","/sdcard/recData/output.netb");
		CopyFromAssets("output.syl","/sdcard/recData/output.syl");
		CopyFromAssets("output.wpa","/sdcard/recData/output.wpa");
		CopyFromAssets("sqrtTable.bin","/sdcard/recData/sqrtTable.bin");
		CopyFromAssets("tangPoem0010.txt","/sdcard/recData/tangPoem0010.txt");
		CopyFromAssets("targetCm.xml","/sdcard/recData/targetCm.xml");
		wordRecRecorder = new wordRecRecorder(this);
		
		startSchedulers();
		this.setIsTouchEnabled(true);
		this.setIsKeyEnabled(true);
		
		Camera.Parameters parameters = mCamera.getParameters();
    	parameters.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE); 
        mCamera.setParameters(parameters);        
        

	    
	}
	public void startSchedulers()
	{
		this.schedule("gameLogic", 0.5f);
		this.schedule("updatePitch", 0.2f);
		this.schedule("update", 0.05f);//update logics 20fps
		this.schedule("bugMove", BUGMOVEUPDATEFREQ);//update logics 20fps
		
	}
	private void CopyFromAssets(String sourcefileName, String targetfileName){
    	
    	try{
			InputStream in = context.getAssets().open(sourcefileName);
			OutputStream out = new FileOutputStream(targetfileName);
					
			byte[] buf = new byte[4096];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			
		} catch(Exception e){
			e.printStackTrace();     
		}
	}
	public void PrintResult(String outputDir){
		try{
    		FileInputStream fr = new FileInputStream(outputDir + "/recogResult.txt");
    		BufferedReader bufReader = new BufferedReader( new InputStreamReader( fr,"BIG5" ) );
    		String line,name;
    		line=bufReader.readLine();
    		line=bufReader.readLine();
    		name=line.split("=")[1];
    		bufReader.close();
    		fr.close();
    		//textview.setText("辨識結果:\n"+name);
    		//Log.d("辨識結果",name);	
    		
    		if(name.equalsIgnoreCase("歐買尬")){
    			ohMyGod();
    			_label.setString("ohMyGod");
    		}
    		else if(name.equalsIgnoreCase("哈雷路亞")){
    			Haleiluya();
    			_label.setString("Haleiluya");
    		}
    		else if(name.equalsIgnoreCase("不要這樣")){
    			dontDoThat();
    			_label.setString("Don't Do That");
    		}
    		else{
    			_label.setString("XXXXX");
    		}
    	}
    	catch (Exception e){ 
    		e.printStackTrace();
    	}
	}
	public void radio_btn_pressed(Object sender)
	{		
		Log.d("DDDDD","radio_btn_pressed");		
		
		pitchRecorder.setRecording(false);
//		try {
//			pitchThread.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		wordRecThread = new Thread(wordRecRecorder);
//		wordRecRecorder.setwaveFileName(new File("/sdcard/recData/output/rec.wav"));
//		wordRecThread.start();
//		wordRecRecorder.setRecording(true);
		masterThread = new Thread(new Runnable(){
            public void run(){			                
            	NativeasraInt.recog("/sdcard/recData/chinese.vc.prm","/sdcard/recData/output/rec.wav", 
        				"/sdcard/recData/tangPoem0010.txt", "/sdcard/recData/output.syl", 
        				"/sdcard/recData/output.netb", "/sdcard/recData/output", "1",
        				"/sdcard/recData/targetCm.xml");
        		PrintResult("/sdcard/recData/output");
            }
        });
		// Vibrate for 300 milliseconds
		v.vibrate(100);

	}
	public void radio_btn_released(Object sender)
	{		
		Log.d("DDDDD","radio_btn_released");	
		v.vibrate(400);
		wordRecRecorder.setRecording(false);
		
//		try {
//			wordRecThread.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		masterThread.start();
//		NativeasraInt.recog("/sdcard/recData/chinese.vc.prm","/sdcard/recData/output/rec.wav", 
//				"/sdcard/recData/tangPoem0010.txt", "/sdcard/recData/output.syl", 
//				"/sdcard/recData/output.netb", "/sdcard/recData/output", "1",
//				"/sdcard/recData/targetCm.xml");
//		PrintResult("/sdcard/recData/output");
//		pitchThread = new Thread(pitchRecorder);
//		pitchThread.start();
//		pitchRecorder.setRecording(true);
	}
	public void clickBong(Object sender)
	{		
		CCMenuItem item = (CCMenuItem) sender;
		Log.d("DDDDD","testClick"+item.getTag());		
		SoundEngine.sharedEngine().playEffect(context, R.raw.gunreload);
		
		// Pew!
		if(item.getTag() == 0 ) //1st weapon
		{
			//shoo_selected_idx = 0;
			//bong_selected_idx = 0;
			//cannon_weapon.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("corn_cannon.png"));
		}
		if(item.getTag() == 1 ) //2nd weapon
		{
			//shoo_selected_idx = 1;
			//bong_selected_idx = 1;
			//cannon_weapon.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("grape_cannon.png"));
		}
		
		
	}
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
		// Choose one of the touches to work with
		//CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
		
		pauseGame();
		
		return true;
	}
	public void pauseGame()
	{

		if(gamePaused==true)
		{
			return;
		}
		//CCDirector.sharedDirector().pause();
		//CCScene scene = PauseScreenLayer.scene();
		//CCDirector.sharedDirector().pushScene(CCPageTurnTransition.transition(1, scene, true));
		//CCDirector.sharedDirector().pushScene(scene);
		
		this.unscheduleAllSelectors();
		this.setIsTouchEnabled(false);
		
		//scene.setVisible(false);
		addChild(pauseScreenLayer,200, 888);
		pauseScreenLayer.runAction(CCFadeTo.action(0.2f,220));
		gamePaused = true;
		
		pauseScreenLayer.setIsTouchEnabled(true);
		float heightS = pauseScreenLayer.console_full.getBoundingBox().size.height;
		pauseScreenLayer.console_full.runAction(CCSequence.actions(
				CCMoveBy.action(0.05f, CGPoint.ccp(0, heightS*0.4f))
		));
		//fireParticle();
	}
	public void gameLogic(float dt)
	{
		//_label.setString(""+_bugs.size());
		
		if(_bugs.size()==0)
		{
			_bugKilled = 0;
			CCDirector.sharedDirector().replaceScene(GameOverLayer.scene(99,true));
			
		}
		//if(bug_CGPoints_size==0)
		//{	
			
			//addBugs(EnemyTypes.BUGA,100,20,5);
			//addBugs(EnemyTypes.BULLEYE,4,20,0);
		//}		
		if(energyBallCoolDownTimer>0)
		{
			energyBallCoolDownTimer--;
		}
		if(superPowerCoolDownTimer>0)
		{
			superPowerCoolDownTimer--;
			if(superPowerCoolDownTimer == 0)
			{
				radioItem.setIsEnabled(true);
				radioItem.runAction(CCFadeTo.action(0, 255));
				radioItem.runAction(CCRepeatForever.action(CCSequence.actions(
						CCRotateBy.action(0.1f, 20),
						CCRotateBy.action(0.1f, -40),
						CCRotateBy.action(0.1f, 20),
						CCDelayTime.action(3)
						)));
			}
		}		
		
		int shakeoffset = 0;				
		if (currentPitch<20 && currentPitch>0)
        {
			
        	
        }
		else if (currentPitch<40 && energyFlags[0])
        {
        	
        }
        else if (currentPitch<60 && energyFlags[1])
        {
        	shakeoffset = 0;	
        	hudNode.runAction(CCSequence.actions(
    				CCMoveBy.action(0.06f, CGPoint.ccp(10+shakeoffset,-20-shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(-20-shakeoffset,10+shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(20+shakeoffset,20+shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(-10-shakeoffset,-10-shakeoffset)),
    				CCMoveTo.action(0.06f, CGPoint.ccp(initialHudPos.x, initialHudPos.y))				
    				));	    		
        }
        else if (currentPitch<80 && energyFlags[2])
        {
        	shakeoffset = 1;	
        	hudNode.runAction(CCSequence.actions(
    				CCMoveBy.action(0.06f, CGPoint.ccp(10+shakeoffset,-20-shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(-20-shakeoffset,10+shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(20+shakeoffset,20+shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(-10-shakeoffset,-10-shakeoffset)),
    				CCMoveTo.action(0.06f, CGPoint.ccp(initialHudPos.x, initialHudPos.y))				
    				));
        }
        else if (currentPitch<100 && energyFlags[3])
        {
        	shakeoffset = 3;	
        	hudNode.runAction(CCSequence.actions(
    				CCMoveBy.action(0.06f, CGPoint.ccp(10+shakeoffset,-20-shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(-20-shakeoffset,10+shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(20+shakeoffset,20+shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(-10-shakeoffset,-10-shakeoffset)),
    				CCMoveTo.action(0.06f, CGPoint.ccp(initialHudPos.x, initialHudPos.y))				
    				));
        }
        else if (currentPitch==100 && energyFlags[4])
        {
        	shakeoffset = 5;	
            hudNode.runAction(CCSequence.actions(
    				CCMoveBy.action(0.06f, CGPoint.ccp(10+shakeoffset,-20-shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(-20-shakeoffset,10+shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(20+shakeoffset,20+shakeoffset)),
    				CCMoveBy.action(0.06f, CGPoint.ccp(-10-shakeoffset,-10-shakeoffset)),
    				CCMoveTo.action(0.06f, CGPoint.ccp(initialHudPos.x, initialHudPos.y))				
    				));
    		
        }
		
		///each bug state update
		for (EnemyBug target : _bugs)		
		{
			if(target.health_points<=0)
			{
				target.health_points = 999;
			CCFiniteTimeAction explosionAction = CCSpawn.actions( 
					CCFadeIn.action(0),	
					CCAnimate.action(explosion_Animation, true)
					);
			//target.bugSprite.stopAllActions();
			target.bugSprite.runAction(CCSequence.actions(
					CCFadeOut.action(0.3f),					
					explosionAction,
					CCCallFuncND.action(this, "funcN_bugKilled", target)));
			removeChild(( (EnemyBug) target).circleProgress,true);				
			}
			else if(target.inStateControl)
			{
				//"continuous" states
				if(target.state == States.POISON )
				{
					target.health_points-=5;					
				}
				else if(target.state == States.ONFIRE)
				{
					target.health_points-=10;
				}
				
				
			
				target.stateCounter--;
				if(target.stateCounter<=0)	//state end  //end "activated" states // return priorstats
				{
					if(target.state == States.SLOW )
					{
						target.zspeed = target.stateTemp;						
					}
					else if(target.state == States.FROZEN)
					{
						target.zspeed = target.stateTemp;
					}
					else if(target.state == States.STUPIDUPDOWN)
					{
						target.zspeed = target.stateTemp;
					}
					else if(target.state == States.CONFUSION)
					{
						target.zspeed = target.stateTemp;
						target.bugSprite.stopAction(CCRotateBy.action()); //end possible previous CONFUSION	
						target.bugSprite.setRotation(0);
						
					}
					else if(target.state == States.ONFIRE)
					{
						target.bugSprite.removeAllChildren(true);
					}
					else if(target.state == States.ARMORDOWN)
					{
						target.bugSprite.removeAllChildren(true);
					}
					target.inStateControl = false;
					target.state = States.NULL;
				}
			
			}
			/*
			if(!target.fifoScripts.isEmpty())
			{
				target.runScript();
			}*/
		}
		//else
		//{
			//unschedule("gameLogic");
		//}
	}
	public void explode(float dt)
	{
		CCParticleFireworks particle  = (CCParticleFireworks) CCParticleFireworks.node();
	     //adding properties to the object....
	  particle.setPosition(CGPoint.ccp(200, 50));	    
	  particle.setAutoRemoveOnFinish(true);
	      // finally add the object to the CClayer
	  addChild(particle);
	}
	public void bugMove(float dt)
	{		
		for (EnemyBug aBug : _bugs)
		{


			aBug.runScript();//every bug run set scripts
			aBug.runScriptDepth();//run depth scripts
		
			if(aBug.distance>10){
			//	aBug.distance = aBug.distance - aBug.zspeed;
				aBug.bugSprite.setScale(maxScale * (aBug.distance * (0.05f-1)/MAXDISTOFBUG + 1));
			}
			else
			{
				aBug.indicatorSprite.runAction(CCBlink.action(BUGMOVEUPDATEFREQ, 1));
				playerHP-=0.2;
				screenRed.stopAllActions();
				screenRed.setVisible(true);
				screenRed.runAction(CCSequence.actions(
						CCBlink.action(0.5f,1),
						CCCallFuncN.action(this,"spriteMoveFinishedHide")
						));		
				
				if(playerHP<0)
				{
					sensorMan.unregisterListener(listener);
					CCDirector.sharedDirector().replaceScene(GameOverLayer.scene(34,false));
					return;
				}
			}			         			       
	        //if(aBug.indicatorOn)
			//{
				//aBug.indicatorSprite.runAction(CCRepeatForever.action(CCBlink.action(0.5f, 500/aBug.distance)));
				//aBug.indicatorSprite.runAction(CCBlink.action(BUGMOVEUPDATEFREQ, MAXDISTOFBUG/aBug.distance));
				//aBug.indicatorSprite.runAction(CCTintTo.action(BUGMOVEUPDATEFREQ, ccColor3B.ccc3((int)(aBug.distance/MAXDISTOFBUG)*255, 0, 0)));				
			//}
		}		
	}
	
	public void update(float dt)
	{
		//updatePitch();
		//update relative projectile position
		for (RealWorldObject target : projectiles_Arr)
		{
			//int d = target.distance;
			float delta_azimuth = target.azimuth - azimuth; 
			float delta_roll = target.roll - roll;
			// fix azimuth dilemma but didn't work 
			if(delta_azimuth>180)
				delta_azimuth = delta_azimuth - 360;
			else if(delta_azimuth<-180)
				delta_azimuth = delta_azimuth + 360;
			
			//target.delta_azimuth = delta_azimuth;
			CGSize winSize = CCDirector.sharedDirector().displaySize();
			//int targetX = (int)(Math.cos(delta_roll)*Math.sin(delta_azimuth)/d+winSize.width/2);
			//int targetY = (int)(Math.sin(delta_roll)/d+winSize.height/2);
			float targetX = (int) (delta_azimuth*(winSize.width/60)+winSize.width/2);  //visual 60 degrees
			float targetY = (int)(delta_roll*(winSize.height/30)+winSize.height/2);    //       30
				
			target.realObject.setPosition(targetX+13 , targetY-4);
			target.timeToLive-=10;
			target.distance+=target.zspeed;
			target.realObject.setScale(maxScaleForProjectile * (target.distance * (0.05f-1)/MAXDISTOFBUG + 1));
			
			if(target.energyLevel>0)
			{
				energyBall.setPosition(target.realObject.getPosition());	
				energyBall.setScale(ENERGY_BALL_DEFAULT_SCALE+target.realObject.getScale());
								
				// emitter position
				energyEmitter.setPosition(energyBall.getPosition());
				
				
			}
			
			if(target.timeToLive<0 || target.distance>MAXDISTOFBUG)
			{
				removeChild(target.realObject, true);	
				realWorldObjectsToDelete.add(target);
				if(target.energyLevel>0)
				{
					hudNode.removeChild(energyBall,true);					
					hudNode.removeChild(energyEmitter,true);
				}
			}			
			
		}
		
		//update relative bug position
		for (EnemyBug target : _bugs)
		{
			//int d = target.distance;
			float delta_azimuth = target.azimuth - azimuth; 
			float delta_roll = target.roll - roll;
			// fix azimuth dilemma but didn't work 
			if(delta_azimuth>180)
				delta_azimuth = delta_azimuth - 360;
			else if(delta_azimuth<-180)
				delta_azimuth = delta_azimuth + 360;
			
			//target.delta_azimuth = delta_azimuth;
			CGSize winSize = CCDirector.sharedDirector().displaySize();
			//int targetX = (int)(Math.cos(delta_roll)*Math.sin(delta_azimuth)/d+winSize.width/2);
			//int targetY = (int)(Math.sin(delta_roll)/d+winSize.height/2);
			float targetX = (int) (delta_azimuth*(winSize.width/60)+winSize.width/2);  //visual 60 degrees
			float targetY = (int)(delta_roll*(winSize.height/30)+winSize.height/2);    //       30
				
			target.bugSprite.setPosition(targetX , targetY);	
			//Draw Indicators
			if( (0>targetX || targetX>winSize.width) || (0>targetY || targetY>winSize.height) ) //offscreen
			{
				if(!target.indicatorOn)//turn on indicator if off
				{
					target.indicatorOn = true;
					addChild(target.indicatorSprite,60);
					removeChild(target.circleProgress,true);
				}
				if( 0<targetX && targetX<winSize.width )//N,S off
				{
					if(targetY>winSize.height) //N off
					{
						targetY = winSize.height - target.indicatorSprite.getBoundingBox().size.height;
						target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("up.png"));
						target.indicatorSprite.setFlipY(false);
					}
					else if(0>targetY) //S off
					{
						targetY = 0 + target.indicatorSprite.getBoundingBox().size.height;
						target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("up.png"));
						target.indicatorSprite.setFlipY(true);
					}
				}
				else if( 0<targetY && targetY<winSize.height )//E,W off
				{
					if(targetX>winSize.width) //E off
					{
						targetX = winSize.width - target.indicatorSprite.getBoundingBox().size.width;
						target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("left.png"));
						target.indicatorSprite.setFlipX(true);
					}
					else if(0>targetX) //W off
					{
						targetX = 0 + target.indicatorSprite.getBoundingBox().size.width;
						target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("left.png"));
						target.indicatorSprite.setFlipX(false);
					}
				}
				else if( (0>targetY && 0>targetX) )//左下 off
				{
					targetX = 0 + target.indicatorSprite.getBoundingBox().size.width;
					targetY = 0 + target.indicatorSprite.getBoundingBox().size.height;
					target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("leftdown.png"));
					target.indicatorSprite.setFlipX(false);
					target.indicatorSprite.setFlipY(false);
				}
				else if( (0>targetY && targetX>winSize.width) )//右下 off
				{
					targetX = winSize.width - target.indicatorSprite.getBoundingBox().size.width;
					targetY = 0 + target.indicatorSprite.getBoundingBox().size.height;
					target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("leftdown.png"));
					target.indicatorSprite.setFlipX(true);
					target.indicatorSprite.setFlipY(false);
				}
				else if( (targetY>winSize.height && 0>targetX) )//左上 off
				{
					targetX = 0 + target.indicatorSprite.getBoundingBox().size.width;
					targetY = winSize.height - target.indicatorSprite.getBoundingBox().size.height;
					target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("leftdown.png"));
					target.indicatorSprite.setFlipX(false);
					target.indicatorSprite.setFlipY(true);
				}
				else if( (targetY>winSize.height && targetX>winSize.width) )//右上 off
				{
					targetX = winSize.width - target.indicatorSprite.getBoundingBox().size.width;
					targetY = winSize.height - target.indicatorSprite.getBoundingBox().size.height;
					target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("leftdown.png"));
					target.indicatorSprite.setFlipX(true);
					target.indicatorSprite.setFlipY(true);
				}
				target.indicatorSprite.setPosition(targetX , targetY);					
				
			}
			else //inside screen
			{
				if(target.indicatorOn)
				{
					target.indicatorOn = false;
					removeChild(target.indicatorSprite, true);
					addChild(target.circleProgress);
				}
				
				eachBug_checkParticleIntersect(target);
				target.update();
				
			}		
			
			
			//calculate the polar coordinates for radar points
			CGPoint p= radarSprite.getPosition();
            double vector = delta_azimuth * Math.PI*2 / 360.0;            
            int LENGTH= (int) (target.distance * DISTANCE_RADAR_RATIO+8);  
            int vx=(int)(p.x/2+LENGTH*Math.sin(vector)-3);
            int vy=(int)(p.y/2+LENGTH*Math.cos(vector));
			bug_points.add(CGPoint.ccp(vx, vy));             			   
			
			
		}
		bug_points.toArray(bug_CGPoints);
		bug_CGPoints_size = bug_points.size();
        bug_points.clear();
        radarSprite.setRadarPoints(bug_CGPoints, bug_CGPoints_size);
        hp_progress.setPercentage(playerHP);
        
		//delete gone bugs
        for (EnemyBug bug : bugsToDelete)
		{
			_bugs.remove(bug);
			removeChild(bug.circleProgress, true);
			
		}
		bugsToDelete.clear();
		
		//delete gone RealWorldObjects
        for (RealWorldObject object : realWorldObjectsToDelete)
		{
        	//remove this object's stuff
        	projectiles_Arr.remove(object);
			
		}
        realWorldObjectsToDelete.clear();
        
		//_label.setString(String.format("Dir:%8d Inc:%+8d\nazimuth:%4.3fpitch:%4.3froll:%4.3f\nX:%.3fY:%.3fZ:%.3f",direction,inclination,azimuth,pitch,roll,x,y,z));
	}
	
	protected void addBugs(EnemyTypes etype, PathTypes ptype, int eHP, int ezSPD, int exySPD)
	{
		/*
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		Random rand = new Random();
		//EnemyBug target = EnemyBug.sprite("fly_worm.png");
		EnemyBug target = new EnemyBug();
		target.bugSprite = CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("fly_worm_a_1.png"));
		addChild(target.bugSprite,-2);
		target.indicatorSprite = CCSprite.sprite(CCSpriteFrameCache.spriteFrameByName("up.png")); //init indicator rect(sprite)
		target.indicatorSprite.setScale(winSize.height/(20f*target.indicatorSprite.getContentSize().height));
		addChild(target.circleProgress,-1);
		
		target.azimuth = rand.nextInt(260)+50;
		target.roll = rand.nextInt(101)+30; //-90<=roll<=90//30~131
		//target.azimuth = 0;
		//target.roll = 0; //-90<=roll<=90
		
		target.distance = rand.nextInt(400)+100;
		//target.distance = rand.nextInt(500);//0~500
		if(maxScale == 0)
		maxScale = winSize.height/(2f*target.bugSprite.getContentSize().height);
		
		target.bugSprite.setScale(maxScale * (target.distance * (0.05f-1)/MAXDISTOFBUG + 1));
		target.bugSprite.setPosition(winSize.width+100,winSize.height+100); //set it off-screen at first
		CCAction fly_worm_aAction = CCRepeatForever.action(CCAnimate.action(fly_worm_aAnimation, false));
		target.bugSprite.runAction(fly_worm_aAction);
		
		//target.bugSprite.setTag(0); //alive
		
		_bugs.add(target);
		
		*/
		
	}
	public void enableSuperPowerA()//歐買尬
	{
//		CCSprite friendlyTomato = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tomato_head.png"));		
//		//player.setPosition(CGPoint.ccp(player.getContentSize().width / 2.0f, winSize.height / 2.0f));
//		friendlyTomato.setScale(winSize.height/(4f*friendlyTomato.getContentSize().height));		
//		friendlyTomato.setPosition(CGPoint.ccp(winSize.width-friendlyTomato.getBoundingBox().size.width, winSize.height / 2.0f));
//		addChild(friendlyTomato);
//		float shoooDuration = 0.3f;
//		CCAction friendTomatoFrenzy = CCSequence.actions(
//				CCBlink.action(0.5f,3),
//				CCDelayTime.action(0.5f),
//				CCJumpBy.action(0.3f,CGPoint.ccp(0, 0),friendlyTomato.getBoundingBox().size.height/2,3),
//				CCSpawn.actions(CCMoveBy.action(shoooDuration,CGPoint.ccp(-winSize.width*0.9f, 0)), CCFadeOut.action(shoooDuration)),				
//				CCMoveTo.action(0,CGPoint.ccp(winSize.width-friendlyTomato.getBoundingBox().size.width, winSize.height * 0.75f)),
//				CCSpawn.actions(CCMoveBy.action(shoooDuration,CGPoint.ccp(-winSize.width*0.9f, 0)), CCFadeOut.action(shoooDuration)),				
//				CCMoveTo.action(0,CGPoint.ccp(winSize.width-friendlyTomato.getBoundingBox().size.width, winSize.height * 0.25f)),
//				CCSpawn.actions(CCMoveBy.action(shoooDuration,CGPoint.ccp(-winSize.width*0.9f, 0)), CCFadeOut.action(shoooDuration)),				
//				CCMoveTo.action(0,CGPoint.ccp(winSize.width-friendlyTomato.getBoundingBox().size.width, winSize.height * 0.85f)),
//				CCSpawn.actions(CCMoveBy.action(shoooDuration,CGPoint.ccp(-winSize.width*0.9f, 0)), CCFadeOut.action(shoooDuration)),				
//				CCMoveTo.action(0,CGPoint.ccp(winSize.width-friendlyTomato.getBoundingBox().size.width, winSize.height * 0.15f)),
//				CCSpawn.actions(CCMoveBy.action(shoooDuration,CGPoint.ccp(-winSize.width*0.9f, 0)), CCFadeOut.action(shoooDuration)),				
//				CCCallFuncN.action(this, "spriteMoveFinished")
//				);
//		friendlyTomato.runAction(friendTomatoFrenzy);
//		//damage bugs!
//		for (EnemyBug target : _bugs)
//		{						
//				target.inStateControl = true;					
//				target.state = States.ONFIRE;
//				target.stateCounter = 10; //will end after 5 secs
//				CCSprite tempNode = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("animated_fire_a_0.png"));
//				tempNode.setPosition(
//						CGPoint.ccp(target.bugSprite.getBoundingBox().size.width, 
//									target.bugSprite.getBoundingBox().size.height));
//				tempNode.setOpacity(100);
//				tempNode.setScale(target.bugSprite.getContentSize().width*1.0f/tempNode.getContentSize().width);
//				target.bugSprite.addChild(tempNode,1);
//				tempNode.runAction(CCRepeatForever.action(CCAnimate.action(fire_Animation,false)));
//				
//			
//			/*
//			CCFiniteTimeAction explosionAction = CCSpawn.actions( 
//					CCFadeIn.action(0),	
//					CCSequence.actions(
//						CCAnimate.action(explosion_Animation, false)
//					)
//						);
//			target.bugSprite.runAction(CCSequence.actions(
//					CCDelayTime.action(2f),
//					CCFadeOut.action(0.5f),
//					explosionAction,
//					CCCallFuncND.action(this, "funcN_bugKilled",target)));		
//					*/
//			
//		}
//		
//		
//		
	}
	public void ohMyGod()//歐買尬
	{
		cannon_status.setVisible(false);
		
		ccColor4F startColor,startColorVar,endColor,endColorVar;
		smokeEmitter.setDuration(0.5f);
		smokeEmitter.setPosition(CGPoint.ccp(
				cannon.getPosition().x, 
				cannon.getPosition().y+cannon.getBoundingBox().size.height/2));
		startColor = new ccColor4F(0.8f, 0.2f, 0.2f, 1.0f);
		smokeEmitter.setStartColor(startColor);
		startColorVar = new ccColor4F(0.2f, 0.0f, 0.0f, 0.1f);
		smokeEmitter.setStartColorVar(startColorVar);
		endColor = new ccColor4F(0.0f, 0.0f, 0.0f, 1.0f);
		smokeEmitter.setEndColor(endColor);
		endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);	
		smokeEmitter.setEndColorVar(endColorVar);
		//smokeEmitter.setEndSize(winSize.height / 2.0f);
		smokeEmitter.setEndSize(CCParticleSystem.kCCParticleStartSizeEqualToEndSize);
		smokeEmitter.setRotation(45.0f);
		smokeEmitter.setTag(100);
		smokeEmitter.resetSystem();
		smokeEmitter.scheduleUpdate(1);
		hudNode.addChild(smokeEmitter,-10,1.0f,1.0f,cannon.getPosition().x, 
				cannon.getPosition().y+cannon.getBoundingBox().size.height/2);
		
		fireEmitter.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.6f));
		startColor = new ccColor4F(0.8f, 0.2f, 0.2f, 1.0f);
		fireEmitter.setStartColor(startColor);
		startColorVar = new ccColor4F(0.2f, 0.0f, 0.0f, 0.0f);
		fireEmitter.setStartColorVar(startColorVar);
		endColor = new ccColor4F(0.0f, 0.0f, 0.0f, 1.0f);
		fireEmitter.setEndColor(endColor);
		endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);
		fireEmitter.setEndColorVar(endColorVar);
		fireEmitter.setLifeVar(0);
		fireEmitter.setLife(10);
		fireEmitter.setSpeed(100);
		fireEmitter.setSpeedVar(0);
		fireEmitter.setEmissionRate(10000);
		//fireEmitter.setVisible(false);
		fireEmitter.setTag(101);
		fireEmitter.resetSystem();
		fireEmitter.scheduleUpdate(1);
		//hudNode.addChild(fireEmitter,-13,1.0f,1.0f,winSize.width * 0.5f,winSize.height * 0.65f);
		
		dragonDevil.setPosition(CGPoint.ccp(winSize.width * 0.52f,winSize.height * 0.6f));
		dragonDevil.setScale(winSize.width / (8.89f * dragonDevil.getContentSize().width));
		dragonDevil.setVisible(false);
		dragonDevil.setTag(102);
		hudNode.addChild(dragonDevil,-12,1.0f,1.0f,winSize.width * 0.52f, winSize.height * 0.6f);
		
		smokeBall.setDuration(1.0f);
		smokeBall.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.6f));
		startColor = new ccColor4F(0.8f, 0.2f, 0.2f, 1.0f);
		smokeBall.setStartColor(startColor);
		startColorVar = new ccColor4F(0.2f, 0.0f, 0.0f, 0.1f);
		smokeBall.setStartColorVar(startColorVar);
		endColor = new ccColor4F(0.0f, 0.0f, 0.0f, 1.0f);
		smokeBall.setEndColor(endColor);
		endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);	
		smokeBall.setEndColorVar(endColorVar);
		smokeBall.setEndSize(winSize.width / 8.0f);
		smokeBall.setTag(104);
		smokeBall.resetSystem();
		smokeBall.scheduleUpdate(1);
		
		
		CCAction emitSmoke = CCSequence.actions(
				CCDelayTime.action(0.8f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		
		smokeEmitter.runAction(emitSmoke);
		
		CCAction showDevil = CCSequence.actions(
				CCDelayTime.action(1.2f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		
		smokeBall.runAction(showDevil);
		
		CCAction devilAttack = CCSequence.actions(
				CCDelayTime.action(2.0f),
				CCShow.action(),
				CCFadeIn.action(0.5f),
				CCBlink.action(0.5f,3),
				CCScaleTo.action(1.0f, 2.0f),
				CCFadeOut.action(0.5f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")

		);
		dragonDevil.runAction(devilAttack);
		
		CCAction starsFire = CCSequence.actions(
				CCDelayTime.action(3.0f),
			    CCCallFunc.action(this, "bugOnFire"),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		fireEmitter.runAction(starsFire);
		
	}
	public void Haleiluya()  //哈雷路亞
	{
		//Log.d("aaaa", "@@@@@" + (25.0f*snowEmitter.getContentSize().width));
		ccColor4F startColor,startColorVar,endColor,endColorVar;

		cannon_status.setVisible(false);
		
		smokeEmitter.setDuration(0.5f);
		smokeEmitter.setPosition(CGPoint.ccp(
				cannon.getPosition().x, 
				cannon.getPosition().y+cannon.getBoundingBox().size.height/2));
		startColor = new ccColor4F(0.5f, 1.0f, 0.0f, 1.0f);
		smokeEmitter.setStartColor(startColor);
		startColorVar = new ccColor4F(0.0f, 0.2f, 0.0f, 0.1f);
		smokeEmitter.setStartColorVar(startColorVar);
		endColor = new ccColor4F(0.0f, 0.0f, 0.0f, 1.0f);
		smokeEmitter.setEndColor(endColor);
		endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);	
		smokeEmitter.setEndColorVar(endColorVar);
		//smokeEmitter.setEndSize(winSize.height / 2.0f);
		smokeEmitter.setEndSize(CCParticleSystem.kCCParticleStartSizeEqualToEndSize);
		smokeEmitter.setRotation(45.0f);
		smokeEmitter.setTag(100);
		smokeEmitter.resetSystem();
		smokeEmitter.scheduleUpdate(1);
		hudNode.addChild(smokeEmitter,-10,1.0f,1.0f,cannon.getPosition().x, 
				cannon.getPosition().y+cannon.getBoundingBox().size.height/2);

		snowEmitter.setDuration(CCParticleSystem.kCCParticleDurationInfinity);
		snowEmitter.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.95f));
		snowEmitter.setLife(3);
		snowEmitter.setLifeVar(1);
		// gravity
		snowEmitter.setGravity(CGPoint.ccp(0,-10));
		// speed of particles
		snowEmitter.setSpeed(230);
		snowEmitter.setSpeedVar(30);
		startColor = new ccColor4F(1.0f, 1.0f, 1.0f, 1.0f);
		snowEmitter.setStartColor(startColor);
		startColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);
		snowEmitter.setStartColorVar(startColorVar);
		endColor = new ccColor4F(1.0f, 1.0f, 1.0f, 0.0f);
		snowEmitter.setEndColor(endColor);
		endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);
		snowEmitter.setEndColorVar(endColorVar);
		snowEmitter.setEmissionRate(snowEmitter.getTotalParticles()/snowEmitter.getLife());
		snowEmitter.setStartSize(35.0f);
		snowEmitter.setEndSize(CCParticleSystem.kCCParticleStartSizeEqualToEndSize);
		snowEmitter.setBlendAdditive(true);
		snowEmitter.setTag(101);
		//snowEmitter.setVisible(false);
		snowEmitter.resetSystem();
		snowEmitter.scheduleUpdate(1);
		//hudNode.addChild(snowEmitter,-11,1.0f,1.0f,winSize.width * 0.5f, winSize.height * 0.95f);
		
		buddhaAngle.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.6f));
		buddhaAngle.setScale(winSize.width / (5.0f * buddhaAngle.getContentSize().width));
		buddhaAngle.setVisible(false);
		buddhaAngle.setTag(103);
		//buddha.setScale(winSize.height/ 2.0f *(buddha.getContentSize().height));
		hudNode.addChild(buddhaAngle,-12,1.0f,1.0f,winSize.width * 0.5f, winSize.height * 0.6f);
		
		smokeBall.setDuration(1.0f);
		smokeBall.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.6f));
		startColor = new ccColor4F(0.5f, 1.0f, 0.0f, 1.0f);
		smokeBall.setStartColor(startColor);
		startColorVar = new ccColor4F(0.0f, 0.2f, 0.0f, 0.1f);
		smokeBall.setStartColorVar(startColorVar);
		endColor = new ccColor4F(0.0f, 0.0f, 0.0f, 1.0f);
		smokeBall.setEndColor(endColor);
		endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);	
		smokeBall.setEndColorVar(endColorVar);
		smokeBall.setEndSize(winSize.width / 8.0f);
		smokeBall.setTag(104);
		smokeBall.resetSystem();
		smokeBall.scheduleUpdate(1);
		
		CCAction emitSmoke = CCSequence.actions(
				CCDelayTime.action(0.8f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		
		smokeEmitter.runAction(emitSmoke);
		
		CCAction showBuddha = CCSequence.actions(
				CCDelayTime.action(1.2f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		
		smokeBall.runAction(showBuddha);
		CCAction buddhaHelp = CCSequence.actions(
				CCDelayTime.action(2.0f),
				CCShow.action(),
				CCFadeIn.action(0.5f),
				CCBlink.action(0.5f,3),
				CCScaleBy.action(1.0f, 2.0f),
				//CCScaleTo.action(1.0f, 2.0f),
				CCFadeOut.action(0.5f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")

		);
		buddhaAngle.runAction(buddhaHelp);
		CCAction starsRain = CCSequence.actions(
				CCDelayTime.action(3.0f),
				//CCShow.action(),
				//CCDelayTime.action(2.0f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		snowEmitter.runAction(starsRain);
		
	}
	public void dontDoThat()
	{
		if(superPowerCoolDownTimer !=0)
		{
			return;
		}
		superPowerCoolDownTimer = (int) (super_cooldown_time*2);
		radioItem.setIsEnabled(false);
		radioItem.stopAllActions();
		radioItem.setRotation(3f);
		radioItem.runAction(CCSequence.actions(
				CCFadeTo.action(0, 0), 
				CCFadeTo.action(super_cooldown_time, 150)
				));
		
		
		cannon_status.setVisible(false);
		
		ccColor4F startColor,startColorVar,endColor,endColorVar;
		smokeEmitter.setDuration(0.5f);
		smokeEmitter.setPosition(CGPoint.ccp(
				cannon.getPosition().x, 
				cannon.getPosition().y+cannon.getBoundingBox().size.height/2));
		startColor = new ccColor4F(1.0f, 1.0f, 0.0f, 1.0f);
		smokeEmitter.setStartColor(startColor);
		startColorVar = new ccColor4F(0.2f, 0.2f, 0.0f, 0.1f);
		smokeEmitter.setStartColorVar(startColorVar);
		endColor = new ccColor4F(0.0f, 0.0f, 0.0f, 1.0f);
		smokeEmitter.setEndColor(endColor);
		endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);	
		smokeEmitter.setEndColorVar(endColorVar);
		//smokeEmitter.setEndSize(winSize.height / 2.0f);
		smokeEmitter.setEndSize(CCParticleSystem.kCCParticleStartSizeEqualToEndSize);
		smokeEmitter.setRotation(45.0f);
		smokeEmitter.setTag(100);
		smokeEmitter.resetSystem();
		smokeEmitter.scheduleUpdate(1);
		hudNode.addChild(smokeEmitter,-10,1.0f,1.0f,cannon.getPosition().x, 
				cannon.getPosition().y+cannon.getBoundingBox().size.height/2);
		
		fireEmitter.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.6f));
		startColor = new ccColor4F(0.8f, 0.2f, 0.2f, 1.0f);
		fireEmitter.setStartColor(startColor);
		startColorVar = new ccColor4F(0.2f, 0.0f, 0.0f, 0.0f);
		fireEmitter.setStartColorVar(startColorVar);
		endColor = new ccColor4F(0.0f, 0.0f, 0.0f, 1.0f);
		fireEmitter.setEndColor(endColor);
		endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);
		fireEmitter.setEndColorVar(endColorVar);
		fireEmitter.setLifeVar(0);
		fireEmitter.setLife(10);
		fireEmitter.setSpeed(100);
		fireEmitter.setSpeedVar(0);
		fireEmitter.setEmissionRate(10000);
		//fireEmitter.setVisible(false);
		fireEmitter.setTag(101);
		fireEmitter.resetSystem();
		fireEmitter.scheduleUpdate(1);
		//hudNode.addChild(fireEmitter,-13,1.0f,1.0f,winSize.width * 0.5f,winSize.height * 0.65f);
		
		pineApple.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.6f));
		pineApple.setScale(winSize.width / (8.0f * pineApple.getContentSize().width));
		pineApple.setVisible(false);
		pineApple.setTag(102);
		FBimage.setScale(1.5f * winSize.width / (16.0f *pineApple.getContentSize().width));
		FBimage.setPosition(CGPoint.ccp(pineApple.getContentSize().width / 2, pineApple.getContentSize().height * 0.4f));
		pineApple.addChild(FBimage);
		hudNode.addChild(pineApple,-12,1.0f,1.0f,winSize.width * 0.5f, winSize.height * 0.6f);
		
		smokeBall.setDuration(1.0f);
		smokeBall.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.6f));
		startColor = new ccColor4F(1.0f, 1.0f, 0.0f, 1.0f);
		smokeBall.setStartColor(startColor);
		startColorVar = new ccColor4F(0.2f, 0.2f, 0.0f, 0.1f);
		smokeBall.setStartColorVar(startColorVar);
		endColor = new ccColor4F(0.0f, 0.0f, 0.0f, 1.0f);
		smokeBall.setEndColor(endColor);
		endColorVar = new ccColor4F(0.0f, 0.0f, 0.0f, 0.0f);	
		smokeBall.setEndColorVar(endColorVar);
		smokeBall.setEndSize(winSize.width / 8.0f);
		smokeBall.setTag(104);
		smokeBall.resetSystem();
		smokeBall.scheduleUpdate(1);
		
		
		CCAction emitSmoke = CCSequence.actions(
				CCDelayTime.action(0.8f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		
		smokeEmitter.runAction(emitSmoke);
		
		CCAction showDevil = CCSequence.actions(
				CCDelayTime.action(1.2f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		
		smokeBall.runAction(showDevil);
		
		CCAction devilAttack = CCSequence.actions(
				CCDelayTime.action(2.0f),
				CCShow.action(),
				CCFadeIn.action(0.5f),
				CCBlink.action(0.5f,3),
				CCScaleTo.action(1.0f, 1.6f),
				CCFadeOut.action(0.5f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")

		);
		pineApple.runAction(devilAttack);
		
		CCAction starsFire = CCSequence.actions(
				CCDelayTime.action(3.0f),
			    //CCCallFunc.action(this, "bugOnFire"),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		fireEmitter.runAction(starsFire);
		CCAction FBremove = CCSequence.actions(
				CCDelayTime.action(4.5f),
			    //CCCallFunc.action(this, "bugOnFire"),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		FBimage.runAction(FBremove);
	}
	public void bugOnFire()
	{
		for (EnemyBug target : _bugs)
		{						
				target.inStateControl = true;					
				target.state = States.ONFIRE;
				target.stateCounter = 10; //will end after 5 secs
				CCSprite tempNode = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("animated_fire_a_0.png"));
				tempNode.setPosition(
						CGPoint.ccp(target.bugSprite.getBoundingBox().size.width, 
									target.bugSprite.getBoundingBox().size.height));
				tempNode.setOpacity(100);
				tempNode.setScale(target.bugSprite.getContentSize().width*1.0f/tempNode.getContentSize().width);
				target.bugSprite.addChild(tempNode,1);
				tempNode.runAction(CCRepeatForever.action(CCAnimate.action(fire_Animation,false)));
				
			
			/*
			CCFiniteTimeAction explosionAction = CCSpawn.actions( 
					CCFadeIn.action(0),	
					CCSequence.actions(
						CCAnimate.action(explosion_Animation, false)
					)
						);
			target.bugSprite.runAction(CCSequence.actions(
					CCDelayTime.action(2f),
					CCFadeOut.action(0.5f),
					explosionAction,
					CCCallFuncND.action(this, "funcN_bugKilled",target)));		
					*/
			
		}
	}
	public void updatePitch(float dt)
	{
		if(energyBallCoolDownTimer>0)
		{
			cannon_status.setString(
					String.format("Cannon Cooling...%2.0f%%", energy_progress.getPercentage()));
			return;
		}
		
		energyFlagsInt[1] = pitchRecorder.getPitch();
		cannon_status.setString(
			String.format("Energy Cannon Ready..."));
		
		if(energyFlagsInt[1]>energyFlagsInt[0])
		{
			energyFlagsInt[0] = energyFlagsInt[1];
			
			currentEnergyIndex++;
		}
		else if(energyFlagsInt[1]==energyFlagsInt[0])
        {
                energyFlagsInt[0] = energyFlagsInt[1];                        
        }
		else
		{
			energyFlagsInt[0] = 0;
			energyFlagsInt[1] = 0;
			currentEnergyIndex = 0;
			cannonPointer.setPercentage(0);
			return;		
			
		}
		int currentPercentage = currentEnergyIndex*10;
		if(currentPercentage>100)
			currentPercentage = 100;
		if(currentPercentage<0)
			currentPercentage = 0;
			
		energy_progress.setPercentage(currentPercentage);
		if(currentPercentage<20&&currentPercentage>0)
		{
			if(hudNode.getChildByTag(99) != null && energyBallCoolDownTimer<=0)
        	{
        		hudNode.removeChild(energyBall,true);
        		hudNode.removeChild(energyEmitter,true);
        		
        	}
		}
		else if(currentPercentage==40)
		{
			if(hudNode.getChildByTag(99) == null  && energyBallCoolDownTimer<=0)
        	{
        		energyBall.setPosition(CGPoint.ccp(
        				cannon.getPosition().x, 
        				cannon.getPosition().y+cannon.getBoundingBox().size.height/2));	
        		energyBall.setScale(ENERGY_BALL_DEFAULT_SCALE+0.5f);
        		energyBall.runAction(CCRepeatForever.action(
        				CCAnimate.action(energy_ball_aAnimation, false)));
        		energyBall.setTag(99);
        		hudNode.addChild(energyBall,-10,1.0f,1.0f,cannon.getPosition().x, 
        				cannon.getPosition().y+cannon.getBoundingBox().size.height/2);
        		
        		// speed of particles
    			energyEmitter.setSpeed(160+currentPercentage);
    			energyEmitter.setRadialAccel(-120-currentPercentage);
    			energyEmitter.setTangentialAccel(30+currentPercentage);
				// emitter position
				energyEmitter.setPosition(energyBall.getPosition());
				energyEmitter.setTag(98);
				energyEmitter.resetSystem();
				energyEmitter.scheduleUpdate(1);
				hudNode.addChild(energyEmitter,-10,1.0f,1.0f,cannon.getPosition().x, 
        				cannon.getPosition().y+cannon.getBoundingBox().size.height/2);
        	}
        	energyBall.runAction(CCScaleTo.action(0.1f,ENERGY_BALL_DEFAULT_SCALE+1.5f));
		}
		else if(currentPercentage==100)
		{
			//energyBallCoolDownTimer = currentPercentage / 10;    	
    		//energy_progress.runAction(CCProgressFromTo.action(energyBallCoolDownTimer*0.5f, currentPercentage,0));
    		//fireParticle();
		}
		else
		{
			// speed of particles
			energyEmitter.setSpeed(160+currentPercentage*3);
			energyEmitter.setRadialAccel(-120-currentPercentage*3);
			energyEmitter.setTangentialAccel(30+currentPercentage*3);			
			
		}
		
		
		/*
		currentPitch = (pitchRecorder.getPitch()-55)*10;
		if(currentPitch>100)
        	currentPitch = 100;		
		if(currentPitch<0)
        	currentPitch = 0;
				
		int shakeoffset = 0;		
		
		if (currentPitch<20 && currentPitch>0)
        {
			energyFlags[0] = true;
        	pitchPointer.setPosition(CGPoint.ccp(
    				cannon.getPosition().x+cannon.getBoundingBox().size.width/2, 
    				cannon.getPosition().y-cannon.getBoundingBox().size.height/2+cannon.getBoundingBox().size.height*20/100));
        	cannonPointer.setPercentage(20);
        	if(hudNode.getChild(99) != null && energyBallCoolDownTimer<=0)
        	{
        		hudNode.removeChild(energyBall,true);
        	}
        	energyLevelState = 0;
        }
		else if (currentPitch<40 && energyFlags[0])
        {
        	energyFlags[1] = true;
        	pitchPointer.setPosition(CGPoint.ccp(
    				cannon.getPosition().x+cannon.getBoundingBox().size.width/2, 
    				cannon.getPosition().y-cannon.getBoundingBox().size.height/2+cannon.getBoundingBox().size.height*40/100));	
        	cannonPointer.setPercentage(40);
        	energyBall.runAction(CCScaleTo.action(0.1f,ENERGY_BALL_DEFAULT_SCALE+1.0f));
        	
        	energy_progress.setPercentage(currentPitch);        
        	energyLevelState = 1;	
        }
        else if (currentPitch<60 && energyFlags[1])
        {
        	energyFlags[2] = true;
        	pitchPointer.setPosition(CGPoint.ccp(
    				cannon.getPosition().x+cannon.getBoundingBox().size.width/2, 
    				cannon.getPosition().y-cannon.getBoundingBox().size.height/2+cannon.getBoundingBox().size.height*60/100));
        	cannonPointer.setPercentage(60);
        	if(hudNode.getChild(99) == null  && energyBallCoolDownTimer<=0)
        	{
        		energyBall.setPosition(CGPoint.ccp(
        				cannon.getPosition().x, 
        				cannon.getPosition().y+cannon.getBoundingBox().size.height/2));	
        		energyBall.setScale(ENERGY_BALL_DEFAULT_SCALE+0.5f);
        		energyBall.runAction(CCRepeatForever.action(
        				CCAnimate.action(energy_ball_aAnimation, false)));
        		energyBall.setTag(99);
        		hudNode.addChild(energyBall,-10,1.0f,1.0f,cannon.getPosition().x, 
        				cannon.getPosition().y+cannon.getBoundingBox().size.height/2);
        	}
        	energyBall.runAction(CCScaleTo.action(0.1f,ENERGY_BALL_DEFAULT_SCALE+1.5f));
        	energy_progress.setPercentage(currentPitch);      
        	energyLevelState = 2;
              		
        }
        else if (currentPitch<80 && energyFlags[2])
        {
        	energyFlags[3] = true;
        	pitchPointer.setPosition(CGPoint.ccp(
    				cannon.getPosition().x+cannon.getBoundingBox().size.width/2, 
    				cannon.getPosition().y-cannon.getBoundingBox().size.height/2+cannon.getBoundingBox().size.height*80/100));
        	cannonPointer.setPercentage(80);
        	energyBall.runAction(CCScaleTo.action(0.1f,ENERGY_BALL_DEFAULT_SCALE+1.5f));
        	energy_progress.setPercentage(currentPitch);       
        	energyLevelState = 3;
        }
        else if (currentPitch<100 && energyFlags[3])
        {
        	energyFlags[4] = true;
        	pitchPointer.setPosition(CGPoint.ccp(
    				cannon.getPosition().x+cannon.getBoundingBox().size.width/2, 
    				cannon.getPosition().y-cannon.getBoundingBox().size.height/2+cannon.getBoundingBox().size.height*100/100));
        	cannonPointer.setPercentage(100);
        	energyBall.runAction(CCScaleTo.action(0.1f,ENERGY_BALL_DEFAULT_SCALE+2.0f));
        	energy_progress.setPercentage(currentPitch);        
        	energyLevelState = 4;
        }
        else if (currentPitch==100 && energyFlags[4])
        {
        	energy_progress.setPercentage(currentPitch);                    
    		energyBall.runAction(CCScaleTo.action(0.1f,ENERGY_BALL_DEFAULT_SCALE+2.5f));
        	energyLevelState = 5;
    		energyBallCoolDownTimer = energyLevelState * 2;    	
    		energy_progress.runAction(CCProgressFromTo.action(energyLevelState, 100,0));
    		cannonPointer.runAction(CCProgressTo.action(energyLevelState, 0));
    		fireParticle();
    		
        }
        else
        {
        	for(int i=0;i<5;i++)
        	{
        		energyFlags[i] = false;
        	}
        }*/
		
	}
	public void funcN_bugKilled(Object sender, Object target)
	{		    			
		//if (++_bugKilled > 12)
		
		_label.setString("meanVol:" + volThreshold);
		
		playerHP+=10;
		
		//remove this enemy's stuff
		removeChild((CCNode) sender, true);	
		removeChild(( (EnemyBug) target).indicatorSprite,true);
		bugsToDelete.add( (EnemyBug) target);
	}
	public void hudSpriteMoveFinished(Object sender)
	{
		CCNode item = (CCNode) sender;
		if(item.getTag() == 101)
			cannon_status.setVisible(true);
		else if(item.getTag() == 102)
			hudNode.addChild(fireEmitter,-13,1.0f,1.0f,winSize.width * 0.5f,winSize.height * 0.6f);
		else if(item.getTag() == 103)
			hudNode.addChild(snowEmitter,-11,1.0f,1.0f,winSize.width * 0.5f, winSize.height * 0.95f);
		else if(item.getTag() == 100)
			hudNode.addChild(smokeBall,-12,1.0f,1.0f,winSize.width * 0.5f,winSize.height * 0.6f);
		
		((CCParticleSystem)sender).stopAction(((CCParticleSystem)sender).getTag());
		
		hudNode.removeChild((CCNode) sender, true);
	}
	public void spriteMoveFinished(Object sender)
	{	
		//((CCParticleSystem)sender).stopAction(((CCParticleSystem)sender).getTag());
		removeChild((CCNode) sender, true);
	}
	public void spriteMoveFinishedHide(Object sender)
	{
		((CCNode)sender).setVisible(false);		
	}
	public void eachBug_checkParticleIntersect(EnemyBug target)
	{
		for(RealWorldObject projectile : projectiles_Arr)
		{
			/*
			CGRect targetRect = CGRect.make(target.bugSprite.getPosition().x - (target.bugSprite.getBoundingBox().size.width),
					target.bugSprite.getPosition().y - (target.bugSprite.getBoundingBox().size.height),
					target.bugSprite.getBoundingBox().size.width,
					target.bugSprite.getBoundingBox().size.height);
			//update intersect check
		
			CGRect projectileRect = CGRect.make(projectile.realObject.getPosition().x - (projectile.realObject.getBoundingBox().size.width / 2.0f),
												projectile.realObject.getPosition().y - (projectile.realObject.getBoundingBox().size.height / 2.0f),
												projectile.realObject.getBoundingBox().size.width,
												projectile.realObject.getBoundingBox().size.height);
			*/
			
				
				
			if (CGRect.intersects(projectile.realObject.getBoundingBox(), target.bugSprite.getBoundingBox()) && projectile.distance>target.distance)//hit bug!!
			{				
				if(target.state == States.ARMORDOWN)
				{
					target.health_points-=(shoo_attack_power+projectile.energyLevel*10);
				}
				else
				{
					target.health_points-=(shoo_attack_power+projectile.energyLevel*10);
				}
				//target.bugSprite.runAction(
				//		CCLiquid.action(4, 20, ccGridSize.ccg((int)target.bugSprite.getBoundingBox().size.width,
				//				(int)target.bugSprite.getBoundingBox().size.height), 0.5f));
				target.bugSprite.runAction(CCSequence.actions(
						CCRotateTo.action(0.1f, 30f),
						CCRotateTo.action(0.1f, -30f),
						CCRotateTo.action(0.1f, 0f)
				));
				/*
				if(target.health_points<0)
				{
				CCFiniteTimeAction explosionAction = CCSpawn.actions( 
						CCFadeIn.action(0),	
						CCSequence.actions(
							CCAnimate.action(explosion_Animation, false)
						)
							);
				target.bugSprite.runAction(CCSequence.actions(
						CCFadeOut.action(0.3f),
						explosionAction,
						CCCallFuncND.action(this, "funcN_bugKilled", target)));
				removeChild(( (EnemyBug) target).circleProgress,true);				
				}
				*/
				//_bugs.remove(target);
				//removeChild(target.bugSprite, true);  
				//removeChild(target.bugSpriteSheet, true);
				if(projectile.energyLevel>0)
				{
					hudNode.removeChild(energyBall, true);
					hudNode.removeChild(energyEmitter, true);	
					
				}
				removeChild(projectile.realObject, true);	
				realWorldObjectsToDelete.add(projectile);
				/*
				if(shoo_selected_idx == 0) //slow cannon
				{
					target.inStateControl = true;					
					target.state = States.SLOW;
					target.stateTemp = target.zspeed;
					target.zspeed = 5;
					target.stateCounter = 10; //will end after 5 secs					
					
				}
				else if(shoo_selected_idx == 1) //poison
				{
					target.inStateControl = true;					
					target.state = States.POISON;
					target.stateCounter = 10; //will end after 5 secs
				}
				else if(shoo_selected_idx == 2)
				{
					target.inStateControl = true;					
					target.state = States.CONFUSION;
					target.stateTemp = target.zspeed;
					target.zspeed = 0;
					target.stateCounter = 4; //will end after 2 secs
				}
					*/
				
				
			}			
			
		}
	}
	public void fireBong()
	{
		if(cooldown_progress.getPercentage()<100)
		{
			return;
		}
		cooldown_progress.runAction(CCProgressFromTo.action(bong_cooldown_time, 0,100));
		
		SoundEngine.sharedEngine().playEffect(context, R.raw.flak_exp);
		CCSprite bong = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("bong.png"));
		bong.setScale(winSize.height/(2f*bong.getContentSize().height));
		bong.setPosition(winSize.width * 0.5f, winSize.height * 0.5f);
		addChild(bong);
		bong.setRotation(-45+rand.nextInt(90));
		bong.runAction(CCSequence.actions(
				CCScaleTo.action(0.1f, winSize.height/(2f*bong.getContentSize().height)+0.1f),
				CCScaleTo.action(0.1f, winSize.height/(2f*bong.getContentSize().height)-0.1f),
				CCRotateBy.action(0.2f, 540f),
				CCFadeOut.action(0.1f),				
				//CCToggleVisibility.action(), 				
				CCCallFuncN.action(this, "spriteMoveFinished")
				));
		
		hudNode.runAction(CCSequence.actions(
				CCMoveBy.action(0.05f, CGPoint.ccp(10,-20)),
				CCMoveBy.action(0.05f, CGPoint.ccp(-20,10)),
				CCMoveBy.action(0.05f, CGPoint.ccp(20,20)),
				CCMoveBy.action(0.05f, CGPoint.ccp(-10,-10)),
				CCMoveTo.action(0.05f, CGPoint.ccp(initialHudPos.x, initialHudPos.y))				
				));
		
		
		for (EnemyBug target : _bugs)
		{	
			if(tomatoFighter.getSelectedWeapon() == 0 && target.state != States.ARMORDOWN) //ARMORDOWN //activated //pineapple
			{				
				target.inStateControl = true;					
				target.state = States.ARMORDOWN;
				target.stateCounter = 4; //will end after 2 secs
				//target.bugSprite.runAction(CCShaky3D.action(5, true, ccGridSize.ccg((int)target.bugSprite.getBoundingBox().size.width,(int)target.bugSprite.getBoundingBox().size.height), target.stateCounter));
				target.bugSprite.runAction(CCBlink.action(target.stateCounter*0.5f, 4)); //2 spins each second
			}
			else if(tomatoFighter.getSelectedWeapon() == 1) //roar //activated //grape
			{				
				target.distance+=100;
				if(target.distance>MAXDISTOFBUG)
					target.distance = MAXDISTOFBUG;
				
				CCTintBy action1 = CCTintBy.action(0.5f, ccColor3B.ccc3(-127, -255, -127));
				target.bugSprite.runAction(CCSequence.actions(action1, action1.reverse()));
			}
			else if(tomatoFighter.getSelectedWeapon() == 2 && target.state != States.CONFUSION) //CONFUSION //corn
			{
				target.inStateControl = true;					
				target.state = States.CONFUSION;
				target.stateTemp = target.zspeed;
				target.zspeed = 0;
				target.stateCounter = 4; //will end after 2 secs
				//target.bugSprite.runAction(CCShaky3D.action(5, true, ccGridSize.ccg((int)target.bugSprite.getBoundingBox().size.width,(int)target.bugSprite.getBoundingBox().size.height), target.stateCounter));
				target.bugSprite.runAction(CCRotateBy.action(target.stateCounter*0.5f, 360*4)); //2 spins each second
			}			
			else if (CGRect.containsRect(CGRect.make(CGPoint.ccp(0,0), winSize), target.bugSprite.getBoundingBox()))//bug in screen
			{	
				if(tomatoFighter.getSelectedWeapon() == 2 && target.state != States.CONFUSION) //CONFUSION //corn
				{
					target.inStateControl = true;					
					target.state = States.CONFUSION;
					target.stateTemp = target.zspeed;
					target.zspeed = 0;
					target.stateCounter = 4; //will end after 2 secs
					//target.bugSprite.runAction(CCShaky3D.action(5, true, ccGridSize.ccg((int)target.bugSprite.getBoundingBox().size.width,(int)target.bugSprite.getBoundingBox().size.height), target.stateCounter));
					target.bugSprite.runAction(CCRotateBy.action(target.stateCounter*0.5f, 360*4)); //2 spins each second
				}
				/*				
				else if(bong_selected_idx==2) //STUPIDUPDOWN
				{
					target.inStateControl = true;					
					target.state = States.STUPIDUPDOWN;
					target.stateTemp = target.zspeed;
					target.zspeed = 0;
					target.stateCounter = 9999; //will end by script
					target.fifoScripts.offer(Scripts.UP); //each 0.5seconds//total 5 seconds
					target.fifoScripts.offer(Scripts.DOWN);
					target.fifoScripts.offer(Scripts.UP);
					target.fifoScripts.offer(Scripts.DOWN);
					target.fifoScripts.offer(Scripts.UP);
					target.fifoScripts.offer(Scripts.DOWN);
					target.fifoScripts.offer(Scripts.UP);
					target.fifoScripts.offer(Scripts.DOWN);
					target.fifoScripts.offer(Scripts.UP);
					target.fifoScripts.offer(Scripts.DOWN);	
					target.fifoScripts.offer(Scripts.SCRIPTEND);
				}
				*/
			}
		}
	}
	public void fireParticle()
	{
		//if(cooldown_progress.getPercentage()<100)
		//{
		//	return;
		//}
		energyBallCoolDownTimer = (int) (energy_progress.getPercentage() / 100);    	
		energy_progress.runAction(CCProgressFromTo.action(energy_cooldown_time*energyBallCoolDownTimer*0.5f, energy_progress.getPercentage(),0));
		//cooldown_progress.runAction(CCProgressFromTo.action(.5f, 0,100));
		
		//energyBallCoolDownTimer = energyLevelState * 2;    	
		//cannonProgress.runAction(CCProgressTo.action(energyBallCoolDownTimer*0.5f, 0));
		//cannonPointer.runAction(CCProgressTo.action(energyBallCoolDownTimer*0.5f, 0));
		//cannonProgress.setPercentage(0);
    	//cannonPointer.setPercentage(0);
    	/*
    	for(int i=0;i<5;i++)
    	{
    		energyFlags[i] = false;
    	}*/
    	//energyBall.runAction(CCScaleTo.action(0.1f,ENERGY_BALL_DEFAULT_SCALE+0.5f));
		
    	//hudNode.removeChild(energyBall,true);
		if(projectiles_Arr.size()>3)
			return;
		/*
		if(projectiles_Arr.size()!=0)
		{
			RealWorldObject tobeFiredProjectile = projectiles_Arr.get(0);
			tobeFiredProjectile.zspeed = 100;
		}*/
		///////projectile.realObject.setPosition(cannon.getPosition());
		//next projectile loaded
		RealWorldObject projectile = new RealWorldObject();			
		// Set up initial projectile
		if(tomatoFighter.getSelectedWeapon() == 2) //corn cannon
		{
			projectile.realObject = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("corn_bullet.png"));
		}
		else if(tomatoFighter.getSelectedWeapon() == 1) //grape
		{
			projectile.realObject = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("grape_bullet.png"));
		}
		else //default cannon
		{
			projectile.realObject = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("corn_bullet.png"));
		}
		projectile.azimuth = azimuth;
		projectile.roll = roll;
		projectile.distance = 0;
		projectile.zspeed = shoo_particle_speed;
		projectile.energyLevel = currentEnergyIndex;
		//if(energyLevelState>0)
		//{
			
		//}
		if(maxScaleForProjectile == 0)
			maxScaleForProjectile = winSize.height/(9f*projectile.realObject.getContentSize().height);			
		projectile.realObject.setScale(maxScaleForProjectile * (projectile.distance * (0.05f-1)/MAXDISTOFBUG + 1));
		projectile.realObject.setOpacity(120);
		
		projectiles_Arr.add(projectile);
		// Determine offset of location to projectile
		////int offX = (int)(location.x - projectile.getPosition().x);
		////int offY = (int)(location.y - projectile.getPosition().y);
		
		// Bail out if we are shooting down or backwards
		//if (offX <= 0)
		//	return true;
		
		// Ok to add now - we've double checked position
		addChild(projectile.realObject);
		
		cannon_flash.runAction(CCSequence.actions(
				CCFadeIn.action(0),
				//CCToggleVisibility.action(), 				
				CCFadeTo.action(0.2f,100),
				CCFadeOut.action(0)
				));
		hudNode.runAction(CCSequence.actions(
				CCMoveBy.action(0.05f, CGPoint.ccp(10,-20)),
				CCMoveBy.action(0.05f, CGPoint.ccp(-20,10)),
				CCMoveBy.action(0.05f, CGPoint.ccp(20,20)),
				CCMoveBy.action(0.05f, CGPoint.ccp(-10,-10)),
				CCMoveTo.action(0.05f, CGPoint.ccp(initialHudPos.x, initialHudPos.y))				
				));
		//projectile.setTag(2);
		//_projectiles.add(projectile);
		
		// Determine where we wish to shoot the projectile to
		////int realY = (int)(winSize.height + (projectile.getContentSize().height / 2.0f));
		////float ratio = (float)offX / (float)offY;
		////int realX = (int)((realY * ratio) + projectile.getPosition().x);
		/*
		int realX = (int)winSize.width/2;
		int realY = (int)winSize.height/2;
		CGPoint realDest = CGPoint.ccp(realX, realY);
		
		// Determine the length of how far we're shooting
		int offRealX = (int)(realX - projectile.getPosition().x);
		int offRealY = (int)(realY - projectile.getPosition().y);
		float length = (float)Math.sqrt((offRealX * offRealX) + (offRealY * offRealY));
		float velocity = 480.0f / 1.0f; // 480 pixels / 1 sec
		float realMoveDuration = length / velocity;
		
		// Move projectile to actual endpoint,shrink to mimic going further
		projectile.runAction(CCSequence.actions(
				CCSpawn.actions(CCMoveTo.action(realMoveDuration, realDest),CCScaleTo.action(realMoveDuration, 0.1f)),
				CCCallFuncND.action(this, "spriteMoveFinished")
				));
		*/
		// Pew!
		//pitchRecorder.setComputeZCR(false);
		////SoundEngine.sharedEngine().playEffect(context, R.raw.flak_shot);
		//pitchRecorder.setComputeZCR(true);
		//_label.setString("test"+i);
		//i++;
		
			
		/*
		energyBall.setPosition(projectile.realObject.getPosition());	
		energyBall.setScale(ENERGY_BALL_DEFAULT_SCALE+0.5f);
		energyBall.runAction(CCRepeatForever.action(
				CCAnimate.action(energy_ball_aAnimation, false)));
		hudNode.addChild(energyBall,4,99);
		*/
		///---------original spritemovefinished
		/*
		CCSprite sprite = (CCSprite)sender;
		
		if (sprite.getTag() == 1)
		{
			_bugs.remove(sprite);
			
			//_projectilesDestroyed = 0;
			//CCDirector.sharedDirector().replaceScene(GameOverLayer.scene("You Lose :("));
		}
		else if (sprite.getTag() == 2)
			_projectiles.remove(sprite);
		
		this.removeChild(sprite, true);*/
	}
	
	private SensorEventListener listener = new SensorEventListener(){
		   //public volatile float direction = (float) 0;
		   //public volatile float inclination;
		   public volatile float rollingZ = (float)0;

		   public volatile float kFilteringFactor = (float)0.05;
		   public float aboveOrBelow = (float)0;
		   
		   DigitalAverage[] averages = {new DigitalAverage(),new DigitalAverage(),new DigitalAverage()};

		   public void onAccuracyChanged(Sensor arg0, int arg1){}

		   public void onSensorChanged(SensorEvent evt)
		   {
		      float vals[] = evt.values;
		      		      
		      if(evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		         {
		    	   //Log.d("D", "in accel");
		        	 //x = vals[0];
			    	  //y = vals[1];
			    	  z = vals[2];
		           // aboveOrBelow =
		           //    (float) ((vals[2] * kFilteringFactor) + 
		           //    (aboveOrBelow * (1.0 - kFilteringFactor)));
		         }
		      if(evt.sensor.getType() == Sensor.TYPE_ORIENTATION)
		      {	 
		    	  //azimuth = (int) vals[0];
		    	  pitch = vals[1];
		    	  //roll = (int) vals[2];
		    	  //azimuth = 340;
		    	  //roll = 70;
		    	  		    		  
		    	  azimuth = averages[0].average(vals[0]);
		    	  roll = averages[2].average(vals[2]);
		    	  //azimuth = vals[0];
		    	  //roll = vals[2];
		    	  
		    	  if(z<0)
		    	  {
		    		  roll = (82-roll) + 82;
		    	  }
		    	  /*
		         float rawDirection = vals[0];

		         direction =(int) ((rawDirection * kFilteringFactor) + 
		            (direction * (1.0 - kFilteringFactor)));

		          inclination = 
		            (int) ((vals[2] * kFilteringFactor) + 
		            (inclination * (1.0 - kFilteringFactor)));

		                
		          if(aboveOrBelow > 0)
		             inclination = inclination * -1;
		          */
		         		         
		      }      
		      //Log.d("DEBUG","Dir:"+direction+"Inc:"+inclination);
		      //tv.setText("Dir:"+direction+"Inc:"+inclination);
		      //_label.setString(new String("Dir:"+direction+"Inc:"+inclination));
		   }
		};
		@Override
    	public void onEnter() {
    		super.onEnter();
    		
    	}
		@Override
    	public void onExit() {
			sensorMan.unregisterListener(listener);
			
			pitchRecorder.setRecording(false);
			pitchThread.stop();
			this.pitchRecorder.setIsEnd(true);
			this.pitchRecorder.setRecording(false);
					
			SoundEngine.sharedEngine().realesAllSounds();
			
  	      	SoundEngine.sharedEngine().setSoundVolume(1f);
  	      	SoundEngine.sharedEngine().playSound(CCDirector.sharedDirector().getActivity(), R.raw.this_is_honkstep, true);
  	      	
  	      	//CCSpriteFrameCache.sharedSpriteFrameCache().removeSpriteFrames();
			
    		super.onExit();
    		
    	}
		@Override
		public void draw(GL10 gl) {
			//HP line
		  	//gl.glDisable(GL10.GL_LINE_SMOOTH);
            //gl.glLineWidth(winSize.height*0.1f);
            //gl.glColor4f(0.0f, 0.8f, 0.0f, 0.8f);
            //CCDrawingPrimitives.ccDrawLine(gl, CGPoint.ccp(winSize.width*0.2f, winSize.height*0.8f), CGPoint.ccp(winSize.width*0.2f+(winSize.width*0.2f*playerHP/100f), winSize.height*0.8f));
            //radar stuff         
            //gl.glPointSize(3);
	        //gl.glColor4f(0.0f, 1.0f, 1.0f, 1.0f);	        
	        //CCDrawingPrimitives.ccDrawPoints(gl, bug_CGPoints, bug_CGPoints_size);    		
	        
            super.draw(gl);
		}
}
