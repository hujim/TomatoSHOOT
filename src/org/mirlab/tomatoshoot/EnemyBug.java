package org.mirlab.tomatoshoot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.interval.CCProgressTo;
import org.cocos2d.actions.interval.CCTintTo;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.mirlab.tomatoshoot.R;

import android.graphics.Bitmap;

public class EnemyBug extends RealWorldObject {
	public enum States {
	    NULL, SLOW, POISON, CONFUSION, ARMORDOWN,
	    STUPIDUPDOWN, LOVELY, FROZEN , ONFIRE
	}
	public enum Scripts {
	    UP, DOWN, LEFT, RIGHT, 
		UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT,
	    TOWARD, AWAY, SCRIPTEND
	}
	public enum EnemyTypes{
		NULL, BULLEYE, BADTOMATO, BOTTLEA, BOTTLEB, BUGA, BUGB, BOSSA,
		STRAW, TOMATOORZ, 
	}
	public enum PathTypes{
		NULL, A, B
	}
	
	float health_points;
	
	LinkedList<Scripts> fifoScripts = new LinkedList<Scripts>();
	//ArrayList<ArrayList<Scripts>> scriptLists;
	Scripts[][] scriptLists;
	Scripts[][] scriptListsDepth;
	int MAXHEALTH=0;

	int scriptCounter=0;
	int scriptNumber=0;
	int scriptCounterD=0;
	int scriptNumberD=0;

	boolean inStateControl = false;
	States state = States.NULL;//0 is null state
	EnemyTypes enemyType = EnemyTypes.NULL;
	PathTypes pathType = PathTypes.NULL;
	int stateCounter = 0;
	int stateTemp;
	
	boolean indicatorOn;
	CCSprite bugSprite;
	CCSprite indicatorSprite;
	CCProgressTimer circleProgress;
	
	int xyspeed;
	//int zspeed;
	
	
	public EnemyBug() {
		// TODO Auto-generated constructor stub
		//sprite(filename);
		super();
		health_points = 100;
		//speed = 20;
		xyspeed = 5;
		zspeed = 6;
		circleProgress = CCProgressTimer.progress("circle_progress.png");
		circleProgress.setType(CCProgressTimer.kCCProgressTimerTypeRadialCCW);
		//bugSprite.addChild(circleProgress);
	}
	public void update()
	{
		circleProgress.setPosition(bugSprite.getPosition().x + bugSprite.getBoundingBox().size.width*0.3f,  
								   bugSprite.getPosition().y - bugSprite.getBoundingBox().size.height*0.3f);	
						   
		circleProgress.setScale(bugSprite.getScale());
		circleProgress.setPercentage(100*(health_points/MAXHEALTH));
		circleProgress.getSprite().runAction(CCTintTo.action(0, ccColor3B.ccc3(230,255, 255)));
	}
	
	public void runScriptDepth()
	{
		if(scriptListsDepth[scriptNumberD].length>scriptCounterD)
		{
			switch(scriptListsDepth[scriptNumberD][scriptCounterD])
			{
			case TOWARD:
				if(distance>zspeed) 
					distance-=zspeed;
			break;
			case AWAY:
				if(distance<500-zspeed) 
					distance+=zspeed;
			break;
			case SCRIPTEND:
				stateCounter = 0; 
			break;
			default:	 
			break;
			}		
			scriptCounterD++;
		}
		else
		{
			scriptCounterD = 0;
			Random randD = new Random();
			scriptNumberD = randD.nextInt(scriptListsDepth.length);
		}
	}
	
	public void runScript()
	{
		if(scriptLists[scriptNumber].length>scriptCounter)
		{
			float scale = distance * (0.05f-1)/500 + 1;
			switch(scriptLists[scriptNumber][scriptCounter])
			{
			case UP: 
				roll+=xyspeed*scale;			
			break;					
			case DOWN: 
				roll-=xyspeed*scale;			
			break;					     
			case LEFT: 
				azimuth-=xyspeed*scale;
				if(azimuth<0)
					azimuth+=360;
			break;						
			case RIGHT: 
				azimuth+=xyspeed*scale;
				if(azimuth>360)
					azimuth-=360;
			break;
			case UP_RIGHT: 
				azimuth+=xyspeed*scale;
				roll+=xyspeed*scale;
				if(azimuth>360)
					azimuth-=360;
				if(roll>131)
					roll-=100;
			break;
			case UP_LEFT: 
				azimuth-=xyspeed*scale;
				roll+=xyspeed*scale;
				if(azimuth<0)
					azimuth+=360;
				if(roll>131)
					roll-=100;
			break;
			case DOWN_RIGHT: 
				azimuth+=xyspeed*scale;
				roll-=xyspeed*scale;
				if(azimuth>360)
					azimuth-=360;
				if(roll<30)
					roll+=100;
			break;
			case DOWN_LEFT: 
				azimuth-=xyspeed*scale;
				roll-=xyspeed*scale;
				if(azimuth<0)
					azimuth+=360;
				if(roll<30)
					roll+=100;
			break;
			case SCRIPTEND:
				stateCounter = 0; 
			break;
			default:	 
			break;
			}		
			scriptCounter++;
		}
		else
		{
			scriptCounter = 0;
			Random rand = new Random();
			scriptNumber = rand.nextInt(scriptLists.length);
		}
	}
}
