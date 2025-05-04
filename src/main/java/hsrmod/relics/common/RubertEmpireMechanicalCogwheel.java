package hsrmod.relics.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import hsrmod.relics.BaseRelic;

public class RubertEmpireMechanicalCogwheel extends BaseRelic implements IRubertEmpireRelic{
    public static final String ID = RubertEmpireMechanicalCogwheel.class.getSimpleName();

    public int goldGain = 50;
    public int threshold = 750;
    
    public RubertEmpireMechanicalCogwheel() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        checkMerge();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (usedUp) return;
        AbstractDungeon.player.gainGold(goldGain);
        if (AbstractDungeon.player.gold >= threshold) {
            AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
            destroy();
        } else {
            AbstractDungeon.effectList.add(new RainingGoldEffect(goldGain));
        }
    }
}
