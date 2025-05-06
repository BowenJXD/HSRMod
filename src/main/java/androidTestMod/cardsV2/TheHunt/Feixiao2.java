package androidTestMod.cardsV2.TheHunt;

import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.effects.PortraitDisplayEffect;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

public class Feixiao2 extends BaseCard {
    public static final String ID = Feixiao2.class.getSimpleName();

    int costCache;
    boolean subscribed = false;

    public Feixiao2() {
        super(ID);
        tags.add(CustomEnums.FOLLOW_UP);
        costCache = cost;
    }

    @Override
    public void updateCost(int amt) {
        super.updateCost(amt);
        followUp();
    }

    @Override
    public void modifyCostForCombat(int amt) {
        super.modifyCostForCombat(amt);
        followUp();
    }

    @Override
    public void setCostForTurn(int amt) {
        super.setCostForTurn(amt);
        followUp();
    }

    void followUp() {
        if (!followedUp && costForTurn == 0) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Feixiao"));
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                CardCrawlGame.sound.play(ID);
            }
        });
        addToBot(new VFXAction(new WhirlwindEffect()));
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                Feixiao2.this.updateCost(costCache - cost);
            }
        });
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        if (AbstractDungeon.player.hand.contains(this) && !followedUp && c.type == CardType.ATTACK) {
            updateCost(-1);
        }
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        super.triggerOnCardPlayed(cardPlayed);
        if (cardPlayed != this && cardPlayed.type == CardType.ATTACK) {
            baseDamage++;
        }
    }
}
