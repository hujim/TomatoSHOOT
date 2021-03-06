package org.mirlab.SpeechRecognition;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.cocos2d.actions.instant.CCShow;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.mirlab.tomatoshoot.GameLayerBase;
import org.mirlab.tomatoshoot.volumeAdjustLayer;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class pitchRecorder implements Runnable {
	private int frequency;
	private int channelConfiguration;
	private float pitch = 0;
	private double volume;
	private volatile boolean isPaused;
	private File waveFileName;
	private volatile boolean isRecording;
	private boolean outputToFile;
	private boolean computePitch;
	private boolean computeZCR;
	private boolean computeMeanVol;
	private boolean autoMean;
	private boolean isEnd;
	private boolean fireOpen;
	private final Object mutex = new Object();
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	private int onTime=0;
	private int savTime;
	private int initialCounter; // 預防錄音一打開音量會爆衝
	private int pitchCounter;
	private int meanVol;
	private int zcr;
	private int volThreshold;
	//private int choose;
	private File root;
	private BufferedWriter out;
	private File gpxfile;
	private FileWriter gpxwriter;
	private int r_start=0;
	private int r_end=0;
	private double zcr_threshold=250;
	private int shot_interval=0;
	private int zcr_len=4;
	
	//ship
	private double front_avg=0;
	private double back_avg=0;	
	private double[] volume_record={0,0,0,0};
	private double[] zcr_record={0,0,0,0};
	private int valume_record_idx=0;
	private boolean shotting=false;
	private boolean summing=false;
	
	// public CCBitmapFontAtlas _label;
	public GameLayerBase mainGame;
	public volumeAdjustLayer adjustThres;
	
	public static int choose;
	public boolean masterOpen;
	public boolean masterEnd;
	public boolean tooShort; 

	public void calc_avg(){
		int i2=valume_record_idx;
		double sum;	
		if(--i2<0) i2=3;
		sum=volume_record[i2];
		if(--i2<0) i2=3;
		sum+=volume_record[i2];
		back_avg= sum / 2;

		if(--i2<0) i2=3;
		sum=volume_record[i2];	
		if(--i2<0) i2=3;
		sum+=volume_record[i2];
		front_avg= sum / 2;
		
	}
	
	public pitchRecorder(int meanVol, int volThreshold ,CCColorLayer Layer, int choose) {
		super();
		this.setFrequency(16000);
		this.setChannelConfiguration(AudioFormat.CHANNEL_CONFIGURATION_MONO);
		this.setPaused(false);
		this.setRecording(false);
		this.setOutputToFile(false);
		this.setComputePitch(false);
		this.setComputeZCR(false);
		this.setComputeMeanVol(false);
		this.setIsEnd(false);
		this.setFireOpen(true);
		this.setAutoMean(false);
		this.initialCounter = 0;
		this.pitchCounter = 0;
		this.onTime = 0;
		this.savTime = 0;
		this.volThreshold = volThreshold;
		this.meanVol = meanVol;
		this.choose = choose;
		this.masterOpen = false;
		this.masterEnd = false;
		this.tooShort = false;
		if(choose == 1)
			this.mainGame = (GameLayerBase) Layer;	
		else if(choose == 2)
			this.adjustThres = (volumeAdjustLayer) Layer;
		/*
		File root = Environment.getExternalStorageDirectory();
	    if (root.canWrite()){
	        gpxfile = new File(root, "pitch.txt");
	        gpxwriter = null;
			try {
				gpxwriter = new FileWriter(gpxfile);
		        out = new BufferedWriter(gpxwriter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    */

	}
	
	protected void finalize() throws Throwable
	{
        //out.close();
	} 
	
	public void run() {
		// Wait until we're recording...
		synchronized (mutex) {
			while (!this.isRecording) {
				try {
					mutex.wait();
				} catch (InterruptedException e) {
					throw new IllegalStateException("Wait() interrupted!", e);
				}
			}
		}

		// We're important...
		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

		// Allocate Recorder and Start Recording...

		ArrayList<Short> allBuf = new ArrayList<Short>();
		ArrayList<Short> masterBuf = new ArrayList<Short>();
		ArrayList<Double> addVolBuf = new ArrayList<Double>();

		int bufferSize = AudioRecord.getMinBufferSize(this.getFrequency(), this
				.getChannelConfiguration(), this.getAudioEncoding());
		AudioRecord recordInstance = new AudioRecord(
				MediaRecorder.AudioSource.MIC, this.getFrequency(), this
						.getChannelConfiguration(), this.getAudioEncoding(),
				bufferSize);
		// short[] tempBuffer = new short[bufferSize];
		short[] buffer = new short[bufferSize / 2];
		double[] volTemp = new double[1];
		try {
			recordInstance.startRecording();
		} catch (Exception e) {
			recordInstance.startRecording();
			// TODO: handle exception
		}
		while (this.isRecording) {
			// Are we paused?
			synchronized (mutex) {
				if (this.isPaused) {
					try {
						mutex.wait(250);
					} catch (InterruptedException e) {
						throw new IllegalStateException("Wait() interrupted!",
								e);
					}
					continue;
				}
			}

			int bufferRead = recordInstance.read(buffer, 0, bufferSize / 2);

			if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException(
						"read() returned AudioRecord.ERROR_INVALID_OPERATION");
			} else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
				throw new IllegalStateException(
						"read() returned AudioRecord.ERROR_BAD_VALUE");
			} else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException(
						"read() returned AudioRecord.ERROR_INVALID_OPERATION");
			}
			
			allBuf.clear();
			for (int idxBuffer = 0; idxBuffer < bufferRead; idxBuffer++) {
				allBuf.add(buffer[idxBuffer]);
			}
			zcr = buffer2ZCR(allBuf);
			
			setVol(frame2volume(buffer, bufferRead));
			volume_record[valume_record_idx]=getVol();
			zcr_record[valume_record_idx++]=zcr;
			valume_record_idx%=zcr_len;
			calc_avg();
			
			//Log.d("@@@@@","@@@@@" + this.getVol());
			//Log.d("@@@@@","@@@@@" + zcr);

			if (this.computeMeanVol) {
				if (this.outputToFile) {
					volTemp[0] = getVol();
					addVolBuf.add(volTemp[0]);
					if(this.autoMean)
						setMeanVol((int)((double)(buffer2MeanVol(addVolBuf))*2));
					
					//Log.d("OOOOO", "OOO" + String.valueOf(meanVol));
				}
			} 
			if(this.masterOpen){
				for (int idxBuffer = 0; idxBuffer < bufferRead; idxBuffer++) {
					masterBuf.add(buffer[idxBuffer]);
				}
			}
			else{
				//Log.e("ship",front_avg+"  "+back_avg+"  "+meanVol+"    "+r_start+"  ");
				//----------------------------ship------------------------------
				if (front_avg>0 && (back_avg-front_avg)/front_avg>2.5){
			        if(r_start==0 && back_avg>meanVol){
			            r_start=1;
			            onTime=0;
			        }
	
	        		int i;
	        		for (i=0;i<zcr_len;i++){
	        			if(zcr_record[i]>zcr_threshold)
	        				break;        				
	        		}
	                if(i!=zcr_len){	//show
	                	shotting=true;
	                	if(shot_interval>=2){
	                    	Log.e("shut","showwwww");
							if(choose == 1){
								mainGame.fireParticle();
								//mainGame._label.setString("show"+ String.valueOf(this.getZCR()));
							}
							else if(choose == 2){
								adjustThres.showWord.runAction(CCSequence.actions(
										CCShow.action(),
										CCFadeIn.action(0.5f),
										CCFadeOut.action(0.5f)));
								//adjustThres._label.setString("show"+ String.valueOf(this.getVol()));
								//adjustThres.showWord.setVisible(true);
								//adjustThres.bombWord.setVisible(false);
							}
					        setPitch(0);
	                	}
						
	                }
	                shot_interval=1;
	                summing=false;
		            r_end=0;
			        onTime=onTime+1;
			        
				}else if(back_avg>0 && (front_avg-back_avg)/back_avg>0.4){;
	
	        		Log.e("curves","down");
	        		
	        		
			        if(r_end==0 && r_start!=0 && front_avg>meanVol ){
			        	if(onTime<8){
			                if(!shotting&&!summing){	//bomb
			                	Log.e("shut","bomb");
			                	if(choose == 1){
									mainGame.fireBong();
									//mainGame._label.setString("bomb"+ String.valueOf(this.getZCR()));
								}
								else if(choose == 2){
									adjustThres.bombWord.runAction(CCSequence.actions(
											CCShow.action(),
											CCFadeIn.action(0.5f),
											CCFadeOut.action(0.5f)));
									//adjustThres._label.setString("bomb"+ String.valueOf(this.getVol()));
									//adjustThres.bombWord.setVisible(true);
									//adjustThres.showWord.setVisible(false);
								}
			    		        //setPitch(0);
			                }
			        	}
	    	            r_end=1;
	    		        shotting=false;
			        }		  
			        
			        summing=false;
			        onTime=0;
			        shot_interval=shot_interval+1;
				}else{
					
			        if(r_start!=0 && back_avg>meanVol){
			            if(onTime>2 ){	//summmmm
			            	if(choose == 1)	{
			            		setPitch(frame2pitchUsingAcfReturnSemitone(buffer, Math.min(bufferRead, 512)));
			            		//mainGame._label.setString("pitch:"+pitch);
			            		summing=true;
			            	}
			            }else{
			            	summing=false;		            	
			            }
			            onTime=onTime+1;
			        }else{
			            onTime=0;
				        //setPitch(0);
				        summing=false;
			        }
		            r_end=0;
		            shot_interval=shot_interval+1;
				}       
				//---------------------end ship--------------------------------
				initialCounter++;
				if (initialCounter == 3)
					this.setOutputToFile(true);
				
				//masterBuf.clear();
			}
			if(masterEnd){
				this.save2wav(masterBuf);
				mainGame.masterThread.start();
				masterEnd = false;
				masterBuf.clear();
			}
			if(tooShort){
				masterBuf.clear();
				tooShort = false;
			}
			
		}
		
		savTime = 0;
		recordInstance.release();
		if (!isEnd) {
			if (this.computeMeanVol) {
				//setMeanVol((int)((double)buffer2MeanVol(addVolBuf)*1.2));
				setMeanVol(2000000);
				this.computeMeanVol = false;
				//Log.d("OOOOOOOOO", "OOOOO" + String.valueOf(meanVol));
			} else {
//				if(choose == 1){
//					mainGame.wordRecThread = new Thread(mainGame.wordRecRecorder);
//					mainGame.wordRecRecorder.setwaveFileName(new File(
//							"/sdcard/recData/output/rec.wav"));
//					mainGame.wordRecThread.start();
//					mainGame.wordRecRecorder.setRecording(true);
//				}
			}
		}

	}

	public void setComputeMeanVol(boolean computeMeanVol) {
		this.computeMeanVol = computeMeanVol;
	}

	public void setComputePitch(boolean computePitch) {
		this.computePitch = computePitch;
	}

	public void setComputeZCR(boolean computeZCR) {
		this.computeZCR = computeZCR;
	}

	public void setOutputToFile(boolean outputToFile) {
		this.outputToFile = outputToFile;
	}

	public void setIsEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public void setFireOpen(boolean fireOpen) {
		this.fireOpen = fireOpen;
	}
	public void setAutoMean(boolean autoMean){
		this.autoMean = autoMean;
	}
	public void setwaveFileName(File waveFileName) {
		this.waveFileName = waveFileName;
	}

	public File getwaveFileName() {
		return waveFileName;
	}

	/**
	 * @param isRecording
	 *            the isRecording to set
	 */
	public void setRecording(boolean isRecording) {
		synchronized (mutex) {
			this.isRecording = isRecording;
			if (this.isRecording) {
				mutex.notify();
			}
		}
	}

	/**
	 * @return the isRecording
	 */
	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * @param channelConfiguration
	 *            the channelConfiguration to set
	 */
	public void setChannelConfiguration(int channelConfiguration) {
		this.channelConfiguration = channelConfiguration;
	}

	/**
	 * @return the channelConfiguration
	 */
	public int getChannelConfiguration() {
		return channelConfiguration;
	}

	/**
	 * @return the audioEncoding
	 */
	public int getAudioEncoding() {
		return audioEncoding;
	}

	/**
	 * @param isPaused
	 *            the isPaused to set
	 */
	public void setPaused(boolean isPaused) {
		synchronized (mutex) {
			this.isPaused = isPaused;
		}
	}

	/**
	 * @return the isPaused
	 */
	public boolean isPaused() {
		synchronized (mutex) {
			return isPaused;
		}
	}

	/**
	 * @param frequency
	 *            the pitch to set
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setVol(double vol) {
		this.volume = vol;
	}

	public void setMeanVol(int vol) {
		this.meanVol = vol;
	}
	public void setThres(int thres){
		this.meanVol = thres;
		//adjustThres._threslabel.setString("Thres:" + String.valueOf(this.volThreshold));
	}

	/**
	 * @return the pitch
	 */
	public float getPitch() {
		return pitch;
	}

	public double getVol() {
		return volume;
	}

	public int getZCR() {
		return zcr;
	}

	public int getMeanVol() {
		return meanVol;
	}

	public boolean isComputeMeanVol() {
		return computeMeanVol;
	}

	private void writeInt(DataOutputStream out, int val) throws IOException {
		out.write(val >> 0);
		out.write(val >> 8);
		out.write(val >> 16);
		out.write(val >> 24);
	}

	private void writeShort(DataOutputStream out, short val) throws IOException {
		out.write(val >> 0);
		out.write(val >> 8);
	}

	public void save2wav(ArrayList<Short> allBuf){	 
   	 // open file
   	 if (waveFileName == null) {throw new IllegalStateException("fileName is null");}
   	 if (waveFileName.exists()) {waveFileName.delete();}
   	 
   	 BufferedOutputStream waveBufferedStreamInstance = null;
        try {
       	 waveBufferedStreamInstance = new BufferedOutputStream(new FileOutputStream(waveFileName));
        }
        catch (FileNotFoundException e) {
            throw new IllegalStateException("Cannot Open File", e);
        }
        DataOutputStream waveOutputStreamInstance =new DataOutputStream(waveBufferedStreamInstance);
        
        int bufLen = allBuf.size();
        int mNumBytes = bufLen*2;
        // write wave file
        try {
       	 // write RIFF header
            waveOutputStreamInstance.writeBytes("RIFF");         // ChunkID
            this.writeInt(waveOutputStreamInstance,36+mNumBytes);// ChunkSize
            waveOutputStreamInstance.writeBytes("WAVE");         // Format
            
            // write fmt chunk
            int ch = 1;
            waveOutputStreamInstance.writeBytes("fmt ");         // Subchunk1ID
            this.writeInt(waveOutputStreamInstance,16);          // Subchunk1Size(16 for PCM)
            this.writeShort(waveOutputStreamInstance,(short)1);  // AudioFormat(PCM = 1)
            this.writeShort(waveOutputStreamInstance,(short)ch);   // NumChannels
            this.writeInt(waveOutputStreamInstance,frequency);   // SampleRate
            this.writeInt(waveOutputStreamInstance,ch*frequency*2); // ByteRate  (fs*ch*nbits/8)
            this.writeShort(waveOutputStreamInstance,(short)(ch*2));// BlockAlign(ch*fs/8)
            this.writeShort(waveOutputStreamInstance,(short)16); // BitsPerSample(fix to 16)
            
            // write data chunk
            waveOutputStreamInstance.writeBytes("data");         // Subchunk2ID
            this.writeInt(waveOutputStreamInstance,mNumBytes);   // Subchunk2Size
            //write sample points
       	 for(int i=0;i<bufLen;i++){
       		 this.writeShort(waveOutputStreamInstance,allBuf.get(i));
       	 }
       	 
       	 waveOutputStreamInstance.flush();
       	 waveOutputStreamInstance.close();
        }
        catch (IOException e) {
       	 throw new IllegalStateException("Wave write error ...QAQ");
        }
    }

	private float frame2pitchUsingAcfReturnSemitone(short[] frame,
			int framelength) {
		int minShift = Math.min(getFrequency() / 1000, framelength);
		int maxShift = Math.min(getFrequency() / 40, framelength);
		int maxIdx = minShift;
		float acf = 0;

		for (int move = minShift; move <= maxShift; move++) {
			float thisAcf = 0;
			for (int i = 0; i + move < framelength; i++) {
				thisAcf += (int) (frame[i] * frame[i + move]);
				// thisAcf += ((float)frameArray[i] / (float)32768.0) *
				// ((float)frameArray[i + move] / (float)32768.0);
			}
			if (thisAcf > acf) {
				acf = thisAcf;
				maxIdx = move;
			}
		}
		return (float) (69 + 12 * Math
				.log(((float) getFrequency() / (float) maxIdx) / 440));
	}

	private double frame2volume(short[] frame, int framelength) {
		double volume = 0;
		for (int i = 0; i < framelength; i++) {
			volume += Math.abs(frame[i]);
		}
		//Log.d("(((((","((((" + volume);
		return volume;
	}

	private int buffer2ZCR(ArrayList<Short> zcrBuf) {
		int zcr = 0;
		int total = 0;
		int mean = 0;
		int zcrTemp[] = new int[zcrBuf.size()];
		int zcrMulti[] = new int[zcrBuf.size() - 1];
		for (int i = 0; i < zcrBuf.size(); i++)
			total = total + zcrBuf.get(i).intValue();

		mean = total / zcrBuf.size();

		for (int i = 0; i < zcrBuf.size(); i++)
			zcrTemp[i] = zcrBuf.get(i).intValue() - mean;

		int i = 0;
		while (i < zcrBuf.size() - 1) {
			zcrMulti[i] = zcrTemp[i] * zcrTemp[i + 1];

			if (zcrMulti[i] <= 0)
				zcr++;

			i++;
		}
		return zcr;
	}

	private int buffer2MeanVol(ArrayList<Double> addVolBuf) {
		float total = 0;
		int meanVol = 0;
		
		for (int i = 0; i < addVolBuf.size(); i++){
			total = total + addVolBuf.get(i).floatValue();
			//Log.d("XXX","XXX" + addVolBuf.get(i).floatValue());
		}
		meanVol = (int) total / addVolBuf.size();
		
		return meanVol;
	}

}