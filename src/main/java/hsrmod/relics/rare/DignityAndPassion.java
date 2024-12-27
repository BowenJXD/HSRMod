package hsrmod.relics.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;

public class DignityAndPassion extends BaseRelic {
    public static final String ID = DignityAndPassion.class.getSimpleName();

    public DignityAndPassion() {
        super(ID);
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        flash();
        AbstractPlayer p = AbstractDungeon.player;
        int hpLost = p.maxHealth - p.currentHealth;
        int value = hpLost * magicNumber / 100;
        int tempHp = TempHPField.tempHp.get(p);
        int amt = value - tempHp;
        if (amt > 0) {
            addToBot(new AddTemporaryHPAction(p, p, amt));
        }
    }
}
