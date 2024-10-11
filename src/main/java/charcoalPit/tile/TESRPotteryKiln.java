package charcoalPit.tile;

import com.mojang.blaze3d.vertex.PoseStack;

import charcoalPit.block.BlockPotteryKiln;
import charcoalPit.block.BlockPotteryKiln.EnumKilnTypes;
import charcoalPit.core.ModBlockRegistry;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRPotteryKiln implements BlockEntityRenderer<TilePotteryKiln>{
	private final ItemRenderer itemRenderer;
	public TESRPotteryKiln(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(TilePotteryKiln tile, float partialTicks, PoseStack matrixStack,
			MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		BlockState state=tile.getLevel().getBlockState(tile.getBlockPos());
		if(state.getBlock()==ModBlockRegistry.Kiln&&(state.getValue(BlockPotteryKiln.TYPE)==EnumKilnTypes.EMPTY||state.getValue(BlockPotteryKiln.TYPE)==EnumKilnTypes.COMPLETE)) {
			int i = (int)tile.getBlockPos().asLong();
			matrixStack.pushPose();
	
	        matrixStack.translate(0.5, 0.1, 0.5);
//	        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//			this.itemRenderer = p_173602_.getItemRenderer();
	        ItemStack stack=tile.pottery.getStackInSlot(0);
//	        BakedModel ibakedmodel = itemRenderer.getModel(stack, tile.getLevel(), null,i);
//	        itemRenderer.render(stack,  ItemDisplayContext.GROUND, true, matrixStack, buffer, combinedLight, combinedOverlay, ibakedmodel);
			this.itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, combinedLight, combinedOverlay, matrixStack, buffer, tile.getLevel(), i);
	        matrixStack.popPose();
		}
	}

}
