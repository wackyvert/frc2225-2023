// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.drivetrain.ArcadeDrive;
import frc.robot.commands.intake.IntakeSpin;
import frc.robot.commands.intake.StartIntakeAuto;
import frc.robot.commands.intake.stopIntake;
import frc.robot.commands.pivot.ZeroPivot;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  public static boolean chargeMode;
  private RobotContainer m_robotContainer;
  SendableChooser<Command> autoChooser ;
  Servo eye1 = new Servo(3);
  Servo eye2 = new Servo(4);
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
  
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    autoChooser= new SendableChooser<>();
    autoChooser.addOption("None", null);
    CameraServer.startAutomaticCapture();
    
    
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {CommandScheduler.getInstance().cancelAll();}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    new ZeroPivot().schedule();
    m_autonomousCommand = null;
    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
    new SequentialCommandGroup(new StartIntakeAuto(), new WaitCommand(1), new stopIntake()).schedule();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    
  }

  @Override
  public void teleopInit() {
    RobotContainer.m_Claw.Grab.setSelectedSensorPosition(0);
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    chargeMode = false;
I=0;
    CommandScheduler.getInstance().setDefaultCommand(RobotContainer.mDrivetrain, new ArcadeDrive());
    
  }
  public double eyeValue;
  int I;
  boolean eye=false;
  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
    I++;
    //SmartDashboard.putData("Zero Pivot Encoder", new ZeroPivot());
    //SmartDashboard.putNumber("Pivot Encoder", RobotContainer.m_Claw.getPivotEncoder());
    SmartDashboard.putBoolean("Landing Gear", chargeMode);
    SmartDashboard.putNumber("Grab Encoder", RobotContainer.m_Claw.grabEncoderVal);
   
   if(I%100==0){
    eye^=eye;
    if(eye){
    eye1.set(.9);
    }else{eye1.set(eye1.getPosition()/2);}
   }
   
    
}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
