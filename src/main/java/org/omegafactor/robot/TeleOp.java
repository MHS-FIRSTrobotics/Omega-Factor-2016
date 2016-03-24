package org.omegafactor.robot;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.omegafactor.robot.hardware.ExtensibleGamepad;


class TeleOp extends AbstractExecutionThreadService implements CoreRobotService {
    private final ExtensibleGamepad gamepad1 = HardwareMap.gamepad1;
    private final ExtensibleGamepad gamepad2 = HardwareMap.gamepad2;
    private boolean stopped = true;
    private boolean isStopped = true;
    private final double ball_retriver_speed = -1;
    private boolean ballRetrieve;
    private boolean ballRetrieveBack;
    private boolean neitherPressed;
    private Thread executionThread;

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
        executionThread = Thread.currentThread();

//        SendableChooser chooser = new SendableChooser();
//        chooser.addDefault("DEFAULT TELEOP", true);
//        chooser.addObject("RECORD AUTO", false);
//        SmartDashboard.putData("TELEOP SELECTION", chooser);
        while (isRunning()) {
            while (stopped) {
                Thread.sleep(50);
            }
            isStopped = false;
//                if (gamepad1.getAxisCount() != 6) {
//                    throw new IllegalStateException("The Controller is using an incorrect amount of axises," +
//                            " please verify the the F130 mode is set to D");
//                }

                HardwareMap.joystick1.leftRumble(1);
                while (!stopped) {
                    loopTeleOp();
                }
                isStopped = true;

            }
        }


    public void loopTeleOp() {
        SmartDashboard.putNumber("BATT_VOLT", DriverStation.getInstance().getBatteryVoltage());
        moveRobot();
        powerBallFetcher();
        driveArmMotor();
        driveWinchMotor();
        driveArmLift();
    }

    private void moveRobot() {
        double rightY;
        double leftY;
       //HardwareMap.navX.loop();
        gamepad1.updateGamepad();
        gamepad2.updateGamepad();

        final double gamePad1LeftX = gamepad1.leftJoystick().X();
        final double gamePad1LeftY = gamepad1.leftJoystick().Y();
        //System.out.println("Joystick: " + HardwareMap.joystick1.leftX() + " Gamepad: " + gamepad1.leftJoystick().X());
        final double x = gamepad1.rightJoystick().X();
        final double y = gamepad1.rightJoystick().Y();

        if (gamepad1.isLeftBumperPressed()) {
            System.out.print("Inverted Mode");
            leftY = -gamePad1LeftY;
            rightY = -y;
        } else {
            leftY = gamePad1LeftY;
            rightY = y;
        }

        if (gamepad1.getLeftTrigger() > .5) {
            leftY = gamePad1LeftY + gamePad1LeftX;
            rightY = gamePad1LeftY - gamePad1LeftX;

            double speed = Math.sqrt(x * x + y * y);
            leftY *= speed;
            rightY *= speed;
            leftY = Math.min(Math.max(leftY, -1), 1);
            rightY = Math.min(Math.max(rightY, -1), 1);
        }

        if (gamepad1.getRightTrigger() > .15 && gamepad1.getRightTrigger() <= .8) {
            leftY /= 5d;
            rightY /= 5d;
        } else if (gamepad1.getRightTrigger() <= .15) {
            leftY *= .75;
            rightY *= .75;
        }

        if (gamepad1.isRightBumperPressed()) {
            System.out.println("High Res Mode");
        } else {
            leftY = Math.round(leftY * 10) / 10d;
            rightY = Math.round(rightY * 10) / 10d;
        }

        if (!DriverStation.getInstance().isBrownedOut()) {
            HardwareMap.rightFront.set(rightY);
            HardwareMap.leftFront.set(leftY);
        }

        HardwareMap.rightBack.set(rightY);
        HardwareMap.leftBack.set(leftY);
    }

    private void powerBallFetcher() {
        if (neitherPressed) {
            if (gamepad1.isAPressed() && !gamepad1.isBPressed()) {
                ballRetrieve = !ballRetrieve;
                ballRetrieveBack = false;
            } else if (gamepad1.isBPressed() && !gamepad1.isAPressed()) {
                ballRetrieveBack = !ballRetrieveBack;
                ballRetrieve = false;
            }
        }

        neitherPressed = !(gamepad1.isAPressed() || gamepad1.isBPressed());

        if (gamepad1.isXPressed()) {
            ballRetrieve = false;
            ballRetrieveBack = false;
            HardwareMap.ballReceiver.set(0);
        }

        if (ballRetrieve) {
            HardwareMap.ballReceiver.set(ball_retriver_speed);
        } else if (ballRetrieveBack) {
            HardwareMap.ballReceiver.set(-ball_retriver_speed);
        } else {
            HardwareMap.ballReceiver.set(0);
        }
    }

    private void driveArmMotor() {
        if (gamepad2.rightJoystick().Y() >= 0.0025 || gamepad2.rightJoystick().Y() <= -0.0025) {
            final double armPower = gamepad2.rightJoystick().Y();
            HardwareMap.armMotor.set(gamepad2.isLeftBumperPressed() ? armPower : armPower / 2);
        } else {
            HardwareMap.armMotor.set(0);
        }
    }

    private void driveWinchMotor() {
        if (gamepad2.isAPressed()) {
            HardwareMap.winchMotor.set(1);
        } else if(gamepad2.isBPressed()) {
            HardwareMap.winchMotor.set(-1);
        } else {
            HardwareMap.winchMotor.set(0);
        }
    }

    private void driveArmLift() {
        if (gamepad2.isXPressed()) {
            HardwareMap.armPushUp.set(.1);
        } else if (gamepad2.isYPressed()) {
            HardwareMap.armPushUp.set(-.1);
        } else {
            HardwareMap.armPushUp.set(0);
        }
    }

    private static double clip(double x, double min, double max) {
        return Math.min(max, Math.max(x, min));
    }

    public synchronized void stopMode() {
        stopped = true;
        executionThread.setPriority(3);
        CameraServer.requestPriorityChange(3);
    }

    public synchronized void startMode() {
        stopped = false;
        executionThread.setPriority(7);
        CameraServer.requestPriorityChange(6);
    }

    public boolean waitUntilStopped() throws InterruptedException {
        while (!isStopped) {
            Thread.sleep(20);
        }

        return isStopped;
    }
}
