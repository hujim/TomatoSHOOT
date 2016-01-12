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
