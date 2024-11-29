package net.exmo.tetra_armor.content.modular;

import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModularAbstractBoots extends ModularAbstractArmor {
    public ModularAbstractBoots(Properties properties) {
        super(properties);
    }
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_boots"
    )
    public static ModularAbstractBoots instance;
}
