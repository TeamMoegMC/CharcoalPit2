package charcoalPit.tile;

import charcoalPit.block.BlockBloomery;
import charcoalPit.block.BlockMainBloomery;
import charcoalPit.core.Config;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.BloomeryRecipe;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class TileBloomery extends BlockEntity implements TickableBlockEntity {

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
		if(!level.isClientSide) {
			if(!done) {
				checkValid();
				if(!getBlockState().getValue(BlockBloomery.DUMMY)) {
					if (burnTime > 0 && airTicks > 0) {
						burnTime--;
						changetime++;
						if (burnTime % 200 == 0)
							setChanged();
						if (airBuffer > 0) {
							airBuffer--;
							airTicks--;
						}
						if (changetime > 40) {
							BlockPos down = worldPosition.relative(Direction.DOWN);
							if (level.getBlockState(down).getBlock() == ModBlockRegistry.MainBloomery) {
								if (airBuffer < 1) {
									level.setBlockAndUpdate(down, ModBlockRegistry.MainBloomery.defaultBlockState().setValue(BlockMainBloomery.STAGE, 1));
								} else {
									level.setBlockAndUpdate(down, ModBlockRegistry.MainBloomery.defaultBlockState().setValue(BlockMainBloomery.STAGE, 2));
								}
							}
							changetime = 0;
						}

					} else {
						if (burnTime != -1) {
							//done
							done = true;
							level.setBlockAndUpdate(worldPosition.relative(Direction.DOWN), ModBlockRegistry.MainBloomery.defaultBlockState().setValue(BlockMainBloomery.STAGE, 0));
							ingots = 4;
							ore = new OneItemHandler(4);
							fuel = new OneItemHandler(4);
							burnTime = Config.BloomCooldown.get();
							if (level.getBlockState(worldPosition.relative(Direction.UP)).getBlock() == ModBlockRegistry.Bloomery) {
								TileBloomery dummy = ((TileBloomery) level.getBlockEntity(worldPosition.relative(Direction.UP)));
								ingots += 4;
								dummy.ore = new OneItemHandler(4);
								dummy.fuel = new OneItemHandler(4);
								level.playSound(null, worldPosition, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.5F, 1F);
								level.removeBlock(worldPosition.relative(Direction.UP), false);
							}else {
								if(level.getBlockState(worldPosition.relative(Direction.UP)).getBlock()==Blocks.FIRE)
									level.removeBlock(worldPosition.relative(Direction.UP), false);
							}
							if(airTicks<=0) {
								workCount=0;
								level.setBlockAndUpdate(worldPosition, ModBlockRegistry.Bloomery.defaultBlockState().setValue(BlockBloomery.STAGE, 10));
							}else {
								level.setBlockAndUpdate(worldPosition, ModBlockRegistry.Bloomery.defaultBlockState().setValue(BlockBloomery.STAGE, 11));
							}
							setChanged();
						}
					}
				}
			}else {
				burnTime--;
				if(burnTime%200==0)
					setChanged();
				if(burnTime==0) {
					//set cool
					level.setBlockAndUpdate(worldPosition, ModBlockRegistry.Bloomery.defaultBlockState().setValue(BlockBloomery.STAGE, 12));
					setChanged();
				}
			}
		}
	}


	public BloomeryRecipe getRecipe() {
		if(recipe==null && getBlockState().getValue(BlockBloomery.DUMMY)) {
			TileBloomery master=((TileBloomery)level.getBlockEntity(worldPosition.relative(Direction.DOWN)));
			recipe=master.getRecipe();
		}
		if(recipe==null) {
			recipe=BloomeryRecipe.getRecipe(ore.getStackInSlot(0), level);
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
				level.setBlockAndUpdate(worldPosition, ModBlockRegistry.Bloomery.defaultBlockState().setValue(BlockBloomery.STAGE, 11));
			}
		}
	}
	
	public void dropInventory() {
		if (level!=null) {
            for (int i = 0; i < ore.getSlots(); i++) {
                if (!ore.getStackInSlot(i).isEmpty())
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), ore.extractItem(i, 1, false));
            }
            for (int i = 0; i < fuel.getSlots(); i++) {
                if (!fuel.getStackInSlot(i).isEmpty())
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), fuel.extractItem(i, 1, false));
            }
            if (getRecipe() == null) {
                return;
            }
            if (ingots > 0) {
                if (workCount != -1) {
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), new ItemStack(getRecipe().output.getItems()[0].getItem(), workCount));
                    ingots -= workCount;
                    while (workCount > 0) {
                        int i = ExperienceOrb.getExperienceValue(workCount);
                        workCount -= i;
                        level.addFreshEntity(new ExperienceOrb(level, (double) worldPosition.getX() + 0.5D, (double) worldPosition.getY() + 0.5D, (double) worldPosition.getZ() + 0.5D, i));
                    }
                    if (ingots > 0) {
						Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), new ItemStack(getRecipe().cool.getItems()[0].getItem(), ingots));
					}
				} else {
					Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), new ItemStack(getRecipe().fail.getItems()[0].getItem(), ingots));
				}
			}
		}
	}
	//pile ignitr
	public void setDummy(boolean dum) {
		level.setBlockAndUpdate(worldPosition,getBlockState().setValue(BlockBloomery.DUMMY,dum));
	}
	public void checkValid() {
		if(!isValid) {
			if(done || burnTime<0 || MethodHelper.Bloomery2ValidPosition(level, worldPosition, getBlockState().getValue(BlockBloomery.DUMMY), burnTime>0)) {
				isValid=true;
				invalidTicks=0;
			}else {
				if(invalidTicks<100) {
					invalidTicks++;
					//set fire
					BlockPos up=worldPosition.relative(Direction.UP);
					BlockState block=this.level.getBlockState(up);
					if(block.getBlock().isAir(block, this.level, up)||
							BaseFireBlock.canBePlacedAt(this.level, up,Direction.UP)){
						BlockState blockstate1 = BaseFireBlock.getState(this.level, up);
			            this.level.setBlock(up, blockstate1, 11);
					}
				}else {
					burnTime = -1;
					airBuffer = 0;
					airTicks = -1;
					level.setBlockAndUpdate(this.worldPosition, getBlockState().setValue(BlockBloomery.STAGE, 8));

					BlockPos charm = worldPosition.relative(Direction.UP);
					BlockPos down = worldPosition.relative(Direction.DOWN);
					if (getBlockState().getValue(BlockBloomery.DUMMY)) {
						if (level.getBlockState(down.relative(Direction.DOWN)).getBlock() == ModBlockRegistry.MainBloomery) {
							level.setBlockAndUpdate(down.relative(Direction.DOWN), ModBlockRegistry.MainBloomery.defaultBlockState().setValue(BlockMainBloomery.STAGE, 0));
						}
						if (level.getBlockState(down).getBlock() == ModBlockRegistry.Bloomery) {
							level.setBlockAndUpdate(down, ModBlockRegistry.Bloomery.defaultBlockState().setValue(BlockBloomery.STAGE, 8));
							TileBloomery master = ((TileBloomery) level.getBlockEntity(down));
							master.airBuffer = 0;
							master.airTicks = -1;
							master.burnTime = -1;
						}
						if (level.getBlockState(charm).getBlock() == Blocks.FIRE) {
							level.removeBlock(charm, false);
						}
					}else {
						if (level.getBlockState(down).getBlock() == ModBlockRegistry.MainBloomery) {
							level.setBlockAndUpdate(down, ModBlockRegistry.MainBloomery.defaultBlockState().setValue(BlockMainBloomery.STAGE, 0));
						}
						if (level.getBlockState(charm).getBlock() == ModBlockRegistry.Bloomery) {
							level.setBlockAndUpdate(charm, ModBlockRegistry.Bloomery.defaultBlockState().setValue(BlockBloomery.STAGE, 8).setValue(BlockBloomery.DUMMY,true));
							TileBloomery dummy = ((TileBloomery) level.getBlockEntity(charm));
							dummy.burnTime = -1;
							BlockPos top = charm.relative(Direction.UP);
							if (level.getBlockState(top).getBlock() == Blocks.FIRE)
								level.removeBlock(top, false);
						}
						if (level.getBlockState(charm).getBlock() == Blocks.FIRE)
							level.removeBlock(charm, false);
						
					}
				}
			}
		}
	}
	
	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
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
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
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
