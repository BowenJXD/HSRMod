package androidTestMod.relics.rare;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashSet;

public class TenLightYearsForesight extends BaseRelic {
    public static final String ID = TenLightYearsForesight.class.getSimpleName();
    
    HashSet<AbstractCreature> targetSet;
    int dmg = 10;

    public TenLightYearsForesight() {
        super(ID);
        targetSet = new HashSet<>();
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        setCounter(0);
        targetSet.clear();
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        setCounter(0);
        targetSet.clear();
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (c.type == AbstractCard.CardType.ATTACK) {
            if (m != null)
                targetSet.add(m);
            else if (c.target == AbstractCard.CardTarget.ALL_ENEMY || c.target == AbstractCard.CardTarget.ALL)
                targetSet.addAll(AbstractDungeon.getMonsters().monsters);
            setCounter(counter+1);
            if (counter > magicNumber) {
                setCounter(0);
                flash();
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (ModHelper.check(mo) && !targetSet.contains(mo)) {
                        addToBot(new DamageAction(mo, new DamageInfo(AbstractDungeon.player, dmg, DamageInfo.DamageType.THORNS)));
                    }
                }
                targetSet.clear();
            }
        }
    }
}
