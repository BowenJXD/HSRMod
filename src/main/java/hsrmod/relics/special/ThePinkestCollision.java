package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.relics.BaseRelic;

import java.util.HashSet;
import java.util.Set;

public class ThePinkestCollision extends BaseRelic {
    public static final String ID = ThePinkestCollision.class.getSimpleName();

    public ThePinkestCollision() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        updateCounter();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        updateCounter();
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BreakEffectPower(AbstractDungeon.player, counter)));
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        super.onObtainCard(c);
        updateCounter();
    }

    void updateCounter() {
        long count = 0L;
        Set<AbstractCard.CardTags> uniqueValues = new HashSet<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            AbstractCard.CardTags cardTags = c instanceof BaseCard ? ((BaseCard) c).pathTag : CustomEnums.TRAILBLAZE;
            if (uniqueValues.add(cardTags)) {
                count++;
            }
        }
        setCounter((int) count
        );
    }
}
