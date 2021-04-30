package io.yedox.imagine3d.utils.animations;

public class LinearAnimation {
    private final float end;
    private final AnimationType animationType;
    public boolean resetIfAnimationEnds;
    private float start;
    private float oldStart;
    private float increment;

    public LinearAnimation(float start, float end, float increment, boolean resetIfAnimationEnds, AnimationType animationType) {
        this.start = start;
        this.oldStart = start;
        this.end = end;
        this.increment = increment;
        this.animationType = animationType;
        this.resetIfAnimationEnds = resetIfAnimationEnds;
    }

    public float getEnd() {
        return this.end;
    }

    public float getStart() {
        return this.start;
    }

    public float getIncrement() {
        return this.increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public float getValue() {
        return this.start;
    }

    public void setValue(int value) {
        this.start = value;
    }

    public void reset() {
        start = oldStart;
    }

    public void animate() throws EnumConstantNotPresentException {
        switch (this.animationType) {
            case INCREMENT:
                if (this.start < this.end)
                    this.start += this.increment;
                break;
            case DECREMENT:
                if (this.end < this.start)
                    this.start -= this.increment;
                break;
            case DIVIDE:
                if (this.start < this.end)
                    this.start /= this.increment;
                break;
            case MULTIPLY:
                if (this.start < this.end)
                    this.start *= this.increment;
                break;
            default:
                throw new EnumConstantNotPresentException(AnimationType.class, "Oops! You found an animation type that doesn't exist!");
        }

        if (resetIfAnimationEnds)
            if (start >= end)
                start = oldStart;
    }
}
