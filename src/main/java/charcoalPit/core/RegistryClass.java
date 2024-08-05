package charcoalPit.core;

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

}
