package charcoalPit.recipe;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import charcoalPit.CharcoalPit;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class OreKilnRecipe implements Recipe<Container>{
	
//	public static final ResourceLocation OREKILN=new ResourceLocation(CharcoalPit.MODID, "orekiln");
	public static RecipeType<OreKilnRecipe> ORE_KILN_RECIPE =RecipeType.simple(new ResourceLocation(CharcoalPit.MODID, "orekiln"));
	
	public static final Serializer SERIALIZER=new Serializer();

	public final ResourceLocation id;
	public Ingredient[] input;
	public ItemStack output;
//	public int amount;
	
	public OreKilnRecipe(ResourceLocation id, ItemStack output, Ingredient... input) {
		this.id=id;
		this.output=output;
//		this.amount=amount;
		this.input=input;
	}
	
	//dynamic///////////
	public boolean isInputEqual(ItemStack in, int slot) {
		if(in.isEmpty())
			return false;
		if(slot>=input.length)
			return false;
		return input[slot].test(in);
	}
	
	public boolean isInputEqual(ItemStack in) {
		if(in.isEmpty())
			return false;
		for(int i=0;i<input.length;i++)
			if(input[i].test(in))
				return true;
		return false;
	}
	
	//static////////////
	public static boolean isValidInput(ItemStack stack, Level world) {
		List<OreKilnRecipe> recipes=world.getRecipeManager().getAllRecipesFor(ORE_KILN_RECIPE);
		for(OreKilnRecipe recipe:recipes)
			if(recipe.isInputEqual(stack)&& !recipe.output.is(Items.BARRIER))
				return true;
		return false;
	}
	
	public static int oreKilnGetFuelRequired(IItemHandler inventory) {
		int f=0;
		for(int i=1;i<inventory.getSlots();i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				f++;
			}
		}
		return f*4;
	}
	
	public static int oreKilnGetFuelAvailable(IItemHandler inventory) {
		if (inventory.getStackInSlot(0)
				.is(ItemTags.create((new ResourceLocation("forge:coal_coke"))))) {
			return inventory.getStackInSlot(0).getCount() * 16;
		}
		return inventory.getStackInSlot(0).getCount() * 8;
	}
	
	public static boolean oreKilnIsEmpty(IItemHandler inventory) {
		for(int i=1;i<inventory.getSlots();i++)
			if(!inventory.getStackInSlot(i).isEmpty())
				return false;
		return true;
	}
	
	public static int oreKilnGetOreAmount(IItemHandler inventory) {
		int a=0;
		for(int i=1;i<inventory.getSlots();i++) {
			if(!inventory.getStackInSlot(i).isEmpty())
				a+=inventory.getStackInSlot(i).getCount();
		}
		return a;
	}
	
	public static ItemStack OreKilnGetOutput(CompoundTag nbt, Level world) {
		List<OreKilnRecipe> recipes=world.getRecipeManager().getAllRecipesFor(ORE_KILN_RECIPE);
		ItemStackHandler kiln=new ItemStackHandler();
		for(OreKilnRecipe recipe:recipes) {
			if(recipe.output.isEmpty())
				continue;
			kiln.deserializeNBT(nbt);
			int r=0;
			while(!oreKilnIsEmpty(kiln)) {
				boolean b=false;
				for(int i=0;i<recipe.input.length;i++) {
					boolean e=false;
					for(int j=1;j<kiln.getSlots();j++) {
						if(recipe.isInputEqual(kiln.getStackInSlot(j), i)) {
							e=true;
							kiln.extractItem(j, 1, false);
							break;
						}
					}
					if(!e) {
						b=true;
						break;
					}
				}
				if(b) {
					r=0;
					break;
				}else {
					r++;
				}
			}
			if(r>0&&oreKilnIsEmpty(kiln)) {
				ItemStack out = recipe.output.copy();
				out.setCount(r*out.getCount());
				return out;
			}
		}
		return ItemStack.EMPTY;
	}
	
	////////////////////////////////////////////////////////
	//junk
	@Override
	public boolean matches(Container inv, Level worldIn) {
		return false;
	}

	@Override
	public ItemStack assemble(Container inv,RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
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
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return ORE_KILN_RECIPE;
	}
	//////////////////////////////////////////////
	
	public static class Serializer implements RecipeSerializer<OreKilnRecipe>{

		@Override
		public OreKilnRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
	         NonNullList<Ingredient> nonnulllist = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
	         if (nonnulllist.isEmpty()) {
	            throw new JsonParseException("No ingredients for shapeless recipe");
	         } else if (nonnulllist.size() > 8) {
	            throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 8);
	         } else {
	        	ItemStack output= CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
//	        	int amount=GsonHelper.getAsInt(json, "amount");
	            return new OreKilnRecipe(recipeId, output, nonnulllist.toArray(new Ingredient[0]));
	         }
		}
		
		private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
	         NonNullList<Ingredient> nonnulllist = NonNullList.create();

	         for(int i = 0; i < ingredientArray.size(); ++i) {
	            Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
	            if (!ingredient.isEmpty()) {
	               nonnulllist.add(ingredient);
	            }
	         }

	         return nonnulllist;
	      }

		@Override
		public OreKilnRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int l=buffer.readInt();
			Ingredient[] in=new Ingredient[l];
			for(int i=0;i<l;i++) {
				in[i]=Ingredient.fromNetwork(buffer);
			}
//			int a=buffer.readInt();
			ItemStack o= buffer.readItem();
			return new OreKilnRecipe(recipeId, o, in);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, OreKilnRecipe recipe) {
			buffer.writeInt(recipe.input.length);
			for(int i=0;i<recipe.input.length;i++) {
				recipe.input[i].toNetwork(buffer);
			}
//			buffer.writeInt(recipe.amount);
			buffer.writeItem(recipe.output);
//			recipe.output.toNetwork(buffer);
		}
		
	}
	@Override
	public boolean isSpecial()
	{
		return true;
	}

}
