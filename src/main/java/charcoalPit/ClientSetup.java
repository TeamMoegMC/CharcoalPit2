package charcoalPit;

import charcoalPit.core.ModContainerRegistry;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.gui.BarrelScreen;
import charcoalPit.gui.CeramicPotScreen;
import charcoalPit.gui.ClayPotScreen;
import charcoalPit.tile.TESRPotteryKiln;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=CharcoalPit.MODID ,bus=Bus.MOD ,value=Dist.CLIENT)
public class ClientSetup {
	
	@SubscribeEvent
	public static void init(FMLClientSetupEvent event) {
		MenuScreens.register(ModContainerRegistry.CeramicPot, CeramicPotScreen::new);
		MenuScreens.register(ModContainerRegistry.ClayPot, ClayPotScreen::new);
		MenuScreens.register(ModContainerRegistry.Barrel, BarrelScreen::new);
	}
	@SubscribeEvent
	public static void registerColors(RegisterColorHandlersEvent.Item event){
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if (p_getColor_2_ == 0) {
					return PotionUtils.getColor(p_getColor_1_);
				}
				return 0xFFFFFF;
			}
		}, ModItemRegistry.AlcoholBottle);
	}

	@SubscribeEvent
	public static void RegisterEntityRender(EntityRenderersEvent.RegisterRenderers event){
		event.registerBlockEntityRenderer(ModTileRegistry.PotteryKiln, TESRPotteryKiln::new);

	}
}
