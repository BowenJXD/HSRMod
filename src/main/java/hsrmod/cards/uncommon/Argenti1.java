package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class Argenti1 extends BaseCard {
    public static final String ID = Argenti1.class.getSimpleName();

    int count = 0;
    
    public Argenti1() {
        super(ID);
        setBaseEnergyCost(90);
        tags.add(CustomEnums.ENERGY_COSTING);
        selfRetain = true;
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Argenti"));
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                CardCrawlGame.sound.play(ID);
            }
        });
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        count = 0;
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                Argenti1.this.execute();
            }
        });
    }

    void execute() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;
        if (ModHelper.getPowerCount(AbstractDungeon.player, EnergyPower.POWER_ID) >= energyCost) {
            ModHelper.addToTopAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    Argenti1.this.execute();
                }
            });
        }
        addToTop(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new EnergyPower(AbstractDungeon.player, -energyCost), -energyCost));
    }
}
