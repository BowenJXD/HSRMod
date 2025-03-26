package hsrmod.cards.uncommon;

import com.codedisaster.steamworks.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
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
import me.antileaf.signature.utils.SignatureHelper;

public class SilverWolf1 extends BaseCard {
    public static final String ID = SilverWolf1.class.getSimpleName();
    private static SteamUserStats steamUserStats;

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
    
    public static void checkUnlockSign(){
        steamUserStats = new SteamUserStats(new SteamUserStatsCallback() {
            @Override
            public void onUserStatsReceived(long l, SteamID steamID, SteamResult steamResult) {
                HSRMod.logger.info("onUserStatsReceived: {} {} {}", l, steamID, steamResult);

                int result = steamUserStats.getStatI("total_time_played", -1);
                if (result > 76) {
                    SignatureHelper.unlock(HSRMod.makePath(SilverWolf1.ID), true);
                }
                int amt = steamUserStats.getNumAchievements();
                HSRMod.logger.info("Achievements: {}", amt);
            }

            @Override
            public void onUserStatsStored(long l, SteamResult steamResult) {}

            @Override
            public void onUserStatsUnloaded(SteamID steamID) {}

            @Override
            public void onUserAchievementStored(long l, boolean b, String s, int i, int i1) {}

            @Override
            public void onLeaderboardFindResult(SteamLeaderboardHandle steamLeaderboardHandle, boolean b) {}

            @Override
            public void onLeaderboardScoresDownloaded(SteamLeaderboardHandle steamLeaderboardHandle, SteamLeaderboardEntriesHandle steamLeaderboardEntriesHandle, int i) {}

            @Override
            public void onLeaderboardScoreUploaded(boolean b, SteamLeaderboardHandle steamLeaderboardHandle, int i, boolean b1, int i1, int i2) {}

            @Override
            public void onNumberOfCurrentPlayersReceived(boolean b, int i) {}

            @Override
            public void onGlobalStatsReceived(long l, SteamResult steamResult) {
                HSRMod.logger.info("onGlobalStatsReceived: " + l + " " + steamResult);

                int result = steamUserStats.getStatI("total_time_played", -1);
                if (result > 76) {
                    SignatureHelper.unlock(HSRMod.makePath(SilverWolf1.ID), true);
                }
            }
        });
        steamUserStats.requestCurrentStats();
        steamUserStats.requestGlobalStats(3);
    }
}
