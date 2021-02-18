package uk.co.hexeption.wct2.item;

import java.util.List;

import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import appeng.container.ContainerLocator;
import appeng.container.ContainerOpener;
import appeng.core.localization.GuiText;
import appeng.items.tools.powered.powersink.AEBasePoweredItem;
import appeng.util.ConfigManager;
import appeng.util.Platform;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.capability.CurioItemCapability;
import uk.co.hexeption.wct2.container.WirelessCraftingTermContainer;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * WirelessCraftTerminal
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 15/02/2021 - 01:47 pm
 */
public class WirelessCraftTerminal extends AEBasePoweredItem implements IWirelessTermHandler {

	public WirelessCraftTerminal(Properties properties) {
		super(() -> 100000, properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!Platform.isClient()) {
			ContainerOpener.openContainer(WirelessCraftingTermContainer.TYPE, playerIn, ContainerLocator.forHand(playerIn, handIn));
		}
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(final ItemStack stack, final World world, final List<ITextComponent> lines,
	                           final ITooltipFlag advancedTooltips) {
		super.addInformation(stack, world, lines, advancedTooltips);

		if (stack.hasTag()) {
			final CompoundNBT tag = stack.getOrCreateTag();
			if (tag != null) {
				final String encKey = tag.getString("encryptionKey");

				if (encKey == null || encKey.isEmpty()) {
					lines.add(GuiText.Unlinked.text());
				} else {
					lines.add(GuiText.Linked.text());
				}
			}
		} else {
			lines.add(new TranslationTextComponent("AppEng.GuiITooltip.Unlinked"));
		}
	}


	@Override
	public boolean canHandle(ItemStack itemStack) {
		return true;
	}

	@Override
	public boolean usePower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean hasPower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
		return true;
	}

	@Override
	public IConfigManager getConfigManager(ItemStack itemStack) {
		final ConfigManager out = new ConfigManager((manager, settingName, newValue) -> {
			final CompoundNBT data = itemStack.getOrCreateTag();
			manager.writeToNBT(data);
		});

		out.registerSetting(Settings.SORT_BY, SortOrder.NAME);
		out.registerSetting(Settings.VIEW_MODE, ViewItems.ALL);
		out.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING);

		out.readFromNBT(itemStack.getOrCreateTag().copy());
		return out;
	}

	@Override
	public String getEncryptionKey(ItemStack itemStack) {
		final CompoundNBT tag = itemStack.getOrCreateTag();
		return tag.getString("encryptionKey");
	}

	@Override
	public void setEncryptionKey(ItemStack itemStack, String s, String s1) {
		final CompoundNBT tag = itemStack.getOrCreateTag();
		tag.putString("encryptionKey", s);
		tag.putString("name", s1);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return CurioItemCapability.createProvider(new ICurio() {
			@Override
			public void curioTick(String identifier, int index, LivingEntity livingEntity) {

			}

		});
	}
}
