package charcoalPit.block;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.tile.TileActivePile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockCoalPile extends Block {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public BlockCoalPile() {
		super(Properties.from(Blocks.COAL_BLOCK));
		this.setDefaultState(this.stateContainer.getBaseState().with(LIT, Boolean.valueOf(false)));
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		if (state.get(LIT))
			return 0;
		return 5;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileActivePile(true);
	}

	@Override
	public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
								boolean isMoving) {
		if (worldIn.getBlockState(fromPos).getBlock() == Blocks.FIRE) {
			if (!state.get(LIT))
				igniteCoal(worldIn, pos);
		} else if (state.get(LIT)) {
			((TileActivePile) worldIn.getTileEntity(pos)).isValid = false;
		}
	}

	public static void igniteCoal(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == ModBlockRegistry.CoalPile && !state.get(BlockStateProperties.LIT)) {
			world.setBlockState(pos, state.with(LIT, true), 2);
			Direction[] neighbors = Direction.values();
			for (int i = 0; i < neighbors.length; i++) {
				igniteCoal(world, pos.offset(neighbors[i]));
			}
		}
	}
}
