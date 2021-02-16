package uk.co.hexeption.wct2.container.screen;

import appeng.api.config.ActionItems;
import appeng.client.gui.widgets.ActionButton;
import appeng.container.implementations.MEPortableCellContainer;
import appeng.container.slot.CraftingMatrixSlot;
import appeng.core.localization.GuiText;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.InventoryActionPacket;
import appeng.helpers.InventoryAction;
import com.mojang.blaze3d.matrix.MatrixStack;
import uk.co.hexeption.wct2.container.MEMonitorableScreen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.ITextComponent;

/**
 * WirelessCraftingTermScreen
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 16/02/2021 - 03:40 pm
 */
public class WirelessCraftingTermScreen extends MEMonitorableScreen<MEPortableCellContainer> {
	public WirelessCraftingTermScreen(MEPortableCellContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.setReservedSpace(73);
	}

	private void clear() {
		Slot s = null;
		for (final Object j : this.container.inventorySlots) {
			if (j instanceof CraftingMatrixSlot) {
				s = (Slot) j;
			}
		}

		if (s != null) {
			final InventoryActionPacket p = new InventoryActionPacket(InventoryAction.MOVE_REGION, s.slotNumber, 0);
			NetworkHandler.instance().sendToServer(p);
		}
	}

	@Override
	public void init() {
		super.init();
		ActionButton clearBtn = this.addButton(
				new ActionButton(this.guiLeft + 92, this.guiTop + this.ySize - 156, ActionItems.STASH, btn -> clear()));
		clearBtn.setHalfSize(true);
	}

	@Override
	public void drawFG(MatrixStack matrixStack, final int offsetX, final int offsetY, final int mouseX,
	                   final int mouseY) {
		super.drawFG(matrixStack, offsetX, offsetY, mouseX, mouseY);
		this.font.drawString(matrixStack, GuiText.CraftingTerminal.getLocal(), 8,
				this.ySize - 96 + 1 - this.getReservedSpace(), 4210752);
	}

	@Override
	protected String getBackground() {
		return "guis/crafting.png";
	}

}
