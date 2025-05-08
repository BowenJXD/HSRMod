package hsrmod.cardsV2.Erudition;

import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.WallopEffect;

import java.util.function.Consumer;

public class TheHerta1 extends BaseCard {
    public static final String ID = TheHerta1.class.getSimpleName();

    public int totalDmg = 0;

    public TheHerta1() {
        super(ID);
        setBaseEnergyCost(220);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);

        totalDmg = 0;
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .setCallback(new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        totalDmg += ci.info.output;
                        TheHerta1.this.addToTop(new VFXAction(new WallopEffect(ci.info.base, ci.target.hb.cX, ci.target.hb.cY)));
                    }
                }));
        int useCount = 0;
        for (AbstractCard abstractCard : p.hand.group) {
            if (abstractCard.hasTag(CustomEnums.ENERGY_COSTING) && abstractCard != TheHerta1.this) {
                useCount++;
                break;
            }
        }
        if (upgraded) {
            for (AbstractCard c : p.hand.group) {
                if ((c.target == CardTarget.ALL_ENEMY || c.target == CardTarget.ALL) && c != TheHerta1.this) {
                    useCount++;
                    break;
                }
            }
        }

        boolean seen = false;
        AbstractMonster best = null;
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped() && mo.currentHealth > 0 && !mo.halfDead) {
                if (!seen || mo.currentHealth > best.currentHealth) {
                    seen = true;
                    best = mo;
                }
            }
        }
        AbstractMonster monsterWithMostHealth = seen ? best : null;
        if (monsterWithMostHealth != null) {
            for (int i = 0; i < useCount; i++) {
                addToBot(new ElementalDamageAction(monsterWithMostHealth, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_VERTICAL)
                        .setCallback(new Consumer<ElementalDamageAction.CallbackInfo>() {
                            @Override
                            public void accept(ElementalDamageAction.CallbackInfo ci) {
                                totalDmg += ci.info.output;
                                TheHerta1.this.addToTop(new VFXAction(new WallopEffect(ci.info.base, ci.target.hb.cX, ci.target.hb.cY)));
                            }
                        }));
            }
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        int val = 0;
        for (AbstractCard abstractCard : AbstractDungeon.player.hand.group) {
            if (abstractCard.hasTag(CustomEnums.ENERGY_COSTING) && abstractCard != TheHerta1.this) {
                val++;
                break;
            }
        }
        if (upgraded) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if ((c.target == CardTarget.ALL_ENEMY || c.target == CardTarget.ALL) && c != TheHerta1.this) {
                    val++;
                    break;
                }
            }
        }
        if (val == 0) {
            glowColor = BLUE_BORDER_GLOW_COLOR;
        } else if (val == 1) {
            glowColor = GREEN_BORDER_GLOW_COLOR;
        } else {
            glowColor = GOLD_BORDER_GLOW_COLOR;
        }
    }
}
