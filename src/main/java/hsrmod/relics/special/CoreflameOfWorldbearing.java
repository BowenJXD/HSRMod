package hsrmod.relics.special;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.cardsV2.Trailblaze.Phainon1;
import hsrmod.cardsV2.Trailblaze.Phainon2;
import hsrmod.characters.StellaCharacter;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.BaseRelic;

import java.util.Objects;

public class CoreflameOfWorldbearing extends BaseRelic implements ClickableRelic {
    public static final String ID = CoreflameOfWorldbearing.class.getSimpleName();

    public CoreflameOfWorldbearing() {
        super(ID);
        try {
            Prefs stellaPref = CardCrawlGame.characterManager.getCharacter(StellaCharacter.PlayerColorEnum.STELLA_CHARACTER).getPrefs();
            counter = stellaPref.getInteger("coreflameCount", 0);
        } catch (Exception e) {
            HSRMod.logger.error("Error while loading path", e);
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
        try {
            Prefs stellaPref = CardCrawlGame.characterManager.getCharacter(StellaCharacter.PlayerColorEnum.STELLA_CHARACTER).getPrefs();
            counter = stellaPref.getInteger("coreflameCount", 0);
        } catch (Exception e) {
            HSRMod.logger.error("Error while loading path", e);
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        try {
            Prefs stellaPref = CardCrawlGame.characterManager.getCharacter(StellaCharacter.PlayerColorEnum.STELLA_CHARACTER).getPrefs();
            counter = stellaPref.getInteger("coreflameCount", 0);
        } catch (Exception e) {
            HSRMod.logger.error("Error while loading path", e);
        }
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        try {
            Prefs stellaPref = CardCrawlGame.characterManager.getCharacter(StellaCharacter.PlayerColorEnum.STELLA_CHARACTER).getPrefs();
            stellaPref.putInteger("coreflameCount", counter);
        } catch (Exception e) {
            HSRMod.logger.error("Error while loading path", e);
        }
    }
    
    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (Objects.equals(c.cardID, HSRMod.makePath(Phainon1.ID))) {
            setCounter(counter + 2);
        }
    }

    @Override
    public void onExhaust(AbstractCard card) {
        super.onExhaust(card);
        if (card != null && card.hasTag(CustomEnums.CHRYSOS_HEIR)) {
            setCounter(counter + 1);
        }
    }

    @Override
    public void onRightClick() {
        if (counter >= 12) {
            setCounter(counter - 12);
            flash();
            addToBot(new MakeTempCardInHandAction(new Phainon2()));
        }
    }
    
    public static void addFlame(int amount) {
        try {
            Prefs stellaPref = CardCrawlGame.characterManager.getCharacter(StellaCharacter.PlayerColorEnum.STELLA_CHARACTER).getPrefs();
            int count = stellaPref.getInteger("coreflameCount", 0);
            stellaPref.putInteger("coreflameCount", count + amount);
            if (AbstractDungeon.player.hasRelic(HSRMod.makePath(ID))) {
                AbstractRelic relic = AbstractDungeon.player.getRelic(HSRMod.makePath(ID)); 
                if (relic != null) relic.setCounter(count + amount);
            }
        } catch (Exception e) {
            HSRMod.logger.error("Error while loading path", e);
        }
    }
}
