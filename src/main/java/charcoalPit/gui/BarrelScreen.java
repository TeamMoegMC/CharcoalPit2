package charcoalPit.gui;

import charcoalPit.CharcoalPit;
import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class BarrelScreen extends AbstractContainerScreen<BarrelContainer> {

	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/barrel.png");
	private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};
	public FluidStack fluid;

	public BarrelScreen(BarrelContainer screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
//		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//		RenderSystem.setShaderTexture(0, BARREL_GUI_TEXTURES);
//		this.minecraft.getTextureManager().bindForSetup(BARREL_GUI_TEXTURES);
		int i = this.leftPos;
		int j = this.topPos;
//		guiGraphics.blit(BARREL_GUI_TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);
		renderFluid(guiGraphics,i,j);


//		RenderSystem.enableDepthTest();

	}
	
	public void renderFluid(GuiGraphics guiGraphics, int i, int j) {
		fluid = FluidStack.loadFluidStackFromNBT(this.menu.slots.get(this.menu.slots.size() - 1).getItem().getTag().getCompound("fluid"));
		IClientFluidTypeExtensions iClientFluidTypeExtension = IClientFluidTypeExtensions.of(fluid.getFluid());

		if (fluid.isEmpty())
			return;
		int height = (int) (58 * fluid.getAmount() / 16000D);

//		Minecraft.getInstance().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);

		TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS)
				.getSprite(iClientFluidTypeExtension.getStillTexture(fluid));

		int color = iClientFluidTypeExtension.getTintColor();


//		RenderSystem.setShaderColor((c >> 16 & 255) / 255.0F, (c >> 8 & 255) / 255.0F, (c & 255) / 255.0F, 1F/*(c>>24&255)/255f*/);

		while (height >= 16) {
			innerBlit(guiGraphics.pose().last().pose(), i + 62, i + 62 + 16, j + 72 - height, j + 72 + 16 - height, 0, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),color);
			height-=16;
		}
		if(height>0)
			innerBlit(guiGraphics.pose().last().pose(), i+62, i+62+16, j+72-height, j+72, 0, sprite.getU0(), sprite.getU1(), sprite.getV0(),
					(sprite.getV1()-sprite.getV0())*(height/16F)+sprite.getV0(),color);
//		innerBlit(guiGraphics.pose().last().pose(), i + 62, i + 62 + 16, j + 72 - height, j + 72 + 16 - height, 0, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),c);
		//innerBlit(matrixStack.getLast().getMatrix(), i+62, i+62+16, j+72-height, j+72, this.getBlitOffset(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
//		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		//this.blit(matrixStack, i+62, j+71-height, (int)(sprite.getWidth()*sprite.getMinU()), (int)(sprite.getHeight()*sprite.getMinV()), 16, height);
		/*minecraft.getTextureManager().bindTexture(fluid.getFluid().getAttributes().getStillTexture());
		this.blit(matrixStack, i+62, j+71-height, 0, 0, 16, height);*/
	}
	private static void innerBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV,int color) {
		float r = ((color >> 16) & 255) / 256f;
		float g = ((color >> 8) & 255) / 256f;
		float b = (color & 255) / 256f;
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		RenderSystem.disableDepthTest();

		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
		bufferbuilder.vertex(matrix, (float)x1, (float)y2, (float)blitOffset).color(r, g, b, 1).uv(minU, maxV).endVertex();
		bufferbuilder.vertex(matrix, (float)x2, (float)y2, (float)blitOffset).color(r, g, b, 1).uv(maxU, maxV).endVertex();
		bufferbuilder.vertex(matrix, (float)x2, (float)y1, (float)blitOffset).color(r, g, b, 1).uv(maxU, minV).endVertex();
		bufferbuilder.vertex(matrix, (float)x1, (float)y1, (float)blitOffset).color(r, g, b, 1).uv(minU, minV).endVertex();
		BufferUploader.drawWithShader(bufferbuilder.end());
		RenderSystem.enableDepthTest();
	}


	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
		super.renderLabels(guiGraphics, x, y);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	      this.minecraft.getTextureManager().bindForSetup(BARREL_GUI_TEXTURES);
		guiGraphics.blit(BARREL_GUI_TEXTURES, 62, 14, 176, 47, 16, 71-14);
	    int time=menu.array.get(0);
	    int total=menu.array.get(1);
	    if(total>0&&time>=0) {
	    	int height=(int)(time*14F/total);
	    	guiGraphics.blit(BARREL_GUI_TEXTURES, 97, 36, 176, 2, 18, 14-height);
	    	height=BUBBLELENGTHS[(time)/2%7];
	    	guiGraphics.blit(BARREL_GUI_TEXTURES, 82, 14+29-height, 176, 18+29-height, 12, height);
	    }
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);

		int i = this.leftPos;
		int j = this.topPos;

		guiGraphics.blit(BARREL_GUI_TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);

		List<Component> tooltip = new ArrayList<>();

		this.renderTooltip(guiGraphics, mouseX, mouseY);
		if (isMouseIn(mouseX, mouseY, 62, 14, 15, 57)) {
			Component displayName = fluid.getDisplayName();
			tooltip.add(displayName);
			tooltip.add(applyFormat(Component.literal(fluid.getAmount() + "/" + "16000mB"), ChatFormatting.GOLD));
		}
		if (!tooltip.isEmpty())
			guiGraphics.renderComponentTooltip(font,tooltip, mouseX, mouseY);
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
