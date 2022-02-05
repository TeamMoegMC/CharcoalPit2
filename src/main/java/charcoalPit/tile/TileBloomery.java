package charcoalPit.tile;

import charcoalPit.block.BlockBloomery;
import charcoalPit.block.BlockMainBloomery;
import charcoalPit.core.Config;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.BloomeryRecipe;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class TileBloomery extends TileEntity implements ITickableTileEntity {

	public boolean isValid;
	public int changetime;
	public int burnTime;
	public int airTicks;
	public int airBuffer;
	public int invalidTicks;
	public int workCount;
	public boolean done;
	public OneItemHandler ore;
	public OneItemHandler fuel;
	public int ingots;
	public BloomeryRecipe recipe;
	
	public TileBloomery() {
		super(ModTileRegistry.Bloomery2);
		isValid = false;
		changetime = 0;
		invalidTicks = 0;
		burnTime = -1;
		airTicks = -1;
		airBuffer = 0;
		workCount = -1;
		done = false;
		ore = new OneItemHandler(4);
		fuel = new OneItemHandler(4);
		ingots = 0;
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
			if(!done) {
				checkValid();
				if(!getBlockState().get(BlockBloomery.DUMMY)) {
					if (burnTime > 0 && airTicks > 0) {
						burnTime--;
						changetime++;
						if (burnTime % 200 == 0)
							markDirty();
						if (airBuffer > 0) {
							airBuffer--;
							airTicks--;
						}
						if (changetime > 40) {
							BlockPos down = pos.offset(Direction.DOWN);
							if (world.getBlockState(down).getBlock() == ModBlockRegistry.MainBloomery) {
								if (airBuffer < 1) {
									world.setBlockState(down, ModBlockRegistry.MainBloomery.getDefaultState().with(BlockMainBloomery.STAGE, 1));
								} else {
									world.setBlockState(down, ModBlockRegistry.MainBloomery.getDefaultState().with(BlockMainBloomery.STAGE, 2));
								}
							}
							changetime = 0;
						}

					} else {
						if (burnTime != -1) {
							//done
							done = true;
							world.setBlockState(pos.offset(Direction.DOWN), ModBlockRegistry.MainBloomery.getDefaultState().with(BlockMainBloomery.STAGE, 0));
							ingots = 4;
							ore = new OneItemHandler(4);
							fuel = new OneItemHandler(4);
							burnTime = Config.BloomCooldown.get();
							if (world.getBlockState(pos.offset(Direction.UP)).getBlock() == ModBlockRegistry.Bloomery) {
								TileBloomery dummy = ((TileBloomery) world.getTileEntity(pos.offset(Direction.UP)));
								ingots += 4;
								dummy.ore = new OneItemHandler(4);
								dummy.fuel = new OneItemHandler(4);
								world.playSound(null, pos, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.5F, 1F);
								world.removeBlock(pos.offset(Direction.UP), false);
							}else {
								if(world.getBlockState(pos.offset(Direction.UP)).getBlock()==Blocks.FIRE)
									world.removeBlock(pos.offset(Direction.UP), false);
							}
							if(airTicks<=0) {
								workCount=0;
								world.setBlockState(pos, ModBlockRegistry.Bloomery.getDefaultState().with(BlockBloomery.STAGE, 10));
							}else {
								world.setBlockState(pos, ModBlockRegistry.Bloomery.getDefaultState().with(BlockBloomery.STAGE, 11));
							}
							markDirty();
						}
					}
				}
			}else {
				burnTime--;
				if(burnTime%200==0)
					markDirty();
				if(burnTime==0) {
					//set cool
					world.setBlockState(pos, ModBlockRegistry.Bloomery.getDefaultState().with(BlockBloomery.STAGE, 12));
					markDirty();
				}
			}
		}
	}


	public BloomeryRecipe getRecipe() {
		if(recipe==null && getBlockState().get(BlockBloomery.DUMMY)) {
			TileBloomery master=((TileBloomery)world.getTileEntity(pos.offset(Direction.DOWN)));
			recipe=master.getRecipe();
		}
		if(recipe==null) {
			recipe=BloomeryRecipe.getRecipe(ore.getStackInSlot(0), world);
		}
		return recipe;
	}
	
	public void ignite() {
		burnTime=Config.BloomeryTime.get()*2;
		airTicks=Config.BloomeryTime.get();
	}
	
	public void blow(int air) {
        airBuffer = Math.min(1200, airBuffer + air);
    }
	
	public void work() {
		if(workCount!=-1) {
			workCount++;
			if(workCount>=ingots) {
				world.setBlockState(pos, ModBlockRegistry.Bloomery.getDefaultState().with(BlockBloomery.STAGE, 11));
			}
		}
	}
	
	public void dropInventory() {
		if (world!=null) {
			for (int i = 0; i < ore.getSlots(); i++) {
				if (!ore.getStackInSlot(i).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ore.extractItem(i, 1, false));
			}
			for (int i = 0; i < fuel.getSlots(); i++) {
				if (!fuel.getStackInSlot(i).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), fuel.extractItem(i, 1, false));
			}
			if (ingots > 0) {
				if (workCount != -1) {
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(getRecipe().output.getMatchingStacks()[0].getItem(), workCount));
					ingots -= workCount;
					while (workCount > 0) {
						int i = ExperienceOrbEntity.getXPSplit(workCount);
						workCount -= i;
						world.addEntity(new ExperienceOrbEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, i));
					}
					if (ingots > 0) {
						InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(getRecipe().cool.getMatchingStacks()[0].getItem(), ingots));
					}
				} else {
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(getRecipe().fail.getMatchingStacks()[0].getItem(), ingots));
				}
			}
		}
	}
	//pile ignitr
	public void setDummy(boolean dum) {
		world.setBlockState(pos,getBlockState().with(BlockBloomery.DUMMY,dum));
	}
	public void checkValid() {
		if(!isValid) {
			if(done || burnTime<0 || MethodHelper.Bloomery2ValidPosition(world, pos, getBlockState().get(BlockBloomery.DUMMY), burnTime>0)) {
				isValid=true;
				invalidTicks=0;
			}else {
				if(invalidTicks<100) {
					invalidTicks++;
					//set fire
					BlockPos up=pos.offset(Direction.UP);
					BlockState block=this.world.getBlockState(up);
					if(block.getBlock().isAir(block, this.world, up)||
							AbstractFireBlock.canLightBlock(this.world, up,Direction.UP)){
						BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(this.world, up);
			            this.world.setBlockState(up, blockstate1, 11);
					}
				}else {
					burnTime = -1;
					airBuffer = 0;
					airTicks = -1;
					world.setBlockState(this.pos, getBlockState().with(BlockBloomery.STAGE, 8));

					BlockPos charm = pos.offset(Direction.UP);
					BlockPos down = pos.offset(Direction.DOWN);
					if (getBlockState().get(BlockBloomery.DUMMY)) {
						if (world.getBlockState(down.offset(Direction.DOWN)).getBlock() == ModBlockRegistry.MainBloomery) {
							world.setBlockState(down.offset(Direction.DOWN), ModBlockRegistry.MainBloomery.getDefaultState().with(BlockMainBloomery.STAGE, 0));
						}
						if (world.getBlockState(down).getBlock() == ModBlockRegistry.Bloomery) {
							world.setBlockState(down, ModBlockRegistry.Bloomery.getDefaultState().with(BlockBloomery.STAGE, 8));
							TileBloomery master = ((TileBloomery) world.getTileEntity(down));
							master.airBuffer = 0;
							master.airTicks = -1;
							master.burnTime = -1;
						}
						if (world.getBlockState(charm).getBlock() == Blocks.FIRE) {
							world.removeBlock(charm, false);
						}
					}else {
						if (world.getBlockState(down).getBlock() == ModBlockRegistry.MainBloomery) {
							world.setBlockState(down, ModBlockRegistry.MainBloomery.getDefaultState().with(BlockMainBloomery.STAGE, 0));
						}
						if (world.getBlockState(charm).getBlock() == ModBlockRegistry.Bloomery) {
							world.setBlockState(charm, ModBlockRegistry.Bloomery.getDefaultState().with(BlockBloomery.STAGE, 8).with(BlockBloomery.DUMMY,true));
							TileBloomery dummy = ((TileBloomery) world.getTileEntity(charm));
							dummy.burnTime = -1;
							BlockPos top = charm.offset(Direction.UP);
							if (world.getBlockState(top).getBlock() == Blocks.FIRE)
								world.removeBlock(top, false);
						}
						if (world.getBlockState(charm).getBlock() == Blocks.FIRE)
							world.removeBlock(charm, false);
						
					}
				}
			}
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putBoolean("valid", isValid);
		compound.putInt("burn", burnTime);
		compound.putInt("air", airTicks);
		compound.putInt("buffer", airBuffer);
		compound.putInt("invalid", invalidTicks);
		compound.putInt("work", workCount);
		compound.putBoolean("done", done);
		compound.put("ore", ore.serializeNBT());
		compound.put("fuel", fuel.serializeNBT());
		compound.putInt("ingots", ingots);
		return compound;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		isValid=nbt.getBoolean("valid");
		burnTime=nbt.getInt("burn");
		airTicks=nbt.getInt("air");
		airBuffer=nbt.getInt("buffer");
		invalidTicks=nbt.getInt("invalid");
		workCount=nbt.getInt("work");
		done=nbt.getBoolean("done");
		ore.deserializeNBT(nbt.getCompound("ore"));
		fuel.deserializeNBT(nbt.getCompound("fuel"));
		ingots=nbt.getInt("ingots");
	}

	public static class OneItemHandler extends ItemStackHandler{
		public OneItemHandler(int s) {
			super(s);
		}
		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
	}
	
}