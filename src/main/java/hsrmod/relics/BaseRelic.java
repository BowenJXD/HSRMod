package hsrmod.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.CardDataCol;
import hsrmod.utils.DataManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicDataCol;

import java.lang.reflect.Type;

public abstract class BaseRelic extends CustomRelic implements CustomSavable<Boolean> {
    public int magicNumber;
    
    public boolean available = false;
    
    public BaseRelic(String id){
        super(HSRMod.makePath(id),
                ImageMaster.loadImage("HSRModResources/img/relics/" + id + ".png"),
                AbstractRelic.RelicTier.valueOf(DataManager.getInstance().getRelicData(id, RelicDataCol.Tier)),
                AbstractRelic.LandingSound.valueOf(DataManager.getInstance().getRelicData(id, RelicDataCol.Sound))
                );
        magicNumber = DataManager.getInstance().getRelicDataInt(id, RelicDataCol.MagicNumber);
    }

    @Override
    public void onEquip() {
        available = true;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onUnequip() {
        available = false;
    }

    @Override
    public Boolean onSave() {
        return available;
    }

    @Override
    public void onLoad(Boolean aBoolean) {
        available = aBoolean;
    }
    
    @Override
    public Type savedType()
    {
        return new TypeToken<Boolean>(){}.getType();
    }
}
