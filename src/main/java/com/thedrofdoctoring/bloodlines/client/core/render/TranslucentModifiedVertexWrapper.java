package com.thedrofdoctoring.bloodlines.client.core.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

public class TranslucentModifiedVertexWrapper implements VertexConsumer {

    private final VertexConsumer original;
    private final float redFactor;
    private final float greenFactor;
    private final float blueFactor;
    private final float alphaFactor;

    public TranslucentModifiedVertexWrapper(VertexConsumer original, float redFactor, float greenFactor, float blueFactor, float alphaFactor) {
        this.original = original;
        this.alphaFactor = alphaFactor;
        this.blueFactor = blueFactor;
        this.greenFactor = greenFactor;
        this.redFactor = redFactor;
    }

    @Override
    public @NotNull VertexConsumer addVertex(float x, float y, float z) {
        return original.addVertex(x, y, z);
    }


    @Override
    public @NotNull VertexConsumer setColor(int r, int g, int b, int a) {
        return original.setColor((int) (r * redFactor), (int) (g * greenFactor), (int) (b * blueFactor), (int) (a * alphaFactor)
        );
    }
    @Override
    public @NotNull VertexConsumer setUv(float a, float b) {
        return original.setUv(a, b);
    }

    @Override
    public @NotNull VertexConsumer setUv1(int a, int b) {
        return original.setUv1(a, b);
    }

    @Override
    public @NotNull VertexConsumer setUv2(int a, int b) {
        return original.setUv2(a, b);
    }


    @Override
    public @NotNull VertexConsumer setNormal(float a, float b, float c) {
        return original.setNormal(a, b, c);
    }
}
