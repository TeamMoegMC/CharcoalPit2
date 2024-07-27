package charcoalPit.block;

import java.util.ArrayList;
import java.util.List;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.tile.TileBloom;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockBloom extends Block{

	public static final IntegerProperty LAYER=BlockStateProperties.LAYERS;
	public static final BooleanProperty HOT=BooleanProperty.create("hot");
	public static final BooleanProperty FAIL=BooleanProperty.create("fail");
	public static final BooleanProperty DOUBLE=BooleanProperty.create("double");
	
	public BlockBloom() {
		super(Properties.of(Material.STONE).strength(5, 6).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().sound(SoundType.ANVIL));
	}
	
	@Override
	public int getLightValue(BlockState state, BlockGetter world, BlockPos pos) {
		return state.getValue(HOT)?15:0;
	}
	
	public void stepOn(Level worldIn, BlockPos pos, Entity entityIn) {
	      if (!entityIn.fireImmune() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn) && worldIn.getBlockState(pos).getValue(HOT)) {
	         entityIn.hurt(DamageSource.HOT_FLOOR, 1.0F);
	      }

	      super.stepOn(worldIn, pos, entityIn);
	   }
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
	      builder.add(LAYER,HOT,FAIL,DOUBLE);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TileBloom();
	}
	
	@Override
	public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player,
			boolean willHarvest, FluidState fluid) {
		if(state.getValue(LAYER)>1&&state.getValue(HOT)&&!state.getValue(FAIL)) {
			getBlock().playerWillDestroy(world, pos, state, player);
			world.setBlock(pos, state.setValue(LAYER, state.getValue(LAYER)-1), 5);
			player.causeFoodExhaustion(0.01F);
			world.playSound(player, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1F, 1F);
			return false;
		}else {
			if(state.getValue(HOT))
				world.playSound(player, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1F, 1F);
			return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		}
		
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		if(state.getValue(FAIL)) {
			ArrayList<ItemStack> drops=new ArrayList<>();
			drops.add(new ItemStack(ModItemRegistry.BloomFail, state.getValue(DOUBLE)?8:4));
			return drops;
		}else {
			int i=state.getValue(DOUBLE)?8:4;
			int j=i;
			i-=state.getValue(LAYER);
			if(state.getValue(HOT))
				i++;
			ArrayList<ItemStack> drops=new ArrayList<>();
			drops.add(new ItemStack(Items.IRON_INGOT, i));
			drops.add(new ItemStack(ModItemRegistry.BloomCool, j-i));
			return drops;
		}
	}

}
