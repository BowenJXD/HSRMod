package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

import java.util.HashMap;
import java.util.Map;

public class Rappa1 extends BaseCard {
    public static final String ID = Rappa1.class.getSimpleName();

    boolean canRepeat = false;

    public Rappa1() {
        super(ID);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        execute();
    }

    void execute() {
        canRepeat = true;
        Map<AbstractCreature, Integer> toughnessMap = new HashMap<>();
        for (AbstractMonster q : AbstractDungeon.getCurrRoom().monsters.monsters) {
            toughnessMap.put(q, ModHelper.getPowerCount(q, ToughnessPower.POWER_ID));
        }

        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BreakEffectPower(AbstractDungeon.player, magicNumber), magicNumber));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL).setCallback((c) -> {
            if ((!toughnessMap.containsKey(c) || toughnessMap.get(c) > 0)
                    && ModHelper.getPowerCount(c, ToughnessPower.POWER_ID) <= 0) {
                addToBot(new BreakDamageAction(c, new DamageInfo(AbstractDungeon.player, tr)));
                if (canRepeat) {
                    canRepeat = false;
                    ModHelper.addToBotAbstract(this::execute);
                }
            }
        }));
    }
}
