
package Team4450.Robot11;

import Team4450.Lib.*;
import Team4450.Robot11.Devices;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jdk.nashorn.internal.ir.Block;

public class Autonomous
{
	private final Robot robot; // I removed the final part so that I could reference robot
	private final int	program = (int) SmartDashboard.getNumber("AutoProgramSelect",0);
	private CubeIntake Block;
	private WinchToogle winch;
	
	

	Autonomous(Robot robot)
	{
		Util.consoleLog();
		
		this.robot = robot;	
		
		
	}

	public void dispose()
	{
		Util.consoleLog();
	}
	
	private boolean isAutoActive()
	{
		return robot.isEnabled() && robot.isAutonomous();
	}

	public void execute()
	{
		Util.consoleLog("Alliance=%s, Location=%d, Program=%d, FMS=%b, msg=%s", robot.alliance.name(), robot.location, program, 
				Devices.ds.isFMSAttached(), robot.gameMessage);
		LCD.printLine(2, "Alliance=%s, Location=%d, FMS=%b, Program=%d, msg=%s", robot.alliance.name(), robot.location, 
				Devices.ds.isFMSAttached(), program, robot.gameMessage);

		Devices.robotDrive.setSafetyEnabled(false);

		//TODO Encoder likely used, so just commenting out.
		// Initialize encoder.
		Devices.SRXEncoder.reset();
		Devices.WinchEncoder.reset();
        
		//TODO NavX likely used, so just commenting out.
        // Set gyro/NavX to heading 0.
        //robot.gyro.reset();
		Devices.navx.resetYaw();
		
        // Wait to start motors so gyro will be zero before first movement.
        Timer.delay(.50);

		switch (program) 
		{
			case 0:	
				SideAutoStraight();// Auto Program where we just go straight from the sides.
				break;
			
			case 1:
				CenterAuto(false);//Auto Program which is used when starting from the center and we ARENT scoring
				break;
				
			case 2:
				CenterAuto(true);// Auto Program which is used when starting from the center and we are scoring
				break;
				
			case 3:
				LeftSide(); // Auto Program which is used for the left side
				break;
				
			case 4:
				RightSide();// Auto Program which is used for the right side
				break;
				
				
		}
		
		Util.consoleLog("end");
	}
	
	

	//TODO May likely be used, will need modification to work.
	/*
	// Auto drive in set direction and power for specified encoder count. Stops
	// with or without brakes on CAN bus drive system. Uses gyro/NavX to go straight.
	*/
	private void autoDrive(double power, int encoderCounts, boolean enableBrakes)
	{
		/*int		angle;
		double	gain = .03;
		
		Util.consoleLog("pwr=%.2f, count=%d, brakes=%b", power, encoderCounts, enableBrakes);

		if (robot.isComp) Devices.SetCANTalonBrakeMode(enableBrakes);

		Devices.encoder.reset();
		Devices.navx.resetYaw();
		
		while (isAutoActive() && Math.abs(Devices.encoder.get()) < encoderCounts) 
		{
			LCD.printLine(4, "encoder=%d", Devices.encoder.get());
			
			// Angle is negative if robot veering left, positive if veering right when going forward.
			// It is opposite when going backward. Note that for this robot, - power means forward and
			// + power means backward.
			
			//angle = (int) robot.gyro.getAngle();
			angle = (int) Devices.navx.getYaw();

			LCD.printLine(5, "angle=%d", angle);
			
			// Invert angle for backwards.
			
			if (power > 0) angle = -angle;
			
			//Util.consoleLog("angle=%d", angle);
			
			// Note we invert sign on the angle because we want the robot to turn in the opposite
			// direction than it is currently going to correct it. So a + angle says robot is veering
			// right so we set the turn value to - because - is a turn left which corrects our right
			// drift.
			
			Devices.robotDrive.tankDrive(power, -angle * gain);
			
			Timer.delay(.020);
		}

		Devices.robotDrive.tankDrive(0, 0, true);				
		
		Util.consoleLog("end: actual count=%d", Math.abs(Devices.encoder.get()));*/
	}
	
	// Auto rotate left or right the specified angle. Left/right from robots forward view.
	// Turn right, power is -
	// Turn left, power is +
	// angle of rotation is always +.

	private void autoRotate(double power, int angle)
	{
		/*Util.consoleLog("pwr=%.2f  angle=%d", power, angle);
		
		Devices.navx.resetYaw();
		
		Devices.robotDrive.tankDrive(power, -power);

		while (isAutoActive() && Math.abs((int) Devices.navx.getYaw()) < angle) {Timer.delay(.020);} 
		
		Devices.robotDrive.tankDrive(0, 0);*/
	}
	
	
	double rotate;//Used only for 90 degree turns. Power when turning.
	int angled;//Used only for 90 degree turns. Angle to turn to.
	
	public void SideAutoStraight(){
		
		autoDrive(.50, 1200, true); //TODO test
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
	
	//The boolean below compares the robot position in relation to the switch (hence the name PosiRela)
	//It tells whether or not the side of the switch we are scoring on is in line with the robot's starting position
	
	public boolean PosiRela(boolean Leftside){ //The boolean Leftside tells the position of the robot. 
												//if true the robot is on the left, if false it is on the right.
	boolean stuff = false; //I should find a better name but this boolean indicates whether or not 
					//the robot's starting position is in line with the corresponding side on the scale
	char firstLetter = robot.gameMessage.charAt(0);
	//The 'if' statement below is checking to see if the robot is in line with the switch. In line means that if the robot is left, the switch would be left as well 
	if ((firstLetter == 'L' && Leftside == true) || (firstLetter == 'R' && Leftside == false)){
	
	stuff = true;//If the robot is in line, it is true
	}
	else if ((firstLetter == 'L' && Leftside == false) || (firstLetter == 'R' && Leftside == true)){
		stuff = false;//else it is false
	}
	
	return stuff;
	}
	
	//This boolean has a similar setup to the previous one. It compares the robot position in relation to the scale (hence the name PosiRela2)
	//It tells whether or not the side of the switch we are scoring on is in line with the robot's starting position
	public boolean PosiRela2(boolean Leftside) {
		boolean stuff = false;
		char secondLetter = robot.gameMessage.charAt(1);
		if((secondLetter == 'L' && Leftside == true) || (secondLetter == 'R' && Leftside == false)) {
			stuff = true;
		}
		else if((secondLetter == 'L' && Leftside == false) || (secondLetter == 'R' && Leftside == true)) {
			
			stuff = false;
		}
		
		return stuff;
	}
	
	//The side autonomous is very similar for both the left and right starting positions. So the two position-relation booleans above can be used
	//The main difference is the turns in the two autos... Which can be used to our advantage ;)
	
	public void SideAutonomous(boolean LeftSide){
		
	if (PosiRela(LeftSide) && !PosiRela2(LeftSide)){
		
		autoDrive(.50, 1200, true); // Make sure to test it. The robot is driving on the straight away to the switch
				if(LeftSide){ //if the robot is on the left side
					autoRotate(rotate, angled);//Make sure to test this. rotate towards the side of the switch
				}
				else if(!LeftSide){//if the robot is on the right side
					autoRotate(rotate, -angled);//Make sure to test this. rotate towards the side of the switch
		Block.deposit();
		autoDrive(.50, 1200, true);//Make sure to test. Drive towards the switch in order to deposit it
		Block.stopCubeIntake(); //So this will be available in a separate class that controls the pneumatics for the Robot Arm. More on that later
		Util.consoleLog("Auto Complete");
	}
	
	//This if statement below details what to do if the robot is in line with the scale on either side but NOT the switch
	
	else if(PosiRela2(LeftSide) && !PosiRela(LeftSide)){
		autoDrive(.50, 1200, true);//Make sure to test this. BTW This is how much the robot should go on the straightaway
				if(LeftSide){ //If the robot is on the leftside
					autoRotate(rotate, angled); //Make sure to test. rotate towards the scale
		}
				else if(!LeftSide){//If the robot is on the right side
		autoRotate(rotate, -angled);//Make sure to test. Rotate towards the scale
		}
		Block.deposit();
		autoDrive(.5, 1200, true); //Make sure to test. Drive towards the scale to score.
		Block.stopCubeIntake(); //So this will be available in a separate class that controls the pneumatics for the Robot Arm. More on that later
		Util.consoleLog("Auto Complete");
	 }
	
	//The third if statement details what should be done if the robot is in line with the scale and the switch
	else if (PosiRela2(LeftSide) && PosiRela(LeftSide)){
		autoDrive(.50, 1200, true); // Make sure to test it and change it accordingly it should go to the switch
		if(LeftSide){
		autoRotate(rotate, angled);//Make sure to test. rotate towards the side of the switch
		}
		else if(!LeftSide){
		autoRotate(rotate, -angled);//Make sure to test. rotate towards the side of the switch
		}
		
		Block.deposit();
		autoDrive(.50, 1200, true);//Make sure to test.Drive towards the switch in order to deposit it
		Block.stopCubeIntake(); //So this will be available in a separate class that controls the pneumatics for the Robot Arm. More on that later
		Util.consoleLog("Auto Complete");
	}
	else if (!PosiRela2(LeftSide) && !PosiRela(LeftSide)) {
		autoDrive(.5, 1200, true);//Make sure to test. Drive until aligned with the platform area
		if (LeftSide) {
			autoRotate(rotate, angled); //Make sure to test this. BTW Turn right 90 degrees so that the robot can go in the platform area
		}
		
		else if(!LeftSide) {
			autoRotate(rotate, -angled); //TODO test this. Turn counter clockwise towards the platform area
		}
			autoDrive(.5, 3000, true); //TODO test this. The robot drives a longer distance cause it is across its respective side
				
		Util.consoleLog("Doomsday Scenario...");

	}
	}
	}
	
	public void CenterAuto(boolean isScoring) { //The boolean is scoring checks if we are going to score a block or not
		char firstLetter = robot.gameMessage.charAt(0);
		if (isScoring == false) {
			autoDrive(.5, 1200, true);//Make sure to test. Drive forward some
			autoRotate(.85, 45);//Make sure to test. Turn 45 degrees to the right
			autoDrive(.5, 1200, true);//Make sure to test. Go forward and break the line
			autoDrive(0,0 ,true);// Make sure to test. Stop the bot.
			Util.consoleLog("Stopping the robot");
		}
		
		else if(isScoring == true) {
			autoDrive(.5, 1200, true); //Make sure to test. Drive forward towards the center goal
			Util.consoleLog("Driving forward towards the switch");
			if(firstLetter == 'L') {
				autoRotate(.5, 90);//Make sure to test. if the score plate on the switch is on the left turn that much
			Util.consoleLog("Rotate towards the left to the switch");
			}
			else if(firstLetter == 'R') {
				autoRotate(.5, -90); //Make sure to test. if the score plate is on the right, then turn right
			Util.consoleLog("Rotate towards the right to the switch");
			}
			autoDrive(.5, 1200, true); //Make sure to test. Drive forward so that the sides of the robot align with the switch
			Util.consoleLog("Drive forward towards the switch");
			if(firstLetter == 'L') {
				autoRotate(.5, 90);//Make sure to test. Turn towards the switch
				Util.consoleLog("Rotate clockwise to position towards switch");
			}
			else if(firstLetter == 'R') {
				autoRotate(.5, -90);//Make sure to test. Turn towards the switch
				Util.consoleLog("Rotate counterclockwise to position towards switch");
				
			}
			
			Block.deposit();
			autoDrive(.5, 1200, true);//Make sure to test. Drive towards the scoring area
			Block.stopCubeIntake();
			
			Util.consoleLog("Score the block");
			
		}
	}
	
	
}