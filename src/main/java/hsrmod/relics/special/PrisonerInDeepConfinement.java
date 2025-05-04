package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.powers.misc.DoTPower;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class PrisonerInDeepConfinement extends BaseRelic {
    public static final String ID = PrisonerInDeepConfinement.class.getSimpleName();

    public PrisonerInDeepConfinement() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        flash();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                c.isEthereal = true;
            }
        }
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        flash();
        for (ModHelper.FindResult r : ModHelper.findCards(new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return c.hasTag(AbstractCard.CardTags.STARTER_STRIKE);
            }
        })) {
            r.card.isEthereal = true;
        }
    }
    
    @Override
    public void onExhaust(AbstractCard card) {
        super.onExhaust(card);
        if (card.isEthereal && card.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
            flash();
            // addToBot(new DrawCardAction(1));
            addToBot(new AOEAction(new Function<AbstractMonster, AbstractGameAction>() {
                @Override
                public AbstractGameAction apply(AbstractMonster q) {
                    return new ApplyPowerAction(q, AbstractDungeon.player, DoTPower.getRandomDoTPower(q, AbstractDungeon.player, 1), 1);
                }
            }));
        }
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (AbstractDungeon.actionManager.turnHasEnded && info.type == DamageInfo.DamageType.THORNS)
            return 0;
        return super.onAttackedToChangeDamage(info, damageAmount);
    }
}
