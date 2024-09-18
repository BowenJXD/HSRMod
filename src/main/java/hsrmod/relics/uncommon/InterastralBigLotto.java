package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import static hsrmod.characters.MyCharacter.PlayerColorEnum.HSR_PINK;

public class InterastralBigLotto extends BaseRelic {
    public static final String ID = InterastralBigLotto.class.getSimpleName();

    AbstractRoom currRoom;
    
    int winChance = 20;
    int loseChance = 10;
    
    public InterastralBigLotto() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        counter = 1;
    }

    @Override
    public void update() {
        super.update();
        if (!usedUp && isObtained
                && AbstractDungeon.getCurrRoom().isBattleOver
                && AbstractDungeon.getCurrRoom() != currRoom
                && !AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
            currRoom = AbstractDungeon.getCurrRoom();
            if (AbstractDungeon.relicRng.random(100) < winChance) {
                flash();
                RewardItem rewardItem = new RewardItem(HSR_PINK);
                RewardEditor.getInstance().setRewardCards(rewardItem);
                AbstractDungeon.combatRewardScreen.rewards.add(rewardItem);
            }
            if (AbstractDungeon.relicRng.random(100) < loseChance) {
                flash();
                AbstractCard card = AbstractDungeon.player.masterDeck.getRandomCard(AbstractDungeon.relicRng);
                if (card != null) {
                    AbstractDungeon.player.masterDeck.removeCard(card);
                }
                destroy();
            }
        }
    }
}
