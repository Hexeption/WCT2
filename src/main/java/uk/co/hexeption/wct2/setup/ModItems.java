package uk.co.hexeption.wct2.setup;

import net.minecraftforge.fml.RegistryObject;
import uk.co.hexeption.wct2.WCT2;
import uk.co.hexeption.wct2.item.WirelessCraftTerminal;

import net.minecraft.item.Item;

/**
 * ModItems
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 15/02/2021 - 01:19 pm
 */
public class ModItems {

	public static final RegistryObject<Item> WIRELESS_CRAFTING_TERMINAL = Registration.ITEMS.register("wireless_crafting_terminal", () -> new WirelessCraftTerminal(new Item.Properties().group(WCT2.ITEM_GROUP).maxStackSize(1).maxDamage(50).setNoRepair()));

	static void register() {
	}

}
