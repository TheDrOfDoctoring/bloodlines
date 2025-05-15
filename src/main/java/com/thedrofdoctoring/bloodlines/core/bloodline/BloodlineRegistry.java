package com.thedrofdoctoring.bloodlines.core.bloodline;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.hunter.BloodlineGravebound;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineBloodknight;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineFrost;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineNoble;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.vamp.BloodlineZealot;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class BloodlineRegistry {

    public static final ResourceKey<Registry<IBloodline>> BLOODLINE_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bloodlines.MODID, "bloodlines"));
    public static final Registry<IBloodline> BLOODLINE_REGISTRY = new RegistryBuilder<>(BLOODLINE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(Bloodlines.MODID, "empty"))
            .create();

    /**

        Registering and fully creating a Bloodline requires a couple of steps.
        <ul>
        <li> Register implementation of IBloodline here
        <li> Create BloodlineSkillType and register it - Not strictly necessary, but recommended </li>
        <li> Create a BloodlineParentSkill for the Bloodline. </li>
        <li> Create a new Skill Tree. All skills should extend BloodlineSkill. Create tags for skill tree. </li>
        <li> Anything else should be done with custom skills, events, or the implementation of IBloodline. </li>
        </ul>

        Additionally,
        <ul>
         <li> Register a BloodlineFang for that Bloodline - If a Vampire Bloodline. </li>
         <li> Create tasks for levelling and perk points for that bloodline - Unless special levelling or perk system. </li>
         <li> Create a BloodlineSpawnModifier and BloodlineRankDistribution - Not necessary if bloodline is only for players </li>
         <li> Create textures for bloodline entity - Not necessary if bloodline only applies to entities that would not be supported by this </li>
         </ul>
     */ 
    public static final DeferredRegister<IBloodline> BLOODLINES = DeferredRegister.create(BLOODLINE_REGISTRY, Bloodlines.MODID);

    public static final DeferredHolder<IBloodline, BloodlineNoble> BLOODLINE_NOBLE = BLOODLINES.register("noble", BloodlineNoble::new);
    public static final DeferredHolder<IBloodline, BloodlineFrost> BLOODLINE_ECTOTHERM = BLOODLINES.register("ectotherm", BloodlineFrost::new);
    public static final DeferredHolder<IBloodline, BloodlineZealot> BLOODLINE_ZEALOT = BLOODLINES.register("zealot", BloodlineZealot::new);
    public static final DeferredHolder<IBloodline, BloodlineBloodknight> BLOODLINE_BLOODKNIGHT = BLOODLINES.register("bloodknight", BloodlineBloodknight::new);

    public static final DeferredHolder<IBloodline, BloodlineGravebound> BLOODLINE_GRAVEBOUND = BLOODLINES.register("gravebound", BloodlineGravebound::new);


}
