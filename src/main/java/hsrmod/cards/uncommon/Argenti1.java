package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class Argenti1 extends BaseCard {
    public static final String ID = Argenti1.class.getSimpleName();

    int energyExhaust = 90;
    int energyGain = 10;

    public Argenti1() {
        super(ID);
        tags.add(CustomEnums.ENERGY_COSTING);
        selfRetain = true;
        isMultiDamage = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && ModHelper.getPowerCount(EnergyPower.POWER_ID) >= energyExhaust;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID));
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        ModHelper.addToBotAbstract(this::execute);
    }

    void execute() {
        if (AbstractDungeon.player.getPower(EnergyPower.POWER_ID).amount < energyExhaust
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;
        ElementalDamageAllAction action = new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        if (upgraded) action.setCallback(c -> {
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new EnergyPower(AbstractDungeon.player, energyGain), energyGain));
        });
        addToBot(action);
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new EnergyPower(AbstractDungeon.player, -energyExhaust), -energyExhaust));
        ModHelper.addToBotAbstract(this::execute);
    }
}
