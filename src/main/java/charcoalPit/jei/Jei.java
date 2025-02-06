package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.gui.BarrelScreen;
import charcoalPit.recipe.BarrelRecipe;
import charcoalPit.recipe.BloomeryRecipe;
import charcoalPit.recipe.OreKilnRecipe;
import charcoalPit.recipe.PotteryKilnRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Objects;
import java.util.stream.Stream;

@JeiPlugin
public class Jei implements IModPlugin {
	
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(CharcoalPit.MODID,"charcoal_pit");
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.useNbtForSubtypes(ModItemRegistry.AlcoholBottle);
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
	
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new PotteryRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new OreKilnRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new BloomeryRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new BarrelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CharcoalPitRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
	
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

//		registration.addRecipes(new RecipeType<>(PotteryRecipeCategory.ID,PotteryKilnRecipe.class),recipeManager.getAllRecipesFor(PotteryKilnRecipe.POTTERY_RECIPE));
		registration.addRecipes(PotteryRecipeCategory.POTTERY_KILN_RECIPE_TYPE,recipeManager.getAllRecipesFor(PotteryKilnRecipe.POTTERY_RECIPE));

//		registration.addRecipes(new RecipeType<>(OreKilnRecipeCategory.ID,OreKilnRecipe.class),recipeManager.getAllRecipesFor(OreKilnRecipe.ORE_KILN_RECIPE).stream().filter((r) -> !r.output.test(new ItemStack(Items.BARRIER))).collect(Collectors.toList()));
		registration.addRecipes(OreKilnRecipeCategory.ORE_KILN_RECIPE_TYPE,recipeManager.getAllRecipesFor(OreKilnRecipe.ORE_KILN_RECIPE));

//		registration.addRecipes(new RecipeType<>(BloomeryRecipeCategory.ID,BloomeryRecipe.class),recipeManager.getAllRecipesFor(BloomeryRecipe.BLOOMERY_RECIPE));
		registration.addRecipes(BloomeryRecipeCategory.BLOOMERY_RECIPE_TYPE,recipeManager.getAllRecipesFor(BloomeryRecipe.BLOOMERY_RECIPE));


//		registration.addRecipes(new RecipeType<>(BarrelRecipeCategory.ID,BarrelRecipe.class),recipeManager.getAllRecipesFor(BarrelRecipe.BARREL_RECIPE));
		registration.addRecipes(BarrelRecipeCategory.BARREL_RECIPE_TYPE,recipeManager.getAllRecipesFor(BarrelRecipe.BARREL_RECIPE));

		registration.addRecipes(CharcoalPitRecipeCategory.CHARCOAL_PIT_RECIPE_TYPE, Stream.of(new CharcoalPitRecipeCategory.CharcoalPitRecipe(new ItemStack(ModBlockRegistry.LogPile),new ItemStack(Items.CHARCOAL))).toList());

//		registration.addIngredientInfo(Arrays.asList(new ItemStack(Items.CHARCOAL), new ItemStack(ModItemRegistry.Coke), new ItemStack(ModBlockRegistry.LogPile), new ItemStack(ModBlockRegistry.CoalPile)),
//				VanillaTypes.ITEM, I18n.get("charcoal_pit.instruction.build_pit"));

	}
	
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {

	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.Straw), PotteryRecipeCategory.POTTERY_KILN_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.ClayPot), OreKilnRecipeCategory.ORE_KILN_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlockRegistry.Bellows), BloomeryRecipeCategory.BLOOMERY_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlockRegistry.Barrel), BarrelRecipeCategory.BARREL_RECIPE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.CHARCOAL), CharcoalPitRecipeCategory.CHARCOAL_PIT_RECIPE_TYPE);
    }
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(BarrelScreen.class,98,35,16,16,BarrelRecipeCategory.BARREL_RECIPE_TYPE);
	}
	
	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {
	
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	
	}
}
