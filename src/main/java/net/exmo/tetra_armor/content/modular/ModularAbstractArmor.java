package net.exmo.tetra_armor.content.modular;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.event.ModularItemDamageEvent;
import se.mickelus.tetra.items.InitializableItem;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.ItemProperties;
import se.mickelus.tetra.module.data.SynergyData;
import se.mickelus.tetra.module.data.ToolData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault

public abstract class ModularAbstractArmor extends ArmorItem implements IModularItem, Equipable, InitializableItem {

    protected final ArmorItem.Type type;
    private int defense;
    private float toughness;
    protected float knockbackResistance;
    public static final UUID attackDamageModifier;
    public static final UUID attackSpeedModifier;
    private static final Logger logger;
    private final Cache<String, Multimap<Attribute, AttributeModifier>> attributeCache;
    private final Cache<String, ToolData> toolCache;
    private final Cache<String, EffectData> effectCache;
    private final Cache<String, ItemProperties> propertyCache;
    protected int honeBase;
    protected int honeIntegrityMultiplier;
    protected boolean canHone;
    protected String[] majorModuleKeys;
    protected String[] minorModuleKeys;
    protected String[] requiredModules;
    protected int baseDurability;
    protected int baseIntegrity;
    protected SynergyData[] synergies;
    private Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ModularAbstractArmor(ArmorItem.Type type, Item.Properties properties) {
        super(ArmorMaterials.DIAMOND, type, properties);
        this.attributeCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.toolCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.effectCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.propertyCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.honeBase = 450;
        this.honeIntegrityMultiplier = 200;
        this.canHone = true;
        this.requiredModules = new String[0];
        this.baseDurability = 0;
        this.baseIntegrity = 0;
        this.synergies = new SynergyData[0];
        DataManager.instance.moduleData.onReload(this::clearCaches);
        this.type = type;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);

        this.defaultModifiers = getDefaultAttributeModifiers(this.type.getSlot());
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    public int getDefense(ItemStack itemStack) {
        return (int) this.getAttributeValue(itemStack, Attributes.ARMOR);
    }

    public float getToughness(ItemStack itemStack) {
        return (float) this.getAttributeValue(itemStack, Attributes.ARMOR_TOUGHNESS);
    }

    public float getKnockbackResistance(ItemStack itemStack) {
        return (float) this.getAttributeValue(itemStack, Attributes.KNOCKBACK_RESISTANCE);
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        String id = null;
        switch (this.type) {
            case HELMET -> id = "2AD3F246-FEE1-4E67-B886-69FD380BB150";
            case CHESTPLATE -> id = "9F3D476D-C118-4544-8365-64846904B48E";
            case LEGGINGS -> id = "D8499B04-0E66-4726-AB29-64469D734E0D";
            case BOOTS -> id = "845DB27C-C624-495F-8C9F-6020A9A58B6B";
        }
        if (slot != this.type.getSlot() || id == null) return super.getDefaultAttributeModifiers(slot);
        try{
            return this.attributeCache.get(id, () -> Optional.of(super.getDefaultAttributeModifiers(slot)).orElseGet(ImmutableMultimap::of));
        } catch (ExecutionException var3) {
            var3.printStackTrace();
            return super.getDefaultAttributeModifiers(slot);
        }
    }

    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    public @NotNull SoundEvent getEquipSound() {
        return ArmorMaterials.DIAMOND.getEquipSound();
    }

    public void clearCaches() {
        logger.debug("Clearing item data caches for {}...", this.toString());
        this.attributeCache.invalidateAll();
        this.toolCache.invalidateAll();
        this.effectCache.invalidateAll();
        this.propertyCache.invalidateAll();
    }

    public String[] getMajorModuleKeys(ItemStack itemStack) {
        return this.majorModuleKeys;
    }

    public String[] getMinorModuleKeys(ItemStack itemStack) {
        return this.minorModuleKeys;
    }

    public String[] getRequiredModules(ItemStack itemStack) {
        return this.requiredModules;
    }

    public int getHoneBase(ItemStack itemStack) {
        return this.honeBase;
    }

    public int getHoneIntegrityMultiplier(ItemStack itemStack) {
        return this.honeIntegrityMultiplier;
    }

    public boolean canGainHoneProgress(ItemStack itemStack) {
        return this.canHone;
    }

    public Cache<String, Multimap<Attribute, AttributeModifier>> getAttributeModifierCache() {
        return this.attributeCache;
    }

    public Cache<String, EffectData> getEffectDataCache() {
        return this.effectCache;
    }

    public Cache<String, ItemProperties> getPropertyCache() {
        return this.propertyCache;
    }

    public Cache<String, ToolData> getToolDataCache() {
        return this.toolCache;
    }

    public Item getItem() {
        return this;
    }


    public @NotNull Component getName(ItemStack stack) {
        return Component.literal(this.getItemName(stack));
    }

    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.addAll(this.getTooltip(stack, world, flag));
    }

    public @NotNull Rarity getRarity(@NotNull ItemStack itemStack) {
        return Optional.ofNullable(this.getPropertiesCached(itemStack)).map((props) -> props.rarity).orElse(super.getRarity(itemStack));
    }

    public int getMaxDamage(ItemStack itemStack) {
        return Optional.of(this.getPropertiesCached(itemStack)).map((properties) -> (float)(properties.durability + this.baseDurability) * properties.durabilityMultiplier).map(Math::round).orElse(0);
    }

    public void setDamage(ItemStack itemStack, int damage) {
        super.setDamage(itemStack, Math.min(itemStack.getMaxDamage() - 1, damage));
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        ModularItemDamageEvent event = new ModularItemDamageEvent(entity, stack, amount);
        MinecraftForge.EVENT_BUS.post(event);
        amount = event.getAmount();
        return Math.min(stack.getMaxDamage() - stack.getDamageValue() - 1, amount);
    }

    public int getBarWidth(ItemStack itemStack) {
        return Math.round(13.0F - (float)itemStack.getDamageValue() * 13.0F / (float)this.getMaxDamage(itemStack));
    }

    public int getBarColor(ItemStack itemStack) {
        float maxDamage = (float)this.getMaxDamage(itemStack);
        float f = Math.max(0.0F, (maxDamage - (float)itemStack.getDamageValue()) / maxDamage);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    public void onCraftedBy(ItemStack itemStack, Level world, Player player) {
        IModularItem.updateIdentifier(itemStack);
        this.defaultModifiers = getAttributeModifiersCached(itemStack);
    }

    public boolean isFoil(@Nonnull ItemStack itemStack) {
        return ConfigHandler.enableGlint.get() && super.isFoil(itemStack);
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public SynergyData[] getAllSynergyData(ItemStack itemStack) {
        return this.synergies;
    }

    public boolean isBookEnchantable(ItemStack itemStack, ItemStack bookStack) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack itemStack, Enchantment enchantment) {
        return this.acceptsEnchantment(itemStack, enchantment, true);
    }

    public int getEnchantmentValue(ItemStack itemStack) {
        return this.getEnchantability(itemStack);
    }

    static {
        attackDamageModifier = Item.BASE_ATTACK_DAMAGE_UUID;
        attackSpeedModifier = Item.BASE_ATTACK_SPEED_UUID;
        logger = LogManager.getLogger();
    }

}
