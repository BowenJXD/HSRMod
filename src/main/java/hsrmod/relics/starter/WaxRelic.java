package hsrmod.relics.starter;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.CardRewardPoolEditor;

import java.util.function.Predicate;

public abstract class WaxRelic extends BaseRelic {
    public static int defaultWeight = 1;
    protected int weight;
    protected AbstractCard.CardTags tag;
    
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
        if (!available) return;
        CardRewardPoolEditor.getInstance().update(AbstractDungeon.getCurrRoom(), tag);
    }
}
