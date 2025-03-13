package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class SecondaryCombustionPower extends StatePower implements PreElementalDamageSubscriber, PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(SecondaryCombustionPower.class.getSimpleName());
    
    AbstractGameEffect effect = null;
    
    public SecondaryCombustionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount, amount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this, true);
        effect = new BorderLongFlashEffect(Color.FIREBRICK);
        addToBot(new VFXAction(effect));
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        addToTop(new RemoveSpecificPowerAction(owner, owner, MoltenCorePower.POWER_ID));
        addToBot(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, 1, !owner.isPlayer)));
        int e = AbstractDungeon.player.energy.energy - EnergyPanel.getCurrentEnergy();
        if (e > 0) addToBot(new GainEnergyAction(e));
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if (!owner.isPlayer && info.type == DamageInfo.DamageType.NORMAL) {
            addToTop(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
        return damageAmount;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (!owner.isPlayer && type == DamageInfo.DamageType.NORMAL) {
            damage += amount;
        }
        return damage;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        if (card.costForTurn > 0 || (card.cost == -1 && card.energyOnUse > 0)) {
            addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (owner.hasPower(SecondaryCombustionPower.POWER_ID)) effect.duration = 1.0F;
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == owner) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this) 
                && action.target == owner
                && action.info.card != null 
                && (action.info.card.costForTurn > 0 || (action.info.card.cost == -1 && action.info.card.energyOnUse > 0))) {
            dmg += amount;
        }
        return dmg;
    }
}
