package uk.co.hexeption.wct2.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import uk.co.hexeption.wct2.WCT2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

/**
 * ClientEvents
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 16/02/2021 - 06:24 pm
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = WCT2.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

	public static KeyBinding OPEN_WIRELESS_CRAFTING_TERMINAL;

	public static void initKeybinds() {
		ClientRegistry.registerKeyBinding(OPEN_WIRELESS_CRAFTING_TERMINAL = new KeyBinding("key.wct2.crafting.open", GLFW.GLFW_KEY_N, "key.wct2.crafting"));
	}


	private static boolean menuDown = false;

	@SubscribeEvent
	public static void handleKeys(TickEvent.ClientTickEvent ev) {
		Minecraft mc = Minecraft.getInstance();

		// TODO: 18/02/2021 Finish
//		if (mc.currentScreen == null) {
//			boolean isKeyDown = OPEN_WIRELESS_CRAFTING_TERMINAL.isKeyDown();
//			if (isKeyDown && !menuDown) {
//				while (OPEN_WIRELESS_CRAFTING_TERMINAL.isPressed()) {
//					if (mc.currentScreen == null) {
//
//						ContainerOpener.openContainer(WirelessCraftingTermContainer.TYPE, mc.player, ContainerLocator.forHand(mc.player, mc.player.getActiveHand()));
//					}
//				}
//			}
//			menuDown = isKeyDown;
//		} else {
//			menuDown = true;
//		}

	}

}
