package org.mirlab.tomatoshoot;

import java.util.Random;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;
import org.mirlab.tomatoshoot.EnemyBug.EnemyTypes;
import org.mirlab.tomatoshoot.EnemyBug.PathTypes;
import org.mirlab.tomatoshoot.EnemyBug.Scripts;
public class GameLayerLV08 extends GameLayerBase
{
	//set level scriptlists
	//ArrayList<ArrayList<Scripts>> scriptLists = new ArrayList<ArrayList<Scripts>>(4);
	Scripts[][] scriptListsA= {
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
	Scripts[][] scriptListsB= {
			//	S-type 90 rotated.
			{Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN_RIGHT,Scripts.RIGHT,Scripts.UP_RIGHT,Scripts.UP,
				Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.RIGHT,Scripts.DOWN_RIGHT,
				Scripts.DOWN,Scripts.DOWN,Scripts.DOWN},
			//	double circle
			{Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.RIGHT,
				Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_LEFT,Scripts.LEFT,Scripts.LEFT,
				Scripts.UP_LEFT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.RIGHT,Scripts.UP_RIGHT,
				Scripts.RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN_RIGHT,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN},
			// lightenig
			{Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,
				Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,
				Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT,Scripts.DOWN_LEFT},
			// lightenig'
			{Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,
				Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,
				Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT,Scripts.UP_RIGHT},

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
		CCColorLayer layer = new GameLayerLV08(ccColor4B.ccc4(0, 0, 0, 0));
		//scene.setVisible(false);
		scene.addChild(layer);
		
		return scene;
	}
	protected GameLayerLV08(ccColor4B color)
	{
		super(color);		

		
		

	}
	@Override
	public void initContents()
	{
		 super.initContents();

		float initHP_A = 55;
		
		float killCounts_A = initHP_A/shoo_attack_power_base;
		
		float finalHP_A = (float) (shoo_attack_power_base*(killCounts_A+loop_no*0.5));
		
		
		addBugs(EnemyTypes.TOMATOORZ, PathTypes.A, finalHP_A,2,2);
		addBugs(EnemyTypes.TOMATOORZ, PathTypes.A, finalHP_A,2,2);
		addBugs(EnemyTypes.TOMATOORZ, PathTypes.A, finalHP_A,2,2);
		addBugs(EnemyTypes.TOMATOORZ, PathTypes.A, finalHP_A,2,2);
		addBugs(EnemyTypes.TOMATOORZ, PathTypes.A, finalHP_A,2,2);
		
	}
	@Override
	protected void addBugs(EnemyTypes etype, PathTypes ptype, float eHP, int ezSPD, int exySPD)
	{
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		Random rand = new Random();
		//EnemyBug target = EnemyBug.sprite("fly_worm.png");
		EnemyBug target = new EnemyBug();
		
		//	change picture of enemies
		
		target.bugSprite = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("TOMATOORZ.png"));
		
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
		//CCAction fly_worm_aAction = CCRepeatForever.action(CCAnimate.action(fly_worm_aAnimation, false));
		//target.bugSprite.runAction(fly_worm_aAction);
		
		//target.bugSprite.setTag(0); //alive
		
		_bugs.add(target);
		
		
	}
}
