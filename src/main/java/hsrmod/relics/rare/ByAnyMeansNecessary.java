package hsrmod.relics.rare;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.RelicGetSubscriber;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class ByAnyMeansNecessary extends BaseRelic implements RelicGetSubscriber, CustomSavable<Void> {
    public static final String ID = ByAnyMeansNecessary.class.getSimpleName();

    public ByAnyMeansNecessary() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        BaseMod.subscribe(this);
        updateCounter();
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
            updateCounter();
        }
    }
    
    void updateCounter() {
        setCounter(AbstractDungeon.player.relics.stream().mapToInt(r -> {
            switch (r.tier) {
                case RARE:
                    return 4;
                case UNCOMMON:
                    return 2;
                default:
                    return 1;
            }
        }).sum());
    }

    @Override
    public Void onSave() {
        return null;
    }

    @Override
    public void onLoad(Void unused) {
        BaseMod.subscribe(this);
    }
}
