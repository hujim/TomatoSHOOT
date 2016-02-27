package org.mirlab.tomatoshoot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;

import android.view.MotionEvent;

public class HoldableRadioCCMenu extends CCMenu {
	protected String selector0;
	private Method invocation0;
	public static HoldableRadioCCMenu menu(CCMenuItem... items) {
        return new HoldableRadioCCMenu(items);
    }
	protected HoldableRadioCCMenu(CCMenuItem... items) {
		super(items);
    }  
	// Menu - Events
    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        if (state != MenuState.kMenuStateWaiting || ! visible_)
            return CCTouchDispatcher.kEventIgnored;

        selectedItem = itemForTouch(event);
        if (selectedItem != null) {
            selectedItem.selected();
            radio_btn_pressed();
            state = MenuState.kMenuStateTrackingTouch;
            return CCTouchDispatcher.kEventHandled;
        }

        return CCTouchDispatcher.kEventIgnored;
    }
	
	public void radio_btn_pressed() {			  			
        //Log.d("DDDDD","selected");		
        selector0 = "radio_btn_pressed";//
    	invocation0 = null;
        if (selectedItem.targetCallback != null && selector0 != null) {
        	Class<?> cls = selectedItem.targetCallback.getClass();
        	try {
        		invocation0 = cls.getMethod(selector0, Object.class);
        		invocation0.invoke(selectedItem.targetCallback, this);
        	} catch (SecurityException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	} catch (NoSuchMethodException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
