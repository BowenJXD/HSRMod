package hsrmod.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.MultiShivFreezeEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;

public class Yanqing1 extends BaseCard {
    public static final String ID = Yanqing1.class.getSimpleName();
    
    public Yanqing1() {
        super(ID);
        setBaseEnergyCost(140);
        tags.add(CustomEnums.ENERGY_COSTING);
        cardsToPreview = new Yanqing2();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new MultiShivFreezeEffect(Color.CYAN, Color.ROYAL, Color.BLUE), 1f));
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

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (AbstractDungeon.player.currentBlock > 0) {
            glowColor = GOLD_BORDER_GLOW_COLOR;
        } else {
            glowColor = BLUE_BORDER_GLOW_COLOR;
        }
    }
}
