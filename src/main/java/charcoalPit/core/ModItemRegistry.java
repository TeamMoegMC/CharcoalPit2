package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;


public class ModItemRegistry {

/*	public static CreativeModeTab CHARCOAL_PIT=new CreativeModeTab("charcoal_pit") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.CHARCOAL);
		}
	};
	public static CreativeModeTab CHARCOAL_PIT_FOODS = new CreativeModeTab("charcoal_pit_foods") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItemRegistry.Cheese);
		}
	};*/

	public static BlockItemFuel Thatch, LogPile, CoalPile, CokeBlock;
	public static BlockItem WoodAsh, CoalAsh, AshBlock,
			SandyBrick, SandySlab, SandyStair, SandyWall, MainBloomery;

	public static ItemFuel Straw, Coke;
	public static Item Ash;
	public static BoneMealItem Fertilizer;
	public static ItemFireStarter FireStarter;
	public static Item BloomCool, BloomFail, BloomNiCool, BloomNiFail;
	public static Item SandyBrickItem, UnfireSandyBrick, UnfiredBrick;

	public static BlockItem SandyCollector;
	public static BlockItem CeramicPot, WhitePot,
			OrangePot, MagentaPot,
			LightBluePot, YellowPot,
			LimePot, PinkPot,
			GrayPot, LightGrayPot,
			CyanPot, PurplePot,
			BluePot, BrownPot,
			GreenPot, RedPot,
			BlackPot;
	public static BlockItem Bellows, TuyereSandy;
	public static ItemClayPot ClayPot;
	public static ItemCrackedPot CrackedPot;

	public static ItemBarrel Barrel;

	//public static BucketItem AlcoholBucket=new BucketItem(()->ModFluidRegistry.AlcoholStill, new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).containerItem(Items.BUCKET));
	public static ItemAlcoholBottle AlcoholBottle;
	public static BucketItem VinegarBucket;
	public static Item VinegarBottle;
	public static Item Cheese;
	public static Item TinyCoke;
	public static BlockItem MechanicalBeellows;
	static {
		Thatch = buildBlockItem(ModBlockRegistry.Thatch, 200);
		LogPile = buildBlockItem(ModBlockRegistry.LogPile, 3000);
		CoalPile = buildBlockItem(ModBlockRegistry.CoalPile, 12000);
		CokeBlock = buildBlockItem(ModBlockRegistry.CokeBlock, 32000);
		WoodAsh = buildBlockItem(ModBlockRegistry.WoodAsh);
		CoalAsh = buildBlockItem(ModBlockRegistry.CoalAsh);
		AshBlock = buildBlockItem(ModBlockRegistry.Ash);
		SandyBrick = buildBlockItem(ModBlockRegistry.SandyBrick);
		SandySlab = buildBlockItem(ModBlockRegistry.SandySlab);
		SandyStair = buildBlockItem(ModBlockRegistry.SandyStair);
		SandyWall = buildBlockItem(ModBlockRegistry.SandyWall);
		MainBloomery = buildBlockItem(ModBlockRegistry.MainBloomery);

		Straw = buildItem(50);
		Coke = buildItem(3200);
		Ash = buildItem();
		Fertilizer = new BoneMealItem(new Item.Properties());
		FireStarter = new ItemFireStarter();
		BloomCool = buildItem();
		BloomFail = buildItem();
		BloomNiCool = buildItem();
		BloomNiFail = buildItem();
		SandyBrickItem = buildItem();
		UnfireSandyBrick = buildItem();
		UnfiredBrick = buildItem();

		SandyCollector = buildBlockItem(ModBlockRegistry.SandyCollector);
		CeramicPot = buildBlockItemP(ModBlockRegistry.CeramicPot); WhitePot = buildBlockItemP(ModBlockRegistry.WhitePot);
				OrangePot = buildBlockItemP(ModBlockRegistry.OrangePot); MagentaPot = buildBlockItemP(ModBlockRegistry.MagentaPot);
				LightBluePot = buildBlockItemP(ModBlockRegistry.LightBluePot); YellowPot = buildBlockItemP(ModBlockRegistry.YellowPot);
				LimePot = buildBlockItemP(ModBlockRegistry.LimePot); PinkPot = buildBlockItemP(ModBlockRegistry.PinkPot);
				GrayPot = buildBlockItemP(ModBlockRegistry.GrayPot); LightGrayPot = buildBlockItemP(ModBlockRegistry.LightGrayPot);
				CyanPot = buildBlockItemP(ModBlockRegistry.CyanPot); PurplePot = buildBlockItemP(ModBlockRegistry.PurplePot);
				BluePot = buildBlockItemP(ModBlockRegistry.BluePot); BrownPot = buildBlockItemP(ModBlockRegistry.BrownPot);
				GreenPot = buildBlockItemP(ModBlockRegistry.GreenPot); RedPot = buildBlockItemP(ModBlockRegistry.RedPot);
				BlackPot = buildBlockItemP(ModBlockRegistry.BlackPot);
		Bellows = buildBlockItem(ModBlockRegistry.Bellows);
		TuyereSandy = buildBlockItem(ModBlockRegistry.TuyereSandy);
		ClayPot = new ItemClayPot();
		CrackedPot = new ItemCrackedPot();

		Barrel = new ItemBarrel(ModBlockRegistry.Barrel, new Item.Properties());

		//public static BucketItem AlcoholBucket=new BucketItem(()->ModFluidRegistry.AlcoholStill, new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).containerItem(Items.BUCKET));
		AlcoholBottle = new ItemAlcoholBottle();
		VinegarBucket = new BucketItem(() -> ModFluidRegistry.VinegarStill, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));
		VinegarBottle = new Item(new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
		Cheese = new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(1.2F).build()));
		TinyCoke = buildItem(1600);
		MechanicalBeellows = buildBlockItem(ModBlockRegistry.MechanicalBellows);
	}
	/*public static TallBlockItem BrickDoor=new TallBlockItem(ModBlockRegistry.BrickDoor,new Item.Properties().group(CHARCOAL_PIT)),
			SandyDoor=new TallBlockItem(ModBlockRegistry.SandyDoor,new Item.Properties().group(CHARCOAL_PIT)),
			NetherDoor=new TallBlockItem(ModBlockRegistry.NetherDoor,new Item.Properties().group(CHARCOAL_PIT)),
			EndDoor=new TallBlockItem(ModBlockRegistry.EndDoor,new Item.Properties().group(CHARCOAL_PIT));*/


//	@SubscribeEvent
	public static void registerItems(RegisterEvent event) {
		event.register(ForgeRegistries.Keys.ITEMS,
				helper -> {
					helper.register(new ResourceLocation(CharcoalPit.MODID, "thatch"), Thatch);
					helper.register(new ResourceLocation( "log_pile"), LogPile);

				}
		);
//		event.getRegistry().registerAll(Thatch.setRegistryName("thatch"), LogPile.setRegistryName("log_pile"), CoalPile.setRegistryName("coal_pile"), WoodAsh.setRegistryName("wood_ash"),
//				CoalAsh.setRegistryName("coal_ash"), CokeBlock.setRegistryName("coke_block"), AshBlock.setRegistryName("ash_block"),
//				SandyBrick.setRegistryName("sandy_brick"), SandySlab.setRegistryName("sandy_slab"), SandyStair.setRegistryName("sandy_stair"), SandyWall.setRegistryName("sandy_wall"), MainBloomery.setRegistryName("main_bloomery"),
//				SandyCollector.setRegistryName("sandy_collector"),
//				Bellows.setRegistryName("bellows"), TuyereSandy.setRegistryName("sandy_tuyere"),
//				Barrel.setRegistryName("barrel")/*,BrickDoor.setRegistryName("brick_door"),SandyDoor.setRegistryNNetherame("sandy_door"),NetherDoor.setRegistryName("nether_door"),
//				EndDoor.setRegistryName("end_door")*/, MechanicalBeellows.setRegistryName("mechanical_bellows"));
//		event.getRegistry().registerAll(Straw.setRegistryName("straw"), Ash.setRegistryName("ash"), Coke.setRegistryName("coke"),
//				Fertilizer.setRegistryName("fertilizer"), FireStarter.setRegistryName("fire_starter"),
//				ClayPot.setRegistryName("clay_pot"), BloomCool.setRegistryName("bloom_cool"), BloomFail.setRegistryName("bloom_fail"),
//				CrackedPot.setRegistryName("cracked_pot"), BloomNiCool.setRegistryName("bloom_nickel_cool"), BloomNiFail.setRegistryName("bloom_nickel_fail"),
//				SandyBrickItem.setRegistryName("sandy_brick_item"), UnfireSandyBrick.setRegistryName("unfired_sandy_brick"),
//				UnfiredBrick.setRegistryName("unfired_brick"), AlcoholBottle.setRegistryName("alcohol_bottle"), VinegarBucket.setRegistryName("vinegar_bucket"),
//				VinegarBottle.setRegistryName("vinegar_bottle"), Cheese.setRegistryName("cheese"), TinyCoke.setRegistryName("tiny_coke"));
//		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"), YellowPot.setRegistryName("yellow_pot"), WhitePot.setRegistryName("white_pot"),
//				RedPot.setRegistryName("red_pot"), PurplePot.setRegistryName("purple_pot"), PinkPot.setRegistryName("pink_pot"), OrangePot.setRegistryName("orange_pot"),
//				MagentaPot.setRegistryName("magenta_pot"), LimePot.setRegistryName("lime_pot"), LightGrayPot.setRegistryName("light_gray_pot"),
//				LightBluePot.setRegistryName("light_blue_pot"), GreenPot.setRegistryName("green_pot"), GrayPot.setRegistryName("gray_pot"), CyanPot.setRegistryName("cyan_pot"),
//				BrownPot.setRegistryName("brown_pot"), BluePot.setRegistryName("blue_pot"), BlackPot.setRegistryName("black_pot"));


		DispenserBlock.registerBehavior(ModItemRegistry.CeramicPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.BlackPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.BluePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.BrownPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.CyanPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.GrayPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.GreenPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.LightBluePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.LightGrayPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.LimePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.MagentaPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.OrangePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.PinkPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.PurplePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.RedPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.WhitePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.YellowPot, new DispenserPlacePot());

	}


	public static BlockItemFuel buildBlockItem(Block block, int time) {
		return new BlockItemFuel(block, new Item.Properties()).setBurnTime(time);
	}

	public static BlockItem buildBlockItem(Block block) {
		return buildBlockItem(block);
	}
	
	public static BlockItem buildBlockItemP(Block block) {
		return new BlockItem(block, new Item.Properties().stacksTo(1));
	}

	public static BlockItem buildBlockItem(Block block, CreativeModeTab group) {
		return new BlockItem(block, new Item.Properties());
	}
	
	public static ItemFuel buildItem(int time) {
		return new ItemFuel(new Item.Properties()).setBurnTime(time);
	}
	
	public static Item buildItem() {
		return new Item(new Item.Properties());
	}
	
}
