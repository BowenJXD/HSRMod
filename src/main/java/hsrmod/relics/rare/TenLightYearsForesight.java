package hsrmod.relics.rare;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class TenLightYearsForesight extends BaseRelic {
    public static final String ID = TenLightYearsForesight.class.getSimpleName();

    public TenLightYearsForesight() {
        super(ID);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (c.type == AbstractCard.CardType.ATTACK && m != null) {
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (mo != m && ModHelper.check(mo)) {
                    addToBot(new DamageAction(mo, new DamageInfo(AbstractDungeon.player, 2, DamageInfo.DamageType.THORNS)));
                }
            }
        }
    }
}
