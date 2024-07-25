package com.thedrofdoctoring.bloodlines.data;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.factions.ISkillNode;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.data.provider.parent.SkillTreeProvider;
import de.teamlapen.vampirism.entity.player.skills.SkillTreeConfiguration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BloodlineSkillTreeProvider extends SkillTreeProvider {

    public BloodlineSkillTreeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, Bloodlines.MODID);
    }

    @Override
    protected void buildSkillTrees(HolderLookup.Provider provider, @NotNull SkillTreeOutput skillTreeOutput) {
        HolderLookup.RegistryLookup<ISkillTree> trees = provider.lookupOrThrow(VampirismRegistries.Keys.SKILL_TREE);
        HolderLookup.RegistryLookup<ISkillNode> nodes = provider.lookupOrThrow(VampirismRegistries.Keys.SKILL_NODE);

        skillTreeOutput.accept(modId("noble"), new SkillTreeConfiguration(trees.getOrThrow(BloodlineSkills.Trees.NOBLE), nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_ROOT),

        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_BETTER_TRADE_PRICES)),
                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_RANK_2),
                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_FASTER_MOVEMENT_SPEED),
                                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_CELERITY)
                                        )
                                ),
                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_RANK_3),
                                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_MESMERISE)),
                                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_INTRIGUE),
                                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_FLANK))),
                                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_RANK_4),
                                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_BAT_FLIGHT_SPEED)),
                                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_CHALICE_SKILL)
                                                )
                                        ),

                                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_FASTER_RESURRECT)),
                                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_INVISIBILITY))
                                ),
                            new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_LEECHING),
                            new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_ENHANCED_LEECHING)
                            ))
                        ),
                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_MORE_TICKS_IN_SUN)),
                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.NOBLE_BETTER_BLOOD_DRAIN)
                )
        )
        );
        skillTreeOutput.accept(modId("zealot"), new SkillTreeConfiguration(trees.getOrThrow(BloodlineSkills.Trees.ZEALOT), nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_ROOT),
                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_STONE_SPEED),
                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_TUNNELER))),
                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_RANK_2),
                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_SHADOWWALK)),
                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_RANK_3),
                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_DARKCLOAK)),
                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_RANK_4),
                                    new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_SHADOW_MASTERY),
                                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_OBSCURED_POWER)))),
                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_FALL_DAMAGE),
                                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_WALL_CLIMB),
                                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_FRENZY)
                                                )
                                        )
                                )
                        ),
                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_FLESH_ARMOUR),
                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_HEX_PROTECTION)))



                                        ),
                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_SPIDER_FRIEND),
                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ZEALOT_SWIFT_SNEAK)))
                )
        );
        skillTreeOutput.accept(modId("ectotherm"), new SkillTreeConfiguration(trees.getOrThrow(BloodlineSkills.Trees.ECTOTHERM), nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_ROOT),
                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_SNOW_WALKER)),

                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_RANK_2),
                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_REFRACTION),
                                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_DIFFUSION))),
                                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_RANK_3),
                                    new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_FROZEN_SLOWNESS)),
                                    new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_RANK_4)),
                                    new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_TENTACLES))
                        ),
                        new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_ICE_LORD),
                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_FROST_CONTROL))
                        ),
                                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_FISHMONGER))
                        ),

                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_UNDERWATER_MINING_SPEED)),
                new SkillTreeConfiguration.SkillTreeNodeConfiguration(nodes.getOrThrow(BloodlineSkills.Nodes.ECTOTHERM_DOLPHIN_LEAP))
                )
        );
    }


}
