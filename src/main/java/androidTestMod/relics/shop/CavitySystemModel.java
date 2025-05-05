package androidTestMod.relics.shop;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import androidTestMod.relics.BaseRelic;

public class CavitySystemModel extends BaseRelic {
    public static final String ID = CavitySystemModel.class.getSimpleName();
    
    public CavitySystemModel() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        setCounter(AbstractDungeon.player.gold / magicNumber);
        AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
        AbstractDungeon.player.masterHandSize += counter;
    }
}
