package hsrmod.relics.starter;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.CustomEnums;

public class WaxOfRemembrance extends WaxRelic {
    public static final String ID = WaxOfRemembrance.class.getSimpleName();

    public WaxOfRemembrance() {
        super(ID, CustomEnums.REMEMBRANCE);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractDungeon.player.masterMaxOrbs += 3;
    }

    @Override
    public void onUnequip() {
        super.onUnequip();
        AbstractDungeon.player.masterMaxOrbs -= 3;
    }
}
