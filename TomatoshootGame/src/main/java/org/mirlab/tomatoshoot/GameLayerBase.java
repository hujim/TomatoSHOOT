package org.mirlab.tomatoshoot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.camera.CCOrbitCamera;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.instant.CCCallFuncND;
import org.cocos2d.actions.instant.CCShow;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCFadeTo;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCProgressFromTo;
import org.cocos2d.actions.interval.CCProgressTo;
import org.cocos2d.actions.interval.CCRepeat;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.actions.interval.CCTintBy;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
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
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.particlesystem.CCParticleFire;
import org.cocos2d.particlesystem.CCParticleFireworks;
import org.cocos2d.particlesystem.CCParticleFlower;
import org.cocos2d.particlesystem.CCParticleMeteor;
import org.cocos2d.particlesystem.CCParticleSnow;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.particlesystem.CCQuadParticleSystem;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCRotoZoomTransition;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.cocos2d.types.ccColor4F;
import org.mirlab.SpeechRecognition.NativeasraInt;
import org.mirlab.SpeechRecognition.pitchRecorder;
import org.mirlab.SpeechRecognition.wordRecRecorder;
import org.mirlab.tomatoshoot.EnemyBug.EnemyTypes;
import org.mirlab.tomatoshoot.EnemyBug.PathTypes;
import org.mirlab.tomatoshoot.EnemyBug.Scripts;
import org.mirlab.tomatoshoot.EnemyBug.States;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

public class GameLayerBase extends CCColorLayer
{
	protected TomatoFighter tomatoFighter;
	Camera mCamera;
	//-----------GameVariables//
	int level_no = 0;
	int loop_no = 0;
	
	int playerLevel = 1;
	int shoo_attack_power;
	int shoo_attack_power_base = 20;
	int shoo_particle_speed = 100;
	int weaponLevel = 0;
	int weaponNumber = 1;
	//--------------------------//
	//-------------save---//
	int money=0;
	//---------------------//
	float bong_cooldown_time= 3.0f; 
	float energy_cooldown_time= 10.0f; 
	float super_cooldown_time = 20f;
	float unbeatable_time;
	int countSec = 0;
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
	static int MAX_ENEMIES_ONSCREEN = 25;
	static Random rand = new Random();
	static String textureFolderPath;
	CGPoint bug_CGPoints[] = new CGPoint[MAX_ENEMIES_ONSCREEN];
	boolean energyFlags[] = {false,false,false,false,false};
	float energyFlagsInt[] = {0,0};
	
	int	bug_CGPoints_size = 0;
	private SensorManager sensorMan;
	public CCBitmapFontAtlas _label;
	public CCBitmapFontAtlas _countLabel;
	public CCBitmapFontAtlas cannon_status;
	public volatile int direction =  0;
	public volatile int inclination;
	float pitch,roll,azimuth;
	float x,y,z;
	float currentPitch = 0;
	int currentEnergyIndex = 0;
	boolean gamePaused = false;
	boolean addBlood = false;
	boolean unbeatable = false;
	boolean ohMyGodLocked = true;
	
	int shoo_selected_idx = 0;
	int bong_selected_idx = 0;
	int energyLevelState = 0;
	float maxScale = 0;
	float maxScaleForProjectile = 0;
	float playerHP;
	//boolean energyBallEnabled = true;
	int energyBallCoolDownTimer = 0;
	int superPowerCoolDownTimer = 0;
	int firingCounter = 0;   //check if audio process too much
	int quickGameTimer = 0;
	int loadingCounter = 0;
	int radioCounter = 0;
	
	int quickGameRoundNum = 1;
	float quickGameScore = 0;
	boolean isQuickGame = false;
	boolean hasShownStory = false; 
	boolean hasShownOpening = false; 
	float quickGameScore_new = 0;
	float quickGameScore_old = 0;
	
	//CCSpriteSheet fly_worm_aSpriteSheet;
	ArrayList<CCSpriteFrame> fly_worm_aArr;
	CCAnimation fly_worm_aAnimation;
	ArrayList<CCSpriteFrame> energy_ball_aArr;
	CCAnimation energy_ball_aAnimation;
	ArrayList<CCSpriteFrame> explosion_Arr;
	CCAnimation explosion_Animation;
	ArrayList<CCSpriteFrame> fire_Arr;
	CCAnimation fire_Animation;
	ArrayList<CCSpriteFrame> armor_down_Arr;
	CCAnimation armor_down_Animation;
	
	ArrayList<EnemyBug> bugsToDelete = new ArrayList<EnemyBug>();		
	ArrayList<RealWorldObject> realWorldObjectsToDelete = new ArrayList<RealWorldObject>();
	ArrayList<RealWorldObject> projectiles_Arr = new ArrayList<RealWorldObject>();
	//CCAction fly_worm_aAction;
		
	CCNode storyNode;
	CCParallaxNode hudNode;
	CGPoint initialHudPos;
	CCSprite hudWindow;
	CCSprite screenRed;
	CCSprite pitchPointer;
	CCSprite energyBall;	
	CCSprite buddhaAngle;
	CCSprite dragonDevil;
	CCSprite pineApple;
	CCSprite FBimage0;
	CCSprite FBimage1;
	CCSprite FBimage2;
	CCSprite redPineApple;
	CCSprite redBar;
	CCSprite ad_aware;
	CCSprite redFrame;
	CCSprite greenFrame;
	CCParticleSystem	energyEmitter;
	CCParticleSystem smokeEmitter,snowEmitter,fireEmitter,smokeBall,addBloodFlower;
	PauseScreenLayer pauseScreenLayer;
	CCMenuItem radioItem;
	CCLabel amount1;
	CCLabel amount2;
    
	CCSprite loading;
	CCLabel loadingCount;
	RadarCCSprite radarSprite;
	CCSprite cannon;
	CCSprite cannon_flash;
	CCSprite cannon_weapon;
	//CCProgressTimer cannonProgress;
	//CCProgressTimer cannonPointer;
	CCProgressTimer hp_progress;
	CCProgressTimer energy_progress;
	CCProgressTimer cooldown_progress;
	CCProgressTimer greenBar,yellowBar;
	CGSize winSize;
	Context context = CCDirector.sharedDirector().getActivity();
	CCBitmapFontAtlas status;
	
	public pitchRecorder pitchRecorder;
	public Thread pitchThread;
	public wordRecRecorder wordRecRecorder;
	public Thread wordRecThread;
	public Thread masterThread;
	
	public static int volThreshold;
	public static int meanVol;
	public static int maxVol;
	int SCREEN_DENSITY;
	static CCScene scene;
	//-----------------story strings
	String [][] storyStrings = {
            {"阿番，是個身材嬌小，卻充滿著雄心壯志的小番茄",
			   "一心只想著報效國家"},
			   
            {"常被欺負的阿番，總是流著番茄汁",
			   "抵死不屈..."},
			   
            {"阿番爺是位天才科學家",
			   "決定要強化阿番成為偉大的番茄戰士！"},
			   
			   {"沒想到...實驗室遭受襲擊",
			   "阿番爺被抓走，只留下一罐梅子..."},
			   
			   {"戰勝臭蟲得意的阿番...終於發現",
			   "番茄與梅子的神奇效應..."},
			   
			   {"沒想到...",
			   "回去村莊時，一切都已經太遲..."},
			   
			   {"OH~~MY~~GOD~~!!!",
			   "WHAT THE..."},
			   
			   {"看著大夥伴們士氣不振",
			   "叮咚，阿番想到了個點子"},
			   
			   {"阿番拎著梅子，喊:\"大家注意!!\"",
			   "\"我們不能輸，對不對!!!\""},
			   
			   {"就這樣...",
			   "阿番帶著他的番茄大軍前進..."},
			   
			   {"經歷一場硬仗後的番茄",
			   "\"唷呼~~堅持到底就對了\""},
			   
			   {"但...阿番隊長眉頭一皺",
			   "發現此案情並不單純..."},
			   
			   {"阿番知道這將會是一場世紀大對決",
			   "決定不連累同胞，獨自前往應戰!"},
			   
			   {"阿番進入了他的內心世界",
			   "獨自回想起爺爺對他的愛戴與期望..."},
			   
			   {"是決一死戰的時候了!!!",
			   "\"可惡的臭蟲，我要把你解決光光!!\""}
	};

	
	//------bugs path for quick game---------------------
	Scripts[][] scriptListsA= {
			// general types
			{Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP},
			{Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN},
			{Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT},
			{Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT},
			// Z-word 
			{Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,
				Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT},
			// M-word
			{Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,
				Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN},
			// W-word
			{Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,
				Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,
				Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,
				Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT},	
			// lightenig
			{Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,
				Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,
				Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT},				
			
	};
	Scripts[][] scriptListsB= {
			{Scripts.SCRIPTEND}
	};
	Scripts[][] scriptListsDepth= {
			{Scripts.TOWARD,Scripts.TOWARD,Scripts.TOWARD,Scripts.TOWARD},
			{Scripts.AWAY,Scripts.AWAY,Scripts.TOWARD,Scripts.TOWARD,Scripts.TOWARD,Scripts.TOWARD},
			{Scripts.TOWARD,Scripts.TOWARD,Scripts.AWAY,Scripts.TOWARD,Scripts.TOWARD,Scripts.AWAY},
			{Scripts.TOWARD,Scripts.AWAY,Scripts.TOWARD,Scripts.AWAY,Scripts.TOWARD,Scripts.TOWARD}
	};
	int quickGameBugNum = 0;
	//------------------------------
	
	public static CCScene scene()
	{
		scene = CCScene.node();
		scene.setTag(2);
		GameLayerBase layer = new GameLayerBase(ccColor4B.ccc4(0, 0, 0, 0));
		layer.setTag(0);
		//scene.setVisible(false);
		scene.addChild(layer);
		
		return scene;
	}

	protected GameLayerBase(ccColor4B color)
	{
		super(color);
		
	}
	@Override
	public void onEnterTransitionDidFinish()
	{
		super.onEnterTransitionDidFinish();
		winSize = CCDirector.sharedDirector().displaySize();
		tomatoFighter = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).tomatoFighter;
		mCamera = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).mCamera;
		
		if(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition)
		{			
			this.schedule("loadingCount", 0.02f);			
			CCColorLayer storyNode = CCColorLayer.node(ccColor4B.ccc4(0, 0, 0, 255));
			hasShownStory = false;
			
			String temp = "lesson"+((tomatoFighter.getCurGameLevel()-1)%6+1)+".jpg";
			Log.d("lesson",temp);
			CCSprite lessonpic = CCSprite.sprite(temp);
			lessonpic.setScale(winSize.height/(1.5f*lessonpic.getContentSize().height));
			lessonpic.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.65f));				
			storyNode.addChild(lessonpic,1);
			
			loading = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("loading.png"));
			loading.setScale(winSize.height/(5.5f*loading.getContentSize().height));
			loading.setPosition(CGPoint.ccp(winSize.width * 0.18f, winSize.height * 0.1f));	
			loading.runAction(CCRepeatForever.action(CCSequence.actions(CCFadeIn.action(0.5f),CCFadeOut.action(0.5f))));
			storyNode.addChild(loading,1);
			
			
			loadingCount = CCLabel.makeLabel("0%", "Arial", 35);
			loadingCount.setPosition(CGPoint.ccp(winSize.width * 0.75f, winSize.height * 0.1f));
			loadingCount.setColor(ccColor3B.ccWHITE);
			storyNode.addChild(loadingCount,3);
			
			addChild(storyNode);
		}
		
	}
	public void loadingCount(float dt)
	{
		loadingCount.setString((loadingCounter*2)+"%");
		if(loadingCounter>50)
		{
			loading.setVisible(false);
			loadingCount.setString("Click to continue...");
			loadingCount.runAction(CCRepeatForever.action(CCSequence.actions(CCFadeIn.action(0.5f),CCFadeOut.action(0.5f))));
			this.setIsTouchEnabled(true);
			this.setIsKeyEnabled(false); //later...			
			this.unschedule("loadingCount");
		}
		loadingCounter++;
		
	}
	public void runStory()
	{
		storyNode =  CCColorLayer.node(ccColor4B.ccc4(0, 0, 0, 255));
		hasShownStory = false;
		this.setIsTouchEnabled(true);
		this.setIsKeyEnabled(false); //later...	
		String temp = "storyPics/"+tomatoFighter.getCurGameLevel()+".jpg";
		Log.e("storyPic",temp);
		CCSprite storypic = CCSprite.sprite(temp);
		storypic.setScale(winSize.height/(1f*storypic.getContentSize().height));
		storypic.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f));				
		storyNode.addChild(storypic,1);				
		CCSprite storyFrame1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("story_frame.png"));
		storyFrame1.setScale(winSize.width/(1f*storyFrame1.getContentSize().width));
		storyFrame1.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height -storyFrame1.getContentSize().height * 0.5f));				
		storyNode.addChild(storyFrame1,2);
		CCSprite storyFrame2 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("story_frame.png"));
		storyFrame2.setScale(winSize.width/(1f*storyFrame2.getContentSize().width));
		storyFrame2.setPosition(CGPoint.ccp(winSize.width * 0.5f, storyFrame2.getContentSize().height * 0.5f));		
		storyFrame2.setFlipY(true);		
		storyNode.addChild(storyFrame2,2);
		
		CCLabel text1 = CCLabel.makeLabel(storyStrings[tomatoFighter.getCurGameLevel()-1][0], "Arial", 35);
		text1.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text1.getContentSize().height * 1.f));
		text1.setColor(ccColor3B.ccc3(73, 153, 52));
		CCSprite textBG1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("story_sub_bg.png"));
		textBG1.setScaleX(text1.getContentSize().width/(1f*textBG1.getContentSize().width));
		textBG1.setScaleY(text1.getContentSize().height/(1f*textBG1.getContentSize().height));
		textBG1.setPosition(text1.getContentSize().width/2f, text1.getContentSize().height/2f);
		text1.addChild(textBG1,-1);
		storyNode.addChild(text1,3);
		CCFiniteTimeAction subupAction = CCSpawn.actions(CCFadeIn.action(4), CCMoveBy.action(4, CGPoint.ccp(0,winSize.height*0.5f)));
		CCFiniteTimeAction subupActionOut = CCSpawn.actions(CCFadeOut.action(4), CCMoveBy.action(8, CGPoint.ccp(0,winSize.height*1.0f)));
		CCAction subUpSeq1 = CCSequence.actions(subupAction,subupActionOut,CCCallFuncN.action(this, "spriteMoveFinished"));
		text1.runAction(subUpSeq1);
		CCLabel text2 = CCLabel.makeLabel(storyStrings[tomatoFighter.getCurGameLevel()-1][1], "Arial", 35);
		text2.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text2.getContentSize().height * 1.f));
		text2.setColor(ccColor3B.ccc3(73, 153, 52));
		CCSprite textBG2 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("story_sub_bg.png"));
		textBG2.setScaleX(text2.getContentSize().width/(1f*textBG2.getContentSize().width));
		textBG2.setScaleY(text2.getContentSize().height/(1f*textBG2.getContentSize().height));
		textBG2.setPosition(text2.getContentSize().width/2f, text2.getContentSize().height/2f);
		text2.addChild(textBG2,-1);
		storyNode.addChild(text2,3);
		CCAction subUpSeq2 = CCSequence.actions(CCDelayTime.action(4),subupAction,subupActionOut,CCCallFuncN.action(this, "spriteMoveFinished"));
		text2.runAction(subUpSeq2);
		//textBG2.runAction(subUpSeq2);
		
		CCLabel text4 = CCLabel.makeLabel("Click to continue...", "Arial", 30);
		text4.setPosition(CGPoint.ccp(winSize.width - text4.getContentSize().width* 0.5f,  text4.getContentSize().height* 0.5f));
		text4.setColor(ccColor3B.ccBLACK);
		CCSprite textBG3 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("story_sub_bg.png"));
		textBG3.setScaleX(text4.getContentSize().width/(1f*textBG3.getContentSize().width));
		textBG3.setScaleY(text4.getContentSize().height/(1f*textBG3.getContentSize().height));
		textBG3.setPosition(text4.getContentSize().width/2f, textBG3.getContentSize().height/2f);
		text4.addChild(textBG3,-1);
		storyNode.addChild(text4,3);
		CCAction subUpSeq4 = CCSequence.actions(CCFadeOut.action(0),CCDelayTime.action(8),CCFadeIn.action(0.5f),CCRepeat.action(CCSequence.actions(CCFadeIn.action(0.5f),CCFadeOut.action(0.5f)), 99));
		text4.runAction(subUpSeq4);
		//textBG3.runAction(subUpSeq4);
		
		
		addChild(storyNode,1);
	}
	public void runOpening()
	{
		storyNode = CCColorLayer.node(ccColor4B.ccc4(0, 0, 0, 255));
		hasShownStory = false;
		this.setIsTouchEnabled(true);
		this.setIsKeyEnabled(false); //later...	
		/*String temp = "storyPics/"+tomatoFighter.getCurGameLevel()+".jpg";
		Log.e("storyPic",temp);
		CCSprite storypic = CCSprite.sprite(temp);
		storypic.setScale(winSize.height/(1f*storypic.getContentSize().height));
		storypic.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.5f));				
		storyNode.addChild(storypic,1);				
		CCSprite storyFrame1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("story_frame.png"));
		storyFrame1.setScale(winSize.width/(1f*storyFrame1.getContentSize().width));
		storyFrame1.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height -storyFrame1.getContentSize().height * 0.5f));				
		storyNode.addChild(storyFrame1,2);
		CCSprite storyFrame2 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("story_frame.png"));
		storyFrame2.setScale(winSize.width/(1f*storyFrame2.getContentSize().width));
		storyFrame2.setPosition(CGPoint.ccp(winSize.width * 0.5f, storyFrame2.getContentSize().height * 0.3f));				
		storyNode.addChild(storyFrame2,2);
		*/
		CCFiniteTimeAction subupAction = CCSpawn.actions(CCFadeIn.action(3), CCMoveBy.action(3, CGPoint.ccp(0,winSize.height*0.5f)));
		CCFiniteTimeAction subupActionOut = CCSpawn.actions(CCFadeOut.action(3), CCMoveBy.action(6, CGPoint.ccp(0,winSize.height*1.0f)));
		CCAction subUpSeq1 = CCSequence.actions(subupAction,subupActionOut);
		int offset = 3;
		if(tomatoFighter.getCurGameLoopNum() > 0)
		{
			offset = 0;
			CCLabel text1 = CCLabel.makeLabel("...1萬個蕃茄日後....", "Arial", 30);
			text1.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text1.getContentSize().height * 1.f));
			text1.setColor(ccColor3B.ccWHITE);
			storyNode.addChild(text1,3);
			text1.runAction(subUpSeq1);
		}
		CCLabel text2 = CCLabel.makeLabel("在浩瀚的宇宙中，某異次元空間裡，", "Arial", 30);
		text2.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text2.getContentSize().height * 1.f));
		text2.setColor(ccColor3B.ccWHITE);
		storyNode.addChild(text2,3);
		CCAction subUpSeq2 = CCSequence.actions(CCDelayTime.action(3-offset),subupAction,subupActionOut);
		text2.runAction(subUpSeq2);
		CCLabel text25 = CCLabel.makeLabel("將面臨一場空前絕後的戰爭", "Arial", 30);
		text25.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text25.getContentSize().height * 1.f));
		text25.setColor(ccColor3B.ccWHITE);
		storyNode.addChild(text25,3);
		CCAction subUpSeq25 = CCSequence.actions(CCDelayTime.action(6-offset),subupAction,subupActionOut);
		text25.runAction(subUpSeq25);
		
		CCLabel text3 = CCLabel.makeLabel("....邪惡的臭蟲帝國...與失落的蕃茄國度...", "Arial", 30);
		text3.setPosition(CGPoint.ccp(winSize.width * 0.5f, -text3.getContentSize().height * 1.f));
		text3.setColor(ccColor3B.ccWHITE);
		storyNode.addChild(text3,3);
		CCAction subUpSeq3 = CCSequence.actions(CCDelayTime.action(9-offset),subupAction,subupActionOut);
		text3.runAction(subUpSeq3);		
		CCLabel text4 = CCLabel.makeLabel("Click to continue...", "Arial", 30);
		text4.setPosition(CGPoint.ccp(winSize.width - text4.getContentSize().width* 0.5f,  text4.getContentSize().height* 0.5f));
		text4.setColor(ccColor3B.ccWHITE);
		storyNode.addChild(text4,3);
		CCAction subUpSeq4 = CCSequence.actions(CCFadeOut.action(0),CCDelayTime.action(12-offset),CCFadeIn.action(0.5f),CCRepeat.action(CCSequence.actions(CCFadeIn.action(0.5f),CCFadeOut.action(0.5f)), 99));
		text4.runAction(subUpSeq4);
		
		
		
		addChild(storyNode,1);
	}
	public void initContents()
	{
		
		_bugs = new ArrayList<EnemyBug>();
		//_projectiles = new ArrayList<CCSprite>();
		_bugKilled = 0;
		
		playerHP = 100;		
		if(tomatoFighter.getPlayerLevel()<7)
			ohMyGodLocked = true;
		else
			ohMyGodLocked = false;
		
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
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("allBugs_I.plist");
		
		armor_down_Arr = new ArrayList<CCSpriteFrame>();
		for(int i = 0; i < 2; i++) {		    
			armor_down_Arr.add(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("animated_armor_down_a_"+i+".png"));
		}
		armor_down_Animation = CCAnimation.animation("armor_down_Animation", 0.2f, armor_down_Arr);
		
		//CCTextureCache.sharedTextureCache().addImage("bong.png");
		
		// Handle sound
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.flak_exp);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.pow);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.shields1);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.flak_shot);
		SoundEngine.sharedEngine().setSoundVolume(0.2f);
		
		status = CCBitmapFontAtlas.bitmapFontAtlas(" ", "Arial.fnt");
		status.setColor(ccColor3B.ccGRAY);
		//status.runAction(CCFadeOut.action(0));
		status.setScale(winSize.height/(14f*status.getContentSize().height));
		status.setPosition(CGPoint.ccp(winSize.width * 0.5f, winSize.height * 0.2f));
		addChild(status,71);
		
		
		
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
		//cannon.setPosition(CGPoint.ccp(winSize.width * 0.5f, cannon.getContentSize().height / 2.0f));		
		hudNode.addChild(cannon,0,1.4f+screenshake_offset, 1.4f+screenshake_offset, winSize.width * 0.5f, cannon.getContentSize().height / 2.0f);
		/*
		cannonProgress = CCProgressTimer.progress("cannon_energy_mountup.png");
		cannonProgress.setType(CCProgressTimer.kCCProgressTimerTypeVerticalBarBT);
		cannonProgress.setScale(cannon.getContentSize().height/(1.0f*cannonProgress.getContentSize().height));
		cannonProgress.setPosition(CGPoint.ccp(cannon.getPosition().x-cannon.getContentSize().width*0.7f, cannon.getPosition().y-cannon.getContentSize().height*0.5f));
		hudNode.addChild(cannonProgress,2,1.4f+screenshake_offset, 1.4f+screenshake_offset,cannon.getPosition().x,cannon.getPosition().y);
		
		cannonPointer = CCProgressTimer.progress("cannon_pointer.png");
		cannonPointer.setType(CCProgressTimer.kCCProgressTimerTypeVerticalBarBT);
		cannonPointer.setScale(cannon.getContentSize().height/(1.0f*cannonPointer.getContentSize().height));
		cannonPointer.setPosition(CGPoint.ccp(cannon.getPosition().x-cannon.getContentSize().width*0.7f, cannon.getPosition().y-cannon.getContentSize().height*0.5f));
		hudNode.addChild(cannonPointer,1,1.4f+screenshake_offset, 1.4f+screenshake_offset,cannon.getPosition().x,cannon.getPosition().y);
		*/
		
		
		cannon_flash = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("cannon_flash.png"));		
		//cannon_flash.setPosition(CGPoint.ccp(winSize.width * 0.5f, cannon.getContentSize().height*1.0f));	
		cannon_flash.setOpacity(0);
		hudNode.addChild(cannon_flash,5,1.4f+screenshake_offset, 1.4f+screenshake_offset, winSize.width * 0.5f, cannon.getContentSize().height*1.0f);
		
		
		
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
		energyEmitter.setScale(energyBall.getScale()*0.4f);
		//addChild(energyEmitter);
		
		radarSprite = RadarCCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("radar.png"));
		//radarSprite = RadarCCSprite.sprite("radar.png");
		//radarSprite.setPosition(CGPoint.ccp(radarSprite.getContentSize().width / 2.0f+winSize.width * 0.07f, radarSprite.getContentSize().height / 2.0f+winSize.height * 0.12f));
		DISTANCE_RADAR_RATIO = radarSprite.getContentSize().width/(2*MAXDISTOFBUG);
		radarSprite.setRadarPoints(bug_CGPoints, bug_CGPoints_size);
		radarSprite.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 10, 0, 0));
		hudNode.addChild(radarSprite,15, 0.4f+screenshake_offset, 0.4f+screenshake_offset,radarSprite.getContentSize().width / 2.0f+winSize.width * 0.065f, radarSprite.getContentSize().height / 2.0f+winSize.height * 0.08f);
		
		CCSprite hp_frame = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("hp_frame.png"));
		hp_frame.setScale(winSize.height/(4f*hp_frame.getContentSize().height));
		hp_frame.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 14, 0, 0));
		hudNode.addChild(hp_frame,15, 0.4f+screenshake_offset, 0.4f+screenshake_offset,hp_frame.getContentSize().width * 0.0f+winSize.width * 0.14f, hp_frame.getContentSize().height * 0.0f+winSize.height * 0.72f);
		hp_progress = CCProgressTimer.progress("hp_full.png");
		hp_progress.setType(CCProgressTimer.kCCProgressTimerTypeVerticalBarBT);
		hp_progress.setScale(winSize.height/(4f*hp_progress.getContentSize().height));
		hp_progress.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 14, 0, 0));
		hudNode.addChild(hp_progress,16, 0.4f+screenshake_offset, 0.4f+screenshake_offset,hp_progress.getContentSize().width * 0.0f+winSize.width * 0.14f, hp_progress.getContentSize().height * 0.0f+winSize.height * 0.72f);
		
		energy_progress = CCProgressTimer.progress("energy_level.png");
		energy_progress.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarLR);
		energy_progress.setScale(winSize.width/(7.5f*hp_progress.getContentSize().width));
		hudNode.addChild(energy_progress,16, 0.4f+screenshake_offset, 0.4f+screenshake_offset,winSize.width * 0.495f, winSize.height * 0.76f);
		
		cooldown_progress = CCProgressTimer.progress("cooldown_bar.png");
		cooldown_progress.setType(CCProgressTimer.kCCProgressTimerTypeRadialCW);
		cooldown_progress.setScale(winSize.width/(14.0f*cooldown_progress.getContentSize().width));
		cooldown_progress.setPercentage(100);
		hudNode.addChild(cooldown_progress,16, 0.4f+screenshake_offset, 0.4f+screenshake_offset,winSize.width * 0.33f, winSize.height * 0.07f);
		
		
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
		//pitchPointer.setPosition(CGPoint.ccp(cannon.getPosition().x+cannon.getContentSize().width/2, cannon.getPosition().y-cannon.getContentSize().height/2));				
		hudNode.addChild(pitchPointer,10,1.4f+screenshake_offset, 1.4f+screenshake_offset, cannon.getPosition().x+cannon.getContentSize().width/2, cannon.getPosition().y-cannon.getContentSize().height/2);
		
		cannon_status = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		cannon_status.setColor(ccColor3B.ccc3(54, 206, 224));
		//cannon_status.setPosition(winSize.width * 0.2f, winSize.height * 0.4f);
		hudNode.addChild(cannon_status,15,1.4f+screenshake_offset, 1.4f+screenshake_offset,winSize.width * 0.5f, winSize.height * 0.7f);

		_label = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		_label.setColor(ccColor3B.ccBLACK);
		_label.setPosition(winSize.width * 0.2f, winSize.height * 0.5f);
		//addChild(_label,999);
		
		CCSprite tempS = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("pause_btn.png"));
		CCMenuItem pause_btn = CCMenuItemSprite.item(
				tempS, 
				tempS, 
				this, "pauseGame");
		pause_btn.setScale(winSize.height/(9f*pause_btn.getContentSize().height));	
		
		CCMenu pause_menu = CCMenu.menu(pause_btn);
		hudNode.addChild(pause_menu,16, 0.3f+screenshake_offset, 0.3f+screenshake_offset,winSize.width * 0.5f, winSize.height * 0.05f);
		
		
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
				
		ad_aware = CCSprite.sprite("ad-aware.png");
		ad_aware.setScale(0.65f * winSize.width/(6.25f*radioItem.getContentSize().width));
		ad_aware.setPosition(CGPoint.ccp(winSize.width * 0.87f, winSize.height * 0.17f));
		ad_aware.runAction(CCOrbitCamera.action(0.0f,1, 0, 0, 13, 0, 0));	
		ad_aware.setVisible(false);
		addChild(ad_aware,71);
		
		CCMenu weaponMenu = CCMenu.menu();;
		CCMenuItemSprite weapon1 = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tool_btn_up.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tool_btn_down.png")), this, "clickTool");
		weapon1.setTag(0);
		weapon1.setScale(winSize.width/(9*weapon1.getContentSize().width));
		CCSprite tool1 = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedPropSprite[0];
		if(tool1!=null)
		{
			tool1.setScale(weapon1.getContentSize().height/(1.4f*tool1.getContentSize().height));
			tool1.setPosition(CGPoint.ccp(weapon1.getContentSize().width/2,weapon1.getContentSize().height/2));
			amount1 = CCLabel.makeLabel(""+tomatoFighter.getPropNum(tomatoFighter.getSelectedProp()[0]), "Arial", 35);
			amount1.setColor(ccColor3B.ccBLACK);
			amount1.setScale(weapon1.getContentSize().height/(5f*amount1.getContentSize().height));
			amount1.setPosition(CGPoint.ccp(weapon1.getContentSize().width*0.75f,weapon1.getContentSize().height*0.75f));
			//amount1.setTag(1);
			weapon1.addChild(amount1, 2);
			weapon1.addChild(tool1, 1);
			weaponMenu = CCMenu.menu(weapon1);
			weaponMenu.setColor(ccColor3B.ccc3(255,255,255));
			weaponMenu.setPosition(CGPoint.ccp(winSize.width * 0.9f, winSize.height * 0.7f));
			weaponMenu.alignItemsVertically(5);
			addChild(weaponMenu,100);
		}
		
		CCMenuItemSprite weapon2 = CCMenuItemSprite.item(
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tool_btn_up.png")), 
				CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tool_btn_down.png")), this, "clickTool");
		weapon2.setTag(1);
		weapon2.setScale(winSize.width/(9*weapon2.getContentSize().width));
		CCSprite tool2 = ((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedPropSprite[1];
		if(tool2!=null)
		{
			tool2.setScale(weapon1.getContentSize().height/(1.4f*tool2.getContentSize().height));
			tool2.setPosition(CGPoint.ccp(weapon2.getContentSize().width/2,weapon2.getContentSize().height/2));
			amount2 = CCLabel.makeLabel(""+tomatoFighter.getPropNum(tomatoFighter.getSelectedProp()[1]), "Arial", 35);
			amount2.setColor(ccColor3B.ccBLACK);
			amount2.setScale(weapon1.getContentSize().height/(5f*amount2.getContentSize().height));
			amount2.setPosition(CGPoint.ccp(weapon1.getContentSize().width*0.75f,weapon2.getContentSize().height*0.75f));
			//amount2.setTag(1);
			weapon2.addChild(amount2, 2);
			weapon2.addChild(tool2, 1);
			weaponMenu = CCMenu.menu(weapon1,weapon2);
			weaponMenu.setColor(ccColor3B.ccc3(255,255,255));
			weaponMenu.setPosition(CGPoint.ccp(winSize.width * 0.9f, winSize.height * 0.7f));
			weaponMenu.alignItemsVertically(5);
			addChild(weaponMenu,100);
		}
				
				
		if(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedFriendSprite[0].getTag()!= 77)
			FBimage0 = CCSprite.sprite(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedFriendSprite[0].getTexture());
		else
			FBimage0 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tomato.png"));
			
		if(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedFriendSprite[1].getTag()!= 77)
			FBimage1 = CCSprite.sprite(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedFriendSprite[1].getTexture());
		else
			FBimage1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tomato.png"));
		
		if(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedFriendSprite[2].getTag()!= 77)
			FBimage2 = CCSprite.sprite(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedFriendSprite[2].getTexture());
		else
			FBimage2 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tomato.png"));
//		FBimage1 = CCSprite.sprite(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedFriendSprite[1].getTexture());
//		FBimage2 = CCSprite.sprite(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).selectedFriendSprite[2].getTexture());
		//大絕招
		//ohMyGod
		smokeBall = CCParticleFire.node(250);
		smokeBall.setTexture(CCTextureCache.sharedTextureCache().addImage("fire.png"));
		fireEmitter = CCParticleFlower.node(500);
		fireEmitter.setTexture(CCTextureCache.sharedTextureCache().addImage("stars_grayscale.png"));
		fireEmitter.setScale(2.0f * (winSize.width/(25.0f * 32)));
		redFrame = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("redFrame.png"));
		//redFrame.setPosition(CGPoint.ccp(cannon.getContentSize().width / 2, cannon.getContentSize().height / 2));
		
		//Haleiluya
		smokeEmitter = CCParticleMeteor.node(150);
		smokeEmitter.setTexture(CCTextureCache.sharedTextureCache().addImage("fire.png"));
		snowEmitter = CCParticleSnow.node(120);
		snowEmitter.setTexture(CCTextureCache.sharedTextureCache().addImage("stars_grayscale.png"));
		//snowEmitter.setScale(3.0f * (winSize.width/(25.0f * 32)));
		addBloodFlower =  new CCQuadParticleSystem(50);
		addBloodFlower.setTexture(CCTextureCache.sharedTextureCache().addImage("stars_grayscale.png"));
		greenFrame = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("greenFrame.png"));
		
		buddhaAngle = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("buddhaAngle.png"));
		dragonDevil = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("dragonDevil.png"));
		pineApple = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("pineApple.png"));
		//FBimage = CCSprite.sprite("test.jpg");
		
		
		redPineApple = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("redPineApple.png"));		
		redPineApple.setPosition(CGPoint.ccp(winSize.width * 0.14f, winSize.height * 0.72f));
		redPineApple.setScale(winSize.width / (8.0f * pineApple.getContentSize().width));
		redPineApple.setOpacity(125);
		redPineApple.setVisible(false);
		hudNode.addChild(redPineApple,21,1.0f,1.0f,winSize.width * 0.14f, winSize.height * 0.72f);
		
		_countLabel = CCBitmapFontAtlas.bitmapFontAtlas("", "Arial.fnt");
		_countLabel.setContentSize(1.0f, 1.0f);
		_countLabel.setColor(ccColor3B.ccBLACK);
		_countLabel.setPosition(CGPoint.ccp(pineApple.getContentSize().width / 2, pineApple.getContentSize().height * 0.7f));
		_countLabel.setVisible(false);
		_countLabel.setString("15");
		pineApple.addChild(_countLabel);
		
		//volume vertical bar
		redBar = CCSprite.sprite("redBar.png");
		redBar.setScaleY(10.0f * winSize.width / (16.0f * redBar.getContentSize().width));
		redBar.setScaleX(2.5f * winSize.width / (16.0f * redBar.getContentSize().width));
		redBar.setPosition(CGPoint.ccp(winSize.width * 0.135f,winSize.height * 0.4f));
		redBar.setRotation(90f);
		//redBar.runAction(CCOrbitCamera.action(0.0f, 1, 0, 0, 14, 0, 0));
		hudNode.addChild(redBar,17,1.4f+screenshake_offset, 1.4f+screenshake_offset,winSize.width * 0.135f,winSize.height * 0.4f);
		greenBar = CCProgressTimer.progress("greenBar.png");
		greenBar.setScaleY(10.0f * winSize.width / (16.0f * greenBar.getContentSize().width));
		greenBar.setScaleX(2.5f * winSize.width / (16.0f * greenBar.getContentSize().width));
		greenBar.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarLR);
		greenBar.setPosition(CGPoint.ccp(winSize.width * 0.135f,winSize.height * 0.4f));
		greenBar.setRotation(-90f);
		//greenBar.runAction(CCOrbitCamera.action(0.0f, 1, 0, 0, 14, 0, 0));
		hudNode.addChild(greenBar,19,1.4f+screenshake_offset, 1.4f+screenshake_offset,winSize.width * 0.135f,winSize.height * 0.4f);
		yellowBar = CCProgressTimer.progress("yellowBar.png");
		yellowBar.setScaleY(10.0f * winSize.width / (16.0f * yellowBar.getContentSize().width));
		yellowBar.setScaleX(2.5f * winSize.width / (16.0f * yellowBar.getContentSize().width));
		yellowBar.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarLR);
		yellowBar.setPosition(CGPoint.ccp(winSize.width * 0.135f,winSize.height * 0.4f));
		yellowBar.setRotation(-90f);
		//yellowBar.runAction(CCOrbitCamera.action(0.0f, 1, 0, 0, 14, 0, 0));
		yellowBar.setPercentage((float)(meanVol * 100f / maxVol));
		hudNode.addChild(yellowBar,18,1.4f+screenshake_offset, 1.4f+screenshake_offset,winSize.width * 0.135f,winSize.height * 0.4f);
		//CCDirector.sharedDirector().getOpenGLView().setVisibility(1);	
		// Get instance of Vibrator from current Context
		v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);		 
		
		///--------------initGameVariables
		weaponLevel = tomatoFighter.getSelectedWeaponLevel();
		weaponNumber = tomatoFighter.getSelectedWeapon();
		level_no = tomatoFighter.getCurGameLevel();
		loop_no = tomatoFighter.getCurGameLoopNum();
		playerLevel = tomatoFighter.getPlayerLevel();
		
		if(weaponNumber == 0) //corn cannon
		{
			shoo_attack_power=(int) (20+(weaponLevel+1)*Math.floor((weaponLevel+1)/3));
			shoo_particle_speed=45+(weaponLevel+1)*5;
			cannon_weapon = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("corn_cannon.png"));		
			
		}
		else if(weaponNumber == 2) //grape
		{
			shoo_attack_power=(int) (25+(weaponLevel+1)*Math.floor((weaponLevel+1)/3));
			shoo_particle_speed=45+(weaponLevel+1)*5;
			cannon_weapon = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("grape_cannon.png"));		
			
		}
		else if(weaponNumber == 1) //mellon
		{
			shoo_attack_power=(int) (20+(weaponLevel+1)*Math.floor((weaponLevel+1)/3));
			shoo_particle_speed=45+(weaponLevel+1)*5;
			cannon_weapon = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("mellon_cannon.png"));		
			
		}
		else //default cannon
		{
			shoo_attack_power=(int) (20+(weaponLevel+1)*Math.floor((weaponLevel+1)/3));
			shoo_particle_speed=45+(weaponLevel+1)*5;
			cannon_weapon = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("corn_cannon.png"));		
			
		}
		cannon_weapon.setScale(cannon.getContentSize().height/(4.0f*cannon_weapon.getContentSize().height));
		cannon_weapon.setRotation(-45f);
		//cannon_weapon.setPosition(CGPoint.ccp(cannon.getPosition().x-cannon.getContentSize().width*0.3f, cannon.getPosition().y-cannon.getContentSize().height*0.01f));
		hudNode.addChild(cannon_weapon,-1,1.4f+screenshake_offset, 1.4f+screenshake_offset, cannon.getPosition().x-cannon.getContentSize().width*0.27f, cannon.getPosition().y+cannon.getContentSize().height*0.01f);
		
		
		///-------------------------------
		
		startSensors();
		
		pitchRecorder = new pitchRecorder(meanVol,volThreshold,this,1);
		pitchThread = new Thread(pitchRecorder);
		pitchRecorder.setwaveFileName(new File("/sdcard/recData/output/rec.wav"));
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
		//wordRecRecorder = new wordRecRecorder(this);
		
		this.setIsTouchEnabled(true);
		this.setIsKeyEnabled(true);
		
		Camera.Parameters parameters = mCamera.getParameters();
    	parameters.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE); 
        mCamera.setParameters(parameters);        
        
        /*SoundEngine.sharedEngine().realesAllSounds();
		SoundEngine.sharedEngine().setSoundVolume(0.5f);
		SoundEngine.sharedEngine().playSound(context, R.raw.mr_reno, true);*/
		startSchedulers();
	    
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = false;
	}
	public void startSensors() {
		// TODO Auto-generated method stub
		sensorMan = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensorMan.unregisterListener(listener);
		sensorMan.registerListener(listener, sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
		sensorMan.registerListener(listener, sensorMan.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);
		
	}

	public void startSchedulers()
	{
		this.schedule("gameLogic", 0.5f);
		this.schedule("updatePitch", 0.2f);
		this.schedule("update", 0.05f);//update logics 20fps
		this.schedule("bugMove", BUGMOVEUPDATEFREQ);//update logics 20fps
		this.schedule("checkFiringCounter", 10.0f);
		
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
    			radioItem.setIsEnabled(true);
    		}
    	}
    	catch (Exception e){ 
    		e.printStackTrace();
    	}
	}
	public void radioBtn_count(float dt)
	{
		radioCounter++;
	}
	public void radio_btn_pressed(Object sender)
	{		
		//Log.d("DDDDD","radio_btn_pressed");
//		if(tomatoFighter.getPlayerLevel()<2)
//		{
//			((TomatoshootGame)CCDirector.sharedDirector().getActivity()).toastMessage("等級不足大絕招 封印中");			
//			return;
//		}
		
		this.schedule("radioBtn_count",0.5f);
		
		//pitchRecorder.setRecording(false);
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
		pitchRecorder.masterOpen = true;
		// Vibrate for 300 milliseconds
		v.vibrate(100);

	}
	public void radio_btn_released(Object sender)
	{		
		//Log.d("DDDDD","radio_btn_released");	
//		if(tomatoFighter.getPlayerLevel()<4)
//		{
//			return;
//		}
		this.unschedule("radioBtn_count");
		
		if(radioCounter>1.5f)
		{
			v.vibrate(400);
			//wordRecRecorder.masterOpen = true;
			//wordRecRecorder.setRecording(false);
			pitchRecorder.masterOpen = false;
			pitchRecorder.masterEnd = true;
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
			radioItem.setIsEnabled(false);
			ad_aware.setVisible(true);
		}
		else
		{
			pitchRecorder.masterOpen = false;
			pitchRecorder.tooShort = true;
			//wordRecRecorder.masterOpen = false;
			//wordRecRecorder.setRecording(false);
			((TomatoshootGame)CCDirector.sharedDirector().getActivity()).toastMessage("請按著念完大絕招，再放開~！");
		}
		radioCounter=0;
	}
	public void statusUpdateString(ccColor3B c, CharSequence t)
	{
		try {
			status.setColor(c);				
			status.setString(t);				
			status.runAction(CCSequence.actions(CCFadeIn.action(0.0f),CCDelayTime.action(0.5f),CCFadeOut.action(1f)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void clickTool(Object sender)
	{		
		CCMenuItem item = (CCMenuItem) sender;
		Log.d("DDDDD","testClick"+item.getTag());		
		///tomatoFighter.setMoney(9999);
		int effectNum = 6;
		if(item.getTag() == 0 ) //1st tool
		{
			effectNum = tomatoFighter.getSelectedProp()[0];
			if(tomatoFighter.getPropNum(effectNum)<=0)
			{
				
					
				statusUpdateString(ccColor3B.ccBLACK, "Items depleted!" );
				//((TomatoshootGame)CCDirector.sharedDirector().getActivity()).toastMessageShort("道具用盡！");
				return;
			}
			if(effectNum == 6)
			{
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).toastMessage("請先購買到具！");
				return;
			}
			if((effectNum == 0 ||effectNum == 1 ||effectNum == 2)&& playerHP == 100)
			{					
				statusUpdateString(ccColor3B.ccBLACK, "HP full, not needed");
				return;
			}
			tomatoFighter.setPropNum(effectNum, tomatoFighter.getPropNum(effectNum)-1);
			amount1.setString(""+tomatoFighter.getPropNum(effectNum));
		}
		if(item.getTag() == 1 ) //2nd tool
		{
			effectNum = tomatoFighter.getSelectedProp()[1];
			if(tomatoFighter.getPropNum(effectNum)<=0)
			{
				statusUpdateString(ccColor3B.ccBLACK, "Items depleted!");
				return;
			}
			if(effectNum == 6)
			{
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).toastMessage("請先購買到具！");
				return;
			}
			if((effectNum == 0 ||effectNum == 1 ||effectNum == 2)&& playerHP == 100)
			{
				statusUpdateString(ccColor3B.ccBLACK, "HP full, not needed");
				return;
			}
			tomatoFighter.setPropNum(effectNum, tomatoFighter.getPropNum(effectNum)-1);
			amount2.setString(""+tomatoFighter.getPropNum(effectNum));
		}
		SoundEngine.sharedEngine().playEffect(context, R.raw.shields1);
		
//		props[0] = new item("維他命C飲料", 1800, "HP + 5");
//		props[1] = new item("維他命D飲料", 3500, "HP + 10");
//		props[2] = new item("維他命E飲料", 6600, "HP + 20");		
//		props[3] = new item("銷魂番茄醬", 8000, "可施放OH MY God技能 (HP - 30)");
//		props[4] = new item("營養雞蛋", 2500, "Attack * 1.5");
//		props[5] = new item("養身雞蛋", 6000, "Attack * 2.5");
		if(effectNum == 0)
		{
			playerHP += 5;
			statusUpdateString(ccColor3B.ccGREEN, "HP + 5");
		}
		else if (effectNum == 1)
		{
			playerHP += 10;
			statusUpdateString(ccColor3B.ccGREEN, "HP + 10");
		}
		else if (effectNum == 2)
		{
			playerHP += 20;
			statusUpdateString(ccColor3B.ccGREEN, "HP + 20");
		}
		else if (effectNum == 3)
		{
			ohMyGodLocked = false;
			playerHP -= 30;
			ohMyGod();			
			statusUpdateString(ccColor3B.ccGREEN, "OH MY God!! (HP - 30)");
		}
		else if (effectNum == 4)
		{
			shoo_attack_power*=1.5;
			statusUpdateString(ccColor3B.ccGREEN, "Attack * 1.5");
		}
		else if (effectNum == 5)
		{
			shoo_attack_power*=2.5;
			statusUpdateString(ccColor3B.ccGREEN, "Attack * 2.5");
		}
		
		
	}
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
		// Choose one of the touches to work with
		//CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
		
		//pauseGame();
		if(loadingCounter>50)
		{			
			removeAllChildren(true);
			loadingCounter = 0;
			if(!isQuickGame)
			{
				if(tomatoFighter.getCurGameLevel()==1 && !hasShownOpening)
				{
					SoundEngine.sharedEngine().realesAllSounds();
					//SoundEngine.sharedEngine().setSoundVolume(0.5f);
					SoundEngine.sharedEngine().playSound(context, R.raw.tsfhx, false);
					
					runOpening();					
				}
				else
				{
					SoundEngine.sharedEngine().realesAllSounds();
					//SoundEngine.sharedEngine().setSoundVolume(0.5f);
					SoundEngine.sharedEngine().playSound(context, R.raw.tsfh, false);
					
					runStory();
				}
			}
			else
			{
				hasShownStory = true;
				initContents();
			}
		}	
		else if(!hasShownStory)
		{
			if(tomatoFighter.getCurGameLevel()==1 && !hasShownOpening)
			{
				removeChild(storyNode,true);
				runStory();
				hasShownOpening = true;
			}
			else
			{
				removeChild(storyNode,true);
				initContents();
				hasShownStory = true;
			}
		}
		
		return true;
	}
	public void pauseGame(Object sender)
	{
		pauseGame();
	}
	public void pauseGame()
	{

		if(gamePaused==true)
		{
			return;
		}
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = true;
		//CCDirector.sharedDirector().pause();
		//CCScene scene = PauseScreenLayer.scene();
		//CCDirector.sharedDirector().pushScene(CCPageTurnTransition.transition(1, scene, true));
		//CCDirector.sharedDirector().pushScene(scene);
		
		this.unscheduleAllSelectors();
		this.setIsTouchEnabled(false);

		pitchRecorder.setRecording(false);
		pitchRecorder.setIsEnd(true);
		try {
			pitchThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//scene.setVisible(false);
		addChild(pauseScreenLayer,200, 888);
		pauseScreenLayer.runAction(CCFadeTo.action(0.2f,220));
		
		pauseScreenLayer.setIsTouchEnabled(true);
		float heightS = pauseScreenLayer.console_full.getContentSize().height;
		pauseScreenLayer.console_full.runAction(CCSequence.actions(
				CCMoveBy.action(0.05f, CGPoint.ccp(0, heightS*0.4f)),
				CCCallFuncN.action(this, "setInTransitionFalse")
				));
		gamePaused = true;
		
		//fireParticle();
	}
	public void checkFiringCounter(float dt)
	{
		if(firingCounter<1)
		{
			//warning and set volume
			((TomatoshootGame)CCDirector.sharedDirector().getActivity()).toastMessage("聽不到你的熱血!!拜託喊大聲一點QQ");
			
		}
		firingCounter = 0;
	}
	public void gameLogic(float dt)
	{
		//_label.setString(""+roll+z);
		//((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = true;
		//CCDirector.sharedDirector().replaceScene(CCRotoZoomTransition.transition(1f, GameOverLayer.scene(1000,1)));
		//CCDirector.sharedDirector().replaceScene(CCRotoZoomTransition.transition(1f, GameOverLayer.scene(1000,23,255,2)));
		quickGameTimer++;
		if(_bugs.size()==0)
		{
			//_bugKilled = 0;
			if(isQuickGame)
			{
				//Log.d("quickGame","create bugs");				
				
				float initHP_A = 40;
				float killCounts_A = initHP_A/shoo_attack_power_base;
				float finalHP_A = (float) (shoo_attack_power_base*(killCounts_A+quickGameRoundNum*0.5));
				float initHP_B = 40;
				float killCounts_B = initHP_B/shoo_attack_power_base;
				float finalHP_B = (float) (shoo_attack_power_base*(killCounts_B+quickGameRoundNum*0.5));
				int xyspeed = (int) Math.ceil((quickGameRoundNum / 5));
				for(int i=0;i<5;i++)
				{
					Random randD = new Random();
					quickGameBugNum = randD.nextInt(2);
					switch (quickGameBugNum)
					{
						case 0:
							addBugs(EnemyTypes.BUGA, PathTypes.A, finalHP_A,1,1+xyspeed);
							break;
						case 1:
							addBugs(EnemyTypes.BUGB, PathTypes.B, finalHP_B,1,1+xyspeed);
							break;
						case 2:
							addBugs(EnemyTypes.BUGC, PathTypes.A, finalHP_A,1,1+xyspeed);
							break;
						default:
							break;
					}
				}
				quickGameScore_old = (float) (1.5*quickGameRoundNum*(playerHP*10*1.5 + _bugKilled * 1000));
				quickGameScore_new = quickGameScore_new + quickGameScore_old;
			}
			else
			{
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = true;
				CCDirector.sharedDirector().replaceScene(CCRotoZoomTransition.transition(1f, GameOverLayer.scene(money,1)));
			}
			
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
				//radioItem.setIsEnabled(true);
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
				if ((target.enemyType != EnemyTypes.BOTTLEB) && (target.enemyType != EnemyTypes.TOMATOHOSTAGE)) 
					money += 5 * (target.MAXHEALTH  * (100 + (target.zspeed*10) + (target.xyspeed*5))/100);
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
					int reduce = (int) (shoo_attack_power * 1.5+2*tomatoFighter.getSelectedFriendLevel(2));
					statusUpdateString(ccColor3B.ccRED, "Damage "+reduce);
					target.health_points-=reduce;
				}
				
				
				//Log.e("jim","stateCounter--"+target.stateCounter);
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
		//_label.setString(""+roll+""+z);
		if(this.unbeatable){
			countSec++;
//			redPineApple.stopAllActions();
//			redPineApple.setVisible(true);
//			redPineApple.runAction(CCSequence.actions(
//					CCBlink.action(0.5f,1),
//					CCCallFuncN.action(this,"spriteMoveFinishedHide")
//					));		
			if(countSec == 10){
				unbeatable_time = unbeatable_time - 1;
				countSec = 0;
			}
			
			_countLabel.setString(String.valueOf((int)unbeatable_time));
		}
		if(unbeatable_time == 0){
			this.unbeatable = false;
			pineApple.runAction(CCSequence.actions(
					//CCFadeOut.action(0.1f),
					CCCallFuncN.action(this, "spriteMoveFinishedHide")
					));
			redPineApple.runAction(CCSequence.actions(
					//CCFadeOut.action(0.1f),
					CCCallFuncN.action(this, "spriteMoveFinishedHide")
					));
//			FBimage.runAction(CCSequence.actions(
//					//CCFadeOut.action(0.1f),
//					CCCallFuncN.action(this, "spriteMoveFinishedHide")
//					));
			_countLabel.setVisible(false);
			radioItem.setIsEnabled(true);
			ad_aware.setVisible(false);
			unbeatable_time = -1;
			//pineApple.removeChild((CCNode) FBimage, true);
			//hudNode.removeChild((CCNode) pineApple, true);
		}
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
				if(this.unbeatable){
					//countSec++;
					redPineApple.stopAllActions();
					redPineApple.setVisible(true);
					redPineApple.runAction(CCSequence.actions(
							CCBlink.action(0.5f,1),
							CCCallFuncN.action(this,"spriteMoveFinishedHide")
							));		
//					if(countSec == 50){
//						unbeatable_time = unbeatable_time - 1;
//						countSec = 0;
//					}
//					
//					_countLabel.setString(String.valueOf((int)unbeatable_time));
				}
				else{
					playerHP-=0.25;
					screenRed.stopAllActions();
					screenRed.setVisible(true);
					screenRed.runAction(CCSequence.actions(
							CCBlink.action(0.5f,1),
							CCCallFuncN.action(this,"spriteMoveFinishedHide")
							));		
				}
				
				if(playerHP<0)
				{
					sensorMan.unregisterListener(listener);
					((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = true;
					if(isQuickGame)
					{
						//tomatoFighter.getQuickGameLeaderBoard(quickGameScore_new);//get quickGame leaderBoard
						quickGameScore_old = (float) (1.5*quickGameRoundNum*(playerHP*10*1.5 + _bugKilled * 1000));
						quickGameScore_new = quickGameScore_new + quickGameScore_old -(quickGameTimer/2)*10;
						if (quickGameScore_new <= 0)
							quickGameScore_new = 0;
						CCDirector.sharedDirector().replaceScene(CCRotoZoomTransition.transition(1f, GameOverLayer.scene((int)quickGameScore_new,_bugKilled,quickGameTimer/2,2)));
					}
					else
					{
						CCDirector.sharedDirector().replaceScene(CCRotoZoomTransition.transition(1f, GameOverLayer.scene(money,0)));
											
					}
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
		if(addBlood){
			playerHP+=  30 + tomatoFighter.getSelectedFriendLevel(1)*2;
			if(playerHP >= 100)	
				playerHP = 100;
			addBlood = false;
		}
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
				energyEmitter.setScale(energyBall.getScale()*0.4f);
				
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
						targetY = winSize.height - target.indicatorSprite.getContentSize().height;
						target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("up.png"));
						target.indicatorSprite.setFlipY(false);
					}
					else if(0>targetY) //S off
					{
						targetY = 0 + target.indicatorSprite.getContentSize().height;
						target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("up.png"));
						target.indicatorSprite.setFlipY(true);
					}
				}
				else if( 0<targetY && targetY<winSize.height )//E,W off
				{
					if(targetX>winSize.width) //E off
					{
						targetX = winSize.width - target.indicatorSprite.getContentSize().width;
						target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("left.png"));
						target.indicatorSprite.setFlipX(true);
					}
					else if(0>targetX) //W off
					{
						targetX = 0 + target.indicatorSprite.getContentSize().width;
						target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("left.png"));
						target.indicatorSprite.setFlipX(false);
					}
				}
				else if( (0>targetY && 0>targetX) )//左下 off
				{
					targetX = 0 + target.indicatorSprite.getContentSize().width;
					targetY = 0 + target.indicatorSprite.getContentSize().height;
					target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("leftdown.png"));
					target.indicatorSprite.setFlipX(false);
					target.indicatorSprite.setFlipY(false);
				}
				else if( (0>targetY && targetX>winSize.width) )//右下 off
				{
					targetX = winSize.width - target.indicatorSprite.getContentSize().width;
					targetY = 0 + target.indicatorSprite.getContentSize().height;
					target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("leftdown.png"));
					target.indicatorSprite.setFlipX(true);
					target.indicatorSprite.setFlipY(false);
				}
				else if( (targetY>winSize.height && 0>targetX) )//左上 off
				{
					targetX = 0 + target.indicatorSprite.getContentSize().width;
					targetY = winSize.height - target.indicatorSprite.getContentSize().height;
					target.indicatorSprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("leftdown.png"));
					target.indicatorSprite.setFlipX(false);
					target.indicatorSprite.setFlipY(true);
				}
				else if( (targetY>winSize.height && targetX>winSize.width) )//右上 off
				{
					targetX = winSize.width - target.indicatorSprite.getContentSize().width;
					targetY = winSize.height - target.indicatorSprite.getContentSize().height;
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
	
	protected void addBugs(EnemyTypes etype, PathTypes ptype, float eHP, int ezSPD, int exySPD)
	{
	
		//Log.d("quick","addbug");
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		Random rand = new Random();
		//EnemyBug target = EnemyBug.sprite("fly_worm.png");
		EnemyBug target = new EnemyBug();
		
		//	change picture of enemies
		//if (etype==EnemyTypes.BUGA)
			target.bugSprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("fly_worm_a_1.png"));
		
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
		
		
		
	}
	public void ohMyGod()//歐買尬
	{
		if(superPowerCoolDownTimer !=0 )
		{
			return;
		}
		if(ohMyGodLocked)
		{
			((TomatoshootGame)CCDirector.sharedDirector().getActivity()).toastMessage("OhMyGod大絕招 封印中");
			radioItem.setIsEnabled(true);
			ad_aware.setVisible(false);
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
				cannon.getPosition().y+cannon.getContentSize().height/2));
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
				cannon.getPosition().y+cannon.getContentSize().height/2);
		
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
		
		redFrame.setPosition(CGPoint.ccp(cannon.getContentSize().width / 2,cannon.getContentSize().height * 0.7f));
		redFrame.setScale(0.4f * winSize.width / (7.92f * redFrame.getContentSize().width));
		redFrame.setVisible(false);
		redFrame.setTag(2);
		cannon.addChild(redFrame);
		FBimage2.setScale(1.8f * winSize.width / (7.92f *redFrame.getContentSize().width));
		FBimage2.setPosition(CGPoint.ccp(redFrame.getContentSize().width * 0.5f, redFrame.getContentSize().height * 0.5f));
		FBimage2.setVisible(false);
		redFrame.addChild(FBimage2);	
		
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
		
		CCAction showFrame = CCSequence.actions(
				CCShow.action(),
				CCFadeIn.action(0.5f),
				CCDelayTime.action(3.5f),
				CCFadeOut.action(0.5f),
				CCCallFuncN.action(this, "frameFinish")
		);
		redFrame.runAction(showFrame);
		
		CCAction showFBimage = CCSequence.actions(
				CCShow.action(),
				CCFadeIn.action(0.5f),
				CCBlink.action(0.5f,3),
				CCDelayTime.action(3.0f),
				CCFadeOut.action(0.5f)
		);
		FBimage2.runAction(showFBimage);
		
	}
	public void Haleiluya()  //哈雷路亞
	{
		//Log.d("aaaa", "@@@@@" + (25.0f*snowEmitter.getContentSize().width));
		ccColor4F startColor,startColorVar,endColor,endColorVar;

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
		
		smokeEmitter.setDuration(0.5f);
		smokeEmitter.setPosition(CGPoint.ccp(
				cannon.getPosition().x, 
				cannon.getPosition().y+cannon.getContentSize().height/2));
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
				cannon.getPosition().y+cannon.getContentSize().height/2);

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
		snowEmitter.setTag(105);
		//snowEmitter.setVisible(false);
		snowEmitter.resetSystem();
		snowEmitter.scheduleUpdate(1);
		//hudNode.addChild(snowEmitter,-11,1.0f,1.0f,winSize.width * 0.5f, winSize.height * 0.95f);

		addBloodFlower.setDuration(CCParticleSystem.kCCParticleDurationInfinity);
		addBloodFlower.setGravity(CGPoint.zero());
		addBloodFlower.setEmitterMode(CCParticleSystem.kCCParticleModeGravity);
		addBloodFlower.setSpeed(160);
		addBloodFlower.setSpeedVar(20);
		addBloodFlower.setRadialAccel(-120);
		addBloodFlower.setRadialAccelVar(0);
		addBloodFlower.setTangentialAccel(30);
		addBloodFlower.setTangentialAccelVar(0);
		addBloodFlower.setAngle(90);
		addBloodFlower.setAngleVar(360);
		addBloodFlower.setPosition(CGPoint.ccp(winSize.width * 0.14f, winSize.height * 0.72f));
		addBloodFlower.setPosVar(CGPoint.zero());
		addBloodFlower.setLife(4);
		addBloodFlower.setLifeVar(1);
		addBloodFlower.setStartSpin(0);
		addBloodFlower.setStartSpinVar(0);
		addBloodFlower.setEndSpin(0);
		addBloodFlower.setEndSpinVar(0);
		startColor = new ccColor4F(0.5f, 0.5f, 0.5f, 1.0f);
		addBloodFlower.setStartColor(startColor);
		startColorVar = new ccColor4F(0.5f, 0.5f, 0.5f, 1.0f);
		addBloodFlower.setStartColorVar(startColorVar);
		endColor = new ccColor4F(0.1f, 0.1f, 0.1f, 0.2f);
		addBloodFlower.setEndColor(endColor);
		endColorVar = new ccColor4F(0.1f, 0.1f, 0.1f, 0.2f);	
		addBloodFlower.setEndColorVar(endColorVar);
		addBloodFlower.setStartSize(50.0f);
		addBloodFlower.setStartSizeVar(30.0f);
		addBloodFlower.setEndSize(CCParticleSystem.kCCParticleStartSizeEqualToEndSize);
		addBloodFlower.setEmissionRate( addBloodFlower.getTotalParticles()/addBloodFlower.getLife());
		addBloodFlower.setBlendAdditive(true);
		addBloodFlower.resetSystem();
		addBloodFlower.scheduleUpdate(1);
		addBloodFlower.setTag(101);
		
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
		
		greenFrame.setPosition(CGPoint.ccp(cannon.getContentSize().width / 2,cannon.getContentSize().height * 0.7f));
		greenFrame.setScale(0.4f * winSize.width / (7.92f * greenFrame.getContentSize().width));
		greenFrame.setVisible(false);
		greenFrame.setTag(1);
		cannon.addChild(greenFrame);
		FBimage1.setScale(1.8f * winSize.width / (7.92f *greenFrame.getContentSize().width));
		FBimage1.setPosition(CGPoint.ccp(greenFrame.getContentSize().width * 0.5f, greenFrame.getContentSize().height * 0.5f));
		FBimage1.setVisible(false);
		greenFrame.addChild(FBimage1);
		
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
				CCDelayTime.action(2.0f),
				//CCShow.action(),
				//CCDelayTime.action(2.0f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		snowEmitter.runAction(starsRain);
		
		CCAction addblood = CCSequence.actions(
			CCDelayTime.action(1.5f),
			CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		addBloodFlower.runAction(addblood);
		
		CCAction showFrame = CCSequence.actions(
				CCShow.action(),
				CCFadeIn.action(0.5f),
				CCDelayTime.action(3.5f),
				CCFadeOut.action(0.5f),
				CCCallFuncN.action(this, "frameFinish")
		);
		greenFrame.runAction(showFrame);
		
		CCAction showFBimage = CCSequence.actions(
				CCShow.action(),
				CCFadeIn.action(0.5f),
				CCBlink.action(0.5f,3),
				CCDelayTime.action(3.0f),
				CCFadeOut.action(0.5f)
		);
		FBimage1.runAction(showFBimage);
		
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
				cannon.getPosition().y+cannon.getContentSize().height/2));
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
				cannon.getPosition().y+cannon.getContentSize().height/2);
		
		pineApple.setPosition(CGPoint.ccp(winSize.width * 0.5f,winSize.height * 0.6f));
		pineApple.setScale(winSize.width / (8.0f * pineApple.getContentSize().width));
		pineApple.setVisible(false);
		//pineApple.setTag(105);
		FBimage0.setScale(1.5f * winSize.width / (16.0f *pineApple.getContentSize().width));
		FBimage0.setPosition(CGPoint.ccp(pineApple.getContentSize().width / 2, pineApple.getContentSize().height * 0.4f));
		FBimage0.setVisible(false);
		pineApple.addChild(FBimage0,0);
		hudNode.addChild(pineApple,20,1.0f,1.0f,winSize.width * 0.5f, winSize.height * 0.6f);
		
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
		
		CCAction showPineapple = CCSequence.actions(
				CCDelayTime.action(1.2f),
				CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		
		smokeBall.runAction(showPineapple);
		
		CCAction appleShow = CCSequence.actions(
				CCDelayTime.action(2.0f),
				CCShow.action(),
				CCFadeIn.action(0.5f),
				CCBlink.action(0.5f,3),
				CCScaleTo.action(1.0f, 1.6f),
				CCSpawn.actions(
						CCMoveTo.action(1.0f,CGPoint.ccp(winSize.width * 0.14f, winSize.height * 0.72f)),
						CCScaleTo.action(1.0f, 0.9f),
						CCRotateBy.action(1.0f, 360)
						),
				//CCFadeOut.action(0.5f),
				CCCallFuncN.action(this, "startProtect")

		);
		pineApple.runAction(appleShow);
		
		CCAction FBremove = CCSequence.actions(
				CCDelayTime.action(2.0f),
				CCShow.action()
			   // CCFadeOut.action(0.5f),
			//	CCCallFuncN.action(this, "hudSpriteMoveFinished")
		);
		FBimage0.runAction(FBremove);
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
						CGPoint.ccp(target.bugSprite.getContentSize().width*.5f, 
									target.bugSprite.getContentSize().height*.5f));
				tempNode.setOpacity(100);
				tempNode.setScale(target.bugSprite.getContentSize().width*1.0f/tempNode.getContentSize().width);
				target.bugSprite.addChild(tempNode,1);
				tempNode.runAction(CCRepeatForever.action(CCAnimate.action(fire_Animation,false)));
			
		}
	}
	public void updatePitch(float dt)
	{
		greenBar.setPercentage((float) (pitchRecorder.getVol() / maxVol) * 100);
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
			energy_progress.setPercentage(0);
			return;		
			
		}
		int currentPercentage = currentEnergyIndex*10;
		if(currentPercentage>=100)
			currentPercentage = 99;
		if(currentPercentage<0)
			currentPercentage = 0;
			
		energy_progress.runAction(CCProgressTo.action(0.1f, currentPercentage));
		//energy_progress.setPercentage(currentPercentage);
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
			firingCounter++;
			if(hudNode.getChildByTag(99) == null  && energyBallCoolDownTimer<=0)
        	{
        		energyBall.setPosition(CGPoint.ccp(
        				cannon.getPosition().x, 
        				cannon.getPosition().y+cannon.getContentSize().height/2));	
        		energyBall.setScale(ENERGY_BALL_DEFAULT_SCALE+0.5f);
        		energyBall.runAction(CCRepeatForever.action(
        				CCAnimate.action(energy_ball_aAnimation, false)));
        		energyBall.setTag(99);
        		hudNode.addChild(energyBall,-10,1.0f,1.0f,cannon.getPosition().x, 
        				cannon.getPosition().y+cannon.getContentSize().height/2);
        		
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
        				cannon.getPosition().y+cannon.getContentSize().height/2);
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
			energyBall.runAction(CCScaleTo.action(0.1f,ENERGY_BALL_DEFAULT_SCALE+1.0f+currentPercentage/40));
    		
			// speed of particles
			energyEmitter.setSpeed(160+currentPercentage*3);
			energyEmitter.setRadialAccel(-120-currentPercentage*3);
			energyEmitter.setTangentialAccel(30+currentPercentage*3);			
			
		}	
		
		
		
		
	}
	public void funcN_bugKilled(Object sender, Object target)
	{		    			
		//if (++_bugKilled > 12)
		_bugKilled++;
		_label.setString("meanVol:" + volThreshold);
		
		//playerHP+=10;
		
		//remove this enemy's stuff
		removeChild((CCNode) sender, true);	
		removeChild(( (EnemyBug) target).indicatorSprite,true);
		bugsToDelete.add( (EnemyBug) target);
	}
	public void startProtect(Object sender){
		pineApple.setPosition(CGPoint.ccp(winSize.width * 0.14f, winSize.height * 0.72f));
		hudNode.addChild(pineApple,20,1.0f,1.0f,winSize.width * 0.14f, winSize.height * 0.72f);
		_countLabel.setVisible(true);
		unbeatable = true;
		unbeatable_time = 3 + tomatoFighter.getSelectedFriendLevel(0)*3;  
	}
	public void frameFinish(Object sender){
		CCNode item = (CCNode) sender;
		if(item.getTag() == 1)
			redFrame.removeChild(FBimage1, true);
		else if(item.getTag() == 2)
			redFrame.removeChild(FBimage2, true);
		
		cannon.removeChild((CCNode) sender, true);
	}
	public void hudSpriteMoveFinished(Object sender)
	{
		CCNode item = (CCNode) sender;
		if(item.getTag() == 101){
			cannon_status.setVisible(true);
			radioItem.setIsEnabled(true);
			ad_aware.setVisible(false);
		}
		else if(item.getTag() == 102)
			hudNode.addChild(fireEmitter,-13,1.0f,1.0f,winSize.width * 0.5f,winSize.height * 0.6f);
		else if(item.getTag() == 103)
			hudNode.addChild(snowEmitter,-11,1.0f,1.0f,winSize.width * 0.5f, winSize.height * 0.95f);
		else if(item.getTag() == 100)
			hudNode.addChild(smokeBall,-12,1.0f,1.0f,winSize.width * 0.5f,winSize.height * 0.6f);
		else if(item.getTag() == 105){
			addBlood = true;
			hudNode.addChild(addBloodFlower,-14,1.0f,1.0f,winSize.width * 0.14f, winSize.height * 0.72f);
		}

		((CCParticleSystem)sender).stopAction(((CCParticleSystem)sender).getTag());
		
		hudNode.removeChild((CCNode) sender, true);
	}
	public void setInTransitionFalse(Object sender)
	{	
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = false;
		
	}
	public void spriteMoveFinished(Object sender)
	{	
		//((CCParticleSystem)sender).stopAction(((CCParticleSystem)sender).getTag());
		removeChild((CCNode) sender, true);
		sender = null;
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
			CGRect targetRect = CGRect.make(target.bugSprite.getPosition().x - (target.bugSprite.getContentSize().width),
					target.bugSprite.getPosition().y - (target.bugSprite.getContentSize().height),
					target.bugSprite.getContentSize().width,
					target.bugSprite.getContentSize().height);
			//update intersect check
		
			CGRect projectileRect = CGRect.make(projectile.realObject.getPosition().x - (projectile.realObject.getContentSize().width / 2.0f),
												projectile.realObject.getPosition().y - (projectile.realObject.getContentSize().height / 2.0f),
												projectile.realObject.getContentSize().width,
												projectile.realObject.getContentSize().height);
			*/
			
				
				
			if (CGRect.intersects(projectile.realObject.getBoundingBox(), target.bugSprite.getBoundingBox()) && projectile.distance>target.distance)//hit bug!!
			{				
				SoundEngine.sharedEngine().playEffect(context, R.raw.pow);
				if(target.state == States.ARMORDOWN)
				{
					int reduce = (shoo_attack_power+projectile.energyLevel*10)*2;
					statusUpdateString(ccColor3B.ccRED, "Damage "+reduce);
					target.health_points-=reduce;
				}
				else
				{
					int reduce = (shoo_attack_power+projectile.energyLevel*10);
					statusUpdateString(ccColor3B.ccRED, "Damage "+reduce);
					target.health_points-=reduce;
				}
				//target.bugSprite.runAction(
				//		CCLiquid.action(4, 20, ccGridSize.ccg((int)target.bugSprite.getContentSize().width,
				//				(int)target.bugSprite.getContentSize().height), 0.5f));
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
		firingCounter++;
		if(firingCounter>3)
		{
			//return;
		}
		if(cooldown_progress.getPercentage()<100)
		{
			float initscale = winSize.width/(14.0f*cooldown_progress.getContentSize().width);
			cooldown_progress.runAction(
					CCSequence.actions(
							CCScaleTo.action(0.25f, initscale*4), 
							CCScaleTo.action(0.25f, initscale)					
					));
			statusUpdateString(ccColor3B.ccBLUE, "Boom Cooling..." );
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
			if(weaponNumber == 0 && target.state != States.ARMORDOWN) //ARMORDOWN //activated //corn
			{				
				target.inStateControl = true;					
				target.state = States.ARMORDOWN;
				target.stateCounter = (int) (2*(2+playerLevel*0.1f)); //will end after 2 secs
				//target.stateCounter = (int) (100); //will end after 2 secs
				//target.bugSprite.runAction(CCShaky3D.action(5, true, ccGridSize.ccg((int)target.bugSprite.getContentSize().width,(int)target.bugSprite.getContentSize().height), target.stateCounter));
				//target.bugSprite.runAction(CCBlink.action(target.stateCounter*0.5f, 4)); //2 spins each second				
				CCSprite tempNode = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("animated_armor_down_a_0.png"));
				tempNode.setPosition(
						CGPoint.ccp(target.bugSprite.getContentSize().width/2, 
									target.bugSprite.getContentSize().height/2));
				//tempNode.setOpacity(100);
				tempNode.setScale(target.bugSprite.getContentSize().height*1.0f/tempNode.getContentSize().height);
				target.bugSprite.addChild(tempNode,1);
				tempNode.runAction(CCRepeatForever.action(CCAnimate.action(armor_down_Animation,false)));
				statusUpdateString(ccColor3B.ccBLACK, "Enemy armor down "+target.stateCounter/2+" secs");
				
			}
			else if(weaponNumber == 1) //roar //activated //watermellon
			{				
				//target.distance+=100;
				int pushAmount = 40 + playerLevel*10;
				if(target.distance<MAXDISTOFBUG*0.4)
					target.distance += pushAmount;
				if(target.distance>MAXDISTOFBUG)
					target.distance = MAXDISTOFBUG;
				statusUpdateString(ccColor3B.ccBLACK, "Enemy pushed "+pushAmount);
				
				
				CCTintBy action1 = CCTintBy.action(0.5f, ccColor3B.ccc3(-127, -255, -127));
				target.bugSprite.runAction(CCSequence.actions(action1, action1.reverse()));
			}
			else if(weaponNumber == 2 && target.state != States.CONFUSION) //CONFUSION  //grape
			{
				target.inStateControl = true;					
				target.state = States.CONFUSION;
				target.stateTemp = target.zspeed;
				target.zspeed = 0;
				target.stateCounter = (int) (2*(2+playerLevel*0.1f)); //will end after 2 secs
				//target.bugSprite.runAction(CCShaky3D.action(5, true, ccGridSize.ccg((int)target.bugSprite.getContentSize().width,(int)target.bugSprite.getContentSize().height), target.stateCounter));
				target.bugSprite.runAction(CCRotateBy.action(target.stateCounter*0.5f, 360*4)); //2 spins each second
				statusUpdateString(ccColor3B.ccBLACK, "Enemy confused "+target.stateCounter/2+" secs");
			}	/*		
			else if (CGRect.containsRect(CGRect.make(CGPoint.ccp(0,0), winSize), target.bugSprite.getBoundingBox()))//bug in screen
			{	
				if(weaponNumber == 2 && target.state != States.CONFUSION) //CONFUSION //corn
				{
					target.inStateControl = true;					
					target.state = States.CONFUSION;
					target.stateTemp = target.zspeed;
					target.zspeed = 0;
					target.stateCounter = 4; //will end after 2 secs
					//target.bugSprite.runAction(CCShaky3D.action(5, true, ccGridSize.ccg((int)target.bugSprite.getContentSize().width,(int)target.bugSprite.getContentSize().height), target.stateCounter));
					target.bugSprite.runAction(CCRotateBy.action(target.stateCounter*0.5f, 360*4)); //2 spins each second
				}
								
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
				
			}*/
		}
	}
	public void fireParticle()
	{
		firingCounter++;
		if(firingCounter>3)
		{
			//return;
		}
		//if(cooldown_progress.getPercentage()<100)
		//{
		//	return;
		//}
		energyBallCoolDownTimer = (int) (energy_cooldown_time*energy_progress.getPercentage() / 100);    	
		energy_progress.runAction(CCProgressFromTo.action(energyBallCoolDownTimer*0.5f, energy_progress.getPercentage(),0));
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
		if(weaponNumber == 0) //corn cannon
		{
			projectile.realObject = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("corn_bullet.png"));
		}
		else if(weaponNumber == 2) //grape
		{
			projectile.realObject = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("grape_bullet.png"));
		}
		else if(weaponNumber == 1) //mellon
		{
			projectile.realObject = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("mellon_bullet.png"));
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
		if(projectile.energyLevel>0)
		{
			// emitter position
			//energyEmitter.setPosition(energyBall.getPosition());	
			
		}
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
			    	  //z = vals[2];
		    	  	z = averages[1].average(vals[2]*1000);
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
//			if(wordRecThread.isAlive())
//            {
//                    wordRecRecorder.setRecording(false);
//            }
			pitchRecorder.setRecording(false);
			//pitchThread.stop();
			try {
				pitchThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.pitchRecorder.setIsEnd(true);
			this.pitchRecorder.setRecording(false);
			
					
			SoundEngine.sharedEngine().realesAllSounds();
			
  	      	SoundEngine.sharedEngine().setSoundVolume(1f);
  	      	//SoundEngine.sharedEngine().playSound(CCDirector.sharedDirector().getActivity(), R.raw.this_is_honkstep, true);
  	      	
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
