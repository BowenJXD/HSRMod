package androidTestMod.cardsV2;

import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.utils.CardDataCol;
import androidTestMod.utils.DataManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.Consumer;

public class Sushang3 extends BaseCard {
    public static final String ID = Sushang3.class.getSimpleName();

    public Sushang3() {
        super(ID);
    }

    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        upgradeTr(1);
        this.timesUpgraded++;
        this.upgraded = true;
        this.name = DataManager.getInstance().getCardData(ID, CardDataCol.Name) + "+" + this.timesUpgraded;
        initializeTitle();
        this.initializeDescription();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                new Consumer<ElementalDamageAction.CallbackInfo>() {
                    @Override
                    public void accept(ElementalDamageAction.CallbackInfo ci) {
                        if (ci.didBreak) {
                            Sushang3.this.addToBot(new ExhaustSpecificCardAction(Sushang3.this, p.discardPile));
                            Sushang3.this.addToBot(new ExhaustSpecificCardAction(Sushang3.this, p.drawPile));
                            Sushang3.this.addToTop(new UpgradeSpecificCardAction(Sushang3.this));
                            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                                if (c.uuid == uuid) {
                                    c.upgrade();
                                    break;
                                }
                            }
                        }
                    }
                }
        ));
    }
}
