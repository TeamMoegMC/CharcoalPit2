package charcoalPit.tile;

import charcoalPit.block.BlockBloom;
import charcoalPit.core.Config;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TileBloom extends BlockEntity implements TickableBlockEntity{

	public int cooldown;
	
	public TileBloom() {
		super(null);
		cooldown=Config.BloomCooldown.get();
	}

	@Override
	public void tick() {
		if(!level.isClientSide) {
			cooldown--;
			if(cooldown==0)
				level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(BlockBloom.HOT, false));
			if(cooldown%200==0)
				setChanged();
		}
		
	}
	
	@Override
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
		cooldown=nbt.getInt("cooldown");
	}
	
	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.putInt("cooldown", cooldown);
		return compound;
	}
	
	

}
