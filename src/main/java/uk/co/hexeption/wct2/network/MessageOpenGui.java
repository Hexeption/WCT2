package uk.co.hexeption.wct2.network;

import net.minecraft.network.PacketBuffer;

/**
 * MessageOpenGui
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 22/02/2021 - 03:31 pm
 */
public class MessageOpenGui {

	public MessageOpenGui() {

	}

	public static void encode(MessageOpenGui msg, PacketBuffer buf) {
	}

	public static MessageOpenGui decode(PacketBuffer buf) {
		return new MessageOpenGui();
	}

}
