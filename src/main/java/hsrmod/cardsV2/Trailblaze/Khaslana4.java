package hsrmod.cardsV2.Trailblaze;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.ShoutVoiceAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.TopWarningEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modifiers.OdeToWorldbearing2Modifier;
import hsrmod.powers.uniqueBuffs.FuturePower;
import hsrmod.powers.uniqueBuffs.RuinousIrontombPower;
import hsrmod.powers.uniqueBuffs.ScourgePower;
import hsrmod.utils.ModHelper;

public class Khaslana4 extends BaseCard {
    public static final String ID = Khaslana4.class.getSimpleName();
    
    public Khaslana4() {
        super(ID);
        exhaust = true;
        tags.add(CustomEnums.CHRYSOS_HEIR);
        tags.add(CustomEnums.TERRITORY);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        addToBot(new ShoutVoiceAction(this.getClass().getSimpleName(), 3.0f));
        addToBot(new VFXAction(new ScreenOnFireEffect()));

        int monsterCount = (int) AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).count();
        int[] damageMatrix = DamageInfo.createDamageMatrix(damage / monsterCount, true);
        addToBot(new ElementalDamageAllAction(p, damageMatrix, damageTypeForTurn, elementType, tr, 
                AbstractGameAction.AttackEffect.SMASH).setBaseCard(this));
        
        if (!p.hasPower(FuturePower.POWER_ID)) {
            addToBot(new RemoveSpecificPowerAction(p, p, RuinousIrontombPower.POWER_ID));
            // addToBot(new PressEndTurnButtonAction());
        } else {
            if (p.hasPower(RuinousIrontombPower.POWER_ID)) {
                addToBot(new ApplyPowerAction(p, p, new RuinousIrontombPower(p, 8)));
                addToBot(new ApplyPowerAction(p, p, new ScourgePower(p, 7)));
                addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 2)));
                addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, 2)));
                addToBot(new ApplyPowerAction(p, p, new BeatOfDeathPower(p, 2)));
            }
            AbstractDungeon.topLevelEffects.add(new TopWarningEffect(cardStrings.EXTENDED_DESCRIPTION[MathUtils.randomBoolean() ? 1:2]));
        }
        addToBot(new ChangeStanceAction(new NeutralStance()));
    }
}
