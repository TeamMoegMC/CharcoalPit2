package charcoalPit.block;

import charcoalPit.fluid.ModFluidRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class BlockAlcohol extends LiquidBlock{
	
	public BlockAlcohol() {
		super(()->ModFluidRegistry.AlcoholStill.get(), Properties.of().mapColor(MapColor.WATER).noCollission().strength(100.0F).noLootTable());
	}
	
	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		worldIn.scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(worldIn));
	}
	
	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		worldIn.scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(worldIn));
	}
}
