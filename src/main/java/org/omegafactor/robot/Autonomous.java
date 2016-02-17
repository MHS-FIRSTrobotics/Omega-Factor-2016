package org.omegafactor.robot;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkArgument;

public class Autonomous extends AbstractExecutionThreadService {
    private boolean start;
    private boolean stop;
    private final String DEFAULT_AUTO = "TEST";
    private LinkedHashMap<String, Class<?>> autoModeMap;
    private final SendableChooser sendableChooser;
    private final ExecutorService autoExecutor;
    private String currentName;
    private NavX navX;
    private TeleOp teleOpInstance;

    public Autonomous(TeleOp teleOpInstance) {
        this.teleOpInstance = teleOpInstance;
        sendableChooser = new SendableChooser();
        autoExecutor = Executors.newSingleThreadExecutor();
        navX = new NavX();
    }

    public void startUp() {
        autoModeMap = new LinkedHashMap<>();

        List<Class<?>> classList = Arrays.asList(this.getClass().getDeclaredClasses());
        for (Class klazz :                classList) {
            if (klazz.isAnnotationPresent(Disabled.class)) {
                continue;
            }

            if (klazz.isAnnotationPresent(AutoMode.class)) {
                final AutoMode annotation = (AutoMode) klazz.getAnnotation(AutoMode.class);
                String name;
                if (annotation.value().isEmpty()) {
                    name = klazz.getSimpleName();
                } else {
                    name = annotation.value();
                }

                autoModeMap.put(name, klazz);
            }
        }

        if (autoModeMap.containsKey(DEFAULT_AUTO)) {
            sendableChooser.addDefault(DEFAULT_AUTO, autoModeMap.get(DEFAULT_AUTO));
        }

        for (Map.Entry<String, Class<?>> opModeEntry : autoModeMap.entrySet()) {
            if (!opModeEntry.getKey().equals(DEFAULT_AUTO)) {
                sendableChooser.addObject(opModeEntry.getKey(), opModeEntry.getValue());
            }
        }
        SmartDashboard.putData("Autonomous Mode:", sendableChooser);
    }

    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            while (!start && isRunning()) {
                if (!isRunning()) {
                    return;
                }
                this.wait();
            }


            Class<?> selected = (Class<?>) sendableChooser.getSelected();
            final AutoRunnable runnable = (AutoRunnable) selected.newInstance();

            autoExecutor.submit((Runnable) runnable::run);


            while (isRunning()) {
                if (stop) {
                    start = false;
                    runnable.stop();
                    break;
                }
                Thread.yield();
            }
        }
    }

    public synchronized void stopActive() {
        stop = true;
        start = false;
        this.notifyAll();
    }

    public synchronized void startActive() {
        start = true;
        stop = false;
        this.notifyAll();
    }

    @Override
    public void shutDown() {
        autoExecutor.shutdown();
    }

    @AutoMode(DEFAULT_AUTO)
    public class Test implements AutoRunnable {
        @Override
        public void run() {
            driveBase(1);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           driveBase(0);
        }

        @Override

        public void stop() {
            Thread.currentThread().interrupt();
        }
    }

    @AutoMode("Drive Backwards")
    public class DriveBackwards implements AutoRunnable {

        @Override
        public void run() {
            driveBase(-1);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           driveBase(0);
        }

        @Override
        public void stop() {
            Thread.currentThread().interrupt();
        }
    }

    @AutoMode("Stop on Collision")
    public class StopOnCollision implements AutoRunnable {
        boolean collision;
        @Override
        public void run() {
            navX.configureNavXCollisionDetectionCallback(navX1 -> {
                collision = true;
                return false;
            });
            while (!Thread.currentThread().isInterrupted() && !collision) {
                driveBase(.5);
            }

            driveBase(0);
        }

        @Override
        public void stop() {
            Thread.currentThread().interrupt();
        }
    }

    @AutoMode("Record Autonomous")
    public class RecordAuto implements AutoRunnable {
        boolean stop = false;
        @Override
        public void run() {
            boolean waitForVerify = true;
            System.out.println("Press X on gamepad 1");
            while (waitForVerify) {
                waitForVerify = !HardwareMap.gamepad1.isXPressed();
                Thread.yield();

                if (stop) {
                    return;
                }
            }

            waitForVerify = true;
            System.out.println("Press Y on gamepad 2");
            while (waitForVerify) {
                waitForVerify = !HardwareMap.gamepad2.isYPressed();
                Thread.yield();

                if (stop) {
                    return;
                }
            }

            System.out.println("Starting Up Recorder");
            HardwareMap.gamepad1.startRecording("AUTO_GP1");
            HardwareMap.gamepad2.startRecording("AUTO_GP2");
            try {
                while (!stop) {
                    teleOpInstance.loopTeleOp();
                }
            } finally {
                HardwareMap.gamepad1.stopRecording();
                HardwareMap.gamepad2.stopRecording();
            }
        }

        @Override
        public void stop() {
            stop = true;
        }
    }

    static void driveBase(double speed) {
        checkArgument(speed >= -1 && speed <= 1, "The drive speed of " + speed + " is not between -1 and 1.");
        HardwareMap.leftBack.set(speed);
        HardwareMap.rightFront.set(speed);
        HardwareMap.leftFront.set(speed);
        HardwareMap.rightBack.set(speed);
    }

    static void stopRobot() {
        driveBase(0);
    }
}
