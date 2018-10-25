package org.usfirst.frc.team2473.robot.subsystems;

import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.commands.GryoCommand;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Gryo extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	
	public AnalogGyro gyro = new AnalogGyro(RobotMap.ANALOG_GYRO.value);
	
	public void bigYeet() {
		GryoCommand g = new GryoCommand();
		g.start();
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

