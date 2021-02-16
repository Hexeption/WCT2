package uk.co.hexeption.wct2.setup;

import appeng.container.AEBaseContainer;
import appeng.container.ContainerOpener;
import appeng.core.AppEng;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import uk.co.hexeption.wct2.WCT2;
import uk.co.hexeption.wct2.container.WirelessCraftingTermContainer;
import uk.co.hexeption.wct2.container.screen.WirelessCraftingTermScreen;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;

/**
 * Registration
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 15/02/2021 - 01:18 pm
 */
public class Registration {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WCT2.ID);

	public static void register() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ITEMS.register(modEventBus);

		ModItems.register();

	}

	public void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> event) {
		final IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

		WirelessCraftingTermContainer.TYPE = registerContainer(registry, "wireless_crafting_term", WirelessCraftingTermContainer::fromNetwork, WirelessCraftingTermContainer::open);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			ScreenManager.registerFactory(WirelessCraftingTermContainer.TYPE, WirelessCraftingTermScreen::new);
		});

	}

	private <T extends AEBaseContainer> ContainerType<T> registerContainer(IForgeRegistry<ContainerType<?>> registry,
	                                                                       String id, IContainerFactory<T> factory, ContainerOpener.Opener<T> opener) {
		ContainerType<T> type = IForgeContainerType.create(factory);
		type.setRegistryName(AppEng.MOD_ID, id);
		registry.register(type);
		ContainerOpener.addOpener(type, opener);
		return type;
	}

}
