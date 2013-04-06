package com.example.tictactoe_by_ashish;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

public class Player1_Instances {

	private CCSprite player;
	private float x_boxNum,y_BoxNum;
	private float x;
	private float y;
	
	Player1_Instances(float x, float y,float x_boxNum,float y_boxNum ){
		
		setX(x);
		setY(y);
		setX_boxNum(x_boxNum);
		setY_BoxNum(y_boxNum);
		
		CGPoint location = CGPoint.ccp(x,y);;		
		CCSprite player=CCSprite.sprite("o.png");
		player.setPosition(location);
		setPlayer(player);
	}

	public CCSprite getPlayer() {
		return player;
	}

	public void setPlayer(CCSprite player) {
		this.player = player;
	}

	public float getY_BoxNum() {
		return y_BoxNum;
	}

	public void setY_BoxNum(float y_BoxNum) {
		this.y_BoxNum = y_BoxNum;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX_boxNum() {
		return x_boxNum;
	}

	public void setX_boxNum(float x_boxNum) {
		this.x_boxNum = x_boxNum;
	}
}
