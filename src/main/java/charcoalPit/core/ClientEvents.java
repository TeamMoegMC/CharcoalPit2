package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.network.ModNetwork;
import charcoalPit.network.PlaceKilnPacket;
import charcoalPit.recipe.PotteryKilnRecipe;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CharcoalPit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        if (event.getAction() != InputConstants.PRESS) return;
        if (!KeyBindings.PLACE_KILN.isDown()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (mc.screen != null) return;

        if (mc.hitResult instanceof BlockHitResult hit
                && hit.getType() == HitResult.Type.BLOCK
                && hit.getDirection() == Direction.UP) {

            Level world = mc.level;

            boolean hasValid = false;
            for (ItemStack stack : new ItemStack[]{mc.player.getMainHandItem(), mc.player.getOffhandItem()}) {
                if (PotteryKilnRecipe.isValidInput(stack, world)) {
                    hasValid = true;
                    break;
                }
            }
            if (hasValid
                    && world.getBlockState(hit.getBlockPos()).isFaceSturdy(world, hit.getBlockPos(), Direction.UP)
                    && world.getBlockState(hit.getBlockPos().above()).canBeReplaced()) {
                ModNetwork.CH.sendToServer(new PlaceKilnPacket(hit.getBlockPos()));
            }
        }
    }
}
