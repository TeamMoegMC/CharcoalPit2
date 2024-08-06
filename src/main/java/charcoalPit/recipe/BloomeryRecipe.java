package charcoalPit.recipe;

import java.util.List;

import com.google.gson.JsonObject;

import charcoalPit.CharcoalPit;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class BloomeryRecipe implements Recipe<Container>{
	
	public static final ResourceLocation BLOOMERY=new ResourceLocation(CharcoalPit.MODID, "bloomery");
	public static final RecipeType<BloomeryRecipe> BLOOMERY_RECIPE=RecipeType.register(BLOOMERY.toString());
	
	public final ResourceLocation id;
	public Ingredient input,output,fail,cool;
	
	public BloomeryRecipe(ResourceLocation id,Ingredient input,Ingredient output,Ingredient fail,Ingredient cool) {
		this.id=id;
		this.input=input;
		this.output=output;
		this.fail=fail;
		this.cool=cool;
	}
	
	public static final Serializer SERIALIZER=new Serializer();
	
	public static BloomeryRecipe getRecipe(ItemStack stack, Level world) {
		if(stack.isEmpty())
			return null;
		List<BloomeryRecipe> recipes=world.getRecipeManager().getAllRecipesFor(BLOOMERY_RECIPE);
		for(BloomeryRecipe recipe:recipes) {
			if(recipe.input.test(stack)&&!recipe.output.isEmpty())
				return recipe;
		}
		return null;
	}
	
	//junk
	@Override
	public boolean matches(Container inv, Level worldIn) {
		return false;
	}

	@Override
	public ItemStack assemble(Container inv, RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		// TODO Auto-generated method stub
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BLOOMERY_RECIPE;
	}
	
	public static class Serializer implements RecipeSerializer<BloomeryRecipe>{

		@Override
		public BloomeryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			Ingredient input=Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
			Ingredient output=Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "output"));
			Ingredient fail=Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "fail"));
			Ingredient cool=Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "cool"));
			return new BloomeryRecipe(recipeId, input, output, fail, cool);
		}

		@Override
		public BloomeryRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			Ingredient input=Ingredient.fromNetwork(buffer);
			Ingredient output=Ingredient.fromNetwork(buffer);
			Ingredient fail=Ingredient.fromNetwork(buffer);
			Ingredient cool=Ingredient.fromNetwork(buffer);
			return new BloomeryRecipe(recipeId, input, output, fail, cool);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, BloomeryRecipe recipe) {
			recipe.input.toNetwork(buffer);
			recipe.output.toNetwork(buffer);
			recipe.fail.toNetwork(buffer);
			recipe.cool.toNetwork(buffer);
			
		}
		
	}
	
}
