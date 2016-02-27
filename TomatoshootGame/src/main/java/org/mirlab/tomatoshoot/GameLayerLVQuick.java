package org.mirlab.tomatoshoot;

import java.util.ArrayList;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.types.ccColor4B;
public class GameLayerLVQuick extends GameLayerBase
{

	static CCScene scene;
	public static CCScene scene()
	{
		scene = CCScene.node();
		scene.setTag(2);
		CCColorLayer layer = new GameLayerLVQuick(ccColor4B.ccc4(0, 0, 0, 0));
		//scene.setVisible(false);
		scene.addChild(layer);
		
		return scene;
	}
	protected GameLayerLVQuick(ccColor4B color)
	{
		super(color);		
		isQuickGame = true;

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

	}
}
