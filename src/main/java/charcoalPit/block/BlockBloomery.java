package charcoalPit.block;

import charcoalPit.CharcoalPit;
import charcoalPit.core.Config;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.tile.TileActivePile;
import charcoalPit.tile.TileBloomery;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;

public class BlockBloomery extends Block implements EntityBlock {

	public static final IntegerProperty STAGE = IntegerProperty.create("stage", 1, 12);
	public static final BooleanProperty DUMMY = BooleanProperty.create("dummy");
	//1-8 layers,9 active,10 done,11 worked,12 cooled

	protected static final VoxelShape[] SHAPES = new VoxelShape[]{Shapes.empty(),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	public BlockBloomery() {
		super(Properties.of().mapColor(MapColor.STONE).strength(3.5F, 6F).lightLevel((state)->{
			int i=state.getValue(STAGE);
			if(i==9)
				return 15;
			if(i==10||i==11)
				return 9;
			return 0;}));
		this.registerDefaultState(this.defaultBlockState().setValue(DUMMY,false));
	}
	
	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
		if(Config.EnableBloomeryPickRequirement.get()&&
				state.getValue(STAGE)>9&&
				player.getMainHandItem().is(ItemTags.create((new ResourceLocation(CharcoalPit.MODID, "blacklist_pickaxes")))))
			return false;
		return super.canHarvestBlock(state, world, pos, player);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPES[Math.min(8, state.getValue(STAGE))];
	}
	
	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return state.getValue(STAGE)<8;
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(STAGE,DUMMY);
	}

	
	@Override
	public boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
		return state.getValue(STAGE)==9;
	}
	
	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		if (worldIn.getBlockState(fromPos).getBlock() == Blocks.FIRE) {
            if (state.getValue(BlockBloomery.STAGE) == 8)
                igniteBloomery(worldIn, pos);
        } else if (state.getValue(BlockBloomery.STAGE) == 9) {
            ((TileBloomery) worldIn.getBlockEntity(pos)).isValid = false;
        }
	}
	
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
			 ((TileBloomery)worldIn.getBlockEntity(pos)).dropInventory();
	         worldIn.removeBlockEntity(pos);
			if(!state.getValue(BlockBloomery.DUMMY)) {
				if(worldIn.getBlockState(pos.relative(Direction.UP)).getBlock()==ModBlockRegistry.Bloomery)
					worldIn.removeBlock(pos.relative(Direction.UP), false);
			}
	      }
	}
	
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player,
			boolean willHarvest, FluidState fluid) {
		if(state.getValue(STAGE)==10) {
			state.getBlock().playerWillDestroy(world, pos, state, player);
			player.causeFoodExhaustion(0.01F);
			world.playSound(player, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1F, 1F);
			TileBloomery tile=((TileBloomery)world.getBlockEntity(pos));
			tile.work();
			return false;
		}else
			return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
	}
	
	public void stepOn(Level worldIn, BlockPos pos, Entity entityIn) {
	      if (!entityIn.fireImmune() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn) && worldIn.getBlockState(pos).getValue(STAGE)>=10 
	    		  && worldIn.getBlockState(pos).getValue(STAGE)<12) {
	         entityIn.hurt(worldIn.damageSources().hotFloor(), 1.0F);
	      }

	      super.stepOn(worldIn, pos, worldIn.getBlockState(pos),entityIn);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
		if(stateIn.getValue(STAGE)!=9)
			return;
		double centerX = pos.getX() + 0.5F;
		double centerY = pos.getY() + 2F;
		double centerZ = pos.getZ() + 0.5F;
		//double d3 = 0.2199999988079071D;
		//double d4 = 0.27000001072883606D;
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY-1, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY-1, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileBloomery(pos,state);
	}
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTileRegistry.Bloomery2,TileBloomery::tick);
	}
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type, BlockEntityType<E> entitytype, BlockEntityTicker<? super E> ticker) {
		return type == entitytype ? (BlockEntityTicker<A>) ticker : null;
	}
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
			InteractionHand handIn, BlockHitResult hit) {
		int size=state.getValue(STAGE);
		if(!worldIn.isClientSide) {
			TileBloomery tile=((TileBloomery)worldIn.getBlockEntity(pos));
			if(tile.getRecipe().input.test(player.getItemInHand(handIn))) {
				if(size<8) {
					for(int i=0;i<tile.ore.getSlots();i++) {
						if(tile.ore.getStackInSlot(i).isEmpty()) {
							player.setItemInHand(handIn, tile.ore.insertItem(i, player.getItemInHand(handIn), false));
							worldIn.setBlockAndUpdate(pos, state.setValue(STAGE, size+1));
							worldIn.playSound(null, pos, SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 1F, 1F);
							return InteractionResult.SUCCESS;
						}
					}
				}else {
					BlockPos up = pos.relative(Direction.UP);
					if (worldIn.isEmptyBlock(up) && MethodHelper.Bloomery2ValidPosition(worldIn, up, true, false) && worldIn.getBlockState(pos.relative(Direction.DOWN)).getBlock() != this) {
						worldIn.setBlockAndUpdate(up, state.setValue(STAGE, 1).setValue(DUMMY,true));
						TileBloomery dummy = ((TileBloomery) worldIn.getBlockEntity(up));
						dummy.recipe = tile.recipe;
						player.setItemInHand(handIn, dummy.ore.insertItem(0, player.getItemInHand(handIn), false));
						worldIn.playSound(null, pos, SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 0.6F, 1F);
						return InteractionResult.SUCCESS;
					}
				}
			}else {
				if(player.getItemInHand(handIn).is(ItemTags.create((new ResourceLocation(CharcoalPit.MODID, "basic_fuels"))))) {
					if(size<8) {
						for(int i=0;i<tile.fuel.getSlots();i++) {
							if(tile.fuel.getStackInSlot(i).isEmpty()) {
								player.setItemInHand(handIn, tile.fuel.insertItem(i, player.getItemInHand(handIn), false));
								worldIn.setBlockAndUpdate(pos, state.setValue(STAGE, size+1));
								worldIn.playSound(null, pos, SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 1F, 1F);
								return InteractionResult.SUCCESS;
							}
						}
					}else {
						BlockPos up = pos.relative(Direction.UP);
						if (worldIn.isEmptyBlock(up) && MethodHelper.Bloomery2ValidPosition(worldIn, up, true, false) && worldIn.getBlockState(pos.relative(Direction.DOWN)).getBlock() != this) {
							worldIn.setBlockAndUpdate(up, state.setValue(STAGE, 1).setValue(DUMMY,true));
							TileBloomery dummy = ((TileBloomery) worldIn.getBlockEntity(up));
							dummy.recipe = tile.recipe;
							player.setItemInHand(handIn, dummy.fuel.insertItem(0, player.getItemInHand(handIn), false));
							worldIn.playSound(null, pos, SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 1F, 1F);
							return InteractionResult.SUCCESS;
						}
					}
				}
			}
			return InteractionResult.FAIL;
		} else {
			if (size>=8){
				return InteractionResult.FAIL;
			}
			else return InteractionResult.SUCCESS;
		}
	}

	public static void igniteBloomery(LevelAccessor world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() == ModBlockRegistry.Bloomery &&
				world.getBlockState(pos).getValue(BlockBloomery.STAGE) == 8) {
			world.setBlock(pos, world.getBlockState(pos).setValue(BlockBloomery.STAGE, 9), 3);
			TileBloomery tile = ((TileBloomery) world.getBlockEntity(pos));
			tile.burnTime = Config.BloomeryTime.get() * 2;
			tile.airTicks = Config.BloomeryTime.get();
			if (world.getBlockState(pos).getValue(DUMMY)) {
				igniteBloomery(world, pos.relative(Direction.DOWN));
			}
		}
	}
}
