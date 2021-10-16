package charcoalPit.block;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.tile.TileActivePile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
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
import net.minecraftforge.common.ToolType;

public class BlockLogPile extends RotatedPillarBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public BlockLogPile() {
		super(Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2F).harvestTool(ToolType.AXE).sound(SoundType.WOOD));
		this.setDefaultState(this.stateContainer.getBaseState().with(LIT, Boolean.valueOf(false)));
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 5;
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 5;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LIT, AXIS);
	}


	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileActivePile(false);
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
				igniteLogs(worldIn, pos);
		}
		((TileActivePile) worldIn.getTileEntity(pos)).isValid = false;
	}

	public static void igniteLogs(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == ModBlockRegistry.LogPile && !state.get(BlockStateProperties.LIT)) {
			world.setBlockState(pos, state.with(LIT, true), 2);
			Direction[] neighbors = Direction.values();
			for (int i = 0; i < neighbors.length; i++) {
				igniteLogs(world, pos.offset(neighbors[i]));
			}
		}
	}
}
