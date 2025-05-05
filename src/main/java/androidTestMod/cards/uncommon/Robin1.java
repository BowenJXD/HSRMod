package androidTestMod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.utils.ModHelper;

import static androidTestMod.modcore.CustomEnums.FOLLOW_UP;

public class Robin1 extends BaseCard {
    public static final String ID = Robin1.class.getSimpleName();

    public Robin1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int amt = energyOnUse + (p.hasRelic("Chemical X") ? 2 : 0);
        addToBot(new DrawCardAction(p, amt));

        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                int amount = amt;
                int count = 0;

                for (AbstractCard card : p.hand.group) {
                    if (!upgraded || !card.hasTag(FOLLOW_UP)) {
                        amount--;
                    }

                    if (amount < 0) break;
                    Robin1.this.addToBot(new FollowUpAction(card));
                    count++;
                }

                p.energy.use(EnergyPanel.totalCount);
            }
        });
    }
}
