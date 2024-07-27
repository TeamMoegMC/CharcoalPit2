package charcoalPit.tile;

import charcoalPit.block.BlockPotteryKiln;
import charcoalPit.block.BlockPotteryKiln.EnumKilnTypes;
import charcoalPit.core.Config;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.PotteryKilnRecipe;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.items.ItemStackHandler;

public class TilePotteryKiln extends BlockEntity implements TickableBlockEntity{
	
	public int invalidTicks;
	public int burnTime;
	public float xp;
	public boolean isValid;
	public PotteryStackHandler pottery;
	
	public TilePotteryKiln() {
		super(ModTileRegistry.PotteryKiln);
		invalidTicks=0;
		burnTime=-1;
		xp=0;
		isValid=false;
		pottery=new PotteryStackHandler();
	}
	
	@Override
	public void tick() {
		if(!this.level.isClientSide&&burnTime>-1){
			checkValid();
			if(burnTime>0) {
				burnTime--;
				if(burnTime%500==0)
					setChanged();
			}else{
				if(burnTime==0){
					PotteryKilnRecipe result=PotteryKilnRecipe.getResult(pottery.getStackInSlot(0), this.level);
					if(result!=null) {
						ItemStack out=PotteryKilnRecipe.processClayPot(pottery.getStackInSlot(0), level);
						if(out.isEmpty())
							out=new ItemStack(result.output,pottery.getStackInSlot(0).getCount());
						xp=result.xp*pottery.getStackInSlot(0).getCount();
						pottery.setStackInSlot(0, out);
					}
					this.level.setBlockAndUpdate(worldPosition, ModBlockRegistry.Kiln.defaultBlockState().setValue(BlockPotteryKiln.TYPE, EnumKilnTypes.COMPLETE));
					this.level.removeBlock(worldPosition.relative(Direction.UP), false);
					burnTime--;
					setChanged();
				}
			}
		}
	}
	public void setActive(boolean active){
		if(active){
			burnTime=Config.PotteryTime.get();
		}else{
			burnTime=-1;
		}
	}
	public void dropInventory(){
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), pottery.getStackInSlot(0));
		int x=(int)xp+Math.random()<(xp-(int)xp)?1:0;
		while(x>0){
			int i=ExperienceOrb.getExperienceValue(x);
			x-=i;
			level.addFreshEntity(new ExperienceOrb(level, (double)worldPosition.getX() + 0.5D, (double)worldPosition.getY() + 0.5D, (double)worldPosition.getZ() + 0.5D, i));
		}
	}
	public void checkValid(){
		boolean valid=true;
		if(Config.RainyPottery.get()&&this.level.isRainingAt(this.worldPosition.relative(Direction.UP))){
			valid=false;
		}else{
			if(isValid)
				return;
		}
		//check structure
		for(Direction facing:Direction.Plane.HORIZONTAL){
			BlockState block=this.level.getBlockState(this.worldPosition.relative(facing));
			if(!block.isFaceSturdy(this.level, worldPosition.relative(facing), facing.getOpposite())){
				valid=false;
				break;
			}
		}
		BlockState block=this.level.getBlockState(this.worldPosition.relative(Direction.UP));
		if(block.getBlock()!=Blocks.FIRE){
			if(block.getBlock().isAir(block, this.level, this.worldPosition.relative(Direction.UP))||
					BaseFireBlock.canBePlacedAt(this.level, this.worldPosition.relative(Direction.UP),Direction.UP)){
				BlockState blockstate1 = BaseFireBlock.getState(this.level, this.worldPosition.relative(Direction.UP));
	            this.level.setBlock(this.worldPosition.relative(Direction.UP), blockstate1, 11);
			}else{
				valid=false;
			}
		}
		if(valid){
			isValid=true;
			invalidTicks=0;
		}else{
			if(invalidTicks<100){
				invalidTicks++;
			}else{
				setActive(false);
				this.level.setBlock(worldPosition, ModBlockRegistry.Kiln.defaultBlockState().setValue(BlockPotteryKiln.TYPE, EnumKilnTypes.WOOD), 2);
				this.level.setBlock(worldPosition.relative(Direction.UP), Blocks.AIR.defaultBlockState(), 2);
				invalidTicks=0;
			}
		}
	}
	
	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.putInt("invalid", invalidTicks);
		compound.putInt("time", burnTime);
		compound.putFloat("xp", xp);
		compound.putBoolean("valid", isValid);
		compound.put("pottery", pottery.serializeNBT());
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
		invalidTicks=nbt.getInt("invalid");
		burnTime=nbt.getInt("time");
		xp=nbt.getFloat("xp");
		isValid=nbt.getBoolean("valid");
		pottery.deserializeNBT(nbt.getCompound("pottery"));
	}
	
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt=super.getUpdateTag();
		nbt.put("pottery", pottery.serializeNBT());
		return nbt;
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(getBlockPos(), 1, pottery.serializeNBT());
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundTag tag) {
		super.handleUpdateTag(state, tag);
		pottery.deserializeNBT(tag.getCompound("pottery"));
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		pottery.deserializeNBT(pkt.getTag());
	}
	
	public class PotteryStackHandler extends ItemStackHandler{
		@Override
		public int getSlotLimit(int slot) {
			return 8;
		}
	}

}
