package androidTestMod.cards.uncommon;

import androidTestMod.actions.AOEAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;

import java.util.function.Function;

public class CallOfTheWilderness extends BaseCard {
    public static final String ID = CallOfTheWilderness.class.getSimpleName();
    
    public CallOfTheWilderness() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AOEAction(new Function<AbstractMonster, AbstractGameAction>() {
            @Override
            public AbstractGameAction apply(AbstractMonster q) {
                return new VFXAction(new BossCrystalImpactEffect(q.hb.cX, q.hb.cY));
            }
        }));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, magicNumber, false)));
            }
        }
        if (upgraded)
            addToBot(new AOEAction(new Function<AbstractMonster, AbstractGameAction>() {
                @Override
                public AbstractGameAction apply(AbstractMonster q) {
                    if (Math.abs(ModHelper.getPowerCount(q, StrengthPower.POWER_ID)) <= Math.abs(ModHelper.getPowerCount(p, StrengthPower.POWER_ID)))
                        return new ApplyPowerAction(q, p, new StrengthPower(q, -magicNumber), -magicNumber);
                    return null;
                }
            }));
    }
}
