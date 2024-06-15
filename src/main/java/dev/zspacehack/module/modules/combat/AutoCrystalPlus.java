package dev.zspacehack.module.modules.combat;

import dev.zspacehack.gui.Color;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.EnumSetting;
import dev.zspacehack.settings.Setting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.BlockUtil;
import dev.zspacehack.utils.CombatUtil;
import dev.zspacehack.utils.InventoryUtil;
import dev.zspacehack.utils.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.Difficulty;
import net.minecraft.world.explosion.Explosion;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import static dev.zspacehack.utils.BlockUtil.canPlace;
import static dev.zspacehack.utils.BlockUtil.getLegitRotations;

public class AutoCrystalPlus extends Module {
    public AutoCrystalPlus Instance;
    // Place
    private final BooleanSetting place
            = new BooleanSetting("Place", true);
    private final BooleanSetting spam
            = new BooleanSetting("Spam", false);
    private final BooleanSetting facePlace
            = new BooleanSetting("FacePlace", true);
    private final BooleanSetting pauseOnUse
            = new BooleanSetting("PauseOnUse", false);
    private final SliderSetting placeDelay
            = new SliderSetting("PlaceDelay", 0, 0, 5, 0.1);
    private final SliderSetting range
            = new SliderSetting("Range", 5, 0, 10, 1);
    // Break
    private final BooleanSetting Break
            = new BooleanSetting("Break", true);
    private final BooleanSetting breakAll
            = new BooleanSetting("Breakall", false);
    // Damage
    private final BooleanSetting antiSuicide
            = new BooleanSetting("AntiSuicide", true);
    private final SliderSetting minDamage
            = new SliderSetting("MinDamage", 5, 0, 36, 1);
    private final SliderSetting maxSelfDamage
            = new SliderSetting("MaxSelfDamage", 6, 0, 36, 1);
    private final SliderSetting predictDamage
            = new SliderSetting("PredictDamage", 10, 0, 36, 1);
    private final SliderSetting facePlaceHealth
            = new SliderSetting("FacePlaceHealth", 10, 0, 36, 1);
    // Predict
    private final SliderSetting predictTicks
            = new SliderSetting("PredictTicks", 2, 0, 10, 1);
    private final SliderSetting radius
            = new SliderSetting("Radius", 3, 0, 5, 1);
    // Switch
    private final EnumSetting switchMode
            = new EnumSetting("SwitchMode", switchmode.Swap);
    // Hand
    private final BooleanSetting swingHand
            = new BooleanSetting("SwingHand", true);
    private final BooleanSetting realHand
            = new BooleanSetting("RealHand", true);
    // Render
    private final EnumSetting renderMode
            = new EnumSetting("Mode", mode.Lemon);
    private final BooleanSetting renderTarget
            = new BooleanSetting("RenderTarget", false);
    private final BooleanSetting renderCrystalPos
            = new BooleanSetting("RenderCrystalPos", false);
    private final BooleanSetting renderEnemyPlace
            = new BooleanSetting("RenderEnemyPlace", false);
    // Rotation
    private final BooleanSetting rotate
            = new BooleanSetting("Rotate", true);

    private BlockPos blockPos = null;
    private BlockPos supportPos = null;
    private PlayerEntity target = null;
    ArrayList<BlockPos> validPos = new ArrayList<>();
    private final Timer timer = new Timer().reset();

    public AutoCrystalPlus() {
        super("ZSpaceAura2", Category.Combat);
        Instance = this;
        try {
            for (Field field : AutoCrystalPlus.class.getDeclaredFields()) {
                if (!Setting.class.isAssignableFrom(field.getType()))
                    continue;
                Setting setting = (Setting) field.get(this);
                addSetting(setting);
            }
        } catch (Exception e) {
            System.out.println(114514);
        }
    }

    private enum mode {
        Lemon, Meteor
    }

    private enum switchmode {
        InvSilent, Swap
    }

    @Override
    public void onRender(MatrixStack matrixStack, float partialTicks) {
        if (supportPos != null) {
            this.getRenderUtils().draw3DBox(matrixStack, new Box(supportPos), new Color(255, 255, 255), 0.4f);
        }
        if (blockPos != null && renderCrystalPos.getValue()) {
            this.getRenderUtils().draw3DBox(matrixStack, new Box(blockPos), new Color(255, 255, 255), 0.4f);
        }
        if (renderTarget.getValue() && target != null) {
            this.getRenderUtils().draw3DBox(matrixStack, target.getBoundingBox(), new Color(255, 0, 0), 0.2f);
        }
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            return;
        }
    }

    @Override
    public void onUpdate() {
        boolean place = this.place.getValue();
        boolean pauseOnUse = this.pauseOnUse.getValue();
        float placeDelay = (float) this.placeDelay.getValue();
        float range = (float) this.range.getValue();
        boolean breakAll = this.breakAll.getValue();
        boolean antiSuicide = this.antiSuicide.getValue();
        float minDamage = (float) this.minDamage.getValue();
        float maxSelfDamage = (float) this.maxSelfDamage.getValue();
        boolean Break = this.Break.getValue();
        int radius = (int) this.radius.getValue();
        if (pauseOnUse && mc.player.isUsingItem()) return;
        if (!timer.passed((long) (placeDelay * 1000))) {
            return;
        }
        for (PlayerEntity target : CombatUtil.getEnemies(range)) {
            for (BlockPos pos : BlockUtil.getSphere(radius, target.getBlockPos())) {
                if (supportDamage(pos, target)
                        && getSupportBlock(pos) == Blocks.BEDROCK || getSupportBlock(pos) == Blocks.OBSIDIAN) {
                    validPos.add(pos);
                    this.target = target;
                }
            }
        }
        float maxDamage = Float.MIN_VALUE;
        BlockPos maxDamagePos = null;

        for (BlockPos pos : validPos) {
            float damage = getDamage(pos, target);
            if (damage > maxDamage) {
                maxDamage = damage;
                maxDamagePos = pos;
                this.blockPos = maxDamagePos;
            }
        }
        if (blockPos!=null){
            if (place){
              //  BlockPos blockPos1 = mc.player.getBlockPos();
                //int blockPosX = blockPos1.getX();
              //  if ((float) ((blockPosX - blockPos.getX()) * (blockPosX - blockPos.getX())) / (blockPos.getX() - blockPos.getX()) > range) return;
                if (!canPlace(blockPos,range)) return;
                if (!shouldFacePlace(target) && blockPos.getY() != target.getBlockPos().getY()) return;
                if (getDamage(blockPos,mc.player) > maxSelfDamage && !mc.player.isCreative()) return;
                doPlaceCrystal(blockPos,rotate.getValue(),Direction.UP);
                this.supportPos = blockPos.add(0,-1,0);
                if (Break){
                    doBreakCrystal(blockPos,rotate.getValue(),Direction.UP);
                }
            }
            timer.reset();

        }
    }
    //  private float calcDamage(BlockPos blockPos, PlayerEntity target,int predictTicks) {
    //   if (predictTicks <= 0){
    //        return getDamage(blockPos,target);
    //  }
    //  else {

    // }

    // }
    public static float getDamage(BlockPos ep, PlayerEntity target) {
        try {
            if (mc.world.getDifficulty() == Difficulty.PEACEFUL) return 0f;

            Vec3d explosionPos = ep.toCenterPos();
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
        } catch (Exception e) {

        }
        return 0f;
    }

    private float getSelfDamage(BlockPos blockPos) {
        return getDamage(blockPos, mc.player);
    }

    private boolean supportDamage(BlockPos blockPos, PlayerEntity target) {
        return getDamage(blockPos, target) > minDamage.getValueFloat();
    }
    private BlockPos getSupportBlockPos(BlockPos blockPos) {
        return blockPos.add(0, -1, 0);
    }

    private Block getBlock(BlockPos blockPos) {
        return mc.world.getBlockState(blockPos).getBlock();
    }

    private Block getSupportBlock(BlockPos blockPos) {
        return getBlock(getSupportBlockPos(blockPos));
    }

    private boolean isAir(BlockPos blockPos) {
        return getBlock(blockPos) == Blocks.AIR;
    }

    private boolean supportPlacePos(BlockPos blockPos) {
        return getSupportBlock(blockPos) == Blocks.OBSIDIAN || getSupportBlock(blockPos) == Blocks.BEDROCK;
    }

    private boolean findCrystal(double range) {
        for (Entity entity : CombatUtil.getEnemies(range)) {
            return entity instanceof EndCrystalEntity;
        }
        return false;
    }

    private void doPlaceCrystal(BlockPos blockPos, boolean rotate, Direction direction) {
        Vec3d directionVec = new Vec3d
                (blockPos.getX()
                        + 0.5
                        + direction.getVector().getX()
                        * 0.5, blockPos.getY() + 0.5
                        + direction.getVector().getY() * 0.5,
                        blockPos.getZ()
                                + 0.5 + direction.getVector().getZ()
                                * 0.5);
        if (rotate) {
            float[] angle = getLegitRotations(directionVec);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], mc.player.isOnGround()));
        }
        int crystal = InventoryUtil.findItem(Items.END_CRYSTAL);
        if (crystal == -1) return;
        InventoryUtil.doSwap(crystal);
        Hand mainHand = Hand.MAIN_HAND;
        Hand offHand = Hand.OFF_HAND;
        mc.interactionManager.interactBlock(mc.player, realHand.getValue() ? mainHand : offHand, new BlockHitResult(blockPos.toCenterPos(), direction, blockPos, false));
    }
    private void doBreakCrystal(BlockPos blockPos, boolean rotate, Direction direction) {
        Vec3d directionVec = new Vec3d
                (blockPos.getX()
                        + 0.5
                        + direction.getVector().getX()
                        * 0.5, blockPos.getY() + 0.5
                        + direction.getVector().getY() * 0.5,
                        blockPos.getZ()
                                + 0.5 + direction.getVector().getZ()
                                * 0.5);
        if (rotate) {
            float[] angle = getLegitRotations(directionVec);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], mc.player.isOnGround()));
        }
        BlockUtil.breakBlock(blockPos);
        mc.player.swingHand(Hand.MAIN_HAND);
        }
        private boolean shouldFacePlace(PlayerEntity target) {
        if (!facePlace.getValue()) return false;
        if (target.getHealth() < facePlaceHealth.getValueFloat()) return true;
        return false;
    }
}



