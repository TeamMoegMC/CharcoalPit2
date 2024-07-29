package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.gui.BarrelContainer;
import charcoalPit.gui.CeramicPotContainer;
import charcoalPit.gui.ClayPotContainer2;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModContainerRegistry {

	public static MenuType<CeramicPotContainer> CeramicPot= IForgeMenuType.create((id, inv, data)->{
		return new CeramicPotContainer(id,data.readBlockPos(),inv);
	});
	/*public static ContainerType<ClayPotContainer> ClayPot=IForgeContainerType.create((id,inv,data)->{
		return new ClayPotContainer(id, data.readBlockPos(), inv);
	});*/
	public static MenuType<ClayPotContainer2> ClayPot=IForgeMenuType.create((id,inv,data)->{
        return new ClayPotContainer2(id, inv, data.readByte());
    });
	public static MenuType<BarrelContainer> Barrel=IForgeMenuType.create((id,inv,data)->{
		return new BarrelContainer(id, data.readBlockPos(), inv);
	});
	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"),ClayPot.setRegistryName("clay_pot"),Barrel.setRegistryName("barrel"));
	}
	
	
}
