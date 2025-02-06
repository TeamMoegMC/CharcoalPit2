package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.item.*;
import charcoalPit.potion.ModPotionRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CharcoalPit.MODID);

    public static final RegistryObject<CreativeModeTab> CHARCOAL_PIT = CREATIVE_MODE_TAB.register("charcoal_pit", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.charcoal_pit"))
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
//                output.accept(ModItemRegistry.AlcoholBottle);
                output.accept(ModItemRegistry.VinegarBucket);
                output.accept(ModItemRegistry.VinegarBottle);
                output.accept(ModItemRegistry.Cheese);
                output.accept(ModItemRegistry.TinyCoke);
                output.accept(ModItemRegistry.MechanicalBeellows);

                ItemStack cider= PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.CIDER);
                ItemStack golden_cider=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.GOLDEN_CIDER);
                ItemStack vodka=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.VODKA);
                ItemStack beetroot_beer=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.BEETROOT_BEER);
                ItemStack sweetberry_wine=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.SWEETBERRY_WINE);
                ItemStack mead=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.MEAD);
                ItemStack beer=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.BEER);
                ItemStack rum=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.RUM);
                ItemStack chorus_cider=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.CHORUS_CIDER);
                ItemStack spider_spirit=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.SPIDER_SPIRIT);
                ItemStack honey_dewois=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.HONEY_DEWOIS);
                ItemStack warped_wine=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.WARPED_WINE);

                cider.getTag().putInt("CustomPotionColor", 0xE50000);
                golden_cider.getTag().putInt("CustomPotionColor", 0xDBB40C);
                vodka.getTag().putInt("CustomPotionColor", 0xE6DAA6);
                beetroot_beer.getTag().putInt("CustomPotionColor", 0x840000);
                sweetberry_wine.getTag().putInt("CustomPotionColor", 0x06470C);
                mead.getTag().putInt("CustomPotionColor", 0xFAC205);
                beer.getTag().putInt("CustomPotionColor", 0xFDAA48);
                rum.getTag().putInt("CustomPotionColor", 0x650021);
                chorus_cider.getTag().putInt("CustomPotionColor", 0x9A0EAA);
                honey_dewois.getTag().putInt("CustomPotionColor", 0xF97306);
                warped_wine.getTag().putInt("CustomPotionColor", 0x0485D1);
                spider_spirit.getTag().putInt("CustomPotionColor", 0xA5A502);

                output.accept(cider);
                output.accept(golden_cider);
                output.accept(chorus_cider);
                output.accept(vodka);
                output.accept(beetroot_beer);
                output.accept(beer);
                output.accept(sweetberry_wine);
                output.accept(warped_wine);
                output.accept(mead);
                output.accept(rum);
                output.accept(honey_dewois);
                output.accept(spider_spirit);
            }).build()
    );

/*
    public static final RegistryObject<CreativeModeTab> CHARCOAL_PIT_FOODS = CREATIVE_MODE_TAB.register("charcoal_pit_foods", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.charcoal_pit"))
            .icon(() -> new ItemStack(ModItemRegistry.Cheese))
            .displayItems((params, output) -> {
                ItemStack cider= PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.CIDER);
                ItemStack golden_cider=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.GOLDEN_CIDER);
                ItemStack vodka=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.VODKA);
                ItemStack beetroot_beer=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.BEETROOT_BEER);
                ItemStack sweetberry_wine=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.SWEETBERRY_WINE);
                ItemStack mead=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.MEAD);
                ItemStack beer=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.BEER);
                ItemStack rum=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.RUM);
                ItemStack chorus_cider=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.CHORUS_CIDER);
                ItemStack spider_spirit=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.SPIDER_SPIRIT);
                ItemStack honey_dewois=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.HONEY_DEWOIS);
                ItemStack warped_wine=PotionUtils.setPotion(new ItemStack(ModItemRegistry.AlcoholBottle), ModPotionRegistry.WARPED_WINE);

                cider.getTag().putInt("CustomPotionColor", 0xE50000);
                golden_cider.getTag().putInt("CustomPotionColor", 0xDBB40C);
                vodka.getTag().putInt("CustomPotionColor", 0xE6DAA6);
                beetroot_beer.getTag().putInt("CustomPotionColor", 0x840000);
                sweetberry_wine.getTag().putInt("CustomPotionColor", 0x06470C);
                mead.getTag().putInt("CustomPotionColor", 0xFAC205);
                beer.getTag().putInt("CustomPotionColor", 0xFDAA48);
                rum.getTag().putInt("CustomPotionColor", 0x650021);
                chorus_cider.getTag().putInt("CustomPotionColor", 0x9A0EAA);
                honey_dewois.getTag().putInt("CustomPotionColor", 0xF97306);
                warped_wine.getTag().putInt("CustomPotionColor", 0x0485D1);
                spider_spirit.getTag().putInt("CustomPotionColor", 0xA5A502);
                
                output.accept(cider);
                output.accept(golden_cider);
                output.accept(chorus_cider);
                output.accept(vodka);
                output.accept(beetroot_beer);
                output.accept(beer);
                output.accept(sweetberry_wine);
                output.accept(warped_wine);
                output.accept(mead);
                output.accept(rum);
                output.accept(honey_dewois);
                output.accept(spider_spirit);
            }).build()
    );
*/


}
