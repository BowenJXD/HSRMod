package hsrmod.cards.uncommon;

import basemod.devcommands.ConsoleCommand;
import basemod.devcommands.deck.DeckAdd;
import basemod.devcommands.hand.HandAdd;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.InversionBeamEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.GeneralUtil;

import java.util.Objects;

public class SilverWolf1 extends BaseCard {
    public static final String ID = SilverWolf1.class.getSimpleName();

    public SilverWolf1() {
        super(ID);
        setBaseEnergyCost(110);
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (Settings.FAST_MODE)
            shout(1);
        else
            shout(0);
        if (m != null)
            addToBot(new VFXAction(new InversionBeamEffect(m.hb.cX)));

        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL
        ));
        for (int i = 0; i < magicNumber; i++) {
            switch (AbstractDungeon.cardRandomRng.random(upgraded ? 2 : 1)) {
                case 0:
                    addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
                    break;
                case 1:
                    addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
                    break;
                case 2:
                    addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -1), -1));
                    break;
            }
        }
    }

    @SpirePatch(clz = HandAdd.class, method = "execute")
    public static class HandAddPatch {
        @SpireInsertPatch(rloc = 4, localvars = {"c"})
        public static void Insert(AbstractCard c) {
            if (Objects.equals(c.cardID, HSRMod.makePath(SilverWolf1.ID))) {
                SignatureHelper.unlock(c.cardID, true);
                if (AbstractDungeon.actionManager != null && AbstractDungeon.isPlayerInDungeon())
                    AbstractDungeon.actionManager.addToTop(new TalkAction(true,
                            GeneralUtil.tryFormat(
                                    CardCrawlGame.languagePack.getCardStrings(HSRMod.makePath(ID)).EXTENDED_DESCRIPTION[2],
                                    AbstractDungeon.player.name
                            ),
                            3, 4));
            }
        }
    }

    @SpirePatch(clz = DeckAdd.class, method = "execute")
    public static class DeckAddPatch {
        @SpireInsertPatch(rloc = 4, localvars = {"c"})
        public static void Insert(AbstractCard c) {
            if (Objects.equals(c.cardID, HSRMod.makePath(SilverWolf1.ID))) {
                SignatureHelper.unlock(c.cardID, true);
                if (AbstractDungeon.actionManager != null && AbstractDungeon.isPlayerInDungeon())
                    AbstractDungeon.actionManager.addToTop(new TalkAction(true,
                            GeneralUtil.tryFormat(
                                    CardCrawlGame.languagePack.getCardStrings(HSRMod.makePath(ID)).EXTENDED_DESCRIPTION[3],
                                    AbstractDungeon.player.name
                            ),
                            2, 3));
            }
        }
    }

    @SpirePatch(clz = ConsoleCommand.class, method = "execute", paramtypez = String[].class)
    public static class ConsoleCommandPatch {
        @SpirePrefixPatch
        public static void Prefix() {
            if (SignatureHelper.isUnlocked(HSRMod.makePath(ID))) {
                SignatureHelper.unlock(HSRMod.makePath(ID), false);
                if (AbstractDungeon.actionManager != null && AbstractDungeon.isPlayerInDungeon())
                    AbstractDungeon.actionManager.addToTop(new TalkAction(true,
                            CardCrawlGame.languagePack.getCardStrings(HSRMod.makePath(ID)).EXTENDED_DESCRIPTION[4],
                            2, 3));
            }
        }
    }
}
