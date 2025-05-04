package hsrmod.relics.rare;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.RelicGetSubscriber;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

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
    public void onUnequip() {
        super.onUnequip();
        BaseMod.unsubscribe(this);
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
            ModHelper.addEffectAbstract(this::updateCounter);
        }
    }

    @Override
    public void reorganizeObtain(AbstractPlayer p, int slot, boolean callOnEquip, int relicAmount) {
        super.reorganizeObtain(p, slot, callOnEquip, relicAmount);
        BaseMod.unsubscribe(this);
        ModHelper.addEffectAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                BaseMod.subscribe(ByAnyMeansNecessary.this);
            }
        });
        updateCounter();
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

    @Override
    public Void onSave() {
        return null;
    }

    @Override
    public void onLoad(Void unused) {
        BaseMod.subscribe(this);
    }
}
