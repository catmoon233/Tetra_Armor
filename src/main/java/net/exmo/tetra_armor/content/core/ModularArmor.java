package net.exmo.tetra_armor.content.core;

import se.mickelus.tetra.items.TetraItem;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.items.modular.ModularItem;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault

public class ModularArmor extends ItemModularHandheld implements IModularItem {


    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }
    public ModularArmor(Properties properties) {
        super(properties);
    }
}
