package charcoalPit.item;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.gui.ClayPotContainer2;
import charcoalPit.recipe.OreKilnRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class ItemClayPot extends Item{
	
	public ItemClayPot() {
		super(new Item.Properties().stacksTo(1));
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			ItemStackHandler inv=new ItemStackHandler();
			inv.deserializeNBT(stack.getTag().getCompound("inventory"));
			if(OreKilnRecipe.oreKilnIsEmpty(inv)) {
				tooltip.add(Component.translatable("info.charcoal_pit.claypot_empty"));
			}else {
				ItemStack out=OreKilnRecipe.OreKilnGetOutput(stack.getTag().getCompound("inventory"), Minecraft.getInstance().level);
				if(out.isEmpty()) {
					tooltip.add(Component.translatable(ChatFormatting.DARK_RED+"info.charcoal_pit.charcoal_pit.claypot_invalid"+" ("+OreKilnRecipe.oreKilnGetOreAmount(inv)+"/8)"));
				}else {
					Component tx=out.getHoverName().plainCopy().append(Component.translatable(" x"+out.getCount()));
					tx.getStyle().applyFormat(ChatFormatting.GREEN);
					tooltip.add(tx);
				}
			}
			int f=OreKilnRecipe.oreKilnGetFuelAvailable(inv);
			int n=OreKilnRecipe.oreKilnGetFuelRequired(inv);
			if(f==0) {
				if(n==0) {
					tooltip.add(Component.translatable("info.charcoal_pit.charcoal_pit.claypot_nofuel"));
				}else {
					tooltip.add(Component.translatable(ChatFormatting.DARK_RED+"No Fuel (0/"+n+")"));
				}
			}else {
				if(f<n) {
					tooltip.add(Component.translatable(ChatFormatting.DARK_RED+"Fuel x"+f+" ("+f+"/"+n+")"));
				}else {
					if(f>n) {
						tooltip.add(Component.translatable(ChatFormatting.YELLOW+"Fuel x"+f+" ("+f+"/"+n+")"));
					}else{
						tooltip.add(Component.translatable(ChatFormatting.GREEN+"Fuel x"+f+" ("+f+"/"+n+")"));
					}
				}
			}
		}
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (!worldIn.isClientSide) {
			if (!playerIn.isShiftKeyDown()) {
				int slot;
				if (handIn == InteractionHand.MAIN_HAND)
					slot = playerIn.getInventory().selected;
				else slot = 40;

				NetworkHooks.openScreen((ServerPlayer) playerIn, new MenuProvider() {

					@Override
					public AbstractContainerMenu createMenu(int arg0, Inventory arg1, Player arg2) {
                        return new ClayPotContainer2(arg0, arg1, slot);
                    }

					@Override
					public Component getDisplayName() {
						return Component.translatable("screen.charcoal_pit.clay_pot");
					}
				}, buf -> buf.writeByte((byte) slot));
			}
			else {
				playerIn.displayClientMessage(Component.translatable("message." + CharcoalPit.MODID + "." + "nofuel"), true);
			}
		}
		return super.use(worldIn, playerIn, handIn);
	}
	
}
