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

public class Autonomous extends AbstractExecutionThreadService {
    private boolean start;
    private boolean stop;
    private final String DEFAULT_AUTO = "TEST";
    private LinkedHashMap<String, Class<?>> autoModeMap;
    private final SendableChooser sendableChooser;
    private final ExecutorService autoExecutor;
    private String currentName;

    public Autonomous() {
        sendableChooser = new SendableChooser();
        autoExecutor = Executors.newSingleThreadExecutor();
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
            HardwareMap.leftBack.set(1);
            HardwareMap.rightFront.set(1);
            HardwareMap.leftFront.set(1);
            HardwareMap.rightBack.set(1);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            HardwareMap.leftBack.set(0);
            HardwareMap.rightFront.set(0);
            HardwareMap.leftFront.set(0);
            HardwareMap.rightBack.set(0);
        }

        @Override
        public void stop() {
            Thread.currentThread().interrupt();
        }
    }
}
