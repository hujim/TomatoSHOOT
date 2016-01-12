package org.mirlab.tomatoshoot;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.nodes.CCSprite;

public class RealWorldObject {
		
	float azimuth;
	float roll;
	int distance;
	int zspeed;
	//float delta_azimuth;
	
	int timeToLive; //updatenumber
	CCSprite realObject;
	int energyLevel = 0;
	
	
	
	public RealWorldObject() {
		// TODO Auto-generated constructor stub
		//sprite(filename);
		
		zspeed = 0;
		azimuth = 0;
		roll = 0;
		distance = 0;
		timeToLive = 300;
		
	}
}
