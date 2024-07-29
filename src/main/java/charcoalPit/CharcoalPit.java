package charcoalPit;

import charcoalPit.core.Config;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModItemRegistry;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CharcoalPit.MODID)
public class CharcoalPit {
	
	public static final String MODID="charcoal_pit";
	{
		ForgeMod.enableMilkFluid();
	}
	
	public CharcoalPit() {

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//		ModItemRegistry.ITEMS.register(modEventBus);
//		MinecraftForge.EVENT_BUS.register(ModItemRegistry.class);
//		MinecraftForge.EVENT_BUS.register(ModBlockRegistry.class);
////		MinecraftForge.EVENT_BUS.register(ModItemRegistry.class);
////		MinecraftForge.EVENT_BUS.register(ModBlockRegistry.class);
//		modEventBus.addListener(ModItemRegistry::registerItems);
//		modEventBus.addGenericListener(ModBlockRegistry.class,ModBlockRegistry::registerBlocks);

		ModLoadingContext.get().registerConfig(Type.COMMON, Config.CONFIG);
	}

	public void commonSetup(){

	}
}
