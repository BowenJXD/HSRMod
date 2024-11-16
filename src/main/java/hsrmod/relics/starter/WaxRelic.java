package hsrmod.relics.starter;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.modcore.Path;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.RewardEditor;

import java.util.List;

public abstract class WaxRelic extends BaseRelic {
    public static int defaultWeight = 1;
    protected int weight;
    public AbstractCard.CardTags tag;
    
    public WaxRelic(String id, AbstractCard.CardTags tag, int weight){
        super(id);
        this.tag = tag;
        this.weight = weight;
    }
    
    public WaxRelic (String id, AbstractCard.CardTags tag){
        this(id, tag, defaultWeight);
    }

    @Override
    public void update() {
        super.update();
        if (!isObtained) return;
        RewardEditor.getInstance().update(AbstractDungeon.getCurrRoom(), tag);
    }

    public static AbstractCard.CardTags getSelectedPathTag(List<AbstractRelic> relics) {
        for (AbstractRelic r : relics) {
            if (r instanceof WaxRelic) {
                return ((WaxRelic) r).tag;
            }
        }
        return null;
    }
    
    public static WaxRelic getWaxRelic(List<AbstractRelic> relics, AbstractCard.CardTags tag) {
        for (AbstractRelic r : relics) {
            if (r instanceof WaxRelic && ((WaxRelic) r).tag == tag) {
                return (WaxRelic) r.makeCopy();
            }
        }
        return null;
    }
}
