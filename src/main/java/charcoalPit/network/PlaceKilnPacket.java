package charcoalPit.network;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.recipe.PotteryKilnRecipe;
import charcoalPit.tile.TilePotteryKiln;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaceKilnPacket {
    private final long posLong;

    public PlaceKilnPacket(BlockPos pos) {
        this.posLong = pos.asLong();
    }

    private PlaceKilnPacket(long posLong) {
        this.posLong = posLong;
    }

    static void encode(PlaceKilnPacket msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.posLong);
    }

    static PlaceKilnPacket decode(FriendlyByteBuf buf) {
        return new PlaceKilnPacket(buf.readLong());
    }

    static void handle(PlaceKilnPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            BlockPos pos = BlockPos.of(msg.posLong);
            if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64)
                return;

            Level world = player.level();
            BlockPos above = pos.above();

            if (!world.getBlockState(pos).isFaceSturdy(world, pos, Direction.UP)
                    || !world.getBlockState(above).canBeReplaced()) return;

            // 检查双手
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);
                if (PotteryKilnRecipe.isValidInput(stack, world)) {
                    world.setBlockAndUpdate(above, ModBlockRegistry.Kiln.defaultBlockState());
                    TilePotteryKiln tile = (TilePotteryKiln) world.getBlockEntity(above);
                    if (tile != null) {
                        player.setItemInHand(hand,
                                tile.potteryStackHandler.insertItem(0, stack, false));
                    }
                    world.playSound(null, pos, SoundEvents.GRAVEL_PLACE,
                            SoundSource.BLOCKS, 1F, 1F);
                    player.swing(hand, true);
                    return;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
