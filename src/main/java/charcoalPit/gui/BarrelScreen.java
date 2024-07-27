package charcoalPit.gui;

import charcoalPit.CharcoalPit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Matrix4f;
import net.minecraft.util.text.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

public class BarrelScreen extends AbstractContainerScreen<BarrelContainer> {

	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/barrel.png");
	private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};
	public FluidStack fluid;

	public BarrelScreen(BarrelContainer screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BARREL_GUI_TEXTURES);
		int i = (this.width - this.imageWidth) / 2;
	      int j = (this.height - this.imageHeight) / 2;
	      this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	      renderFluid(matrixStack,i,j);
		
	}
	
	public void renderFluid(PoseStack matrixStack, int i, int j) {
		fluid = FluidStack.loadFluidStackFromNBT(this.menu.slots.get(this.menu.slots.size() - 1).getItem().getTag().getCompound("fluid"));
		if (fluid.isEmpty())
			return;
		int height = (int) (58 * fluid.getAmount() / 16000D);
		Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
		TextureAtlasSprite sprite = this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluid.getFluid().getAttributes().getStillTexture());
		int c = fluid.getFluid().getAttributes().getColor(fluid);
		RenderSystem.color4f((c >> 16 & 255) / 255.0F, (c >> 8 & 255) / 255.0F, (c & 255) / 255.0F, 1F/*(c>>24&255)/255f*/);
		//blit(matrixStack, i+62, j+71-height, this.getBlitOffset(), 16, height+1, sprite);
		while (height >= 16) {
			innerBlit(matrixStack.last().pose(), i + 62, i + 62 + 16, j + 72 - height, j + 72 + 16 - height, this.getBlitOffset(), sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
			height-=16;
		}
		if(height>0)
			innerBlit(matrixStack.last().pose(), i+62, i+62+16, j+72-height, j+72, this.getBlitOffset(), sprite.getU0(), sprite.getU1(), sprite.getV0(), 
				(sprite.getV1()-sprite.getV0())*(height/16F)+sprite.getV0());
		//innerBlit(matrixStack.getLast().getMatrix(), i+62, i+62+16, j+72-height, j+72, this.getBlitOffset(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//this.blit(matrixStack, i+62, j+71-height, (int)(sprite.getWidth()*sprite.getMinU()), (int)(sprite.getHeight()*sprite.getMinV()), 16, height);
		/*minecraft.getTextureManager().bindTexture(fluid.getFluid().getAttributes().getStillTexture());
		this.blit(matrixStack, i+62, j+71-height, 0, 0, 16, height);*/
	}
	
	private static void innerBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
	      BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
	      bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
	      bufferbuilder.vertex(matrix, (float)x1, (float)y2, (float)blitOffset).uv(minU, maxV).endVertex();
	      bufferbuilder.vertex(matrix, (float)x2, (float)y2, (float)blitOffset).uv(maxU, maxV).endVertex();
	      bufferbuilder.vertex(matrix, (float)x2, (float)y1, (float)blitOffset).uv(maxU, minV).endVertex();
	      bufferbuilder.vertex(matrix, (float)x1, (float)y1, (float)blitOffset).uv(minU, minV).endVertex();
	      bufferbuilder.end();
	      RenderSystem.enableAlphaTest();
	      BufferUploader.end(bufferbuilder);
	   }
	
	@Override
	protected void renderLabels(PoseStack matrixStack, int x, int y) {
		super.renderLabels(matrixStack, x, y);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	      this.minecraft.getTextureManager().bind(BARREL_GUI_TEXTURES);
	      //int i = (this.width - this.xSize) / 2;
	      //int j = (this.height - this.ySize) / 2;
	      this.blit(matrixStack, 62, 14, 176, 47, 16, 71-14);
	    int time=menu.array.get(0);
	    int total=menu.array.get(1);
	    if(total>0&&time>=0) {
	    	int height=(int)(time*14F/total);
	    	this.blit(matrixStack, 97, 36, 176, 2, 18, 14-height);
	    	height=BUBBLELENGTHS[(time)/2%7];
	    	this.blit(matrixStack, 82, 14+29-height, 176, 18+29-height, 12, height);
	    }
	}
	
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		List<Component> tooltip = new ArrayList<>();

		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
		if (isMouseIn(mouseX, mouseY, 62, 14, 15, 57)) {
			Component displayName = fluid.getDisplayName();
			tooltip.add(displayName);
			tooltip.add(applyFormat(new TextComponent(fluid.getAmount() + "/" + "16000mB"), ChatFormatting.GOLD));
		}
		if (!tooltip.isEmpty())
			GuiUtils.drawHoveringText(matrixStack, tooltip, mouseX, mouseY, width, height, -1, font);
	}

	public static MutableComponent applyFormat(Component component, ChatFormatting... color) {
		Style style = component.getStyle();
		for (ChatFormatting format : color)
			style = style.applyFormat(format);
		return component.copy().setStyle(style);
	}

	public boolean isMouseIn(int mouseX, int mouseY, int x, int y, int w, int h) {
		return mouseX >= leftPos + x && mouseY >= topPos + y
				&& mouseX < leftPos + x + w && mouseY < topPos + y + h;
	}
}
