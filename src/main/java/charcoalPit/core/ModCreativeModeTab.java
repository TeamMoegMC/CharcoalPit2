package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
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

                output.accept(ModItemRegistry.CoalPile);
                output.accept(ModItemRegistry.CokeBlock);
                output.accept(ModItemRegistry.WoodAsh);
                output.accept(ModItemRegistry.CoalAsh);
                output.accept(ModItemRegistry.AshBlock);
                output.accept(ModItemRegistry.SandyBrick);

                output.accept(ModItemRegistry.SandySlab);
                output.accept(ModItemRegistry.SandyStair);
                output.accept(ModItemRegistry.SandyWall);
                output.accept(ModItemRegistry.MainBloomery);
                output.accept(ModItemRegistry.Straw);
                output.accept(ModItemRegistry.Coke);
                output.accept(ModItemRegistry.Ash);
                output.accept(ModItemRegistry.Fertilizer);
                output.accept(ModItemRegistry.FireStarter);
                output.accept(ModItemRegistry.BloomCool);
                output.accept(ModItemRegistry.BloomFail);

                output.accept(ModItemRegistry.BloomNiCool);
                output.accept(ModItemRegistry.BloomNiFail);
                output.accept(ModItemRegistry.SandyBrickItem);
                output.accept(ModItemRegistry.MainBloomery);
                output.accept(ModItemRegistry.UnfireSandyBrick);
                output.accept(ModItemRegistry.UnfiredBrick);
                output.accept(ModItemRegistry.Ash);
                output.accept(ModItemRegistry.SandyCollector);
                output.accept(ModItemRegistry.FireStarter);
                output.accept(ModItemRegistry.CeramicPot);
                output.accept(ModItemRegistry.Bellows);

                output.accept(ModItemRegistry.TuyereSandy);
                output.accept(ModItemRegistry.ClayPot);
                output.accept(ModItemRegistry.CrackedPot);
                output.accept(ModItemRegistry.Barrel);
                output.accept(ModItemRegistry.AlcoholBottle);
                output.accept(ModItemRegistry.VinegarBucket);
                output.accept(ModItemRegistry.VinegarBottle);
                output.accept(ModItemRegistry.Cheese);
                output.accept(ModItemRegistry.TinyCoke);
                output.accept(ModItemRegistry.MechanicalBeellows);


            }).build()
    );

//    public static final RegistryObject<CreativeModeTab> CHARCOAL_PIT_FOODS = CREATIVE_MODE_TAB.register("charcoal_pit_foods", () -> CreativeModeTab.builder()
//            .title(Component.translatable("charcoal_pit"))
//            .icon(() -> new ItemStack(ModItemRegistry.Cheese))
//            .displayItems((params, output) -> {
////                output.accept(ItemAlcoholBottle.cider);
////                output.accept(ItemAlcoholBottle.golden_cider);
////                output.accept(ItemAlcoholBottle.chorus_cider);
////                output.accept(ItemAlcoholBottle.vodka);
////                output.accept(ItemAlcoholBottle.beetroot_beer);
////                output.accept(ItemAlcoholBottle.beer);
////                output.accept(ItemAlcoholBottle.sweetberry_wine);
////                output.accept(ItemAlcoholBottle.warped_wine);
////                output.accept(ItemAlcoholBottle.mead);
////                output.accept(ItemAlcoholBottle.rum);
////                output.accept(ItemAlcoholBottle.honey_dewois);
////                output.accept(ItemAlcoholBottle.spider_spirit);
//            }).build()
    );


}
