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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.transitions.CCTransitionScene.tOrientation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

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
	ArrayList<String> FriendList = new ArrayList<String>(); 

    mirfb FB;
    TomatoFighter tomatoFighter;
    boolean inTransition = false;
    boolean firstLaunch;
    AlertDialog.Builder alert;
	EditText input;
	boolean isQuickGame = false;
	boolean isCreatingNew = false;
    boolean isInternetOn = false;
    ProgressDialog dialog;
    final String FILENAME ="game_data";
    File file;
    Toast toast;
    
    /**
     * yujhe, v0927
     */
	CCSprite [] selectedFriendSprite = new CCSprite[3];
	CCSprite [] selectedPropSprite = new CCSprite[2];
	CCSprite [] friendSprite = new CCSprite[6];
	JSONArray topNArray;
	JSONArray appUserArray;
	JSONObject friendObj;
	ArrayList<String> fNameList = new ArrayList<String>(); 
	ArrayList<String> fLevelList = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		ConnectivityManager cm = (ConnectivityManager) getSystemService(TomatoshootGame.CONNECTIVITY_SERVICE);
		try
		{
			if(cm.getActiveNetworkInfo()!=null)
			isInternetOn = cm.getActiveNetworkInfo().isConnectedOrConnecting();
		}	catch(Exception e){
			isInternetOn = false;
			e.printStackTrace();
		}
		//String url = SDKService.getAMDownloadURL(this); 
		//Uri uri = Uri.parse(url);
		//Intent intent = new Intent(Intent.ACTION_VIEW, uri); intent.addCategory(Intent.CATEGORY_BROWSABLE); startActivity(intent);
		
		
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

		//TextView tv = new TextView(this);
		//tv.setText("U dont see me!");
		//tv.setVisibility(View.VISIBLE);
		//tv.setBackgroundColor(Color.BLUE);
		//CCDirector.sharedDirector().getActivity().addContentView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		
		// Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        mPreview = new Preview(this);
        FB=new mirfb(this);
        file = getFileStreamPath(FILENAME);
        
        if(file.exists())
        {
        	firstLaunch = false;        	
        	Log.e("save0","file.exists()soLoadFighter");
        	loadFighter();
        	
        	//Log.e("save0","figherID"+tomatoFighter.getID());
        }
    	else
	    {
    		Log.e("save0","file not found");
    		firstLaunch = true;
    		//newFighter();   		
    		
    	}
        //CustomCameraView cv = new CustomCameraView(
		//	this.getApplicationContext());
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				
        //input.setInputType(type)
		
        
		ll.addView(mPreview);
		//ll.addView(tv);
		ll.addView(_glSurfaceView);
		
		
		setContentView(ll);		
		
	}
	public void sdAccess()
	{
		runOnUiThread(new Runnable() { 
	        public void run() 
	        { 
	        	alert.setTitle("SD card");
				alert.setMessage("SD card unavailible and needed! ");
				alert.setIcon(R.drawable.icon);
				alert.setCancelable(false);
				alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						onStop();
						
					}
			        }
				);
					}
				});				
				alert.show();
	        	
	}
	public void fbLogin()
	{
		runOnUiThread(new Runnable() { 
	        public void run() 
	        { 
	        	alert.setTitle("Facebook");
				alert.setMessage("登入FB邀請親朋好友一起戰鬥?!");
				alert.setIcon(R.drawable.afbicon);
				alert.setCancelable(false);
				alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if(isInternetOn)
						{
							inTransition = true;
							DialogListener fbDialog=new DialogListener() {
					            @Override
					            public void onComplete(Bundle values) {
					            	//FB.get_me() is required
					            	Log.e("fbbbbbbb",FB.get_me().toString());  		            	
					            	
					            }
	
					            @Override
					            public void onFacebookError(FacebookError error) {
					            	Log.e("fbbbbbbb","login error:" + error.toString() );
					            }
	
					            @Override
					            public void onError(DialogError e) {
					            	Log.e("fbbbbbbb","error:"+ e.toString());
						        }
	
					            @Override
					            public void onCancel() {
					            	Log.e("fbbbbbbb","login: cancel");
						        }
					        };
					        Log.e("@@@@", (FB==null)? "null" :"not null");
							FB.login(fbDialog);
				        }
						else
						{
							toastMessage("請連上網路以登入FB~");
							alert.show();
						}
					}
			        }
				);
					}
				});
				alert.setNeutralButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {			
						CCScene scene = GameLoadingLayer.scene();
						//CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(1, scene, ccColor3B.ccWHITE));
						CCDirector.sharedDirector().runWithScene(scene);							
					}
				});
				alert.show();
	        	
	}
	public void newFighter()
	{
		firstLaunch = false;
		runOnUiThread(new Runnable() { 
	        public void run() 
	        { 
				//input.setInputType(type)
				alert.setTitle("歡迎，番茄戰士!");
				alert.setMessage("第一次執行，進入教學頁面~");
				//alert.setView(input);
				alert.setIcon(R.drawable.icon);
				alert.setPositiveButton("繼續", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//String value = input.getText().toString().trim();
						tomatoFighter = new TomatoFighter("Fighter", 1000);
						tomatoFighter.setGameStatus(0, 1);	//?�放第�???	
						tomatoFighter.setGameStatus(14, 1);	//?�放第�???	DEMO!
			        	tomatoFighter.setMoney(999999);
			        	tomatoFighter.setWeaponStatus(1, 1);
			        	tomatoFighter.setPlayerLevel(14);
						
						tomatoFighter.setGameStatus(1, 1);
						tomatoFighter.setGameStatus(2, 1);
						tomatoFighter.setGameStatus(3, 1);
						tomatoFighter.setGameStatus(4, 1);
						tomatoFighter.setGameStatus(5, 1);
						tomatoFighter.setGameStatus(6, 1);
						tomatoFighter.setGameStatus(7, 1);
//						tomatoFighter.setPlayerLevel(4);
//						tomatoFighter.setWeaponStatus(1, 1);
						saveFighter();
						isCreatingNew = true;
						CCDirector.sharedDirector().pushScene(HelpLayer.scene());
					}
				});
				alert.setNeutralButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
					}
				});/*
				alert.setNegativeButton("?��?", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
									
					}
				});*/
				alert.show();
	        }
	        }
		);
	}
	public void leaveGameSessionConfirmAlert()
	{
		runOnUiThread(new Runnable() { 
	        public void run() 
	        { 
				//input.setInputType(type)
				alert.setTitle("確定離開?");
				alert.setMessage("此關遊戲進度不會被保存");
				alert.setIcon(R.drawable.icon);
				alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inTransition = true;
						CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameStartLayer.scene(), tOrientation.kOrientationLeftOver));
					}
				});
				alert.setNeutralButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {						
					}
				});
				alert.show();
	        }
	        }
		);
	}
	
	public void toastMessage(final CharSequence textmsg)
	{
		runOnUiThread(new Runnable() { 
			public void run() 
			{ 
				if(toast != null)
				{
					toast.cancel();
				}
				toast = new Toast(getApplicationContext());
				TextView textView = new TextView(getApplicationContext());  
				textView.setBackgroundColor(Color.DKGRAY);
				textView.setText(textmsg);
				textView.setPadding(10, 10, 10, 10);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout linearLayout = new LinearLayout(getApplicationContext());  
				ImageView iView= new ImageView(getApplicationContext());  
				iView.setImageResource(R.drawable.icon); //請自?��????�是?�R.drawable.icon 
				linearLayout.addView(iView);  
				linearLayout.addView(textView);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(linearLayout);  
				toast.show();
				/*
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast_layout,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView image = (ImageView) layout.findViewById(R.id.image);
				image.setImageResource(R.drawable.icon);
				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText(textmsg);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();*/
			}} );		
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
		} catch (Exception e) {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
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
		} catch (Exception e) {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	try {
			tomatoFighter = (TomatoFighter) ois.readObject();
    		//String test = (String) ois.readObject();
			//Log.e("save1","oisread"+tomatoFighter.getID());
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onStart()
	{
		super.onStart();
		
		((TomatoshootGame)CCDirector.sharedDirector().getActivity()).inTransition = true;
		
		CCDirector.sharedDirector().attachInView(_glSurfaceView);
		//CCDirector.sharedDirector().
		//CCDirector.sharedDirector().setAlphaBlending(_glSurfaceView.get, true);
		//CCDirector.sharedDirector().getOpenGLView().setBackgroundColor(Color.TRANSPARENT);
		//_glSurfaceView.set
		
		CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
		
		///CCDirector.sharedDirector().setDisplayFPS(true);
		
		CCDirector.sharedDirector().setAnimationInterval(1.0f / 40.0f);
		
		//CCDirector.sharedDirector().runWithScene(GameStartLayer.scene());
		String state = Environment.getExternalStorageState();	
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    //mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    //mExternalStorageAvailable = mExternalStorageWriteable = false;
			sdAccess();			
			return;
		}
		
		if(FB.is_login()){
			CCScene scene = GameLoadingLayer.scene();
			//CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(1, scene, ccColor3B.ccWHITE));
			CCDirector.sharedDirector().runWithScene(scene);	
			
			//CCScene scene = GameStartLayer.scene();
			//CCDirector.sharedDirector().runWithScene(scene);	
			
		}
		else{
			//((TomatoshootGame)CCDirector.sharedDirector().getActivity()).fbLogin();
			fbLogin();
			
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
		 onStop();
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
		if(CCDirector.sharedDirector().getRunningScene() != null)
		CCDirector.sharedDirector().getRunningScene().onExit();
		
		saveFighter();
		SoundEngine.purgeSharedEngine();
		CCDirector.sharedDirector().end();
		Log.d("-----","onStop!");
		//finish();
	}	
	@Override
	public void onBackPressed()
	{
		Log.d("-----","keyback!");
		if(inTransition)
		{
			return;
		}
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
			if(((GameLayerBase) CCDirector.sharedDirector().getRunningScene().getChildren().get(0)).gamePaused)
			{
				((GameLayerBase) CCDirector.sharedDirector().getRunningScene().getChildren().get(0)).pauseScreenLayer.unPauseGame();
			}
			else
			{
				////((GameLayerBase) CCDirector.sharedDirector().getRunningScene().getChildren().get(0)).pauseGame();
				((TomatoshootGame)CCDirector.sharedDirector().getActivity()).leaveGameSessionConfirmAlert();
				//inTransition = true;
				//CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationLeftOver));
				//CCDirector.sharedDirector().replaceScene(SetGameItemLayer.scene());
			}
		}
		else if(CCDirector.sharedDirector().getRunningScene().getTag() == 3) //help layer //about
		{
			CCDirector.sharedDirector().popScene();
		}
		else if(CCDirector.sharedDirector().getRunningScene().getTag() == 4) //setting layer
		{
			CCDirector.sharedDirector().popScene();
		}
		else if(CCDirector.sharedDirector().getRunningScene().getTag() == 5) //gameover layer
		{
			inTransition = true;
			CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, SetGameItemLayer.scene(), tOrientation.kOrientationLeftOver));
			
		}
		else if(CCDirector.sharedDirector().getRunningScene().getTag() == 1) //setgame layer
		{
			inTransition = true;
			//SoundEngine.sharedEngine().realesAllSounds();
			CCDirector.sharedDirector().replaceScene(CCFlipAngularTransition.transition(0.5f, GameStartLayer.scene(), tOrientation.kOrientationLeftOver));
			//CCDirector.sharedDirector().replaceScene(GameStartLayer.scene());
		}
		//task to be done here.. Eg, an alert dialog asking weather to exit?
		return;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(inTransition)
		{
			return true;
		}
	    /*if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	  Log.d("-----","keyback!");
	    	  if(inTransition)
	    	  {
	    		  return true;
	    	  }
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
	    else */
	    if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {	    	  
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
		
        CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("SetGameItemLayer.plist");        		
		appUserArray = FB.get_appusers();
		friendObj = FB.get_friends();
		topNArray = FB.get_top_n("level", 6);
		
		Bitmap mIcon1 = null;
		URL img_value = null;
		
		dialog = ProgressDialog.show(this, "Friends Information", 
	            "Loading. Please wait...", true);
		for(int i = 0;i < topNArray.length();i++) {			
			try {
				JSONObject person = topNArray.getJSONObject(i);	// ?��?每個人?��???已�??��??��?)
				try {
					fNameList.add(person.getString("name"));
					fLevelList.add(person.getString("value"));
					img_value = new URL("http://graph.facebook.com/" + person.getString("id") + "/picture");
					mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());	
					friendSprite[i] = CCSprite.sprite(mIcon1);//CCMenuItemSprite.item(CCSprite.sprite(mIcon1), CCSprite.sprite(mIcon1), this, null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					fNameList.add("正義使者");
					fLevelList.add("1");
					friendSprite[i] = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tomato.png"));
					friendSprite[i].setTag(77);
				}
			} catch (JSONException e) {
				fNameList.add("正義使者");
				fLevelList.add("1");
				friendSprite[i] = CCSprite.sprite(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName("tomato.png"));
				friendSprite[i].setTag(77);
				//e.printStackTrace();
			}			
		}
		dialog.dismiss();
    }
}