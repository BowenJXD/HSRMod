package hsrmod.relics.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.relics.Orrery;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.CardRewardPoolEditor;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.Iterator;

import static hsrmod.characters.MyCharacter.PlayerColorEnum.HSR_PINK;

public class CasketOfInaccuracy extends BaseRelic {
    public static final String ID = CasketOfInaccuracy.class.getSimpleName();
    
    public CasketOfInaccuracy() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        // AbstractDungeon.getCurrRoom().addCardReward(new RewardItem(HSR_PINK));
        // AbstractDungeon.getCurrRoom().addCardToRewards();
        // AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(HSR_PINK));
        // AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
        // AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
        CardRewardPoolEditor.getInstance().extraCards++;
    }
}
