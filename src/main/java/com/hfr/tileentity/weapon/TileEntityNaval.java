package com.hfr.tileentity.weapon;

import com.hfr.entity.projectile.EntityShell;
import com.hfr.items.ModItems;
import com.hfr.packet.PacketDispatcher;
import com.hfr.packet.tile.AuxGaugePacket;
import com.hfr.packet.tile.RailgunRotationPacket;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileEntityNaval extends TileEntity implements ISidedInventory {

	private ItemStack slots[];
	
	private static final int[] slots_top = new int[] { 0 };
	private static final int[] slots_bottom = new int[] { 0 };
	private static final int[] slots_side = new int[] { 0 };
	
	public int powder;
	public static final int maxPowder = 30 * 64 * 4;

	//system time for interpolation
	public long startTime;
	//prev pitch for interpolation
	public float lastPitch;
	//prev yaw for interpolation
	public float lastYaw;

	public static int cooldownDurationMillis = 5000;
	public static int cooldownDurationTicks = 100;
	
	public float pitch;
	public float yaw;
	//delay so the server disables fire buton while turning
	public int delay;
	
	private String customName;
	
	public TileEntityNaval() {
		slots = new ItemStack[3];
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return slots[i];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if(slots[i] != null)
		{
			ItemStack itemStack = slots[i];
			slots[i] = null;
			return itemStack;
		} else {
		return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		slots[i] = itemStack;
		if(itemStack != null && itemStack.stackSize > getInventoryStackLimit())
		{
			itemStack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.naval";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && this.customName.length() > 0;
	}
	
	public void setCustomName(String name) {
		this.customName = name;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		if(worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
		{
			return false;
		}else{
			return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <=64;
		}
	}
	
	//You scrubs aren't needed for anything (right now)
	@Override
	public void openInventory() {}
	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		
		return false;
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(slots[i] != null)
		{
			if(slots[i].stackSize <= j)
			{
				ItemStack itemStack = slots[i];
				slots[i] = null;
				return itemStack;
			}
			ItemStack itemStack1 = slots[i].splitStack(j);
			if (slots[i].stackSize == 0)
			{
				slots[i] = null;
			}
			
			return itemStack1;
		} else {
			return null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("items", 10);

		slots = new ItemStack[getSizeInventory()];
		pitch = nbt.getFloat("pitch");
		yaw = nbt.getFloat("yaw");
		powder = nbt.getInteger("powder");
		
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			byte b0 = nbt1.getByte("slot");
			if(b0 >= 0 && b0 < slots.length)
			{
				slots[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		NBTTagList list = new NBTTagList();
		nbt.setFloat("pitch", pitch);
		nbt.setFloat("yaw", yaw);
		nbt.setInteger("powder", powder);
		
		for(int i = 0; i < slots.length; i++)
		{
			if(slots[i] != null)
			{
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("slot", (byte)i);
				slots[i].writeToNBT(nbt1);
				list.appendTag(nbt1);
			}
		}
		nbt.setTag("items", list);
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_)
    {
        return p_94128_1_ == 0 ? slots_bottom : (p_94128_1_ == 1 ? slots_top : slots_side);
    }

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {
		return this.isItemValidForSlot(i, itemStack);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int j) {
		return false;
	}
	
	public int getPowderScaled(int i) {
		return (powder * i) / maxPowder;
	}

	@Override
	public void updateEntity() {
		
		if(!worldObj.isRemote) {
			
			if(delay > 0) {
				delay--;
			}
			
			for(int i = 0; i < 64; i++) {
				if(slots[0] != null) {
					
					if(slots[0].getItem() == Items.gunpowder && powder < maxPowder) {
						this.decrStackSize(0, 1);
						powder++;
					} else if(slots[0].getItem() == ModItems.drum && powder + 30 * 64 <= maxPowder) {
						this.decrStackSize(0, 1);
						powder += 30 * 64;
					} else {
						break;
					}
					
				} else {
					break;
				}
			}
			
			PacketDispatcher.wrapper.sendToAll(new AuxGaugePacket(xCoord, yCoord, zCoord, powder, 0));
			PacketDispatcher.wrapper.sendToAll(new RailgunRotationPacket(xCoord, yCoord, zCoord, pitch, yaw));
		}
	}
	
	public boolean setAngles(boolean miss) {
		
		if(slots[1] != null &&
				(slots[1].getItem() == ModItems.designator || slots[1].getItem() == ModItems.designator_range || slots[1].getItem() == ModItems.designator_manual) &&
				slots[1].stackTagCompound != null) {

    		int x = slots[1].stackTagCompound.getInteger("xCoord");
    		int z = slots[1].stackTagCompound.getInteger("zCoord");

    		Vec3 vec = Vec3.createVectorHelper(x - xCoord, 0, z - zCoord);
    		Vec3 unit = Vec3.createVectorHelper(1, 0, 0);
    		
    		if(miss) {
    			vec.rotateAroundY((float) (worldObj.rand.nextGaussian() * Math.PI / 45));
    		}
    		
    		if(vec.lengthVector() < 1 || vec.lengthVector() > 4950)
    			return false;
    		
    		double yawUpper = vec.xCoord * unit.xCoord/* + vec.zCoord * unit.zCoord*/; //second side falls away since unit.z is always 0
    		double yawLower = vec.lengthVector()/* * unit.lengthVector()*/; //second side falls away since unit always has length 1
    		float yaw = (float) Math.acos(yawUpper / yawLower);
    		float pitch = (float) (Math.asin((vec.lengthVector() * 2.02031) / (100 * 100)) / 2D);
			
    		float newYaw = (float) (yaw * 180D / Math.PI);
    		float newPitch = (float) (pitch * 180D / Math.PI) - 90F;
    		
    		if(vec.zCoord > 0)
    			newYaw = 0 - (float) (yaw * 180D / Math.PI);
    		
    		if(newYaw != this.yaw || newPitch != this.pitch) {
    			this.yaw = newYaw;
    			this.pitch = newPitch;
    			this.delay = this.cooldownDurationTicks;
    			return true;
    		}
		}
		
		return false;
	}
	
	public boolean canFire() {
		
		return slots[2] != null && (slots[2].getItem() == ModItems.charge_naval || slots[2].getItem() == ModItems.charge_at) && powder >= 30 * 64;
	}
	
	public void tryFire() {
		
		if(canFire()) {
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hfr:block.buttonYes", 1.0F, 1.0F);
			fire();
			this.decrStackSize(2, 1);
			powder -= 30 * 64;
		} else {
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hfr:block.buttonNo", 1.0F, 1.0F);
		}
	}
	
	public void fire() {
		
		Vec3 vec = Vec3.createVectorHelper(6, 0, 0);
		vec.rotateAroundZ((float) (pitch * Math.PI / 180D));
		vec.rotateAroundY((float) (yaw * Math.PI / 180D));

		double fX = xCoord + 0.5 + vec.xCoord;
		double fY = yCoord + 1 + vec.yCoord;
		double fZ = zCoord + 0.5 + vec.zCoord;
		
		vec = vec.normalize();
		double motionX = vec.xCoord * 5D;
		double motionY = vec.yCoord * 5D;
		double motionZ = vec.zCoord * 5D;
		
		EntityShell fart = new EntityShell(worldObj);
		fart.posX = fX;
		fart.posY = fY;
		fart.posZ = fZ;
		fart.motionX = motionX;
		fart.motionY = motionY;
		fart.motionZ = motionZ;
		
		if(slots[2].getItem() == ModItems.charge_naval)
			fart.raiding = true;
		else
			fart.raiding = false;
		
		worldObj.spawnEntityInWorld(fart);
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hfr:block.navalFire", 100.0F, 1.0F);
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 65536.0D;
	}
}
