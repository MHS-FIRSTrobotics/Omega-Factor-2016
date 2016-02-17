package org.omegafactor.robot;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.omegafactor.robot.hardware.ExtensibleGamepad;
import org.omegafactor.robot.hardware.internal.F130Gamepad;


class TeleOp extends AbstractExecutionThreadService implements CoreRobotService {
    private final ExtensibleGamepad gamepad1 = HardwareMap.gamepad1;
    private final ExtensibleGamepad gamepad2 = HardwareMap.gamepad2;
    private boolean stopped = true;
    private boolean isStopped = true;
    private final double ball_retriver_speed = -1;
    private boolean ballRetrieve;
    private boolean ballRetrieveBack;
    private boolean neitherPressed;

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
        while (isRunning()) {
            while (stopped) {
                Thread.sleep(50);
            }
            isStopped = false;
//                if (gamepad1.getAxisCount() != 6) {
//                    throw new IllegalStateException("The Controller is using an incorrect amount of axises," +
//                            " please verify the the F130 mode is set to D");
//                }
            while (!stopped) {
                loopTeleOp();
            }
            isStopped = true;
        }
    }

    public void loopTeleOp() {
        double rightY;
        double leftY;

        final double gamePad1LeftX = gamepad1.leftJoystick().X();
        final double gamePad1LeftY = gamepad1.leftJoystick().Y();
        final double x = gamepad1.rightJoystick().X();
        final double y = gamepad1.rightJoystick().Y();
        if (gamepad1.isLeftBumperPressed()) {
            System.out.print("Inverted Mode");
            leftY = -gamePad1LeftY;
            rightY = -y;
        } else {
            leftY = y;
            rightY = y;
        }

        if (gamepad1.getLeftTrigger() > .5) {
            leftY = gamePad1LeftY + gamePad1LeftX;

            rightY = gamePad1LeftY - x;

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
            leftY /= 2d;
            rightY /= 2d;
        }

        if (gamepad1.isRightBumperPressed()) {
            System.out.println("High Res Mode");
        } else {
            leftY = Math.round(leftY * 10) / 10d;
            rightY = Math.round(rightY * 10) / 10d;
        }

        HardwareMap.rightFront.set(rightY);
        HardwareMap.rightBack.set(rightY);
        HardwareMap.leftBack.set(leftY);
        HardwareMap.leftFront.set(leftY);

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
            HardwareMap.ballRetreiver.set(0);
        }

        if (ballRetrieve) {
            HardwareMap.ballRetreiver.set(ball_retriver_speed);
        } else if (ballRetrieveBack) {
            HardwareMap.ballRetreiver.set(-ball_retriver_speed);
        } else {
            HardwareMap.ballRetreiver.set(0);
        }

        if (gamepad2.rightJoystick().Y() >= 0.0025 || gamepad2.rightJoystick().Y() <= -0.0025) {
            HardwareMap.armMotor.set(clip(Math.tanh(-gamepad2.rightJoystick().Y()), -1d, 1d));
        } else {
            HardwareMap.armMotor.set(0);
        }

        if (gamepad2.isAPressed()) {
            HardwareMap.winchMotor.set(-1);
        } else if (gamepad2.isBPressed()) {
            HardwareMap.winchMotor.set(1);
        } else {
            HardwareMap.winchMotor.set(0);
        }


    }

    private static double clip(double x, double min, double max) {
        return Math.min(max, Math.max(x, min));
    }

    public void stopMode() {
        stopped = true;
    }

    public void startMode() {
        stopped = false;
    }

    public boolean waitUntilStopped() throws InterruptedException {
        while (!isStopped) {
            Thread.sleep(20);
        }

        return isStopped;
    }
}
