package charcoalPit.network;

import charcoalPit.CharcoalPit;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    public static final SimpleChannel CH = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CharcoalPit.MODID, "n"),
            () -> "1", "1"::equals, "1"::equals
    );

    public static void register() {
        CH.registerMessage(0, PlaceKilnPacket.class,
                PlaceKilnPacket::encode, PlaceKilnPacket::decode, PlaceKilnPacket::handle);
    }
}
