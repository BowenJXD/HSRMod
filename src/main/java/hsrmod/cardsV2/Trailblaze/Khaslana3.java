package hsrmod.cardsV2.Trailblaze;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.extraeffects.ExtraEffectModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.uniqueBuffs.ScourgePower;
import hsrmod.utils.ModHelper;

public class Khaslana3 extends BaseCard {
    public static final String ID = Khaslana3.class.getSimpleName();
    
    float bounceDmgP = 0.1f;
    float splitDmgP = 0.6f;
    
    public Khaslana3() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
        tags.add(CustomEnums.TERRITORY);
        returnToHand = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && ModHelper.getPowerCount(p, ScourgePower.POWER_ID) > 0;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {        
        int scourgeCount = ModHelper.getPowerCount(p, ScourgePower.POWER_ID);
        scourgeCount = Math.min(4, scourgeCount);
        if (scourgeCount < 4) {
            shout(0, 1);
        } else {
            shout(2, 3);
        }
        AbstractCreature mon = ModHelper.betterGetRandomMonster();
        ElementalDamageAction eda = new ElementalDamageAction(
                mon, 
                new ElementalDamageInfo(p, Math.round(damage * bounceDmgP), elementType, 1).setBaseCard(this), 
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        );
        addToBot(new BouncingAction(mon, scourgeCount, eda).setPreAction(c -> {
            addToTop(new VFXAction(new WeightyImpactEffect(c.hb.cX,  c.hb.cY)));
        }));
        
        if (scourgeCount == 4) {
            int monsterCount = (int) AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).count();
            int[] damageMatrix = DamageInfo.createDamageMatrix(Math.round(damage * splitDmgP) / monsterCount, true);
            addToBot(new ElementalDamageAllAction(p, damageMatrix, damageTypeForTurn, elementType, tr - 4, 
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY).setBaseCard(this));
        }
        
        if (!freeToPlayOnce && !isInAutoplay)
            addToBot(new ReducePowerAction(p, p, ScourgePower.POWER_ID, scourgeCount));
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (ModHelper.getPowerCount(AbstractDungeon.player, ScourgePower.POWER_ID) >= 4) {
            glowColor = GOLD_BORDER_GLOW_COLOR;
        } else {
            glowColor = BLUE_BORDER_GLOW_COLOR;
        }
    }
}
