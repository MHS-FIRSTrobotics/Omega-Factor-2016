package org.omegafactor.robot.logger;

import java.io.IOException;
import java.util.logging.*;

public class Logger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static public void setup() throws IOException {

        // get the global logger to configure it
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);


        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler("robot.log");

        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
    }
}
