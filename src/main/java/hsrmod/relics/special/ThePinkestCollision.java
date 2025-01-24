package hsrmod.relics.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.relics.BaseRelic;

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
        setCounter((int) AbstractDungeon.player.masterDeck.group.stream()
                .map(c -> c instanceof BaseCard ? ((BaseCard)c).pathTag : CustomEnums.TRAILBLAZE)
                .distinct().count()
        );
    }
}
