package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.SubscribeManager;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class AventurinePower extends PowerPower implements OnPlayerDamagedSubscriber {
    public static final String POWER_ID = HSRMod.makePath(AventurinePower.class.getSimpleName());

    int damageStack, followUpStack, stackLimit = 10, triggerAmount = 7;
    
    boolean isPlayerTurn = true;

    public AventurinePower(boolean upgraded, int damageStack, int followUpStack) {
        super(POWER_ID, upgraded);
        this.damageStack = damageStack;
        this.followUpStack = followUpStack;
        
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[upgraded ? 1 : 0], damageStack, followUpStack);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            isPlayerTurn = false;
        }
    }

    @Override
    public void atStartOfTurn() {
        isPlayerTurn = true;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            this.amount = stackLimit;
        }
        if (this.amount >= triggerAmount) {
            trigger();
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.hasTag(FOLLOW_UP)) {
            if (card instanceof BaseCard) {
                BaseCard c = (BaseCard) card;
                if (c.followedUp) {
                    flash();
                    stackPower(followUpStack);
                }
            }
        }
    }
    
    void trigger(){
        reducePower(triggerAmount);
        
        int d = AbstractDungeon.cardRandomRng.random(1,triggerAmount);
        int b = AbstractDungeon.cardRandomRng.random(1,triggerAmount);
        
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        
        if (m == null) return;
        addToBot(new ElementalDamageAction(
                m,
                new DamageInfo(
                        AbstractDungeon.player,
                        d,
                        DamageInfo.DamageType.NORMAL
                ),
                ElementType.Imaginary,
                1,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        ));
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, b));
        if (!isPlayerTurn && upgraded)
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, 0), 0));
    }

    @Override
    public void onInitialApplication() {
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        BaseMod.unsubscribe(this);
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (SubscribeManager.checkSubscriber(this) 
                && owner.currentBlock > 0) {
            flash();
            stackPower(damageStack);
        }
        return i;
    }
}

    
