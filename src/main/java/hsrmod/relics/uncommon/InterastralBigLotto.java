package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.CardRewardPoolEditor;

import static hsrmod.characters.MyCharacter.PlayerColorEnum.HSR_PINK;

public class InterastralBigLotto extends BaseRelic {
    public static final String ID = InterastralBigLotto.class.getSimpleName();

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
    public void atBattleStart() {
        super.atBattleStart();
        if (counter <= 0) return;
        if (AbstractDungeon.relicRng.random(100) < winChance) {
            flash();
            CardRewardPoolEditor.getInstance().extraCards++;
        } else if (AbstractDungeon.relicRng.random(100) < loseChance) {
            flash();
            AbstractCard card = AbstractDungeon.player.masterDeck.getRandomCard(AbstractDungeon.relicRng);
            if (card != null) {
                AbstractDungeon.player.masterDeck.removeCard(card);
            }
            destroy();
        }
    }
}
