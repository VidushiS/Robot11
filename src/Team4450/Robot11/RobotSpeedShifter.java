package Team4450.Robot11;

import Team4450.Lib.*;

public class RobotSpeedShifter {
	public boolean isSlow;
	public Robot robot;
	public RobotSpeedShifter(Robot robot) {
		
	}
	public void slowSpeed() {
		Devices.speedShift.SetA();
		Util.consoleLog("Slow Speed Mode enabled");
		isSlow = true;
	}
	public void fastSpeed() {
		Devices.speedShift.SetB();
		isSlow = false;
		Util.consoleLog("Fast Speed Mode enabled");
	}

}
