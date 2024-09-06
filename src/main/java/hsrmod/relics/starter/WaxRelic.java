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
    protected boolean equipped = false;
    
    public WaxRelic(String id, AbstractCard.CardTags tag, int weight){
        super(id);
        this.tag = tag;
        this.weight = weight;
    }
    
    public WaxRelic (String id, AbstractCard.CardTags tag){
        this(id, tag, defaultWeight);
    }

    @Override
    public void onEquip() {
        CardRewardPoolEditor.getInstance().RegisterWeight(weight, this::hasTag);
        equipped = true;
    }

    @Override
    public void update() {
        super.update();
        if (!equipped) return;
        CardRewardPoolEditor.getInstance().update(AbstractDungeon.getCurrRoom());
    }

    @Override
    public void onUnequip() {
        CardRewardPoolEditor.getInstance().UnregisterWeight(weight, this::hasTag);
        equipped = false;
    }
    
    boolean hasTag(AbstractCard card){
        return card.hasTag(tag);
    }
}
