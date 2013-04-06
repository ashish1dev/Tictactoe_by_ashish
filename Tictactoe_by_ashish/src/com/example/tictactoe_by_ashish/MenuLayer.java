package com.example.tictactoe_by_ashish;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.util.Log;
import android.view.MotionEvent;

public class MenuLayer extends CCColorLayer {

	
	protected static CCLabel _label;
	protected CCLabel _player1_label,_robot_player,_OR_label;
	private CGSize winSize;
	
	public static CCScene scene()
	{
		CCScene scene = CCScene.node();
		CCColorLayer menuLayer = new MenuLayer(ccColor4B.ccc4(255, 255, 255, 255));

		

		scene.addChild(menuLayer);

		return scene;
	}
	
	
	protected MenuLayer(ccColor4B color) {
		super(color);
		
		this.setIsTouchEnabled(true);
		
		 winSize = CCDirector.sharedDirector().displaySize();

		
		_label = CCLabel.makeLabel("Select First Player", "DroidSans", 32);
		_label.setColor(ccColor3B.ccBLACK);
		_label.setPosition(CGPoint.ccp((winSize.width / 2.0f), winSize.height-100));
		
		
		_player1_label = CCLabel.makeLabel("Player 1 (You)", "DroidSans", 32);
		_player1_label.setColor(ccColor3B.ccBLACK);
		_player1_label.setPosition(CGPoint.ccp((winSize.width / 2.0f), winSize.height-200));
		
		_OR_label = CCLabel.makeLabel("OR", "DroidSans", 32);
		_OR_label.setColor(ccColor3B.ccBLACK);
		_OR_label.setPosition(CGPoint.ccp((winSize.width / 2.0f), winSize.height-250));
		
		_robot_player = CCLabel.makeLabel("Robot", "DroidSans", 32);
		_robot_player.setColor(ccColor3B.ccBLACK);
		_robot_player.setPosition(CGPoint.ccp((winSize.width / 2.0f), winSize.height-300));
		
		
		addChild(_label);
		addChild(_player1_label);
		addChild(_OR_label);
		addChild(_robot_player);
	}
	
	String TAG="MenuLayer Tag";
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{


		CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(),event.getY()));
		
		Log.d(TAG, String.format(" (%f,%f)", location.x, location.y));
		
		if(location.x>((winSize.width / 2.0f)-90)  && location.x<((winSize.width / 2.0f)+90) && location.y>(winSize.height-200-205) && location.y<(winSize.height-200+20)){
			
			Log.d(TAG, String.format("%s","Player 1 (you) clicked !" ));
			loadGame("Player1");
		}
		else
		if(location.x>((winSize.width / 2.0f)-60)  && location.x<((winSize.width / 2.0f)+60) && location.y>(winSize.height-300-20) && location.y<(winSize.height-300+20)){
			
			Log.d(TAG, String.format("%s","Robot clicked !" ));
			loadGame("Robot");
		}
		return true;

		
	}
	
	public void loadGame(String firstPlayerName)
	{
		CCDirector.sharedDirector().replaceScene(GameLayer.scene(firstPlayerName));
	}

}
