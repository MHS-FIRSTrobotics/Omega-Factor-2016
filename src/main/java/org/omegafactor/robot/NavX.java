package org.omegafactor.robot;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.google.common.base.Preconditions.checkNotNull;


public class NavX extends AbstractExecutionThreadService {
    private double lastWorldLinearAccelX;
    private double lastWorldLinearAccelY;
    private double kCollisionThreshold_DeltaG = .5;
    private NavXCallback navXCollisionCallback;
    private double maxLinearAccelY;
    private double maxJerkX;
    private double maxJerkY;

    public NavX() {
        lastWorldLinearAccelX = 0;
        lastWorldLinearAccelY = 0;
        navXCollisionCallback = null;
    }

    @Override
    protected void run() throws Exception {
        Thread.currentThread().setPriority(4);
        while (isRunning()) {
            loop();
            Thread.yield();
        }
    }

    public synchronized void loop() {
        if (HardwareMap.isNavXPresent()) {
            boolean collisionDetected = false;
            AHRS ahrs = HardwareMap.navx;
            double worldLinearAccelX = ahrs.getWorldLinearAccelX();
            double currentJerkX = worldLinearAccelX - getLastWorldLinearAccelerationX();
            lastWorldLinearAccelX = worldLinearAccelX;
            double worldLinearAccelY = ahrs.getWorldLinearAccelY();
            double currentJerkY = worldLinearAccelY - getLastWorldLinearAccelerationY();
            lastWorldLinearAccelY = worldLinearAccelY;

            if ((Math.max(Math.abs(currentJerkX), Math.abs(currentJerkY)) > getCollisionThresholdDeltaG())) {
                collisionDetected = true;
                if (isNaxVCollisionDetectionCallbackPresent()) {
                    navXCollisionCallback.call(this);
                }
            }

            maxLinearAccelY = Math.max(Math.abs(getLastWorldLinearAccelerationY()), maxLinearAccelY);
            maxJerkX = Math.max(Math.abs(currentJerkX), maxJerkX);
            maxJerkY = Math.max(Math.abs(currentJerkY), maxJerkY);
            SmartDashboard.putNumber("LIN_ACCEL_Y_MAX", maxLinearAccelY);
            SmartDashboard.putNumber("LIN_ACCEL_Y", getLastWorldLinearAccelerationY());
            SmartDashboard.putNumber("COLL_DETECT_THRESHOLD", getCollisionThresholdDeltaG());

            SmartDashboard.putNumber("X_JERK_MAX", maxJerkX);
            SmartDashboard.putNumber("Y_JERK_MAX", maxJerkY);
            SmartDashboard.putBoolean("Collision Detected", collisionDetected);
        }
    }

    public  boolean isNavXPresent() {
        return HardwareMap.isNavXPresent();
    }

    public synchronized double getLastWorldLinearAccelerationX() {
        return lastWorldLinearAccelX;
    }

    public synchronized double getLastWorldLinearAccelerationY() {
        return lastWorldLinearAccelY;
    }

    public synchronized double getCollisionThresholdDeltaG() {
        return kCollisionThreshold_DeltaG;
    }

    public synchronized boolean isNaxVCollisionDetectionCallbackPresent() {
        return navXCollisionCallback != null;
    }

    public synchronized void configureNavXCollisionDetectionCallback(NavXCallback cb) {
        navXCollisionCallback = checkNotNull(cb);
    }

    public synchronized void setCollisionThresholdDeltaG(double kCollisionThreshold_DeltaG) {
        this.kCollisionThreshold_DeltaG = kCollisionThreshold_DeltaG;
    }

    public interface NavXCallback {
        boolean call(NavX navX);
    }
}
