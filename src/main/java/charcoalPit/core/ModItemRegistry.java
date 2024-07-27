package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModItemRegistry {
	
	public static CreativeModeTab CHARCOAL_PIT=new CreativeModeTab("charcoal_pit") {
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
	};

	public static BlockItemFuel Thatch = buildBlockItem(ModBlockRegistry.Thatch, 200), LogPile = buildBlockItem(ModBlockRegistry.LogPile, 3000), CoalPile = buildBlockItem(ModBlockRegistry.CoalPile, 12000), CokeBlock = buildBlockItem(ModBlockRegistry.CokeBlock, 32000);
	public static BlockItem WoodAsh = buildBlockItem(ModBlockRegistry.WoodAsh), CoalAsh = buildBlockItem(ModBlockRegistry.CoalAsh), AshBlock = buildBlockItem(ModBlockRegistry.Ash),
			SandyBrick = buildBlockItem(ModBlockRegistry.SandyBrick), SandySlab = buildBlockItem(ModBlockRegistry.SandySlab), SandyStair = buildBlockItem(ModBlockRegistry.SandyStair), SandyWall = buildBlockItem(ModBlockRegistry.SandyWall), MainBloomery = buildBlockItem(ModBlockRegistry.MainBloomery);

	public static ItemFuel Straw = buildItem(CHARCOAL_PIT, 50), Coke = buildItem(CHARCOAL_PIT, 3200);
	public static Item Ash = buildItem(CHARCOAL_PIT);
	public static BoneMealItem Fertilizer = new BoneMealItem(new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemFireStarter FireStarter = new ItemFireStarter();
	public static Item BloomCool = buildItem(CHARCOAL_PIT), BloomFail = buildItem(CHARCOAL_PIT), BloomNiCool = buildItem(CHARCOAL_PIT), BloomNiFail = buildItem(CHARCOAL_PIT);
	public static Item SandyBrickItem = buildItem(CHARCOAL_PIT), UnfireSandyBrick = buildItem(CHARCOAL_PIT), UnfiredBrick = buildItem(CHARCOAL_PIT);

	public static BlockItem SandyCollector = buildBlockItem(ModBlockRegistry.SandyCollector, CHARCOAL_PIT);
	public static BlockItem CeramicPot = buildBlockItemP(ModBlockRegistry.CeramicPot), WhitePot = buildBlockItemP(ModBlockRegistry.WhitePot),
			OrangePot = buildBlockItemP(ModBlockRegistry.OrangePot), MagentaPot = buildBlockItemP(ModBlockRegistry.MagentaPot),
			LightBluePot = buildBlockItemP(ModBlockRegistry.LightBluePot), YellowPot = buildBlockItemP(ModBlockRegistry.YellowPot),
			LimePot = buildBlockItemP(ModBlockRegistry.LimePot), PinkPot = buildBlockItemP(ModBlockRegistry.PinkPot),
			GrayPot = buildBlockItemP(ModBlockRegistry.GrayPot), LightGrayPot = buildBlockItemP(ModBlockRegistry.LightGrayPot),
			CyanPot = buildBlockItemP(ModBlockRegistry.CyanPot), PurplePot = buildBlockItemP(ModBlockRegistry.PurplePot),
			BluePot = buildBlockItemP(ModBlockRegistry.BluePot), BrownPot = buildBlockItemP(ModBlockRegistry.BrownPot),
			GreenPot = buildBlockItemP(ModBlockRegistry.GreenPot), RedPot = buildBlockItemP(ModBlockRegistry.RedPot),
			BlackPot = buildBlockItemP(ModBlockRegistry.BlackPot);
	public static BlockItem Bellows = buildBlockItem(ModBlockRegistry.Bellows), TuyereSandy = buildBlockItem(ModBlockRegistry.TuyereSandy);
	public static ItemClayPot ClayPot = new ItemClayPot();
	public static ItemCrackedPot CrackedPot = new ItemCrackedPot();

	public static ItemBarrel Barrel = new ItemBarrel(ModBlockRegistry.Barrel, new Item.Properties().tab(CHARCOAL_PIT));

	//public static BucketItem AlcoholBucket=new BucketItem(()->ModFluidRegistry.AlcoholStill, new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).containerItem(Items.BUCKET));
	public static ItemAlcoholBottle AlcoholBottle = new ItemAlcoholBottle();
	public static BucketItem VinegarBucket = new BucketItem(() -> ModFluidRegistry.VinegarStill, new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	public static Item VinegarBottle = new Item(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
	public static Item Cheese = new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(5).saturationMod(1.2F).build()));
	public static Item TinyCoke = buildItem(CHARCOAL_PIT, 1600);
	public static BlockItem MechanicalBeellows = buildBlockItem(ModBlockRegistry.MechanicalBellows);

	/*public static TallBlockItem BrickDoor=new TallBlockItem(ModBlockRegistry.BrickDoor,new Item.Properties().group(CHARCOAL_PIT)),
			SandyDoor=new TallBlockItem(ModBlockRegistry.SandyDoor,new Item.Properties().group(CHARCOAL_PIT)),
			NetherDoor=new TallBlockItem(ModBlockRegistry.NetherDoor,new Item.Properties().group(CHARCOAL_PIT)),
			EndDoor=new TallBlockItem(ModBlockRegistry.EndDoor,new Item.Properties().group(CHARCOAL_PIT));*/


	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(Thatch.setRegistryName("thatch"), LogPile.setRegistryName("log_pile"), CoalPile.setRegistryName("coal_pile"), WoodAsh.setRegistryName("wood_ash"),
				CoalAsh.setRegistryName("coal_ash"), CokeBlock.setRegistryName("coke_block"), AshBlock.setRegistryName("ash_block"),
				SandyBrick.setRegistryName("sandy_brick"), SandySlab.setRegistryName("sandy_slab"), SandyStair.setRegistryName("sandy_stair"), SandyWall.setRegistryName("sandy_wall"), MainBloomery.setRegistryName("main_bloomery"),
				SandyCollector.setRegistryName("sandy_collector"),
				Bellows.setRegistryName("bellows"), TuyereSandy.setRegistryName("sandy_tuyere"),
				Barrel.setRegistryName("barrel")/*,BrickDoor.setRegistryName("brick_door"),SandyDoor.setRegistryNNetherame("sandy_door"),NetherDoor.setRegistryName("nether_door"),
				EndDoor.setRegistryName("end_door")*/, MechanicalBeellows.setRegistryName("mechanical_bellows"));
		event.getRegistry().registerAll(Straw.setRegistryName("straw"), Ash.setRegistryName("ash"), Coke.setRegistryName("coke"),
				Fertilizer.setRegistryName("fertilizer"), FireStarter.setRegistryName("fire_starter"),
				ClayPot.setRegistryName("clay_pot"), BloomCool.setRegistryName("bloom_cool"), BloomFail.setRegistryName("bloom_fail"),
				CrackedPot.setRegistryName("cracked_pot"), BloomNiCool.setRegistryName("bloom_nickel_cool"), BloomNiFail.setRegistryName("bloom_nickel_fail"),
				SandyBrickItem.setRegistryName("sandy_brick_item"), UnfireSandyBrick.setRegistryName("unfired_sandy_brick"),
				UnfiredBrick.setRegistryName("unfired_brick"), AlcoholBottle.setRegistryName("alcohol_bottle"), VinegarBucket.setRegistryName("vinegar_bucket"),
				VinegarBottle.setRegistryName("vinegar_bottle"), Cheese.setRegistryName("cheese"), TinyCoke.setRegistryName("tiny_coke"));
		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"), YellowPot.setRegistryName("yellow_pot"), WhitePot.setRegistryName("white_pot"),
				RedPot.setRegistryName("red_pot"), PurplePot.setRegistryName("purple_pot"), PinkPot.setRegistryName("pink_pot"), OrangePot.setRegistryName("orange_pot"),
				MagentaPot.setRegistryName("magenta_pot"), LimePot.setRegistryName("lime_pot"), LightGrayPot.setRegistryName("light_gray_pot"),
				LightBluePot.setRegistryName("light_blue_pot"), GreenPot.setRegistryName("green_pot"), GrayPot.setRegistryName("gray_pot"), CyanPot.setRegistryName("cyan_pot"),
				BrownPot.setRegistryName("brown_pot"), BluePot.setRegistryName("blue_pot"), BlackPot.setRegistryName("black_pot"));

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
		return buildBlockItem(block, CHARCOAL_PIT, time);
	}
	
	public static BlockItemFuel buildBlockItem(Block block, CreativeModeTab group, int time) {
		return new BlockItemFuel(block, new Item.Properties().tab(group)).setBurnTime(time);
	}
	
	public static BlockItem buildBlockItem(Block block) {
		return buildBlockItem(block, CHARCOAL_PIT);
	}
	
	public static BlockItem buildBlockItemP(Block block) {
		return new BlockItem(block, new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1));
	}
	
	public static BlockItem buildBlockItem(Block block, CreativeModeTab group) {
		return new BlockItem(block, new Item.Properties().tab(group));
	}
	
	public static ItemFuel buildItem(CreativeModeTab group,int time) {
		return new ItemFuel(new Item.Properties().tab(group)).setBurnTime(time);
	}
	
	public static Item buildItem(CreativeModeTab group) {
		return new Item(new Item.Properties().tab(group));
	}
	
}
