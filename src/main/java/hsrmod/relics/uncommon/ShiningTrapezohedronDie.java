package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShiningTrapezohedronDie extends BaseRelic {
    public static final String ID = ShiningTrapezohedronDie.class.getSimpleName();
    
    public ShiningTrapezohedronDie() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        
        AbstractRelic[] relics = AbstractDungeon.player.relics.stream()
                .filter(r -> r.tier == RelicTier.COMMON || r.tier == RelicTier.UNCOMMON || r.tier == RelicTier.RARE)
                .toArray(AbstractRelic[]::new);
        if (relics.length > 0) {
            RelicEventHelper.loseRelicsAfterwards(relics);
            RelicEventHelper.gainRelicsAfterwards(relics.length);
        }
    }
}
