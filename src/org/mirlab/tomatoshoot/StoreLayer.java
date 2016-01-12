package org.mirlab.tomatoshoot;

import java.util.LinkedList;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemFont;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.content.Context;

public class StoreLayer extends CCColorLayer {		
	protected int currentLayer = -1;
	
	private LinkedList<SubBtnLayer> _weaponLayers;	
	private LinkedList<SubBtnLayer> _armorLayers;
	private LinkedList<SubBtnLayer> _clothLayers;	
	
	protected CCLabel _label1;
	//protected TomatoFighter tomatoFighter;
	
	public static CCScene scene()
	{
		CCScene scene = CCScene.node();
		CCColorLayer layer = new StoreLayer(ccColor4B.ccc4(255, 255, 255, 0));
				
		scene.addChild(layer);
		
		return scene;
	}
	
	protected StoreLayer(ccColor4B color)
	{
		super(color);
		
		this.setIsTouchEnabled(true);
						
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		CCSprite background = CCSprite.sprite("background_store_com.png");			
		
		// 依照背景圖伸縮的比例去調整其他圖片的伸縮比例
		float backgroundScale = winSize.height/(background.getTextureRect().size.height);
		
		background.setPosition(CGPoint.ccp(winSize.width / 2.0f, winSize.height / 2.0f));
		background.setScale(backgroundScale);
		addChild(background);
		
		// 武器、防具、服飾選單按鈕		
		CCMenuItemImage weaponBtn = CCMenuItemImage.item("button_weapon_com.png", "button_weapon_com.png", this, "clickMenuBtn");		
		weaponBtn.setScale(backgroundScale);
		weaponBtn.setTag(0);
		
		
		///////
		_label1 = CCLabel.makeLabel("None", "Arial", 25);
		_label1.setColor(ccColor3B.ccBLACK);
		_label1.setPosition(winSize.width * 3 / 4.0f, winSize.height / 2.0f + 40);		
		addChild(_label1);
		
		// 設定使用者		
		//tomatoFighter = new MIRFighter("YuJhe", 9999);				
		
		CGSize btnSize = weaponBtn.getContentSize();			
		btnSize.width = weaponBtn.getContentSize().width * backgroundScale;
		btnSize.height = weaponBtn.getContentSize().height * backgroundScale;
		
		CCMenuItemImage armorBtn = CCMenuItemImage.item("button_armor_com.png", "button_armor_com.png", this, "clickMenuBtn");
		armorBtn.setScale(backgroundScale);
		armorBtn.setTag(1);
		
		CCMenuItemImage clothBtn = CCMenuItemImage.item("button_cloth_com.png", "button_cloth_com.png", this, "clickMenuBtn");		
		clothBtn.setScale(backgroundScale);
		clothBtn.setTag(2);			
		
		CCMenu btnMenu = CCMenu.menu(weaponBtn, armorBtn, clothBtn);		
		btnMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		btnMenu.setPosition(CGPoint.ccp(btnSize.width * 2.5f, winSize.height - btnSize.height * 1.3f));
		btnMenu.alignItemsHorizontally(20);
		addChild(btnMenu);			
				
		// 下一頁按鈕
		CCMenuItemImage nextBtn = CCMenuItemImage.item("button_next.png", "button_next.png", this, "clickMenuBtn");
		nextBtn.setScale(backgroundScale);
		nextBtn.setTag(3);
		
		CCMenu nextBtnMenu = CCMenu.menu(nextBtn);
		nextBtnMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		nextBtnMenu.setPosition(CGPoint.ccp(btnSize.width * 4.8f, winSize.height / 2.0f));
		addChild(nextBtnMenu);
		
		// 購買按鈕
		CCMenuItem buyBtn = CCMenuItemFont.item("Buy", this, "clickMenuBtn");
		buyBtn.setTag(4);
		CCMenu buyBtnMenu = CCMenu.menu(buyBtn);		
		buyBtnMenu.setColor(ccColor3B.ccc3(0,0,0));
		buyBtnMenu.setPosition(CGPoint.ccp(winSize.width * 0.8f, winSize.height * 0.3f));
		addChild(buyBtnMenu);		
		/*
		SubBtnLayer weaponLayer01 = new SubBtnLayer(
			"button_corn_unselected.png", "button_corn_selected.png", "button_grape_unselected.png", "button_grape_selected.png", "button_pineapple_unselected.png", "button_pineapple_selected.png", 
			"button_watermelon_unselected.png", "button_watermelon_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
			"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"
			);		
		weaponLayer01.setAllBtnTag(0);
		weaponLayer01.setTag(1);
		
		SubBtnLayer weaponLayer02 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"
				);
		weaponLayer02.setAllBtnTag(0);
		weaponLayer02.setTag(0);
		
		SubBtnLayer weaponLayer03 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"
				);
		weaponLayer03.setAllBtnTag(0);		
		weaponLayer03.setTag(0);	
		
		SubBtnLayer weaponLayer04= new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, 
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"
				);
		weaponLayer04.setAllBtnTag(0);		
		weaponLayer04.setTag(0);
		
		SubBtnLayer weaponLayer05 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png", 
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"
				);
		weaponLayer05.setAllBtnTag(0);		
		weaponLayer05.setTag(0);
		
		SubBtnLayer weaponLayer06 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"
				);
		weaponLayer06.setAllBtnTag(0);		
		weaponLayer06.setTag(0);

		SubBtnLayer weaponLayer07 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, 
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"
				);
		weaponLayer07.setAllBtnTag(0);		
		weaponLayer07.setTag(0);

		SubBtnLayer weaponLayer08 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png", 
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"
				);
		weaponLayer08.setAllBtnTag(0);		
		weaponLayer08.setTag(0);
		
		SubBtnLayer weaponLayer09 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"
				);
		weaponLayer09.setAllBtnTag(0);		
		weaponLayer09.setTag(0);
		
		_weaponLayers = new LinkedList<SubBtnLayer>();
		_weaponLayers.add(weaponLayer01);
		_weaponLayers.add(weaponLayer02);		
		_weaponLayers.add(weaponLayer03);
		_weaponLayers.add(weaponLayer04);
		_weaponLayers.add(weaponLayer05);
		_weaponLayers.add(weaponLayer06);
		_weaponLayers.add(weaponLayer07);
		_weaponLayers.add(weaponLayer08);
		_weaponLayers.add(weaponLayer09);
		
		SubBtnLayer armorLayer01 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", //
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png"
				);	
		armorLayer01.setAllBtnTag(0);
		armorLayer01.setTag(1);
		
		SubBtnLayer armorLayer02 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"//, //
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"
				);	
		armorLayer02.setAllBtnTag(0);
		armorLayer02.setTag(0);
		
		_armorLayers = new LinkedList<SubBtnLayer>();
		_armorLayers.add(armorLayer01);
		_armorLayers.add(armorLayer02);
		
		SubBtnLayer clothLayer01 = new SubBtnLayer(
				"button_locked_unselected.png", "button_locked_selected.png", "button_aim_unselected.png", "button_aim_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", //
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"
				);	
		clothLayer01.setAllBtnTag(0);
		clothLayer01.setTag(1);
		
		SubBtnLayer clothLayer02 = new SubBtnLayer(
				"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", 
				"button_unselected.png", "button_selected.png"//, "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", //
				//"button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png", "button_unselected.png", "button_selected.png"
				);	
		clothLayer02.setAllBtnTag(0);
		clothLayer02.setTag(0);
		
		_clothLayers = new LinkedList<SubBtnLayer>();
		_clothLayers.add(clothLayer01);
		_clothLayers.add(clothLayer02);
		*/
		
		// Handle sound
		Context context = CCDirector.sharedDirector().getActivity();
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.pew_pew_lei);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.next);
		//SoundEngine.sharedEngine().playSound(context, R.raw.background_music_aac, true);			
		
		
	}	
	
	public void clickMenuBtn(Object sender)
	{		
		CCMenuItemImage item = (CCMenuItemImage) sender;		
		
		Context context = CCDirector.sharedDirector().getActivity();
						
		if(item.getTag() == 0 )	{			// click weapon button				
		
			SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);
			if (currentLayer == 0) {
				//	Nothing
			} else if (currentLayer == 1) {
				for (SubBtnLayer _armorLayer : _armorLayers) {
					if (_armorLayer.getTag() == 1) {						
						_armorLayer.setTag(0);
						_armorLayer.setAllBtnTag(0);
						_armorLayer.setAllBtnUnselected();
						removeChild(_armorLayer, true);
						break;
					}
				}
								
				addChild(_weaponLayers.getFirst());
				_weaponLayers.getFirst().setTag(1);
			} else if (currentLayer == 2) {
				for (SubBtnLayer _clothLayer : _clothLayers) {
					if (_clothLayer.getTag() == 1) {
						_clothLayer.setTag(0);
						_clothLayer.setAllBtnTag(0);
						_clothLayer.setAllBtnUnselected();
						removeChild(_clothLayer, true);
						break;
					}
				}
								
				addChild(_weaponLayers.getFirst());
				_weaponLayers.getFirst().setTag(1);
			} else {								
				addChild(_weaponLayers.getFirst());
				_weaponLayers.getFirst().setTag(1);
			}
											
			currentLayer = 0;		
		} else if(item.getTag() == 1 ) { 	// click armor button					
			
			SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);			
			
			if (currentLayer == 0) {
				for (SubBtnLayer _weaponLayer : _weaponLayers) {
					if (_weaponLayer.getTag() == 1) {
						_weaponLayer.setTag(0);
						_weaponLayer.setAllBtnTag(0);
						_weaponLayer.setAllBtnUnselected();
						removeChild(_weaponLayer, true);
						break;
					}					
				}
								
				addChild(_armorLayers.getFirst());
				_armorLayers.getFirst().setTag(1);
			} else if (currentLayer == 1) {
				//	Nothing
			} else if (currentLayer == 2) {
				for (SubBtnLayer _clothLayer : _clothLayers) {
					if (_clothLayer.getTag() == 1) {
						_clothLayer.setTag(0);
						_clothLayer.setAllBtnTag(0);
						_clothLayer.setAllBtnUnselected();
						removeChild(_clothLayer, true);
						break;
					}
				}
								
				addChild(_armorLayers.getFirst());
				_armorLayers.getFirst().setTag(1);
			} else {								
				addChild(_armorLayers.getFirst());
				_weaponLayers.getFirst().setTag(1);
			}
											
			currentLayer = 1;	
		} else if (item.getTag() == 2) {	// click cloth button			
			SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);
			
			if (currentLayer == 0) {
				for (SubBtnLayer _weaponLayer : _weaponLayers) {
					if (_weaponLayer.getTag() == 1) {						
						_weaponLayer.setTag(0);
						_weaponLayer.setAllBtnTag(0);
						_weaponLayer.setAllBtnUnselected();
						removeChild(_weaponLayer, true);
						break;
					}					
				}
				
				addChild(_clothLayers.getFirst());
				_clothLayers.getFirst().setTag(1);
			} else if (currentLayer == 1) {
				for (SubBtnLayer _armorLayer : _armorLayers) {
					if (_armorLayer.getTag() == 1) {
						_armorLayer.setTag(0);
						_armorLayer.setAllBtnTag(0);
						_armorLayer.setAllBtnUnselected();
						removeChild(_armorLayer, true);
						break;
					}
				}
								
				addChild(_clothLayers.getFirst());
				_clothLayers.getFirst().setTag(1);
			} else if (currentLayer == 2) {
				//	Nothing
			} else {								
				addChild(_clothLayers.getFirst());
				_clothLayers.getFirst().setTag(1);
			}
											
			currentLayer = 2;
			
		} else if (item.getTag() == 3) {	// click next button			
			
			int layerIndex = 0;
			
			if (currentLayer == 0) {
				SoundEngine.sharedEngine().playEffect(context, R.raw.next);
				
				for (SubBtnLayer _weaponLayer : _weaponLayers) {
					if (_weaponLayer.getTag() == 1) {						
						_weaponLayer.setAllBtnTag(0);
						_weaponLayer.setAllBtnUnselected();
						_weaponLayer.setTag(0);
						removeChild(_weaponLayer, true);
						layerIndex = _weaponLayers.indexOf(_weaponLayer);						
						break;
					}
				}
								
				addChild(_weaponLayers.get((layerIndex+1) % _weaponLayers.size()));
				_weaponLayers.get((layerIndex+1) % _weaponLayers.size()).setTag(1);
			} else if (currentLayer == 1) {
				SoundEngine.sharedEngine().playEffect(context, R.raw.next);
				
				for (SubBtnLayer _armorLayer : _armorLayers) {
					if (_armorLayer.getTag() == 1) {
						_armorLayer.setAllBtnTag(0);
						_armorLayer.setAllBtnUnselected();
						_armorLayer.setTag(0);
						removeChild(_armorLayer, true);
						layerIndex = _armorLayers.indexOf(_armorLayer);
						break;
					}
				}
				
				addChild(_armorLayers.get((layerIndex+1) % _armorLayers.size()));
				_armorLayers.get((layerIndex+1) % _armorLayers.size()).setTag(1);				
			} else if (currentLayer == 2) {
				SoundEngine.sharedEngine().playEffect(context, R.raw.next);
				
				for (SubBtnLayer _clothLayer : _clothLayers) {
					if (_clothLayer.getTag() == 1) {
						_clothLayer.setAllBtnTag(0);
						_clothLayer.setAllBtnUnselected();
						_clothLayer.setTag(0);
						removeChild(_clothLayer, true);
						layerIndex = _clothLayers.indexOf(_clothLayer);
						break;
					}
				}
				
				addChild(_clothLayers.get((layerIndex+1) % _clothLayers.size()));
				_clothLayers.get((layerIndex+1) % _clothLayers.size()).setTag(1);					
			} else {
				//	Nothing
			} 
							
		} else if (item.getTag() == 4) {	// click buy button
			
		}
	}
	
}
