package hsrmod.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.CardDataCol;
import hsrmod.utils.DataManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicDataCol;

import java.lang.reflect.Type;

public abstract class BaseRelic extends CustomRelic {
    public int magicNumber;
    
    public BaseRelic(String id){
        super(HSRMod.makePath(id),
                ImageMaster.loadImage("HSRModResources/img/relics/" + id + ".png"),
                ImageMaster.loadImage("HSRModResources/img/relics/outline/" + id + ".png"),
                AbstractRelic.RelicTier.valueOf(DataManager.getInstance().getRelicData(id, RelicDataCol.Tier)),
                AbstractRelic.LandingSound.valueOf(DataManager.getInstance().getRelicData(id, RelicDataCol.Sound))
                );
        magicNumber = DataManager.getInstance().getRelicDataInt(id, RelicDataCol.MagicNumber);
    }
    
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    
    protected void destroy(){
        this.setCounter(-2);
        this.description = "该遗物已损毁。";
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
}
