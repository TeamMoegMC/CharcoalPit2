package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModItemRegistry;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;

public class CharcoalPitRecipeCategory implements IRecipeCategory<CharcoalPitRecipeCategory.CharcoalPitRecipe> {

	public static final ResourceLocation ID=new ResourceLocation(CharcoalPit.MODID,"charcoal_pit_recipe");
	public final String loc_name;
	public final IDrawable backgroung;
	public final IDrawable icon;
	public final IDrawable overlay;
	public static final RecipeType<CharcoalPitRecipe> CHARCOAL_PIT_RECIPE_TYPE = new RecipeType<>(ID,CharcoalPitRecipe.class);
	public CharcoalPitRecipeCategory(IGuiHelper helper){
		loc_name=I18n.get("charcoal_pit.jei.charcoal_pit_recipe");
		backgroung=helper.createBlankDrawable(175,103);
		icon=helper.createDrawableItemStack(new ItemStack(Items.CHARCOAL));
		overlay= helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/charcoal_pit_recipe.png"),0,0,175,103);
	}


	@Override
	public RecipeType<CharcoalPitRecipe> getRecipeType() {
		return CHARCOAL_PIT_RECIPE_TYPE;
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
	public void setRecipe(IRecipeLayoutBuilder builder, CharcoalPitRecipe charcoalPitRecipeRecipe, IFocusGroup iFocusGroup) {
		builder.addSlot(RecipeIngredientRole.INPUT,56,17).addItemStack(charcoalPitRecipeRecipe.input);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY,56,35).addItemStack(new ItemStack(ModBlockRegistry.SandyCollector));

		if (!ForgeRegistries.FLUIDS.getValue(new ResourceLocation("immersiveengineering", "creosote")).getFluidType().isAir())
		{
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 56, 53).addItemStack(new ItemStack(ForgeRegistries.FLUIDS.
					getValue(new ResourceLocation("immersiveengineering", "creosote")).getBucket()));
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT,116,35).addItemStack(charcoalPitRecipeRecipe.output);
	}
	@Override
	public void draw(CharcoalPitRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		overlay.draw(guiGraphics);
	}
	public static class CharcoalPitRecipe{
		ItemStack input,output;
		boolean coke;
		public CharcoalPitRecipe(ItemStack in,ItemStack out){
			input=in;
			output=out;
		}
	}
}
