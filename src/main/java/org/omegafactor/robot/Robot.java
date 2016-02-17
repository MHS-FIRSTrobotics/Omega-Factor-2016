package org.omegafactor.robot;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.ServiceManager;
import edu.wpi.first.wpilibj.IterativeRobot;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Robot extends IterativeRobot {
    private TeleOp teleOp;
    private final CameraServer server;
    private final Autonomous autonomous;
    private final Watchdog watchdog;

    public Robot() {
        super();
        HardwareMap.configure();
        teleOp = new TeleOp();
        server = new CameraServer();
        autonomous = new Autonomous(teleOp);
        watchdog = new Watchdog();
        final List<AbstractExecutionThreadService> services = Arrays.asList(teleOp, server, /*autonomous,*/ watchdog);
        ServiceManager manager = new ServiceManager(services);
        manager.startAsync();
        try {
            manager.awaitHealthy(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void robotInit() {
        System.out.println("Hi! I am starting up");

    }

    @Override
    public void teleopInit() {
        teleOp.startMode();
    }

    @Override
    public void autonomousInit() {
        autonomous.startActive();
    }

    @Override
    public void disabledInit() {
        teleOp.stopMode();
        autonomous.stopActive();
        try {
            System.err.print("Waiting for TeleOp shutdown...");
            teleOp.waitUntilStopped();
            System.err.println("Done!");
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }
}

