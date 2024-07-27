package charcoalPit.tile;

import charcoalPit.core.Config;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class TileActivePile extends BlockEntity implements TickableBlockEntity {

	//18H=18000ticks
	public int invalidTicks;
	public int burnTime;
	public int itemsLeft;
	public boolean isValid;
	public boolean isCoke;
	public FluidTank creosote;
	public static Fluid creosote_fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation("immersiveengineering", "creosote"));

	public TileActivePile() {
		this(false);
	}

	public TileActivePile(boolean coal) {
		super(ModTileRegistry.ActivePile);
		invalidTicks = 0;
		burnTime = coal ? Config.CokeTime.get() / 10 : Config.CharcoalTime.get() / 10;
		itemsLeft = 9;
		isValid = false;
		isCoke=coal;
		creosote=new FluidTank(1000);
	}

	@Override
	public void tick() {
		if(!this.level.isClientSide) {
			if (getBlockState().getValue(BlockStateProperties.LIT)) {
				checkValid();
				if (burnTime > 0) {
					burnTime--;
					if (burnTime % 1000 == 0)
						setChanged();
				} else {
					if (itemsLeft > 0) {
						itemsLeft--;
						creosote.fill(new FluidStack(creosote_fluid, isCoke ? Config.CokeCreosote.get() : Config.CharcoalCreosote.get()), FluidAction.EXECUTE);
						burnTime = isCoke ? Config.CokeTime.get() / 10 : Config.CharcoalTime.get() / 10;
					}else{
						this.level.setBlockAndUpdate(this.worldPosition, isCoke ? ModBlockRegistry.CoalAsh.defaultBlockState() : ModBlockRegistry.WoodAsh.defaultBlockState());
					}
				}
				if (creosote.getFluidAmount() > 0) {
					if (this.level.getBlockEntity(this.worldPosition.relative(Direction.DOWN)) instanceof TileActivePile) {
						TileActivePile down = (TileActivePile) this.level.getBlockEntity(this.worldPosition.relative(Direction.DOWN));
						creosote.drain(down.creosote.fill(creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE);
					}
				}
			}
		} else {
			if (level.getBlockState(this.worldPosition).getValue(BlockStateProperties.LIT)) {
				Random random = level.random;
				double centerX = worldPosition.getX() + 0.5F;
				double centerY = worldPosition.getY() + 2F;
				double centerZ = worldPosition.getZ() + 0.5F;
				level.addParticle(ParticleTypes.SMOKE, centerX + (random.nextDouble() - 0.5), centerY, centerZ + (random.nextDouble() - 0.5), 0.0D, 0.1D, 0.0D);
				level.addParticle(ParticleTypes.SMOKE, centerX + (random.nextDouble() - 0.5), centerY, centerZ + (random.nextDouble() - 0.5), 0.0D, 0.15D, 0.0D);
			}
		}
	}
	
	public void checkValid(){
		if(!isValid){
			boolean valid=true;
			Direction[] neighbors=Direction.values();
			//check structure
			for(Direction facing:neighbors){
				if(!MethodHelper.CharcoalPitIsValidBlock(level, this.worldPosition, facing, isCoke)){
					valid=false;
					break;
				}
			}
			if(valid){
				isValid=true;
				invalidTicks=0;
			}else{
				if(invalidTicks<100){
					invalidTicks++;
					for(Direction facing:neighbors){
						//set fire
						BlockState block=this.level.getBlockState(this.worldPosition.relative(facing));
						if(block.getBlock().isAir(block, this.level, this.worldPosition.relative(facing))||
								BaseFireBlock.canBePlacedAt(this.level, this.worldPosition.relative(facing),facing)){
							BlockState blockstate1 = BaseFireBlock.getState(this.level, this.worldPosition.relative(facing));
				            this.level.setBlock(this.worldPosition.relative(facing), blockstate1, 11);
						}
					}
				}else{
					this.level.removeBlock(this.worldPosition, false);
				}
			}
		}
	}
	
	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.putInt("invalid", invalidTicks);
		compound.putInt("time", burnTime);
		compound.putInt("items", itemsLeft);
		compound.putBoolean("valid", isValid);
		compound.putBoolean("coke", isCoke);
		compound.put("creosote", creosote.writeToNBT(new CompoundTag()));
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundTag compound) {
		super.load(state, compound);
		invalidTicks=compound.getInt("invalid");
		burnTime=compound.getInt("time");
		itemsLeft=compound.getInt("items");
		isValid=compound.getBoolean("valid");
		isCoke=compound.getBoolean("coke");
		creosote.readFromNBT(compound.getCompound("creosote"));
		setChanged();
	}
}
