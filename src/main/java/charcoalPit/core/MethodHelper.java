package charcoalPit.core;

import charcoalPit.CharcoalPit;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class MethodHelper {
	private static ResourceLocation cow=new ResourceLocation(CharcoalPit.MODID, "coke_oven_walls");
	public static boolean CharcoalPitIsValidBlock(Level world, BlockPos pos, Direction facing, boolean isCoke) {
		BlockState state=world.getBlockState(pos.relative(facing));
		if(state.isFlammable(world, pos.relative(facing), facing.getOpposite())) {
			return false;
		}
		if(isCoke&&!CokeOvenIsValidBlock(state)) {
			return false;
		}
		return state.isFaceSturdy(world, pos.relative(facing), facing.getOpposite());
	}
	
	public static boolean CokeOvenIsValidBlock(BlockState state) {
        Block block = state.getBlock();
        return block == ModBlockRegistry.CoalPile || ForgeRegistries.BLOCKS.tags().getTag(BlockTags.create(cow)).contains(block);
    }
	//for placement
	/*public static boolean BloomeryIsValidPosition(World world, BlockPos pos, boolean init) {
		for(Direction dir:Direction.Plane.HORIZONTAL) {
			if(!(world.getBlockState(pos.offset(dir)).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls")))))
				return false;
		}
		return (init&&world.getBlockState(pos.offset(Direction.DOWN)).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls"))))||
				(!init&&world.getBlockState(pos.offset(Direction.DOWN)).getBlock()==ModBlockRegistry.BloomeryPile);
	}
	//for active bloomery
	public static boolean BloomeryIsValidPosition(World world, BlockPos pos) {
		for(Direction dir:Direction.Plane.HORIZONTAL) {
			if(!(world.getBlockState(pos.offset(dir)).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls")))))
				return false;
		}
		if(!(world.getBlockState(pos.offset(Direction.DOWN)).getBlock()==ModBlockRegistry.ActiveBloomery||
				world.getBlockState(pos.offset(Direction.DOWN)).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls")))))
			return false;
		if(!(world.getBlockState(pos.offset(Direction.UP)).getBlock()==Blocks.FIRE||
				world.getBlockState(pos.offset(Direction.UP)).getBlock()==ModBlockRegistry.ActiveBloomery))
			return false;
		return true;
			
	}*/
	
	public static boolean Bloomery2ValidPosition(Level world, BlockPos pos, boolean dummy, boolean active) {
		for(Direction dir:Direction.Plane.HORIZONTAL) {
			if(!(world.getBlockState(pos.relative(dir)).is(BlockTags.create((new ResourceLocation(CharcoalPit.MODID, "bloomery_walls"))))))
				return false;
		}
		Block block=world.getBlockState(pos.relative(Direction.DOWN)).getBlock();
		if(		!(
                (!dummy && block == ModBlockRegistry.MainBloomery)
                        ||
                        (dummy && block == ModBlockRegistry.Bloomery)
        )
			)
			return false;
		block=world.getBlockState(pos.relative(Direction.UP)).getBlock();
		if(
				!(
					(!dummy && block==ModBlockRegistry.Bloomery)||
					(block==Blocks.FIRE)
					)&&active
			)
			return false;
		return true;
	}
	
	public static int calcRedstoneFromInventory(IItemHandler inv) {
	      if (inv == null) {
	         return 0;
	      } else {
	         int i = 0;
	         float f = 0.0F;

	         for(int j = 0; j < inv.getSlots(); ++j) {
	            ItemStack itemstack = inv.getStackInSlot(j);
	            if (!itemstack.isEmpty()) {
	               f += (float)itemstack.getCount() / (float)Math.min(inv.getSlotLimit(j), itemstack.getMaxStackSize());
	               ++i;
	            }
	         }

	         f = f / (float)inv.getSlots();
	         return Mth.floor(f * 14.0F) + (i > 0 ? 1 : 0);
	      }
	   }
	
	public static boolean isItemInString(String key, Item item) {
		if(key.startsWith("item:")) {
			return key.substring(5).equals(item.getRegistryName().toString());
		}
		if(key.startsWith("tag:")) {
			Tag<Item> tag= (Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(new ResourceLocation(key.substring(4))));
			return tag!=null&&tag.getValues().contains(item);
		}
		if(key.startsWith("ore:")) {
			Tag<Item> tag;
			String ore="forge:ores/".concat(key.substring(4));
			tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(new ResourceLocation(ore)));
			if(tag!=null&&tag.getValues().contains(item))
				return true;
			String ingot="forge:ingots/".concat(key.substring(4));
			tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(new ResourceLocation(ingot)));
			if(tag!=null&&tag.getValues().contains(item))
				return true;
			String dust="forge:dusts/".concat(key.substring(4));
			tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(new ResourceLocation(dust)));
			if(tag!=null&&tag.getValues().contains(item))
				return true;
		}
		return false;
	}

	public static boolean doesStringHaveItem(String key) {
		if(key.startsWith("item:")) {
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(key.substring(5)))!=null;
		}
		if(key.startsWith("tag:")) {
			return (Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create((new ResourceLocation(key.substring(4)))))!=null;
		}
		if(key.startsWith("ore:")) {
			Tag<Item> tag;
			String ore="forge:ores/".concat(key.substring(4));
			tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create((new ResourceLocation(ore))));
			if(tag!=null)
				return true;
			String ingot="forge:ingots/".concat(key.substring(4));
			tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create((new ResourceLocation(ingot))));
			if(tag!=null)
				return true;
			String dust="forge:dusts/".concat(key.substring(4));
			tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create((new ResourceLocation(dust))));
			if(tag!=null)
				return true;
		}
		return false;
	}

	public static Item getItemForString(String key) {
		if(key.startsWith("item:")) {
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(key.substring(5)));
		}
		if(key.startsWith("tag:")) {
			Tag<Item> tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create((new ResourceLocation(key.substring(4)))));
			return tag==null?null:tag.getValues().get(0);
		}
		if(key.contains("ore:")) {
			Tag<Item> tag;
			String ore="forge:ores/".concat(key.substring(4));
			tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(new ResourceLocation(ore)));
			if(tag!=null)
				return tag.getValues().get(0);
			String ingot="forge:ingots/".concat(key.substring(4));
			tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(new ResourceLocation(ingot)));
			if(tag!=null)
				return tag.getValues().get(0);
			String dust="forge:dusts/".concat(key.substring(4));
			tag=(Tag<Item>) ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(new ResourceLocation(dust)));
			if(tag!=null)
				return tag.getValues().get(0);
		}
		return null;
	}
	
}
