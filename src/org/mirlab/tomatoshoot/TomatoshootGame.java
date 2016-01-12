package org.mirlab.tomatoshoot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCFlipXTransition;
import org.cocos2d.transitions.CCTransitionScene.tOrientation;
import org.cocos2d.types.ccColor3B;
import org.mirlab.tomatoshoot.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class TomatoshootGame extends Activity implements OnKeyListener
{
	static {
        System.loadLibrary("gdx");
	}
	
	protected CCGLSurfaceView _glSurfaceView;
	private Preview mPreview;
	Camera mCamera;
	int numberOfCameras;
	int cameraCurrentlyLocked;

    // The first rear facing camera
    int defaultCameraId;
    
    int effectCount = 0;
    List<String> effectList;
	ArrayList<String> IdList = new ArrayList<String>();
	ArrayList<String> NameList = new ArrayList<String>();     
	ArrayList<String> FriendList = new ArrayList<String>(); 

    mirfb FB;
    TomatoFighter tomatoFighter;
    boolean firstLaunch;
    AlertDialog.Builder alert;
	EditText input;
    
    final String FILENAME ="game_data";
    File file;
    

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		alert = new AlertDialog.Builder(this);
		input = new EditText(this);
		
		FrameLayout ll = new FrameLayout (this);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		//_glSurfaceView = new GLSurfaceView(this);
		_glSurfaceView = new CCGLSurfaceView(this);
		_glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		//_glSurfaceView.setRenderer(new CubeRenderer(true));
		_glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		_glSurfaceView.setZOrderOnTop(true);
		//_glSurfaceView.setBackgroundColor(Color.RED);
		//_glSurfaceView.bringToFront();
		//setContentView(_glSurfaceView);

		TextView tv = new TextView(this);
		tv.setText("U dont see me!");
		tv.setVisibility(View.VISIBLE);
		tv.setBackgroundColor(Color.BLUE);
		//CCDirector.sharedDirector().getActivity().addContentView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		
		// Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        mPreview = new Preview(this);
        FB=new mirfb(this);
        file = getFileStreamPath(FILENAME);
        
        if(file.exists())
        {
        	firstLaunch = false;        	
        	loadFighter();
        }
    	else
	    {
    		firstLaunch = true;
    		//newFighter();   		
    		
    	}
        //CustomCameraView cv = new CustomCameraView(
		//	this.getApplicationContext());
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				
		ll.addView(mPreview);
		//ll.addView(tv);
		ll.addView(_glSurfaceView);
		
		
		setContentView(ll);		
		
	}
	public void newFighter()
	{
		firstLaunch = false;
		runOnUiThread(new Runnable() { 
	        public void run() 
	        { 
				//input.setInputType(type)
				alert.setTitle("報上的你的名，勇士");
				alert.setMessage("我是番茄戰士！");
				alert.setView(input);
				alert.setPositiveButton("衝！", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString().trim();
						tomatoFighter = new TomatoFighter(value, 9999);
						tomatoFighter.setGameStatus(0, 1);	//開放第一關						
						saveFighter();
						CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationRightOver));
					}
				});
				alert.show();
	        }
	        }
		);
	}
	public void saveFighter()
	{
		FileOutputStream fos = null;
		try {
			fos = openFileOutput(FILENAME, MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	try {
    		oos.writeObject(tomatoFighter);
			//tomatoFighter = (TomatoFighter) oos.readObject();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void loadFighter()
	{
		Log.e("save1","file.exists()");
    	FileInputStream fis = null;
		try {
			fis = openFileInput(FILENAME);
			Log.e("save1","openfile:"+fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(fis);
			Log.e("save1","ObjectInputStream:"+ois);
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	try {
			tomatoFighter = (TomatoFighter) ois.readObject();
    		//String test = (String) ois.readObject();
			Log.e("save1","oisread"+tomatoFighter.getID());
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onStart()
	{
		super.onStart();
		
		CCDirector.sharedDirector().attachInView(_glSurfaceView);
		//CCDirector.sharedDirector().
		//CCDirector.sharedDirector().setAlphaBlending(_glSurfaceView.get, true);
		//CCDirector.sharedDirector().getOpenGLView().setBackgroundColor(Color.TRANSPARENT);
		//_glSurfaceView.set
		
		CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
		
		CCDirector.sharedDirector().setDisplayFPS(true);
		
		CCDirector.sharedDirector().setAnimationInterval(1.0f / 40.0f);
		
		//CCDirector.sharedDirector().runWithScene(GameStartLayer.scene());
		if(FB.is_login()){
			CCScene scene = GameStartLayer.scene();
			//CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(1, scene, ccColor3B.ccWHITE));
			CCDirector.sharedDirector().runWithScene(scene);	
			
		}
		else{
			CCScene scene = GameLoadingLayer.scene();
			//CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(1, scene, ccColor3B.ccWHITE));
			CCDirector.sharedDirector().runWithScene(scene);
		}
		//CCDirector.sharedDirector().end();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		SoundEngine.sharedEngine().realesAllSounds();
		CCDirector.sharedDirector().pause();
		Log.d("-----","onPause!");
		
		 if (mCamera != null) {
	            mPreview.setCamera(null);
	            mCamera.release();
	            mCamera = null;
	        }
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		CCDirector.sharedDirector().resume();
		Log.d("-----","onResume!");
		 // Open the default i.e. the first rear facing camera.
        mCamera = Camera.open();
        
        cameraCurrentlyLocked = defaultCameraId;
        mPreview.setCamera(mCamera);        
        
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		
		SoundEngine.purgeSharedEngine();
		CCDirector.sharedDirector().end();
		Log.d("-----","onStop!");
	}	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	  Log.d("-----","keyback!");
	    	  if(CCDirector.sharedDirector().getRunningScene().getTag() == 0)
	    	  {	    		  
	    		  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		  builder.setCancelable(true);
	    		  builder.setIcon(R.drawable.icon);
	    		  builder.setTitle("Exit Tomato?!");
	    		  builder.setInverseBackgroundForced(true);
	    		  builder.setPositiveButton("Yessireee!", new DialogInterface.OnClickListener() {
	    		    @Override
	    		    public void onClick(DialogInterface dialog, int which) {
	    		      dialog.dismiss();
	    		      SoundEngine.sharedEngine().realesAllSounds();
		    		  finish();	  
	    		    }
	    		  });
	    		  builder.setNegativeButton("Hell No!", new DialogInterface.OnClickListener() {
	    		    @Override
	    		    public void onClick(DialogInterface dialog, int which) {
	    		      dialog.dismiss();
	    		    }
	    		  });
	    		  AlertDialog alert = builder.create();
	    		  alert.show();	    		    		  
	    	  }
	    	  else if(CCDirector.sharedDirector().getRunningScene().getTag() == 2) //ingame
	    	  {
	    		  CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationLeftOver));
	    		  //CCDirector.sharedDirector().replaceScene(SetGameItemLayer.scene());
	    		  	
	    	  }
	    	  else
	    	  {
	    		  //SoundEngine.sharedEngine().realesAllSounds();
	    		  CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameStartLayer.scene(), tOrientation.kOrientationLeftOver));
	    		  //CCDirector.sharedDirector().replaceScene(GameStartLayer.scene());
	    	  }
	          //task to be done here.. Eg, an alert dialog asking weather to exit?
	          return true;
	    }
	    else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {	    	  
	    	  AudioManager am = (AudioManager)getSystemService(Service.AUDIO_SERVICE); 
	    		  am.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
                      AudioManager.ADJUST_LOWER, 
                      AudioManager.FLAG_SHOW_UI);
	    	return true;
	    }
	    else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) {	    	  
	    	  AudioManager am = (AudioManager)getSystemService(Service.AUDIO_SERVICE); 
	    		  am.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
                    AudioManager.ADJUST_RAISE, 
                    AudioManager.FLAG_SHOW_UI);
	    	return true;
	    }
	    else if(keyCode == KeyEvent.KEYCODE_MENU) {
	    	/*
	    	effectList = mCamera.getParameters().getSupportedColorEffects();
	    	Camera.Parameters parameters = mCamera.getParameters();
	    	if(effectCount<effectList.size())
	    	{
	    		parameters.setColorEffect(effectList.get(effectCount));
	    		effectCount++;	    		
	    	}
	    	else
	    	{
	    		effectCount = 0;
	    		parameters.setColorEffect(effectList.get(effectCount));	    		
	    	}
	        mCamera.setParameters(parameters);
	        */
	    	if(CCDirector.sharedDirector().getRunningScene().getTag() == 2) //ingame
	    	{
	    		GameLayerBase layer = (GameLayerBase) CCDirector.sharedDirector().getRunningScene().getChildren().get(0);
	    		if(layer.gamePaused)
	    		{
	    			layer.pauseScreenLayer.unPauseGame();
	    		}
	    		else
	    		{
	    			layer.pauseGame();
	    		}
	    		
	    	}

	    	return true;
	    }	    
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FB.authorizeCallback(requestCode, resultCode, data);
        Log.d("FB", "onactivityresultcalled");
        
    }
}