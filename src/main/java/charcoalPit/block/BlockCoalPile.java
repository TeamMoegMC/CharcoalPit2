package charcoalPit.block;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.tile.TileActivePile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockCoalPile extends Block {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public BlockCoalPile() {
		super(Properties.copy(Blocks.COAL_BLOCK));
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TileActivePile(true);
	}

	@Override
	public boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
								boolean isMoving) {
		if (worldIn.getBlockState(fromPos).getBlock() == Blocks.FIRE) {
			if (!state.getValue(LIT))
				igniteCoal(worldIn, pos);
		} else if (state.getValue(LIT)) {
			((TileActivePile) worldIn.getBlockEntity(pos)).isValid = false;
		}
	}

	public static void igniteCoal(LevelAccessor world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == ModBlockRegistry.CoalPile && !state.getValue(BlockStateProperties.LIT)) {
			world.setBlock(pos, state.setValue(LIT, true), 2);
			Direction[] neighbors = Direction.values();
			for (int i = 0; i < neighbors.length; i++) {
				igniteCoal(world, pos.relative(neighbors[i]));
			}
		}
	}
}
