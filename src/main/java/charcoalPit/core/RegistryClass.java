package charcoalPit.core;

import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.potion.ModPotionRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "charcoal_pit", bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryClass {
    public RegistryClass() {
    }

    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        ModBlockRegistry.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(RegisterEvent event) {
        ModItemRegistry.registerItems(event);
    }

    @SubscribeEvent
    public static void registerContainers(RegisterEvent event) {
        ModContainerRegistry.registerContainers(event);
    }

    @SubscribeEvent
    public static void registerTileEntity(RegisterEvent event) {
        ModTileRegistry.registerTileEntity(event);
    }

    @SubscribeEvent
    public static void registerRecipeType(RegisterEvent event) {
        ModRecipeRegistry.registerRecipeType(event);
    }

    @SubscribeEvent
    public static void registerFluids(RegisterEvent event) {
        ModFluidRegistry.registerFluids(event);
    }

    @SubscribeEvent
    public static void registerEffects(RegisterEvent event) {
        ModPotionRegistry.registerEffects(event);
    }

    @SubscribeEvent
    public static void registerPotions(RegisterEvent event) {
        ModPotionRegistry.registerPotions(event);
    }

}
