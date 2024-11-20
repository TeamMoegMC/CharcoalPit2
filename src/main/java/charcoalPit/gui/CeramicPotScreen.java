package charcoalPit.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CeramicPotScreen extends AbstractContainerScreen<CeramicPotContainer>{
	
	private static final ResourceLocation DISPENSER_GUI_TEXTURES = new ResourceLocation("textures/gui/container/dispenser.png");
	
	public CeramicPotScreen(CeramicPotContainer screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//	      this.minecraft.getTextureManager().bindForSetup(DISPENSER_GUI_TEXTURES);
	      int i = (this.width - this.imageWidth) / 2;
	      int j = (this.height - this.imageHeight) / 2;
	      guiGraphics.blit(DISPENSER_GUI_TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);
		
	}
	
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
	      this.renderBackground(guiGraphics);
	      super.render(guiGraphics, mouseX, mouseY, partialTicks);
	      this.renderTooltip(guiGraphics, mouseX, mouseY);
	   }

}
