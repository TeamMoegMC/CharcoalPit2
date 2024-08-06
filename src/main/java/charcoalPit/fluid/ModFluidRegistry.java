package charcoalPit.fluid;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ModFluidRegistry {
	
	public static final ResourceLocation alcohol_still=new ResourceLocation("minecraft:block/water_still"),
			alcohol_flowing=new ResourceLocation("minecraft:block/water_flow");
//	public static ForgeFlowingFluid AlcoholStill, AlcoholFlowing;

	public static final ResourceLocation vinegar_still=new ResourceLocation("minecraft:block/water_still"),
			vinegar_flowing=new ResourceLocation("minecraft:block/water_flow");
//	public static ForgeFlowingFluid VinegarStill, VinegarFlowing;
//	public static ForgeFlowingFluid.Properties Vinegar;

//	public static FluidType AlcoholType;
//	public static FluidType VinegarType;

//	public static final RegistryObject<FluidType> MILK_TYPE = RegistryObject.createOptional(new ResourceLocation("milk"), ForgeRegistries.Keys.FLUID_TYPES.location(), "minecraft");

	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, CharcoalPit.MODID);
	public static final RegistryObject<FluidType> AlcoholType = FLUID_TYPES.register("alcohol",
			() -> new ModFluidTypes(FluidType.Properties.create(),alcohol_still, alcohol_flowing,0xFF000000));
	public static final RegistryObject<FluidType> VinegarType = FLUID_TYPES.register("vinegar",
			() -> new ModFluidTypes(FluidType.Properties.create(),vinegar_still, vinegar_flowing,0xFFCEB301));


	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, CharcoalPit.MODID);
	public static final RegistryObject<FlowingFluid> AlcoholStill = FLUIDS.register("alcohol_still",
			() -> new ForgeFlowingFluid.Source(ModFluidRegistry.Alcohol));
	public static final RegistryObject<FlowingFluid> AlcoholFlowing = FLUIDS.register("alcohol_flowing",
			() -> new ForgeFlowingFluid.Flowing(ModFluidRegistry.Alcohol));
	public static final RegistryObject<FlowingFluid> VinegarStill = FLUIDS.register("vinegar_still",
			() -> new ForgeFlowingFluid.Source(ModFluidRegistry.Vinegar));
	public static final RegistryObject<FlowingFluid> VinegarFlowing = FLUIDS.register("vinegar_flowing",
			() -> new ForgeFlowingFluid.Flowing(ModFluidRegistry.Vinegar));

	public static final ForgeFlowingFluid.Properties Alcohol = new ForgeFlowingFluid.Properties( AlcoholType, AlcoholStill, AlcoholFlowing);
	public static final ForgeFlowingFluid.Properties Vinegar = new ForgeFlowingFluid.Properties( VinegarType, VinegarStill, VinegarFlowing);



	public static void registerFluids(RegisterEvent event) {

//		AlcoholType = new ModFluidTypes(FluidType.Properties.create(),alcohol_still, alcohol_flowing,0xFF000000);
//		VinegarType = new ModFluidTypes(FluidType.Properties.create(),vinegar_still, vinegar_flowing,0xFFCEB301);
//
//		Alcohol=new ForgeFlowingFluid.Properties(()-> AlcoholType, ()->AlcoholStill, ()->AlcoholFlowing);
//
//		AlcoholStill=new ForgeFlowingFluid.Source(Alcohol);
//		AlcoholFlowing = new ForgeFlowingFluid.Flowing(Alcohol);
////		AlcoholStill.setRegistryName("alcohol_still");
////		AlcoholFlowing.setRegistryName("alcohol_flowing");
//
//		Vinegar = new Properties(()->VinegarType, () -> VinegarStill,()->VinegarFlowing).bucket(() -> ModItemRegistry.VinegarBucket);
//		VinegarStill = new ForgeFlowingFluid.Source(Vinegar);
//		VinegarFlowing = new ForgeFlowingFluid.Flowing(Vinegar);
//		VinegarStill.setRegistryName("vinegar_still");
//		VinegarFlowing.setRegistryName("vinegar_flowing");

//		event.getRegistry().registerAll(AlcoholStill, AlcoholFlowing, VinegarStill, VinegarFlowing);

//		event.register(ForgeRegistries.Keys.FLUID_TYPES,
//				helper -> {
//					helper.register(new ResourceLocation(CharcoalPit.MODID, "alcohol"), AlcoholType);
//					helper.register(new ResourceLocation(CharcoalPit.MODID,"vinegar"), VinegarType);
//				}
//		);
//
//		event.register(ForgeRegistries.Keys.FLUIDS,
//				helper -> {
//					helper.register(new ResourceLocation(CharcoalPit.MODID, "alcohol_still"), AlcoholStill);
//					helper.register(new ResourceLocation(CharcoalPit.MODID,"alcohol_flowing"), AlcoholFlowing);
//					helper.register(new ResourceLocation(CharcoalPit.MODID, "vinegar_still"), VinegarStill);
//					helper.register(new ResourceLocation(CharcoalPit.MODID,"vinegar_flowing"), VinegarFlowing);
//				}
//		);
	}

	public static class ModFluidTypes extends FluidType{
		public ResourceLocation stillTexture;
		public ResourceLocation flowingTexture;
		public int tint;
		ModFluidTypes(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, int tint)
		{
			super(properties);
			this.stillTexture = stillTexture;
			this.flowingTexture = flowingTexture;
			this.tint = tint;
		}


		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
			consumer.accept(new IClientFluidTypeExtensions() {
				@Override
				public int getTintColor() {
					return tint;
				}

				@Override
				public ResourceLocation getStillTexture() {
					return stillTexture;
				}

				@Override
				public ResourceLocation getFlowingTexture() {
					return flowingTexture;
				}
			});


		}
//
//		@Override
//		public int getColor(FluidStack stack) {
//			if(stack.hasTag()&&stack.getTag().contains("CustomPotionColor")){
//				return stack.getTag().getInt("CustomPotionColor")+0xFF000000;
//			}
//			return 0xFFFFFFFF;
//		}
//		public static Builder builder(ResourceLocation stillTexture, ResourceLocation flowingTexture) {
//			return new Builder2(stillTexture, flowingTexture, AlcoholProperties::new);
//		}
//		public  static class Builder2 extends Builder{
//			public Builder2(ResourceLocation stillTexture, ResourceLocation flowingTexture, BiFunction<Builder,Fluid,FluidAttributes> factory) {
//				super(stillTexture,flowingTexture,factory);
//			}
//		}
	}

}
