package hsrmod.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.cards.BaseCard;
import hsrmod.utils.SubscribeManager;

import java.util.Iterator;

public class Anicca extends BaseCard {
    public static final String ID = Anicca.class.getSimpleName();

    public Anicca() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, magicNumber), magicNumber));
        if (upgraded) addToBot(new MoveCardsAction(p.exhaustPile, p.hand, card -> card.type == CardType.CURSE));
    }
}
