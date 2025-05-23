package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RelicEventHelper;

public class SilverCoinOfDiscord extends BaseRelic {
    public static final String ID = SilverCoinOfDiscord.class.getSimpleName();

    public SilverCoinOfDiscord() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        flash();
        int amt = AbstractDungeon.player.gold * magicNumber / 100;
        if (amt > 0) {
            RelicEventHelper.gainGold(amt);
        }
    }
}
