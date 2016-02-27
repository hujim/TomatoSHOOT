package org.mirlab.tomatoshoot;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCTransitionScene.tOrientation;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.view.MotionEvent;

public class HelpLayer extends CCColorLayer {

	protected HelpLayer(ccColor4B color) {
		super(color);
		// TODO Auto-generated constructor stub
	}
	
	static String textureFolderPath;
	int SCREEN_DENSITY;
	Context context = CCDirector.sharedDirector().getActivity();
	CCSprite tutorialImages[] = new CCSprite[2];
	int currentPage = 1;
	CCLabel _label;
	
	private CGSize winSize;	
	static CCScene scene;
	public static CCScene scene()
	{
		scene = CCScene.node();
		scene.setTag(0);
		HelpLayer layer = new HelpLayer(ccColor4B.ccc4(0, 0, 0, 255));
		layer.setTag(3);
		//layer.getLabel().setString(message);
		
		scene.addChild(layer);
		
		return scene;
	}
		@Override
	public void onEnterTransitionDidFinish()
	{
		super.onEnterTransitionDidFinish();
		initContents();
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = false;
	}
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
		// Choose one of the touches to work with
		//CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
		
		//pauseGame();
		if(currentPage == 1)
		{	
			removeChild(tutorialImages[0],true);
			addChild(tutorialImages[1]);
			_label.setString("Page 2/2");
			
			currentPage++;
		}
		else if(currentPage == 2)
		{
			//removeChild(tutorialImages[1],true);
			
			CCDirector.sharedDirector().popScene();
			if(((TomatoshootGame)CCDirector.sharedDirector().getActivity()).isCreatingNew)
			{
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).isQuickGame = false;
				CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationRightOver));
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).isCreatingNew = false;
			}
		}
		else
		{
			//CCDirector.sharedDirector().pushScene(HelpLayer.scene());
		}
		return true;
	}
	public void initContents()
	{
		winSize = CCDirector.sharedDirector().displaySize();
		_label = CCLabel.makeLabel("Page 1/2", "DroidSansFallback",25);
		_label.setColor(ccColor3B.ccWHITE);
		_label.setPosition(CGPoint.ccp(winSize.width * 0.07f, winSize.height * 0.03f));
		addChild(_label,50);
		
		tutorialImages[0] = CCSprite.sprite("help_1.png");
		tutorialImages[0].setScale(winSize.height/tutorialImages[0].getContentSize().height);
		tutorialImages[0].setPosition(CGPoint.ccp(winSize.width*0.5f, winSize.height*0.5f));
		tutorialImages[1] = CCSprite.sprite("help_2.png");
		tutorialImages[1].setScale(winSize.height/tutorialImages[1].getContentSize().height);
		tutorialImages[1].setPosition(CGPoint.ccp(winSize.width*0.5f, winSize.height*0.5f));
		addChild(tutorialImages[0]);
		
		this.setIsTouchEnabled(true);
	}
}
