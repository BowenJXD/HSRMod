package hsrmod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RelicAboveCreatureEffect;
import hsrmod.effects.BetterWarningSignEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.patches.RelicTagField;
import hsrmod.utils.DataManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicDataCol;

public abstract class BaseRelic extends CustomRelic {
    public int magicNumber;
    public String modNameCache = null;
    public boolean hsrOnly = false;
    
    public BaseRelic(String id){
        super(HSRMod.makePath(id),
                ImageMaster.loadImage("HSRModResources/img/relics/" + id + ".png"),
                ImageMaster.loadImage("HSRModResources/img/relics/outline/" + id + ".png"),
                AbstractRelic.RelicTier.valueOf(DataManager.getInstance().getRelicData(id, RelicDataCol.Tier)),
                AbstractRelic.LandingSound.valueOf(DataManager.getInstance().getRelicData(id, RelicDataCol.Sound))
                );
        magicNumber = DataManager.getInstance().getRelicDataInt(id, RelicDataCol.MagicNumber);
        RelicTagField.destructible.set(this, DataManager.getInstance().getRelicDataBoolean(id, RelicDataCol.Destructible));
        RelicTagField.subtle.set(this, DataManager.getInstance().getRelicDataBoolean(id, RelicDataCol.Subtle));
        hsrOnly = DataManager.getInstance().getRelicDataBoolean(id, RelicDataCol.Special);
    }
    
    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        if (counter <= -2) {
            this.usedUp();
        }
    }

    @Override
    public void flash() {
        if (!AbstractDungeon.player.relics.contains(this)) {
            AbstractRelic relic = AbstractDungeon.player.getRelic(relicId);
            if (relic != null) {
                relic.flash();
            }
        } else {
            super.flash();
        }
    }

    public boolean reduceCounterAndCheckDestroy() {
        boolean result = false;
        AbstractRelic relic = this;
        if (!AbstractDungeon.player.relics.contains(this)) {
            relic = AbstractDungeon.player.getRelic(relicId);
        }
        if (relic != null) {
            relic.setCounter(relic.counter - 1);
            if (relic.counter <= 0) {
                relic.setCounter(-2);
                AbstractDungeon.topLevelEffectsQueue.add(new RelicAboveCreatureEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.6f, relic));
                AbstractDungeon.topLevelEffectsQueue.add(new BetterWarningSignEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.7f, 4.0f));
                result = true;
            }
        }
        return result;
    }
    
    protected void destroy(){
        AbstractRelic relic = this;
        if (!AbstractDungeon.player.relics.contains(this)) {
            relic = AbstractDungeon.player.getRelic(relicId);
        }
        if (relic != null) {
            relic.setCounter(-2);
            AbstractDungeon.topLevelEffectsQueue.add(new RelicAboveCreatureEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.6f, relic));
            AbstractDungeon.topLevelEffectsQueue.add(new BetterWarningSignEffect(Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.7f, 4.0f));
        }
/*        this.description = "该遗物已损毁。";
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();*/
    }
    
    public void recover() {
        this.grayscale = false;
        this.usedUp = false;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
    
    @Override
    protected void initializeTips() {
        if (modNameCache == null) {
            modNameCache = tips.get(0).body;
        }
        super.initializeTips();
    }
    
    public void updateDescription(String newDescription){
        description = newDescription;
        tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public boolean canSpawn() {
        return !ModHelper.hasRelic(this.relicId);
    }
}
