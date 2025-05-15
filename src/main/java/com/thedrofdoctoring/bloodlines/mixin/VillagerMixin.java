package com.thedrofdoctoring.bloodlines.mixin;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineHelper;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineManager;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.core.ModTags;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {
    @Shadow
    public abstract VillagerData getVillagerData();

    @Shadow public abstract void playWorkSound();

    public VillagerMixin(EntityType<? extends AbstractVillager> vil, Level level) {
        super(vil, level);
    }

    //Better prices for Nobles.

    @Inject(method = "updateSpecialPrices", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;getPlayerReputation(Lnet/minecraft/world/entity/player/Player;)I"))
    private void updateSpecialPrices(Player player, CallbackInfo ci) {
        if (!BuiltInRegistries.VILLAGER_PROFESSION.wrapAsHolder(this.getVillagerData().getProfession()).is(ModTags.Professions.HAS_FACTION) && Helper.isVampire(player)) {
            if (VampirePlayer.get(player).getSkillHandler().isSkillEnabled(BloodlineSkills.NOBLE_BETTER_TRADE_PRICES.get())) {
                int rank = BloodlineHelper.getBloodlineRank(player);
                for (MerchantOffer merchantoffer1 : this.getOffers()) {
                    double rankMult = CommonConfig.nobleTradePricesMultiplier.get().get(rank - 1).floatValue();
                    rankMult = 1 - rankMult;
                    int diff = rankMult != 0 ? (int) Math.floor((merchantoffer1.getBaseCostA().getCount()) * (rankMult - 1)) : 0;
                    merchantoffer1.addToSpecialPriceDiff(diff);
                }
            } else if (BloodlineManager.get(player).getBloodline() == BloodlineRegistry.BLOODLINE_ZEALOT.get()) {
                int rank = BloodlineHelper.getBloodlineRank(player);
                for (MerchantOffer merchantoffer1 : this.getOffers()) {
                    double rankMult = CommonConfig.zealotTradePricesMultiplier.get().get(rank - 1).floatValue();
                    int diff = rankMult != 0 ? (int) Math.floor((merchantoffer1.getBaseCostA().getCount()) * (rankMult - 1)) : 0;
                    merchantoffer1.addToSpecialPriceDiff(diff);
                }
            } else if(BloodlineManager.get(player).getBloodline() == BloodlineRegistry.BLOODLINE_BLOODKNIGHT.get()) {
                int rank = BloodlineHelper.getBloodlineRank(player);
                for (MerchantOffer merchantoffer1 : this.getOffers()) {
                    double rankMult = CommonConfig.bloodknightTradePricesMultiplier.get().get(rank - 1).floatValue();
                    int diff = rankMult != 0 ? (int) Math.floor((merchantoffer1.getBaseCostA().getCount()) * (rankMult - 1)) : 0;
                    merchantoffer1.addToSpecialPriceDiff(diff);
                }
            }
        }
    }
}
