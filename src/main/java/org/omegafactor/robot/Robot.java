package org.omegafactor.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;

public class Robot extends IterativeRobot {

    private final Talon test;

    public Robot() {
        test = new Talon(2);
    }

    @Override
    public void robotInit() {
        System.out.print("Hi!");
    }

    @Override
    public void teleopInit() {
        System.err.println("TeleOp Called");
       test.set(1);
   }

    @Override
    public  void disabledInit() {
        test.set(0);
    }

    @Override
    public void teleopPeriodic() {
        System.out.println("TeleOpP Called");
        test.set(.9);
    }
}

