package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

@EventBusSubscriber(modid = CharcoalPit.MODID, bus = Bus.MOD)
public class ModBlockRegistry {

	public static BlockThatch Thatch = new BlockThatch();
	public static RotatedPillarBlock LogPile = new BlockLogPile();
	public static Block CoalPile = new BlockCoalPile();
	public static Block WoodAsh = new BlockAsh(), CoalAsh = new BlockAsh(), SandyBrick = new Block(Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).strength(2, 6).requiresCorrectToolForDrops().harvestLevel(0).harvestTool(ToolType.PICKAXE)),
			CokeBlock = new Block(Properties.of(Material.WOOD, MaterialColor.COLOR_BLACK).strength(5F, 6F).harvestLevel(0).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()) {
				public int getFireSpreadSpeed(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter world, net.minecraft.core.BlockPos pos, net.minecraft.core.Direction face) {
					return 5;
				}

				;

				public int getFlammability(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter world, net.minecraft.core.BlockPos pos, net.minecraft.core.Direction face) {
					return 5;
				}

				;
			};
	public static FallingBlock Ash = new FallingBlock(Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.SAND));
	public static SlabBlock SandySlab=new SlabBlock(Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).strength(2, 6).requiresCorrectToolForDrops().harvestLevel(0).harvestTool(ToolType.PICKAXE));
	public static StairBlock SandyStair=new StairBlock(()->SandyBrick.defaultBlockState(), Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).strength(2, 6).requiresCorrectToolForDrops().harvestLevel(0).harvestTool(ToolType.PICKAXE));
	public static WallBlock SandyWall=new WallBlock(Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).strength(2, 6).requiresCorrectToolForDrops().harvestLevel(0).harvestTool(ToolType.PICKAXE));

	public static BlockCreosoteCollector SandyCollector=new BlockCreosoteCollector(Properties.copy(SandyBrick));
	public static BlockPotteryKiln Kiln=new BlockPotteryKiln();
	public static BlockCeramicPot CeramicPot=new BlockCeramicPot(MaterialColor.COLOR_ORANGE),WhitePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_WHITE),
			OrangePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_ORANGE),MagentaPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_MAGENTA),
			LightBluePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_LIGHT_BLUE),YellowPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_YELLOW),
			LimePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_LIGHT_GREEN),PinkPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_PINK),
			GrayPot = new BlockCeramicPot(MaterialColor.TERRACOTTA_GRAY), LightGrayPot = new BlockCeramicPot(MaterialColor.TERRACOTTA_LIGHT_GRAY),
			CyanPot = new BlockCeramicPot(MaterialColor.TERRACOTTA_CYAN), PurplePot = new BlockCeramicPot(MaterialColor.TERRACOTTA_PURPLE),
			BluePot = new BlockCeramicPot(MaterialColor.TERRACOTTA_BLUE), BrownPot = new BlockCeramicPot(MaterialColor.TERRACOTTA_BROWN),
			GreenPot = new BlockCeramicPot(MaterialColor.TERRACOTTA_GREEN), RedPot = new BlockCeramicPot(MaterialColor.TERRACOTTA_RED),
			BlackPot = new BlockCeramicPot(MaterialColor.TERRACOTTA_BLACK);
	public static BlockBellows Bellows = new BlockBellows();
	public static Block TuyereSandy = new Block(Properties.copy(SandyBrick));
	//public static BlockClayPot ClayPot=new BlockClayPot();
	public static BlockBloomery Bloomery = new BlockBloomery();
	public static BlockMainBloomery MainBloomery = new BlockMainBloomery();
	public static BlockBarrel Barrel = new BlockBarrel();
	public static BlockMechanicalBellows MechanicalBellows = new BlockMechanicalBellows();

	/*public static DoorBlock BrickDoor=new DoorBlock(AbstractBlock.Properties.from(Blocks.IRON_DOOR)),
			SandyDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR)),
			NetherDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR)),
			EndDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR));*/
	

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(Thatch.setRegistryName("thatch"), LogPile.setRegistryName("log_pile"), CoalPile.setRegistryName("coal_pile"),
				WoodAsh.setRegistryName("wood_ash"), CoalAsh.setRegistryName("coal_ash"),
				CokeBlock.setRegistryName("coke"), Ash.setRegistryName("ash"),
				SandyBrick.setRegistryName("sandy_brick"), SandySlab.setRegistryName("sandy_slab"), SandyStair.setRegistryName("sandy_stair"), SandyWall.setRegistryName("sandy_wall"),
				SandyCollector.setRegistryName("sandy_collector"),
				Kiln.setRegistryName("pottery_kiln"), Bellows.setRegistryName("bellows"),
				TuyereSandy.setRegistryName("sandy_tuyere"),
				Bloomery.setRegistryName("bloomery"), MainBloomery.setRegistryName("main_bloomery"),
				Barrel.setRegistryName("barrel")/*,BrickDoor.setRegistryName("brick_door"),SandyDoor.setRegistryName("sandy_door"),NetherDoor.setRegistryName("nether_door"),
				EndDoor.setRegistryName("end_door")*/, MechanicalBellows.setRegistryName("mechanical_bellows"));
		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"), YellowPot.setRegistryName("yellow_pot"), WhitePot.setRegistryName("white_pot"),
				RedPot.setRegistryName("red_pot"), PurplePot.setRegistryName("purple_pot"), PinkPot.setRegistryName("pink_pot"), OrangePot.setRegistryName("orange_pot"),
				MagentaPot.setRegistryName("magenta_pot"), LimePot.setRegistryName("lime_pot"), LightGrayPot.setRegistryName("light_gray_pot"),
				LightBluePot.setRegistryName("light_blue_pot"), GreenPot.setRegistryName("green_pot"), GrayPot.setRegistryName("gray_pot"), CyanPot.setRegistryName("cyan_pot"),
				BrownPot.setRegistryName("brown_pot"), BluePot.setRegistryName("blue_pot"), BlackPot.setRegistryName("black_pot"));
	}
	
}
