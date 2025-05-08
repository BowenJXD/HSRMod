package hsrmod.relics.rare;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.List;

public class ByAnyMeansNecessary extends BaseRelic {
    public static final String ID = ByAnyMeansNecessary.class.getSimpleName();
    
    public List<AbstractRelic> cachedRelics = new ArrayList<>();
    
    boolean isEquipped = false;

    public ByAnyMeansNecessary() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        cachedRelics = new ArrayList<>(AbstractDungeon.player.relics);
        isEquipped = true;
        updateCounter();
    }

    @Override
    public void onUnequip() {
        super.onUnequip();
        isEquipped = false;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        updateCounter();
        AbstractMonster monster = ModHelper.getMonsterWithMaxHealth();
        if (monster != null) {
            addToBot(new DamageAction(monster, new DamageInfo(AbstractDungeon.player, counter)));
        }
    }

    @Override
    public void update() {
        super.update();
        if (!isEquipped) return;
        for (int i = AbstractDungeon.player.relics.size() - 1; i >= cachedRelics.size(); i--) {
            if (!cachedRelics.contains(AbstractDungeon.player.relics.get(i))) {
                receiveRelicGet(AbstractDungeon.player.relics.get(i));
            }
        }
        cachedRelics = new ArrayList<>(AbstractDungeon.player.relics);
    }

    public void receiveRelicGet(AbstractRelic abstractRelic) {
        if (SubscriptionManager.checkSubscriber(this)) {
            int goldGain = 0;
            switch (abstractRelic.tier) {
                case RARE:
                    goldGain = magicNumber * 3;
                    break;
                case UNCOMMON:
                    goldGain = magicNumber * 2;
                    break;
                case COMMON:
                    goldGain = magicNumber;
                    break;
            }
            if (goldGain > 0) {
                flash();
                AbstractDungeon.player.gainGold(goldGain);
            }
            ModHelper.addEffectAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    ByAnyMeansNecessary.this.updateCounter();
                }
            });
        }
    }

    void updateCounter() {
        int counter = 0;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            switch (relic.tier) {
                case RARE:
                    counter += 4;
                    break;
                case UNCOMMON:
                    counter += 2;
                    break;
                default:
                    counter += 1;
                    break;
            }
        }
        setCounter(counter);
    }
}
