package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.breaks.ShockPower;
import hsrmod.utils.ModHelper;

public class Kafka2 extends BaseCard {
    public static final String ID = Kafka2.class.getSimpleName();

    public Kafka2() {
        super(ID);
        isMultiDamage = true;
        energyCost = 120;
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID));
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));

        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.LIGHTNING)
                .setCallback(ci -> {
                    if (!ci.target.isDeadOrEscaped() && ci.target.currentHealth > 0) {
                        addToBot(new TriggerDoTAction(ci.target, 1, true));
                        addToBot(new ApplyPowerAction(ci.target, p, new ShockPower(ci.target, p, magicNumber), magicNumber));
                    }
                })
        );
    }
}
