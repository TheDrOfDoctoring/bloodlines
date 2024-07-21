package com.thedrofdoctoring.bloodlines.skills.actions;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BloodlineActions {

    public static final DeferredRegister<IAction<?>> ACTIONS = DeferredRegister.create(VampirismRegistries.Keys.ACTION, Bloodlines.MODID);

    public static final DeferredHolder<IAction<?>, NobleCelerityAction> NOBLE_CELERITY_ACTION = ACTIONS.register("noble_celerity_action", NobleCelerityAction::new);
    public static final DeferredHolder<IAction<?>, NobleMesmeriseAction> NOBLE_MESMERISE_ACTION = ACTIONS.register("noble_mesmerise_action", NobleMesmeriseAction::new);
    public static final DeferredHolder<IAction<?>, NobleLeechingAction> NOBLE_LEECHING_ACTION = ACTIONS.register("noble_leeching_action", NobleLeechingAction::new);
    public static final DeferredHolder<IAction<?>, NobleInvisibilityAction> NOBLE_INVISIBILITY_ACTION = ACTIONS.register("noble_invisibility_action", NobleInvisibilityAction::new);
    public static final DeferredHolder<IAction<?>, NobleFlankAction> NOBLE_FLANK_ACTION = ACTIONS.register("noble_flank_action", NobleFlankAction::new);
    public static final DeferredHolder<IAction<?>, ZealotShadowwalkAction> ZEALOT_SHADOWWALK_ACTION = ACTIONS.register("zealot_shadowwalk_action", ZealotShadowwalkAction::new);
    public static final DeferredHolder<IAction<?>, ZealotDarkCloakAction> ZEALOT_DARK_CLOAK_ACTION = ACTIONS.register("zealot_darkcloak_action", ZealotDarkCloakAction::new);
    public static final DeferredHolder<IAction<?>, ZealotWallClimbAction> ZEALOT_WALL_CLIMB_ACTION = ACTIONS.register("zealot_wall_climb_action", ZealotWallClimbAction::new);
    public static final DeferredHolder<IAction<?>, ZealotFrenzyAction> ZEALOT_FRENZY_ACTION = ACTIONS.register("zealot_frenzy_action", ZealotFrenzyAction::new);
    public static final DeferredHolder<IAction<?>, EctothermFrostLord> ECTOTHERM_FROST_LORD_ACTION = ACTIONS.register("ectotherm_frostlord_action", EctothermFrostLord::new);
    public static final DeferredHolder<IAction<?>, EctothermDolphinLeap> ECTOTHERM_DOLPHIN_LEAP_ACTION = ACTIONS.register("ectotherm_dolphin_leap_action", EctothermDolphinLeap::new);


}