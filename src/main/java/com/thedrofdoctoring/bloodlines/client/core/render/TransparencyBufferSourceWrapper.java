package com.thedrofdoctoring.bloodlines.client.core.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;


public class TransparencyBufferSourceWrapper implements MultiBufferSource {

    private final MultiBufferSource original;
    private final float redFactor;
    private final float greenFactor;
    private final float blueFactor;
    private final float alphaFactor;

    public TransparencyBufferSourceWrapper(MultiBufferSource original, float redFactor, float greenFactor, float blueFactor, float alphaFactor) {
        this.original = original;
        this.redFactor = redFactor;
        this.blueFactor = blueFactor;
        this.greenFactor = greenFactor;
        this.alphaFactor = alphaFactor;
    }
    public TransparencyBufferSourceWrapper(MultiBufferSource original, float alphaFactor) {
        this.original = original;
        this.redFactor = 1f;
        this.blueFactor = 1f;
        this.greenFactor = 1f;
        this.alphaFactor = alphaFactor;
    }


    @Override
    public @NotNull VertexConsumer getBuffer(@NotNull RenderType pRenderType) {
        return new TranslucentModifiedVertexWrapper(original.getBuffer(pRenderType), redFactor, greenFactor, blueFactor, alphaFactor);
    }
}
