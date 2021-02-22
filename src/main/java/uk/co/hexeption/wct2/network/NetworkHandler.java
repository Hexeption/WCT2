package uk.co.hexeption.wct2.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import uk.co.hexeption.wct2.WCT2;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

/**
 * NetworkHandler
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 22/02/2021 - 03:18 pm
 */
public class NetworkHandler {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(WCT2.ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private static int index;

	public static void init() {
		registerMessage(MessageOpenGui.class, MessageOpenGui::encode, MessageOpenGui::decode, MessageOpenGuiHandler::handle);
	}

	private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
		INSTANCE.registerMessage(index++, messageType, encoder, decoder, messageConsumer);
	}


}
