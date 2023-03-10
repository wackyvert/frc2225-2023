// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.OperatorConstants.AutoConstants;
import frc.robot.Constants.OperatorConstants.DriveConstants;
import frc.robot.Constants.*;
import frc.robot.commands.ArmIn;
import frc.robot.commands.ArmOut;

import frc.robot.commands.ChargeSwitch;
import frc.robot.commands.CloseClaw;

import frc.robot.commands.Grab;
import frc.robot.commands.IntakeSpin;
import frc.robot.commands.IntakeSpinIn;
import frc.robot.commands.OpenClaw;
import frc.robot.commands.RaiseIntake;
import frc.robot.commands.StopDrop;
import frc.robot.commands.TurnPivot;
import frc.robot.commands.TurnPivotDown;
import frc.robot.commands.dropIntake;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Drivetrain;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.OperatorInput;

import org.opencv.osgi.OpenCVNativeLoader;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of Pthe robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public static final Drivetrain mDrivetrain = new Drivetrain();
  public static final Claw m_Claw = new Claw();
  public static final Intake mIntake = new Intake();
  
  // Replace with CommandPS4Controller or CommandJoystick if needed
  Joystick rightjoystick = new Joystick(Constants.OperatorConstants.RIGHTJOYSTICK_ID);
  Joystick leftjoystick = new Joystick(Constants.OperatorConstants.LEFTJOYSTICK_ID);
  Trigger button1 = new JoystickButton(leftjoystick, 1);
  Trigger button2 = new JoystickButton(leftjoystick, 2);
  Trigger button3 = new JoystickButton(leftjoystick, 3);
  Trigger button4 = new JoystickButton(leftjoystick, 4);
  Trigger button5 = new JoystickButton(leftjoystick, 5);
  Trigger button6 = new JoystickButton(leftjoystick, 6);
  Trigger button11 = new JoystickButton(rightjoystick, 11);
  Trigger button12 = new JoystickButton(rightjoystick, 12);
  Trigger button14 = new JoystickButton(rightjoystick, 14);
  Trigger button13 = new JoystickButton(rightjoystick, 13);
  Trigger y = new Trigger(leftjoystick.axisGreaterThan(1, .5, null));

  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
    

  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    button14.onTrue(new ChargeSwitch());
    
    button2.whileTrue(new CloseClaw());
    button1.whileTrue(new OpenClaw());
    button3.whileTrue(new ArmOut());
    button4.whileTrue(new ArmIn());
    button5.whileTrue(new TurnPivotDown());
    button6.whileTrue(new TurnPivot());
    button11.onTrue(new SequentialCommandGroup(new dropIntake(), new WaitCommand(.27), new StopDrop()));
    button12.onTrue(new SequentialCommandGroup(new RaiseIntake(), new WaitCommand(.27), new StopDrop()));
    button13.whileTrue(new IntakeSpin());
    button14.whileTrue(new IntakeSpinIn());
    
    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand(Trajectory chosenTrajectory) {
    
    // An example command will be run in autonomous
    var autoVoltageConstraint =
           new DifferentialDriveVoltageConstraint(
               new SimpleMotorFeedforward(
                    DriveConstants.ksVolts,
                   DriveConstants.kvVoltSecondsPerMeter,
                   DriveConstants.kaVoltSecondsSquaredPerMeter),
               DriveConstants.kDriveKinematics,
           10);
    
       
      

              RamseteCommand ramseteCommand =
                      new RamseteCommand(
                          chosenTrajectory,
                          mDrivetrain::getPose,
                        new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
                          new SimpleMotorFeedforward(
                              DriveConstants.ksVolts,
                             DriveConstants.kvVoltSecondsPerMeter,
                             DriveConstants.kaVoltSecondsSquaredPerMeter),
                          DriveConstants.kDriveKinematics,
                          mDrivetrain::getWheelSpeeds,
                          new PIDController(DriveConstants.kPDriveVel, 0, 0),
                          new PIDController(DriveConstants.kPDriveVel, 0, 0),
                          // RamseteCommand passes volts to the callback
                          mDrivetrain::tankDriveVolts,
                          mDrivetrain);
              
                  // Reset odometry to the starting pose of the trajectory.
                mDrivetrain.resetOdometry(chosenTrajectory.getInitialPose());
              
                  // Run path following command, then stop at the end.
                  return ramseteCommand.andThen(() -> mDrivetrain.tankDriveVolts(0, 0));
  }
}
