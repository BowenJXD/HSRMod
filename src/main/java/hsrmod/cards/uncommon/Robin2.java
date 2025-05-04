package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.RobinPower;
import hsrmod.utils.ModHelper;

public class Robin2 extends BaseCard {
    public static final String ID = Robin2.class.getSimpleName();
    
    public Robin2() {
        super(ID);
        setBaseEnergyCost(140);
        tags.add(CustomEnums.ENERGY_COSTING);
    }
    
    @Override
    public void onEnterHand() {
        super.onEnterHand();
        CardCrawlGame.music.precacheTempBgm("RobinBGM");
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Robin"));
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                CardCrawlGame.sound.play(ID);
            }
        });
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 3.0F, 4.0F));
        // addToBot(new ForceWaitAction(2.0F));

        addToBot(new DrawCardAction(p, magicNumber));
        AbstractPower power = p.getPower(RobinPower.POWER_ID);
        if (power != null)
            ((RobinPower) power).upgraded = upgraded;
        else
            addToBot(new ApplyPowerAction(p, p, new RobinPower(p, upgraded)));
    }
}
