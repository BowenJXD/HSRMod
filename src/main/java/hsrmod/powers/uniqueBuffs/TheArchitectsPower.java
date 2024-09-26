package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PostBreakBlockSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class TheArchitectsPower extends PowerPower implements PostBreakBlockSubscriber {
    public static final String POWER_ID = HSRMod.makePath(TheArchitectsPower.class.getSimpleName());

    public int damage = 4;
    public int block = 4;
    public int increment = 2;
    
    public TheArchitectsPower(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (upgraded)
            this.description = String.format(DESCRIPTIONS[1], block, damage, increment);
        else
            this.description = String.format(DESCRIPTIONS[0], block, damage, increment);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        if (!upgraded) SubscriptionManager.getInstance().subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.getInstance().unsubscribe(this);
    }

    /*@Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        AbstractCreature m = action.target;
        if (card.type == AbstractCard.CardType.SKILL) {
            if (m != null && m.currentBlock > 0) {
                m = null;
            }
            if (m == null) {
                m = ModHelper.getRandomMonster((mo) -> mo.currentBlock <= 0, true);
            }
            if (m != null) {
                this.flash();
                this.addToBot(new GainBlockAction(m, block));
            }
        }
    }*/

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (upgraded 
                && target.currentBlock > 0) {
            this.flash();
            attack(target);
        }
    }

    @Override
    public void postBreakBlock(AbstractCreature c) {
        if (SubscriptionManager.checkSubscriber(this) && c != owner && !upgraded) {
            this.flash();
            attack(c);
        }
    }
    
    void attack(AbstractCreature c) {
        int dmg = damage;
        dmg += AbstractDungeon.player.currentBlock / 10 * increment;
        this.addToTop(new DamageAction(c, new DamageInfo(AbstractDungeon.player, dmg, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SHIELD));
    }
}
