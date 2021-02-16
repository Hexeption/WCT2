package uk.co.hexeption.wct2;

import appeng.api.features.IWirelessTermHandler;
import appeng.core.Api;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.hexeption.wct2.setup.ModItems;
import uk.co.hexeption.wct2.setup.Registration;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

@Mod("wct2")
public class WCT2 {

	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String ID = "wct2";

	public static final ItemGroup ITEM_GROUP = new ItemGroup(ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.WIRELESS_CRAFTING_TERMINAL.get());
		}
	};

	private final Registration registration;

	public WCT2() {

		Registration.register();

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		registration = new Registration();
		modEventBus.addGenericListener(ContainerType.class, registration::registerContainerTypes);
		modEventBus.addListener(this::commonSetup);
	}

	@SubscribeEvent
	public void commonSetup(FMLCommonSetupEvent event) {
		LOGGER.info("RUN");
		Api.instance().registries().wireless().registerWirelessHandler((IWirelessTermHandler) ModItems.WIRELESS_CRAFTING_TERMINAL.get());
		Api.instance().registries().wireless().registerWirelessHandler((IWirelessTermHandler) ModItems.WIRELESS_CRAFTING_TERMINAL_CREATIVE.get());
	}
}
