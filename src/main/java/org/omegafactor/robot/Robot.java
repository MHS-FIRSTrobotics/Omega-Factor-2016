package org.omegafactor.robot;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class Robot extends IterativeRobot {
    private final Talon rightFront;
    private final Talon leftFront;
    private final Talon leftBack;
    private final Talon rightBack;
    private TeleOp teleOp;

    public Robot() {
        super();
        rightFront = new Talon(2);
        rightBack = new Talon(3);
        leftBack = new Talon(1);
        leftFront = new Talon(0);
        teleOp = new TeleOp();
        teleOp.startAsync();
    }

    @Override
    public void robotInit() {
        System.out.print("Hi!");
    }

    @Override
    public void teleopInit() {
        teleOp.startMode();
    }

    @Override
    public void disabledInit() {
        teleOp.stopMode();
        try {
            System.err.print("Waiting for TeleOp shutdown...");
            teleOp.waitUntilStopped();
            System.err.println("Done!");
        } catch (/*Timeout*/Exception e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public void teleopPeriodic() {

    }

    private class TeleOp extends AbstractExecutionThreadService implements CoreRobotService {
        F130Gamepad joystick = new F130Gamepad(new Joystick(0));
        private boolean stopped = true;
        private boolean isStopped = true;

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
                    rightFront.set(joystick.rightY());
                    rightBack.set(joystick.rightY());
                    leftBack.set(joystick.leftY());
                    leftFront.set(joystick.leftY());
                    if (joystick.a()) {
                        System.out.println("Joystick A button pressed");
                    }
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

    private class F130Gamepad {
        private final Joystick joystick;

        private F130Gamepad(@NotNull final Joystick joystick) {
            this.joystick = checkNotNull(joystick);
        }

        public double leftX() {
            return -joystick.getRawAxis(0);
        }

        public double leftY() {
            return -joystick.getRawAxis(1);
        }

        public double leftTrigger() {
            return joystick.getRawAxis(2);
        }

        public double rightTrigger() {
            return joystick.getRawAxis(3);
        }

        public double rightX() {
            return -joystick.getRawAxis(4);
        }

        public double rightY() {
            return -joystick.getRawAxis(5);
        }

        public boolean a() {
            return joystick.getRawButton(1);
        }

        public boolean b() {
            return joystick.getRawButton(2);
        }

        public boolean x() {
            return joystick.getRawButton(3);
        }

        public boolean y() {
            return joystick.getRawButton(4);
        }

        public boolean backLeft() {
            return joystick.getRawButton(5);
        }

        public boolean backRight() {
            return joystick.getRawButton(6);
        }

        public boolean back() {
            return joystick.getRawButton(7);
        }

        public boolean start() {
            return joystick.getRawButton(8);
        }

        public boolean leftJoystickButton() {
            return joystick.getRawButton(9);
        }

        public boolean rightJoystickButton() {
            return joystick.getRawButton(10);
        }

        public void leftRumble(int value) {
            joystick.setRumble(Joystick.RumbleType.kLeftRumble, value);
        }
    }
}

