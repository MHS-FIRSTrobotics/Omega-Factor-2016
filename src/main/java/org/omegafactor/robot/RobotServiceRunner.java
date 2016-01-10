package org.omegafactor.robot;

import com.google.common.util.concurrent.ServiceManager;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by FIRST on 1/9/2016.
 */
public class RobotServiceRunner {
    private ServiceManager serviceManager;

    public RobotServiceRunner(CoreRobotService... services) {
        serviceManager = new ServiceManager(Arrays.asList(services));
        try {
            serviceManager.startAsync().awaitHealthy(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
