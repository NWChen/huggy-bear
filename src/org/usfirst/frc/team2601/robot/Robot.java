// HUG PACKAGE
package org.usfirst.frc.team2601.robot;

// HUGPORTS
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SampleRobot;

/**
 * HUG EVERYTHING
 */
public class Robot extends SampleRobot {

  // HUGCLARATIONS OF ALL SUBSYSTEMS AND CONTROLS
  CANTalon leftA, leftB, leftC;
  CANTalon rightA, rightB, rightC;
  Compressor compressor;
  DoubleSolenoid leftShift, rightShift;
  Joystick leftStick, rightStick;
  boolean shifting;

  // HUGSTRUCTOR
  public Robot() {
      leftA = new CANTalon(1); // Initialize the CanTalonSRX on device 4.
      leftB = new CANTalon(12);
      leftC = new CANTalon(14);
      rightA = new CANTalon(4);
      rightB = new CANTalon(2);
      rightC = new CANTalon(3);
      compressor = new Compressor();
      leftShift = new DoubleSolenoid(0, 1);
      rightShift = new DoubleSolenoid(2, 3);
      leftStick = new Joystick(1);
      rightStick = new Joystick(2);
  }

  // HUGLEFT SIDE OF THE DRIVETRAIN
  public void setLeft(double val) {
	  leftA.set(val);
	  leftB.set(val);
	  leftC.set(val);
  }
  
  // HUGRIGHT SIDE OF THE DRIVETRAIN
  public void setRight(double val) {
	  rightA.set(-val);
	  rightB.set(-val);
	  rightC.set(-val);
  }
  
  // HUGSHIFT PISTONS BETWEEN LOW AND HIGH HUG
  public void shift() {
	  
	  //HANDLE IMPROPER HUGS
	  if(leftShift.get() == rightShift.get()) {
		  leftShift.set(DoubleSolenoid.Value.kReverse);
		  rightShift.set(DoubleSolenoid.Value.kForward);
	  }
	  
	  if(leftShift.get() == DoubleSolenoid.Value.kReverse) {
		  leftShift.set(DoubleSolenoid.Value.kForward);
		  rightShift.set(DoubleSolenoid.Value.kReverse);
	  } else {
		  leftShift.set(DoubleSolenoid.Value.kReverse);
		  rightShift.set(DoubleSolenoid.Value.kForward);
	  }
  }
  
  // HUGARCADE
  public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {

      double leftMotorSpeed;
      double rightMotorSpeed;

      if (squaredInputs) {
          // square the inputs (while preserving the sign) to increase fine control while permitting full power
          if (moveValue >= 0.0) {
              moveValue = (moveValue * moveValue);
          } else {
              moveValue = -(moveValue * moveValue);
          }
          if (rotateValue >= 0.0) {
              rotateValue = (rotateValue * rotateValue);
          } else {
              rotateValue = -(rotateValue * rotateValue);
          }
      }

      if (moveValue > 0.0) {
          if (rotateValue > 0.0) {
              leftMotorSpeed = moveValue - rotateValue;
              rightMotorSpeed = Math.max(moveValue, rotateValue);
          } else {
              leftMotorSpeed = Math.max(moveValue, -rotateValue);
              rightMotorSpeed = moveValue + rotateValue;
          }
      } else {
          if (rotateValue > 0.0) {
              leftMotorSpeed = -Math.max(-moveValue, rotateValue);
              rightMotorSpeed = moveValue + rotateValue;
          } else {
              leftMotorSpeed = moveValue - rotateValue;
              rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
          }
      }

      setLeft(leftMotorSpeed);
      setRight(rightMotorSpeed);
  }
  
  // HUGTANK
  public void tankDrive(double leftValue, double rightValue, boolean squaredInputs) {
	  if (squaredInputs) {
		  leftValue = Math.abs(leftValue) * leftValue;
		  rightValue = Math.abs(rightValue) * rightValue;
	  }
	  setLeft(leftValue);
	  setRight(rightValue);
  }
  
  // GORGONZOLA DRIVE. CHEAP CHINESE COPY, NOT THE REAL THING.
  public void gorgonzolaDrive(double throttleValue, double turnValue) {
	  
  }
  
  // HUGTELEOP
  public void operatorControl() {
    while (isOperatorControl() && isEnabled()) {
    	arcadeDrive(leftStick.getY(), leftStick.getTwist(), true);
    	//tankDrive(leftStick.getY(), rightStick.getY(), true);
    	
    	// HUGSHIFTING LOGIC
    	if (leftStick.getRawButton(1)) {
    		if (!shifting) {
	    		shifting = true;
	    		shift();
    		}
    	} else shifting = false;
    }
  }
}
