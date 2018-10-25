/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot;

import edu.wpi.first.wpilibj.*;

public class OI {
	// JoySticks flicker back and forth even at rest; if magnitude of displacement is less than this, don't do anything
	public final double DEADZONE = 0.05;
	public final Joystick LEFT_JOYSTICK = new Joystick(RobotMap.leftJoystickIndex);
	public final Joystick RIGHT_JOYSTICK = new Joystick(RobotMap.rightJoystickIndex);
	
	public OI() {
		
	}
	
	public double leftJoyX(){
		return (Math.abs(LEFT_JOYSTICK.getX()) < DEADZONE) ? 0 : LEFT_JOYSTICK.getX();
	}
	
	public double rightJoyX() {
		return (Math.abs(RIGHT_JOYSTICK.getX()) < DEADZONE) ? 0 : RIGHT_JOYSTICK.getX();
	}
	
	public double leftJoyY(){
		return (Math.abs(LEFT_JOYSTICK.getY()) < DEADZONE) ? 0 : LEFT_JOYSTICK.getY();
	}
	
	public double rightJoyY() {
		return (Math.abs(RIGHT_JOYSTICK.getY()) < DEADZONE) ? 0 : RIGHT_JOYSTICK.getY();
	}
}
