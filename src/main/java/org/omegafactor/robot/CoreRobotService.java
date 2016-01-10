package org.omegafactor.robot;

import com.google.common.util.concurrent.Service;

/**
 * Created by FIRST on 1/9/2016.
 */
public interface CoreRobotService extends Service {
    String getName();

    OperationalType getType();

    enum OperationalType {
        STOP, TELEOP, AUTONOMOUS
    }
}
