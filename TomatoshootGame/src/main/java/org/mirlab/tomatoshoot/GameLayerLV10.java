package org.mirlab.tomatoshoot;

import java.util.ArrayList;
import java.util.Random;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;
import org.mirlab.tomatoshoot.EnemyBug.EnemyTypes;
import org.mirlab.tomatoshoot.EnemyBug.PathTypes;
import org.mirlab.tomatoshoot.EnemyBug.Scripts;
public class GameLayerLV10 extends GameLayerBase
{
	
	ArrayList<CCSpriteFrame> fly_bossA_aArr;
	CCAnimation fly_bossA_aAnimation;
	//set level scriptlists
	//ArrayList<ArrayList<Scripts>> scriptLists = new ArrayList<ArrayList<Scripts>>(4);
	Scripts[][] scriptListsA= {
			//	for boss
			{Scripts.RIGHT}
			
	};
	Scripts[][] scriptListsB= {
			// horizontal swing types
			{Scripts.RIGHT,Scripts.LEFT,Scripts.LEFT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,
				Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,
				Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,
				Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,},
			// vertical swing types
			{Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.UP,Scripts.UP,Scripts.UP,
				Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,
				Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,
				Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,},
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
		CCColorLayer layer = new GameLayerLV10(ccColor4B.ccc4(0, 0, 0, 0));
		//scene.setVisible(false);
		scene.addChild(layer);
		
		return scene;
	}
	protected GameLayerLV10(ccColor4B color)
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
			
			
			
			fly_bossA_aArr = new ArrayList<CCSpriteFrame>();
			for(int i = 1; i <= 2; ++i) {		    
				fly_bossA_aArr.add(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("BOSSA_"+i+".png"));
			}
			fly_bossA_aAnimation = CCAnimation.animation("fly_bossA_aAnimation", 0.05f, fly_bossA_aArr);
			 
		 
		 
		float initHP_A = 420;
		float initHP_B = 60;
		float killCounts_A = initHP_A/shoo_attack_power_base;
		float killCounts_B = initHP_B/shoo_attack_power_base;
		float finalHP_A = (float) (shoo_attack_power_base*(killCounts_A+loop_no*0.5));
		float finalHP_B = (float) (shoo_attack_power_base*(killCounts_B+loop_no*0.5));
		
		addBugs(EnemyTypes.BOSSA, PathTypes.A, finalHP_A,3,0);
		
		
		addBugs(EnemyTypes.BUGA, PathTypes.B, finalHP_B,1,2);
		addBugs(EnemyTypes.BUGA, PathTypes.B, finalHP_B,1,2);
		
		addBugs(EnemyTypes.BUGB, PathTypes.B, finalHP_B,2,2);
		addBugs(EnemyTypes.BUGB, PathTypes.B, finalHP_B,2,2);
		
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
		else if (etype==EnemyTypes.BUGB)
			target.bugSprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("bugb.png"));
		else
			target.bugSprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("BOSSA_1.png"));
		
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
		
		target.distance = rand.nextInt(100)+400;
		//target.distance = rand.nextInt(500);//0~500
		if(maxScale == 0)
		maxScale = winSize.height/(2f*target.bugSprite.getContentSize().height);
		
		target.bugSprite.setScale(maxScale * (target.distance * (0.05f-1)/MAXDISTOFBUG + 1));
		target.bugSprite.setPosition(winSize.width+100,winSize.height+100); //set it off-screen at first
		
		if (etype==EnemyTypes.BUGA){
			CCAction fly_worm_aAction = CCRepeatForever.action(CCAnimate.action(fly_worm_aAnimation, false));
			target.bugSprite.runAction(fly_worm_aAction);
		}
		else if (etype==EnemyTypes.BOSSA){
			CCAction fly_bossA_aAction = CCRepeatForever.action(CCAnimate.action(fly_bossA_aAnimation, false));
			target.bugSprite.runAction(fly_bossA_aAction);
		}
		
		
		//target.bugSprite.setTag(0); //alive
		
		_bugs.add(target);
		
		
	}
}
