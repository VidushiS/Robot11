
package Team4450.Robot11;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

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
		Devices.SRXEncoder2.reset();
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
				//When we have absolutely no auto program.
				break;
			
			case 1:	
				SideAutoStraight();// Auto Program where we just go straight from the sides.
				break;
			
			case 2:
				CenterAuto(false);//Auto Program which is used when starting from the center and we ARENT scoring
				break;
				
			case 3:
				CenterAuto(true);// Auto Program which is used when starting from the center and we are scoring
				break;
				
			case 4:
				LeftSide(); // Auto Program which is used for the left side
				break;
				
			case 5:
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
	public void autoDrive(double power, int encoderCounts, boolean enableBrakes)
	{
		int		angle;
		double	gain = .05;
		
		Util.consoleLog("pwr=%.2f, count=%d, brakes=%b", power, encoderCounts, enableBrakes);

		if (robot.isComp) Devices.SetCANTalonBrakeMode(enableBrakes);

		Devices.SRXEncoder.reset();
		//Devices.SRXEncoder2.reset();
		Devices.navx.resetYaw();
		
		while (isAutoActive() && Math.abs(Devices.SRXEncoder.get()) < encoderCounts) 
		{
			LCD.printLine(4, "Wheel encoder 1 =%d", Devices.SRXEncoder.get());
		//	LCD.printLine(6, "Wheel encoder 2 =%d", Devices.SRXEncoder2.get());
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
			
			Devices.robotDrive.curvatureDrive(power, angle * gain, false);
			
			Timer.delay(.020);
		}

		Devices.robotDrive.tankDrive(0, 0, true);				
		
		Util.consoleLog("end: actual count of Encoder 1=%d", Math.abs(Devices.SRXEncoder.get()));
		//Util.consoleLog("end: actual count of Encoder 2=%d", Math.abs(Devices.SRXEncoder2.get()));
	}
	
	// Auto rotate left or right the specified angle. Left/right from robots forward view.
	// Turn right, power is -
	// Turn left, power is +
	// angle of rotation is always +.

	private void autoRotate(double power, int angle)
	{
		Util.consoleLog("pwr=%.2f  angle=%d", power, angle);
		
		Devices.navx.resetYaw();
		
		Devices.robotDrive.tankDrive(power, -power);

		while (isAutoActive() && Math.abs((int) Devices.navx.getYaw()) < angle) {Timer.delay(.020);} 
		
		Devices.robotDrive.tankDrive(0, 0);
	}
	
	
	double rotate = 0.45;//Used only for 90 degree turns. Power when turning.
	int angled = 90;//Used only for 90 degree turns. Angle to turn to.
	
	int SwitchEncoderUp; //Used only for when the lift goes up when scoring on the switch
	int SwitchEncoderDown; //Used only for when the lift goes down when scoring on the switch
	//int ScaleEncoderUp; //Used only for when the lift goes up when scoring on the scale
	//int ScaleEncoderDown; //Used only for when the lift goes down when scoring on the scale
	
	public void SideAutoStraight(){
		//E1 2490; E2 1680 
		autoDrive(-.50, 2490, true); //TODO test
		//autoDrive(-.5, 1606, true);
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
	
	public void SideAutonomous(boolean LeftSide){
		
		//E1 3180; E2 2050 this is on the way to the switch
		//E1 380 ;E2 230 when scoring
	if (PosiRela(LeftSide)){
		
		autoDrive(-.50, 3180, true); 
		//autoDrive(-.5, 2050, true);
				if(LeftSide){
					autoRotate(-rotate, angled);
				}
				else if(!LeftSide){
					autoRotate(rotate, angled);
		autoDrive(-.30, 320, true);
		//autoDrive(-.3, 205, true);
		//winch.WinchScoreSwitch(SwitchEncoderUp, SwitchEncoderDown); 
	}
	}
	
	else if(!PosiRela(LeftSide)){
		
		//E1 4600; E2 2804 on the way the platform zone
		//E1 1470; E2 960 going in the platform 
				autoDrive(-.5, 4600, true);
				//autoDrive(-.5, 2970, true);
				if (LeftSide) {
					autoRotate(-rotate, angled); 
				}
				else if(!LeftSide) {
					autoRotate(rotate, angled); 
				}
				autoDrive(-.5, 1470, true); 
				//autoDrive(-.5, 950, true);
	}

	}
	
	public void CenterAuto(boolean isScoring) { //The boolean is scoring checks if we are going to score a block or not
		//E1 1970; E2 1250
		//50 percent power maybe more cause more stuff will be added to the bot
		char firstLetter = robot.gameMessage.charAt(0);
		if (isScoring == false) {
			autoDrive(-.3, 1970, true);//Make sure to test. Drive forward some
			//autoDrive(-.3, 1270, true);
		}
		
		else if(isScoring == true) {
			
			//E1 924; E2 593 
			autoDrive(-.5, 1200, true); //Make sure to test. Drive forward towards the center goal
			//autoDrive(-.5, 774, true);
			if(firstLetter == 'L') {
				autoRotate(rotate, angled);//Make sure to test. if the score plate on the switch is on the left turn that much
			}
			else if(firstLetter == 'R') {
				autoRotate(-rotate, angled); //Make sure to test. if the score plate is on the right, then turn right
			}
			//E1 1028/ 1329; E2 670/ 857
			autoDrive(-.5, 1330, true); //Make sure to test. Drive forward so that the sides of the robot align with the switch
			//autoDrive(-.5, 858, true);
			if(firstLetter == 'L') {
				autoRotate(-rotate, angled);//Make sure to test. Turn towards the switch
			}
			else if(firstLetter == 'R') {
				autoRotate(rotate, angled);//Make sure to test. Turn towards the switch
			}
			//E1 1118 ;E2 720
			autoDrive(-.5, 1118, true);
			//autoDrive(-.5, 721, true);
			//winch.WinchScoreSwitch(SwitchEncoderUp, SwitchEncoderDown);
		}
	}
	
	
}