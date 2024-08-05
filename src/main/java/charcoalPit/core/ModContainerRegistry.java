package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.gui.BarrelContainer;
import charcoalPit.gui.CeramicPotContainer;
import charcoalPit.gui.ClayPotContainer2;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;



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

	public static void registerContainers(RegisterEvent event) {
		event.register(ForgeRegistries.Keys.MENU_TYPES,
				helper -> {
					helper.register(new ResourceLocation(CharcoalPit.MODID, "ceramic_pot"), CeramicPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"clay_pot"), ClayPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID, "barrel"), Barrel);
				}
		);
	}
}
