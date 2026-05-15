package hsrmod.cardsV2.Trailblaze;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.CleanAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.actions.GridCardManipulator;
import hsrmod.actions.SimpleGridCardSelectBuilder;
import hsrmod.cards.BaseCard;
import hsrmod.cardsV2.Abundance.Mydei1;
import hsrmod.cardsV2.Elation.Tribbie2;
import hsrmod.cardsV2.Nihility.Cipher1;
import hsrmod.cardsV2.Nihility.Hysilens1;
import hsrmod.cardsV2.Preservation.PermansorTerrae1;
import hsrmod.cardsV2.Propagation.Anaxa1;
import hsrmod.cardsV2.Propagation.Cerydra1;
import hsrmod.cardsV2.Remembrance.*;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.modifiers.*;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Demiurge3 extends BaseCard {
    public static final String ID = Demiurge3.class.getSimpleName();

    List<String> cachedCardIds = new ArrayList<>(Arrays.asList("Aglaea", "Tribbie", "Mydei", "Castorice", "Anaxa", "Hyacine", "Cipher", "Phainon", "Hysilens", "Cerydra", "Evernight", "PermansorTerrae"));
    
    public Demiurge3() {
        super(ID);
        purgeOnUse = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0,4);
        addToBot(new CleanAction(p, 1, false));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1)));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.hasPower(StrengthPower.POWER_ID)) {
                addToBot(new ApplyPowerAction(monster, p, new StrengthPower(monster, -1)));
            }
        }
        addToBot(new HealAction(p, p, magicNumber));
        
        addToBot(new SimpleGridCardSelectBuilder(c -> true)
                .setCardGroup(p.drawPile, p.discardPile, p.exhaustPile, p.hand)
                .setAmount(1)
                .setAnyNumber(false)
                .setCanCancel(true)
                .setMsg(cardStrings.EXTENDED_DESCRIPTION[5])
                .setManipulator(new GridCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int index, CardGroup group) {
                        if (card.type == CardType.CURSE || card.type == CardType.STATUS) {
                            group.removeCard(card);
                            return false;
                        }
                        card.upgrade();
                        card.exhaust = false;
                        if (card.hasTag(CustomEnums.CHRYSOS_HEIR)) {
                            Cyrene4.ChrysosHeirBuff(card);
                            cachedCardIds.stream().filter(id -> card.cardID.contains(id)).findFirst().ifPresent(id -> {
                                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + id, 3.0f));
                            });
                        }
                        moveToHand(card, group);
                        return false;
                    }
                })
        );
        /*GeneralUtil.getRandomElements(p.exhaustPile.group, AbstractDungeon.cardRandomRng, magicNumber)
                .forEach(c -> addToTop(new ExhaustToHandAction(c)));*/

    }
}
