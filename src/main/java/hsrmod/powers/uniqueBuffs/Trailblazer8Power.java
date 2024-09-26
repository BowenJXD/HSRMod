package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Collections;
import java.util.List;

public class Trailblazer8Power extends PowerPower implements OnPlayerLoseBlockSubscriber, DamageModApplyingPower {
    public static final String POWER_ID = HSRMod.makePath(Trailblazer8Power.class.getSimpleName());
    
    int damageIncrement = 0;
    
    public Trailblazer8Power(boolean upgraded) {
        super(POWER_ID, upgraded);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], damageIncrement);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        BaseMod.unsubscribe(this);
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return o instanceof AbstractCard
                && owner.currentBlock > 0
                && damageIncrement > 0
                && list.stream().noneMatch(mod -> mod instanceof Trailblazer8DamageMod);
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new Trailblazer8DamageMod(damageIncrement));
    }

    @Override
    public int receiveOnPlayerLoseBlock(int i) {
        if (!SubscriptionManager.checkSubscriber(this)) return i;
        int playerBlock = AbstractDungeon.player.currentBlock;
        ModHelper.addToTopAbstract(() -> {
            int dmg = playerBlock - AbstractDungeon.player.currentBlock;
            if (dmg >= 0) {
                damageIncrement = dmg;
                this.updateDescription();
            }
            if (dmg > 0) {
                AbstractMonster monster = AbstractDungeon.getRandomMonster();
                if (monster != null) {
                    addToTop(new ElementalDamageAction(monster, new DamageInfo(AbstractDungeon.player, dmg, DamageInfo.DamageType.NORMAL), 
                            ElementType.Physical, 2,
                            AbstractGameAction.AttackEffect.SHIELD));
                }
            }
        });
        return i;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        damageIncrement = 0;
        this.updateDescription();
    }

    public static class Trailblazer8DamageMod extends AbstractDamageModifier {
        int damageIncrement = 0;
        
        public Trailblazer8DamageMod(int damageIncrement) {
            this.damageIncrement = damageIncrement;
        }
        
        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
            if (target != null && target.currentBlock > 0)
                return damage * (1 + damageIncrement / 100.0f);
            return damage;
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new Trailblazer8DamageMod(damageIncrement);
        }
    }
}
