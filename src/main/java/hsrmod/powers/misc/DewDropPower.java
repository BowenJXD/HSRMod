package hsrmod.powers.misc;

import hsrmod.Hsrmod;
import hsrmod.powers.BuffPower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DewDropPower extends BuffPower {
    public static final String POWER_ID = Hsrmod.makePath(DewDropPower.class.getSimpleName());

    public DewDropPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        onSpecificTrigger();
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        List<AbstractMonster> toSort = new ArrayList<>();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (ModHelper.check(m) && !m.hasPower(IntangiblePower.POWER_ID)) {
                toSort.add(m);
            }
        }
        toSort.sort(new Comparator<AbstractMonster>() {
            @Override
            public int compare(AbstractMonster a, AbstractMonster b) {
                return Integer.compare(Math.abs(amount - a.currentHealth), Math.abs(amount - b.currentHealth));
            }
        });
        AbstractMonster monster = ModHelper.betterGetRandomMonster();
        for (AbstractMonster m : toSort) {
            monster = m;
            break;
        }
        if (monster != null) {
            addToBot(new DamageAction(monster, new DamageInfo(owner, amount)));
        }
    }
}
