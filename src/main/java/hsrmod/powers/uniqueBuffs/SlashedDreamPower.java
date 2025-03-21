package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.effects.GrayscaleScreenEffect;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.utils.ModHelper;

public class SlashedDreamPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(SlashedDreamPower.class.getSimpleName());

    boolean canTrigger = false;

    int stackLimit = 12;

    int triggerAmount = 9;

    int baseDamage = 5;

    public SlashedDreamPower(int Amount) {
        super(POWER_ID, Amount);
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], baseDamage, baseDamage);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        canTrigger = true;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (canTrigger && power.type == PowerType.DEBUFF && source == owner) {
            canTrigger = false;
            flash();
            stackPower(1);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            this.amount = stackLimit;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        canTrigger = false;
        if (amount >= triggerAmount) {
            flash();
            trigger();
            reducePower(triggerAmount);
        }
    }

    void trigger() {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Acheron"));
        CardCrawlGame.sound.play("SlashedDream1");
        addToBot(new TalkAction(true, String.format(" #r%s ", DESCRIPTIONS[1]), 1.0F, 2.0F));
        // addToBot(new VFXAction(new GrayscaleScreenEffect(Settings.FAST_MODE ? 3 : 4)));
        
        AbstractCreature target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
        if (target == null) return;
        
        ElementalDamageAction action = new ElementalDamageAction(
                target, 
                new ElementalDamageInfo(owner, baseDamage, ElementType.Lightning, 1), 
                AbstractGameAction.AttackEffect.LIGHTNING
        );
        action.doApplyPower = true;
        addToBot(new BouncingAction(target, 3, action.makeCopy()));

        ElementalDamageAction action2 = new ElementalDamageAction(
                target, 
                new ElementalDamageInfo(owner, baseDamage, ElementType.Lightning, 1), 
                AbstractGameAction.AttackEffect.LIGHTNING, null,
                ci -> ci.info.output += ci.target.powers.stream().mapToInt(p -> p.type == PowerType.DEBUFF ? 1 : 0).sum()
        );
        action2.doApplyPower = true;
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play("SlashedDream2"));
        addToBot(new TalkAction(true, String.format(" #r%s ", DESCRIPTIONS[2]), 1.0F, 2.0F));
        addToBot(new AOEAction(q -> {
            AbstractGameAction a = action2.makeCopy();
            a.target = q;
            return a;
        }));
    }
}
