package charcoalPit.block;

import java.util.List;

import charcoalPit.core.ModTileRegistry;
import charcoalPit.tile.TileActivePile;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.lwjgl.glfw.GLFW;

import charcoalPit.tile.TileCreosoteCollector;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;

public class BlockCreosoteCollector extends Block implements EntityBlock {

	public BlockCreosoteCollector(Properties properties) {
		super(properties);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_1"));
			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_2"));
			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_3"));
//			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_4"));
			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_5"));
			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_6"));
			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_7"));
			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_8"));
//			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_9"));
		}else {
			tooltip.add(Component.translatable("info.charcoal_pit.creosotecollector_0"));
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileCreosoteCollector(pos,state);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
			InteractionHand handIn, BlockHitResult hit) {
		TileCreosoteCollector tile=(TileCreosoteCollector)worldIn.getBlockEntity(pos);
		if(worldIn.isClientSide)
			return player.getItemInHand(handIn).getCapability(ForgeCapabilities.FLUID_HANDLER, null).isPresent()?InteractionResult.SUCCESS:InteractionResult.FAIL;
		else {
			return FluidUtil.interactWithFluidHandler(player, handIn, tile.external)?InteractionResult.SUCCESS:InteractionResult.FAIL;
		}
	}
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTileRegistry.CreosoteCollector, TileCreosoteCollector::tick);
	}
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type, BlockEntityType<E> entitytype, BlockEntityTicker<? super E> ticker) {
		return type == entitytype ? (BlockEntityTicker<A>) ticker : null;
	}
}
