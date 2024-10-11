package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.recipe.BarrelRecipe;
import charcoalPit.recipe.BloomeryRecipe;
import charcoalPit.recipe.OreKilnRecipe;
import charcoalPit.recipe.PotteryKilnRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;


public class ModRecipeRegistry {

	public static void registerRecipeType(RegisterEvent event) {
		event.register(ForgeRegistries.Keys.RECIPE_TYPES,
				helper -> {
					helper.register(new ResourceLocation(CharcoalPit.MODID, "pottery"), PotteryKilnRecipe.POTTERY_RECIPE);
					helper.register(new ResourceLocation(CharcoalPit.MODID, "orekiln"), OreKilnRecipe.ORE_KILN_RECIPE);
					helper.register(new ResourceLocation(CharcoalPit.MODID, "bloomery"), BloomeryRecipe.BLOOMERY_RECIPE);
					helper.register(new ResourceLocation(CharcoalPit.MODID, "barrel"), BarrelRecipe.BARREL_RECIPE);
				}
		);
		event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS,
				helper -> {
					helper.register(new ResourceLocation(CharcoalPit.MODID, "pottery"), PotteryKilnRecipe.SERIALIZER);
					helper.register(new ResourceLocation(CharcoalPit.MODID, "orekiln"), OreKilnRecipe.SERIALIZER);
					helper.register(new ResourceLocation(CharcoalPit.MODID, "bloomery"), BloomeryRecipe.SERIALIZER);
					helper.register(new ResourceLocation(CharcoalPit.MODID, "barrel"), BarrelRecipe.SERIALIZER);
				}
		);
	}
	
}
