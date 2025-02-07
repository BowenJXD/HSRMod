package hsrmod.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Aventurine2 extends BaseCard {
    public static final String ID = Aventurine2.class.getSimpleName();
    
    public Aventurine2() {
        super(ID);
        tags.add(FOLLOW_UP);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new FastingEffect(p.hb.cX, p.hb.cY, Color.YELLOW)));
        for (int i = 0; i < magicNumber; i++) {
            int blockThisTurn = AbstractDungeon.cardRandomRng.random(1, block);
            addToBot(new GainBlockAction(p, p, blockThisTurn));
        }
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        if (!AbstractDungeon.player.hand.contains(this)) return;
        if (!followedUp && c.baseBlock > 0 && c.block > 0) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
