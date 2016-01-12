package org.mirlab.tomatoshoot;

import java.io.IOException;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class CustomCameraView extends SurfaceView implements SurfaceHolder.Callback
{

	Camera camera;
	SurfaceHolder previewHolder;
	public CustomCameraView(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
		previewHolder = this.getHolder();
			previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			previewHolder.addCallback(this);

			
			
	}
	
//	SurfaceHolder.Callback surfaceHolderListener = new SurfaceHolder.Callback() {
//		
//		@Override
//		public void surfaceDestroyed(SurfaceHolder holder) {
//			// TODO Auto-generated method stub
//			camera.stopPreview();
//			camera.release();
//			camera = null;
//			
//		}
//		
//		@Override
//		public void surfaceCreated(SurfaceHolder holder) {
//			// TODO Auto-generated method stub
//			camera=Camera.open();
//
//
//			
//
//			
//		}
//		
//		@Override
//		public void surfaceChanged(SurfaceHolder holder, int format, int width,
//				int height) {
//			// TODO Auto-generated method stub
//			Parameters params = camera.getParameters();
//			params.setPreviewSize(width, height);
//			params.setPictureFormat(PixelFormat.JPEG);
//				camera.setParameters(params);
//				
//				try {
//					camera.setPreviewDisplay(previewHolder);
//					camera.startPreview();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			
//		}
//	};
	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Parameters params = camera.getParameters();
//		params.setPreviewSize(arg2, arg3);
		params.setPictureFormat(PixelFormat.JPEG);
			camera.setParameters(params);
			
			try {
				camera.setPreviewDisplay(previewHolder);
				camera.startPreview();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera=Camera.open();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
		camera.release();
		camera = null;
	}
	
}