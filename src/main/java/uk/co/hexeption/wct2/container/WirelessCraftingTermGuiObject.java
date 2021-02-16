package uk.co.hexeption.wct2.container;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.features.ILocatable;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.implementations.guiobjects.IPortableCell;
import appeng.api.implementations.tiles.ISegmentedInventory;
import appeng.api.implementations.tiles.IViewCellStorage;
import appeng.api.implementations.tiles.IWirelessAccessPoint;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IMachineSet;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.IConfigManager;
import appeng.container.interfaces.IInventorySlotAware;
import appeng.core.Api;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.networking.WirelessTileEntity;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import net.minecraftforge.items.IItemHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * WirelessCraftingTermGuiObject
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 16/02/2021 - 03:03 pm
 */
public class WirelessCraftingTermGuiObject implements IPortableCell, IActionHost, IInventorySlotAware, IAEAppEngInventory, ISegmentedInventory, IViewCellStorage {

	private final ItemStack effectiveItem;
	private final IWirelessTermHandler wth;
	private final String encryptionKey;
	private final PlayerEntity myPlayer;
	private IGrid targetGrid;
	private IStorageGrid sg;
	private IMEMonitor<IAEItemStack> itemStorage;
	private IWirelessAccessPoint myWap;
	private double sqRange = Double.MAX_VALUE;
	private double myRange = Double.MAX_VALUE;
	private final int inventorySlot;

	private final AppEngInternalInventory craftingGrid = new AppEngInternalInventory(this, 9);
	private final AppEngInternalInventory viewCell = new AppEngInternalInventory(this, 5);

	public WirelessCraftingTermGuiObject(final IWirelessTermHandler wh, final ItemStack is, final PlayerEntity ep,
	                                     int inventorySlot) {
		this.encryptionKey = wh.getEncryptionKey(is);
		this.effectiveItem = is;
		this.myPlayer = ep;
		this.wth = wh;
		this.inventorySlot = inventorySlot;

		ILocatable obj = null;

		try {
			final long encKey = Long.parseLong(this.encryptionKey);
			obj = Api.instance().registries().locatable().getLocatableBy(encKey);
		} catch (final NumberFormatException err) {
			// :P
		}

		if (obj instanceof IActionHost) {
			final IGridNode n = ((IActionHost) obj).getActionableNode();
			if (n != null) {
				this.targetGrid = n.getGrid();
				if (this.targetGrid != null) {
					this.sg = this.targetGrid.getCache(IStorageGrid.class);
					if (this.sg != null) {
						this.itemStorage = this.sg
								.getInventory(Api.instance().storage().getStorageChannel(IItemStorageChannel.class));
					}
				}
			}
		}
	}

	public double getRange() {
		return this.myRange;
	}

	@Override
	public <T extends IAEStack<T>> IMEMonitor<T> getInventory(IStorageChannel<T> channel) {
		return this.sg.getInventory(channel);
	}

	@Override
	public void addListener(final IMEMonitorHandlerReceiver<IAEItemStack> l, final Object verificationToken) {
		if (this.itemStorage != null) {
			this.itemStorage.addListener(l, verificationToken);
		}
	}

	@Override
	public void removeListener(final IMEMonitorHandlerReceiver<IAEItemStack> l) {
		if (this.itemStorage != null) {
			this.itemStorage.removeListener(l);
		}
	}

	@Override
	public IItemList<IAEItemStack> getAvailableItems(final IItemList<IAEItemStack> out) {
		if (this.itemStorage != null) {
			return this.itemStorage.getAvailableItems(out);
		}
		return out;
	}

	@Override
	public IItemList<IAEItemStack> getStorageList() {
		if (this.itemStorage != null) {
			return this.itemStorage.getStorageList();
		}
		return null;
	}

	@Override
	public AccessRestriction getAccess() {
		if (this.itemStorage != null) {
			return this.itemStorage.getAccess();
		}
		return AccessRestriction.NO_ACCESS;
	}

	@Override
	public boolean isPrioritized(final IAEItemStack input) {
		if (this.itemStorage != null) {
			return this.itemStorage.isPrioritized(input);
		}
		return false;
	}

	@Override
	public boolean canAccept(final IAEItemStack input) {
		if (this.itemStorage != null) {
			return this.itemStorage.canAccept(input);
		}
		return false;
	}

	@Override
	public int getPriority() {
		if (this.itemStorage != null) {
			return this.itemStorage.getPriority();
		}
		return 0;
	}

	@Override
	public int getSlot() {
		if (this.itemStorage != null) {
			return this.itemStorage.getSlot();
		}
		return 0;
	}

	@Override
	public boolean validForPass(final int i) {
		return this.itemStorage.validForPass(i);
	}

	@Override
	public IAEItemStack injectItems(final IAEItemStack input, final Actionable type, final IActionSource src) {
		if (this.itemStorage != null) {
			return this.itemStorage.injectItems(input, type, src);
		}
		return input;
	}

	@Override
	public IAEItemStack extractItems(final IAEItemStack request, final Actionable mode, final IActionSource src) {
		if (this.itemStorage != null) {
			return this.itemStorage.extractItems(request, mode, src);
		}
		return null;
	}

	@Override
	public IStorageChannel getChannel() {
		if (this.itemStorage != null) {
			return this.itemStorage.getChannel();
		}
		return Api.instance().storage().getStorageChannel(IItemStorageChannel.class);
	}

	@Override
	public double extractAEPower(final double amt, final Actionable mode, final PowerMultiplier usePowerMultiplier) {
		if (this.wth != null && this.effectiveItem != null) {
			if (mode == Actionable.SIMULATE) {
				return this.wth.hasPower(this.myPlayer, amt, this.effectiveItem) ? amt : 0;
			}
			return this.wth.usePower(this.myPlayer, amt, this.effectiveItem) ? amt : 0;
		}
		return 0.0;
	}

	@Override
	public ItemStack getItemStack() {
		return this.effectiveItem;
	}

	@Override
	public IConfigManager getConfigManager() {
		return this.wth.getConfigManager(this.effectiveItem);
	}

	@Override
	public IGridNode getActionableNode() {
		this.rangeCheck();
		if (this.myWap != null) {
			return this.myWap.getActionableNode();
		}
		return null;
	}

	public boolean rangeCheck() {
		this.sqRange = this.myRange = Double.MAX_VALUE;

		if (this.targetGrid != null && this.itemStorage != null) {
			if (this.myWap != null) {
				if (this.myWap.getGrid() == this.targetGrid) {
					if (this.testWap(this.myWap)) {
						return true;
					}
				}
				return false;
			}

			final IMachineSet tw = this.targetGrid.getMachines(WirelessTileEntity.class);

			this.myWap = null;

			for (final IGridNode n : tw) {
				final IWirelessAccessPoint wap = (IWirelessAccessPoint) n.getMachine();
				if (this.testWap(wap)) {
					this.myWap = wap;
				}
			}

			return this.myWap != null;
		}
		return false;
	}

	private boolean testWap(final IWirelessAccessPoint wap) {
		double rangeLimit = wap.getRange();
		rangeLimit *= rangeLimit;

		final DimensionalCoord dc = wap.getLocation();

		if (dc.getWorld() == this.myPlayer.world) {
			final double offX = dc.x - this.myPlayer.getPosX();
			final double offY = dc.y - this.myPlayer.getPosY();
			final double offZ = dc.z - this.myPlayer.getPosZ();

			final double r = offX * offX + offY * offY + offZ * offZ;
			if (r < rangeLimit && this.sqRange > r) {
				if (wap.isActive()) {
					this.sqRange = r;
					this.myRange = Math.sqrt(r);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getInventorySlot() {
		return this.inventorySlot;
	}

	@Override
	public void saveChanges() {

	}

	@Override
	public void onChangeInventory(IItemHandler iItemHandler, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {

	}

	@Override
	public IItemHandler getInventoryByName(String s) {
		if (s.equals("crafting")) {
			return this.craftingGrid;
		}
		return null;
	}


	@Override
	public IItemHandler getViewCellStorage() {
		return viewCell;
	}
}
