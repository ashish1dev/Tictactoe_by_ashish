package com.example.tictactoe_by_ashish;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

public class Robot_Player {

	private CCSprite robot_player;
	private float x_boxNum,y_BoxNum;
	private float x;
	private float y;


	public  Robot_Player(float x, float y, float x_boxNum,float y_boxNum){
		
		setX(x);
		setY(y);
		setX_boxNum(x_boxNum);
		setY_BoxNum(y_boxNum);
		
		CGPoint location = CGPoint.ccp(x,y);
		CCSprite robot_player=CCSprite.sprite("x.png");
		robot_player.setPosition(location);
		setRobot_player(robot_player);
	}
	public CCSprite getRobot_player() {
		return robot_player;
	}
	public void setRobot_player(CCSprite robot_player) {
		this.robot_player = robot_player;
	}
	public float getX_boxNum() {
		return x_boxNum;
	}
	public void setX_boxNum(float x_boxNum) {
		this.x_boxNum = x_boxNum;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY_BoxNum() {
		return y_BoxNum;
	}
	public void setY_BoxNum(float y_BoxNum) {
		this.y_BoxNum = y_BoxNum;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	

}
