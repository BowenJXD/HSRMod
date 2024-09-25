package hsrmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;

public class ResonanceTransfer extends BaseCard {
    public static final String ID = ResonanceTransfer.class.getSimpleName();

    public ResonanceTransfer() {
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
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), elementType, 2, AbstractGameAction.AttackEffect.SHIELD));
    }
}
