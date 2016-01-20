///* Copyright � 2016 David Sargent
//* Permission is hereby granted, free of charge, to any person obtaining a copy of this software
//* and associated documentation files (the "Software"), to deal in the Software without restriction,
//* including without limitation  the rights to use, copy, modify, merge, publish, distribute, sublicense,
//* and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
//* the following conditions:
//*
//* The above copyright notice and this permission notice shall be included in all copies or
//* substantial portions of the Software.
//*
//* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
//* BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
//* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//* FROM,OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//*/
//
//package org.omegafactor.robot.hardware;
////
//
///*
// * The extensible Gamepad, based on <code>{@link Gamepad}</code>. This takes care of multiple issues
// * of the design of the original <code>Gamepad</code>, including code readablity, design, and
// * extending the capilabties of what the Gamepad can do.
// *
// * @author David Sargent
// * @since 0.1
// */
//public class ExtensibleGamepad {
//    private final Joystick leftJoystick;
//    private final Joystick rightJoystick;
//    private final Dpad dpad;
//    private boolean a;
//    private boolean b;
//    private boolean x;
//    private boolean y;
//    private boolean guide;
//    private boolean start;
//    private boolean back;
//    private boolean leftBumper;
//    private boolean rightBumper;
//    private double leftTrigger;
//    private double rightTrigger;
//    private long timestamp;
//
//    /*
//     * Setup a basic gamepad; be sure to call {@link ExtensibleGamepad#updateGamepad} to make this
//     * safe to use
//	*/
//    public ExtensibleGamepad() {
//        leftJoystick = new Joystick();
//        rightJoystick = new Joystick();
//        dpad = new Dpad();
//    }
//
//    /*
//     * Updates this to the status of the provided Gamepad (recast this from a <code>{@link
//     * Gamepad}</code>)
//     *
//     * @param ctx the Robot Context (nullable, depending on the given <code>{@link
//     *            JoystickScaler}</code>)
//     * @param gp  the <code>Gamepad</code> to cast into
//     */
//
//    public void updateGamepad(Robocol.Gamepad gp) {
//        a = gp.a;
//        b = gp.b;
//        x = gp.x;
//        y = gp.y;
//
//        guide = gp.guide;
//        start = gp.start;
//        back = gp.back;
//
//        leftBumper = gp.leftBumper;
//        rightBumper = gp.rightBumper;
//
//        leftTrigger = gp.leftTrigger;
//        rightTrigger = gp.rightTrigger;
//
//        timestamp = gp.timestamp;
//
//        getDpad().update(gp.dpad.up, gp.dpad.down, gp.dpad.right, gp.dpad.left);
//
//        final int rightJoystickIndex = 1;
//        rightJoystick().update(gp.joysticks[rightJoystickIndex].x,
//                gp.joysticks[rightJoystickIndex].y, gp.joysticks[rightJoystickIndex].press);
//
//        final int leftJoystickIndex = 0;
//        leftJoystick().update(gp.joysticks[leftJoystickIndex].x,
//                gp.joysticks[leftJoystickIndex].y, gp.joysticks[leftJoystickIndex].press);
//    }
//
//    /*
//     * Gets the status of Button A
//     *
//     * @return is button A pressed
//     */
//    public boolean isAPressed() {
//        return a;
//    }
//
//    /*
//     * Gets the status of Button B
//     *
//     * @return is button B pressed
//     */
//
//    public boolean isBPressed() {
//        return b;
//    }
//
///*
//     * Gets the status of Button X
//     *
//     * @return is button X pressed
//     */
//
//    public boolean isXPressed() {
//        return x;
//    }
//
///*
//     * Gets the status of Button Y
//     *
//     * @return is button Y pressed
//     */
//
//    public boolean isYPressed() {
//        return y;
//    }
//
///*
//     * Gets the status of the guide button
//     *
//     * @return is the Guide button pressed
//     */
//
//    public boolean isGuidePressed() {
//        return guide;
//    }
//
///*
//     * Gets the status of the Start button
//     *
//     * @return is the Start button pressed
//     */
//
//    public boolean isStartPressed() {
//        return start;
//    }
//
///*
//     * Gets the status of the back button
//     *
//     * @return is the Back button pressed
//     */
//
//    public boolean isBackPressed() {
//        return back;
//    }
//
//    /*
//     * Gets the status of the Left Bumper (on the Gamepad)
//     *
//     * @return is the Left Bumper pressed
//     */
//
//    public boolean isLeftBumperPressed() {
//        return leftBumper;
//    }
//
//    /*
//     * Gets the status of the Right Bumper (on the Gamepad)
//     *
//     * @return is the Right Bumper pressed
//     */
//
//    public boolean isRightBumperPressed() {
//        return rightBumper;
//    }
//
//    /*
//     * Gets the value of the Left Trigger (no idea what this is; we are just exposing it)
//     *
//     * @return the value of the Left Trigger
//     */
//    public double getLeftTrigger() {
//        return leftTrigger;
//    }
//
//    /*
//     * Gets the value of the Right Trigger (again no idea what the value means; we are just exposing
//     * it)
//     *
//     * @return the value of the Right Trigger
//     */
//    public double getRightTrigger() {
//        return rightTrigger;
//    }
//
//    /*
//     * The time the joystick was update (in milliseconds) from the FTC SDK
//     *
//     * @return the time in milliseconds of last update
//     */
//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    /*
//     * Gets the left joystick
//     *
//     * @return the left <code> {@link ExtensibleGamepad.Joystick} </code> of this controller
//    */
//    public Joystick leftJoystick() {
//        return leftJoystick;
//    }
//
//    /*
//     * Gets the right joystick
//     *
//     * @return the right <code>Joystick</code> of this controller
//     */
//
//    public Joystick rightJoystick() {
//        return rightJoystick;
//    }
//
//    /**
//     * Gets the D-Pad of this controller
//     *
//     * @return {@link ExtensibleGamepad.Dpad} of this controller
//     */
//
//    public Dpad getDpad() {
//        return dpad;
//    }
//
//    /**
//     * The Joystick for use in {@link ExtensibleGamepad}. This is an object representative of the
//     * data present in the joysticks, and the data that the FTC SDK can give
//     *
//     * @author David Sargent
//     * @since 0.1
//     */
//
//    public class Joystick {
//        private double x;
//        private double y;
//        private boolean pressed;
//
//        /**
//         * Update the joystick to match the parameters given
//         *
//         * @param X         the value of X coordinates, should be between -1 and 1
//         * @param Y         the value of Y coordinates, should be between -1 and 1
//         * @param isPressed is the Joystick being pressed
//         */
//
//        void update(double X, double Y, boolean isPressed) {
//            x = X;
//            y = Y;
//            pressed = isPressed;
//        }
//
//        /**
//         * Gets the X (in Cartesian) of the Joystick. This should range between -1 and 1, but it is
//         * not guaranteed to be so.
//         *
//         * @return the Cartesian X of the Joystick
//         */
//
//        public double X() {
//            return x;
//        }
//
//        /**
//         * Gets the Y (in Cartesian) of the Joystick. This should range between -1 and 1, but it is
//         * not guaranteed to be so.
//         *
//         * @return the Cartesian Y-coordinate of the Joystick
//         */
//
//        public double Y() {
//            return y;
//        }
//
//        /**
//         * Gets the button within the Joystick
//         *
//         * @return is the Joystick button pressed
//         */
//
//        public boolean isPressed() {
//            return pressed;
//        }
//
//        /**
//         * Gets the current coordinates in a Cartesian plane
//         *
//         * @return the current state of the joystick in a Cartesian plane
//         */
//        public CartesianCoordinates cartesian() {
//            return new CartesianCoordinates(x, y);
//        }
//
//        /**
//         * Gets the current coordinates in a Polar plane
//         *
//         * @return the current state of the joystick in a Polar plane
//         */
//
//        public PolarCoordinates polar() {
//            return new PolarCoordinates(cartesian());
//        }
//    }
//
//    /**
//     * An representation of a gamepad's D-Pad control
//     *
//     * @author David Sargent
//     * @since 0.1.0
//     */
//
//    public class Dpad {
//        private boolean up;
//        private boolean down;
//        private boolean right;
//        private boolean left;
//
//        /**
//         * Updates the D-Pad based on the Gamepad's current status
//         *
//         * @param upPressed    is the Up button pressed
//         * @param downPressed  is the Down button pressed
//         * @param rightPressed is the right button pressed
//         * @param leftPressed  is the left button pressed
//         */
//
//        void update(boolean upPressed, boolean downPressed,
//                    boolean rightPressed, boolean leftPressed) {
//            up = upPressed;
//            down = downPressed;
//            right = rightPressed;
//            left = leftPressed;
//        }
//
//        /**
//         * Gets the status of the D-Pad Up button
//         *
//         * @return is the Dpad Up button pressed
//         */
//
//        public boolean isUpPressed() {
//            return up;
//        }
//
//        /**
//         * Gets the status of the D-Pad Down button
//         *
//         * @return is the D-Pad Down button pressed
//         */
//
//        public boolean isDownPressed() {
//            return down;
//        }
//
//        /**
//         * Gets the status of the D-Pad Right button
//         *
//         * @return is the D-Pad Right pressed
//         */
//
//        public boolean isRightPressed() {
//            return right;
//        }
//
///*
//         * Gets the status of the D-Pad Left button
//         *
//         * @return is the D-Pad Left pressed
//         */
//
//        public boolean isLeftPressed() {
//            return left;
//        }
//    }
//}