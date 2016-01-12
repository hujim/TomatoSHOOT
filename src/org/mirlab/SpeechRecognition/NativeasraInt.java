package org.mirlab.SpeechRecognition;
import android.util.Log;

public class NativeasraInt {
	static{
		try{
			Log.i("JNI","Trying to load libNativeasraInt.so");
			System.loadLibrary("NativeasraInt");
			Log.i("JNI","Load libNativeasraInt.so successful");
		}
		catch(UnsatisfiedLinkError ule){
			Log.e("JNI", "WARNING: Could not load libNativeasraInt.so");
		}
	}  
	public static native int recog(String prmFile, String waveFile, String txtFile, String sylFile, String netFile, String outputDir, String getPitch, String targetCmFile);
}
