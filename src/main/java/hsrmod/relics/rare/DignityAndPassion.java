package hsrmod.relics.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
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
        if (hpLost > 0) {
            addToBot(new AddTemporaryHPAction(p, p, hpLost * magicNumber / 100));
        }
    }
}
