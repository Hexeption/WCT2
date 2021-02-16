package uk.co.hexeption.wct2.container;

import appeng.api.implementations.tiles.IViewCellStorage;
import appeng.container.ContainerLocator;
import appeng.container.ContainerNull;
import appeng.container.implementations.ContainerHelper;
import appeng.container.implementations.MEPortableCellContainer;
import appeng.container.slot.CraftingMatrixSlot;
import appeng.container.slot.CraftingTermSlot;
import appeng.container.slot.RestrictedInputSlot;
import appeng.helpers.IContainerCraftingPacket;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import appeng.util.inv.WrapperInvItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;

/**
 * WirelessCraftingTermContainer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 16/02/2021 - 03:01 pm
 */

public class WirelessCraftingTermContainer extends MEPortableCellContainer implements IContainerCraftingPacket, IAEAppEngInventory {

	public static ContainerType<WirelessCraftingTermContainer> TYPE;
	private static final ContainerHelper<WirelessCraftingTermContainer, WirelessCraftingTermGuiObject> helper = new ContainerHelper<>(WirelessCraftingTermContainer::new, WirelessCraftingTermGuiObject.class);
	private final WirelessCraftingTermGuiObject wirelessCraftingTermGuiObject;

	public static WirelessCraftingTermContainer fromNetwork(int windowId, PlayerInventory inv, PacketBuffer buf) {
		return (WirelessCraftingTermContainer) helper.fromNetwork(windowId, inv, buf);
	}

	public static boolean open(PlayerEntity player, ContainerLocator locator) {
		return helper.open(player, locator);
	}

	private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);
	private final CraftingMatrixSlot[] craftingSlots = new CraftingMatrixSlot[9];
	private final CraftingTermSlot outputSlot;
	private IRecipe<CraftingInventory> currentRecipe;

	private RestrictedInputSlot[] cellViews;


	public WirelessCraftingTermContainer(int i, PlayerInventory ip, WirelessCraftingTermGuiObject gui) {
		super(TYPE, i, ip, gui);
		this.wirelessCraftingTermGuiObject = gui;
		this.cellViews = new RestrictedInputSlot[5];

		final IItemHandler crafting = this.wirelessCraftingTermGuiObject.getInventoryByName("crafting");

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				this.addSlot(this.craftingSlots[x + y * 3] = new CraftingMatrixSlot(this, crafting, x + y * 3,
						37 + x * 18, -72 + y * 18));
			}
		}

		this.addSlot(this.outputSlot = new CraftingTermSlot(this.getPlayerInv().player, this.getActionSource(),
				this.getPowerSource(), gui, crafting, crafting, this.output, 131, -72 + 18, this));

		this.bindPlayerInventory(ip, 0, 0);
		for (int y = 0; y < 5; y++) {
			this.cellViews[y] = new RestrictedInputSlot(RestrictedInputSlot.PlacableItemType.VIEW_CELL,
					((IViewCellStorage) gui).getViewCellStorage(), y, 206, y * 18 + 8,
					this.getPlayerInventory());
			this.cellViews[y].setAllowEdit(this.canAccessViewCells);
			this.addSlot(this.cellViews[y]);
		}

		this.onCraftMatrixChanged(new WrapperInvItemHandler(crafting));

	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		final ContainerNull cn = new ContainerNull();
		final CraftingInventory ic = new CraftingInventory(cn, 3, 3);

		for (int x = 0; x < 9; x++) {
			ic.setInventorySlotContents(x, this.craftingSlots[x].getStack());
		}

		if (this.currentRecipe == null || !this.currentRecipe.matches(ic, this.getPlayerInv().player.world)) {
			World world = this.getPlayerInv().player.world;
			this.currentRecipe = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, ic, world).orElse(null);
		}

		if (this.currentRecipe == null) {
			this.outputSlot.putStack(ItemStack.EMPTY);
		} else {
			final ItemStack craftingResult = this.currentRecipe.getCraftingResult(ic);

			this.outputSlot.putStack(craftingResult);
		}
	}

	@Override
	public IItemHandler getInventoryByName(String s) {
		if (s.equals("player")) {
			return new PlayerInvWrapper(this.getPlayerInventory());
		}
		return this.wirelessCraftingTermGuiObject.getInventoryByName(s);
	}

	@Override
	public boolean useRealItems() {
		return true;
	}

	@Override
	public void saveChanges() {

	}

	@Override
	public void onChangeInventory(IItemHandler iItemHandler, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {

	}

	@Override
	public ItemStack[] getViewCells() {
		ItemStack[] list = new ItemStack[this.cellViews.length];

		for (int x = 0; x < this.cellViews.length; ++x) {
			list[x] = this.cellViews[x].getStack();
		}

		return list;
	}



}
