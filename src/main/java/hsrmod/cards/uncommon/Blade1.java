package hsrmod.cards.uncommon;

import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.CleanAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.onlyBuffs.ReinforcePower;
import hsrmod.utils.SubscribeManager;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class Blade1 extends BaseCard {
    public static final String ID = Blade1.class.getSimpleName();
    
    int healthCache = -1;
    
    public Blade1() {
        super(ID);
        tags.add(FOLLOW_UP);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = (int) (AbstractDungeon.player.maxHealth * magicNumber / 100f);
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (p.maxHealth / 2 < p.currentHealth)
            addToBot(new LoseHPAction(p, p, 3));
        else
            addToBot(new HealAction(p, p, 3));
        addToBot(new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage), ElementType.Wind, 1, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)));
    }

    @Override
    public void triggerWhenDrawn() {
        healthCache = AbstractDungeon.player.currentHealth;
    }

    @Override
    public void atTurnStart() {
        healthCache = AbstractDungeon.player.currentHealth;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (healthCache == -1) {
            healthCache = AbstractDungeon.player.currentHealth;
            return;
        }
        if (healthCache != AbstractDungeon.player.currentHealth 
                && !followedUp
                && AbstractDungeon.player.hand.contains(this)
                && c != this) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
