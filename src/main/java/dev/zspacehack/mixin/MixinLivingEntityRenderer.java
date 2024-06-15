package dev.zspacehack.mixin;

import dev.zspacehack.ZSpace;
import dev.zspacehack.module.modules.SubEvent;
import dev.zspacehack.module.modules.render.NoRender;
import dev.zspacehack.utils.MathUtil;
import dev.zspacehack.utils.RenderUtils;
import dev.zspacehack.utils.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> {
    @Unique
    private LivingEntity lastEntity;
    @Unique
    private float originalYaw;
    @Unique
    private float originalHeadYaw;
    @Unique
    private float originalBodyYaw;
    @Unique
    private float originalPitch;

    @Unique
    private float originalPrevYaw;
    @Unique
    private float originalPrevHeadYaw;
    @Unique
    private float originalPrevBodyYaw;
    @Inject(method = "render", at = @At("HEAD"))
    public void onRenderPre(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null && livingEntity == MinecraftClient.getInstance().player && ZSpace.HUD.rotations.getValue()) {
            originalYaw = livingEntity.getYaw();
            originalHeadYaw = livingEntity.headYaw;
            originalBodyYaw = livingEntity.bodyYaw;
            originalPitch = livingEntity.getPitch();
            originalPrevYaw = livingEntity.prevYaw;
            originalPrevHeadYaw = livingEntity.prevHeadYaw;
            originalPrevBodyYaw = livingEntity.prevBodyYaw;

            livingEntity.setYaw(SubEvent.getRenderYawOffset());
            livingEntity.headYaw = SubEvent.getRotationYawHead();
            livingEntity.bodyYaw = SubEvent.getRenderYawOffset();
            livingEntity.setPitch(SubEvent.getRenderPitch());
            livingEntity.prevYaw = SubEvent.getPrevRenderYawOffset();
            livingEntity.prevHeadYaw = SubEvent.getPrevRotationYawHead();
            livingEntity.prevBodyYaw = SubEvent.getPrevRenderYawOffset();
            livingEntity.prevPitch = SubEvent.getPrevPitch();
        }
        lastEntity = livingEntity;
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void onRenderPost(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null && livingEntity == MinecraftClient.getInstance().player && ZSpace.HUD.rotations.getValue()) {
            livingEntity.setYaw(originalYaw);
            livingEntity.headYaw = originalHeadYaw;
            livingEntity.bodyYaw = originalBodyYaw;
            livingEntity.setPitch(originalPitch);
            livingEntity.prevYaw = originalPrevYaw;
            livingEntity.prevHeadYaw = originalPrevHeadYaw;
            livingEntity.prevBodyYaw = originalPrevBodyYaw;
            livingEntity.prevPitch = originalPitch;
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void onRenderModel(EntityModel entityModel, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        Color newColor = new Color(red, green, blue, alpha);
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.antiPlayerCollision.getValue() && lastEntity != Wrapper.mc.player) {
            float overrideAlpha = (float) (Wrapper.mc.player.squaredDistanceTo(lastEntity.getPos()) / 3f) + 0.2f;
            newColor = RenderUtils.injectAlpha(newColor, (int) (255f * MathUtil.clamp(overrideAlpha, 0f, 1f)));
        }
        entityModel.render(matrices, vertices, light, overlay, newColor.getRed() / 255F, newColor.getGreen() / 255F, newColor.getBlue() / 255F, newColor.getAlpha() / 255F);
    }
}
