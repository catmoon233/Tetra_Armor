package net.exmo.tetra_armor.content.modular;

import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModularAbstractHelmet extends ModularAbstractArmor {
    public ModularAbstractHelmet(Properties properties) {
        super(properties);
    }
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_helmet"
    )
    public static ModularAbstractHelmet instance;
}
