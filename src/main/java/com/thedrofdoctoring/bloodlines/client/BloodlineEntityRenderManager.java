package com.thedrofdoctoring.bloodlines.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.BloodlinesClient;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.entity.BloodlineMobManager;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import de.teamlapen.vampirism.entity.VampirismEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.util.*;


public class BloodlineEntityRenderManager implements ResourceManagerReloadListener {

    private final Map<IBloodline, ResourceLocation[]> bloodlineToTextures = new HashMap<>();
    private final Minecraft mc;
    public BloodlineEntityRenderManager(Minecraft mc) {
        this.mc = mc;
    }


    @Override
    public void onResourceManagerReload(@NotNull ResourceManager __resourceManager) {

        bloodlineToTextures.clear();
        // Using resource manager param doesn't work, for some reason. Oh well.
        ResourceManager manager = mc.getResourceManager();
        ResourceLocation[] textureJsons = manager.listResources("bloodline_textures", s -> s.getPath().endsWith(".json")).keySet().toArray(ResourceLocation[]::new);
        ArrayList<JsonObject> jsonObjects = new ArrayList<>();
        for(ResourceLocation location : textureJsons) {
            Optional<Resource> resource = manager.getResource(location);
            if(resource.isPresent()) {
                try (Reader reader = resource.get().openAsReader()) {
                    jsonObjects.add(JsonParser.parseReader(reader).getAsJsonObject());
                } catch(Exception exception) {
                    Bloodlines.LOGGER.error("Could not read bloodlines entity texture json for {}", location,  exception);
                }

            }
        }
        HashMap<IBloodline, ArrayList<ResourceLocation>> tempMap = new HashMap<>();
        for(JsonObject object : jsonObjects) {
            ResourceLocation bloodlineRl = ResourceLocation.parse(GsonHelper.getAsString(object, "bloodline"));
            IBloodline bloodline = BloodlineRegistry.BLOODLINE_REGISTRY.get(bloodlineRl);
            ArrayList<ResourceLocation> currentLocations = tempMap.get(bloodline);
            JsonArray textures = object.getAsJsonArray("textures");

            for(int i = 0; i < textures.size(); i++){
                ResourceLocation location = ResourceLocation.parse(textures.get(i).getAsString().concat(".png"));
                if(currentLocations == null) {
                    currentLocations = new ArrayList<>();
                    currentLocations.add(location);
                    tempMap.put(bloodline, currentLocations);
                } else {
                    currentLocations.add(location);
                }
            }

        }
        for(Map.Entry<IBloodline, ArrayList<ResourceLocation>> blEntries : tempMap.entrySet()) {
            ResourceLocation[] locationsArray = blEntries.getValue().toArray(new ResourceLocation[0]);
            bloodlineToTextures.put(blEntries.getKey(), locationsArray);
        }

    }

    public ResourceLocation[] getBloodlineTextures(IBloodline bloodline) {
        return bloodlineToTextures.get(bloodline);
    }
    public ResourceLocation getBloodlineTexture(ResourceLocation original, VampirismEntity entity) {
        IBloodline bloodline = BloodlineMobManager.get(entity).getBloodline();
        if(bloodline != null) {
            ResourceLocation[] textures = BloodlinesClient.getInstance().getRenderManager().getBloodlineTextures(bloodline);
            if(textures == null || textures.length == 0) return original;
            return textures[entity.getId() % textures.length];
        }
        return original;
    }
}
