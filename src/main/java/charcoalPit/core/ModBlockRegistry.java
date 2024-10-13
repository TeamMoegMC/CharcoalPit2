package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.block.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class ModBlockRegistry {

	public static BlockThatch Thatch;
	public static RotatedPillarBlock LogPile;
	public static Block CoalPile;
	public static Block WoodAsh, CoalAsh, SandyBrick, CokeBlock;
	public static FallingBlock Ash;
	public static SlabBlock SandySlab;
	public static StairBlock SandyStair;
	public static WallBlock SandyWall;

	public static BlockCreosoteCollector SandyCollector;
	public static BlockPotteryKiln Kiln;
	public static BlockCeramicPot CeramicPot;/*,WhitePot,
			OrangePot,MagentaPot,
			LightBluePot,YellowPot,
			LimePot,PinkPot,
			GrayPot, LightGrayPot,
			CyanPot, PurplePot,
			BluePot, BrownPot,
			GreenPot, RedPot,
			BlackPot;*/
	public static BlockBellows Bellows;
	public static Block TuyereSandy;
	//public static BlockClayPot ClayPot=new BlockClayPot();
	public static BlockBloomery Bloomery;
	public static BlockMainBloomery MainBloomery;
	public static BlockBarrel Barrel;
	public static BlockMechanicalBellows MechanicalBellows;
	static {
		Thatch= new BlockThatch();
		LogPile = new BlockLogPile();
		CoalPile = new BlockCoalPile();
		WoodAsh = new BlockAsh();
		CoalAsh =new BlockAsh();
//		Ash = new BlockAsh();
		SandyBrick =new Block(Properties.of().mapColor(MapColor.STONE).strength(2, 6).requiresCorrectToolForDrops());
		CokeBlock= new Block(Properties.of().mapColor(MapColor.WOOD).strength(5F, 6F).requiresCorrectToolForDrops()) {
			public int getFireSpreadSpeed(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter world, net.minecraft.core.BlockPos pos, net.minecraft.core.Direction face) {
				return 5;
			}

			;

			public int getFlammability(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter world, net.minecraft.core.BlockPos pos, net.minecraft.core.Direction face) {
				return 5;
			}
        };
		Ash = new FallingBlock(Properties.of().mapColor(MapColor.SAND).strength(0.5F).sound(SoundType.SAND));
		SandySlab=new SlabBlock(Properties.of().mapColor(MapColor.STONE).strength(2, 6).requiresCorrectToolForDrops());
		SandyStair=new StairBlock(()->SandyBrick.defaultBlockState(), Properties.of().mapColor(MapColor.STONE).strength(2, 6).requiresCorrectToolForDrops());
		SandyWall=new WallBlock(Properties.of().mapColor(MapColor.STONE).strength(2, 6).requiresCorrectToolForDrops());

		SandyCollector=new BlockCreosoteCollector(Properties.copy(SandyBrick));
		Kiln=new BlockPotteryKiln();
		CeramicPot=new BlockCeramicPot(MapColor.COLOR_ORANGE);
		/*WhitePot=new BlockCeramicPot(MapColor.TERRACOTTA_WHITE);
		OrangePot=new BlockCeramicPot(MapColor.TERRACOTTA_ORANGE);
		MagentaPot=new BlockCeramicPot(MapColor.TERRACOTTA_MAGENTA);
		LightBluePot=new BlockCeramicPot(MapColor.TERRACOTTA_LIGHT_BLUE);
		YellowPot=new BlockCeramicPot(MapColor.TERRACOTTA_YELLOW);
		LimePot=new BlockCeramicPot(MapColor.TERRACOTTA_LIGHT_GREEN);
		PinkPot=new BlockCeramicPot(MapColor.TERRACOTTA_PINK);
		GrayPot = new BlockCeramicPot(MapColor.TERRACOTTA_GRAY);
		LightGrayPot = new BlockCeramicPot(MapColor.TERRACOTTA_LIGHT_GRAY);
		CyanPot = new BlockCeramicPot(MapColor.TERRACOTTA_CYAN);
		PurplePot = new BlockCeramicPot(MapColor.TERRACOTTA_PURPLE);
		BluePot = new BlockCeramicPot(MapColor.TERRACOTTA_BLUE);
		BrownPot = new BlockCeramicPot(MapColor.TERRACOTTA_BROWN);
		GreenPot = new BlockCeramicPot(MapColor.TERRACOTTA_GREEN);
		RedPot = new BlockCeramicPot(MapColor.TERRACOTTA_RED);
		BlackPot = new BlockCeramicPot(MapColor.TERRACOTTA_BLACK);*/
		Bellows = new BlockBellows();
		TuyereSandy = new Block(Properties.copy(SandyBrick));

		Bloomery = new BlockBloomery();
		MainBloomery = new BlockMainBloomery();
		Barrel = new BlockBarrel();
		MechanicalBellows = new BlockMechanicalBellows();
	}

	/*public static DoorBlock BrickDoor=new DoorBlock(AbstractBlock.Properties.from(Blocks.IRON_DOOR)),
			SandyDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR)),
			NetherDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR)),
			EndDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR));*/


	public static void registerBlocks(RegisterEvent event) {
		event.register(ForgeRegistries.Keys.BLOCKS,
				helper -> {
					helper.register(new ResourceLocation(CharcoalPit.MODID,"thatch"), Thatch);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"log_pile"), LogPile);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"coal_pile"), CoalPile);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"wood_ash"), WoodAsh);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"coal_ash"), CoalAsh);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"coke"), CokeBlock);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"ash"), Ash);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"sandy_brick"), SandyBrick);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"sandy_slab"), SandySlab);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"sandy_stair"), SandyStair);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"sandy_wall"), SandyWall);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"sandy_collector"), SandyCollector);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"pottery_kiln"),Kiln);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"bellows"),Bellows );
					helper.register(new ResourceLocation(CharcoalPit.MODID,"sandy_tuyere"), TuyereSandy);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"bloomery"), Bloomery);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"main_bloomery"), MainBloomery);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"barrel"), Barrel);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"mechanical_bellows"), MechanicalBellows);

					helper.register(new ResourceLocation(CharcoalPit.MODID,"ceramic_pot"),CeramicPot );
					/*helper.register(new ResourceLocation(CharcoalPit.MODID,"yellow_pot"), YellowPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"white_pot"), WhitePot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"red_pot"), RedPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"purple_pot"), PurplePot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"pink_pot"), PinkPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"orange_pot"), OrangePot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"magenta_pot"), MagentaPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"lime_pot"), LimePot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"light_gray_pot"), LightGrayPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"light_blue_pot"), LightBluePot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"green_pot"), GreenPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"gray_pot"), GrayPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"cyan_pot"), CyanPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"brown_pot"), BrownPot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"blue_pot"), BluePot);
					helper.register(new ResourceLocation(CharcoalPit.MODID,"black_pot"), BlackPot);*/
				}
		);

//		event.getRegistry().registerAll(Thatch.setRegistryName("thatch"), LogPile.setRegistryName("log_pile"), CoalPile.setRegistryName("coal_pile"),
//				WoodAsh.setRegistryName("wood_ash"), CoalAsh.setRegistryName("coal_ash"),
//				CokeBlock.setRegistryName("coke"), Ash.setRegistryName("ash"),

//				SandyBrick.setRegistryName("sandy_brick"), SandySlab.setRegistryName("sandy_slab"), SandyStair.setRegistryName("sandy_stair"), SandyWall.setRegistryName("sandy_wall"),
//				SandyCollector.setRegistryName("sandy_collector"),

//				Kiln.setRegistryName("pottery_kiln"), Bellows.setRegistryName("bellows"),
//				TuyereSandy.setRegistryName("sandy_tuyere"),

//				Bloomery.setRegistryName("bloomery"), MainBloomery.setRegistryName("main_bloomery"),

//				Barrel.setRegistryName("barrel")/*,BrickDoor.setRegistryName("brick_door"),SandyDoor.setRegistryName("sandy_door"),NetherDoor.setRegistryName("nether_door"),
//				EndDoor.setRegistryName("end_door")*/, MechanicalBellows.setRegistryName("mechanical_bellows"));

//		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"), YellowPot.setRegistryName("yellow_pot"), WhitePot.setRegistryName("white_pot"),
//				RedPot.setRegistryName("red_pot"), PurplePot.setRegistryName("purple_pot"), PinkPot.setRegistryName("pink_pot"), OrangePot.setRegistryName("orange_pot"),
//				MagentaPot.setRegistryName("magenta_pot"), LimePot.setRegistryName("lime_pot"), LightGrayPot.setRegistryName("light_gray_pot"),
//				LightBluePot.setRegistryName("light_blue_pot"), GreenPot.setRegistryName("green_pot"), GrayPot.setRegistryName("gray_pot"), CyanPot.setRegistryName("cyan_pot"),
//				BrownPot.setRegistryName("brown_pot"), BluePot.setRegistryName("blue_pot"), BlackPot.setRegistryName("black_pot"));
	}

}
