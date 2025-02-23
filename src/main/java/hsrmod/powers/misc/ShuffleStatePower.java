package hsrmod.powers.misc;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnDrawPileShufflePower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.utils.GeneralUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShuffleStatePower extends StatePower implements OnDrawPileShufflePower {
    public static final String POWER_ID = HSRMod.makePath(ShuffleStatePower.class.getSimpleName());

    public ShuffleStatePower(AbstractCreature owner) {
        super(POWER_ID, owner);
        this.priority = 2;
        this.amount = -1;
        
        updateDescription();
    }

    @Override
    public void onShuffle() {
        List<AbstractCard> cards = CardLibrary.getAllCards().stream().filter((c) -> c.type == AbstractCard.CardType.STATUS).collect(Collectors.toList());
        if (!cards.isEmpty()) {
            AbstractCard card = GeneralUtil.getRandomElement(cards, AbstractDungeon.cardRandomRng);
            addToBot(new MakeTempCardInDrawPileAction(card.makeCopy(), 1, true, true));
        }
    }
}
