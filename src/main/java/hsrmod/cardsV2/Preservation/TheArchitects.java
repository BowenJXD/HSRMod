package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.QuakePower;

public class TheArchitects extends BaseCard {
    public static final String ID = TheArchitects.class.getSimpleName();

    public TheArchitects() {
        super(ID);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = AbstractDungeon.player.currentBlock;
        if (upgraded) baseDamage += mo.currentBlock;
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int blockLose = p.currentBlock + (upgraded ? m.currentBlock : 0);
        addToBot(new RemoveAllBlockAction(p, p));
        if (p.hasPower(QuakePower.POWER_ID)) {
            AbstractPower quakePower = p.getPower(QuakePower.POWER_ID);
            ((QuakePower)quakePower).attack(m, p.currentBlock);
        }
        if (upgraded) addToBot(new RemoveAllBlockAction(m, p));
        
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SHIELD));
        
        addToBot(new GainBlockAction(p, p, blockLose / 2));
        addToBot(new GainBlockAction(m, p, blockLose / 2));
    }
}
