package org.omegafactor.robot;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.Service;
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

        final List<AbstractExecutionThreadService> services = Arrays.asList(teleOp, server, autonomous, watchdog, HardwareMap.navX);
        ServiceManager manager = new ServiceManager(services);
        manager.startAsync();
        try {
            manager.awaitHealthy(2, TimeUnit.SECONDS);
        } catch (TimeoutException|IllegalStateException e) {
            final ImmutableMultimap<Service.State, Service> stateServiceImmutableMultimap = manager.servicesByState();

            if (stateServiceImmutableMultimap.containsKey(Service.State.FAILED)) {
                 ImmutableCollection<Service> services1 = stateServiceImmutableMultimap.get(Service.State.FAILED);
                System.err.println(services1.size() + " service(s) failed to start");
                for (Service service : services1) {
                    final Throwable throwable = service.failureCause();
                    System.err.println(service.getClass().getSimpleName() + " FAILED");
                    if (throwable != null) {
                        System.err.println(throwable.getMessage());
                        throwable.printStackTrace();
                    }
                }
            }

            System.err.print(e.getMessage() + "\n" + Throwables.getStackTraceAsString(e));
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
            System.err.println("Waiting for TeleOp shutdown...");
            teleOp.waitUntilStopped();
            System.err.println("Waiting for Autonomous shutdown...");
            autonomous.waitUntilStopped();
            System.err.println("Done!");
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }
}

