package hsrmod.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.MultiShivFreezeEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.enemyOnly.SummonedPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.CardDataCol;
import hsrmod.utils.DataManager;

import java.util.Iterator;

public class Seele1 extends BaseCard {
    public static final String ID = Seele1.class.getSimpleName();

    public Seele1() {
        super(ID);
        setBaseEnergyCost(120);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        upgradeDamage(magicNumber);
        this.timesUpgraded++;
        this.upgraded = true;
        this.name = DataManager.getInstance().getCardData(ID, CardDataCol.Name) + "+" + this.timesUpgraded;
        initializeTitle();
        this.initializeDescription();
        
        if (timesUpgraded == 22) {
            SignatureHelper.unlock(cardID, true);
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        addToBot(new VFXAction(new MultiShivFreezeEffect(Color.PURPLE, Color.BLUE, Color.MAGENTA), 1f));
        AbstractGameAction action = new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                (info) -> {
                    if ((info.target.isDying || info.target.currentHealth <= 0) && !info.target.halfDead) {
                        addToBot(new GainEnergyAction(1));
                        addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.discardPile, card -> card.uuid == this.uuid));
                        
                        if (!info.target.hasPower("Minion") && !info.target.hasPower(SummonedPower.POWER_ID)){
                            Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();

                            AbstractCard c;
                            while (var1.hasNext()) {
                                c = (AbstractCard) var1.next();
                                if (c.uuid == this.uuid) {
                                    c.upgrade();
                                    c.isDamageModified = false;
                                }
                            }

                            for (var1 = GetAllInBattleInstances.get(this.uuid).iterator(); var1.hasNext(); ) {
                                c = (AbstractCard) var1.next();
                                c.upgrade();
                            }
                        }
                    }
                });
        addToBot(action);
    }
}
