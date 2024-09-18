package hsrmod.misc;

import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import hsrmod.modcore.HSRMod;

public class ChargeIcon extends AbstractCustomIcon {
    public static final String ID = HSRMod.makePath("Charge");
    private static ChargeIcon singleton;

    public ChargeIcon() {
        super(ID, ImageMaster.loadImage("HSRModResources/img/UI/icons/ChargeIcon.png"));
    }

    public static ChargeIcon get()
    {
        if (singleton == null) {
            singleton = new ChargeIcon();
        }
        return singleton;
    }
}
