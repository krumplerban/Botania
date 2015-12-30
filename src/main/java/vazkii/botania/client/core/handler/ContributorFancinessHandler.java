/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 9, 2015, 5:35:26 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.api.subtile.signature.SubTileSignature;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.version.VersionChecker;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.item.material.ItemManaResource;
import net.minecraftforge.fml.common.FMLLog;

public final class ContributorFancinessHandler {

	public volatile static Map<String, ItemStack> flowerMap = null;
	private volatile static boolean startedLoading = false;

	private static boolean phi = true;

	public static void render(RenderPlayerEvent.Specials event) {
		String name = event.entityPlayer.getDisplayName().getUnformattedText();

		if(name.equals("Vazkii") || name.equals("_phi")) {
			if(phi)
				renderPhiFlower(event);
			else renderTwintails(event);
		} else if(name.equals("haighyorkie"))
			renderGoldfish(event);

		firstStart();
		
		name = name.toLowerCase();
		if(event.entityPlayer.isWearing(EnumPlayerModelParts.CAPE) && flowerMap != null && flowerMap.containsKey(name))
			renderFlower(event, flowerMap.get(name));
	}

	public static void firstStart() {
		if(!startedLoading) {
			new ThreadContributorListLoader();
			startedLoading = true;
		}
	}

	public static void load(Properties props) {
		flowerMap = new HashMap();
		for(String key : props.stringPropertyNames()) {
			String value = props.getProperty(key);

			try {
				int i = Integer.parseInt(value);
				if(i < 0 || i >= 16)
					throw new NumberFormatException();
				flowerMap.put(key, new ItemStack(ModBlocks.flower, 1, i));
			} catch(NumberFormatException e) {
				SubTileSignature sig = BotaniaAPI.getSignatureForName(value);
				if(sig != null)
					flowerMap.put(key, ItemBlockSpecialFlower.ofType(value));
			}
		}
	}

	private static void renderTwintails(RenderPlayerEvent event) {
		GlStateManager.pushMatrix();
		TextureAtlasSprite icon = RenderEventHandler.INSTANCE.tailIcon;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		Helper.translateToHeadLevel(event.entityPlayer);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		float t = 0.13F;
		GlStateManager.translate(t, -0.5F, -0.1F);
		if(event.entityPlayer.motionY < 0)
			GlStateManager.rotate((float) event.entityPlayer.motionY * 20F, 1F, 0F, 0F);

		float r = -18F + (float) Math.sin((ClientTickHandler.ticksInGame + event.partialRenderTick) * 0.05F) * 2F;
		GlStateManager.rotate(r, 0F, 0F, 1F);
		float s = 0.9F;
		GlStateManager.scale(s, s, s);
		//ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GlStateManager.rotate(-r, 0F, 0F, 1F);
		GlStateManager.translate(-t, -0F, 0F);
		GlStateManager.scale(-1F, 1F, 1F);
		GlStateManager.translate(t, -0F, 0F);
		GlStateManager.rotate(r, 0F, 0F, 1F);
		//ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GlStateManager.popMatrix();
	}

	private static void renderPhiFlower(RenderPlayerEvent event) {
		GlStateManager.pushMatrix();
		TextureAtlasSprite icon = RenderEventHandler.INSTANCE.phiFlowerIcon;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		Helper.translateToHeadLevel(event.entityPlayer);
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(-0.4F, 0.1F, -0.25F);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		GlStateManager.scale(0.4F, 0.4F, 0.4F);
		GlStateManager.translate(-1.2F, 0.2F, 0.125F);
		GlStateManager.rotate(20F, 1F, 0F, 0F);
		//ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GlStateManager.popMatrix();
	}

	private static void renderGoldfish(RenderPlayerEvent event) {
		GlStateManager.pushMatrix();
		TextureAtlasSprite icon = RenderEventHandler.INSTANCE.goldfishIcon;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		Helper.rotateIfSneaking(event.entityPlayer);
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.translate(-0.75F, 0.5F, 0F);
		GlStateManager.scale(0.4F, 0.4F, 0.4F);
		GlStateManager.translate(1.2F, 0.5F, 0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		//ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GlStateManager.popMatrix();
	}

	private static void renderFlower(RenderPlayerEvent event, ItemStack flower) {
		GlStateManager.pushMatrix();
		Helper.translateToHeadLevel(event.entityPlayer);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		ShaderHelper.useShader(ShaderHelper.gold);
		Minecraft.getMinecraft().getRenderItem().renderItemModelForEntity(flower, event.entityPlayer, ItemCameraTransforms.TransformType.THIRD_PERSON);
		ShaderHelper.releaseShader();
		GlStateManager.popMatrix();

//		float f = icon.getMinU();
//		float f1 = icon.getMaxU();
//		float f2 = icon.getMinV();
//		float f3 = icon.getMaxV();
//		GlStateManager.rotate(180F, 0F, 0F, 1F);
//		GlStateManager.rotate(90F, 0F, 1F, 0F);
//		GlStateManager.scale(0.5F, 0.5F, 0.5F);
//		GlStateManager.translate(-0.5F, 0.7F, 0F);
//
//		ShaderHelper.useShader(ShaderHelper.gold);
//		ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
//		ShaderHelper.releaseShader();
//		GlStateManager.popMatrix();
	}

	public static class ThreadContributorListLoader extends Thread {

		public ThreadContributorListLoader() {
			setName("Botania Contributor Fanciness Thread");
			setDaemon(true);
			start();
		}

		@Override
		public void run() {
			try {
				URL url = new URL("https://raw.githubusercontent.com/Vazkii/Botania/master/contributors.properties");
				Properties props = new Properties();
				props.load(new InputStreamReader(url.openStream()));
				load(props);
			} catch(Exception e) {
				FMLLog.info("[Botania] Could not load contributors list. Either you're offline or github is down. Nothing to worry about, carry on~");
				e.printStackTrace();
			}
			VersionChecker.doneChecking = true;
		}

	}

}
