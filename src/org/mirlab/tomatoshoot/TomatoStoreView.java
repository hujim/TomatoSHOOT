package org.mirlab.tomatoshoot;

import java.util.ArrayList;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class TomatoStoreView extends Activity
{
	protected CCGLSurfaceView _glSurfaceView;
    
	ArrayList<String> IdList = new ArrayList<String>();
	ArrayList<String> NameList = new ArrayList<String>();     
	ArrayList<String> FriendList = new ArrayList<String>(); 
	mirfb FB;
    TomatoFighter fighter;
    
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// facebook
        FB = new mirfb(this);
        
        // 新增使用者
        fighter = new TomatoFighter("YuJhe", 9999);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		_glSurfaceView = new CCGLSurfaceView(this);						
		
		setContentView(_glSurfaceView);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		if (!FB.is_login()) {
			Log.d("fb", "login in TomatoStoreView.java");
			
			DialogListener fbDialog = new DialogListener() {
	            @Override
	            public void onComplete(Bundle values) {	            	
	            	Log.d("fb",FB.get_me().toString());  	            	
	            	IdList = FB.get_friendFieldArray("id");
	            	NameList = FB.get_friendFieldArray("name");
	            	FriendList = FB.get_friendAppUsersFieldArray();
	            	Log.d("fb", FriendList.toString());
	            }
	
	            @Override
	            public void onFacebookError(FacebookError error) {
	            	Log.d("fb","login error:" + error.toString() );
	            }
	
	            @Override
	            public void onError(DialogError e) {
	            	Log.e("fb","error:"+ e.toString());
		        }
	
	            @Override
	            public void onCancel() {
	            	Log.e("fb","login: cancel");
		        }
	        };
	        //Log.e("@@@@", (FB==null)? "null" :"not null");
			FB.login(fbDialog);
		} else {
			Log.d("fb", "have been login");
		}								
		
		// 設定使用者
		fighter.addOwnWeapons(0);	
		fighter.addOwnGames(0);		
		
		CCDirector.sharedDirector().attachInView(_glSurfaceView);
		
		CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
		
		CCDirector.sharedDirector().setDisplayFPS(true);
		
		CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);
				
		CCScene scene = SetGameItemLayer.scene();				
		
		CCDirector.sharedDirector().runWithScene(scene);
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		CCDirector.sharedDirector().pause();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		CCDirector.sharedDirector().resume();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		
		CCDirector.sharedDirector().end();
	}

}