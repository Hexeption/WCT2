package uk.co.hexeption.wct2.mixins;

import appeng.api.features.IWirelessTermHandler;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerLocator;
import appeng.container.implementations.ContainerHelper;
import appeng.core.AELog;
import appeng.core.Api;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.co.hexeption.wct2.container.WirelessCraftingTermGuiObject;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * MixinContainerHelper
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 16/02/2021 - 03:53 pm
 */
@Mixin(value = ContainerHelper.class, remap = false)
public class MixinContainerHelper<C extends AEBaseContainer, I> {

	@Shadow
	@Final
	private Class<I> interfaceClass;

	@Inject(method = "getHostFromPlayerInventory", at = @At("HEAD"), cancellable = true)
	private void getHostFromPlayerInventory(PlayerEntity player, ContainerLocator locator, CallbackInfoReturnable<I> cir) {
		ItemStack it = player.inventory.getStackInSlot(locator.getItemIndex());

		if (it.isEmpty()) {
			AELog.debug("Cannot open container for player %s since they no longer hold the item in slot %d", player, locator.hasItemIndex());
			cir.setReturnValue(null);
		}
		if (interfaceClass.isAssignableFrom(WirelessCraftingTermGuiObject.class)) {
			final IWirelessTermHandler wh = Api.instance().registries().wireless().getWirelessTerminalHandler(it);
			if (wh != null) {
				cir.setReturnValue(interfaceClass.cast(new WirelessCraftingTermGuiObject(wh, it, player, locator.getItemIndex())));
			}
		}
	}
}
