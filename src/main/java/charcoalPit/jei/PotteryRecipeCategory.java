package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.PotteryKilnRecipe;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PotteryRecipeCategory implements IRecipeCategory<PotteryKilnRecipe> {
	
	public static final ResourceLocation ID=new ResourceLocation(CharcoalPit.MODID,"pottery_recipe");
	public final String loc_name;
	public final IDrawable backgroung;
	public final IDrawable icon;
	public final IDrawable overlay;
	public static final RecipeType<PotteryKilnRecipe> POTTERY_KILN_RECIPE_TYPE = new RecipeType<>(ID,PotteryKilnRecipe.class);
	public PotteryRecipeCategory(IGuiHelper helper){
		loc_name= I18n.get("charcoal_pit.jei.pottery");
		backgroung=helper.createBlankDrawable(175,85);
		icon=helper.createDrawableItemStack(new ItemStack(ModItemRegistry.Straw));
		overlay=helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/pottery_kiln.png"),0,0,175,85);
	}

	@Override
	public RecipeType<PotteryKilnRecipe> getRecipeType() {
		return POTTERY_KILN_RECIPE_TYPE;
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
	public void setRecipe(IRecipeLayoutBuilder builder, PotteryKilnRecipe potteryKilnRecipe, IFocusGroup iFocusGroup) {
		ArrayList<ItemStack> logs=new ArrayList();
		for(Item i: ForgeRegistries.ITEMS.tags().getTag(ItemTags.LOGS_THAT_BURN)){
			logs.add(new ItemStack(i,3));
		}
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY,26,17).addItemStacks(logs);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY,26,35).addItemStack(new ItemStack(ModItemRegistry.Straw,6));

		builder.addSlot(RecipeIngredientRole.INPUT,26,53).addIngredients(potteryKilnRecipe.input);
		builder.addSlot(RecipeIngredientRole.OUTPUT,134,35).addItemStack(potteryKilnRecipe.output.getDefaultInstance());

		builder.addSlot(RecipeIngredientRole.RENDER_ONLY,62,35).addItemStack(Items.DIRT.getDefaultInstance());
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY,98,35).addItemStack(Items.DIRT.getDefaultInstance());
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY,80,53).addItemStack(Items.DIRT.getDefaultInstance());
	}

//	@Override
//	public void setIngredients(PotteryKilnRecipe recipe, IIngredients iIngredients) {
//		ArrayList<List<ItemStack>> list=new ArrayList<>();
//		list.add(Arrays.asList(recipe.input.getItems()));
//		iIngredients.setInputLists(VanillaTypes.ITEM,list);
//		//iIngredients.setInputs(VanillaTypes.ITEM, Arrays.asList(recipe.input.getMatchingStacks()));
//		iIngredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe.output));
//	}
//
//	@Override
//	public void setRecipe(IRecipeLayout iRecipeLayout, PotteryKilnRecipe potteryKilnRecipe, IIngredients iIngredients) {
//		iRecipeLayout.getItemStacks().init(0,true,25,52);
//		iRecipeLayout.getItemStacks().set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
//		iRecipeLayout.getItemStacks().init(1,false,133,34);
//		iRecipeLayout.getItemStacks().set(1,iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
//		iRecipeLayout.getItemStacks().init(2,true,25,34);
//		iRecipeLayout.getItemStacks().set(2, new ItemStack(ModItemRegistry.Straw,6));
//		iRecipeLayout.getItemStacks().init(3,true,25,16);
//		ArrayList<ItemStack> logs=new ArrayList();
////		for(Item i:ItemTags.LOGS_THAT_BURN.getValues()){
////			logs.add(new ItemStack(i,3));
////		}
//		iRecipeLayout.getItemStacks().set(3,logs);
//		iRecipeLayout.getItemStacks().init(4,true,61,34);
//		iRecipeLayout.getItemStacks().init(5,true,97,34);
//		iRecipeLayout.getItemStacks().init(6,true,79,52);
//		iRecipeLayout.getItemStacks().set(4,new ItemStack(Items.DIRT));
//		iRecipeLayout.getItemStacks().set(5,new ItemStack(Items.DIRT));
//		iRecipeLayout.getItemStacks().set(6,new ItemStack(Items.DIRT));
//
//	}
	
	@Override
	public void draw(PotteryKilnRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		overlay.draw(guiGraphics);
	}
	
	@Override
	public List<Component> getTooltipStrings(PotteryKilnRecipe recipe, IRecipeSlotsView recipeSlotsView,double mouseX, double mouseY) {
		if(mouseX>=79&&mouseX<97&&mouseY>=34&&mouseY<52)
			return Arrays.asList(Component.translatable("charcoal_pit.instruction.place_kiln"));
		else
			return Collections.emptyList();
	}
	
	/*@Override
	public boolean handleClick(PotteryKilnRecipe recipe, double mouseX, double mouseY, int mouseButton) {
		return false;
	}
	
	@Override
	public boolean isHandled(PotteryKilnRecipe recipe) {
		return false;
	}*/
}
