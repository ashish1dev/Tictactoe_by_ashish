package com.example.tictactoe_by_ashish;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	protected CCGLSurfaceView _glSurfaceView;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        
        _glSurfaceView =new CCGLSurfaceView(this);
        //_glSurfaceView.setBackgroundDrawable(Drawable.createFromPath("board.png"));
        _glSurfaceView.setEnabled(true);
        _glSurfaceView.setZOrderOnTop(true);
        
        setContentView(_glSurfaceView);

    }
    
    @Override
    public void onStart(){
    	
    	super.onStart();
    	CCDirector.sharedDirector().attachInView(_glSurfaceView);
    	CCDirector.sharedDirector().setDisplayFPS(true);
    	CCDirector.sharedDirector().setAnimationInterval(1.0f/60.0f);
    	CCScene scene = MenuLayer.scene();//GameLayer.scene("Robot");////
		CCDirector.sharedDirector().runWithScene(scene);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	CCDirector.sharedDirector().pause();
    }
    
    @Override public void onResume(){
    	super.onResume();
    	CCDirector.sharedDirector().resume();
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	CCDirector.sharedDirector().end();
    }
    
}
