package com.hfr.blocks.props;

import com.hfr.blocks.BlockDummyable;
import com.hfr.blocks.ModBlocks;
import com.hfr.handler.MultiblockHandler;
import com.hfr.lib.RefStrings;
import com.hfr.main.ResourceManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PropWall extends BlockDummyable {

	public PropWall(Material mat) {
		super(mat);
        this.isBlockContainer = false;
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon iconModel;

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {

		/*if(meta >= ForgeDirection.UNKNOWN.ordinal())
			return new TileEntityProp();*/
		
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {

		if(this == ModBlocks.stone_wall) {
			this.iconModel = iconRegister.registerIcon(RefStrings.MODID + ":stone_wall");
			this.blockIcon = iconRegister.registerIcon(RefStrings.MODID + ":stone_wall_icon");
		}
		if(this == ModBlocks.brick_wall) {
			this.iconModel = iconRegister.registerIcon(RefStrings.MODID + ":brick_wall");
			this.blockIcon = iconRegister.registerIcon(RefStrings.MODID + ":brick_wall_icon");
		}
		if(this == ModBlocks.great_wall) {
			this.iconModel = iconRegister.registerIcon(RefStrings.MODID + ":great_wall");
			this.blockIcon = iconRegister.registerIcon(RefStrings.MODID + ":great_wall_icon");
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		
		if(side == 0)
			return iconModel;
		
		return blockIcon;
	}

	@Override
	public int[] getDimensions() {
		return MultiblockHandler.wall;
	}

	@Override
	public int getOffset() {
		return 0;
	}
	
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	
        float f = 0.0625F;
        
    	if(world.getBlockMetadata(x, y, z) == ForgeDirection.UP.ordinal() && world.getBlock(x, y + 1, z) != this) {
    		return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + f * 10F, z + 1);
    	} else {
    		return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    	}
    }
	
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {

        float f = 0.0625F;
        
    	if(world.getBlockMetadata(x, y, z) == ForgeDirection.UP.ordinal() && world.getBlock(x, y + 1, z) != this) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f * 10F, 1.0F);
    	} else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    	}
    }
	
	@Override
	public int getRenderType(){
		return ResourceManager.id_wall;
	}

}
