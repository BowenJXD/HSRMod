package hsrmod.cardsV2.Trailblaze;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.TopWarningEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modifiers.OdeToWorldbearing2Modifier;
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
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName(), 3.0f));

        int monsterCount = (int) AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).count();
        int[] damageMatrix = DamageInfo.createDamageMatrix(damage / monsterCount, true);
        addToBot(new ElementalDamageAllAction(p, damageMatrix, damageTypeForTurn, elementType, tr, 
                AbstractGameAction.AttackEffect.SMASH).setBaseCard(this));
        
        if (!CardModifierManager.hasModifier(this, OdeToWorldbearing2Modifier.ID)) {
            addToBot(new RemoveSpecificPowerAction(p, p,RuinousIrontombPower.POWER_ID));
            // addToBot(new PressEndTurnButtonAction());
        } else {
            if (p.hasPower(RuinousIrontombPower.POWER_ID)) 
                addToBot(new ApplyPowerAction(p, p, new ScourgePower(p, 8)));
            addToBot(new VFXAction(new TopWarningEffect(cardStrings.EXTENDED_DESCRIPTION[MathUtils.randomBoolean() ? 1:2])));
        }
    }
}
