package io.yedox.imagine3d.util;

public class Ticker {
    private int ticks;
    private int maxTicks;
    private int tpc;

    public Ticker(int maxTicks, int ticksPerCall) {
        this.tpc = ticksPerCall;
        this.maxTicks = maxTicks;
    }

    public void tick() {
        if(!(this.ticks > this.maxTicks))
            this.ticks += tpc;
    }

    public int getTicks() {
        return ticks;
    }

    public int getTps() {
        return tpc;
    }

    public void setTps(int tps) {
        this.tpc = tps;
    }
}
