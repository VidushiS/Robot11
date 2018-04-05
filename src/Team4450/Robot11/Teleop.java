
package Team4450.Robot11;

import java.lang.Math;

import Team4450.Lib.*;
import Team4450.Lib.JoyStick.*;
import Team4450.Lib.LaunchPad.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class Teleop
{
	private final Robot 		robot;
	public  JoyStick			rightStick, leftStick, utilityStick;
	public  LaunchPad			launchPad;
	private boolean				autoTarget, invertDrive, altDriveMode;
	private Vision				vision;
	private RobotSpeedShifter   speedShifter;
	private CubeIntake          Block;
	private WinchToogle			Winch;
	

	// Constructor.

	Teleop(Robot robot)
	{
		Util.consoleLog();

		this.robot = robot;
		Winch = new WinchToogle(robot);
		Block = new CubeIntake(robot);
		speedShifter = new RobotSpeedShifter(robot);
		
		LCD.printLine(4, "Wheel Encoder Counts", Devices.SRXEncoder.get());
		LCD.printLine(5, "Wheel Encoder Counts", Devices.SRXEncoder2.get());
		if(robot.isOperatorControl())speedShifter.slowSpeed();
		
		vision = Vision.getInstance(robot);
	}

	// Free all objects that need it.

	void dispose()
	{
		Util.consoleLog("We have disposed the teleop");

		if (leftStick != null) leftStick.dispose();
		if (rightStick != null) rightStick.dispose();
		if (utilityStick != null) utilityStick.dispose();
		if (launchPad != null) launchPad.dispose();
	}
	
	
	void OperatorControl()
	{
		double	rightY = 0, leftY = 0, utilX = 0, rightX = 0, leftX = 0, utilY = 0;
		double	gain = .05;
		boolean steeringAssistMode = false;
		int		angle;
					
		// Motor safety turned off during initialization.
		Devices.robotDrive.setSafetyEnabled(false);
		Devices.SetCANTalonBrakeMode(false);
		Util.consoleLog();

		LCD.printLine(1, "Mode: OperatorControl");
		LCD.printLine(2, "All=%s, Start=%d, FMS=%b", robot.alliance.name(), robot.location, Devices.ds.isFMSAttached());
		
		// Configure LaunchPad and Joystick event handlers.

		launchPad = new LaunchPad(Devices.launchPad, LaunchPadControlIDs.BUTTON_BLUE, this);

		LaunchPadControl lpControl = launchPad.AddControl(LaunchPadControlIDs.ROCKER_LEFT_BACK);
		lpControl.controlType = LaunchPadControlTypes.SWITCH;

		lpControl = launchPad.AddControl(LaunchPadControlIDs.ROCKER_LEFT_FRONT);
		lpControl.controlType = LaunchPadControlTypes.SWITCH;
		//Example on how to track button:
		//launchPad.AddControl(LaunchPadControlIDs.BUTTON_COLOR_HERE);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_RED_RIGHT);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_BLUE);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_BLUE_RIGHT);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_YELLOW);
		launchPad.AddControl(LaunchPadControlIDs.ROCKER_RIGHT);
		launchPad.AddControl(LaunchPadControlIDs.ROCKER_LEFT_BACK);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_GREEN);
		launchPad.addLaunchPadEventListener(new LaunchPadListener());
		launchPad.Start();

		leftStick = new JoyStick(Devices.leftStick, "LeftStick", JoyStickButtonIDs.TRIGGER, this);
		//Example on how to track button:
		//leftStick.AddButton(JoyStickButtonIDs.BUTTON_NAME_HERE);
		leftStick.AddButton(JoyStickButtonIDs.TRIGGER);
		leftStick.addJoyStickEventListener(new LeftStickListener());
		leftStick.Start();

		rightStick = new JoyStick(Devices.rightStick, "RightStick", JoyStickButtonIDs.TRIGGER, this);
		//Example on how to track button:
		//rightStick.AddButton(JoyStickButtonIDs.BUTTON_NAME_HERE);
		rightStick.AddButton(JoyStickButtonIDs.TRIGGER);
		rightStick.AddButton(JoyStickButtonIDs.TOP_LEFT);
		rightStick.AddButton(JoyStickButtonIDs.TOP_MIDDLE);
		rightStick.AddButton(JoyStickButtonIDs.TOP_RIGHT);
		rightStick.addJoyStickEventListener(new RightStickListener());
		rightStick.Start();

		utilityStick = new JoyStick(Devices.utilityStick, "UtilityStick", JoyStickButtonIDs.TRIGGER, this);
		//Example on how to track button:
		//utilityStick.AddButton(JoyStickButtonIDs.BUTTON_NAME_HERE);
		utilityStick.AddButton(JoyStickButtonIDs.TOP_BACK);
		utilityStick.AddButton(JoyStickButtonIDs.TOP_MIDDLE);
		utilityStick.addJoyStickEventListener(new UtilityStickListener());
		utilityStick.Start();

		// Tighten up dead zone for smoother climber movement.
		utilityStick.deadZone = .05;

		// Set CAN Talon brake mode by rocker switch setting.
		// We do this here so that the Utility stick thread has time to read the initial state
		// of the rocker switch.
		if (robot.isComp) Devices.SetCANTalonBrakeMode(lpControl.latchedState);

		// Set gyro/Navx to heading 0.
		//robot.gyro.reset();
		Devices.navx.resetYaw();

		Devices.navx.setHeading(90);

		// Reset encoder.
		//Devices.encoder.reset();

		// Motor safety turned on.
		Devices.robotDrive.setSafetyEnabled(true);

		// Driving loop runs until teleop is over.

		while (robot.isEnabled() && robot.isOperatorControl())
		{
			// Get joystick deflection and feed to robot drive object
			// using calls to our JoyStick class.
			LCD.printLine(3, "Winch Encoder Counts = %d", Devices.WinchEncoder.get());
			LCD.printLine(7, "Wheel encoder 1 =  %d", Devices.SRXEncoder.get());
			SmartDashboard.putBoolean("GearBox", speedShifter.isSlow);
			SmartDashboard.putBoolean("Intake", Block.isIntakeIntaking());
			SmartDashboard.putBoolean("Gear Wrist Out", Block.isOut());
			SmartDashboard.putBoolean("Gear Wrist Open", Block.isGrabberOpen());
			SmartDashboard.putBoolean("Auto Intake", Block.ISAUTOINTAKERUNNING());
			
			rightY = stickLogCorrection(rightStick.GetY());	// fwd/back
			leftY = stickLogCorrection(leftStick.GetY());	// fwd/back

			rightX = stickLogCorrection(rightStick.GetX());	// left/right
			leftX = stickLogCorrection(leftStick.GetX());	// left/right

			utilX = utilityStick.GetX();
			utilY = utilityStick.GetY();

			LCD.printLine(4, "leftY=%.4f  rightY=%.4f  utilX=%.4f", leftY, rightY, utilX);
			LCD.printLine(6, "yaw=%.2f, total=%.2f, rate=%.2f, hdng=%.2f", Devices.navx.getYaw(), Devices.navx.getTotalYaw(), 
					Devices.navx.getYawRate(), Devices.navx.getHeading());
			LCD.printLine(8, "pressureV=%.2f  psi=%d", robot.monitorCompressorThread.getVoltage(), robot.monitorCompressorThread.getPressure());
			// Set wheel motors.
			// Do not feed JS input to robotDrive if we are controlling the motors in automatic functions.

			//if (!autoTarget) robot.robotDrive.tankDrive(leftY, rightY);

			// Two drive modes, full tank and alternate. Switch on right stick trigger.
			
			if (!autoTarget) 
			{
				if (altDriveMode)
				{	// normal tank with straight drive assist when sticks within 10% of each other.
					if (leftRightEqual(leftY, rightY, 10) && Math.abs(rightY) > .50)
					{
						if (!steeringAssistMode) Devices.navx.resetYaw();

						// Angle is negative if robot veering left, positive if veering right when going forward.
						// It is opposite when going backward. Note that for this robot, - power means forward and
						// + power means backward.

						angle = (int) Devices.navx.getYaw();

						LCD.printLine(5, "angle=%d", angle);

						// Invert angle for backwards.

						if (rightY > 0) angle = -angle;

						//Util.consoleLog("angle=%d", angle);

						// Note we invert sign on the angle because we want the robot to turn in the opposite
						// direction than it is currently going to correct it. So a + angle says robot is veering
						// right so we set the turn value to - because - is a turn left which corrects our right
						// drift.

						Devices.robotDrive.curvatureDrive(rightY, angle * gain, false);

						steeringAssistMode = true;
					}
					else
					{
						steeringAssistMode = false;
						Devices.robotDrive.tankDrive(leftY, rightY);		// Normal tank drive.
					}

					SmartDashboard.putBoolean("Overload", steeringAssistMode);
				}
				else
					Devices.robotDrive.tankDrive(leftY, rightY);		// Normal tank drive.
			}
					Winch.WinchMotorTeleOp(utilY);
			
				
				
			
			// Update the robot heading indicator on the DS.

			SmartDashboard.putNumber("Gyro", Devices.navx.getHeading());

			// End of driving loop.

			Timer.delay(.020);	// wait 20ms for update from driver station.
		}
			
		// End of teleop mode.

		Util.consoleLog("end");
	}

	private boolean leftRightEqual(double left, double right, double percent)
	{
		//if (left == right) return true;

		if (Math.abs(left - right) <= (1 * (percent / 100))) return true;

		return false;
	}

	// Custom base logarithm.
	// Returns logarithm base of the value.

	private double baseLog(double base, double value)
	{
		return Math.log(value) / Math.log(base);
	}

	// Map joystick y value of 0.0 to 1.0 to the motor working power range of approx 0.5 to 1.0 using
	// logarithmic curve.

	private double stickLogCorrection(double joystickValue)
	{
		double base = Math.pow(2, 1/3) + Math.pow(2, 1/3);

		if (joystickValue > 0)
			joystickValue = baseLog(base, joystickValue + 1);
		else if (joystickValue < 0)
			joystickValue = -baseLog(base, -joystickValue + 1);

		return joystickValue;
	}

	

	// Handle LaunchPad control events.

	public class LaunchPadListener implements LaunchPadEventListener 
	{
		public void ButtonDown(LaunchPadEvent launchPadEvent) 
		{
			LaunchPadControl	control = launchPadEvent.control;
			
			Util.consoleLog("%s, latchedState=%b", control.id.name(),  control.latchedState);

			switch(control.id)
			{
			//Example of case:
			/*
			case BUTTON_NAME_HERE:
				if (launchPadEvent.control.latchedState)
					DoOneThing();
				else
					DoOtherThing();
				break;
			*/
			case BUTTON_BLUE:
				//Winch.deployForks();
				break;
				
			case BUTTON_RED_RIGHT:
				if (launchPadEvent.control.latchedState)
					Block.WristIn();
				else
					Block.WristOut();
				break;
				
			case BUTTON_YELLOW:
				if(!Winch.isFixedPosition()) {
					Winch.winchSetPosition(14000);
				}
				else {
					Winch.DisablePID();
				}
				break;
				
			case BUTTON_GREEN:
					Devices.SRXEncoder.reset();
					Devices.SRXEncoder2.reset();
				break;
			case BUTTON_BLACK:
				if(launchPadEvent.control.latchedState) {
					Winch.winchStop();	
				}
				else {
					Winch.winchActive();
				}
				break;
			
			default:
				break;
			}
		}

		public void ButtonUp(LaunchPadEvent launchPadEvent) 
		{
			//Util.consoleLog("%s, latchedState=%b", launchPadEvent.control.name(),  launchPadEvent.control.latchedState);
		}

		public void SwitchChange(LaunchPadEvent launchPadEvent) 
		{
			LaunchPadControl	control = launchPadEvent.control;

			Util.consoleLog("%s", control.id.name());

			switch(control.id)
			{
			// Example of Rocker:
			/*
			case ROCKER_NAME_HERE:
				if (control.latchedState)
					DoOneThing();
				else
					DoOtherThing();
				break;
			*/
			case ROCKER_LEFT_FRONT:
				if (robot.cameraThread != null)robot.cameraThread.ChangeCamera();
				
			break;
			case ROCKER_RIGHT:
				if (control.latchedState)robot.cameraThread.ChangeCamera();
				break;
			case ROCKER_LEFT_BACK: 
				if (Devices.isBrakeMode())
					Devices.SetCANTalonBrakeMode(false);	// coast
				else
					Devices.SetCANTalonBrakeMode(true);		// brake
				
				break;
				
			default:
				break;
			}
		}
	}

	// Handle Right JoyStick Button events.

	private class RightStickListener implements JoyStickEventListener 
	{

		public void ButtonDown(JoyStickEvent joyStickEvent) 
		{
			JoyStickButton	button = joyStickEvent.button;

			Util.consoleLog("%s, latchedState=%b", button.id.name(),  button.latchedState);

			switch(button.id)
			{
			case TRIGGER:
				altDriveMode = !altDriveMode;
				break;
				
			case TOP_RIGHT:
				Winch.HoldBar();
				break;
			case TOP_BACK:
				Winch.DeployBar();
				break;
			
			//Example of Joystick Button case:
			/*
			case BUTTON_NAME_HERE:
				if (button.latchedState)
					DoOneThing();
				else
					DoOtherThing();
				break;
			 */
			default:
				break;
			}
		}

		public void ButtonUp(JoyStickEvent joyStickEvent) 
		{
			//Util.consoleLog("%s", joyStickEvent.button.name());
		}
	}

	// Handle Left JoyStick Button events.

	private class LeftStickListener implements JoyStickEventListener 
	{
		public void ButtonDown(JoyStickEvent joyStickEvent) 
		{
			JoyStickButton	button = joyStickEvent.button;

			Util.consoleLog("%s, latchedState=%b", button.id.name(),  button.latchedState);

			switch(button.id)
			{
			//Example of Joystick Button case:
			/*
			case BUTTON_NAME_HERE:
				if (button.latchedState)
					DoOneThing();
				else
					DoOtherThing();
				break;
			 */
			case TRIGGER:
				if (button.latchedState)
    				speedShifter.slowSpeed();
    			else
    				speedShifter.fastSpeed();

				break;
			
			default:
				break;
			}
		}

		public void ButtonUp(JoyStickEvent joyStickEvent) 
		{
			//Util.consoleLog("%s", joyStickEvent.button.name());
		}
	}

	// Handle Utility JoyStick Button events.

	private class UtilityStickListener implements JoyStickEventListener 
	{
		public void ButtonDown(JoyStickEvent joyStickEvent) 
		{
			JoyStickButton	button = joyStickEvent.button;

			Util.consoleLog("%s, latchedState=%b", button.id.name(),  button.latchedState);

			switch(button.id)
			{
			//Example of Joystick Button case:
			/*
			case BUTTON_NAME_HERE:
				if (button.latchedState)
					DoOneThing();
				else
					DoOtherThing();
				break;
			 */
			case TRIGGER:
				if(button.latchedState) {
					Block.WristOpen();
				}
				else Block.WristClose();
				break;
				
			case TOP_MIDDLE:
				if(button.latchedState) {
					Block.MotorStartIntake();
				}
				else{
					Block.stopCubeIntake();
				}
				break;
			case TOP_BACK:
				if(button.latchedState) {
					Block.MotorStartDeposit();
				}
				else {
					Block.stopCubeIntake();
				}
				break;
			case TOP_RIGHT:
				if(!Block.ISAUTOINTAKERUNNING) {
					Block.AutoIntakeStart();
				}
				else {
					Block.AutoIntakeStop();
				}
				break; 
			case TOP_LEFT:
				if (!Winch.isFixedPosition()) {
					Winch.winchSetPosition(10100);
				}
				else {
					Winch.DisablePID();
				}
				break;
			default:
				break;
			}
		}

		public void ButtonUp(JoyStickEvent joyStickEvent) 
		{
			//Util.consoleLog("%s", joyStickEvent.button.id.name());
		}
	}
}
