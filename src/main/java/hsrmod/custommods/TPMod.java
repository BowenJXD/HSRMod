package hsrmod.custommods;

import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.HSRModConfig;

import java.util.function.Consumer;

public class TPMod extends CustomMod {
    Consumer<Boolean> setter;

    public TPMod(String ID, Consumer<Boolean> setter) {
        super(HSRMod.makePath(ID), "r", false);
        this.setter = setter;
    }

    @Override
    public void update(float y) {
        super.update(y);
        if (InputHelper.justClickedLeft) {
            HSRModConfig.getInstance().checkMod(this, setter);
        }
    }
}
