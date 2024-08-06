package charcoalPit;

import charcoalPit.core.Config;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModCreativeModeTab;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.fluid.ModFluidRegistry;
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
		ModCreativeModeTab.CREATIVE_MODE_TAB.register(modEventBus);
		ModFluidRegistry.FLUID_TYPES.register(modEventBus);
		ModFluidRegistry.FLUIDS.register(modEventBus);

		ModLoadingContext.get().registerConfig(Type.COMMON, Config.CONFIG);
	}

	public void commonSetup(){

	}
}
