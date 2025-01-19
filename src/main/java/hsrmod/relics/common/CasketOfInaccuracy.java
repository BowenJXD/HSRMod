package hsrmod.relics.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.TinyHouse;
import com.megacrit.cardcrawl.rewards.RewardItem;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import java.util.ArrayList;
import java.util.Iterator;

public class CasketOfInaccuracy extends BaseRelic {
    public static final String ID = CasketOfInaccuracy.class.getSimpleName();
    
    public CasketOfInaccuracy() {
        super(ID);
    }

    public void onEquip() {
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getMonsters() == null) {
            flash();
            AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
        } else {
            flash();
            AbstractDungeon.getCurrRoom().addCardToRewards();
        }
    }
}
