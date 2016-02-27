package org.mirlab.tomatoshoot;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.view.MotionEvent;

public class AboutLayer extends CCColorLayer {

	public AboutLayer(ccColor4B color) {
		super(color);
		// TODO Auto-generated constructor stub
	}

	public AboutLayer(ccColor4B color, float w, float h) {
		super(color, w, h);
		// TODO Auto-generated constructor stub
	}
	int SCREEN_DENSITY;
	Context context = CCDirector.sharedDirector().getActivity();
	
	int currentPage = 1;
	//CCLabel _label;
	
	private CGSize winSize;	
	static CCScene scene;
	public static CCScene scene()
	{
		scene = CCScene.node();
		scene.setTag(3);
		AboutLayer layer = new AboutLayer(ccColor4B.ccc4(45, 32, 11, 255));
		//layer.setTag(5);
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
		
		CCDirector.sharedDirector().popScene();
		
		return true;
	}
	public void initContents()
	{
		winSize = CCDirector.sharedDirector().displaySize();
		//_label = CCLabel.makeLabel("Page 1/2", "DroidSansFallback",25);
		//_label.setColor(ccColor3B.ccWHITE);
		//_label.setPosition(CGPoint.ccp(winSize.width * 0.07f, winSize.height * 0.03f));
		//addChild(_label,50);
		
		CCSprite aboutTitle = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_title.png"));
		aboutTitle.setScale(winSize.height/(7.5f*aboutTitle.getContentSize().height));
		aboutTitle.setPosition(CGPoint.ccp(winSize.width*0.5f, winSize.height*0.89f));
		addChild(aboutTitle,3);
		CCSprite aboutMail = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_mail2.png"));
		aboutMail.setScale(winSize.height/(6*aboutMail.getContentSize().height));
		aboutMail.setPosition(CGPoint.ccp(winSize.width*0.54f, winSize.height*0.7f));
		addChild(aboutMail,3);
		CCSprite aboutHome = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_home2.png"));
		aboutHome.setScale(winSize.height/(6*aboutHome.getContentSize().height));
		aboutHome.setPosition(CGPoint.ccp(winSize.width*0.48f, winSize.height*0.5f));
		addChild(aboutHome,3);
		CCSprite aboutBg = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_bg2.png"));
		aboutBg.setScale(winSize.height/(2.3f*aboutBg.getContentSize().height));
		aboutBg.setPosition(CGPoint.ccp(winSize.width*0.26f, winSize.height*0.22f));
		addChild(aboutBg,3);
		CCSprite aboutLogo = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_lablogo2.png"));
		aboutLogo.setScale(winSize.height/(4.f*aboutLogo.getContentSize().height));
		aboutLogo.setPosition(CGPoint.ccp(winSize.width*0.78f, winSize.height*0.12f));
		addChild(aboutLogo,3);
		
		CCSprite tomato1 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_bg_tomato.png"));
		tomato1.setScale(winSize.height/(4.8f*tomato1.getContentSize().height));
		tomato1.setPosition(CGPoint.ccp(winSize.width*0.1f, winSize.height*0.8f));
		addChild(tomato1,1);
		CCSprite tomato2 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_bg_tomato.png"));
		tomato2.setScale(winSize.height/(4.8f*tomato2.getContentSize().height));
		tomato2.setPosition(CGPoint.ccp(winSize.width*0.45f, winSize.height*0.85f));
		addChild(tomato2,1);
		CCSprite tomato3 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_bg_tomato.png"));
		tomato3.setScale(winSize.height/(4.8f*tomato3.getContentSize().height));
		tomato3.setPosition(CGPoint.ccp(winSize.width*0.8f, winSize.height*0.75f));
		addChild(tomato3,1);
		CCSprite tomato4 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_bg_tomato.png"));
		tomato4.setScale(winSize.height/(4.8f*tomato4.getContentSize().height));
		tomato4.setPosition(CGPoint.ccp(winSize.width*1.0f, winSize.height*0.5f));
		addChild(tomato4,1);
		CCSprite tomato5 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_bg_tomato.png"));
		tomato5.setScale(winSize.height/(4.8f*tomato5.getContentSize().height));
		tomato5.setPosition(CGPoint.ccp(winSize.width*0.03f, winSize.height*0.18f));
		addChild(tomato5,1);
		CCSprite tomato6 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_bg_tomato.png"));
		tomato6.setScale(winSize.height/(4.8f*tomato6.getContentSize().height));
		tomato6.setPosition(CGPoint.ccp(winSize.width*0.38f, winSize.height*0.38f));
		addChild(tomato6,1);
		CCSprite tomato7 = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("about_bg_tomato.png"));
		tomato7.setScale(winSize.height/(4.8f*tomato7.getContentSize().height));
		tomato7.setPosition(CGPoint.ccp(winSize.width*0.73f, winSize.height*0.25f));
		addChild(tomato7,1);
		
		
		
		this.setIsTouchEnabled(true);
	}
}
