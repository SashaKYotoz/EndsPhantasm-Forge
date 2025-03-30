package net.lyof.phantasm.entities.animations;

public enum BehemothAnimation {
    IDLE(-1),
    WALKING(-1),
    SLEEPING(-1),
    WAKING_UP(10),
    WAKING_DOWN(20);

    public int maxTime;

    BehemothAnimation(int maxTime) {
        this.maxTime = maxTime;
    }
}
