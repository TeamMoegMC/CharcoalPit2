package charcoalPit.tile;

import charcoalPit.block.BlockPotteryKiln;
import charcoalPit.block.BlockPotteryKiln.EnumKilnTypes;
import charcoalPit.core.Config;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.PotteryKilnRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.items.ItemStackHandler;

public class TilePotteryKiln extends BlockEntity {
	
	public int invalidTicks;
	public int burnTime;
	public float xp;
	public boolean isValid;
	public PotteryStackHandler potteryStackHandler;
	
	public TilePotteryKiln(BlockPos blockPos, BlockState state) {
		super(ModTileRegistry.PotteryKiln,blockPos,state);
		invalidTicks=0;
		burnTime=-1;
		xp=0;
		isValid=false;
		potteryStackHandler =new PotteryStackHandler();
	}

	public static void tick(Level level, BlockPos blockPos, BlockState state, TilePotteryKiln tile) {
		if(!tile.level.isClientSide&&tile.burnTime>-1){
			tile.checkValid();
			if(tile.burnTime>0) {
				tile.burnTime--;
				if(tile.burnTime%500==0)
					tile.setChanged();
			}else{
				if(tile.burnTime==0){
					PotteryKilnRecipe result=PotteryKilnRecipe.getResult(tile.potteryStackHandler.getStackInSlot(0), tile.level);
					if(result!=null) {
						ItemStack out=PotteryKilnRecipe.processClayPot(tile.potteryStackHandler.getStackInSlot(0), level);
						if(out.isEmpty())
							out=new ItemStack(result.output,tile.potteryStackHandler.getStackInSlot(0).getCount());
						tile.xp=result.xp*tile.potteryStackHandler.getStackInSlot(0).getCount();
						tile.potteryStackHandler.setStackInSlot(0, out);
					}
					tile.level.setBlockAndUpdate(tile.worldPosition, ModBlockRegistry.Kiln.defaultBlockState().setValue(BlockPotteryKiln.TYPE, EnumKilnTypes.COMPLETE));
					tile.level.removeBlock(tile.worldPosition.relative(Direction.UP), false);
					tile.burnTime--;
					tile.setChanged();
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
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), potteryStackHandler.getStackInSlot(0));
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
			if(block.isAir()||
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
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt("invalid", invalidTicks);
		compound.putInt("time", burnTime);
		compound.putFloat("xp", xp);
		compound.putBoolean("valid", isValid);
		compound.put("pottery", potteryStackHandler.serializeNBT());
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		invalidTicks=nbt.getInt("invalid");
		burnTime=nbt.getInt("time");
		xp=nbt.getFloat("xp");
		isValid=nbt.getBoolean("valid");
		potteryStackHandler.deserializeNBT(nbt.getCompound("pottery"));
	}
	
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt=super.getUpdateTag();
		nbt.put("pottery", potteryStackHandler.serializeNBT());
		return nbt;
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		potteryStackHandler.deserializeNBT(tag.getCompound("pottery"));
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//		potteryStackHandler.deserializeNBT(pkt.getTag());
		super.onDataPacket(net, pkt);
		this.load(pkt.getTag());
	}
	
	public class PotteryStackHandler extends ItemStackHandler{
		@Override
		public int getSlotLimit(int slot) {
			return 8;
		}
		@Override
		protected void onContentsChanged(int slot) {
			assert level != null;
			setChanged();
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
		}
	}

}
