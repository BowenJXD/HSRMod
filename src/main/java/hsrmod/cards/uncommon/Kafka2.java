package hsrmod.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.MultiShivFreezeEffect;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.breaks.BurnPower;
import hsrmod.powers.breaks.ShockPower;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

import java.util.function.Consumer;

public class Kafka2 extends BaseCard {
    public static final String ID = Kafka2.class.getSimpleName();

    int count = 0;
    
    public Kafka2() {
        super(ID);
        isMultiDamage = true;
        setBaseEnergyCost(120);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Kafka"));
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                CardCrawlGame.sound.play(ID);
            }
        });
        addToBot(new VFXAction(new MultiShivFreezeEffect(Color.MAROON, Color.MAGENTA, Color.PURPLE), 0.3F));
        
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));

        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.LIGHTNING)
                .setCallback(new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        if (!ci.target.isDeadOrEscaped() && ci.target.currentHealth > 0) {
                            Kafka2.this.addToBot(new TriggerDoTAction(ci.target, p, 1, true));
                            Kafka2.this.addToBot(new ApplyPowerAction(ci.target, p, new ShockPower(ci.target, p, magicNumber), magicNumber));

                            if (ci.target.hasPower(ShockPower.POWER_ID)
                                    && ci.target.hasPower(BurnPower.POWER_ID)
                                    && ci.target.hasPower(BleedingPower.POWER_ID)
                                    && ci.target.hasPower(WindShearPower.POWER_ID)) {
                                count++;
                                if (count == 2) {
                                    SignatureHelper.unlock(cardID, true);
                                }
                            }
                        }
                    }
                })
        );
    }
}
