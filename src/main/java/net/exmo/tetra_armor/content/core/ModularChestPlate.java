package net.exmo.tetra_armor.content.core;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.impl.shield.ApplyBannerSchematic;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModularChestPlate extends ModularArmor{
    public static final String plateKey = "chestplate/plate";
    public static final String shoulderKey = "chestplate/shoulder_pads";
    public static final String armGuardsKey = "chestplate/arm_guards";
    public static final String abdominalPlateKey = "chestplate/abdominal_plate";
    public static final String backPlateKey = "chestplate/back_plate";
    public static final String pauldronsKey = "chestplate/pauldrons";
    //public static final String beltKey = "chestplate/belt";
    public static final String identifier = "modular_chestplate";

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(2, -4, 1, 23, -11, 23, -12, -4);
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(-24, 12, 15, 12);

    public ModularChestPlate(Properties properties) {
        super((new Item.Properties()).stacksTo(1).fireResistant());
        this.majorModuleKeys = new String[]{plateKey, abdominalPlateKey,backPlateKey};
        this.minorModuleKeys = new String[]{armGuardsKey,shoulderKey,pauldronsKey};
        this.requiredModules = new String[]{plateKey, abdominalPlateKey,backPlateKey,shoulderKey};
        this.updateConfig((Integer) ConfigHandler.honeShieldBase.get(), (Integer)ConfigHandler.honeShieldIntegrityMultiplier.get());
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
        //SchematicRegistry.instance.registerSchematic(new ApplyBannerSchematic());
       // DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_chestplate"
    )
    public static ModularChestPlate instance;





    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }
}
