package org.omegafactor.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.google.common.base.Preconditions.checkNotNull;


public class NavX {
    private double lastWorldLinearAccelX;
    private double lastWorldLinearAccelY;
    private double kCollisionThreshold_DeltaG = .5f;
    private NavXCallback navXCollisionCallback;

    public NavX() {
        lastWorldLinearAccelX = 0;
        lastWorldLinearAccelY = 0;
        navXCollisionCallback = null;
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

            if ((Math.abs(currentJerkX) > getCollisionThresholdDeltaG()) || (Math.abs(currentJerkY) > getCollisionThresholdDeltaG())) {
                collisionDetected = true;
                if (isNaxVCollisionDetectionCallbackPresent()) {
                    navXCollisionCallback.call(this);
                }
            }
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
