package net.lyof.phantasm.entity.animations;


import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class ModAnimations {
    public static class Behemoth {
        public static final AnimationDefinition USING_TONGUE = AnimationDefinition.Builder.withLength(0.5f)
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.0833f, KeyframeAnimations.degreeVec(-60.0f, 0.0f, 0.0f),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167f, KeyframeAnimations.degreeVec(-60.0f, 0.0f, 0.0f),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                                AnimationChannel.Interpolations.LINEAR)))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
                        new Keyframe(0.0f, KeyframeAnimations.degreeVec(1.0f, 1.0f, 1.0f),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.0833f, KeyframeAnimations.degreeVec(0.998f, 1.0f, 1.0f),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167f, KeyframeAnimations.degreeVec(0.998f, 1.0f, 1.0f),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5f, KeyframeAnimations.degreeVec(1.0f, 1.0f, 1.0f),
                                AnimationChannel.Interpolations.LINEAR))).build();

        //public static final Animation WALKING = AnimationDefinition.Builder.create(1).addBoneAnimation("body", ).looping().build();
        public static final AnimationDefinition SLEEPING = AnimationDefinition.Builder.withLength(1)
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE,
                        new Keyframe(0, KeyframeAnimations.scaleVec(1, 1, 1),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25f, KeyframeAnimations.scaleVec(1, 1, 0),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5f, KeyframeAnimations.scaleVec(1, 1, 1),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeAnimations.scaleVec(1, 1, 2),
                                AnimationChannel.Interpolations.LINEAR)
                )).looping().build();
    }
}
