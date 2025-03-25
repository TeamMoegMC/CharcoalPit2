package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.block.BlockBloomery;
import charcoalPit.block.BlockCeramicPot;
import charcoalPit.block.BlockCoalPile;
import charcoalPit.recipe.BloomeryRecipe;
import charcoalPit.recipe.PotteryKilnRecipe;
import charcoalPit.tile.TileBloomery;
import charcoalPit.tile.TilePotteryKiln;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.FORGE)
public class PileIgnitr {
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void placeKiln(PlayerInteractEvent.RightClickBlock event) {
		//undye pots
		Level world = event.getLevel();
		if (!world.isClientSide) {
//			if (world.getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON &&
//					world.getBlockState(event.getPos()).getValue(CauldronBlock.LEVEL) > 0) {
//				Block block = Block.byItem(event.getItemStack().getItem());
//				if (block instanceof BlockCeramicPot && block != ModBlockRegistry.CeramicPot) {
//					ItemStack stack = new ItemStack(ModBlockRegistry.CeramicPot, 1);
//					stack.setTag(event.getItemStack().getTag());
//					event.getPlayer().setItemInHand(event.getHand(), stack);
//					world.setBlockAndUpdate(event.getPos(), Blocks.CAULDRON.defaultBlockState().setValue(CauldronBlock.LEVEL, world.getBlockState(event.getPos()).getValue(CauldronBlock.LEVEL) - 1));
//					event.setUseBlock(Result.DENY);
//					event.setUseItem(Result.DENY);
//				}
//			}
			//place bloomery
			if (event.getEntity().isShiftKeyDown()) {
				if (PotteryKilnRecipe.isValidInput(event.getItemStack(), world) &&
						event.getFace() == Direction.UP && world.getBlockState(event.getPos()).isFaceSturdy(world, event.getPos(), Direction.UP) &&
						world.getBlockState(event.getPos().relative(Direction.UP)).canBeReplaced()) {
					world.setBlockAndUpdate(event.getPos().relative(Direction.UP), ModBlockRegistry.Kiln.defaultBlockState());
					TilePotteryKiln tile = ((TilePotteryKiln) world.getBlockEntity(event.getPos().relative(Direction.UP)));
					event.getEntity().setItemInHand(event.getHand(), tile.potteryStackHandler.insertItem(0, event.getItemStack(), false));
//					world.sendBlockUpdated(event.getPos().relative(Direction.UP), world.getBlockState(event.getPos().relative(Direction.UP)), world.getBlockState(event.getPos().relative(Direction.UP)), 3);

					world.playSound(null, event.getPos(), SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 1F, 1F);
					event.setUseBlock(Result.DENY);
					event.setUseItem(Result.DENY);
				} else if (BloomeryRecipe.getRecipe(event.getItemStack(), world) != null &&
						event.getFace() == Direction.UP && world.getBlockState(event.getPos()).is(BlockTags.create(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls"))) &&
						world.getBlockState(event.getPos().relative(Direction.UP)).canBeReplaced() &&
						MethodHelper.Bloomery2ValidPosition(world, event.getPos().relative(Direction.UP), false, false)) {
					world.setBlockAndUpdate(event.getPos().relative(Direction.UP), ModBlockRegistry.Bloomery.defaultBlockState().setValue(BlockBloomery.STAGE, 1));
					TileBloomery tile = ((TileBloomery) world.getBlockEntity(event.getPos().relative(Direction.UP)));
					tile.recipe = BloomeryRecipe.getRecipe(event.getItemStack(), world);
					event.getEntity().setItemInHand(event.getHand(), tile.ore.insertItem(0, event.getItemStack(), false));
					world.playSound(null, event.getPos(), SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 1F, 1F);
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
					BlockCoalPile.igniteCoal(world, event.getPos());
				}
			}
		}

	}
	@SubscribeEvent
	public static void savePotInventory(ItemCraftedEvent event) {
		if(Block.byItem(event.getCrafting().getItem()) instanceof BlockCeramicPot) {
			for(int i=0;i<event.getInventory().getContainerSize();i++) {
				if(Block.byItem(event.getInventory().getItem(i).getItem()) instanceof BlockCeramicPot) {
					event.getCrafting().setTag(event.getInventory().getItem(i).getTag());
					return;
				}
			}
		}
	}
}
