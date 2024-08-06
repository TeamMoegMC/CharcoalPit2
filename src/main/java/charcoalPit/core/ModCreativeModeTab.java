package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.item.ItemAlcoholBottle;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CharcoalPit.MODID);

    public static final RegistryObject<CreativeModeTab> CHARCOAL_PIT = CREATIVE_MODE_TAB.register("charcoal_pit", () -> CreativeModeTab.builder()
            .title(Component.translatable("charcoal_pit"))
            .icon(() -> new ItemStack(Items.CHARCOAL))
            .displayItems((params, output) -> {
                output.accept(ModItemRegistry.Thatch);
                output.accept(ModItemRegistry.LogPile);
            }).build()
    );

    public static final RegistryObject<CreativeModeTab> CHARCOAL_PIT_FOODS = CREATIVE_MODE_TAB.register("charcoal_pit_foods", () -> CreativeModeTab.builder()
            .title(Component.translatable("charcoal_pit"))
            .icon(() -> new ItemStack(ModItemRegistry.Cheese))
            .displayItems((params, output) -> {
                output.accept(ItemAlcoholBottle.cider);
                output.accept(ItemAlcoholBottle.golden_cider);
                output.accept(ItemAlcoholBottle.chorus_cider);
                output.accept(ItemAlcoholBottle.vodka);
                output.accept(ItemAlcoholBottle.beetroot_beer);
                output.accept(ItemAlcoholBottle.beer);
                output.accept(ItemAlcoholBottle.sweetberry_wine);
                output.accept(ItemAlcoholBottle.warped_wine);
                output.accept(ItemAlcoholBottle.mead);
                output.accept(ItemAlcoholBottle.rum);
                output.accept(ItemAlcoholBottle.honey_dewois);
                output.accept(ItemAlcoholBottle.spider_spirit);
            }).build()
    );


}
