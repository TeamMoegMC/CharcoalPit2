package charcoalPit.tile;

import charcoalPit.core.Config;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class TileActivePile extends BlockEntity {

	//18H=18000ticks
	public int invalidTicks;
	public int burnTime;
	public int itemsLeft;
	public boolean isValid;
	public boolean isCoke;
	public FluidTank creosote;
	public static Fluid creosote_fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation("immersiveengineering", "creosote"));

	public TileActivePile(BlockPos blockPos, BlockState state) {
		this(false,blockPos,state);
	}

	public TileActivePile(boolean coal,BlockPos blockPos, BlockState state) {
		super(ModTileRegistry.ActivePile,blockPos,state);
		invalidTicks = 0;
		burnTime = coal ? Config.CokeTime.get() / 10 : Config.CharcoalTime.get() / 10;
		itemsLeft = 9;
		isValid = false;
		isCoke=coal;
		creosote=new FluidTank(1000);
	}

	public static void tick(Level level, BlockPos blockPos, BlockState state, TileActivePile tile) {
		if(!level.isClientSide) {
			if (state.getValue(BlockStateProperties.LIT)) {
				tile.checkValid();
				if (tile.burnTime > 0) {
					tile.burnTime--;
					if (tile.burnTime % 1000 == 0)
						tile.setChanged();
				} else {
					if (tile.itemsLeft > 0) {
						tile.itemsLeft--;
						tile.creosote.fill(new FluidStack(creosote_fluid, tile.isCoke ? Config.CokeCreosote.get() : Config.CharcoalCreosote.get()), FluidAction.EXECUTE);
						tile.burnTime = tile.isCoke ? Config.CokeTime.get() / 10 : Config.CharcoalTime.get() / 10;
					}else{
						level.setBlockAndUpdate(tile.worldPosition, tile.isCoke ? ModBlockRegistry.CoalAsh.defaultBlockState() : ModBlockRegistry.WoodAsh.defaultBlockState());
					}
				}
				if (tile.creosote.getFluidAmount() > 0) {
					if (level.getBlockEntity(tile.worldPosition.relative(Direction.DOWN)) instanceof TileActivePile) {
						TileActivePile down = (TileActivePile) tile.level.getBlockEntity(tile.worldPosition.relative(Direction.DOWN));
						tile.creosote.drain(down.creosote.fill(tile.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE);
					}
				}
			}
		} else {
			if (level.getBlockState(tile.worldPosition).getValue(BlockStateProperties.LIT)) {
				RandomSource random = level.random;
				double centerX = tile.worldPosition.getX() + 0.5F;
				double centerY = tile.worldPosition.getY() + 2F;
				double centerZ = tile.worldPosition.getZ() + 0.5F;
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
						if(block.isAir()||
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
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt("invalid", invalidTicks);
		compound.putInt("time", burnTime);
		compound.putInt("items", itemsLeft);
		compound.putBoolean("valid", isValid);
		compound.putBoolean("coke", isCoke);
		compound.put("creosote", creosote.writeToNBT(new CompoundTag()));
	}
	
	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		invalidTicks=compound.getInt("invalid");
		burnTime=compound.getInt("time");
		itemsLeft=compound.getInt("items");
		isValid=compound.getBoolean("valid");
		isCoke=compound.getBoolean("coke");
		creosote.readFromNBT(compound.getCompound("creosote"));
		setChanged();
	}
}
