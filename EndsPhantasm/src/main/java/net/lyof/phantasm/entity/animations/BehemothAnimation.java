package net.lyof.phantasm.entity.animations;

public enum BehemothAnimation {
    IDLE("idle",-1),
    WALKING("walking",-1),
    SLEEPING("sleeping",-1),
    WAKING_UP("waking_up",10),
    WAKING_DOWN("waking_down",20);

    public final String name;
    public final int maxTime;

    BehemothAnimation(String name, int maxTime) {
        this.name = name;
        this.maxTime = maxTime;
    }
    public static BehemothAnimation byName(String name) {
        for (BehemothAnimation anim : values()) {
            if (anim.name.equalsIgnoreCase(name)) {
                return anim;
            }
        }
        return SLEEPING;
    }
}
