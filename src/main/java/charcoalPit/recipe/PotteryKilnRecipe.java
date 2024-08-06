package charcoalPit.recipe;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModItemRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class PotteryKilnRecipe implements Recipe<Container>{
	
	public static final ResourceLocation POTTERY=new ResourceLocation(CharcoalPit.MODID, "pottery");
	public static final RecipeType<PotteryKilnRecipe> POTTERY_RECIPE= new RecipeType<>() {
        public String toString() {
            return POTTERY.toString();
        }
    };
	
	public static final Serializer SERIALIZER=new Serializer();
	
	public final ResourceLocation id;
	public Ingredient input;
	public Item output;
	public float xp;
	
	public PotteryKilnRecipe(ResourceLocation id, Ingredient in, Item out, float exp) {
		this.id=id;
		input=in;
		output=out;
		xp=exp;
	}
	
	public static boolean isValidInput(ItemStack input, Level world) {
		if(input.getItem()==ModItemRegistry.ClayPot) {
			if(input.hasTag()&&input.getTag().contains("inventory")) {
				ItemStackHandler inv=new ItemStackHandler();
				inv.deserializeNBT(input.getTag().getCompound("inventory"));
				if(!OreKilnRecipe.oreKilnIsEmpty(inv)) {
					if(OreKilnRecipe.OreKilnGetOutput(input.getTag().getCompound("inventory"), world)!=null) {
						 return OreKilnRecipe.oreKilnGetFuelAvailable(inv)>=OreKilnRecipe.oreKilnGetFuelRequired(inv);
					}else {
						return false;
					}
				}else {
					if(OreKilnRecipe.oreKilnGetFuelAvailable(inv)!=0)
						return false;
				}
			}
		}
		List<PotteryKilnRecipe> recipes=world.getRecipeManager().getAllRecipesFor(POTTERY_RECIPE);
		for(PotteryKilnRecipe recipe:recipes) {
			if(recipe.input.test(input))
				return true;
		}
		return false;
	}
	
	public static PotteryKilnRecipe getResult(ItemStack input, Level world) {
		List<PotteryKilnRecipe> recipes=world.getRecipeManager().getAllRecipesFor(POTTERY_RECIPE);
		for(PotteryKilnRecipe recipe:recipes) {
			if(recipe.input.test(input))
				return recipe;
		}
		return null;
	}
	
	public static ItemStack processClayPot(ItemStack in, Level world) {
		if(in.getItem()==ModItemRegistry.ClayPot) {
			if(in.hasTag()&&in.getTag().contains("inventory")) {
				ItemStackHandler tag=new ItemStackHandler(1);
				tag.setStackInSlot(0, OreKilnRecipe.OreKilnGetOutput(in.getTag().getCompound("inventory"), world));
				ItemStack out=new ItemStack(ModItemRegistry.CrackedPot);
				out.addTagElement("inventory", tag.serializeNBT());
				ItemStackHandler inv=new ItemStackHandler();
				inv.deserializeNBT(in.getTag().getCompound("inventory"));
				out.getTag().putInt("xp", OreKilnRecipe.oreKilnGetFuelRequired(inv)/4);
				return out;
			}
			return ItemStack.EMPTY;
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean matches(Container inv, Level worldIn) {
		return input.test(inv.getItem(0));
	}
	@Override
	public ItemStack assemble(Container inv,RegistryAccess access) {
		return new ItemStack(output);
	}
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return new ItemStack(output);
	}
	@Override
	public ResourceLocation getId() {
		return id;
	}
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	@Override
	public RecipeType<?> getType() {
		return POTTERY_RECIPE;
	}
	
	public static class Serializer implements RecipeSerializer<PotteryKilnRecipe>{

		@SuppressWarnings("deprecation")
		@Override
		public PotteryKilnRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			JsonElement jsonelement = (JsonElement)(GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient"));
		    Ingredient ingredient = Ingredient.fromJson(jsonelement);
		    //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
		    if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		    ItemStack itemstack;
		    if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "result")).getDefaultInstance();
		    else {
		    String s1 = GsonHelper.getAsString(json, "result");
		    ResourceLocation resourcelocation = new ResourceLocation(s1);
		    itemstack = new ItemStack(ForgeRegistries.ITEMS.getDelegateOrThrow(resourcelocation));
		    }
		    float f = GsonHelper.getAsFloat(json, "experience", 0.0F);
			return new PotteryKilnRecipe(recipeId, ingredient, itemstack.getItem(), f);
		}

		@Override
		public PotteryKilnRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			Ingredient in=Ingredient.fromNetwork(buffer);
			Item out=buffer.readItem().getItem();
			float exp=buffer.readFloat();
			return new PotteryKilnRecipe(recipeId, in, out, exp);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, PotteryKilnRecipe recipe) {
			recipe.input.toNetwork(buffer);
			buffer.writeItem(new ItemStack(recipe.output));
			buffer.writeFloat(recipe.xp);
			
		}
		
		
		
	}
	
}
