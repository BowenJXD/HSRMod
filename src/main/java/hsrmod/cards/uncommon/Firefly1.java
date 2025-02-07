package hsrmod.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.MultiSlashEffect;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
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
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = false;
        //addToBot(new VFXAction(new VerticalImpactEffect(m.hb.cX, m.hb.cY)));
        addToBot(new VFXAction(new MultiSlashEffect(m.hb.cX, m.hb.cY, 5, Color.CHARTREUSE, Color.ORANGE)));

        if (AbstractDungeon.cardRandomRng.randomBoolean()) {
            ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID + "-1"));
            addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        } else {
            ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID + "-2"));
            addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[1], 1.0F, 2.0F));
        }
        addToBot(
                new ElementalDamageAction(
                        m,
                        new ElementalDamageInfo(this),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                )
        );
        ModHelper.addToBotAbstract(() -> {
            if (m.hasPower(BrokenPower.POWER_ID)) {
                addToBot(new BreakDamageAction(m, new DamageInfo(p, tr), 0.5f));
                returnToHand = true;
                setCostForTurn(costCache);
            }
        });
    }
}
