package charcoalPit.block;

import charcoalPit.CharcoalPit;
import charcoalPit.core.Config;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.recipe.PotteryKilnRecipe;
import charcoalPit.tile.TilePotteryKiln;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockPotteryKiln extends Block{
	
	public static final EnumProperty<EnumKilnTypes> TYPE=EnumProperty.create("kiln_type", EnumKilnTypes.class);
	
	public static final VoxelShape EMPTY=VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
	public static final VoxelShape THATCH=VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 10D/16D, 1.0D);
	public static final VoxelShape COMPLETE=VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);

	public BlockPotteryKiln() {
		super(Properties.create(Material.MISCELLANEOUS, MaterialColor.RED));
	}
	
	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		switch(state.get(TYPE)) {
		case ACTIVE:return SoundType.WOOD;
		case COMPLETE:return SoundType.SAND;
		case EMPTY:return SoundType.GROUND;
		case THATCH:return SoundType.PLANT;
		case WOOD:return SoundType.WOOD;
		default:return SoundType.GROUND;
		}
	}
	
	public float getHardness(BlockState state) {
		switch(state.get(TYPE)) {
		case ACTIVE:return 2F;
		case COMPLETE:return 0.5F;
		case EMPTY:return 0F;
		case THATCH:return 0.5F;
		case WOOD:return 2F;
		default:return 0F;
		}
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn,
			BlockPos pos) {
		float f = getHardness(state);
	      if (f == -1.0F) {
	         return 0.0F;
	      } else {
	         int i = net.minecraftforge.common.ForgeHooks.canHarvestBlock(state, player, worldIn, pos) ? 30 : 100;
	         return player.getDigSpeed(state, pos) / f / (float)i;
	      }
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch(state.get(TYPE)) {
		case COMPLETE:return COMPLETE;
		case EMPTY:return EMPTY;
		case THATCH:return THATCH;
		default:return VoxelShapes.fullCube();
		}
	}
	
	@Override
	public boolean isTransparent(BlockState state) {
		return !(state.get(TYPE)==EnumKilnTypes.ACTIVE||state.get(TYPE)==EnumKilnTypes.WOOD);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(TYPE);
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.offset(Direction.DOWN)).isSolidSide(worldIn, pos.offset(Direction.DOWN), Direction.UP);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if(facing==Direction.DOWN&&!isValidPosition(stateIn, worldIn, currentPos))return Blocks.AIR.getDefaultState();
		return stateIn;
	}
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		if (state.get(TYPE) == EnumKilnTypes.ACTIVE) {
			if (pos.offset(Direction.UP).equals(fromPos)) {
				if (!(worldIn.getBlockState(fromPos).getBlock() == Blocks.FIRE)) {
					((TilePotteryKiln) worldIn.getTileEntity(pos)).isValid = false;
				}
			} else {
				((TilePotteryKiln) worldIn.getTileEntity(pos)).isValid = false;
			}
		} else if (worldIn.getBlockState(fromPos).getBlock() == Blocks.FIRE) {
			if (state.get(BlockPotteryKiln.TYPE) == EnumKilnTypes.WOOD)
				ignitePottery(worldIn, pos);
		}
	}
	
	@Override
	public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
		return state.get(TYPE)==EnumKilnTypes.ACTIVE;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.get(TYPE)==EnumKilnTypes.EMPTY?VoxelShapes.empty():super.getRenderShape(state, worldIn, pos);
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			((TilePotteryKiln)worldIn.getTileEntity(pos)).dropInventory();
			worldIn.removeTileEntity(pos);
	    }
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TilePotteryKiln();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		ItemStack itemStack = player.getHeldItem(handIn);
		switch (state.get(TYPE)) {
			case EMPTY: {
				if (player.getHeldItemMainhand().isEmpty()) {
					if (worldIn.isRemote) {
						return ActionResultType.SUCCESS;
					}
					TilePotteryKiln tile = ((TilePotteryKiln) worldIn.getTileEntity(pos));
					player.setHeldItem(handIn, tile.pottery.extractItem(0, 8, false));
					worldIn.destroyBlock(pos, true);
					return ActionResultType.SUCCESS;
				} else {
					if (PotteryKilnRecipe.isValidInput(itemStack, worldIn)) {
						if (worldIn.isRemote) {
							return ActionResultType.SUCCESS;
						}
						TilePotteryKiln tile = ((TilePotteryKiln) worldIn.getTileEntity(pos));
						player.setHeldItem(handIn, tile.pottery.insertItem(0, itemStack, false));
						worldIn.notifyBlockUpdate(pos, state, state, 2);
						return ActionResultType.SUCCESS;
					} else {
						if (itemStack.getItem().isIn(ItemTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "kiln_straw")))) {
							if (worldIn.isRemote) {
								return ActionResultType.SUCCESS;
							}
							if (itemStack.getCount() >= Config.StrawAmount.get()) {
								itemStack.setCount(itemStack.getCount() - Config.StrawAmount.get());
								worldIn.setBlockState(pos, this.getDefaultState().with(TYPE, EnumKilnTypes.THATCH));
								worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1F, 1F);
								return ActionResultType.SUCCESS;
							}
							player.sendStatusMessage(new TranslationTextComponent("message.charcoal_pit.pottery_kiln"), true);
						} else {
							player.sendStatusMessage(new TranslationTextComponent("message.charcoal_pit.pottery_kiln"), true);
							return ActionResultType.FAIL;
						}
				}
			}
		}
		case THATCH: {
			if (itemStack.getItem().isIn(ItemTags.LOGS_THAT_BURN)) {
				if (worldIn.isRemote) {
					return ActionResultType.SUCCESS;
				}
				if (itemStack.getCount() >= Config.WoodAmount.get()) {
					itemStack.setCount(itemStack.getCount() - Config.WoodAmount.get());
					worldIn.setBlockState(pos, this.getDefaultState().with(TYPE, EnumKilnTypes.WOOD));
					worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1F, 1F);
					return ActionResultType.SUCCESS;
				}
				player.sendStatusMessage(new TranslationTextComponent("message.charcoal_pit.pottery_kiln"), true);
			}else
				return ActionResultType.FAIL;
		}
		default:{
			return ActionResultType.FAIL;
		}
		}
	}
	
	public enum EnumKilnTypes implements IStringSerializable{
		//stage 1
		EMPTY("empty"),
		//stage 2
		THATCH("thatch"),
		//stage 3
		WOOD("wood"),
		//stage 4
		ACTIVE("active"),
		//complete
		COMPLETE("complete");

		private String name;

		private EnumKilnTypes(String id) {
			name = id;
		}

		@Override
		public String getString() {
			return name;
		}

	}

	public static void ignitePottery(IWorld world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() == ModBlockRegistry.Kiln &&
				world.getBlockState(pos).get(BlockPotteryKiln.TYPE) == EnumKilnTypes.WOOD) {
			world.setBlockState(pos, ModBlockRegistry.Kiln.getDefaultState().with(BlockPotteryKiln.TYPE, EnumKilnTypes.ACTIVE), 3);
			((TilePotteryKiln) world.getTileEntity(pos)).setActive(true);
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					ignitePottery(world, new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z));
				}
			}
		}
	}

}
