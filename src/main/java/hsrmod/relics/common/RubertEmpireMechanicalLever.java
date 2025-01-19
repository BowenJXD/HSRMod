package hsrmod.relics.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.RelicEventHelper;

public class RubertEmpireMechanicalLever extends BaseRelic implements IRubertEmpireRelic {
    public static final String ID = RubertEmpireMechanicalLever.class.getSimpleName();

    public RubertEmpireMechanicalLever() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        checkMerge();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (usedUp) return;
        if (room instanceof EventRoom) {
            AbstractCard card = GeneralUtil.getRandomElement(AbstractDungeon.player.masterDeck.group, AbstractDungeon.relicRng);
            if (card != null && !card.upgraded) {
                RelicEventHelper.upgradeCards(card);
            } else {
                destroy();
            }
        }
    }
}
