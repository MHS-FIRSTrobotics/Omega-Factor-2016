package org.omegafactor.robot;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.Service;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by FIRST on 2/2/2016.
 */
public class Watchdog extends AbstractExecutionThreadService {
    private final Service[] services;

    public Watchdog(Service... services) {
        this.services = services;
    }

    @Override
    protected void run() throws Exception {
        Thread.currentThread().setPriority(2);

        while (isRunning()) {
            for (Service service : services) {
                SmartDashboard.putBoolean(service.getClass().getSimpleName() + "_STATUS", service.isRunning());
            }

            Thread.yield();
        }
    }
}
