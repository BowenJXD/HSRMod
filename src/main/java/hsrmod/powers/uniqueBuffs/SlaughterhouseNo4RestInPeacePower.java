package hsrmod.powers.uniqueBuffs;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;

import java.util.Collections;
import java.util.List;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class SlaughterhouseNo4RestInPeacePower extends PowerPower implements DamageModApplyingPower {
    public static final String POWER_ID = HSRMod.makePath(SlaughterhouseNo4RestInPeacePower.class.getSimpleName());
    
    int percentage;

    public SlaughterhouseNo4RestInPeacePower(boolean upgraded, int percentage) {
        super(POWER_ID, upgraded);
        this.percentage = percentage;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], percentage);
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        boolean result = false;
        if (!(o instanceof AbstractCard)) return result;
        if (list.stream().anyMatch(mod -> mod instanceof SlaughterhouseNo4RestInPeaceModifier)) return result;
        AbstractCard card = (AbstractCard) o;
        if (card.hasTag(FOLLOW_UP)) {
            if (upgraded) result = true;
            else if (card instanceof BaseCard) {
                BaseCard c = (BaseCard) card;
                if (c.followedUp) result = true;
            }
        }
        
        return result;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new SlaughterhouseNo4RestInPeaceModifier(percentage, upgraded));
    }

    public static class SlaughterhouseNo4RestInPeaceModifier extends AbstractDamageModifier{
        int percentage;
        boolean upgraded;
        
        public SlaughterhouseNo4RestInPeaceModifier(int percentage, boolean upgraded) {
            this.percentage = percentage;
            this.upgraded = upgraded;
        }
        
        @Override
        public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
            if (lastDamageTaken > 0
                    && target != AbstractDungeon.player) {
                if (target.hasPower(BrokenPower.POWER_ID)) {
                    if (AbstractDungeon.cardRandomRng.random(100) < percentage) {
                        addToBot(new DrawCardAction(AbstractDungeon.player, 1));
                    }
                }
                else if (upgraded) {
                    if (AbstractDungeon.cardRandomRng.random(100) < (100 - percentage)) {
                        addToBot(new DrawCardAction(AbstractDungeon.player, 1));
                    }
                }
            }
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new SlaughterhouseNo4RestInPeaceModifier(percentage, upgraded);
        }
    } 
}
