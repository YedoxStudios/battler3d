package io.yedox.imagine3d.utils.animations;

public class LinearAnimation {
    private final int end;
    private final AnimationType animationType;
    public boolean resetIfAnimationEnds;
    private int start;
    private int oldStart;
    private int increment;

    public LinearAnimation(int start, int end, int increment, boolean resetIfAnimationEnds, AnimationType animationType) {
        this.start = start;
        this.oldStart = start;
        this.end = end;
        this.increment = increment;
        this.animationType = animationType;
        this.resetIfAnimationEnds = resetIfAnimationEnds;
    }

    public int getEnd() {
        return this.end;
    }

    public int getStart() {
        return this.start;
    }

    public int getIncrement() {
        return this.increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getValue() {
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
                if (this.start < this.end)
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
