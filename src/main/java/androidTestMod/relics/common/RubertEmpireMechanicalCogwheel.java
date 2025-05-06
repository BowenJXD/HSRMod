package androidTestMod.relics.common;

import androidTestMod.AndroidTestMod;
import androidTestMod.effects.MergeEffect;
import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;

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
    
    public void checkMerge() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean merge = p.hasRelic(AndroidTestMod.makePath(RubertEmpireMechanicalCogwheel.ID))
                && p.hasRelic(AndroidTestMod.makePath(RubertEmpireMechanicalLever.ID))
                && p.hasRelic(AndroidTestMod.makePath(RubertEmpireMechanicalPiston.ID));
        if (merge) {
            boolean b = true;
            for (AbstractGameEffect e : AbstractDungeon.effectList) {
                if (e instanceof MergeEffect) {
                    b = false;
                    break;
                }
            }
            if (b) {
                AbstractDungeon.effectList.add(new MergeEffect());
            }
        }
    }
}
