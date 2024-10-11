package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.OreKilnRecipe;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OreKilnRecipeCategory implements IRecipeCategory<OreKilnRecipe> {
	
	public static final ResourceLocation ID=new ResourceLocation(CharcoalPit.MODID,"orekiln_recipe");
	public final String loc_name;
	public final IDrawable backgroung;
	public final IDrawable icon;
	public final IDrawable overlay;
	public static final RecipeType<OreKilnRecipe> ORE_KILN_RECIPE_TYPE = new RecipeType<>(ID,OreKilnRecipe.class);
	public OreKilnRecipeCategory(IGuiHelper helper){
		loc_name= I18n.get("charcoal_pit.jei.orekiln");
		backgroung=helper.createBlankDrawable(211,85);
		icon=helper.createDrawableItemStack(new ItemStack(ModItemRegistry.ClayPot));
		overlay=helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/ore_kiln.png"),0,0,211,85);
	}


	@Override
	public RecipeType<OreKilnRecipe> getRecipeType() {
		return ORE_KILN_RECIPE_TYPE;
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

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, OreKilnRecipe oreKilnRecipe, IFocusGroup iFocusGroup) {
		int index=0;
		for(index=0;index<oreKilnRecipe.input.length;index++){
			int pos=index;
			if(pos>4)
				pos++;
//			iRecipeLayout.getItemStacks().init(index,true,7+18*(pos%3),16+18*(pos/3));
			builder.addSlot(RecipeIngredientRole.INPUT,8+18*(pos%3),17+18*(pos/3)).addIngredients(oreKilnRecipe.input[index]);
//			iRecipeLayout.getItemStacks().set(index,iIngredients.getInputs(VanillaTypes.ITEM).get(index));
		}
		index++;
//		iRecipeLayout.getItemStacks().init(index,false,187,34);
		builder.addSlot(RecipeIngredientRole.OUTPUT,188,35).addIngredients(oreKilnRecipe.output);
//		iRecipeLayout.getItemStacks().set(index,iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
		////
//		index++;
//		ArrayList<ItemStack> logs=new ArrayList();
////		for(Item i: ItemTags.LOGS_THAT_BURN.getValues()){
////			logs.add(new ItemStack(i,3));
////		}
//		iRecipeLayout.getItemStacks().init(index,true,79,16);
//		iRecipeLayout.getItemStacks().set(index,logs);
//		index++;
//		iRecipeLayout.getItemStacks().init(index,true,79,34);
//		iRecipeLayout.getItemStacks().set(index,new ItemStack(ModItemRegistry.Straw,6));
//		index++;
//		iRecipeLayout.getItemStacks().init(index,true,79,52);
//		iRecipeLayout.getItemStacks().set(index,new ItemStack(ModItemRegistry.ClayPot));
//		index++;
//		ArrayList<ItemStack> coal=new ArrayList();
//		for(Item i: ItemTags.getAllTags().getTag(new ResourceLocation(CharcoalPit.MODID,"orekiln_fuels")).getValues()){
//			coal.add(new ItemStack(i));
//		}
//		iRecipeLayout.getItemStacks().init(index,true,25,34);
//		iRecipeLayout.getItemStacks().set(index,coal);
//		index++;
//		iRecipeLayout.getItemStacks().init(index,true,115,34);
//		iRecipeLayout.getItemStacks().init(index+1,true,151,34);
//		iRecipeLayout.getItemStacks().init(index+2,true,133,52);
//		iRecipeLayout.getItemStacks().set(index,new ItemStack(Items.DIRT));
//		iRecipeLayout.getItemStacks().set(index+1,new ItemStack(Items.DIRT));
//		iRecipeLayout.getItemStacks().set(index+2,new ItemStack(Items.DIRT));
	}

//	@Override
//	public void setIngredients(OreKilnRecipe recipe, IIngredients iIngredients) {
//		ArrayList<List<ItemStack>> inputs=new ArrayList();
//		for(Ingredient i: recipe.input){
//			inputs.add(Arrays.asList(i.getItems()));
//		}
//		iIngredients.setInputLists(VanillaTypes.ITEM,inputs);
//		iIngredients.setOutput(VanillaTypes.ITEM,new ItemStack(recipe.output.getItems()[0].getItem(), recipe.amount));
//	}
//
//	@Override
//	public void setRecipe(IRecipeLayout iRecipeLayout, OreKilnRecipe oreKilnRecipe, IIngredients iIngredients) {
//		int index=0;
//		for(index=0;index<iIngredients.getInputs(VanillaTypes.ITEM).size();index++){
//			int pos=index;
//			if(pos>4)
//				pos++;
//			iRecipeLayout.getItemStacks().init(index,true,7+18*(pos%3),16+18*(pos/3));
//			iRecipeLayout.getItemStacks().set(index,iIngredients.getInputs(VanillaTypes.ITEM).get(index));
//		}
//		index++;
//		iRecipeLayout.getItemStacks().init(index,false,187,34);
//		iRecipeLayout.getItemStacks().set(index,iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
//		index++;
//		ArrayList<ItemStack> logs=new ArrayList();
////		for(Item i: ItemTags.LOGS_THAT_BURN.getValues()){
////			logs.add(new ItemStack(i,3));
////		}
//		iRecipeLayout.getItemStacks().init(index,true,79,16);
//		iRecipeLayout.getItemStacks().set(index,logs);
//		index++;
//		iRecipeLayout.getItemStacks().init(index,true,79,34);
//		iRecipeLayout.getItemStacks().set(index,new ItemStack(ModItemRegistry.Straw,6));
//		index++;
//		iRecipeLayout.getItemStacks().init(index,true,79,52);
//		iRecipeLayout.getItemStacks().set(index,new ItemStack(ModItemRegistry.ClayPot));
//		index++;
//		ArrayList<ItemStack> coal=new ArrayList();
////		for(Item i: ItemTags.getAllTags().getTag(new ResourceLocation(CharcoalPit.MODID,"orekiln_fuels")).getValues()){
////			coal.add(new ItemStack(i));
////		}
//		iRecipeLayout.getItemStacks().init(index,true,25,34);
//		iRecipeLayout.getItemStacks().set(index,coal);
//		index++;
//		iRecipeLayout.getItemStacks().init(index,true,115,34);
//		iRecipeLayout.getItemStacks().init(index+1,true,151,34);
//		iRecipeLayout.getItemStacks().init(index+2,true,133,52);
//		iRecipeLayout.getItemStacks().set(index,new ItemStack(Items.DIRT));
//		iRecipeLayout.getItemStacks().set(index+1,new ItemStack(Items.DIRT));
//		iRecipeLayout.getItemStacks().set(index+2,new ItemStack(Items.DIRT));
//	}
	
	@Override
	public void draw(OreKilnRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		overlay.draw(guiGraphics);
	}
	
	@Override
	public List<Component> getTooltipStrings(OreKilnRecipe recipe, IRecipeSlotsView recipeSlotsView,double mouseX, double mouseY) {
		if(mouseX>=133&&mouseX<151&&mouseY>=34&&mouseY<52)
			return Arrays.asList(Component.translatable("charcoal_pit.instruction.place_kiln"));
		else
			return Collections.emptyList();
	}
}
