package hsrmod.powers.misc;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.ModHelper;

import java.util.Collections;
import java.util.List;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class AftertastePower extends AbstractPower implements DamageModApplyingPower {
    public static final String POWER_ID = HSRMod.makePath(AftertastePower.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AftertastePower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = Amount;

        String path128 = String.format("HSRModResources/img/powers/%s128.png", this.getClass().getSimpleName());
        String path48 = String.format("HSRModResources/img/powers/%s48.png", this.getClass().getSimpleName());
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        boolean result = false;
        if (!(o instanceof AbstractCard)) return result;
        if (list.stream().anyMatch(mod -> mod instanceof AftertasteModifier)) return result;
        AbstractCard card = (AbstractCard) o;
        if (card.hasTag(FOLLOW_UP)) {
            return true;
        }

        return result;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new AftertasteModifier());
    }
    
    public static class AftertasteModifier extends AbstractDamageModifier {

        public AftertasteModifier() {
        }

        @Override
        public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
            if (info.owner.hasPower(AftertastePower.POWER_ID)) {
                AftertastePower power = (AftertastePower) info.owner.getPower(AftertastePower.POWER_ID);
                addToBot(new ElementalDamageAction(target, new DamageInfo(info.owner, power.amount, DamageInfo.DamageType.NORMAL), 
                        ModHelper.getRandomEnumValue(ElementType.class), 1, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(info.owner, info.owner, power, 1));
            }
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new AftertasteModifier();
        }
    }
}
