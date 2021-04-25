package io.yedox.imagine3d.utils.animations;

import processing.core.PApplet;

public class EasingAnimation {
    public float start;
    public int oldStart;
    public int end;
    public float modifier;

    public EasingAnimation(int start, int end, int modifier) {
        this.start = start;
        this.oldStart = start;
        this.end = end;
        this.modifier = modifier;
    }

    public void reset() {
        this.start = this.oldStart;
    }
    public float getValue() {
        return this.start;
    }

    public void animate() {
        this.start = (int) PApplet.lerp(this.oldStart, this.end, this.modifier);
    }
}
