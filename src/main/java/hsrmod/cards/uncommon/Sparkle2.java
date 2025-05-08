package hsrmod.cards.uncommon;

import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.List;
import java.util.function.Predicate;

public class Sparkle2 extends BaseCard {
    public static final String ID = Sparkle2.class.getSimpleName();
    
    public Sparkle2() {
        super(ID);
        setBaseEnergyCost(110);
        tags.add(CustomEnums.ENERGY_COSTING);
        exhaust = true;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Sparkle"));
        int x = energyOnUse + (p.hasRelic("Chemical X") ? 2 : 0);
        int num = Math.min(x * 2, p.energy.energy * 2 + magicNumber);

        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                List<ModHelper.FindResult> sparkles = ModHelper.findCards(new Predicate<AbstractCard>() {
                    @Override
                    public boolean test(AbstractCard c) {
                        return c instanceof Sparkle2 && c.uuid != uuid;
                    }
                });
                if (!sparkles.isEmpty()) {
                    Sparkle2.this.addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
                } else {
                    CardCrawlGame.sound.play(ID);
                    Sparkle2.this.addToTop(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[1], 1.0F, 2.0F));
                }
                for (ModHelper.FindResult result : sparkles) {
                    result.group.removeCard(result.card);
                    for (AbstractCard abstractCard : p.masterDeck.group) {
                        if (abstractCard.uuid == result.card.uuid) {
                            p.masterDeck.removeCard(abstractCard);
                            break;
                        }
                    }
                }
            }
        });
        addToBot(new GainEnergyAction(num));
        addToBot(new LoseEnergyAction(EnergyPanel.totalCount));
    }
}
