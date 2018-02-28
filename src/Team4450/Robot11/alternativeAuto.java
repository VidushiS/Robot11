package Team4450.Robot11;

import Team4450.Lib.Util;

//Make sure to put in the scale again, we had to take it out for the first competition
public class alternativeAuto {
	
/*	double rotate = 0.45;//Used only for 90 degree turns. Power when turning.
	int angled = 90;//Used only for 90 degree turns. Angle to turn to.
	
	int SwitchEncoderUp; //Used only for when the lift goes up when scoring on the switch
	int SwitchEncoderDown; //Used only for when the lift goes down when scoring on the switch
	int ScaleEncoderUp; //Used only for when the lift goes up when scoring on the scale
	int ScaleEncoderDown; //Used only for when the lift goes down when scoring on the scale
	public void SideAutoStraight(){
		//E1 2490; E2 1680 
		autoDrive(-.50, 2490, true); //TODO test
		Util.consoleLog("Driving forward to break the line from the sides");

	}
	
	//This method is used to direct the actions the robot should take from the left side. Follow the methods and the parameters to understand
	public void LeftSide(){ 
		SideAutonomous(true);
	}
	
	//This method is used to direct the actions the robot should take from the right side. Follow the methods and the parameters to understand
	public void RightSide(){
		SideAutonomous(false);
	}
	
	public boolean PosiRela(boolean Leftside){ 
	boolean startPos = false; 
	char firstLetter = robot.gameMessage.charAt(0);
	if ((firstLetter == 'L' && Leftside == true) || (firstLetter == 'R' && Leftside == false)){
	
	startPos = true;//If the robot is in line, it is true
	}
	else if ((firstLetter == 'L' && Leftside == false) || (firstLetter == 'R' && Leftside == true)){
		startPos = false;//else it is false
	}
	
	return startPos;
	}
	
		public boolean PosiRela2(boolean Leftside) {
		boolean startPos = false;
		char secondLetter = robot.gameMessage.charAt(1);
		if((secondLetter == 'L' && Leftside == true) || (secondLetter == 'R' && Leftside == false)) {
			startPos = true;
		}
		else if((secondLetter == 'L' && Leftside == false) || (secondLetter == 'R' && Leftside == true)) {
			
			startPos = false;
		}
		
		return startPos;
	}
	
	//The side autonomous is very similar for both the left and right starting positions. So the two position-relation booleans above can be used
	//The main difference is the turns in the two autos... Which can be used to our advantage ;)
	
	public void SideAutonomous(boolean LeftSide){
		
		//E1 3180; E2 2050 this is on the way to the switch
		//E1 380 ;E2 230 when scoring
	if (PosiRela(LeftSide) && !PosiRela2(LeftSide){
		
		autoDrive(-.50, 3180, true); // Make sure to test it. The robot is driving on the straight away to the switch
				if(LeftSide){
					autoRotate(-rotate, angled);//Make sure to test this. rotate towards the side of the switch
				}
				else if(!LeftSide){
					autoRotate(rotate, angled);//Make sure to test this. rotate towards the side of the switch
		autoDrive(-.30, 320, true);
				//	winch.WinchScoreSwitch(SwitchEncoderUp, SwitchEncoderDown); //TODO MAKE SURE TO TEST THIS
	}
	}
	//This if statement below details what to do if the robot is in line with the scale on either side but NOT the switch
	
	else if(!PosiRela(LeftSide) && PosiRela2(LeftSide){
		
		//E1 4600; E2 2804 on the way the platform zone
				//E1 1470; E2 960 going in the platform 
				autoDrive(-.5, 4600, true);//Make sure to test. Drive until aligned with the platform area
				if (LeftSide) {
					autoRotate(-rotate, angled); //Make sure to test this. BTW Turn right 90 degrees so that the robot can go in the platform area
				}
				else if(!LeftSide) {
					autoRotate(rotate, angled); //TODO test this. Turn counter clockwise towards the platform area
				}
					autoDrive(-.5, 1470, true); //TODO test this. The robot drives a longer distance cause it is across its respective sid
	 }
				/*autoDrive(.50, 1200, true);//Make sure to test this. BTW This is how much the robot should go on the straightaway
				if(LeftSide){ //If the robot is on the leftside
					autoRotate(rotate, angled); //Make sure to test. rotate towards the scale
		}
				else if(!LeftSide){//If the robot is on the right side
		autoRotate(rotate, -angled);//Make sure to test. Rotate towards the scale
		}
		winch.WinchScoreScale(ScaleEncoderUp, ScaleEncoderDown); //TODO make sure to test*/
	//The third if statement details what should be done if the robot is in line with the scale and the switch
	/*else if (PosiRela2(LeftSide) && PosiRela(LeftSide)){
		autoDrive(.50, 1200, true); // Make sure to test it and change it accordingly it should go to the switch
		if(LeftSide){
		autoRotate(rotate, angled);//Make sure to test. rotate towards the side of the switch
		}
		else if(!LeftSide){
		autoRotate(-rotate, angled);//Make sure to test. rotate towards the side of the switch
		}
		
	//	winch.WinchScoreSwitch(SwitchEncoderUp, SwitchEncoderDown);
	}
	else if (!PosiRela2(LeftSide) && !PosiRela(LeftSide)) {
	autoDrive(-.5, 4600, true);//Make sure to test. Drive until aligned with the platform area
				if (LeftSide) {
					autoRotate(-rotate, angled); //Make sure to test this. BTW Turn right 90 degrees so that the robot can go in the platform area
				}
				else if(!LeftSide) {
					autoRotate(rotate, angled); //TODO test this. Turn counter clockwise towards the platform area
				}
					autoDrive(-.5, 1470, true); //TODO test this. The robot drives a longer distance cause it is across its respective sid
	 }
			}
	}*/
	
	/*}
	
	public void CenterAuto(boolean isScoring) { //The boolean is scoring checks if we are going to score a block or not
		//E1 1970; E2 1250
		//50 percent power maybe more cause more stuff will be added to the bot
		char firstLetter = robot.gameMessage.charAt(0);
		if (isScoring == false) {
			autoDrive(-.3, 1970, true);//Make sure to test. Drive forward some
			
		}
		
		else if(isScoring == true) {
			
			//E1 924; E2 593 
			autoDrive(-.5, 1200, true); //Make sure to test. Drive forward towards the center goal
			if(firstLetter == 'L') {
				autoRotate(rotate, angled);//Make sure to test. if the score plate on the switch is on the left turn that much
			}
			else if(firstLetter == 'R') {
				autoRotate(-rotate, angled); //Make sure to test. if the score plate is on the right, then turn right
			}
			//E1 1028/ 1329; E2 670/ 857
			autoDrive(-.5, 1330, true); //Make sure to test. Drive forward so that the sides of the robot align with the switch
			if(firstLetter == 'L') {
				autoRotate(-rotate, angled);//Make sure to test. Turn towards the switch
			}
			else if(firstLetter == 'R') {
				autoRotate(rotate, angled);//Make sure to test. Turn towards the switch
			}
			//E1 1118 ;E2 720
			autoDrive(-.5, 1118, true);
			//winch.WinchScoreSwitch(SwitchEncoderUp, SwitchEncoderDown);
		}
	}*/
	


}
