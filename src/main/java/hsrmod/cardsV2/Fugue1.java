package hsrmod.cardsV2;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.powers.uniqueDebuffs.CloudflameLusterPower;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.stream.Collectors;

public class Fugue1 extends BaseCard {
    public static final String ID = Fugue1.class.getSimpleName();
    
    public Fugue1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        List<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters.stream().filter(mo -> !mo.isDeadOrEscaped()).collect(Collectors.toList());
        for (AbstractMonster monster : monsters) {
            int amt = magicNumber - ModHelper.getPowerCount(monster, ToughnessPower.POWER_ID);
            amt = Math.min(magicNumber, Math.max(amt, -magicNumber));
            AbstractPower power = monster.getPower(ToughnessPower.POWER_ID);
            if (power != null) {
                int finalAmt = amt;
                ModHelper.addToBotAbstract(() -> ((ToughnessPower)power).alterPower(finalAmt));
            }
            addToBot(new ApplyPowerAction(monster, p, new CloudflameLusterPower(monster, magicNumber, upgraded), magicNumber));
        }
    }
}
