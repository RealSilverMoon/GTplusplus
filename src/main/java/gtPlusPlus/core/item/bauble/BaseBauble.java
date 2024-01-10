package gtPlusPlus.core.item.bauble;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ModularArmourUtils.BT;
import gtPlusPlus.core.util.minecraft.NBTUtils;

@Optional.InterfaceList(
        value = { @Optional.Interface(iface = "baubles.api.IBauble", modid = Mods.Names.BAUBLES),
                @Optional.Interface(iface = "baubles.api.BaubleType", modid = Mods.Names.BAUBLES) })
public class BaseBauble extends Item implements IBauble {

    /**
     * Implementation suggestions taken from Botania.
     */
    private BaubleType mThisBauble;

    private List<String> damageNegations = new ArrayList<>();
    Multimap<String, AttributeModifier> attributes = HashMultimap.create();

    public BaseBauble(BaubleType type, String displayName) {
        this.mThisBauble = type;
        Utils.registerEvent(this);
        this.setMaxStackSize(1);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(Utils.sanitizeString(displayName.toLowerCase()));
        GT_LanguageManager.addStringLocalization("gtplusplus." + getUnlocalizedName() + ".name", displayName);
        GameRegistry.registerItem(this, getUnlocalizedName());
    }

    public BaseBauble(BaubleType type, String unlocalName, int register) {
        this.mThisBauble = type;
        Utils.registerEvent(this);
        this.setMaxStackSize(1);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {
        String key = "gtplusplus." + getUnlocalizedName() + ".name";
        if (key.equals(GT_LanguageManager.getTranslation(key))) {
            return super.getItemStackDisplayName(tItem).replaceAll(".name", "");
        }
        return GT_LanguageManager.getTranslation(key);
    }

    @SubscribeEvent
    public void onPlayerAttacked(LivingAttackEvent event) {
        if (event.entityLiving instanceof EntityPlayer player) {
            if (getCorrectBauble(player) != null && damageNegations.contains(event.source.damageType))
                event.setCanceled(true);
        }
    }

    public boolean addDamageNegation(DamageSource damageSource) {
        return addDamageNegation(damageSource, null);
    }

    public boolean addDamageNegation(DamageSource damageSource, ItemStack aStack) {
        return damageNegations.add(damageSource.damageType);
    }

    public void clearDamageNegation() {
        damageNegations.clear();
    }

    @Override
    public boolean canEquip(ItemStack arg0, EntityLivingBase arg1) {
        return EntityPlayer.class.isInstance(arg1) ? true : false;
    }

    @Override
    public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1) {
        return EntityPlayer.class.isInstance(arg1) ? true : false;
    }

    @Override
    public BaubleType getBaubleType(ItemStack arg0) {
        return mThisBauble;
    }

    public boolean SetBaubleType(BT arg0) {
        return SetBaubleType(arg0.getType());
    }

    public boolean SetBaubleType(BaubleType arg0) {
        BaubleType temp = this.mThisBauble;
        this.mThisBauble = arg0;
        if (this.mThisBauble != temp) {
            return true;
        }
        return false;
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {
        if (entity != null && EntityPlayer.class.isInstance(entity)) {
            onEquippedOrLoadedIntoWorld(stack, entity);
            setPlayerHashcode(stack, entity.hashCode());
        }
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        if (getPlayerHashcode(stack) != player.hashCode()) {
            onEquippedOrLoadedIntoWorld(stack, player);
            setPlayerHashcode(stack, player.hashCode());
        }
    }

    public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
        attributes.clear();
        fillModifiers(attributes, stack);
        player.getAttributeMap().applyAttributeModifiers(attributes);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {
        attributes.clear();
        fillModifiers(attributes, stack);
        player.getAttributeMap().removeAttributeModifiers(attributes);
    }

    void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {}

    public ItemStack getCorrectBauble(EntityPlayer player) {
        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
        ItemStack stack1 = baubles.getStackInSlot(1);
        ItemStack stack2 = baubles.getStackInSlot(2);
        return isCorrectBauble(stack1) ? stack1 : isCorrectBauble(stack2) ? stack2 : null;
    }

    private boolean isCorrectBauble(ItemStack stack) {
        return stack != null && (stack.getItem() == this);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    public static UUID getBaubleUUID(ItemStack stack) {
        long most = NBTUtils.getLong(stack, "baubleUUIDMost");
        if (most == 0) {
            UUID uuid = UUID.randomUUID();
            NBTUtils.setLong(stack, "baubleUUIDMost", uuid.getMostSignificantBits());
            NBTUtils.setLong(stack, "baubleUUIDLeast", uuid.getLeastSignificantBits());
            return getBaubleUUID(stack);
        }

        long least = NBTUtils.getLong(stack, "baubleUUIDLeast");
        return new UUID(most, least);
    }

    public static int getPlayerHashcode(ItemStack stack) {
        return NBTUtils.getInteger(stack, "mPlayerHashcode");
    }

    public static void setPlayerHashcode(ItemStack stack, int hash) {
        NBTUtils.setInteger(stack, "mPlayerHashcode", hash);
    }
}
