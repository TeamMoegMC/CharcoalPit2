package charcoalPit.tile;

import charcoalPit.block.BlockBloom;
import charcoalPit.core.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TileBloom extends BlockEntity{

	public int cooldown;
	
	public TileBloom(BlockPos blockPos, BlockState state) {
		super(null,blockPos,state);
		cooldown=Config.BloomCooldown.get();
	}

	public static void tick(Level level, BlockPos blockPos, BlockState state, TileBloom tile) {
		if(!level.isClientSide) {
			tile.cooldown--;
			if(tile.cooldown==0)
				level.setBlockAndUpdate(tile.worldPosition, level.getBlockState(tile.worldPosition).setValue(BlockBloom.HOT, false));
			if(tile.cooldown%200==0)
				tile.setChanged();
		}
		
	}
	
	@Override
	public void load( CompoundTag nbt) {
		super.load(nbt);
		cooldown=nbt.getInt("cooldown");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt("cooldown", cooldown);
	}
	
	

}
