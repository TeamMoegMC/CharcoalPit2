package charcoalPit.item;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Item.Properties;

public class BlockItemFuel extends BlockItem{

	public int burnTime;
	public BlockItemFuel(Block blockIn, Properties builder) {
		super(blockIn, builder);
		// TODO Auto-generated constructor stub
	}
	
	public BlockItemFuel setBurnTime(int t) {
		burnTime=t;
		return this;
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
		return burnTime;
	}

}
