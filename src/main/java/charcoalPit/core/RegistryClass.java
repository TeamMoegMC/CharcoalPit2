package charcoalPit.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = "charcoal_pit",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class RegistryClass {
    public RegistryClass() {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlockRegistry.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItemRegistry.registerItems(event);
    }

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<BlockEntityType<?>> event) {
        ModTileRegistry.registerTileEntity(event);
    }

}
