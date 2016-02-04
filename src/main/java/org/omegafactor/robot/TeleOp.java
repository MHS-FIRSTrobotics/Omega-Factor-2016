package org.omegafactor.robot;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import org.omegafactor.robot.hardware.internal.F130Gamepad;


class TeleOp extends AbstractExecutionThreadService implements CoreRobotService {
    F130Gamepad joystick = new F130Gamepad(new Joystick(0));
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
//                if (joystick.getAxisCount() != 6) {
//                    throw new IllegalStateException("The Controller is using an incorrect amount of axises," +
//                            " please verify the the F130 mode is set to D");
//                }
            while (!stopped) {
                //System.out.println("Number of axises" + joystick.getAxisCount());
                //joystick.getRawAxis()

                double rightY;
                double leftY;
                if (joystick.backLeft()) {
                    System.out.print("Inverted Mode");
                    leftY = -joystick.leftY();
                    rightY = -joystick.rightY();
                } else {
                    leftY = joystick.leftY();
                    rightY = joystick.rightY();
                }

                if (joystick.leftTrigger() > .5) {
                    leftY = joystick.leftY() + joystick.leftX();
                    rightY = joystick.leftY() - joystick.rightX();

                    double speed = Math.sqrt(joystick.rightX() * joystick.rightX() + joystick.rightY() * joystick.rightY());
                    leftY *= speed;
                    rightY *= speed;
                    leftY = Math.min(Math.max(leftY, -1), 1);
                    rightY = Math.min(Math.max(rightY, -1), 1);
                }

                if (joystick.rightTrigger() > .15 && joystick.rightTrigger() <= .8) {
                    leftY /= 5d;
                    rightY /= 5d;
                } else if (joystick.rightTrigger() <= .15) {
                    leftY /= 2d;
                    rightY /= 2d;
                }

                if (joystick.backRight()) {
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
                    if (joystick.a() && !joystick.b()) {
                        ballRetrieve = !ballRetrieve;
                        ballRetrieveBack = false;
                    } else if (joystick.b() && !joystick.a()) {
                        ballRetrieveBack = !ballRetrieveBack;
                        ballRetrieve = false;
                    }
                }

                neitherPressed = !(joystick.a() || joystick.b());

                if (joystick.x()) {
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
//
//                if (joystick.a()) {
//                    HardwareMap.ballRetreiver.set(ball_retriver_speed); //change reverse value accordingly
//                } else if (joystick.b()) {
//                    HardwareMap.ballRetreiver.set(-ball_retriver_speed);
//                } else {
//                    HardwareMap.ballRetreiver.set(0);
//                }
            }
            isStopped = true;
        }
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
