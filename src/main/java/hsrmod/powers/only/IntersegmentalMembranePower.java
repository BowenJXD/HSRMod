package hsrmod.powers.only;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.EnergyPower;

public class IntersegmentalMembranePower extends AbstractPower {
    public static final String POWER_ID = HSRMod.makePath(IntersegmentalMembranePower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    int block;
    
    public IntersegmentalMembranePower(AbstractCreature owner, int Amount, int block) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = Amount;
        this.block = block;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.block);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof BaseCard && ((BaseCard) card).followedUp) 
            return;
        int energyChange = card.costForTurn;
        if (card.costForTurn == -1) {
            energyChange = card.energyOnUse;
        }
        if (energyChange > 0) {
            flash();
            addToBot(new GainBlockAction(owner, owner, energyChange * block));
        }
    }
}
