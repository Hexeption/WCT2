package uk.co.hexeption.wct2.network;

import java.util.function.Supplier;

import appeng.container.ContainerLocator;
import appeng.container.ContainerOpener;
import appeng.util.Platform;
import net.minecraftforge.fml.network.NetworkEvent;
import uk.co.hexeption.wct2.container.WirelessCraftingTermContainer;

import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * MessageOpenGuiHandler
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 22/02/2021 - 03:31 pm
 */
public class MessageOpenGuiHandler {

	public static void handle(MessageOpenGui msg, Supplier<NetworkEvent.Context> ctx) {
		ServerPlayerEntity player = ctx.get().getSender();
		ctx.get().enqueueWork(() -> {
			if (Platform.isClient()) {
				return;
			}
			ContainerOpener.openContainer(WirelessCraftingTermContainer.TYPE, player, ContainerLocator.forHand(player, player.getActiveHand()));
		});
		ctx.get().setPacketHandled(true);
	}

}
