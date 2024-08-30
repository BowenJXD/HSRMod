package hsrmod.powers.misc;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.only.ThanatoplumRebloomPower;

public class ToughnessPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(ToughnessPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    int stackLimit = 0;
    
    public ToughnessPower(AbstractCreature owner, int Amount, int stackLimit) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = Amount;
        this.stackLimit = stackLimit;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
        
        canGoNegative = true;
    }
    
    public ToughnessPower(AbstractCreature owner, int Amount) {
        this(owner, Amount, getStackLimit(owner));
    }
    
    public ToughnessPower(AbstractCreature owner) {
        this(owner, getStackLimit(owner));
    }

    @Override
    public void updateDescription() {
        if (amount > 0)
            this.description = String.format(DESCRIPTIONS[0], this.amount, stackLimit);
        else
            this.description = String.format(DESCRIPTIONS[1], -this.amount, stackLimit);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL)
            return damage * ( 1 - (this.amount / 100.0F) );
        return damage;
    }

    @Override
    public void stackPower(int stackAmount) {
        if (owner.hasPower(ThanatoplumRebloomPower.POWER_ID) && stackAmount > 0) {
            addToTop(new ApplyPowerAction(owner, owner, new ThanatoplumRebloomPower(owner, -1), -1));
            return;
        }
        
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        
        if (this.amount < -stackLimit) {
            this.amount = -stackLimit;
        }
        else if (this.amount > stackLimit) {
            this.amount = stackLimit;
        }
    }
    
    public static int getStackLimit(AbstractCreature c){
        int result = 0;
        if (c instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)c;
            switch (m.type) {
                case NORMAL:
                    result = 3;
                    break;
                case ELITE:
                    result = 5;
                    break;
                case BOSS:
                    result = 10;
                    break;
            }
            result += AbstractDungeon.actNum * result;
        }
        else if (c instanceof AbstractPlayer) {
            result = 50;
        }
        return result;
    }
}
