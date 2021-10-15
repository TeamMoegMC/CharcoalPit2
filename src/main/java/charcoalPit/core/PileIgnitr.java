package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.block.BlockBloomery;
import charcoalPit.block.BlockCeramicPot;
import charcoalPit.block.BlockPotteryKiln;
import charcoalPit.block.BlockPotteryKiln.EnumKilnTypes;
import charcoalPit.recipe.BloomeryRecipe;
import charcoalPit.recipe.PotteryKilnRecipe;
import charcoalPit.tile.TileBloomery2;
import charcoalPit.tile.TilePotteryKiln;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.FORGE)
public class PileIgnitr {
	
	@SubscribeEvent
	public static void checkIgnition(NeighborNotifyEvent event) {
		if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.FIRE) {
			for (Direction facing : event.getNotifiedSides()) {
				BlockPos pos = event.getPos().offset(facing);
				if (event.getWorld().getBlockState(pos).getBlock() == ModBlockRegistry.LogPile) {
					//found log pile to ignite
					igniteLogs(event.getWorld(), pos);

				} else if (event.getWorld().getBlockState(pos).getBlock() == ModBlockRegistry.CoalPile) {
					//found coal pile to ignite
					igniteCoal(event.getWorld(), pos);
				} else if (facing == Direction.DOWN && event.getWorld().getBlockState(pos).getBlock() == ModBlockRegistry.Kiln) {
					//found pottery kiln to ignite
					ignitePottery(event.getWorld(), pos);
				} else if (event.getWorld().getBlockState(pos).getBlock() == ModBlockRegistry.Bloomery) {
					//found bloomery to ignite
					igniteBloomery(event.getWorld(), pos);
				}
			}
		}
	}
	public static void igniteLogs(IWorld world, BlockPos pos){
		if(world.getBlockState(pos).getBlock()==ModBlockRegistry.LogPile) {
			world.setBlockState(pos, ModBlockRegistry.LogPile.getDefaultState().with(BlockStateProperties.AXIS, world.getBlockState(pos).get(BlockStateProperties.AXIS))
					.with(BlockStateProperties.LIT, true), 2);
			Direction[] neighbors = Direction.values();
			for (int i = 0; i < neighbors.length; i++) {
				igniteLogs(world, pos.offset(neighbors[i]));
			}
		}
	}
	public static void igniteCoal(IWorld world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() == ModBlockRegistry.CoalPile) {
			world.setBlockState(pos, ModBlockRegistry.CoalPile.getDefaultState().with(BlockStateProperties.LIT, true), 2);
			Direction[] neighbors = Direction.values();
			for (int i = 0; i < neighbors.length; i++) {
				igniteCoal(world, pos.offset(neighbors[i]));
			}
		}
	}
	public static void ignitePottery(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock()==ModBlockRegistry.Kiln&&
				world.getBlockState(pos).get(BlockPotteryKiln.TYPE)==EnumKilnTypes.WOOD) {
			world.setBlockState(pos, ModBlockRegistry.Kiln.getDefaultState().with(BlockPotteryKiln.TYPE, EnumKilnTypes.ACTIVE), 3);
			((TilePotteryKiln)world.getTileEntity(pos)).setActive(true);
			for(int x=-1;x<=1;x++) {
				for(int z=-1;z<=1;z++) {
					ignitePottery(world, new BlockPos(pos.getX()+x, pos.getY(), pos.getZ()+z));
				}
			}
		}
	}
	public static void igniteBloomery(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock()==ModBlockRegistry.Bloomery&&
				world.getBlockState(pos).get(BlockBloomery.STAGE)==8) {
			world.setBlockState(pos, world.getBlockState(pos).with(BlockBloomery.STAGE, 9), 3);
			TileBloomery2 tile=((TileBloomery2)world.getTileEntity(pos));
			tile.burnTime=Config.BloomeryTime.get()*2;
			tile.airTicks=Config.BloomeryTime.get();
			if(tile.dummy) {
				igniteBloomery(world, pos.offset(Direction.DOWN));
			}
		}
	}
	/*public static boolean igniteBloomery(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock()==ModBlockRegistry.BloomeryPile) {
			world.setBlockState(pos, ModBlockRegistry.ActiveBloomery.getDefaultState(), 3);
			if(igniteBloomery(world, pos.offset(Direction.DOWN))) {
				((TileBloomery)world.getTileEntity(pos)).setDummy(true);
			}else
				((TileBloomery)world.getTileEntity(pos)).setDummy(false);
			return true;
		}
		return false;
	}*/
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void placeKiln(PlayerInteractEvent.RightClickBlock event) {
		//undye pots
		World world = event.getWorld();
		if (!world.isRemote) {
			if (world.getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON &&
					world.getBlockState(event.getPos()).get(CauldronBlock.LEVEL) > 0) {
				Block block = Block.getBlockFromItem(event.getItemStack().getItem());
				if (block instanceof BlockCeramicPot && block != ModBlockRegistry.CeramicPot) {
					ItemStack stack = new ItemStack(ModBlockRegistry.CeramicPot, 1);
					stack.setTag(event.getItemStack().getTag());
					event.getPlayer().setHeldItem(event.getHand(), stack);
					world.setBlockState(event.getPos(), Blocks.CAULDRON.getDefaultState().with(CauldronBlock.LEVEL, world.getBlockState(event.getPos()).get(CauldronBlock.LEVEL) - 1));
					event.setUseBlock(Result.DENY);
					event.setUseItem(Result.DENY);
				}
			}
			//place bloomery
			if (event.getPlayer().isSneaking()) {
				if (PotteryKilnRecipe.isValidInput(event.getItemStack(), world) &&
						event.getFace() == Direction.UP && world.getBlockState(event.getPos()).isSolidSide(world, event.getPos(), Direction.UP) &&
						world.getBlockState(event.getPos().offset(Direction.UP)).getMaterial().isReplaceable()) {
					world.setBlockState(event.getPos().offset(Direction.UP), ModBlockRegistry.Kiln.getDefaultState());
					TilePotteryKiln tile = ((TilePotteryKiln) world.getTileEntity(event.getPos().offset(Direction.UP)));
					event.getPlayer().setHeldItem(event.getHand(), tile.pottery.insertItem(0, event.getItemStack(), false));
					world.playSound(null, event.getPos(), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
					event.setUseBlock(Result.DENY);
					event.setUseItem(Result.DENY);
				} else if (BloomeryRecipe.getRecipe(event.getItemStack(), world) != null &&
						event.getFace() == Direction.UP && world.getBlockState(event.getPos()).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls"))) &&
						world.getBlockState(event.getPos().offset(Direction.UP)).getMaterial().isReplaceable() &&
						MethodHelper.Bloomery2ValidPosition(world, event.getPos().offset(Direction.UP), false, false)) {
					world.setBlockState(event.getPos().offset(Direction.UP), ModBlockRegistry.Bloomery.getDefaultState().with(BlockBloomery.STAGE, 1));
					TileBloomery2 tile = ((TileBloomery2) world.getTileEntity(event.getPos().offset(Direction.UP)));
					tile.recipe = BloomeryRecipe.getRecipe(event.getItemStack(), world);
					event.getPlayer().setHeldItem(event.getHand(), tile.ore.insertItem(0, event.getItemStack(), false));
					world.playSound(null, event.getPos(), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
					event.setUseBlock(Result.DENY);
					event.setUseItem(Result.DENY);

				}
		/*if(!event.isCanceled()&&event.getPlayer().isSneaking()&&event.getItemStack().getItem().isIn(Tags.Items.ORES_IRON)&&
				event.getFace()==Direction.UP&&event.getWorld().getBlockState(event.getPos()).isSolidSide(event.getWorld(), event.getPos(), Direction.UP)&&
				event.getWorld().getBlockState(event.getPos().offset(Direction.UP)).getMaterial().isReplaceable()&&
				MethodHelper.BloomeryIsValidPosition(event.getWorld(), event.getPos().offset(Direction.UP),true)) {
			if(!event.getWorld().isRemote) {
				event.getWorld().setBlockState(event.getPos().offset(Direction.UP), ModBlockRegistry.BloomeryPile.getDefaultState());
				event.getPlayer().getHeldItem(event.getHand()).shrink(1);
				event.getWorld().playSound(null, event.getPos(), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
			}
			event.setUseBlock(Result.DENY);
			event.setUseItem(Result.DENY);
		}*/
			}
			if (world.getBlockState(event.getPos()).getBlock() == ModBlockRegistry.CoalPile) {
				Item item = event.getItemStack().getItem();
				if (item == Items.FLINT_AND_STEEL || item == ModItemRegistry.FireStarter) {
					igniteCoal(world, event.getPos());
				}
			}
		}

	}
	@SubscribeEvent
	public static void savePotInventory(ItemCraftedEvent event) {
		if(Block.getBlockFromItem(event.getCrafting().getItem()) instanceof BlockCeramicPot) {
			for(int i=0;i<event.getInventory().getSizeInventory();i++) {
				if(Block.getBlockFromItem(event.getInventory().getStackInSlot(i).getItem()) instanceof BlockCeramicPot) {
					event.getCrafting().setTag(event.getInventory().getStackInSlot(i).getTag());
					return;
				}
			}
		}
	}
}
