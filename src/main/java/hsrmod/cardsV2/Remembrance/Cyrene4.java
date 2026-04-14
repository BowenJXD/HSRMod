package hsrmod.cardsV2.Remembrance;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.unique.ApotheosisAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.SpotlightEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import hsrmod.cards.BaseCard;
import hsrmod.cardsV2.Abundance.Mydei1;
import hsrmod.cardsV2.Elation.Tribbie2;
import hsrmod.cardsV2.Nihility.Cipher1;
import hsrmod.cardsV2.Nihility.Hysilens1;
import hsrmod.cardsV2.Preservation.PermansorTerrae1;
import hsrmod.cardsV2.Propagation.Anaxa1;
import hsrmod.cardsV2.Propagation.Cerydra1;
import hsrmod.effects.PetalFallingEffect;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.modifiers.*;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.uniqueBuffs.FuturePower;
import hsrmod.utils.CachedCondition;
import hsrmod.utils.ModHelper;

import javax.crypto.Cipher;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

public class Cyrene4 extends BaseCard {
    public static final String ID = Cyrene4.class.getSimpleName();

    public Cyrene4() {
        super(ID);
        exhaust = true;
        cardsToPreview = new Demiurge1();
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName(), 3.0f));
        addToBot(new VFXAction(new SpotlightEffect()));
        addToBot(new VFXAction(new PetalFallingEffect()));
        addToBot(new ApplyPowerAction(p, p, new FuturePower(p, 1)));
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, 1000)));
        
        addToBot(new IncreaseMaxOrbAction(1));
        addToBot(new DrawCardAction(BaseMod.MAX_HAND_SIZE));
        int num = p.energy.energy - energyOnUse;
        if (num > 0) {
            addToBot(new GainEnergyAction(num));
        }
        addToBot(new ApotheosisAction());
        ModHelper.addToBotAbstract(() -> {
            int orbCount = p.maxOrbs - p.filledOrbCount();
            for (int i = orbCount; i > 0; i--) {
                addToBot(new ChannelAction(AbstractOrb.getRandomOrb(true)));
            }
        });
        ModHelper.addToBotAbstract(this::ChrysosHeirBuff);
        ModHelper.addToBotAbstract(() -> {
            ModHelper.addToBotAbstract(() -> {
                ModHelper.findCards(c -> c==this).forEach(r -> {
                    r.group.removeCard(r.card);
                });
            });
        });
    }
    
    HashMap<String, Supplier<AbstractCardModifier>> cardModMap = new HashMap<String, Supplier<AbstractCardModifier>>(){{
        put(Aglaea2.ID, () -> new OdeToRomanceModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.ROMANCE.ordinal()]));
        put(Tribbie2.ID, () -> new OdeToPassageModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.PASSAGE.ordinal()]));
        put(Mydei1.ID, () -> new OdeToStrifeModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.STRIFE.ordinal()]));
        put(Castorice3.ID, () -> new OdeToLifeAndDeathModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.LIFE_AND_DEATH.ordinal()]));
        put(Hyacine1.ID, () -> new OdeToSkyModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.SKY.ordinal()]));
        put(Anaxa1.ID, () -> new OdeToReasonModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.REASON.ordinal()]));
        put(Cipher1.ID, () -> new OdeToTrickeryModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.TRICKERY.ordinal()]));
        put("Phaimon2", () -> new OdeToWorldbearing1Modifier(cardStrings.EXTENDED_DESCRIPTION[Ode.WORLD_BEARING_1.ordinal()]));
        put("Khaslana4", () -> new OdeToWorldbearing2Modifier(cardStrings.EXTENDED_DESCRIPTION[Ode.WORLD_BEARING_2.ordinal()]));
        put(Hysilens1.ID, () -> new OdeToOceanModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.OCEAN.ordinal()]));
        put(Cerydra1.ID, () -> new OdeToLawModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.LAW.ordinal()]));
        put(Evernight1.ID, () -> new OdeToTimeModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.TIME.ordinal()]));
        put(PermansorTerrae1.ID, () -> new OdeToEarthModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.EARTH.ordinal()]));
        put(Cyrene2.ID, () -> new OdeToEgoModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.EGO.ordinal()]));
        put(Trailblazer9.ID, () -> new OdeToGenesisModifier(cardStrings.EXTENDED_DESCRIPTION[Ode.GENESIS.ordinal()]));
    }};
    
    void ChrysosHeirBuff() {
        ModHelper.findCards(c -> {
            return c.hasTag(CustomEnums.CHRYSOS_HEIR);
        }).forEach(r -> {
            AbstractCard c = r.card;
            cardModMap.forEach((id, mod) -> {
                if (Objects.equals(c.cardID, HSRMod.makePath(id))) {
                    CardModifierManager.addModifier(c, mod.get());
                }
            });
        });
    }
    
    enum Ode {
        NONE,
        ROMANCE,
        PASSAGE, 
        STRIFE,
        LIFE_AND_DEATH,
        SKY,
        REASON,
        TRICKERY,
        WORLD_BEARING_1,
        WORLD_BEARING_2,
        OCEAN,
        LAW,
        TIME,
        EARTH,
        EGO,
        GENESIS,
    }
}
