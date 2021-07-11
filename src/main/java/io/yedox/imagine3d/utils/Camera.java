package io.yedox.imagine3d.utils;

import com.jogamp.newt.opengl.GLWindow;
import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.entity.Entity;
import io.yedox.imagine3d.gui.GUI;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.awt.*;
import java.util.HashMap;

public class Camera extends Entity {
    protected final PApplet applet;
    private final PVector up;
    private final HashMap<Character, Boolean> keys;
    public float fovy;
    public float speed;
    public float sensitivity;
    public float pan;
    public float tilt;
    public float friction;
    public PVector blockPosition;
    public PVector position;
    public PVector velocity;
    public PVector center;
    public Robot robot;
    public GLWindow window;
    protected int var1;
    protected int var2;
    protected PImage skyBoxTexture;
    protected PImage skyBoxTopTexture;
    protected PImage skyBoxBottomTexture;
    private boolean controllable;
    private PVector right;
    private PVector forward;
    private Point mouse;
    private Point prevMouse;
    private PVector forwardMovement;

    public Camera(PApplet var1) {
        this.applet = var1;

        var1.registerMethod("keyEvent", this);
//        var1.registerMethod("draw", this);

        try {
            this.robot = new Robot();
        } catch (Exception var3) {
            Utils.printExceptionMessage(var3, applet);
        }

        this.controllable = true;
        this.speed = 0.1F;
        this.sensitivity = 0.5F;
        this.position = new PVector(0.0F, 0.0F, 0.0F);
        this.up = new PVector(0.0F, 1.0F, 0.0F);
        this.right = new PVector(1.0F, 0.0F, 0.0F);
        this.forward = new PVector(0.0F, 0.0F, 1.0F);
        this.velocity = new PVector(0.0F, 0.0F, 0.0F);
        this.pan = 1.0F;
        this.tilt = 3.0F;
        this.friction = 0.75F;
        this.fovy = 1.0471976F;
        this.keys = new HashMap();

        this.skyBoxTexture = applet.loadImage("textures/gui/skybox.png");
        this.skyBoxTopTexture = applet.loadImage("textures/gui/skybox_top.png");
        this.skyBoxBottomTexture = applet.loadImage("textures/gui/skybox_bottom.png");

        var1.perspective(fovy, (float) var1.width / (float) var1.height, 0.00001F, 10000.0F);
    }

    public void resetFov() {
        fovy = 1.0471976F;
    }

    public void resetSensitivity() {
        sensitivity = 0.5F;
    }

    public void renderSkybox() {
        applet.push();

        if (GUI.lightsEnabled) {
            applet.noLights();
            applet.resetShader();
        }

        applet.rotateY(-pan);
        applet.translate(position.x, position.y, position.z);
        applet.scale(100000);
        drawSkyboxCube();
        applet.scale(1);
        applet.fill(255);

        if (GUI.lightsEnabled) {
            applet.lights();
        }

        applet.pop();
    }

    private void drawSkyboxCube() {
        applet.beginShape(PConstants.QUADS);

        // Use image coords for mapping
        applet.textureMode(applet.IMAGE);

        applet.texture(skyBoxTexture);

        // +Z "front" face
        applet.vertex(-1, -1, 1, 0, 0);
        applet.vertex(1, -1, 1, 128, 0);
        applet.vertex(1, 1, 1, 128, 128);
        applet.vertex(-1, 1, 1, 0, 128);

        // -Z "back" face
        applet.vertex(1, -1, -1, 0, 0);
        applet.vertex(-1, -1, -1, 128, 0);
        applet.vertex(-1, 1, -1, 128, 128);
        applet.vertex(1, 1, -1, 0, 128);

        applet.texture(skyBoxBottomTexture);

        // +Y "bottom" face
        applet.vertex(-1, 1, 1, -42, -42);
        applet.vertex(1, 1, 1, 0, -42);
        applet.vertex(1, 1, -1, 0, 0);
        applet.vertex(-1, 1, -1, -42, 0);

        applet.texture(skyBoxTopTexture);

        // -Y "top" face
        applet.vertex(-1, -1, -1, 0, 0);
        applet.vertex(1, -1, -1, 1, 0);
        applet.vertex(1, -1, 1, 1, 1);
        applet.vertex(-1, -1, 1, 0, 1);

        applet.texture(skyBoxTexture);

        // +X "right" face
        applet.vertex(1, -1, 1, 0, 0);
        applet.vertex(1, -1, -1, 128, 0);
        applet.vertex(1, 1, -1, 128, 128);
        applet.vertex(1, 1, 1, 0, 128);

        // -X "left" face
        applet.vertex(-1, -1, -1, 0, 0);
        applet.vertex(-1, -1, 1, 128, 0);
        applet.vertex(-1, 1, 1, 128, 128);
        applet.vertex(-1, 1, -1, 0, 128);

        // Reset textureMode
        applet.textureMode(applet.NORMAL);

        applet.endShape();
    }

    public void render() {
        if (Game.getCurrentScreen().equals(Game.Screen.MAIN_GAME_SCREEN)) {
            if (this.controllable) {
                window = (GLWindow) applet.getSurface().getNative();

                int a = 150;
                this.mouse = MouseInfo.getPointerInfo().getLocation();
                if (this.prevMouse == null) {
                    this.prevMouse = new Point(this.mouse.x, this.mouse.y);
                }

                var1 = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
                var2 = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;

                if (applet.focused && !GUI.chatBox.visible && !GUI.pauseScreen) {
                    if (this.mouse.x < window.getX() + a && this.mouse.x - this.prevMouse.x < 0) {
                        this.robot.mouseMove(window.getX() + applet.width - a, this.mouse.y);
                        this.mouse.x = window.getX() + applet.width - a;
                        this.prevMouse.x = window.getX() + applet.width - a;
                    }

                    if (this.mouse.x > window.getX() + applet.width - a && this.mouse.x - this.prevMouse.x > 0) {
                        this.robot.mouseMove(window.getX() + a, this.mouse.y);
                        this.mouse.x = window.getX() + a;
                        this.prevMouse.x = window.getX() + a;
                    }

                    if (this.mouse.y < window.getY() + a && this.mouse.y - this.prevMouse.y < 0) {
                        this.robot.mouseMove(this.mouse.x, window.getY() + applet.height - a);
                        this.mouse.y = window.getY() + applet.height - a;
                        this.prevMouse.y = window.getY() + applet.height - a;
                    }

                    if (this.mouse.y > window.getY() + applet.height - a && this.mouse.y - this.prevMouse.y > 0) {
                        this.robot.mouseMove(this.mouse.x, window.getY() + a);
                        this.mouse.y = window.getY() + a;
                        this.prevMouse.y = window.getY() + a;
                    }


                    this.pan += PApplet.map((float) (this.mouse.x - this.prevMouse.x), 0.0F, (float) this.applet.width, 0.0F, 6.2831855F) * this.sensitivity;
                    this.tilt += PApplet.map((float) (this.mouse.y - this.prevMouse.y), 0.0F, (float) this.applet.height, 0.0F, 3.1415927F) * this.sensitivity;
                    this.tilt = this.clamp(this.tilt, -1.5629815F, 1.5629815F);
                    if (this.tilt == 1.5707964F) {
                        this.tilt += 0.001F;
                    }

                    this.forward = new PVector(PApplet.cos(this.pan), PApplet.tan(this.tilt), PApplet.sin(this.pan));
                    this.forward.normalize();

                    this.forwardMovement = forward.copy();
                    this.forwardMovement.y = 0.0f;

                    this.right = new PVector(PApplet.cos(this.pan - 1.5707964F), 0.0F, PApplet.sin(this.pan - 1.5707964F));
                    this.prevMouse = new Point(this.mouse.x, this.mouse.y);
                    if (this.keys.containsKey('a') && this.keys.get('a')) {
                        this.velocity.add(PVector.mult(this.right, this.speed));
                    }

                    if (this.keys.containsKey('d') && this.keys.get('d')) {
                        this.velocity.sub(PVector.mult(this.right, this.speed));
                    }

                    if (this.keys.containsKey('w') && this.keys.get('w')) {
                        this.velocity.add(PVector.mult(this.forwardMovement, this.speed));
                    }

                    if (this.keys.containsKey('s') && this.keys.get('s')) {
                        this.velocity.sub(PVector.mult(this.forwardMovement, this.speed));
                    }

                    if (GUI.player.observerMode) {
                        if (this.keys.containsKey('q') && this.keys.get('q')) {
                            this.velocity.add(PVector.mult(this.up, this.speed));
                        }

                        if (this.keys.containsKey('e') && this.keys.get('e')) {
                            this.velocity.sub(PVector.mult(this.up, this.speed));
                        }
                    }

                    this.velocity.mult(this.friction);
                    this.position.add(this.velocity);
                    this.center = PVector.add(this.position, this.forward);
                }
            }
            this.applet.camera(this.position.x, this.position.y, this.position.z, this.center.x, this.center.y, this.center.z, this.up.x, this.up.y, this.up.z);
            this.blockPosition = BlockUtils.getBlockCoords(this.position);
        }
    }

    public void keyEvent(KeyEvent var1) {
        char var2 = var1.getKey();
        switch (var1.getAction()) {
            case 1:
                this.keys.put(Character.toLowerCase(var2), true);
                break;
            case 2:
                this.keys.put(Character.toLowerCase(var2), false);
        }

    }

    private float clamp(float var1, float var2, float var3) {
        if (var1 > var3) {
            return var3;
        } else {
            return var1 < var2 ? var2 : var1;
        }
    }

    public PVector getForward() {
        return this.forward;
    }

    public PVector getUp() {
        return this.up;
    }

    public PVector getRight() {
        return this.right;
    }

    public boolean isControllable() {
        return this.controllable;
    }

    public void setControllable(boolean bool) {
        this.controllable = bool;
    }
}
