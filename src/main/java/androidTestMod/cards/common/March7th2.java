package androidTestMod.cards.common;

import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static androidTestMod.modcore.CustomEnums.FOLLOW_UP;

public class March7th2 extends BaseCard {
    public static final String ID = March7th2.class.getSimpleName();
    
    boolean canBeUsed = false;
    public AbstractCreature priorityTarget;

    public March7th2() {
        super(ID);
        this.tags.add(FOLLOW_UP);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(
                new ElementalDamageAction(
                        m,
                        new ElementalDamageInfo(this),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                )
        );
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && (canBeUsed || followedUp);
    }

    @Override
    public void atTurnStartPreDraw() {
        canBeUsed = true;
        addToTop(new FollowUpAction(this, ModHelper.check(priorityTarget) ? priorityTarget : null));
    }
}
