package hsrmod.cards.rare;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BreakDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class CourtOfHomogeneity extends BaseCard {
    public static final String ID = CourtOfHomogeneity.class.getSimpleName();
    
    public CourtOfHomogeneity() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int dmg = ModHelper.getPowerCount(AbstractDungeon.player, ToughnessPower.POWER_ID) - ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        if (ModHelper.getPowerCount(p, ToughnessPower.POWER_ID) > ModHelper.getPowerCount(m, ToughnessPower.POWER_ID)) {
            addToBot(new BreakDamageAction(m, new DamageInfo(p, dmg)));
        }
    }
}
