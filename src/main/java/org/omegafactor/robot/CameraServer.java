package org.omegafactor.robot;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class CameraServer  extends AbstractExecutionThreadService {
    private NIVision.Image frame;
    private int session;

    @Override
    public void startUp() throws Exception {
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

        // the camera name (ex "cam0") can be found through the roborio web interface
        session = NIVision.IMAQdxOpenCamera("cam1",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);
        //NIVision.IMAQdxOpenCamera()

        Thread.currentThread().setPriority(2);
    }

    @Override
    protected void run() throws Exception {
        NIVision.IMAQdxStartAcquisition(session);
        //NIVision.IMA

        while (isRunning()) {
            NIVision.IMAQdxGrab(session, frame, 1);
            //NetworkTable.
            edu.wpi.first.wpilibj.CameraServer.getInstance().setImage(frame);

            Thread.yield();
        }
    }

    @Override
    public void shutDown() {
        NIVision.IMAQdxStopAcquisition(session);
    }
}
