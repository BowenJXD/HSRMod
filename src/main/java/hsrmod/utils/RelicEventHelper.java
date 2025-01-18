package hsrmod.utils;

import basemod.devcommands.relic.Relic;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.RelicAboveCreatureEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import hsrmod.effects.BetterWarningSignEffect;
import hsrmod.relics.common.IRubertEmpireRelic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RelicEventHelper {
    public static void upgradeCards(int amount) {
        int count = 0;
        List<String> upgradedCards = new ArrayList<>();
        List<AbstractCard> list = AbstractDungeon.player.masterDeck.group;
        Collections.shuffle(list, AbstractDungeon.relicRng.random);

        for (AbstractCard c : list) {
            if (c.canUpgrade() && !upgradedCards.contains(c.cardID)) {
                upgradedCards.add(c.cardID);
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                
                ++count;
                if (count <= 20) {
                    float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                    float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                }

                if (count >= amount) {
                    break;
                }
            }
        }
    }
    
    public static void upgradeCards(AbstractCard... card) {
        for (AbstractCard c : card) {
            if (c.canUpgrade()) {
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
            }
        }
    }
    
    public static void gainRelicsAfterwards(int amount) {
        ModHelper.addEffectAbstract(() -> gainRelics(amount));
    }
    
    public static void gainRelics(int amount){
        for (int i = 0; i < amount; ++i) {
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());
            if (r instanceof IRubertEmpireRelic)
                --i;
            else
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), r);
        }
    }
    
    public static void gainRelics(int amount, AbstractRelic.RelicTier tier) {
        for (int i = 0; i < amount; ++i) {
            AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(tier);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), r);
        }
    }
    
    public static void gainRelics(String... relicIDs) {
        for (String relicId : relicIDs) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), RelicLibrary.getRelic(relicId).makeCopy());
        }
    }
    
    /*public static void loseRelics(String... relicIDs) {
        loseRelics(Arrays.stream(relicIDs).map(RelicLibrary::getRelic).map(AbstractRelic::makeCopy).toArray(AbstractRelic[]::new));
    }*/
    
    public static void loseRelics(AbstractRelic... relics) {
        if (relics.length == 0) return;
        if (relics.length == 1) {
            AbstractDungeon.effectList.add(new BetterWarningSignEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f, 4.0f));
            AbstractDungeon.player.loseRelic(relics[0].relicId);
            AbstractDungeon.effectList.add(new RelicAboveCreatureEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.4f, relics[0]));
        } else {
            for (int i = relics.length - 1; i >= 0; --i) {
                float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                AbstractDungeon.effectList.add(new BetterWarningSignEffect(x, y, 4.0f));
                AbstractDungeon.player.loseRelic(relics[i].relicId);
                AbstractDungeon.effectList.add(new RelicAboveCreatureEffect(x, y - 0.1f, relics[i]));
            }
        }
    }
    
    public static void gainGold(int amount) {
        AbstractDungeon.effectList.add(new RainingGoldEffect(amount));
        AbstractDungeon.player.gainGold(amount);
    }
}
