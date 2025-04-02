package hsrmod.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.MultiSlashEffect;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.utils.CachedCondition;
import hsrmod.utils.ModHelper;
import hsrmod.signature.utils.SignatureHelper;

import java.util.Objects;

public class Firefly1 extends BaseCard {
    public static final String ID = Firefly1.class.getSimpleName();

    int costCache = -1;
    boolean detectUnlock = false;

    public Firefly1() {
        super(ID);
        costCache = cost;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = false;
        //addToBot(new VFXAction(new VerticalImpactEffect(m.hb.cX, m.hb.cY)));
        if (AbstractDungeon.cardRandomRng.randomBoolean()) {
            ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID + "-1"));
            addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        } else {
            ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID + "-2"));
            addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[1], 1.0F, 2.0F));
        }
        addToBot(new VFXAction(new MultiSlashEffect(m.hb.cX, m.hb.cY, 5, Color.CHARTREUSE, Color.ORANGE), Settings.FAST_MODE ? 0.6f : 1.2f));

        addToBot(
                new ElementalDamageAction(
                        m,
                        new ElementalDamageInfo(this),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                        ci -> {
                            if (ci.target.isDying && detectUnlock) {
                                SignatureHelper.unlock(cardID, true);
                            }
                        }
                )
        );
        ModHelper.addToBotAbstract(() -> {
            if (m.hasPower(BrokenPower.POWER_ID)) {
                addToBot(new BreakDamageAction(m, new DamageInfo(p, tr), 0.5f).setCallback(c -> {
                    if (c.isDying && detectUnlock) {
                        SignatureHelper.unlock(cardID, true);
                    }
                }));
                returnToHand = true;
                setCostForTurn(costCache);
            }
        });

        if (p.currentHealth == 1) {
            int count = 0;
            for (int i = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1; i >= 0; i--) {
                if (Objects.equals(AbstractDungeon.actionManager.cardsPlayedThisCombat.get(i).cardID, cardID)) {
                    count++;
                } else break;
            }
            if (count >= 3) {
                detectUnlock = true;
            }
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        glowColor = CachedCondition.check(CachedCondition.Key.ANY_BROKEN) ? GREEN_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
    }
}
