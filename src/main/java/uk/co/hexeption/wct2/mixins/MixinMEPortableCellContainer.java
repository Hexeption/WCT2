package uk.co.hexeption.wct2.mixins;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.implementations.guiobjects.IPortableCell;
import appeng.api.storage.ITerminalHost;
import appeng.container.implementations.MEMonitorableContainer;
import appeng.container.implementations.MEPortableCellContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;

/**
 * MixinMEPortableCellContainer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 22/02/2021 - 05:29 pm
 */
@Mixin(MEPortableCellContainer.class)
public abstract class MixinMEPortableCellContainer extends MEMonitorableContainer {

	@Shadow
	@Final
	private int slot;

	@Shadow
	@Final
	private IPortableCell civ;

	@Shadow
	private int ticks;

	public MixinMEPortableCellContainer(int id, PlayerInventory ip, ITerminalHost monitorable) {
		super(id, ip, monitorable);
	}

	public MixinMEPortableCellContainer(ContainerType<?> containerType, int id, PlayerInventory ip, ITerminalHost monitorable, boolean bindInventory) {
		super(containerType, id, ip, monitorable, bindInventory);
	}

	@Shadow
	protected abstract double getPowerMultiplier();

	/**
	 * @author Make sure that the gui opens when the item is in the {@link top.theillusivec4.curios.Curios} slot
	 */
	@Overwrite
	public void detectAndSendChanges() {
		if (slot < 300) {
			ItemStack currentItem = this.slot < 0 ? this.getPlayerInv().getCurrentItem() : this.getPlayerInv().getStackInSlot(this.slot);
			if (this.civ != null && !currentItem.isEmpty()) {
				if (this.civ != null && !this.civ.getItemStack().isEmpty() && currentItem != this.civ.getItemStack()) {
					if (ItemStack.areItemsEqual(this.civ.getItemStack(), currentItem)) {
						this.getPlayerInv().setInventorySlotContents(this.getPlayerInv().currentItem, this.civ.getItemStack());
					} else {
						this.setValidContainer(false);
					}
				}
			} else {
				this.setValidContainer(false);
			}
		}

		++this.ticks;
		if (this.ticks > 10) {
			this.civ.extractAEPower(this.getPowerMultiplier() * (double) this.ticks, Actionable.MODULATE, PowerMultiplier.CONFIG);
			this.ticks = 0;
		}

		super.detectAndSendChanges();
	}
}
