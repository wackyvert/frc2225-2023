// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final int FL_CAN_ID = 1;
    public static final int FR_CAN_ID = 2;
    public static final int BL_CAN_ID = 3;
    public static final int BR_CAN_ID = 4;
    public static final int H_CAN_ID = 5;
    public static final int Arm_CAN_ID = 8;
    public static final int Grab_CAN_ID = 7;
    public static final int Pivot_CAN_ID = 6;
    public static final int RightSb_CAN_ID = 10;
    public static final int LeftSb_CAN_ID = 5;
    public static final int RightBag_CAN_ID = 11;
    public static final int LeftBag_CAN_ID = 12;
    public static final double armkS=.14221;
    public static final double armkA=.45488;
    public static final double armkV=9.7735;
    public static final double armkG=.2783;
    public static final int L_BRAKE_CAN_ID = LeftSb_CAN_ID;
    public static final int R_BRAKE_CAN_ID = RightSb_CAN_ID;

  public static class OperatorConstants {
    public static final int LEFTJOYSTICK_ID = 1;
    public static final int RIGHTJOYSTICK_ID=0;
    public static final int XBOX_CONTROLLER_ID = 2;
  
  public static class DriveConstants{
    public static final double kaVoltSecondsSquaredPerMeter=.54701;
    public static final double kMaxAccelerationMetersPerSecondSquared=1;
    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kvVoltSecondsPerMeter = 1.3265;
    public static final double ksVolts = 0.29285;
    public static final double kPDriveVel=1.9821;
    public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(.6);
  }
  public static class AutoConstants{
    public static final double kRamseteB=2;
    public static final double kRamseteZeta=0.7;
  }
    
    
  }
}
