package hsrmod.powers.uniqueDebuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPowersModifiedSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.DoTPower;
import hsrmod.subscribers.SubscribeManager;

public class EpiphanyPower extends AbstractPower implements OnPowersModifiedSubscriber {
    public static final String POWER_ID = HSRMod.makePath(EpiphanyPower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public EpiphanyPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;

        this.amount = Amount;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0]);
    }

    @Override
    public void onInitialApplication() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = false);
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = true);
        BaseMod.unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        reducePower(1);
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void receivePowersModified() {
        owner.powers.stream().filter(power -> power instanceof DoTPower).map(power -> (DoTPower) power).forEach(p -> p.removeOnTrigger = false);
    }
}
