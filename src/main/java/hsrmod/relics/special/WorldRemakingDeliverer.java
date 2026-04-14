package hsrmod.relics.special;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import hsrmod.cards.base.March7th0;
import hsrmod.cards.base.Welt0;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.List;

public class WorldRemakingDeliverer extends BaseRelic {
    public static final String ID = WorldRemakingDeliverer.class.getSimpleName();
    
    List<String> triggeredCardNames;

    public WorldRemakingDeliverer() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        triggeredCardNames = new ArrayList<>();
        CardBorderGlowManager.removeGlowInfo(ID);
        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard c) {
                return c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) && !triggeredCardNames.contains(c.cardID);
            }

            @Override
            public Color getColor(AbstractCard abstractCard) {
                return Color.GOLD;
            }

            @Override
            public String glowID() {
                return ID;
            }
        });
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE) && !triggeredCardNames.contains(c.cardID)) {
            AbstractOrb orb = null;
            if (c instanceof March7th0) {
                orb = new Frost();
            } else if (c instanceof Welt0) {
                orb = new Dark();
            } else {
                orb = AbstractOrb.getRandomOrb(true);
            }
            
            addToBot(new ChannelAction(orb));
            triggeredCardNames.add(c.cardID);
        }
    }
}
