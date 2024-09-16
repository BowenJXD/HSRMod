package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.breaks.WindShearPower;
import hsrmod.utils.ModHelper;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class JingYuan1 extends BaseCard {
    public static final String ID = JingYuan1.class.getSimpleName();
    
    int costCache = -1;
    
    public JingYuan1() {
        super(ID);
        tags.add(FOLLOW_UP);
        costCache = cost;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID));
        addToBot(new TalkAction(true, "斩无赦！", 1.0F, 2.0F));

        ElementalDamageAction elementalDamageAction = new ElementalDamageAction(m, new DamageInfo(p, this.damage,
                damageTypeForTurn), elementType, 1, AbstractGameAction.AttackEffect.LIGHTNING
        );
        this.addToBot(new BouncingAction(m, magicNumber, elementalDamageAction));
        
        modifyCostForCombat(costCache - costForTurn);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this) || followedUp) return;
        modifyCostForCombat(-1);
        if (cost == 0) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
