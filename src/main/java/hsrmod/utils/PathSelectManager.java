package hsrmod.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.android.mods.interfaces.PostRenderSubscriber;
import com.megacrit.cardcrawl.android.mods.interfaces.PostUpdateSubscriber;
import com.megacrit.cardcrawl.android.mods.utils.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import hsrmod.modcore.PlayerColorEnum;

import java.util.Objects;

public class PathSelectManager implements PostUpdateSubscriber, PostRenderSubscriber {
    private PathSelectManager() {
        
    }
    
    public static PathSelectManager Inst = new PathSelectManager();
    
    @Override
    public void receivePostRender(SpriteBatch spriteBatch) {
        if (CardCrawlGame.mainMenuScreen != null 
                && CardCrawlGame.mainMenuScreen.charSelectScreen != null
                && CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.CHAR_SELECT
                && CardCrawlGame.chosenCharacter == PlayerColorEnum.STELLA_CHARACTER
                && Objects.equals(CardCrawlGame.mode.name, CardCrawlGame.GameMode.CHAR_SELECT.name)
                && (Boolean) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen, CharacterSelectScreen.class, "anySelected")) {
            PathSelectScreen.Inst.render(spriteBatch);
        }
    }

    @Override
    public void receivePostUpdate() {
        if (CardCrawlGame.mainMenuScreen != null
                && CardCrawlGame.mainMenuScreen.charSelectScreen != null
                && CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.CHAR_SELECT
                && CardCrawlGame.chosenCharacter == PlayerColorEnum.STELLA_CHARACTER
                && Objects.equals(CardCrawlGame.mode.name, CardCrawlGame.GameMode.CHAR_SELECT.name)
                && (Boolean) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen, CharacterSelectScreen.class, "anySelected")) {
            PathSelectScreen.Inst.update();
        }
    }
}
