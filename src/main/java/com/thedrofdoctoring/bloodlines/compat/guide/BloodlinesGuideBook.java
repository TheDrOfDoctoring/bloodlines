package com.thedrofdoctoring.bloodlines.compat.guide;

import com.thedrofdoctoring.bloodlines.Bloodlines;
import com.thedrofdoctoring.bloodlines.core.BloodlinesItems;
import de.maxanier.guideapi.api.IPage;
import de.maxanier.guideapi.api.impl.abstraction.CategoryAbstract;
import de.maxanier.guideapi.api.impl.abstraction.EntryAbstract;
import de.maxanier.guideapi.api.util.BookHelper;
import de.maxanier.guideapi.api.util.PageHelper;
import de.maxanier.guideapi.category.CategoryItemStack;
import de.teamlapen.vampirism.modcompat.guide.EntryText;
import de.teamlapen.vampirism.modcompat.guide.VampirismGuideBookCategoriesEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;

import static de.teamlapen.vampirism.modcompat.guide.GuideBook.translateComponent;
public class BloodlinesGuideBook {

    public static void createCategoriesEvent(VampirismGuideBookCategoriesEvent event) {
        BookHelper helper = new BookHelper.Builder(Bloodlines.MODID).build();
        CategoryAbstract bloodlinesCategory = new CategoryItemStack(buildBloodlines(helper), translateComponent("guide.bloodlines.title"), BloodlinesItems.BLOODLINE_FANG_ZEALOT.get().getDefaultInstance());
        helper.registerLinkablePages(Collections.singletonList(bloodlinesCategory));
        event.categories.add(event.categories.size() - 1, bloodlinesCategory);

    }
    private static Map<ResourceLocation, EntryAbstract> buildBloodlines(BookHelper helper) {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
        String base = "guide.bloodlines.";

        List<IPage> levelling = new ArrayList<>(helper.addLinks(PageHelper.pagesForLongText(translateComponent(base + "levelling.text"))));
        entries.put(Bloodlines.rl(base + "levelling"), new EntryText(levelling, translateComponent(base + "levelling")));

        List<IPage> noblePages = new ArrayList<>();
        noblePages.addAll(PageHelper.pagesForLongText(translateComponent(base + "noble.become")));
        noblePages.addAll(PageHelper.pagesForLongText(translateComponent(base + "noble.skills")));
        noblePages.addAll(PageHelper.pagesForLongText(translateComponent(base + "noble.weakness")));
        entries.put(Bloodlines.rl(base + "noble"), new EntryText(noblePages, translateComponent(base + "noble")));

        List<IPage> zealotPages = new ArrayList<>();
        zealotPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "zealot.become")));
        zealotPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "zealot.skills")));
        zealotPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "zealot.weakness")));
        entries.put(Bloodlines.rl(base + "zealot"), new EntryText(zealotPages, translateComponent(base + "zealot")));

        List<IPage> bloodknightPages = new ArrayList<>();
        bloodknightPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "bloodknight.become")));
        bloodknightPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "bloodknight.skills")));
        bloodknightPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "bloodknight.weakness")));
        entries.put(Bloodlines.rl(base + "bloodknight"), new EntryText(bloodknightPages, translateComponent(base + "bloodknight")));

        List<IPage> ectothermPages = new ArrayList<>();
        ectothermPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "ectotherm.become")));
        ectothermPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "ectotherm.skills")));
        ectothermPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "ectotherm.weakness")));
        entries.put(Bloodlines.rl(base + "ectotherm"), new EntryText(ectothermPages, translateComponent(base + "ectotherm")));

        List<IPage> graveboundPages = new ArrayList<>();
        graveboundPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "gravebound.become")));
        graveboundPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "gravebound.mechanics")));
        graveboundPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "gravebound.skills")));
        graveboundPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "gravebound.weakness")));
        graveboundPages.addAll(PageHelper.pagesForLongText(translateComponent(base + "gravebound.phylactery")));
        entries.put(Bloodlines.rl(base + "gravebound"), new EntryText(graveboundPages, translateComponent(base + "gravebound")));

        List<IPage> removeBloodline = new ArrayList<>(helper.addLinks(PageHelper.pagesForLongText(translateComponent(base + "remove_bloodline.text", loc(BloodlinesItems.PURITY_INJECTION.get())))));
        entries.put(Bloodlines.rl(base + "remove_bloodline"), new EntryText(removeBloodline, translateComponent(base + "remove_bloodline")));

        return entries;
    }
    private static Component loc(Item i) {
        return i.getDescription();
    }
}
