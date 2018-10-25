/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2473.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// Slot leftMotor is plugged into in the RoboRIO. PWM mapping.
	public static int leftMotor = 0;
	// Slot rightMotor is plugged into in the RoboRIO. PWM mapping.
	public static int rightMotor = 1;
	// JoyStick configuration
	public static int leftJoystickIndex = 0;
	public static int rightJoystickIndex = 1;
}
