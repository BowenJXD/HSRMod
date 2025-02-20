package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.utils.ModHelper;

public class Gepard1 extends BaseCard {
    public static final String ID = Gepard1.class.getSimpleName();
    
    public Gepard1() {
        super(ID);
        setBaseEnergyCost(100);
        tags.add(CustomEnums.ENERGY_COSTING);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Gepard"));
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID));
        
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        addToBot(new GainBlockAction(p, p, block));
    }
}
