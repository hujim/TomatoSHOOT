package org.mirlab.tomatoshoot;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.Util;

public class mirfb {
	public static final String APP_ID = "196113547121855";//"165449133505066"; //"175729095772478";
	private static final String BASE_URL="http://www.apponline.tw/cht/";
	private static final String PROPERTY_URL=BASE_URL+"index.php/property/";
	
	public String id="";
    private Facebook mFacebook;    
    //private AsyncFacebookRunner mAsyncRunner;

    private static final String[] PERMISSIONS =
        new String[] {"publish_stream", "read_stream", "offline_access"};
    
    private Activity view;
    
    public mirfb(Activity v) {
        mFacebook=new Facebook(APP_ID);    
        //mAsyncRunner = new AsyncFacebookRunner(mFacebook);    	
    	view=v;
    }
    
    public void login(DialogListener e){
        mFacebook.authorize(view,PERMISSIONS, e);
    }
    
    public Boolean is_login(){
		return mFacebook.isSessionValid();
    }
    
    /*
    public JSONObject get_appusers(){
    	Bundle parameters = new Bundle();	//friends.getAppUsers
    	parameters.putString("method", "friends.getAppUsers");
    	return get_legacy(parameters);
    }*/
    

    public JSONArray get_appusers(){
    	Bundle parameters = new Bundle();	//friends.getAppUsers
    	parameters.putString("method", "friends.getAppUsers");
    	JSONArray result;
		try {
			result = new JSONArray(mFacebook.request(parameters));
		} catch (Throwable e) {
			result = new JSONArray();
			e.printStackTrace();
		}
		return result;
    }

    public JSONObject get_friends(){
    	return get_request("me/friends");
    }

    public JSONObject get_me(){
    	JSONObject me = get_request("me");
    	Log.d("get_me","getting");
    	try{
    		id = me.getString("id");
        	Log.d("getted_id",id);
    	}catch(Exception e){

        	Log.d("get_me","except"+e.toString());
    	}
    	
    	Log.d("id",id);
    	return me;
    }
    
    
    
    /*	Legacy API	*/
    public JSONObject get_legacy(Bundle parameters){
    	try{
    		String s = mFacebook.request(parameters);
    		if(s.substring(1, 1)=="["){
    			s= "{'data':"+s+"}";
    		}
	        return Util.parseJson(  s );
    		
        } catch (Throwable e) {
            e.printStackTrace();
        	Log.d("get_legacy","error:"+e.toString());
            return new JSONObject();
        }
    }
    
    
    /*	graph	*/
    private JSONObject get_request(String req){
    	try{
	        return Util.parseJson( mFacebook.request(req) );
        } catch (Throwable e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }    

    /*	get personal property	*/
    public String get_property(String property){
    	try{
    		String url=PROPERTY_URL + "get/";
    		String httpMethod="POST";
    		Bundle params=new Bundle();
    		params.putString("id", id);
    		params.putString("property", property);
    		String a= Util.openUrl(url, httpMethod, params);
    		
    		Log.d("get_property", a);
    		return a;
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }
    

    /*	get personal property	*/
    public String get_friends_property(String friends_id,String property){
    	try{
    		String url=PROPERTY_URL + "get/";
    		String httpMethod="POST";
    		Bundle params=new Bundle();
    		params.putString("id", friends_id);
    		params.putString("property", property);
    		String a= Util.openUrl(url, httpMethod, params);
    		
    		Log.d("get_friends_property", a);
    		return a;
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }

    /*	set personal property	*/
    public Boolean set_property(String property,String value){
    	try{
    		String url=PROPERTY_URL + "set/";
    		String httpMethod="POST";
    		Bundle params=new Bundle();
    		params.putString("id", id);
    		params.putString("property", property);
    		params.putString("value", value);
    		
    		String a= Util.openUrl(url, httpMethod, params);

    		//Log.d("set_property:excute", params.toString());
    		//Log.d("set_property:result", a);
    		return a.equals("success");
    		//return a;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
            //return "";
        }
    }
    
    public void authorizeCallback(int requestCode, int resultCode, Intent data) {
    	mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    public String logout(Context context){
    	try{
    		return mFacebook.logout(context);
    	}catch(Exception e){
    		
    	}
    	return null;
    }
    public void postOnWall(String msg) {
        Log.d("Tests", "Testing graph API wall post");
         try {
                String response = mFacebook.request("me");
                Bundle parameters = new Bundle();
                parameters.putString("message", msg);
                parameters.putString("description", "test test test");
                response = mFacebook.request("me/feed", parameters, 
                        "POST");
                Log.d("Tests", "got response: " + response);
                if (response == null || response.equals("") || 
                        response.equals("false")) {
                   Log.v("Error", "Blank response");
                }
         } catch(Exception e) {
             e.printStackTrace();
         }
    }
    public ArrayList<String> get_friendFieldArray(String field){
    	ArrayList<String> fieldList = new ArrayList<String>(); 
    	JSONObject jObject = null;
		JSONArray fieldObject = null;
		try {
			jObject = new JSONObject(this.get_friends().toString());
			fieldObject = jObject.getJSONArray("data");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0;i < fieldObject.length();i++){
			try {
				fieldList.add(fieldObject.getJSONObject(i).getString(field).toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//Log.d("FFFFFF","@@" + fieldList.get(0));
    	return fieldList;
    }
    
    public ArrayList<String> get_friendAppUsersFieldArray(){
    	ArrayList<String> fieldList = new ArrayList<String>();
		JSONArray fieldObject = this.get_appusers();
		for(int i = 0;i < fieldObject.length();i++){
			try {
				fieldList.add(fieldObject.get(i).toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    	return fieldList;
    }
    public ArrayList<String> get_friendNameArray(){
    	ArrayList<String> NameList = new ArrayList<String>(); 
    	return NameList;
    }
}
