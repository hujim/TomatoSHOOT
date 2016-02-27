package org.mirlab.tomatoshoot;

import javax.microedition.khronos.opengles.GL10;

import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.opengl.CCDrawingPrimitives;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import android.graphics.Bitmap;

public class RadarCCSprite extends CCSprite {

	static int	_bug_CGPoints_size = 0;
	static CGPoint _bug_CGPoints[];
	static CCNode drawPointsNode = new CCNode() {
		@Override
		public void draw(GL10 gl) {
			//HP line
		  	//gl.glDisable(GL10.GL_LINE_SMOOTH);
	        //gl.glLineWidth(45.0f);
	        //gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
	        //CCDrawingPrimitives.ccDrawLine(gl, CGPoint.ccp(0, 0), CGPoint.ccp(100, 100));
	        //radar stuff         
	        gl.glPointSize(3);
	        gl.glColor4f(0.0f, 1.0f, 1.0f, 1.0f);	        
	        CCDrawingPrimitives.ccDrawPoints(gl, _bug_CGPoints, _bug_CGPoints_size);    		
	        
	        super.draw(gl);
		}
	};
	
	public RadarCCSprite(CCTexture2D texture) {
		super(texture);
		// TODO Auto-generated constructor stub
	}

	public RadarCCSprite(CCTexture2D texture, CGRect rect) {
		super(texture, rect);
		// TODO Auto-generated constructor stub
	}

	public RadarCCSprite(CCSpriteFrame spriteFrame) {
		super(spriteFrame);
		// TODO Auto-generated constructor stub
	}

	public RadarCCSprite(String spriteFrameName, boolean isFrame) {
		super(spriteFrameName, isFrame);
		// TODO Auto-generated constructor stub
	}

	public RadarCCSprite(String filename) {
		super(filename);
		// TODO Auto-generated constructor stub
	}

	public RadarCCSprite() {
		// TODO Auto-generated constructor stub
	}

	public RadarCCSprite(String filename, CGRect rect) {
		super(filename, rect);
		// TODO Auto-generated constructor stub
	}

	public RadarCCSprite(Bitmap image, String key) {
		super(image, key);
		// TODO Auto-generated constructor stub
	}

	public RadarCCSprite(CCSpriteSheet spritesheet, CGRect rect) {
		super(spritesheet, rect);
		// TODO Auto-generated constructor stub
	}
	
	public void setRadarPoints(CGPoint[] bug_CGPoints, int bug_CGPoints_size)
	{
		_bug_CGPoints = bug_CGPoints;
		_bug_CGPoints_size = bug_CGPoints_size;
	}
	/** Creates an sprite with an sprite frame.
	    */
    public static RadarCCSprite sprite(CCSpriteFrame spriteFrame) {
    	RadarCCSprite aRadarCCSprite = new RadarCCSprite(spriteFrame);
    	aRadarCCSprite.addChild(drawPointsNode);
        return aRadarCCSprite;
    }
	/** Creates an sprite with an image filename.
    The rect used will be the size of the image.
    The offset will be (0,0).
    */
	
	public static RadarCCSprite sprite(String filename) {
		RadarCCSprite aRadarCCSprite = new RadarCCSprite(filename);
    	aRadarCCSprite.addChild(drawPointsNode);
	    return aRadarCCSprite;
	}	
}
