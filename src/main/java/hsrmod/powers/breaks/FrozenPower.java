package hsrmod.powers.breaks;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;

public class FrozenPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(FrozenPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private int amountRequired = 99;
    
    AbstractMonster monsterOwner;

    public FrozenPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;

        this.amount = Amount;
        if (owner instanceof AbstractMonster) {
            monsterOwner = (AbstractMonster) owner;
        }
        amountRequired = getAmountRequired();

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
        
        priority = 6;
    }

    @Override
    public void updateDescription() {
        if (amount < amountRequired){
            this.description = String.format(DESCRIPTIONS[0], amountRequired) + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[1];
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount <= 0){
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void atStartOfTurn() {
        if (amount >= amountRequired){
            addToBot(new StunMonsterAction((AbstractMonster) owner, AbstractDungeon.player));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        reducePower(1);
        if (amount <= 0){
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
        return damageAmount;
    }

    public int getAmountRequired(){
        int result = 99;
        if (monsterOwner != null){
            switch (monsterOwner.type){
                case NORMAL:
                    result = 1;
                    break;
                case ELITE:
                    result = 2;
                    break;
                case BOSS:
                    result = 3;
                    break;
            }
        }
        return result;
        
    }
}
