package hsrmod.powers.onlyBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.BreakDamageAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;

import java.util.Collections;
import java.util.List;

public class Trailblazer6Power extends AbstractPower implements DamageModApplyingPower {
    public static final String POWER_ID = HSRMod.makePath(Trailblazer6Power.class.getSimpleName());

    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    AbstractCard cardCache;
    
    public Trailblazer6Power(AbstractCreature owner, int Amount) {
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
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        cardCache = card;
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        boolean result = false;
        if (!(o instanceof AbstractCard)) return result;
        if (list.stream().anyMatch(mod -> mod instanceof Trailblazer6Modifier)) return result;
        if (damageInfo == null
            || damageInfo.type != DamageInfo.DamageType.NORMAL) return result;
        if (cardCache == null) return result;
        cardCache = null;
        result = true;
        return result;
    }

    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return Collections.singletonList(new Trailblazer6Modifier());
    }
    
    public static class Trailblazer6Modifier extends AbstractDamageModifier {
        @Override
        public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
            if (lastDamageTaken > 0) {
                AbstractPower toughnessPower = target.getPower(ToughnessPower.POWER_ID);
                AbstractPower brokenPower = target.getPower(BrokenPower.POWER_ID);
                AbstractPower trailblazer6Power = info.owner.getPower(Trailblazer6Power.POWER_ID);
                if (toughnessPower != null
                        && brokenPower != null 
                        && trailblazer6Power != null) {
                    trailblazer6Power.flash();
                    int dmg = Math.abs(toughnessPower.amount);
                    addToBot(new BreakDamageAction(target, new DamageInfo(info.owner, dmg)));
                }
            }
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new Trailblazer6Modifier();
        }
    }
}
