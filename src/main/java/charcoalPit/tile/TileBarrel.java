package charcoalPit.tile;

import java.util.NoSuchElementException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.recipe.BarrelRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileBarrel extends BlockEntity {

	public FluidTank tank;
	public ItemStackHandler input, output;
	public int process,total;
	public boolean valid;
	public String recipeId;
	
	public TileBarrel(BlockPos blockPos, BlockState state) {
		super(ModTileRegistry.Barrel,blockPos,state);
		tank=new FluidTank(16000, f->f.getFluid().getFluidType().getTemperature()<450 && !f.getFluid().getFluidType().isAir()) {
			@Override
			protected void onContentsChanged() {
				setChanged();
				valid=false;
			}
		};
		input=new ItemStackHandler(1) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				valid=false;
			}
		};
		output=new ItemStackHandler(1) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				valid=false;
			}
		};
		process=-1;
		valid=false;
		recipeId="null";
		total=0;
	}


	public static void tick(Level level, BlockPos blockPos, BlockState state, TileBarrel tile) {
		if(!level.isClientSide) {
				if(!tile.valid) {
					tile.valid=true;
					BarrelRecipe recipe=BarrelRecipe.getRecipe(tile.input.getStackInSlot(0), tile.tank.getFluid(), level);
					if(tile.validateRecipe(recipe)>0) {
						tile.process=recipe.time;
						tile.total=tile.process;
						tile.recipeId=recipe.id.toString();
						tile.setChanged();
					}else {
						tile.process=-1;
						tile.total=0;
						tile.recipeId="null";
						tile.setChanged();
					}
				}
				if(tile.process>0) {
					tile.process--;
					if(tile.process%200==0)
						tile.setChanged();
				}
				else if(tile.process==0) {
					tile.process--;
					tile.total=0;
					BarrelRecipe recipe;
					try {
						recipe=(BarrelRecipe)level.getRecipeManager().byKey(new ResourceLocation(tile.recipeId)).get();
					}catch(NoSuchElementException e) {
						tile.recipeId="null";
						e.printStackTrace();
						return;
					}
					int rounds=tile.validateRecipe(recipe);
					boolean has_output_item=(recipe.flags&0b100)==0b100;
					boolean has_output_fluid=(recipe.flags&0b1000)==0b1000;
					tile.tank.drain(rounds*recipe.fluid_in.amount, FluidAction.EXECUTE);
					if(has_output_fluid) {
						tile.tank.setFluid(FluidStack.EMPTY);
						tile.tank.fill(new FluidStack(recipe.fluid_out.getFluid(), rounds*recipe.fluid_out.amount, recipe.fluid_out.nbt), FluidAction.EXECUTE);
					}
					ItemStack container=tile.input.getStackInSlot(0).getCraftingRemainingItem().copy();
					container.setCount(rounds*recipe.in_amount);
					tile.input.extractItem(0, rounds*recipe.in_amount, false);
					container=tile.input.insertItem(0,container,false);
					if(!container.isEmpty()){
						Containers.dropItemStack(level, tile.worldPosition.getX(), tile.worldPosition.getY(), tile.worldPosition.getZ(), container);
					}
					if(has_output_item) {
						tile.output.insertItem(0, new ItemStack(recipe.item_out.getItems()[0].getItem(), rounds*recipe.out_amount, recipe.nbt_out), false);
					}
				}else
					if(!tile.input.getStackInSlot(0).isEmpty()) {
						if(tile.input.getStackInSlot(0).getItem()==Items.GLASS_BOTTLE){
							if(tile.tank.getFluid().getFluid()==ModFluidRegistry.AlcoholStill.get()&&tile.tank.getFluidAmount()>=250){
								ItemStack stack=new ItemStack(ModItemRegistry.AlcoholBottle);
								stack.setTag(tile.tank.getFluid().getTag().copy());
								if(tile.output.insertItem(0,stack,true)==ItemStack.EMPTY){
									tile.output.insertItem(0,stack,false);
									tile.tank.drain(250,FluidAction.EXECUTE);
									tile.input.extractItem(0,1,false);
								}
							}
							if(tile.tank.getFluid().getFluid()==ModFluidRegistry.VinegarStill.get()&&tile.tank.getFluidAmount()>=250){
								ItemStack stack=new ItemStack(ModItemRegistry.VinegarBottle);
								if(tile.output.insertItem(0,stack,true)==ItemStack.EMPTY){
									tile.output.insertItem(0,stack,false);
									tile.tank.drain(250,FluidAction.EXECUTE);
									tile.input.extractItem(0,1,false);
								}
							}
						}
						tile.transferFluid();
					}
			
		}else {
			
		}
		
	}
	
	public int validateRecipe(BarrelRecipe recipe) {
		if(recipe==null)
			return 0;
		boolean void_excess_input_fluid=(recipe.flags&0b1)==0b1;
		boolean void_excess_output_item=(recipe.flags&0b10)==0b10;
		boolean has_output_item=(recipe.flags&0b100)==0b100;
		boolean has_output_fluid=(recipe.flags&0b1000)==0b1000;
		if(!recipe.fluid_in.test(tank.getFluid().getFluid()))
			return 0;
		if(!recipe.item_in.test(input.getStackInSlot(0)))
			return 0;
		//inputs valid;
		FluidTank sim_tank=new FluidTank(16000);
		FluidTank sim_fluid=new FluidTank(16000);
		sim_fluid.readFromNBT(tank.writeToNBT(new CompoundTag()));
		if(has_output_fluid&&
				tank.getFluid().isFluidEqual(new FluidStack(recipe.fluid_out.getFluid(), recipe.fluid_out.amount, recipe.fluid_out.nbt))) {
			sim_tank.readFromNBT(tank.writeToNBT(new CompoundTag()));
		}
		ItemStackHandler sim_in=new ItemStackHandler(1);
		sim_in.deserializeNBT(input.serializeNBT());
		ItemStackHandler sim_out=new ItemStackHandler(1);
		sim_out.deserializeNBT(output.serializeNBT());
		int rounds=0;
		boolean once_inserted_item=false;
		boolean ok;
		do {
			ok=true;
			if(sim_fluid.getFluidAmount()<recipe.fluid_in.amount)
				ok=false;
			else
				sim_fluid.drain(recipe.fluid_in.amount, FluidAction.EXECUTE);
			if(sim_in.getStackInSlot(0).isEmpty()||sim_in.getStackInSlot(0).getCount()<recipe.in_amount)
				ok=false;
			else 
				sim_in.extractItem(0, recipe.in_amount, false);
			if(has_output_fluid) {
				if(sim_tank.fill(new FluidStack(recipe.fluid_out.getFluid(), recipe.fluid_out.amount, recipe.fluid_out.nbt), FluidAction.EXECUTE)<recipe.fluid_out.amount)
					ok=false;
			}
			if(has_output_item) {
				if(sim_out.insertItem(0, new ItemStack(recipe.item_out.getItems()[0].getItem(), recipe.out_amount, recipe.nbt_out), false)!=ItemStack.EMPTY) {
					if(!once_inserted_item||!void_excess_output_item) {
						ok=false;
					}
				}else
					once_inserted_item=true;
			}
			if(ok)
				rounds++;
		}while(ok);
		if(rounds==0)
			return 0;
		if(!void_excess_input_fluid&&sim_fluid.getFluidAmount()>0)
			return 0;
		return rounds;
	}
	
	
	public void transferFluid() {
		FluidActionResult empty=tryEmptyContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, false);
		if(empty.success) {
			if(empty.getResult()!=null&&!empty.getResult().isEmpty()) {
				if(output.insertItem(0, empty.getResult(), true).isEmpty()) {
					//all fits
					output.insertItem(0, tryEmptyContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, true).getResult(), false);
					input.extractItem(0, 1, false);
				}
			}else {
				//no container
				tryEmptyContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, true);
				input.extractItem(0, 1, false);
			}
		}else {
			FluidActionResult fill=tryFillContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, false);
			if(fill.success) {
				if(output.insertItem(0, fill.getResult(), true).isEmpty()) {
					//all fits
					output.insertItem(0, tryFillContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, true).getResult(), false);
					input.extractItem(0, 1, false);
				}
			}else {
				//fail
			}
		}
	}
	//forge ingores doDrain
	public static FluidActionResult tryEmptyContainer(@Nonnull ItemStack container, IFluidHandler fluidDestination, int maxAmount, @Nullable Player player, boolean doDrain) {
		ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
        return FluidUtil.getFluidHandler(containerCopy)
                .map(containerFluidHandler -> {

                    // We are acting on a COPY of the stack, so performing changes is acceptable even if we are simulating.
                    FluidStack transfer = FluidUtil.tryFluidTransfer(fluidDestination, containerFluidHandler, maxAmount, false);
                    if (transfer.isEmpty())
                        return FluidActionResult.FAILURE;

                    if(doDrain) {
                    	FluidUtil.tryFluidTransfer(fluidDestination, containerFluidHandler, maxAmount, true);
                    }else {
                    	containerFluidHandler.drain(transfer, FluidAction.EXECUTE);
                    }
                    
                    if (doDrain && player != null)
                    {
                        SoundEvent soundevent = transfer.getFluid().getFluidType().getSound(transfer,SoundActions.BUCKET_EMPTY);
                        player.level().playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }

                    ItemStack resultContainer = containerFluidHandler.getContainer();
                    return new FluidActionResult(resultContainer);
                })
                .orElse(FluidActionResult.FAILURE);
	}
	
	public static FluidActionResult tryFillContainer(@Nonnull ItemStack container, IFluidHandler fluidSource, int maxAmount, @Nullable Player player, boolean doFill) {
		ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
        return FluidUtil.getFluidHandler(containerCopy)
                .map(containerFluidHandler -> {
                    FluidStack simulatedTransfer = FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, false);
                    if (!simulatedTransfer.isEmpty())
                    {
                        if (doFill)
                        {
                            FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, true);
                            if (player != null)
                            {
                                SoundEvent soundevent = simulatedTransfer.getFluid().getFluidType().getSound(simulatedTransfer, SoundActions.BUCKET_FILL);
                                player.level().playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                        else
                        {
                            //broken in forge
                        	containerFluidHandler.fill(simulatedTransfer, IFluidHandler.FluidAction.EXECUTE);
                        }

                        ItemStack resultContainer = containerFluidHandler.getContainer();
                        return new FluidActionResult(resultContainer);
                    }
                    return FluidActionResult.FAILURE;
                })
                .orElse(FluidActionResult.FAILURE);
	}
	
	public void dropInventory() {
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), input.getStackInSlot(0));
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), output.getStackInSlot(0));
	}


	public static Capability<IFluidHandler> FLUID= ForgeCapabilities.FLUID_HANDLER;
    public LazyOptional<IFluidHandler> fluid_out=LazyOptional.of(()->tank);


	public static Capability<IFluidHandlerItem> ITEM=ForgeCapabilities.FLUID_HANDLER_ITEM;

    public LazyOptional<IItemHandler> item_out=LazyOptional.of(()->new IItemHandler() {
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).isPresent()||
					BarrelRecipe.isValidItem(stack, getLevel())||stack.getItem()==Items.GLASS_BOTTLE;
		}
		
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if(slot==0&&isItemValid(slot, stack))
				return input.insertItem(0, stack, simulate);
			return stack;
		}
		
		@Override
		public ItemStack getStackInSlot(int slot) {
			if(slot==0)
				return input.getStackInSlot(0);
			return output.getStackInSlot(0);
		}
		
		@Override
		public int getSlots() {
			return 2;
		}
		
		@Override
		public int getSlotLimit(int slot) {
			if(slot==0)
				return input.getSlotLimit(0);
			return output.getSlotLimit(0);
		}
		
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(slot==1)
				return output.extractItem(0, amount, simulate);
			return ItemStack.EMPTY;
		}
	});
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap==FLUID)
			return fluid_out.cast();
		if(cap==ITEM)
			return item_out.cast();
		return super.getCapability(cap, side);
	}
	
	
	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put("Fluid", tank.writeToNBT(new CompoundTag()));
		compound.put("input", input.serializeNBT());
		compound.put("output", output.serializeNBT());
		compound.putInt("process", process);
		compound.putInt("total", total);
		compound.putBoolean("valid", valid);
		compound.putString("recipeId", recipeId);
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load( nbt);
		tank.readFromNBT(nbt.getCompound("Fluid"));
		input.deserializeNBT(nbt.getCompound("input"));
		output.deserializeNBT(nbt.getCompound("output"));
		process=nbt.getInt("process");
		total=nbt.getInt("total");
		valid=nbt.getBoolean("valid");
		recipeId=nbt.getString("recipeId");
	}

}
