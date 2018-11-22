package org.usfirst.frc.team2473.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2473.robot.RobotMap;
import org.usfirst.frc.team2473.robot.commands.TankDrive; 

/**
 *
 */
public class DriveTrain extends Subsystem {

    private Spark leftMotor;
    private Spark rightMotor;

    public DriveTrain(){
    	leftMotor = new Spark(RobotMap.leftMotor); // Integer values inside Spark constructor correspond to slot it is plugged in to RoboRIO
    	rightMotor = new Spark(RobotMap.rightMotor);
    }
    
    public void setSpeed(double leftVal, double rightVal){
    	leftMotor.set(leftVal);
    	rightMotor.set(rightVal);
    } 
    
    public void initDefaultCommand() {
        setDefaultCommand(new TankDrive());
    }
}


public void act(String code) {
	
	HashMap<String, String> map = new HashMap<>();
	
}
