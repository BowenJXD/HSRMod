package hsrmod.cards.base;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.misc.ICanChangeToMulti;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.special.TheWindSoaringValorous;
import hsrmod.utils.ModHelper;
import me.antileaf.signature.utils.SignatureHelper;

public class Trailblazer1 extends BaseCard implements ICanChangeToMulti {
    public static final String ID = Trailblazer1.class.getSimpleName();

    public Trailblazer1() {
        super(ID);
        this.tags.add(CardTags.STRIKE);
        this.tags.add(CardTags.STARTER_STRIKE);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (ModHelper.hasRelic(TheWindSoaringValorous.ID)) changeToMulti();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (isMultiDamage)
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        else
            addToBot(
                    new ElementalDamageAction(
                            m,
                            new ElementalDamageInfo(this),
                            AbstractGameAction.AttackEffect.SLASH_HEAVY,
                            ci -> {
                                if (ci.target.isDying && ci.target instanceof CorruptHeart) {
                                    SignatureHelper.unlock(HSRMod.makePath(ID), true);
                                }
                            }
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