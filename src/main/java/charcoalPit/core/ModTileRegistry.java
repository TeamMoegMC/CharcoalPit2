package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.tile.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class ModTileRegistry {



    public static BlockEntityType<TileActivePile> ActivePile = BlockEntityType.Builder.of(TileActivePile::new, ModBlockRegistry.LogPile, ModBlockRegistry.CoalPile).build(null);
    public static BlockEntityType<TileCreosoteCollector> CreosoteCollector = BlockEntityType.Builder.of(TileCreosoteCollector::new, ModBlockRegistry.SandyCollector).build(null);
    public static BlockEntityType<TilePotteryKiln> PotteryKiln = BlockEntityType.Builder.of(TilePotteryKiln::new, ModBlockRegistry.Kiln).build(null);
    public static BlockEntityType<TileCeramicPot> CeramicPot = BlockEntityType.Builder.of(TileCeramicPot::new, ModBlockRegistry.CeramicPot/*, ModBlockRegistry.BlackPot,
            ModBlockRegistry.BluePot, ModBlockRegistry.BrownPot, ModBlockRegistry.CyanPot, ModBlockRegistry.GrayPot, ModBlockRegistry.GreenPot,
            ModBlockRegistry.LightBluePot, ModBlockRegistry.LightGrayPot, ModBlockRegistry.LimePot, ModBlockRegistry.MagentaPot, ModBlockRegistry.OrangePot,
            ModBlockRegistry.PinkPot, ModBlockRegistry.PurplePot, ModBlockRegistry.RedPot, ModBlockRegistry.WhitePot, ModBlockRegistry.YellowPot*/).build(null);
    //public static TileEntityType<TileClayPot> ClayPot=TileEntityType.Builder.create(TileClayPot::new, ModBlockRegistry.ClayPot).build(null);
    public static BlockEntityType<TileBloomery> Bloomery2 = BlockEntityType.Builder.of(TileBloomery::new, ModBlockRegistry.Bloomery).build(null);
    public static BlockEntityType<TileBarrel> Barrel = BlockEntityType.Builder.of(TileBarrel::new, ModBlockRegistry.Barrel).build(null);
	

	public static void registerTileEntity(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES,
                helper -> {
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "active_pile"), ActivePile);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "creosote_collector"), CreosoteCollector);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "pottery_kiln"), PotteryKiln);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "ceramic_pot"), CeramicPot);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "bloomery2"), Bloomery2);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "barrel"), Barrel);
                }
        );
	}
	
}
