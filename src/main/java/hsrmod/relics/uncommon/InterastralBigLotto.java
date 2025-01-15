package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import static hsrmod.characters.StellaCharacter.PlayerColorEnum.HSR_PINK;

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
        currRoom = AbstractDungeon.getCurrRoom();
    }

    @Override
    public void update() {
        super.update();
        if (!usedUp && isObtained
                && AbstractDungeon.getCurrRoom().isBattleOver
                && AbstractDungeon.getCurrRoom() != currRoom
                && !AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
            currRoom = AbstractDungeon.getCurrRoom();
            if (AbstractDungeon.cardRng.random(100) < winChance) {
                flash();
                RewardItem rewardItem = new RewardItem(HSR_PINK);
                RewardEditor.getInstance().setRewardCards(rewardItem);
                AbstractDungeon.combatRewardScreen.rewards.add(rewardItem);
            }
            if (AbstractDungeon.cardRng.random(100) < loseChance) {
                flash();
                AbstractCard card = AbstractDungeon.player.masterDeck.getRandomCard(AbstractDungeon.cardRng);
                if (card != null) {
                    AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card));
                    AbstractDungeon.player.masterDeck.removeCard(card);
                }
                destroy();
            }
        }
    }
}
