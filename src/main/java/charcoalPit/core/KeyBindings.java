package charcoalPit.core;

import charcoalPit.CharcoalPit;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CharcoalPit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindings {
    public static final KeyMapping PLACE_KILN = new KeyMapping(
            "key.charcoalpit.place_kiln",
            InputConstants.KEY_C,
            "key.categories.charcoalpit"
    );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent e) {
        e.register(PLACE_KILN);
    }
}
