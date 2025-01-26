package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.RewardEditor;

import static hsrmod.characters.StellaCharacter.PlayerColorEnum.HSR_PINK;

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
        if (usedUp) return;
        if (AbstractDungeon.relicRng.random(100) < winChance) {
            flash();
            RewardEditor.addExtraCardRewardToTop();
        }
        if (AbstractDungeon.relicRng.random(100) < loseChance) {
            flash();
            AbstractCard card = GeneralUtil.getRandomElement(AbstractDungeon.player.masterDeck.getPurgeableCards().group, AbstractDungeon.relicRng);
            if (card != null) {
                AbstractDungeon.topLevelEffectsQueue.add(new PurgeCardEffect(card));
                AbstractDungeon.player.masterDeck.removeCard(card);
            }
            destroy();
        }
    }
}
