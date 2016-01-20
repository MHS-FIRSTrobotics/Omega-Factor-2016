package org.omegafactor.robot;

import com.google.common.base.Throwables;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;

import static com.google.common.base.Preconditions.checkNotNull;

public class Robot extends IterativeRobot {
    private TeleOp teleOp;

    public Robot() {
        super();
        HardwareMap.configure();
        teleOp = new TeleOp();
        teleOp.startAsync();
    }

    @Override
    public void robotInit() {
        System.out.print("Hi!");
    }

    @Override
    public void teleopInit() {
        teleOp.startMode();
    }

    @Override
    public void disabledInit() {
        teleOp.stopMode();
        try {
            System.err.print("Waiting for TeleOp shutdown...");
            teleOp.waitUntilStopped();
            System.err.println("Done!");
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public void teleopPeriodic() {

    }
}

