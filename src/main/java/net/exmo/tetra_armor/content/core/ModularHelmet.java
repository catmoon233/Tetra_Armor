package net.exmo.tetra_armor.content.core;

import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModularHelmet extends ModularArmor{
    public ModularHelmet(Properties properties) {
        super(properties);
    }
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_helmet"
    )
    public static ModularHelmet instance;
}
