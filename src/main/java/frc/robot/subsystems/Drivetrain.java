package frc.robot.subsystems;


import org.photonvision.PhotonCamera;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {

    public WPI_TalonFX FL_drive, FR_drive, BL_drive, BR_drive;
    public WPI_VictorSPX R_Brake;
    public WPI_TalonSRX L_Brake;
    public CANSparkMax H_drive;
    public Encoder flEncoder, frEncoder, blEncoder, brEncoder;
    private MotorControllerGroup leftMotorControllerGroup, rightMotorControllerGroup;
    private DifferentialDrive robotDrive;
    public DifferentialDriveOdometry robotDriveOdometry;
    public ADXRS450_Gyro gyro;
    public ADIS16470_IMU IMU;
    public PIDController turnController = new PIDController(0.45, 0, 0);
    public PhotonCamera camera = new PhotonCamera("OV5647");
    //public PhotonPipelineResult result;
    double visionRotationSpeed;
    double visionForwardSpeed;
    public Drivetrain() {
        initialize();
    }

    public void initialize() {
        gyro = new ADXRS450_Gyro();
        IMU = new ADIS16470_IMU();
        
        H_drive = new CANSparkMax(Constants.H_CAN_ID, MotorType.kBrushless);

        FL_drive = new WPI_TalonFX(Constants.FL_CAN_ID);

        FR_drive = new WPI_TalonFX(Constants.FR_CAN_ID);

        BL_drive = new WPI_TalonFX(Constants.BL_CAN_ID);

        BR_drive = new WPI_TalonFX(Constants.BR_CAN_ID);

        L_Brake = new WPI_TalonSRX(Constants.L_BRAKE_CAN_ID);
        L_Brake.setInverted(true);
        R_Brake = new WPI_VictorSPX(Constants.R_BRAKE_CAN_ID);
        R_Brake.setNeutralMode(NeutralMode.Brake);
        L_Brake.setNeutralMode(NeutralMode.Brake);

        leftMotorControllerGroup = new MotorControllerGroup(FL_drive, BL_drive);

        rightMotorControllerGroup = new MotorControllerGroup(FR_drive, BR_drive);
        // one side needs to be inverted so that we can send positive values to both
        // motors and still go forward
        rightMotorControllerGroup.setInverted(true);

        robotDrive = new DifferentialDrive(leftMotorControllerGroup, rightMotorControllerGroup);

        resetEncoders();

        robotDriveOdometry = new DifferentialDriveOdometry(gyro.getRotation2d(), getLeftEncoder(),
                getRightEncoder());

    }

    public double getLeftEncoder() {
        return getEncoderSide(BL_drive, FL_drive);
    }
    public double getRightEncoder() {
        return getEncoderSide(BL_drive, FL_drive);
    }
    public double getLeftEncoderSpeed() {
        return getEncoderSideSpeed(BL_drive, FL_drive);
    }
    public void VisionAlign(){
        
    }
    public void dropBreaks(){
        L_Brake.set(-.48);
        R_Brake.set(-.48);
    }
    public void raiseBreaks(){
        L_Brake.set(.48);
        R_Brake.set(.48);
    }
    public void stopBreaks(){
        L_Brake.set(0);
        R_Brake.set(0);
    }

    public double getRightEncoderSpeed() {
        return getEncoderSideSpeed(BR_drive, FR_drive);
    }

    public double getEncoderSide(WPI_TalonFX m1, WPI_TalonFX m2) {
        return ticksToMeter(averageEncoders(m1, m2));

    }
    public double getEncoderSideSpeed(WPI_TalonFX m1, WPI_TalonFX m2) {
        return ticksToMeter(averageEncodersRate(m1, m2));

    }
    public double getAverageEncoders(){
        return (getEncoderSide(BR_drive, FR_drive)+getEncoderSide(BL_drive, FL_drive))/2;
    }
    public double ticksToMeter(double encoder) {
        //TODO: Change this once chassis finished
        double whd = Units.inchesToMeters(6);//2048 talon fx cpr 
        return (double) ((encoder/2048) * (.1666*(whd * Math.PI))) ;
    }

    public double getHeading() {
        return gyro.getRotation2d().getDegrees();
    }

    // Check here for troubleshooting its possible this value is not correct and
    // needs to be inversed
    public double getTurnRate() {
        return gyro.getRate();
    }
    public void stopAll(){
        leftMotorControllerGroup.set(0);
        rightMotorControllerGroup.set(0);
        H_drive.set(0);
    }
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftMotorControllerGroup.setVoltage(leftVolts);
        rightMotorControllerGroup.setVoltage(rightVolts);
        robotDrive.feed();
        

    }
        //make sure avg is plausible
    public double averageEncoders(WPI_TalonFX m1, WPI_TalonFX m2) {
        double avg = (m1.getSelectedSensorPosition() + m2.getSelectedSensorPosition()) / 2;
        return avg;
    }
    public double averageEncodersRate(WPI_TalonFX m1, WPI_TalonFX m2) {
        double avg = (m1.getSelectedSensorVelocity() + m2.getSelectedSensorVelocity()) / 2;
        return avg;
    }
    public Pose2d getPose() {
        //return null;    
        return robotDriveOdometry.getPoseMeters();
          }

    public void resetEncoders() {
        BL_drive.setSelectedSensorPosition(0);
        BR_drive.setSelectedSensorPosition(0);
        FL_drive.setSelectedSensorPosition(0);
        FR_drive.setSelectedSensorPosition(0);
    }

    public void resetOdometry(Pose2d pose) {
        resetEncoders();
       robotDriveOdometry.resetPosition(
                gyro.getRotation2d(), getLeftEncoder(), getRightEncoder(), pose);
    }

    public void arcadeDrive(double speed, double turn) {
        robotDrive.arcadeDrive(speed, turn);
    }
    public void hDrive(double speed, double turn, double strafe){
        arcadeDrive(speed, turn);
        H_drive.set(strafe);
    }
    /*pxublic void VisionAlign(){
        if(result.hasTargets()){
            result.getBestTarget().getBestCameraToTarget().getX()
        }

    }*/
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
           return new DifferentialDriveWheelSpeeds(getLeftEncoderSpeed(), getRightEncoderSpeed());
          }
    @Override
    public void periodic(){
        robotDriveOdometry.update(gyro.getRotation2d(), getEncoderSide(BL_drive, BL_drive), getEncoderSide(BR_drive, FR_drive));
            SmartDashboard.putNumber("Encoder", getAverageEncoders());
    }
    public PIDController chargeStationPID;
    double pitchAxis = 0;
    public void correctYaw(){
        chargeStationPID = new PIDController(.2, 0, 0);
        leftMotorControllerGroup.set(chargeStationPID.calculate(pitchAxis, 0));
        rightMotorControllerGroup.set(chargeStationPID.calculate(pitchAxis, 0));
        

    }

}
