package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.tree.BentTrunkPlacer;
import charcoalPit.tree.DragonFoliagePlacer;
import charcoalPit.tree.PalmFoliagePlacer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid=CharcoalPit.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {
	
	
	public static FoliagePlacerType<DragonFoliagePlacer> DRAGON_PLACER=new FoliagePlacerType<>(DragonFoliagePlacer.CODEC);
	public static FoliagePlacerType<PalmFoliagePlacer> PALM_PLACER=new FoliagePlacerType<>(PalmFoliagePlacer.CODEC);
	
	public static TrunkPlacerType<BentTrunkPlacer> BENT_PLACER=new TrunkPlacerType<BentTrunkPlacer>(BentTrunkPlacer.CODEC);
	
	@SubscribeEvent
	public static void registerPlacers(RegistryEvent.Register<FoliagePlacerType<?>> event){
		event.getRegistry().registerAll(DRAGON_PLACER.setRegistryName("dragon_placer"),
				PALM_PLACER.setRegistryName("palm_placer"));
	}
	
	
	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {

		Registry.register(Registry.TRUNK_REPLACER, new ResourceLocation(CharcoalPit.MODID), BENT_PLACER);
	}
}
