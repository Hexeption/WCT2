package uk.co.hexeption.wct2.mixins;

import appeng.container.ContainerLocator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import top.theillusivec4.curios.api.CuriosApi;
import uk.co.hexeption.wct2.item.WirelessCraftTerminal;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/**
 * MixinContainerLocator
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 22/02/2021 - 01:28 pm
 */
@Mixin(value = ContainerLocator.class, remap = false)
public abstract class MixinContainerLocator {

	/**
	 * @author
	 */
	@Overwrite
	private static int getPlayerInventorySlotFromHand(PlayerEntity player, Hand hand) {
		ItemStack is = player.getHeldItem(hand);
		if (is.isEmpty()) {
			ItemStack term = CuriosApi.getCuriosHelper().findEquippedCurio(stack -> stack.getItem() instanceof WirelessCraftTerminal, player)
					.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);

			if (!term.isEmpty()) {
				return 300;
			}

			throw new IllegalArgumentException("Cannot open an item-inventory with empty hands");
		} else {
			int invSize = player.inventory.getSizeInventory();

			for (int i = 0; i < invSize; ++i) {
				if (player.inventory.getStackInSlot(i) == is) {
					return i;
				}
			}

			throw new IllegalArgumentException("Could not find item held in hand " + hand + " in player inventory");
		}
	}
}
