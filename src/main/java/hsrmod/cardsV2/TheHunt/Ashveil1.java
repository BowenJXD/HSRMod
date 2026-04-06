package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;

import java.util.List;
import java.util.stream.Collectors;

public class Ashveil1 extends BaseCard {
    public static final String ID = Ashveil1.class.getSimpleName();
    
    public Ashveil1() {
        super(ID);
        setBaseEnergyCost(150);
        tags.add(CustomEnums.ENERGY_COSTING);
        cardsToPreview = new Arrows();
    }

    @Override
    public void upgrade() {
        super.upgrade();
        cardsToPreview.upgrade();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        List<Integer> cardIndexes = p.hand.group.stream().filter(c -> c.type == CardType.STATUS)
                .map(c -> p.hand.group.indexOf(c)).collect(Collectors.toList());

        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        for (Integer card : cardIndexes) {
            addToBot(new TransformCardInHandAction(card, new Arrows()));
            addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        }
    }
}
