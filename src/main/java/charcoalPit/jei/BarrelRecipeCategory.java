package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.recipe.BarrelRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

public class BarrelRecipeCategory implements IRecipeCategory<BarrelRecipe> {
	
	public static final ResourceLocation ID=new ResourceLocation(CharcoalPit.MODID,"barrel_recipe");
	public final String loc_name;
	public final IDrawable backgroung;
	public final IDrawable icon;
	public final IDrawable overlay;
	public final IDrawable tank_overlay;
	public static final RecipeType<BarrelRecipe> BARREL_RECIPE_TYPE = new RecipeType<>(ID,BarrelRecipe.class);
	public BarrelRecipeCategory(IGuiHelper helper){
		loc_name= I18n.get("charcoal_pit.jei.barrel");
		backgroung=helper.createBlankDrawable(175,85);
		icon=helper.createDrawableItemStack(new ItemStack(ModBlockRegistry.Barrel));
		overlay=helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/barrel_recipe.png"),0,0,175,85);
		tank_overlay=helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/barrel_recipe.png"),176,0,16,71-13);
	}


	@Override
	public RecipeType<BarrelRecipe> getRecipeType() {
		return BARREL_RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.literal(loc_name);
	}
	
	@Override
	public IDrawable getBackground() {
		return backgroung;
	}
	
	@Override
	public IDrawable getIcon() {
		return icon;
	}
	
//	@Override
//	public void setIngredients(BarrelRecipe recipe, IIngredients iIngredients) {
//		/*ArrayList<ItemStack> list=new ArrayList<>();
//		for(ItemStack s:recipe.item_in.getMatchingStacks()){
//			list.add(new ItemStack(s.getItem(), recipe.in_amount));
//		}
//		iIngredients.setInputs(VanillaTypes.ITEM, list);*/
//		ArrayList<List<ItemStack>> list2=new ArrayList<>();
//		ItemStack stack = recipe.item_in.getItems()[0];
//		stack.setCount(recipe.in_amount);
//		list2.add(Arrays.asList(stack));
//		iIngredients.setInputLists(VanillaTypes.ITEM,list2);
//		iIngredients.setInput(ForgeTypes.FLUID_STACK,new FluidStack(recipe.fluid_in.getFluid(),recipe.fluid_in.amount,recipe.fluid_in.nbt));
//		if((recipe.flags&0b100)==0b100){
//			iIngredients.setOutput(VanillaTypes.ITEM,new ItemStack(recipe.item_out.getItems()[0].getItem(),recipe.out_amount,recipe.nbt_out));
//		}
//		if((recipe.flags&0b1000)==0b1000){
//			iIngredients.setOutput(ForgeTypes.FLUID_STACK,new FluidStack(recipe.fluid_out.getFluid(), recipe.fluid_out.amount,recipe.fluid_out.nbt));
//		}
//	}
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BarrelRecipe barrelRecipe, IFocusGroup focuses) {

		ItemStack[] stacks = barrelRecipe.item_in.getItems();
		ItemStack[] stacks2 = stacks;
		int var6 = stacks.length;

		int var7;
		for(var7 = 0; var7 < var6; ++var7) {
			ItemStack stack = stacks2[var7];
			stack.setCount(barrelRecipe.in_amount);
		}

		builder.addSlot(RecipeIngredientRole.INPUT, 80, 17).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(stacks).toList());
		builder.addSlot(RecipeIngredientRole.INPUT, 44, 14).addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(barrelRecipe.fluid_in.getFluid(),barrelRecipe.fluid_in.amount,barrelRecipe.fluid_in.nbt)).setFluidRenderer(Math.min(16000, barrelRecipe.fluid_in.amount * 2), false, 16, 58).setOverlay(this.tank_overlay, 0, 0);
		if (barrelRecipe.item_out != null) {
			stacks2 = barrelRecipe.item_out.getItems();
			ItemStack[] var10 = stacks2;
			var7 = stacks2.length;

			for(int var11 = 0; var11 < var7; ++var11) {
				ItemStack stack2 = var10[var11];
				stack2.setCount(barrelRecipe.out_amount);
			}

			builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 53).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(stacks2).toList());
		}

		if (barrelRecipe.fluid_out != null) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 14).addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(barrelRecipe.fluid_out.getFluid(),barrelRecipe.fluid_out.amount,barrelRecipe.fluid_out.nbt)).setFluidRenderer(Math.min(16000, barrelRecipe.fluid_out.amount * 2), false, 16, 58).setOverlay(this.tank_overlay, 0, 0);
		}

	}
//	@Override
//	public void setRecipe(IRecipeLayout iRecipeLayout, BarrelRecipe recipe, IIngredients iIngredients) {
//		iRecipeLayout.getItemStacks().init(0,true,79,16);
//		iRecipeLayout.getItemStacks().set(0,iIngredients.getInputs(VanillaTypes.ITEM).get(0));
//		iRecipeLayout.getFluidStacks().init(1,true,44,14,59-43,71-13,Math.min(16000,recipe.fluid_in.amount*2),false,tank_overlay);
//		iRecipeLayout.getFluidStacks().set(1,iIngredients.getInputs(VanillaTypes.FLUID).get(0));
//		if((recipe.flags&0b100)==0b100){
//			iRecipeLayout.getItemStacks().init(2,false,79,52);
//			iRecipeLayout.getItemStacks().set(2,iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
//		}
//		if((recipe.flags&0b1000)==0b1000){
//			iRecipeLayout.getFluidStacks().init(3,false,116,14,59-43,71-13,Math.min(16000,recipe.fluid_out.amount*2),false,tank_overlay);
//			iRecipeLayout.getFluidStacks().set(3,iIngredients.getOutputs(VanillaTypes.FLUID).get(0));
//			if (recipe.fluid_out != null) {
//				((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 14).addIngredients(ForgeTypes.FLUID_STACK, recipe.fluid_out.getAllStacks())).setFluidRenderer(Math.min(16000, recipe.fluid_out.amount * 2), false, 16, 58).setOverlay(this.tankOverlay, 0, 0);
//			}
//		}
//
//	}
	
	@Override
	public void draw(BarrelRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		overlay.draw(guiGraphics);
	}
}
