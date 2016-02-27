package org.mirlab.tomatoshoot;

import java.util.ArrayList;

import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.types.ccColor4B;
import org.mirlab.tomatoshoot.EnemyBug.Scripts;

import android.util.DisplayMetrics;
public class GameLayer extends GameLayerBase
{
	//set level scriptlists
	//ArrayList<ArrayList<Scripts>> scriptLists = new ArrayList<ArrayList<Scripts>>(4);
	Scripts[][] scriptLists= {
			{Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP,Scripts.UP},
			{Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN,Scripts.DOWN},
			{Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT,Scripts.LEFT},
			{Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT,Scripts.RIGHT},
			//	W-type path
			{Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT,
			 Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT},
			
			//	M-type path
			{Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT,
			 Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.UP_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT, Scripts.DOWN_RIGHT},
			 
			//	Lightning-type path
			{Scripts.DOWN_LEFT, Scripts.DOWN_LEFT, Scripts.DOWN_LEFT, Scripts.DOWN, Scripts.DOWN, Scripts.DOWN_LEFT, Scripts.DOWN_LEFT,
				Scripts.RIGHT, Scripts.RIGHT, Scripts.UP_RIGHT, Scripts.DOWN_RIGHT, Scripts.RIGHT, Scripts.RIGHT,
				Scripts.DOWN_LEFT, Scripts.DOWN_LEFT, Scripts.DOWN_LEFT, Scripts.DOWN, Scripts.DOWN_LEFT, Scripts.DOWN, Scripts.DOWN_LEFT},
			 
			//	Horizontal Sway-type path
			{Scripts.LEFT, Scripts.RIGHT, Scripts.RIGHT, Scripts.LEFT, Scripts.LEFT, Scripts.LEFT, Scripts.RIGHT, Scripts.RIGHT, 
					Scripts.RIGHT, Scripts.RIGHT, Scripts.RIGHT, Scripts.LEFT, Scripts.LEFT, Scripts.LEFT,
					Scripts.LEFT, Scripts.LEFT, Scripts.LEFT, Scripts.LEFT, Scripts.RIGHT, Scripts.RIGHT, Scripts.RIGHT, Scripts.RIGHT},
			
			//	Vertical Sway-type path
			{Scripts.UP, Scripts.DOWN, Scripts.DOWN, Scripts.UP, Scripts.UP, Scripts.UP, Scripts.DOWN, Scripts.DOWN, 
						Scripts.DOWN, Scripts.DOWN, Scripts.DOWN, Scripts.UP, Scripts.UP, Scripts.UP,
						Scripts.UP, Scripts.UP, Scripts.UP, Scripts.UP, Scripts.DOWN, Scripts.DOWN, Scripts.DOWN, Scripts.DOWN
			}
	};
	
	protected GameLayer(ccColor4B color)
	{
		super(color);		
		
		if(SCREEN_DENSITY == DisplayMetrics.DENSITY_HIGH)
		{
			
		}
		else if(SCREEN_DENSITY == DisplayMetrics.DENSITY_MEDIUM)
		{
			
		}
		else
		{
			
		}
		//Draw animate fly_worm		
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(textureFolderPath+"fly_worm_a.plist");
		fly_worm_aArr = new ArrayList<CCSpriteFrame>();
		for(int i = 1; i <= 6; ++i) {		    
			fly_worm_aArr.add(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("fly_worm_a_"+i+".png"));
		}
		fly_worm_aAnimation = CCAnimation.animation("fly_worm_aAnimation", 0.05f, fly_worm_aArr);				
	    
	}
	
}
