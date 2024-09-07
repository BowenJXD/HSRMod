package hsrmod.relics.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.relics.BaseRelic;

import java.util.ArrayList;
import java.util.Iterator;

public class CasketOfInaccuracy extends BaseRelic {
    public static final String ID = CasketOfInaccuracy.class.getSimpleName();
    
    public CasketOfInaccuracy() {
        super(ID);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem());
        // AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
        // AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
    }
}
