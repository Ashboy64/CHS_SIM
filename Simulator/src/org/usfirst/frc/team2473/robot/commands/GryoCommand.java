package org.usfirst.frc.team2473.robot.commands;

import org.usfirst.frc.team2473.robot.subsystems.Gryo;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GryoCommand extends Command {

	public Gryo g = new Gryo();

    public GryoCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	g.gyro.calibrate();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	System.out.println(g.gyro.getName());
    	System.out.println(g.gyro.getCenter());
    	System.out.println(g.gyro.getAngle());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
