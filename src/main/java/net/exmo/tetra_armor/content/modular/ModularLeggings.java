package net.exmo.tetra_armor.content.modular;

import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModularLeggings extends ModularAbstractArmor {
    public ModularLeggings(Properties properties) {
        super(properties);
    }
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_leggings"
    )
    public static ModularLeggings instance;
}
