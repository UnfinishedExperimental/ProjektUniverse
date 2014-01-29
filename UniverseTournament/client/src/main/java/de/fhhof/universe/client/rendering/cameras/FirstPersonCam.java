/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.cameras;

import darwin.util.math.composits.ViewMatrix;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;

/**
 * Die CameraController Klasse dient dazu dem GLCanvas eine Kamerasteuerung
 * mittels Maus zu erm�gliche. Es kann die der Kamera zugeh�rigen ModelView
 * Matrix berechnet werden
 *
 * @author Daniel Heinrich
 */
@Deprecated
public class FirstPersonCam implements Camera, KeyListener, MouseMotionListener, GLEventListener {

    private static final int forward = KeyEvent.VK_W;
    private static final int backward = KeyEvent.VK_S;
    private static final int leftward = KeyEvent.VK_A;
    private static final int rightward = KeyEvent.VK_D;
    private static final int space = KeyEvent.VK_SPACE;
    private static final int shift = KeyEvent.VK_SHIFT;
    private static final int mouseSpeed = 1;
    private static final int keySpeed = 3;
    private static final int rotVMax = 1;
    private static final int rotVMin = -1;
    private final Map<Integer, Boolean> keys = new ConcurrentHashMap<Integer, Boolean>();
    private final float[] modelview = new float[16];
    private final GLCanvas canvas;
    private final Robot robot;
    private float rotX, rotY, rotZ, rotV;
    private float posX, posY, posZ;
    private int centerX, centerY;
    private long lastTime = -1;

    // a very simple first person shooter style camera
    public FirstPersonCam(final GLCanvas canvas) {

        // we need the robot to get full control over the mouse
        Robot r = null;
        try {

            r = new Robot();

        } catch (final AWTException e) {

            e.printStackTrace();

        }

        // setup the modelview matrix
        for (int i = 0; i < 4; i++) {
            modelview[i * 5] = 1.0f;
        }

        this.canvas = canvas;
        this.robot = r;

        canvas.addKeyListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addGLEventListener(this);

    }

    /**
     * Berrechnet die aktuelle View Matrix aus der aktuellen Kamera Position
     */
    @Override
    public ViewMatrix getViewMatrix() {
        calculatePosition();
        calculateView();
        ViewMatrix viewMatrix = new ViewMatrix();
        viewMatrix.setMat(modelview);
        return viewMatrix;
    }

    private void calculatePosition() {

        if (lastTime == -1) {
            lastTime = System.nanoTime();
        }

        double mul = 1.0;

        Boolean value = null;
        if ((value = keys.get(shift)) != null && value == true) {
            mul = 0.1;
        }

        final double speed = keySpeed * -((lastTime - (lastTime = System.nanoTime())) / 10E7) * mul;

        if ((value = keys.get(forward)) != null && value == true) {

            posX -= Math.sin(rotY) * speed;
            posZ += Math.cos(rotY) * speed;
            posY += rotV * speed;
        }

        if ((value = keys.get(backward)) != null && value == true) {

            posX += Math.sin(rotY) * speed;
            posZ -= Math.cos(rotY) * speed;
            posY -= rotV * speed;
        }

        if ((value = keys.get(leftward)) != null && value == true) {

            posX += Math.cos(rotY) * speed;
            posZ += Math.sin(rotY) * speed;

        }

        if ((value = keys.get(rightward)) != null && value == true) {

            posX -= Math.cos(rotY) * speed;
            posZ -= Math.sin(rotY) * speed;

        }

    }

    private void calculateView() {

        final float sinX = (float) Math.sin(rotX);
        final float sinY = (float) Math.sin(rotY);
        final float sinZ = (float) Math.sin(rotZ);

        final float cosX = (float) Math.cos(rotX);
        final float cosY = (float) Math.cos(rotY);
        final float cosZ = (float) Math.cos(rotZ);

        modelview[0] = cosY * cosZ + sinY * sinX * sinZ;
        modelview[1] = cosX * sinZ;
        modelview[2] = -sinY * cosZ + cosY * sinX * sinZ;
        modelview[4] = -cosY * sinZ + sinY * sinX * cosZ;
        modelview[5] = cosX * cosZ;
        modelview[6] = sinY * sinZ + cosY * sinX * cosZ;
        modelview[8] = sinY * cosX;
        modelview[9] = -sinX;
        modelview[10] = cosY * cosX;

        modelview[12] = modelview[0] * posX + modelview[4] * posY + modelview[8] * posZ;
        modelview[13] = modelview[1] * posX + modelview[5] * posY + modelview[9] * posZ;
        modelview[14] = modelview[2] * posX + modelview[6] * posY + modelview[10] * posZ;

    }

    /**
     * Setzt die Kamera an eine bestimmte Position
     */
    public void setPosition(float x, float y, float z) {
        posX = x;
        posY = y;
        posZ = z;
    }

    @Override
    public void init(final GLAutoDrawable drawable) {
    }

    /**
     * Daf�r zust�ndig den Mittelpunkt des Canvas, nach einer �nderung dessen
     * gr��e zu berechnen. Um den Mauszeiger korrekt zur�cksetzten zu k�nnen
     */
    @Override
    public void reshape(final GLAutoDrawable drawable, final int x, final int y, final int width, final int height) {
        rotV = 0;
        final Rectangle r = canvas.getParent().getBounds();
        final Point p = canvas.getParent().getLocationOnScreen();

        centerX = r.x + p.x + width / 2;
        centerY = r.y + p.y + height / 2;
        Boolean value = null;
        if ((value = keys.get(space)) != null && value == true) {
            if (robot != null) {
                robot.mouseMove(centerX, centerY);
            }
        }
    }

    /**
     * Berrechnet den neuen Viewing Angle
     * @param e
     */
    @Override
    public void mouseMoved(final MouseEvent e) {

        Boolean value = null;
        if ((value = keys.get(space)) != null && value == true) {
            rotY -= (centerX - e.getXOnScreen()) / 1000.0 * mouseSpeed;
            rotV -= (centerY - e.getYOnScreen()) / 1000.0 * mouseSpeed;

            if (rotV > rotVMax) {
                rotV = rotVMax;
            }

            if (rotV < rotVMin) {
                rotV = rotVMin;
            }

            rotX = (float) (Math.cos(rotY) * rotV);
            rotZ = (float) (Math.sin(rotY) * rotV);

            if (robot != null) {
                robot.mouseMove(centerX, centerY);
            }
        }

    }

    @Override
    public void mouseDragged(final MouseEvent e) {

        mouseMoved(e);

    }

    @Override
    public void keyPressed(final KeyEvent e) {

        keys.put(e.getKeyCode(), true);

    }

    @Override
    public void keyReleased(final KeyEvent e) {

        keys.put(e.getKeyCode(), false);

    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void display(final GLAutoDrawable drawable) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }
}
