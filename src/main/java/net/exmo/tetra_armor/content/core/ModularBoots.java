package net.exmo.tetra_armor.content.core;

import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModularBoots extends ModularArmor{
    public ModularBoots(Properties properties) {
        super(properties);
    }
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_boots"
    )
    public static ModularBoots instance;
}
