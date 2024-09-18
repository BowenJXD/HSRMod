package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class Firefly1 extends BaseCard {
    public static final String ID = Firefly1.class.getSimpleName();
    
    int costCache = -1;
    
    public Firefly1() {
        super(ID);
        costCache = cost;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = ModHelper.getPowerCount(BreakEffectPower.POWER_ID);
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = false;

        if (AbstractDungeon.cardRandomRng.randomBoolean()) {
            ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID + "-1"));
            addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        } else {
            ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID + "-2"));
            addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[1], 1.0F, 2.0F));
        }
        addToBot(
                new ElementalDamageAction(
                        m, new DamageInfo(p, damage),
                        ElementType.Fire, magicNumber,
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                )
        );
        ModHelper.addToBotAbstract(() -> {
            if (m.hasPower(BrokenPower.POWER_ID)) {
                // int val = m.hasPower(ToughnessPower.POWER_ID) ? Math.abs(m.getPower(ToughnessPower.POWER_ID).amount) : 0;
                // addToBot(new BreakDamageAction(m, new DamageInfo(p, val)));
                returnToHand = true;
                setCostForTurn(costCache);
            }
        });
    }
}
