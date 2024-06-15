package dev.zspacehack.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.explosion.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CombatUtil implements Wrapper {
    public static List<PlayerEntity> getEnemies(double range) {
        List<PlayerEntity> list = new ArrayList<>();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (mc.player.distanceTo(player) > range) continue;
            list.add(player);
        }
        return list;
    }
    public static Vec3d getEntityPosVec(PlayerEntity entity, int ticks) {
        return entity.getPos().add(getMotionVec(entity, ticks, true));
    }

    public static Vec3d getMotionVec(Entity entity, int ticks, boolean collision) {
        double dX = entity.getX() - entity.prevX;
        double dY = entity.getY() - entity.prevY;
        double dZ = entity.getZ() - entity.prevZ;
        double entityMotionPosX = 0;
        double entityMotionPosY = 0;
        double entityMotionPosZ = 0;
        if (collision) {
            for (double i = 1; i <= ticks; i = i + 0.5) {
                if (!mc.world.canCollide(entity, entity.getBoundingBox().offset(new Vec3d(dX * i, 0, dZ * i)))) {
                    entityMotionPosX = dX * i;
                    entityMotionPosZ = dZ * i;
                } else {
                    break;
                }
            }
            for (double i = 1; i <= 3; i = i + 0.5) {
                if (!mc.world.canCollide(entity, entity.getBoundingBox().offset(new Vec3d(0, dY * i, 0)))) {
                    entityMotionPosY = dY * i;
                } else {
                    break;
                }
            }
        } else {
            entityMotionPosX = dX * ticks;
            entityMotionPosY = dY * ticks;
            entityMotionPosZ = dZ * ticks;
        }

        return new Vec3d(entityMotionPosX, entityMotionPosY, entityMotionPosZ);
    }
    public static boolean terrainIgnore = false;
    public static BlockPos anchorIgnore = null;

    public static float getAnchorDamage(BlockPos anchorPos, PlayerEntity target, int predictTicks, boolean terrainIgnore2) {
        float final_result;
        anchorIgnore = anchorPos;
        terrainIgnore = terrainIgnore2;
        if(predictTicks <= 0) {
            final_result = getDamage(anchorPos.toCenterPos(), target);
        } else {
            PlayerEntity copyEntity = new PlayerEntity(mc.world, target.getBlockPos(), target.getYaw(), new GameProfile(UUID.fromString("66123666-6666-6666-6666-667563866600"), "PredictEntity228")) {
                @Override public boolean isSpectator() {
                    return false;
                }
                @Override public boolean isCreative() {
                    return false;
                }
            };
            copyEntity.setPosition(getEntityPosVec(target, predictTicks));
            final_result = getDamagePredict(anchorPos.toCenterPos(), target, copyEntity);
        }
        anchorIgnore = null;
        terrainIgnore = false;
        return final_result;
    }

    public static float getDamagePredict(Vec3d explosionPos, PlayerEntity target, PlayerEntity predict) {
        if (mc.world.getDifficulty() == Difficulty.PEACEFUL) return 0f;

        Explosion explosion = new Explosion(mc.world, null, explosionPos.x, explosionPos.y, explosionPos.z, 6f, false, Explosion.DestructionType.DESTROY);
        if (!new Box(
                MathHelper.floor(explosionPos.x - 11d),
                MathHelper.floor(explosionPos.y - 11d),
                MathHelper.floor(explosionPos.z - 11d),
                MathHelper.floor(explosionPos.x + 13d),
                MathHelper.floor(explosionPos.y + 13d),
                MathHelper.floor(explosionPos.z + 13d)).intersects(predict.getBoundingBox())
        ) {
            return 0f;
        }

        if (!target.isImmuneToExplosion() && !target.isInvulnerable()) {
            double distExposure = MathHelper.sqrt((float) predict.squaredDistanceTo(explosionPos)) / 12d;
            if (distExposure <= 1.0) {
                double xDiff = predict.getX() - explosionPos.x;
                double yDiff = predict.getY() - explosionPos.y;
                double zDiff = predict.getX() - explosionPos.z;
                double diff = MathHelper.sqrt((float) (xDiff * xDiff + yDiff * yDiff + zDiff * zDiff));
                if (diff != 0.0) {
                    double exposure = Explosion.getExposure(explosionPos, predict);
                    double finalExposure = (1.0 - distExposure) * exposure;

                    float toDamage = (float) Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12d + 1.0);

                    if (mc.world.getDifficulty() == Difficulty.EASY) {
                        toDamage = Math.min(toDamage / 2f + 1f, toDamage);
                    } else if (mc.world.getDifficulty() == Difficulty.HARD) {
                        toDamage = toDamage * 3f / 2f;
                    }

                    toDamage = DamageUtil.getDamageLeft(toDamage, target.getArmor(), (float) Objects.requireNonNull(target.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).getValue());

                    if (target.hasStatusEffect(StatusEffects.RESISTANCE)) {
                        int resistance = 25 - (Objects.requireNonNull(target.getStatusEffect(StatusEffects.RESISTANCE)).getAmplifier() + 1) * 5;
                        float resistance_1 = toDamage * resistance;
                        toDamage = Math.max(resistance_1 / 25f, 0f);
                    }

                    if (toDamage <= 0f) {
                        toDamage = 0f;
                    } else {
                        int protAmount = EnchantmentHelper.getProtectionAmount(target.getArmorItems(), explosion.getDamageSource());
                        if (protAmount > 0) {
                            toDamage = DamageUtil.getInflictedDamage(toDamage, protAmount);
                        }
                    }
                    return toDamage;
                }
            }
        }
        return 0f;
    }
    public static float getDamage(Vec3d explosionPos, PlayerEntity target) {
        try {
            if (mc.world.getDifficulty() == Difficulty.PEACEFUL) return 0f;

            Explosion explosion = new Explosion(mc.world, null, explosionPos.x, explosionPos.y, explosionPos.z, 6f, false, Explosion.DestructionType.DESTROY);

            double maxDist = 12;
            if (!new Box(MathHelper.floor(explosionPos.x - maxDist - 1.0), MathHelper.floor(explosionPos.y - maxDist - 1.0), MathHelper.floor(explosionPos.z - maxDist - 1.0), MathHelper.floor(explosionPos.x + maxDist + 1.0), MathHelper.floor(explosionPos.y + maxDist + 1.0), MathHelper.floor(explosionPos.z + maxDist + 1.0)).intersects(target.getBoundingBox())) {
                return 0f;
            }

            if (!target.isImmuneToExplosion() && !target.isInvulnerable()) {
                double distExposure = MathHelper.sqrt((float) target.squaredDistanceTo(explosionPos)) / maxDist;
                if (distExposure <= 1.0) {
                    double xDiff = target.getX() - explosionPos.x;
                    double yDiff = target.getY() - explosionPos.y;
                    double zDiff = target.getX() - explosionPos.z;
                    double diff = MathHelper.sqrt((float) (xDiff * xDiff + yDiff * yDiff + zDiff * zDiff));
                    if (diff != 0.0) {
                        double exposure = Explosion.getExposure(explosionPos, target);
                        double finalExposure = (1.0 - distExposure) * exposure;

                        float toDamage = (float) Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * maxDist + 1.0);

                        if (mc.world.getDifficulty() == Difficulty.EASY) {
                            toDamage = Math.min(toDamage / 2f + 1f, toDamage);
                        } else if (mc.world.getDifficulty() == Difficulty.HARD) {
                            toDamage = toDamage * 3f / 2f;
                        }

                        toDamage = DamageUtil.getDamageLeft(toDamage, target.getArmor(), (float) target.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());

                        if (target.hasStatusEffect(StatusEffects.RESISTANCE)) {
                            int resistance = 25 - (target.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
                            float resistance_1 = toDamage * resistance;
                            toDamage = Math.max(resistance_1 / 25f, 0f);
                        }

                        if (toDamage <= 0f) {
                            toDamage = 0f;
                        } else {
                            int protAmount = EnchantmentHelper.getProtectionAmount(target.getArmorItems(), explosion.getDamageSource());
                            if (protAmount > 0) {
                                toDamage = DamageUtil.getInflictedDamage(toDamage, protAmount);
                            }
                        }
                        return toDamage;
                    }
                }
            }
        } catch (Exception e){

        }
        return 0f;
    }
}
