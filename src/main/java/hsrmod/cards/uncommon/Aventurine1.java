package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.powers.uniqueBuffs.AventurinePower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

public class Aventurine1 extends BaseCard {
    public static final String ID = Aventurine1.class.getSimpleName();
    
    public Aventurine1() {
        super(ID);
        cardsToPreview = new Aventurine3();
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Aventurine"));
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID));
        
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        addToBot(new ApplyPowerAction(p, p, new AventurinePower(upgraded, upgraded ? 5 : 4, 2)));
        if (p.gold == 0) {
            SignatureHelper.unlock(cardID, true);
        }
    }
}
