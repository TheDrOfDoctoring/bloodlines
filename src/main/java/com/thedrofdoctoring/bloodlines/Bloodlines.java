package com.thedrofdoctoring.bloodlines;

import com.mojang.logging.LogUtils;
import com.thedrofdoctoring.bloodlines.capabilities.BloodlineManager;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.client.ClientRegistryHandler;
import com.thedrofdoctoring.bloodlines.commands.BloodlineCommands;
import com.thedrofdoctoring.bloodlines.config.CommonConfig;
import com.thedrofdoctoring.bloodlines.core.BloodlineAttachments;
import com.thedrofdoctoring.bloodlines.core.BloodlineEntities;
import com.thedrofdoctoring.bloodlines.core.BloodlinesBlocks;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import com.thedrofdoctoring.bloodlines.data.BloodlineSkillTreeProvider;
import com.thedrofdoctoring.bloodlines.data.BloodlinesData;
import com.thedrofdoctoring.bloodlines.data.BloodlinesTagsProviders;
import com.thedrofdoctoring.bloodlines.items.BottomlessChaliceFluidHandler;
import com.thedrofdoctoring.bloodlines.items.BottomlessChaliceItem;
import com.thedrofdoctoring.bloodlines.networking.ServerPayloadHandler;
import com.thedrofdoctoring.bloodlines.networking.ServerboundIcePacket;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkillType;
import com.thedrofdoctoring.bloodlines.skills.BloodlineSkills;
import com.thedrofdoctoring.bloodlines.skills.actions.BloodlineActions;
import com.thedrofdoctoring.bloodlines.tasks.BloodlineTasks;
import de.teamlapen.lib.HelperRegistry;
import de.teamlapen.vampirism.api.VampirismAPI;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(Bloodlines.MODID)
public class Bloodlines {
    public static final String MODID = "bloodlines";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Bloodlines(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_CONFIG);
        if (FMLEnvironment.dist.isClient()) {
            ClientRegistryHandler.init();
        }
        BloodlineRegistry.registerBloodline(BloodlineReference.NOBLE);
        BloodlineRegistry.registerBloodline(BloodlineReference.ZEALOT);
        BloodlineRegistry.registerBloodline(BloodlineReference.ECTOTHERM);


        modEventBus.addListener(this::loadComplete);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerPayloads);


        BloodlineCommands.COMMAND_ARGUMENT_TYPES.register(modEventBus);
        BloodlinesItems.register(modEventBus);
        BloodlinesBlocks.BLOCKS.register(modEventBus);
        BloodlineSkills.SKILLS.register(modEventBus);
        BloodlineEntities.ENTITY_SUB_PREDICATES.register(modEventBus);
        BloodlineActions.ACTIONS.register(modEventBus);
        BloodlineAttachments.ATTACHMENT_TYPES.register(modEventBus);
        BloodlineTasks.TASK_REWARDS.register(modEventBus);
        BloodlineTasks.TASK_UNLOCKER.register(modEventBus);
        BloodlineTasks.TASK_REWARD_INSTANCES.register(modEventBus);

        NeoForge.EVENT_BUS.addListener(this::onCommandsRegister);
    }

    public void onCommandsRegister(final RegisterCommandsEvent event) {
        BloodlineCommands.registerCommands(event.getDispatcher(), event.getBuildContext());
    }

    public void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0");
        registrar.playToServer(
                ServerboundIcePacket.TYPE,
                ServerboundIcePacket.CODEC,
                ServerPayloadHandler::handleIcePacket
        );
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        DatapackBuiltinEntriesProvider provider = new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, BloodlinesData.DATA_BUILDER, Set.of(MODID));
        generator.addProvider(event.includeServer(), provider);

        lookupProvider = provider.getRegistryProvider();
        BloodlinesTagsProviders.register(generator, event, packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), new BloodlineSkillTreeProvider(packOutput, lookupProvider));
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (item, b) -> new BottomlessChaliceFluidHandler(item, BottomlessChaliceItem.CAPACITY), BloodlinesItems.CHALICE_ITEM.get());
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        VampirismAPI.skillManager().registerSkillType(BloodlineSkillType.NOBLE);
        VampirismAPI.skillManager().registerSkillType(BloodlineSkillType.ZEALOT);

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        HelperRegistry.registerSyncablePlayerCapability((AttachmentType) BloodlineAttachments.BLOODLINE_MANAGER.get(), BloodlineManager.class);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(Bloodlines.MODID, path);
    }

}
