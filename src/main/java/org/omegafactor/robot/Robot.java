package org.omegafactor.robot;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.Service;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Robot extends IterativeRobot {
    private final Talon test;
    private TeleOp teleOp;

    public Robot() {
        super();
        test = new Talon(2);
        teleOp = new TeleOp();
    }

    @Override
    public void robotInit() {
        System.out.print("Hi!");
    }

    @Override
    public void teleopInit() {
        teleOp = new TeleOp();
        teleOp.startAsync();
   }

    @Override
    public void disabledInit() {
        teleOp.stopAsync();
        try {
            teleOp.awaitTerminated(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public void teleopPeriodic() {
        if (!teleOp.isRunning()) {
            teleOp.startAsync();
        }
    }

    private class TeleOp extends AbstractExecutionThreadService implements CoreRobotService {
        @Override
        public String getName() {
            return "TeleOp";
        }

        @Override
        public OperationalType getType() {
            return OperationalType.TELEOP;
        }


        @Override
        protected void run() throws Exception {
            while (isRunning()) {
                test.set(1);
            }
        }
    }
}

