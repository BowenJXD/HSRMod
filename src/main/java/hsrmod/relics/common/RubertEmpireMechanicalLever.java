package hsrmod.relics.common;

import hsrmod.Hsrmod;
import hsrmod.effects.MergeEffect;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.RelicEventHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

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
            AbstractCard card = GeneralUtil.getRandomElement(AbstractDungeon.player.masterDeck.group, AbstractDungeon.miscRng);
            if (card != null && !card.upgraded) {
                RelicEventHelper.upgradeCards(card);
            } else {
                RelicEventHelper.purgeCards(card);
                destroy();
            }
        }
    }

    public void checkMerge() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean merge = p.hasRelic(Hsrmod.makePath(RubertEmpireMechanicalCogwheel.ID))
                && p.hasRelic(Hsrmod.makePath(RubertEmpireMechanicalLever.ID))
                && p.hasRelic(Hsrmod.makePath(RubertEmpireMechanicalPiston.ID));
        if (merge) {
            boolean b = true;
            for (AbstractGameEffect e : AbstractDungeon.effectList) {
                if (e instanceof MergeEffect) {
                    b = false;
                    break;
                }
            }
            if (b) {
                AbstractDungeon.effectList.add(new MergeEffect());
            }
        }
    }
}
