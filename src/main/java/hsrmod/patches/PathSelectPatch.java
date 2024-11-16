package hsrmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import hsrmod.characters.StellaCharacter;

public class PathSelectPatch {
    public PathSelectPatch() {
    }

    public static boolean isSelected() {
        return CardCrawlGame.chosenCharacter == StellaCharacter.PlayerColorEnum.STELLA_CHARACTER && (Boolean)ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen, CharacterSelectScreen.class, "anySelected");
    }

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "render"
    )
    public static class RenderButtonPatch {
        public RenderButtonPatch() {
        }

        public static void Postfix(CharacterSelectScreen _inst, SpriteBatch sb) {
            if (PathSelectPatch.isSelected()) {
                PathSelectScreen.Inst.render(sb);
            }

        }
    }

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "update"
    )
    public static class UpdateButtonPatch {
        public UpdateButtonPatch() {
        }

        public static void Prefix(CharacterSelectScreen _inst) {
            if (PathSelectPatch.isSelected()) {
                PathSelectScreen.Inst.update();
            }

        }
    }
}

