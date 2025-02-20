package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.AOEAction;
import hsrmod.powers.misc.DoTPower;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class PrisonerInDeepConfinement extends BaseRelic {
    public static final String ID = PrisonerInDeepConfinement.class.getSimpleName();

    public PrisonerInDeepConfinement() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        flash();
        AbstractDungeon.player.masterDeck.group.stream()
                .filter(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE))
                .forEach(c -> c.isEthereal = true);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        flash();
        ModHelper.findCards(c -> c.hasTag(AbstractCard.CardTags.STARTER_STRIKE))
                .forEach(r -> r.card.isEthereal = true);
    }
    
    @Override
    public void onExhaust(AbstractCard card) {
        super.onExhaust(card);
        if (card.isEthereal && card.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
            flash();
            // addToBot(new DrawCardAction(1));
            addToBot(new AOEAction((q) -> new ApplyPowerAction(q, AbstractDungeon.player, DoTPower.getRandomDoTPower(q, AbstractDungeon.player, 1), 1)));
        }
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (AbstractDungeon.actionManager.turnHasEnded && info.type == DamageInfo.DamageType.THORNS)
            return 0;
        return super.onAttackedToChangeDamage(info, damageAmount);
    }
}
