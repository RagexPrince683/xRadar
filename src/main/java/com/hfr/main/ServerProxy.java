package com.hfr.main;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ServerProxy
{
	public void registerRenderInfo() { }
	
	public void registerTileEntitySpecialRenderer() { }
	
	public void howDoIUseTheZOMG(World world, double posX, double posY, double posZ, int type) { }

	public void addBlip(float x, float y, float z, float posX, float posZ, int type) { }

	public void clearBlips(boolean sufficient, boolean enabled, int offset, int range) { }
	
	public boolean isPressed(int id) { return false; }

	public static final int SFX_RAILGUN = 0;
	public static final int SFX_BORDER = 1;
	public void spawnSFX(World world, double posX, double posY, double posZ, int type, Object payload) { }
	
	public void spawnEFX(double posX, double posY, double posZ, float pow) { }

	public void updateFlag(ResourceLocation flag, ResourceLocation overlay, int color, String name) { }
}