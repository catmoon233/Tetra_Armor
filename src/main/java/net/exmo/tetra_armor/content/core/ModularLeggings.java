package net.exmo.tetra_armor.content.core;

import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModularLeggings extends ModularArmor{
    public ModularLeggings(Properties properties) {
        super(properties);
    }
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_leggings"
    )
    public static ModularLeggings instance;
}
