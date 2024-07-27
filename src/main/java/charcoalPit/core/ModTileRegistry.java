package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.tile.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = CharcoalPit.MODID, bus = Bus.MOD)
public class ModTileRegistry {


    public static BlockEntityType<TileActivePile> ActivePile = BlockEntityType.Builder.of(TileActivePile::new, ModBlockRegistry.LogPile, ModBlockRegistry.CoalPile).build(null);
    public static BlockEntityType<TileCreosoteCollector> CreosoteCollector = BlockEntityType.Builder.of(TileCreosoteCollector::new, ModBlockRegistry.SandyCollector).build(null);
    public static BlockEntityType<TilePotteryKiln> PotteryKiln = BlockEntityType.Builder.of(TilePotteryKiln::new, ModBlockRegistry.Kiln).build(null);
    public static BlockEntityType<TileCeramicPot> CeramicPot = BlockEntityType.Builder.of(TileCeramicPot::new, ModBlockRegistry.CeramicPot, ModBlockRegistry.BlackPot,
            ModBlockRegistry.BluePot, ModBlockRegistry.BrownPot, ModBlockRegistry.CyanPot, ModBlockRegistry.GrayPot, ModBlockRegistry.GreenPot,
            ModBlockRegistry.LightBluePot, ModBlockRegistry.LightGrayPot, ModBlockRegistry.LimePot, ModBlockRegistry.MagentaPot, ModBlockRegistry.OrangePot,
            ModBlockRegistry.PinkPot, ModBlockRegistry.PurplePot, ModBlockRegistry.RedPot, ModBlockRegistry.WhitePot, ModBlockRegistry.YellowPot).build(null);
    //public static TileEntityType<TileClayPot> ClayPot=TileEntityType.Builder.create(TileClayPot::new, ModBlockRegistry.ClayPot).build(null);
    public static BlockEntityType<TileBloomery> Bloomery2 = BlockEntityType.Builder.of(TileBloomery::new, ModBlockRegistry.Bloomery).build(null);
    public static BlockEntityType<TileBarrel> Barrel = BlockEntityType.Builder.of(TileBarrel::new, ModBlockRegistry.Barrel).build(null);
	
	
	@SubscribeEvent
	public static void registerTileEntity(RegistryEvent.Register<BlockEntityType<?>> event) {
		event.getRegistry().registerAll(ActivePile.setRegistryName("active_pile"),CreosoteCollector.setRegistryName("creosote_collector"),
				PotteryKiln.setRegistryName("pottery_kiln"),CeramicPot.setRegistryName("ceramic_pot"),Bloomery2.setRegistryName("bloomery2"),
				Barrel.setRegistryName("barrel"));
	}
	
}
