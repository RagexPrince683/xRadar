package com.hfr.inventory.container;

import com.hfr.tileentity.machine.TileEntityMachineRadar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;

public class ContainerMachineRadar extends Container {
	
	private TileEntityMachineRadar radar;
	
	public ContainerMachineRadar(InventoryPlayer invPlayer, TileEntityMachineRadar tedf) {
		this.radar = tedf;
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting crafting) {
		super.addCraftingToCrafters(crafting);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
