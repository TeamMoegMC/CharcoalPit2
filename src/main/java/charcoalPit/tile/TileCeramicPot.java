package charcoalPit.tile;

import charcoalPit.block.BlockCeramicPot;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCeramicPot extends BlockEntity{
	
	public CeramicPotHandler inventory;
	@CapabilityInject(IItemHandler.class)
	public static Capability<IItemHandler> ITEM_CAP=null;
	public LazyOptional<IItemHandler> out;
	
	public TileCeramicPot() {
		super(ModTileRegistry.CeramicPot);
		inventory=new CeramicPotHandler(9,()->{
			setChanged();
		});
		out=LazyOptional.of(()->inventory);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap.equals(ITEM_CAP)) {
			return out.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.put("inventory", inventory.serializeNBT());
		return compound;
	}
	
	@Override
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
		inventory.deserializeNBT(nbt.getCompound("inventory"));
	}
	
	public static class CeramicPotHandler extends ItemStackHandler{
		Runnable function;
		private static final ResourceLocation forbid=new ResourceLocation("immersiveengineering:forbidden_in_crates");
		public CeramicPotHandler(int i,Runnable r) {
			super(i);
			function=r;
		}
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return !(Block.byItem(stack.getItem()) instanceof BlockCeramicPot||Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock||stack.getItem().getTags().contains(forbid));
		}
		@Override
		protected void onContentsChanged(int slot) {
			function.run();
		}
	}

}
