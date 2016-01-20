package org.omegafactor.robot;

import edu.wpi.first.wpilibj.Talon;


public class HardwareMap {
    public static final int DRIVE_RIGHT_BACK_PWM_PIN = 0;
    public static final int DRIVE_LEFT_BACK_PWM_PIN = 1;
    public static final int DRIVE_RIGHT_FRONT_PWM_PIN = 2;
    public static final int DRIVE_LEFT_FRONT_PWM_PIN = 3;

    public static final Talon rightFront =  new Talon(DRIVE_RIGHT_FRONT_PWM_PIN);
    public static final Talon leftFront =  new Talon(DRIVE_LEFT_FRONT_PWM_PIN);
    public static final Talon leftBack = new Talon(DRIVE_LEFT_BACK_PWM_PIN);
    public static final Talon rightBack =  new Talon(DRIVE_RIGHT_BACK_PWM_PIN);

    private static boolean configured;
    private static HardwareMap instance;

    private HardwareMap() {
        instance = this;
        HardwareMap.configure();
    }

    public static void configure() {
        HardwareMap instance = getInstance();
        if (!configured) {
            HardwareMap.rightBack.setInverted(true);
            HardwareMap.rightFront.setInverted(true);

            configured = true;
        }
    }

    private static HardwareMap getInstance() {
        if (instance == null) {
            instance = new HardwareMap();
        }

        return instance;
    }
}