package hsrmod.powers.onlyBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.EnergyPower;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class RobinPower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(RobinPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    boolean upgraded = false;

    public RobinPower(AbstractCreature owner, int Amount, boolean upgraded) {
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
        this.description = upgraded ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) reducePower(1);
        if (amount == 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {        
        if (card.hasTag(FOLLOW_UP)) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1));
            addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, 1), 1));
            if (upgraded) addToBot(new ApplyPowerAction(owner, owner, new EnergyPower(owner, 20), 20));
        }
        else if (owner.hasPower(LoseStrengthPower.POWER_ID)) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -1), -1));
            addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, -1), -1));
        }
    }
}
