package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

import java.util.HashMap;
import java.util.Map;

public class Gallagher1 extends BaseCard {
    public static final String ID = Gallagher1.class.getSimpleName();

    public Gallagher1() {
        super(ID);
        energyCost = 110;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        Map<AbstractCreature, Integer> toughnessMap = new HashMap<>();
        for (AbstractMonster q : AbstractDungeon.getCurrRoom().monsters.monsters) {
            toughnessMap.put(q, ModHelper.getPowerCount(q, ToughnessPower.POWER_ID));
        }
        addToBot(
                new AOEAction((q) ->
                    new ElementalDamageAction(q, new DamageInfo(p, damage),
                            ElementType.Fire, magicNumber,
                            AbstractGameAction.AttackEffect.FIRE,
                            (c) -> {
                                if (ModHelper.getPowerCount(q, ToughnessPower.POWER_ID) <= 0
                                        && (!toughnessMap.containsKey(c) || toughnessMap.get(c) > 0 || upgraded)) {
                                    addToBot(new DrawCardAction(1));
                                    addToBot(new ApplyPowerAction(c, p, new BlockReturnPower(c, 1), 1));
                                }
                            })
                )
        );
    }
}
