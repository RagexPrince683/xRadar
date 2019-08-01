package com.hfr.render;

import org.lwjgl.opengl.GL11;

import com.hfr.entity.EntityShell;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTom extends Render {

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f0, float f1) {

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
		
        int i = 0;
        
        if(entity instanceof EntityShell)
        	i = 1;
        
		TomPronter.prontTom(i);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}

}