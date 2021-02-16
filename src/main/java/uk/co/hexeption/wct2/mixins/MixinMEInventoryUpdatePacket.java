package uk.co.hexeption.wct2.mixins;

import java.util.List;

import javax.annotation.Nullable;

import appeng.api.storage.data.IAEItemStack;
import appeng.core.sync.network.INetworkInfo;
import appeng.core.sync.packets.MEInventoryUpdatePacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.hexeption.wct2.container.MEMonitorableScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;

/**
 * MixinMEInventoryUpdatePacket
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 16/02/2021 - 05:05 pm
 */
@Mixin(value = MEInventoryUpdatePacket.class, remap = false)
public class MixinMEInventoryUpdatePacket {

	@Shadow
	@Final
	@Nullable
	private List<IAEItemStack> list;

	@Inject(method = "clientPacketData", at = @At("HEAD"))
	private void clientPacketData(INetworkInfo network, PlayerEntity player, CallbackInfo ci) {
		final Screen gs = Minecraft.getInstance().currentScreen;
		if (gs instanceof MEMonitorableScreen) {
			((MEMonitorableScreen<?>) gs).postUpdate(this.list);
		}

	}

}
