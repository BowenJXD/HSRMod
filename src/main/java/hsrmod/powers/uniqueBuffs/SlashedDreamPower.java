package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;

import javax.swing.*;
import java.util.List;

public class SlashedDreamPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(SlashedDreamPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    boolean canTrigger = false;
    
    int stackLimit = 12;
    
    int triggerAmount = 9;
    
    int baseDamage = 3;
    
    boolean upgraded = false;

    public SlashedDreamPower(AbstractCreature owner, int Amount, boolean upgraded) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = Amount;
        this.upgraded = upgraded;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], baseDamage, baseDamage);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        canTrigger = true;
        if (upgraded && card.isEthereal){
            canTrigger = false;
            flash();
            stackPower(1);
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        List<AbstractGameAction> tmp = AbstractDungeon.actionManager.actions;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (canTrigger && power.type == PowerType.DEBUFF) {
            canTrigger = false;
            flash();
            stackPower(1);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            this.amount = stackLimit;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (amount >= triggerAmount) {
            flash();
            trigger();
            reducePower(triggerAmount);
        }
    }
    
    void trigger(){
        AbstractCreature target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        ElementalDamageAction action = new ElementalDamageAction(target, new DamageInfo(owner, baseDamage), 
                ElementType.Lightning, 1, AbstractGameAction.AttackEffect.LIGHTNING, null, 
                c -> c.powers.stream().filter(p -> p.type == PowerType.DEBUFF).mapToInt(p -> 1).sum());
        addToBot(new BouncingAction(target, 3, action.makeCopy()));
        
        addToBot(new AOEAction(q -> {
            action.makeCopy();
            action.target = q;
            return action;
        }));
    }
}