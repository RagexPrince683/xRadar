package com.hfr.render.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.hfr.main.ResourceManager;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderRift extends TileEntitySpecialRenderer {

	private static final ResourceLocation sky = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation portal = new ResourceLocation("textures/entity/end_portal.png");
	private static final Random random = new Random(31100L);
	FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
		
		World world = tile.getWorldObj();
        GL11.glPushMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y, z + 0.5D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        bindTexture(ResourceManager.rift_tex);
        ResourceManager.rift.renderPart("Frame");
        GL11.glPopMatrix();
        
        float f1 = (float)this.field_147501_a.field_147560_j;
        float f2 = (float)this.field_147501_a.field_147561_k;
        float f3 = (float)this.field_147501_a.field_147558_l;
        GL11.glDisable(GL11.GL_LIGHTING);
        
        random.setSeed(31110L);

		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
		GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
		GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
		GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
		GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
		GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
		GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
		GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		float f100 = world.getTotalWorldTime() % 500L / 500F;
		GL11.glTranslatef(random.nextFloat(), f100, random.nextFloat());

		Tessellator tessellator = Tessellator.instance;

		final int end = 10;
		for (int i = 0; i < end; ++i) {
			float f5 = end - i;
			float f7 = 1.0F / (f5 + 1.0F);

			if (i == 0) {
				this.bindTexture(sky);
				f7 = 0.0F;
				f5 = 65.0F;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			if (i == 1) {
				this.bindTexture(portal);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			}

			GL11.glTranslatef(random.nextFloat() * (1 - f7), random.nextFloat() * (1 - f7), random.nextFloat() * (1 - f7));
			float scale = 0.9F;
            GL11.glScalef(scale, scale, scale);
            float ang = 360 / end;
            GL11.glRotatef(ang * i + ang * random.nextFloat(), 0.0F, 0.0F, 1.0F);

			float f11 = (float) random.nextDouble() * 0.5F + 0.9F;
			float f12 = (float) random.nextDouble() * 0.5F + 0.1F;
			float f13 = (float) random.nextDouble() * 0.5F + 0.9F;
			if (i == 0) {
				f13 = 1.0F;
				f12 = 1.0F;
				f11 = 1.0F;
			}
			f13 *= f7;
			f12 *= f7;
			f11 *= f7;

			GL11.glTexGen(GL11.GL_S, GL11.GL_EYE_PLANE, this.func_147525_a(1, 0, 0, 0));
			GL11.glTexGen(GL11.GL_T, GL11.GL_EYE_PLANE, this.func_147525_a(0, 0, 1, 0));
			GL11.glTexGen(GL11.GL_R, GL11.GL_EYE_PLANE, this.func_147525_a(0, 0, 0, 1));
			GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, this.func_147525_a(0, 1, 0, 0));

			GL11.glRotatef(180, 0, 0, 1);
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_F(f11, f12, f13);
			tessellator.setBrightness(0xF000F0);
			tessellator.addVertex(x + 0.125, y + 0.125, z + 0.875);
			tessellator.addVertex(x + 0.125, y + 0.125, z + 0.125);
			tessellator.addVertex(x + 0.875, y + 0.125, z + 0.125);
			tessellator.addVertex(x + 0.875, y + 0.125, z + 0.875);
			tessellator.draw();

			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_F(f11, f12, f13);
			tessellator.setBrightness(0xF000F0);
			tessellator.addVertex(x + 0.875, y + 0.875, z + 0.875);
			tessellator.addVertex(x + 0.875, y + 0.875, z + 0.125);
			tessellator.addVertex(x + 0.125, y + 0.875, z + 0.125);
			tessellator.addVertex(x + 0.125, y + 0.875, z + 0.875);
			tessellator.draw();

			GL11.glTexGen(GL11.GL_S, GL11.GL_EYE_PLANE, this.func_147525_a(0, 1, 0, 0));
			GL11.glTexGen(GL11.GL_T, GL11.GL_EYE_PLANE, this.func_147525_a(1, 0, 0, 0));
			GL11.glTexGen(GL11.GL_R, GL11.GL_EYE_PLANE, this.func_147525_a(0, 0, 0, 1));
			GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, this.func_147525_a(0, 0, 1, 0));

			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_F(f11, f12, f13);
			tessellator.setBrightness(0xF000F0);
			tessellator.addVertex(x + 0.125, y + 0.125, z + 0.125);
			tessellator.addVertex(x + 0.125, y + 0.875, z + 0.125);
			tessellator.addVertex(x + 0.875, y + 0.875, z + 0.125);
			tessellator.addVertex(x + 0.875, y + 0.125, z + 0.125);

			tessellator.addVertex(x + 0.875, y + 0.125, z + 0.875);
			tessellator.addVertex(x + 0.875, y + 0.875, z + 0.875);
			tessellator.addVertex(x + 0.125, y + 0.875, z + 0.875);
			tessellator.addVertex(x + 0.125, y + 0.125, z + 0.875);
			tessellator.draw();

			GL11.glTexGen(GL11.GL_S, GL11.GL_EYE_PLANE, this.func_147525_a(0, 1, 0, 0));
			GL11.glTexGen(GL11.GL_T, GL11.GL_EYE_PLANE, this.func_147525_a(0, 0, 1, 0));
			GL11.glTexGen(GL11.GL_R, GL11.GL_EYE_PLANE, this.func_147525_a(0, 0, 0, 1));
			GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, this.func_147525_a(1, 0, 0, 0));

			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_F(f11, f12, f13);
			tessellator.setBrightness(0xF000F0);
			tessellator.addVertex(x + 0.125, y + 0.125, z + 0.875);
			tessellator.addVertex(x + 0.125, y + 0.875, z + 0.875);
			tessellator.addVertex(x + 0.125, y + 0.875, z + 0.125);
			tessellator.addVertex(x + 0.125, y + 0.125, z + 0.125);
			tessellator.draw();

			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_F(f11, f12, f13);
			tessellator.setBrightness(0xF000F0);
			tessellator.addVertex(x + 0.875, y + 0.125, z + 0.125);
			tessellator.addVertex(x + 0.875, y + 0.875, z + 0.125);
			tessellator.addVertex(x + 0.875, y + 0.875, z + 0.875);
			tessellator.addVertex(x + 0.875, y + 0.125, z + 0.875);
			tessellator.draw();
		}

		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
		GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();

	}

    private FloatBuffer func_147525_a(float x, float y, float z, float w) {
    	
        this.buffer.clear();
        this.buffer.put(x).put(y).put(z).put(w);
        this.buffer.flip();
        return this.buffer;
    }
}
