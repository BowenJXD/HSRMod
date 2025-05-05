package androidTestMod.cards.base;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.actions.ElementalDamageAllAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.misc.ICanChangeToMulti;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.relics.special.TheWindSoaringValorous;
import androidTestMod.utils.ModHelper;

public class Danheng0 extends BaseCard implements ICanChangeToMulti {
    public static final String ID = Danheng0.class.getSimpleName();

    public Danheng0() {
        super(ID);
        this.tags.add(CardTags.STRIKE);
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CustomEnums.REVIVE);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (ModHelper.hasRelic(TheWindSoaringValorous.ID)) changeToMulti();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (isMultiDamage)
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        else
            addToBot(
                    new ElementalDamageAction(
                            m,
                            new ElementalDamageInfo(this),
                            AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                    )
            );
    }

    @Override
    public void changeToMulti() {
        this.isMultiDamage = true;
        this.target = CardTarget.ALL_ENEMY;
        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }
}