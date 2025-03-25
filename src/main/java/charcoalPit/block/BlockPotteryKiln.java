package charcoalPit.block;

import charcoalPit.CharcoalPit;
import charcoalPit.core.Config;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.PotteryKilnRecipe;
import charcoalPit.tile.TilePotteryKiln;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class BlockPotteryKiln extends Block implements EntityBlock {
	
	public static final EnumProperty<EnumKilnTypes> TYPE=EnumProperty.create("kiln_type", EnumKilnTypes.class);
	
	public static final VoxelShape EMPTY=Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
	public static final VoxelShape THATCH=Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 10D/16D, 1.0D);
	public static final VoxelShape COMPLETE=Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);

	public BlockPotteryKiln() {
		super(Properties.of().mapColor(MapColor.COLOR_RED));
	}
	
	@Override
	public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
		switch(state.getValue(TYPE)) {
		case ACTIVE:return SoundType.WOOD;
		case COMPLETE:return SoundType.SAND;
		case EMPTY:return SoundType.GRAVEL;
		case THATCH:return SoundType.GRASS;
		case WOOD:return SoundType.WOOD;
		default:return SoundType.GRAVEL;
		}
	}
	
	public float getHardness(BlockState state) {
		switch(state.getValue(TYPE)) {
		case ACTIVE:return 2F;
		case COMPLETE:return 0.5F;
		case EMPTY:return 0F;
		case THATCH:return 0.5F;
		case WOOD:return 2F;
		default:return 0F;
		}
	}
	
	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn,
			BlockPos pos) {
		float f = getHardness(state);
	      if (f == -1.0F) {
	         return 0.0F;
	      } else {
	         int i = net.minecraftforge.common.ForgeHooks.isCorrectToolForDrops(state, player) ? 30 : 100;
	         return player.getDigSpeed(state, pos) / f / (float)i;
	      }
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch(state.getValue(TYPE)) {
		case COMPLETE:return COMPLETE;
		case EMPTY:return EMPTY;
		case THATCH:return THATCH;
		default:return Shapes.block();
		}
	}
	
	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return !(state.getValue(TYPE)==EnumKilnTypes.ACTIVE||state.getValue(TYPE)==EnumKilnTypes.WOOD);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(TYPE);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.relative(Direction.DOWN)).isFaceSturdy(worldIn, pos.relative(Direction.DOWN), Direction.UP);
	}
	
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if(facing==Direction.DOWN&&!canSurvive(stateIn, worldIn, currentPos))return Blocks.AIR.defaultBlockState();
		return stateIn;
	}
	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		if (state.getValue(TYPE) == EnumKilnTypes.ACTIVE) {
			if (pos.relative(Direction.UP).equals(fromPos)) {
				if (!(worldIn.getBlockState(fromPos).getBlock() == Blocks.FIRE)) {
					((TilePotteryKiln) worldIn.getBlockEntity(pos)).isValid = false;
				}
			} else {
				((TilePotteryKiln) worldIn.getBlockEntity(pos)).isValid = false;
			}
		} else if (worldIn.getBlockState(fromPos).getBlock() == Blocks.FIRE) {
			if (state.getValue(BlockPotteryKiln.TYPE) == EnumKilnTypes.WOOD)
				ignitePottery(worldIn, pos);
		}
	}
	
	@Override
	public boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
		return state.getValue(TYPE)==EnumKilnTypes.ACTIVE;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return state.getValue(TYPE)==EnumKilnTypes.EMPTY?Shapes.empty():super.getOcclusionShape(state, worldIn, pos);
	}
	
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
			((TilePotteryKiln)worldIn.getBlockEntity(pos)).dropInventory();
			worldIn.removeBlockEntity(pos);
	    }
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TilePotteryKiln(pos,state);
	}
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTileRegistry.PotteryKiln, TilePotteryKiln::tick);
	}
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type, BlockEntityType<E> entitytype, BlockEntityTicker<? super E> ticker) {
		return type == entitytype ? (BlockEntityTicker<A>) ticker : null;
	}
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
			InteractionHand handIn, BlockHitResult hit) {
		ItemStack itemStack = player.getItemInHand(handIn);
		switch (state.getValue(TYPE)) {
			case EMPTY: {
				if (player.getMainHandItem().isEmpty()) {
					if (worldIn.isClientSide) {
						return InteractionResult.SUCCESS;
					}
					TilePotteryKiln tile = ((TilePotteryKiln) worldIn.getBlockEntity(pos));
					player.setItemInHand(handIn, tile.potteryStackHandler.extractItem(0, 8, false));
					worldIn.destroyBlock(pos, true);
					return InteractionResult.SUCCESS;
				} else {
					if (PotteryKilnRecipe.isValidInput(itemStack, worldIn)) {
						if (worldIn.isClientSide) {
							return InteractionResult.SUCCESS;
						}
						TilePotteryKiln tile = ((TilePotteryKiln) worldIn.getBlockEntity(pos));
						player.setItemInHand(handIn, tile.potteryStackHandler.insertItem(0, itemStack, false));
						worldIn.sendBlockUpdated(pos, state, state, 2);
						return InteractionResult.SUCCESS;
					} else {
						if (itemStack.is(ItemTags.create((new ResourceLocation(CharcoalPit.MODID, "kiln_straw"))))) {
							if (!worldIn.isClientSide) {
								if (itemStack.getCount() >= Config.StrawAmount.get()) {
									itemStack.setCount(itemStack.getCount() - Config.StrawAmount.get());
									worldIn.setBlockAndUpdate(pos, this.defaultBlockState().setValue(TYPE, EnumKilnTypes.THATCH));
									worldIn.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1F, 1F);
								} else
									player.displayClientMessage(Component.translatable("message.charcoal_pit.pottery_kiln"), true);
							}
							return InteractionResult.SUCCESS;
						}
						return InteractionResult.FAIL;
					}
			}
		}
		case THATCH: {
			if (itemStack.is(ItemTags.LOGS_THAT_BURN)) {
				if (!worldIn.isClientSide) {
					if (itemStack.getCount() >= Config.WoodAmount.get()) {
						itemStack.setCount(itemStack.getCount() - Config.WoodAmount.get());
						worldIn.setBlockAndUpdate(pos, this.defaultBlockState().setValue(TYPE, EnumKilnTypes.WOOD));
						worldIn.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1F, 1F);
					} else
						player.displayClientMessage(Component.translatable("message.charcoal_pit.pottery_kiln"), true);
				}
				return InteractionResult.SUCCESS;
			}else
				return InteractionResult.FAIL;
		}
		default:{
			return InteractionResult.FAIL;
		}
		}
	}
	
	public enum EnumKilnTypes implements StringRepresentable{
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
		public String getSerializedName() {
			return name;
		}

	}

	public static void ignitePottery(LevelAccessor world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() == ModBlockRegistry.Kiln &&
				world.getBlockState(pos).getValue(BlockPotteryKiln.TYPE) == EnumKilnTypes.WOOD) {
			world.setBlock(pos, ModBlockRegistry.Kiln.defaultBlockState().setValue(BlockPotteryKiln.TYPE, EnumKilnTypes.ACTIVE), 3);
			((TilePotteryKiln) world.getBlockEntity(pos)).setActive(true);
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					ignitePottery(world, new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z));
				}
			}
		}
	}

}
