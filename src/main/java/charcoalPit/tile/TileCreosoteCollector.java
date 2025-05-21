package charcoalPit.tile;

import charcoalPit.core.ModTileRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileCreosoteCollector extends BlockEntity{
	
	public FluidTank creosote;
	int tick;
	boolean flag;
	
	public TileCreosoteCollector(BlockPos blockPos, BlockState state) {
		super(ModTileRegistry.CreosoteCollector,blockPos,state);
		tick=0;
		creosote=new FluidTank(8000);
	}

	public static void tick(Level level, BlockPos blockPos, BlockState state, TileCreosoteCollector tile) {
		if(tile.tick<20){
			tile.tick++;
		}else{
			tile.tick=0;
			tile.flag=false;
			//collect creosote
			if(tile.creosote.getFluidAmount()<tile.creosote.getCapacity()&&tile.level.getBlockEntity(tile.worldPosition.relative(Direction.UP))instanceof TileActivePile){
				TileActivePile up=(TileActivePile)tile.level.getBlockEntity(tile.worldPosition.relative(Direction.UP));
				tile.flag=tile.flag||up.creosote.drain(tile.creosote.fill(up.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
				for(Direction facing:Direction.Plane.HORIZONTAL){
					if(tile.creosote.getFluidAmount()<tile.creosote.getCapacity())
						tile.flag=tile.flag||tile.collectCreosote(tile.worldPosition.relative(Direction.UP).relative(facing), facing, 3);
				}
			}
			//chanel creosote
			if(tile.level.hasNeighborSignal(tile.worldPosition)){
				for(Direction facing:Direction.Plane.HORIZONTAL){
					if(tile.creosote.getFluidAmount()<tile.creosote.getCapacity())
						tile.flag=tile.flag||tile.chanelCreosote(tile.worldPosition.relative(facing), facing, 3);
				}
			}
			//output creosote
			if(tile.creosote.getFluidAmount()>0&&tile.level.hasNeighborSignal(tile.worldPosition)){
				BlockEntity tiledown=tile.level.getBlockEntity(tile.worldPosition.relative(Direction.DOWN));
				if(tiledown!=null){
					tile.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.UP).ifPresent((handler)->{
						tile.flag=tile.flag||tile.creosote.drain(handler.fill(tile.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
					});
				}
			}
			if(tile.flag)
				tile.setChanged();
		}
		
	}
	public boolean collectCreosote(BlockPos pos, Direction facing, int runs){
		boolean flag=false;
		if(this.level.getBlockEntity(pos)instanceof TileActivePile){
			TileActivePile up=(TileActivePile)this.level.getBlockEntity(pos);
			flag=up.creosote.drain(creosote.fill(up.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
			if(runs>0&&creosote.getFluidAmount()<creosote.getCapacity())
				flag=flag||collectCreosote(pos.relative(facing), facing, --runs);
		}
		return flag;
	}
	public boolean chanelCreosote(BlockPos pos, Direction facing, int runs){
		boolean flag=false;
		if(this.level.getBlockEntity(pos)instanceof TileCreosoteCollector){
			TileCreosoteCollector up=(TileCreosoteCollector)this.level.getBlockEntity(pos);
			flag=up.creosote.drain(creosote.fill(up.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
			if(runs>0&&creosote.getFluidAmount()<creosote.getCapacity())
				flag=flag||chanelCreosote(pos.relative(facing), facing, --runs);
		}
		return flag;
	}
	public IFluidHandler external=new IFluidHandler() {
		
		@Override
		public boolean isFluidValid(int tank, FluidStack stack) {
			return creosote.isFluidValid(tank, stack);
		}
		
		@Override
		public int getTanks() {
			return creosote.getTanks();
		}
		
		@Override
		public int getTankCapacity(int tank) {
			return creosote.getTankCapacity(tank);
		}
		
		@Override
		public FluidStack getFluidInTank(int tank) {
			return creosote.getFluidInTank(tank);
		}
		
		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return 0;
		}
		
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return creosote.drain(maxDrain, action);
		}
		
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return creosote.drain(resource, action);
		}
	};
	
	public LazyOptional<IFluidHandler> fluid=LazyOptional.of(()->external);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap==ForgeCapabilities.FLUID_HANDLER) {
			return fluid.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void setRemoved() {
		fluid.invalidate();
		super.setRemoved();
	}
	
	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put("creosote", creosote.writeToNBT(new CompoundTag()));
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		creosote.readFromNBT(nbt.getCompound("creosote"));
	}

}
