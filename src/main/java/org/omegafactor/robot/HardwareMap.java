package org.omegafactor.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import org.omegafactor.robot.hardware.ExtensibleGamepad;
import org.omegafactor.robot.hardware.internal.F130Gamepad;


public class HardwareMap {
    public static final int DRIVE_RIGHT_BACK_PWM_PIN = 0;
    public static final int DRIVE_LEFT_BACK_PWM_PIN = 1;
    public static final int DRIVE_RIGHT_FRONT_PWM_PIN = 2;
    public static final int DRIVE_LEFT_FRONT_PWM_PIN = 3;
    public static final int COMP_BALL_RETRIEVER_PWM_PIN = 4;
    public static final int COMP_ARM_MOTOR_PWM_PIN = 5;
    public static final int COMP_WINCH_MOTOR = 6;

    public static final Talon rightFront =  new Talon(DRIVE_RIGHT_FRONT_PWM_PIN);
    public static final Talon leftFront =  new Talon(DRIVE_LEFT_FRONT_PWM_PIN);
    public static final Talon leftBack = new Talon(DRIVE_LEFT_BACK_PWM_PIN);
    public static final Talon rightBack =  new Talon(DRIVE_RIGHT_BACK_PWM_PIN);
    public static final VictorSP ballRetreiver = new VictorSP(COMP_BALL_RETRIEVER_PWM_PIN);
    public static final Spark armMotor = new Spark(COMP_ARM_MOTOR_PWM_PIN);
    public static final VictorSP winchMotor = new VictorSP(COMP_WINCH_MOTOR);
    public static AHRS navx;

    public static F130Gamepad joystick1 = new F130Gamepad(new Joystick(0));
    public static F130Gamepad joystick2 = new F130Gamepad(new Joystick(1));
    public static ExtensibleGamepad gamepad1 = new ExtensibleGamepad(joystick1);
    public static ExtensibleGamepad gamepad2 = new ExtensibleGamepad(joystick2);

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
            if (navx == null) {
                configureNavX();
            }
            configured = true;
        }
    }

    private static void configureNavX() {
        try {
            navx = new AHRS(SerialPort.Port.kMXP);
        } catch (RuntimeException ex) {
            System.err.println("FAILED to get NavX");
            ex.printStackTrace();
        }
    }

    public static boolean isNavXPresent() {
        if (navx == null) {
            configureNavX();
        }

        return navx != null;
    }

    private static HardwareMap getInstance() {
        if (instance == null) {
            instance = new HardwareMap();
        }

        return instance;


    }
}