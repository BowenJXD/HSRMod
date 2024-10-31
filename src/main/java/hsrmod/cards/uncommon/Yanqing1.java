package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.CardDataCol;
import hsrmod.utils.DataManager;

public class Yanqing1 extends BaseCard {
    public static final String ID = Yanqing1.class.getSimpleName();
    
    public Yanqing1() {
        super(ID);
        energyCost = 140;
        tags.add(CustomEnums.ENERGY_COSTING);
        cardsToPreview = new Yanqing2();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.cardRandomRng.random(100) < 60) {
            AbstractCard card = new Yanqing2();
            if (upgraded) card.upgrade();
            addToBot(new MakeTempCardInHandAction(card));
        }
        
        ElementalDamageAction action = new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        );
        addToBot(action);
        if (p.currentBlock > 0) {
            addToBot(action.makeCopy());
        }
    }
}
