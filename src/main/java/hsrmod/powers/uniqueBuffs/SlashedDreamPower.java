package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.utils.ModHelper;

public class SlashedDreamPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(SlashedDreamPower.class.getSimpleName());

    boolean canTrigger = false;

    int stackLimit = 12;

    int triggerAmount = 9;

    int baseDamage = 3;

    boolean upgraded = false;

    public SlashedDreamPower(int Amount, boolean upgraded) {
        super(POWER_ID, Amount, upgraded);
        
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
        if (canTrigger && power.type == PowerType.DEBUFF) {
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
        CardCrawlGame.music.silenceBGMInstantly();
        AbstractDungeon.scene.fadeOutAmbiance();
        
        CardCrawlGame.sound.play("SlashedDream1");
        addToBot(new TalkAction(true, String.format(" #r%s ", DESCRIPTIONS[1]), 1.0F, 2.0F));
        AbstractCreature target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
        if (target == null) return;
        ElementalDamageAction action = new ElementalDamageAction(target, new DamageInfo(owner, baseDamage),
                ElementType.Lightning, 1, AbstractGameAction.AttackEffect.LIGHTNING, null);
        action.doApplyPower = true;
        addToBot(new BouncingAction(target, 3, action.makeCopy()));

        ElementalDamageAction action2 = new ElementalDamageAction(target, new DamageInfo(owner, baseDamage),
                ElementType.Lightning, 1, AbstractGameAction.AttackEffect.LIGHTNING, null,
                c -> c.powers.stream().filter(p -> p.type == PowerType.DEBUFF).mapToInt(p -> 1).sum());
        action2.doApplyPower = true;
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play("SlashedDream2"));
        addToBot(new TalkAction(true, String.format(" #r%s ", DESCRIPTIONS[2]), 1.0F, 2.0F));
        addToBot(new AOEAction(q -> {
            AbstractGameAction a = action2.makeCopy();
            a.target = q;
            return a;
        }));
        ModHelper.addToBotAbstract(() -> {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeInAmbiance();
        });
    }
}
