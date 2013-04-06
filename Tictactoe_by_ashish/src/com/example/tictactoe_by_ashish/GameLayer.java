package com.example.tictactoe_by_ashish;

import java.util.ArrayList;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemLabel;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Contacts.Intents.UI;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.PopupWindow;

public class GameLayer extends CCColorLayer
{
	private static String TAG =null;	
	protected CCLabel _label,_label_restart;	
	public static  String firstPlayerName;
	protected float board_bottom_left_x,board_bottom_left_y;
	protected float board_top_left_x,board_top_left_y;
	protected float board_right_bottom_x,board_right_bottom_y;
	protected float board_right_top_x,board_right_top_y;
	protected ArrayList<Player1_Instances> _player1_instances_arraylist;
	protected int _player1_instance_count=0;
	protected ArrayList<Robot_Player> _robot_player_instances_arraylist;
	protected int _robot_player_instance_count=0;	
	protected int[][] matrix;
	protected CCMenu menu1,menu2;
	protected CCMenuItem menuitem1,menuitem2;
	private CGSize winSize;

	public static CCScene scene(String _first_PlayerName)
	{
		CCScene scene = CCScene.node();
		firstPlayerName=new String(_first_PlayerName);
		CCColorLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255));

		Log.d(TAG,String.format("received name is =%s",_first_PlayerName));

		scene.addChild(layer);

		return scene;
	}


	protected GameLayer(ccColor4B color)
	{
		super(color);

		this.setIsTouchEnabled(true);
		TAG="game screen";
		isGameOver=false;
		matrix=new int[3][3];

		_player1_instances_arraylist=new ArrayList<Player1_Instances>();
		_player1_instance_count=0;

		_robot_player_instances_arraylist=new ArrayList<Robot_Player>();
		_robot_player_instance_count=0;

		winSize = CCDirector.sharedDirector().displaySize();

		Log.d(TAG,String.format("winSize width = %f, height = %f",winSize.width,winSize.height));

		CCSprite boardImageSprite=CCSprite.sprite("board.png");
		CGPoint center_position=CGPoint.ccp(winSize.width/2,winSize.height/2);
		boardImageSprite.setPosition(center_position);
		Log.d(TAG,String.format("background position x = %f, y = %f",winSize.width/2,winSize.height/2));

		addChild(boardImageSprite);


		//length of each box on board=50 units ;
		board_top_left_x=center_position.x-(50+25); 
		board_top_left_y=center_position.y+(50+25);

		board_right_top_x=center_position.x+(75);
		board_right_top_y=center_position.y+(75);

		board_right_bottom_x=center_position.x+(75);
		board_right_bottom_y=center_position.y-(75);

		board_bottom_left_x=center_position.x-(75);
		board_bottom_left_y=center_position.y-(75);

		_label = CCLabel.makeLabel("Player 1's (Your) Turn", "DroidSans", 32);
		_label.setColor(ccColor3B.ccBLACK);
		_label.setPosition(CGPoint.ccp(winSize.width/2,board_top_left_y+50));
		addChild(_label);

		menuitem1=CCMenuItemLabel.item("Player1 (You) Wins !", this, " ");
		menu1=CCMenu.menu(menuitem1);
		menu1.setVertexZ(50);			
		menu1.setPosition(CGPoint.ccp(winSize.width/2,board_top_left_y+50));
		menu1.setColor(ccColor3B.ccBLUE);
		menu1.setVisible(false);

		menuitem2=CCMenuItemLabel.item("Robot Wins !",this, " ");
		menu2=CCMenu.menu(menuitem2);
		menu2.setColor(ccColor3B.ccBLUE);
		menu2.setVertexZ(50);
		menu2.setPosition(CGPoint.ccp(winSize.width/2,board_top_left_y+50));
		menu2.setVisible(false);

		addChild(menu1);
		addChild(menu2);

		_label_restart = CCLabel.makeLabel("Restart", "DroidSans", 32);
		_label_restart.setColor(ccColor3B.ccBLACK);
		_label_restart.setPosition(CGPoint.ccp(winSize.width/2,board_bottom_left_y-75));
		_label_restart.setVisible(false);
		addChild(_label_restart);

		Log.d(TAG,String.format("firstplayername = %s", firstPlayerName));

		if(firstPlayerName.equals("Robot")){
			make_robot_players_move();
		}
	}



	protected int count=0;
	protected int winner=0;//0 means no winner yet
	private float xBoxNum;
	private float yBoxNum;
	private boolean box_status;
	private float center_x;
	private float center_y;
	private String lastPlayedBy=null,currentPlayer=null;
	private boolean isGameOver=false;


	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{


		CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(),event.getY()));
		Log.d(TAG, String.format(" (%f,%f)", location.x, location.y));

		if((location.x>board_top_left_x && location.x<board_right_top_x) && (location.y>board_bottom_left_y && location.y< board_top_left_y)){

			yBoxNum=get_X_BoxNumber(location.x);
			xBoxNum=get_Y_BoxNumber(location.y);
			Log.d(TAG,String.format("y_BoxNum =  %f, x_BoxNum = %f",yBoxNum,xBoxNum));

			box_status=isBoxAlreadyOccupied(xBoxNum,yBoxNum);

			currentPlayer="P1";

			Log.d(TAG,String.format("currentPlayer = %s , lastPlayedBy =%s ;",currentPlayer,lastPlayedBy));
			if(yBoxNum!=-1  && xBoxNum!=-1 && _player1_instance_count<5 && _robot_player_instance_count<5 && winner==0 && box_status==false && !currentPlayer.equals(lastPlayedBy)){

				currentPlayer="P1";
				center_x=get_center_x_of_boxNumber(yBoxNum);
				center_y=get_center_y_of_boxNumber(xBoxNum);

				
				matrix[(int)xBoxNum][(int) yBoxNum]=1;
				displayMatrixValues(matrix);

				Player1_Instances p1_instance=new Player1_Instances(center_x,center_y, xBoxNum, yBoxNum);
				addChild(p1_instance.getPlayer());
				_player1_instances_arraylist.add(_player1_instance_count++,p1_instance);
				_label.setString("Robot's Turn");
				lastPlayedBy="P1";

				winner=check_for_winner();
				process_checked_winner_result(winner);

				if(winner==0 && _player1_instance_count<5)
					make_robot_players_move();

				if(winner==0 && _player1_instance_count>=5){
					_label.setString("It's a Draw !");
					_label_restart.setVisible(true);
					isGameOver=true;
				}

			
			}

		}
		else if(location.x>((winSize.width/2)-50) && location.x<((winSize.width/2)+50) && location.y>(board_bottom_left_y-75-25) && location.y<(board_bottom_left_y-75+25) && isGameOver==true){//winSize.width/2,board_bottom_left_y-75)

			loadMenuScreen();
		}
		return true;
	}

	public void loadMenuScreen(){
		CCDirector.sharedDirector().replaceScene(MenuLayer.scene());
	}

	protected int break_counter=0;
	public void make_robot_players_move(){


		float[] XY_boxNum=apply_AI_and_get_robots_move();

		plot_robot_players_move(XY_boxNum[0],XY_boxNum[1]);
	}

	private float[] apply_AI_and_get_robots_move() {

		float[] XY_boxNum=new float[2];
		//	XY_boxNum[0]=1; //0 th index represents x_BoxNum
		//	XY_boxNum[1]=2; //1st index represents y_BoxNum



		XY_boxNum=play_and_fill_third_empty_box_in_same_row_col();

		if(XY_boxNum[0]==-1 && XY_boxNum[1]==-1)
			XY_boxNum=play_and_block_third_empty_box_in_same_row_col();		

		if(XY_boxNum[0]==-1 && XY_boxNum[1]==-1)
			XY_boxNum=block_opponents_fork();
		
		if(XY_boxNum[0]==-1 && XY_boxNum[1]==-1)
			XY_boxNum=play_the_center();

		if(XY_boxNum[0]==-1 && XY_boxNum[1]==-1)
			XY_boxNum=play_center_of_row_col_if_opponent_in_corner();

		if(XY_boxNum[0]==-1 && XY_boxNum[1]==-1)
			XY_boxNum=play_in_a_corner_square();

		if(XY_boxNum[0]==-1 && XY_boxNum[1]==-1)
			XY_boxNum=play_in_middle_square_of_any_4_sides();

		return XY_boxNum;
	}

	public float[] block_opponents_fork(){
		float[] possible_move=new float[2];
		possible_move[0]=-1;
		possible_move[1]=-1;
		
		if(matrix[0][0]==2 && matrix[1][1]==1 && matrix[2][2]==1 && isBoxAlreadyOccupied(0,2)==false){
			possible_move[0]=0;
			possible_move[1]=2;
		}
		
		if(matrix[1][1]==2 && matrix[0][2]==1 && matrix[2][1]==1 & isBoxAlreadyOccupied(2,2)==false){
			possible_move[0]=2;
			possible_move[1]=2;
		}
		
		if(matrix[1][1]==2 && matrix[0][0]==1 && matrix[2][1]==1 & isBoxAlreadyOccupied(2,0)==false){
			possible_move[0]=2;
			possible_move[1]=0;
		}
		
		if(matrix[1][1]==2 && matrix[1][0]==1 && matrix[2][2]==1 & isBoxAlreadyOccupied(2,0)==false){
			possible_move[0]=2;
			possible_move[1]=0;
		}
		
		if(matrix[1][1]==2 && matrix[1][2]==1 && matrix[2][0]==1 & isBoxAlreadyOccupied(2,2)==false){
			possible_move[0]=2;
			possible_move[1]=2;
		}
		
		if(matrix[1][1]==2 && matrix[1][2]==1 && matrix[2][1]==1 && isBoxAlreadyOccupied(2,2)==false){
			possible_move[0]=2;
			possible_move[1]=2;
		}
		
		Log.d(TAG,String.format("in block opponent fork ... possible_move [] = %f , %f",possible_move[0],possible_move[1]));


		return possible_move;
	}

	public float[] play_and_block_third_empty_box_in_same_row_col(){

		float[] possible_move=new float[2];
		possible_move[0]=-1;
		possible_move[1]=-1;

		//row wise
		//check 1st row
		if(matrix[0][0]==1 && matrix[0][1]==1 && isBoxAlreadyOccupied(0,2)==false){	possible_move[0]=0;		possible_move[1]=2;		}
		else 
			if(matrix[0][1]==1 && matrix[0][2]==1  && isBoxAlreadyOccupied(0,0)==false){		possible_move[0]=0;		possible_move[1]=0;		}
			else 
				if(matrix[0][0]==1 && matrix[0][2]==1 && isBoxAlreadyOccupied(0,1)==false){		possible_move[0]=0;		possible_move[1]=1;		}

				else
					//check 2nd row
					if(matrix[1][0]==1 && matrix[1][1]==1  && isBoxAlreadyOccupied(1,2)==false){		possible_move[0]=1;		possible_move[1]=2;		}
					else 
						if(matrix[1][1]==1 && matrix[1][2]==1 && isBoxAlreadyOccupied(1,0)==false){		possible_move[0]=1;		possible_move[1]=0;		}
						else 
							if(matrix[1][0]==1 && matrix[1][2]==1  && isBoxAlreadyOccupied(1,1)==false){		possible_move[0]=1;		possible_move[1]=1;		}

							else
								//check 3rd row
								if(matrix[2][0]==1 && matrix[2][1]==1  && isBoxAlreadyOccupied(2,2)==false){		possible_move[0]=2;		possible_move[1]=2;		}
								else 
									if(matrix[2][1]==1 && matrix[2][2]==1 && isBoxAlreadyOccupied(2,0)==false){		possible_move[0]=2;		possible_move[1]=0;		}
									else 
										if(matrix[2][0]==1 && matrix[2][2]==1 && isBoxAlreadyOccupied(2,1)==false){		possible_move[0]=2;		possible_move[1]=1;		}

		//column wise
		//check 1st column
										else
											if(matrix[0][0]==1 && matrix[1][0]==1 && isBoxAlreadyOccupied(2,0)==false){		possible_move[0]=2;		possible_move[1]=0;		}
											else 
												if(matrix[1][0]==1 && matrix[2][0]==1 && isBoxAlreadyOccupied(0,0)==false){		possible_move[0]=0;		possible_move[1]=0;		}
												else 
													if(matrix[0][0]==1 && matrix[2][0]==1 && isBoxAlreadyOccupied(1,0)==false){		possible_move[0]=1;		possible_move[1]=0;		}

													else
														//check 2nd column
														if(matrix[0][1]==1 && matrix[1][1]==1 && isBoxAlreadyOccupied(2,1)==false){		possible_move[0]=2;		possible_move[1]=1;		}
														else 
															if(matrix[1][1]==1 && matrix[2][1]==1 && isBoxAlreadyOccupied(0,1)==false){		possible_move[0]=0;		possible_move[1]=1;		}
															else 
																if(matrix[0][1]==1 && matrix[2][1]==1 && isBoxAlreadyOccupied(1,1)==false){		possible_move[0]=1;		possible_move[1]=1;		}

																else
																	//check 3rd column
																	if(matrix[0][2]==1 && matrix[1][2]==1 && isBoxAlreadyOccupied(2,2)==false){		possible_move[0]=2;		possible_move[1]=2;		}
																	else 
																		if(matrix[1][2]==1 && matrix[2][2]==1 && isBoxAlreadyOccupied(0,2)==false){		possible_move[0]=0;		possible_move[1]=2;		}
																		else 
																			if(matrix[0][2]==1 && matrix[2][2]==1 && isBoxAlreadyOccupied(1,2)==false){		possible_move[0]=1;		possible_move[1]=2;		}


																			else
																				//check left to right diagonal
																				if(matrix[0][0]==1 && matrix[1][1]==1 && isBoxAlreadyOccupied(2,2)==false){		possible_move[0]=2;		possible_move[1]=2;		}
																				else 
																					if(matrix[1][1]==1 && matrix[2][2]==1 && isBoxAlreadyOccupied(0,0)==false){		possible_move[0]=0;		possible_move[1]=0;		}
																					else 
																						if(matrix[0][0]==1 && matrix[2][2]==1 && isBoxAlreadyOccupied(1,1)==false){		possible_move[0]=1;		possible_move[1]=1;		}


																						else
																							//check right to left diagonal
																							if(matrix[0][2]==1 && matrix[1][1]==1 && isBoxAlreadyOccupied(2,0)==false){		possible_move[0]=2;		possible_move[1]=0;		}
																							else 
																								if(matrix[1][1]==1 && matrix[2][0]==1 && isBoxAlreadyOccupied(0,2)==false){		possible_move[0]=0;		possible_move[1]=2;		}
																								else 
																									if(matrix[0][2]==1 && matrix[2][0]==1 && isBoxAlreadyOccupied(1,1)==false){		possible_move[0]=1;		possible_move[1]=1;		}



		Log.d(TAG,String.format("possibl_move [] = %f , %f",possible_move[0],possible_move[1]));



		return possible_move;
	}


	public float[] play_and_fill_third_empty_box_in_same_row_col(){

		float[] possible_move=new float[2];
		possible_move[0]=-1;
		possible_move[1]=-1;

		//row wise
		//check 1st row
		if(matrix[0][0]==2 && matrix[0][1]==2 && isBoxAlreadyOccupied(0,2)==false){	possible_move[0]=0;		possible_move[1]=2;		}
		else 
		if(matrix[0][1]==2 && matrix[0][2]==2  && isBoxAlreadyOccupied(0,0)==false){		possible_move[0]=0;		possible_move[1]=0;		}
		else 
		if(matrix[0][0]==2 && matrix[0][2]==2 && isBoxAlreadyOccupied(0,1)==false){		possible_move[0]=0;		possible_move[1]=1;		}

		else
		//check 2nd row
		if(matrix[1][0]==2 && matrix[1][1]==2  && isBoxAlreadyOccupied(1,2)==false){		possible_move[0]=1;		possible_move[1]=2;		}
		else 
		if(matrix[1][1]==2 && matrix[1][2]==2 && isBoxAlreadyOccupied(1,0)==false){		possible_move[0]=1;		possible_move[1]=0;		}
		else 
		if(matrix[1][0]==2 && matrix[1][2]==2  && isBoxAlreadyOccupied(1,1)==false){		possible_move[0]=1;		possible_move[1]=1;		}

		else
		//check 3rd row
		if(matrix[2][0]==2 && matrix[2][1]==2  && isBoxAlreadyOccupied(2,2)==false){		possible_move[0]=2;		possible_move[1]=2;		}
		else 
		if(matrix[2][1]==2 && matrix[2][2]==2 && isBoxAlreadyOccupied(2,0)==false){		possible_move[0]=2;		possible_move[1]=0;		}
		else 
		if(matrix[2][0]==2 && matrix[2][2]==2 && isBoxAlreadyOccupied(2,1)==false){		possible_move[0]=2;		possible_move[1]=1;		}

		//column wise
		//check 1st column
		else
		if(matrix[0][0]==2 && matrix[1][0]==2 && isBoxAlreadyOccupied(2,0)==false){		possible_move[0]=2;		possible_move[1]=0;		}
		else 
		if(matrix[1][0]==2 && matrix[2][0]==2 && isBoxAlreadyOccupied(0,0)==false){		possible_move[0]=0;		possible_move[1]=0;		}
		else 
		if(matrix[0][0]==2 && matrix[2][0]==2 && isBoxAlreadyOccupied(1,0)==false){		possible_move[0]=1;		possible_move[1]=0;		}

		else
		//check 2nd column
		if(matrix[0][1]==2 && matrix[1][1]==2 && isBoxAlreadyOccupied(2,1)==false){		possible_move[0]=2;		possible_move[1]=1;		}
		else 
		if(matrix[1][1]==2 && matrix[2][1]==2 && isBoxAlreadyOccupied(0,1)==false){		possible_move[0]=0;		possible_move[1]=1;		}
		else 
		if(matrix[0][1]==2 && matrix[2][1]==2 && isBoxAlreadyOccupied(1,1)==false){		possible_move[0]=1;		possible_move[1]=1;		}

		else
		//check 3rd column
		if(matrix[0][2]==2 && matrix[1][2]==2 && isBoxAlreadyOccupied(2,2)==false){		possible_move[0]=2;		possible_move[1]=2;		}
		else 
		if(matrix[1][2]==2 && matrix[2][2]==2 && isBoxAlreadyOccupied(0,2)==false){		possible_move[0]=0;		possible_move[1]=2;		}
		else 
		if(matrix[0][2]==2 && matrix[2][2]==2 && isBoxAlreadyOccupied(1,2)==false){		possible_move[0]=1;		possible_move[1]=2;		}


		else
		//check left to right diagonal
		if(matrix[0][0]==2 && matrix[1][1]==2 && isBoxAlreadyOccupied(2,2)==false){		possible_move[0]=2;		possible_move[1]=2;		}
		else 
		if(matrix[1][1]==2 && matrix[2][2]==2 && isBoxAlreadyOccupied(0,0)==false){		possible_move[0]=0;		possible_move[1]=0;		}
		else 
		if(matrix[0][0]==2 && matrix[2][2]==2 && isBoxAlreadyOccupied(1,1)==false){		possible_move[0]=1;		possible_move[1]=1;		}


		else
		//check right to left diagonal
		if(matrix[0][2]==2 && matrix[1][1]==2 && isBoxAlreadyOccupied(2,0)==false){		possible_move[0]=2;		possible_move[1]=0;		}
		else 
		if(matrix[1][1]==2 && matrix[2][0]==2 && isBoxAlreadyOccupied(0,2)==false){		possible_move[0]=0;		possible_move[1]=2;		}
		else 
		if(matrix[0][2]==2 && matrix[2][0]==2 && isBoxAlreadyOccupied(1,1)==false){		possible_move[0]=1;		possible_move[1]=1;		}



		Log.d(TAG,String.format("possibl_move [] = %f , %f",possible_move[0],possible_move[1]));

		return possible_move;

	}

	public float[] play_the_center(){

		float[] possible_move=new float[2];
		possible_move[0]=-1;
		possible_move[1]=-1;

		if(matrix[1][1]==0){
			possible_move[0]=1;
			possible_move[1]=1;
		}

		Log.d(TAG,String.format("possibl_move [] = %f , %f",possible_move[0],possible_move[1]));

		return possible_move;
	}
	public float[] play_center_of_row_col_if_opponent_in_corner(){

		float[] possible_move=new float[2];
		possible_move[0]=-1;
		possible_move[1]=-1;

		//1 represents player1's position

		//check for top left corner
		//check an empty cell in row and column
		if((matrix[0][0]==1)  &&	(matrix[0][1]==0 )){
			possible_move[0]=0;
			possible_move[1]=1;
		}
		else 
			if ((matrix[0][0]==1)  && (matrix[1][0]==0)){
				possible_move[0]=1;
				possible_move[1]=0;
			}

		//check for right top corner
		else 
			if((matrix[0][2]==1) &&	(matrix[0][1]==0)){
				possible_move[0]=0;
				possible_move[1]=1;
			}
		else 
			if((matrix[0][2]==1) && (matrix[1][2]==0)){
				possible_move[0]=1;
				possible_move[1]=2;
			}


		//check for bottom left corner
		else 
			if((matrix[2][0]==1) &&	(matrix[1][0]==0)){
				possible_move[0]=1;
				possible_move[1]=0;
			}
		else 
			if ((matrix[2][0]==1) && (matrix[2][1]==0)){
				possible_move[0]=2;
				possible_move[1]=1;
			}


		//check for right bottom corner
		else
			if((matrix[2][2]==1) &&	(matrix[1][2]==0)){
				possible_move[0]=1;
				possible_move[1]=2;
			}
		else 
			if ((matrix[2][2]==1) && (matrix[2][1]==0)){
				possible_move[0]=2;
				possible_move[1]=1;
			}


		Log.d(TAG,String.format("possibl_move [] = %f , %f",possible_move[0],possible_move[1]));
		return possible_move;
	}

	public float[] play_in_a_corner_square(){

		float[] possible_move=new float[2];
		possible_move[0]=-1;
		possible_move[1]=-1;

		if(matrix[0][0]==0){
			possible_move[0]=0;
			possible_move[1]=0;			
		}
		else if(matrix[0][2]==0){
			possible_move[0]=0;
			possible_move[1]=2;
		}
		else if(matrix[2][0]==0){
			possible_move[0]=2;
			possible_move[1]=0;			
		}
		else if(matrix[2][2]==0){
			possible_move[0]=2;
			possible_move[1]=2;
		}
		Log.d(TAG,String.format("possibl_move [] = %f , %f",possible_move[0],possible_move[1]));
		return possible_move;
	}

	public float[] play_in_middle_square_of_any_4_sides(){

		float[] possible_move=new float[2];
		possible_move[0]=-1;
		possible_move[1]=-1;

		if(matrix[0][1]==0){
			possible_move[0]=0;
			possible_move[1]=1;
		}
		else if(matrix[1][0]==0){
			possible_move[0]=1;
			possible_move[1]=0;
		}
		else if(matrix[2][1]==0){
			possible_move[0]=2;
			possible_move[1]=1;
		}
		else if(matrix[1][2]==0){
			possible_move[0]=1;
			possible_move[1]=2;
		}

		Log.d(TAG,String.format("possibl_move [] = %f , %f",possible_move[0],possible_move[1]));
		return possible_move;
	} 

	public void plot_robot_players_move(float xBoxNum,float yBoxNum){

		box_status=isBoxAlreadyOccupied(xBoxNum,yBoxNum);

		currentPlayer="Robot";
		if(_player1_instance_count<5 && _robot_player_instance_count<5 && winner==0 && box_status==false ){

			matrix[(int)xBoxNum][(int) yBoxNum]=2;
			displayMatrixValues(matrix);
			center_x=get_center_x_of_boxNumber(yBoxNum);
			center_y=get_center_y_of_boxNumber(xBoxNum);
			Robot_Player rp_instance=new Robot_Player(center_x,center_y,xBoxNum,yBoxNum);
			addChild(rp_instance.getRobot_player());
			_robot_player_instances_arraylist.add(_robot_player_instance_count++,rp_instance);					
			_label.setString("Player1's (Your) Turn");
			lastPlayedBy="Robot";

			winner=check_for_winner();
			process_checked_winner_result(winner);

			if(winner==0 && _robot_player_instance_count>=5){
				_label.setString("It's a Draw !");
				_label_restart.setVisible(true);
				isGameOver=true;
			}
			break_counter++;;
		}
	}


	public void process_checked_winner_result(int winner){

		if(winner==1){	// player 1 (you) win
			menu1.setVisible(true);
			_label.setString(" ");
			Log.d(TAG,String.format("winner is Player1 (You) %d",winner));
			isGameOver=true;
			_label_restart.setVisible(true);


		}else if(winner==2){ //robot wins
			menu2.setVisible(true);
			_label.setString(" ");
			Log.d(TAG,String.format("winner is Robot %d",winner));
			isGameOver=true;
			_label_restart.setVisible(true);


		}
		else{
			Log.d(TAG,String.format("No Winner Yet"));
		}

	}

	boolean isBoxAlreadyOccupied(float x_boxNum, float y_boxNum){
		if(matrix[(int)x_boxNum][(int) y_boxNum]==0)
			return false;
		else
			return true;

	}


	public int check_for_winner(){

		if(		(matrix[0][0]==1 && matrix[0][1]==1 && matrix[0][2]==1) || 
				(matrix[1][0]==1 && matrix[1][1]==1 && matrix[1][2]==1) ||
				(matrix[2][0]==1 && matrix[2][1]==1 && matrix[2][2]==1)

				|| 

				(matrix[0][0] ==1 && matrix[1][0]==1 && matrix[2][0]==1) ||
				(matrix[0][1] ==1 && matrix[1][1]==1 && matrix[2][1]==1)  ||
				(matrix[0][2] ==1 && matrix[1][2]==1 && matrix[2][2]==1)

				||
				(matrix[0][0]==1 && matrix[1][1]==1 && matrix[2][2]==1)
				||
				(matrix[0][2]==1 && matrix[1][1]==1 && matrix[2][0]==1)
				){
			return 1;
		}

		if(		(matrix[0][0]==2 && matrix[0][1]==2 && matrix[0][2]==2) || 
				(matrix[1][0]==2 && matrix[1][1]==2 && matrix[1][2]==2) ||
				(matrix[2][0]==2 && matrix[2][1]==2 && matrix[2][2]==2)

				|| 

				(matrix[0][0] ==2 && matrix[1][0]==2 && matrix[2][0]==2) ||
				(matrix[0][1] ==2 && matrix[1][1]==2 && matrix[2][1]==2)  ||
				(matrix[0][2] ==2 && matrix[1][2]==2 && matrix[2][2]==2)

				||
				(matrix[0][0]==2 && matrix[1][1]==2 && matrix[2][2]==2)
				||
				(matrix[0][2]==2 && matrix[1][1]==2 && matrix[2][0]==2)

				){
			return 2;
		}
		return 0 ;// 0 means no winner yet !
	}

	float get_center_x_of_boxNumber(float x_boxNum){

		float center_x=board_top_left_x+(x_boxNum+1)*50-25;
		return center_x;
	}

	float get_center_y_of_boxNumber(float y_boxNum){
		float center_y=board_top_left_y-((y_boxNum+1)*50 -(25));
		return center_y;
	}

	public void displayMatrixValues(int[][] matrix){
		int i=0,j=0;
		String str=""; 
		for(i=0;i<3;i++){

			for(j=0;j<3;j++){
				str=str+" "+matrix[i][j];
			}
			str=str+"\n";
		}
		Log.d(TAG,String.format(" %s ",str));

	}
	public float  get_X_BoxNumber(float x){


		float x_boxNum = -1;

		if(x>=board_top_left_x && x<=(board_top_left_x+50)){
			x_boxNum=0;			
		}
		if(x>board_top_left_x+50 && x<=board_top_left_x+100){
			x_boxNum=1;
		}
		if(x>board_top_left_x+100 && x<=board_right_top_x){
			x_boxNum=2;
		}
		//	 Log.d(TAG,String.format("x_BoxNum =  %f",x_boxNum));
		return x_boxNum;
	}


	public  float get_Y_BoxNumber(float y){


		float y_boxNum=-1;

		if(y>=board_bottom_left_y && y<=(board_bottom_left_y+50)){
			y_boxNum=2;			
		}
		if(y>board_bottom_left_y+50 && y<=board_bottom_left_y+100){
			y_boxNum=1;
		}
		if(y>board_bottom_left_y+100 && y<=board_top_left_y){
			y_boxNum=0;
		}

		// Log.d(TAG,String.format("y_BoxNum = %f",y_boxNum));
		return y_boxNum;
	}

}
