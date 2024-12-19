package hsrmod.patches;

import basemod.BaseMod;
import basemod.eventUtil.EventUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
import hsrmod.characters.StellaCharacter;
import hsrmod.events.StelleAwakeEvent;
import hsrmod.modcore.HSRMod;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;

public interface StelleAwakeWithNeo {
    public static class ForceBlessing {
        @SpireInsertPatch(rloc = 1)
        public static void Insert(Object o, boolean b) {
            Settings.isTestingNeow = true;
        }
    }

    @SpirePatch(clz = NeowRoom.class, method = "<ctor>", paramtypez = {boolean.class})
    public static class AddBetterRewardsButton {
        @SpirePostfixPatch
        public static void Postfix(NeowRoom room, boolean b) {
            boolean isNeowDone = b;
            if (!b && AbstractDungeon.player.chosenClass == StellaCharacter.PlayerColorEnum.STELLA_CHARACTER
                    && (BaseMod.hasModID("spireTogether:") || Settings.isTrial)) {
                room.event.roomEventText.clear();
                if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT)
                    room.event.roomEventText.addDialogOption(" #b“喂……醒醒……醒醒……” ");
                else
                    room.event.roomEventText.addDialogOption(" “Hey... Wake Up...” ");
            }
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "<ctor>", paramtypez = {boolean.class})
    public static class FixEventImage {
        @SpirePostfixPatch
        public static void Postfix(NeowEvent e, boolean b) {
            e.imageEventText.clear();
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "buttonEffect")
    public static class MaybeStartRewards {
        @SpirePrefixPatch
        public static void Prefix(AbstractEvent e, int buttonPressed) {
            if (AbstractDungeon.player.chosenClass == StellaCharacter.PlayerColorEnum.STELLA_CHARACTER
                    && (BaseMod.hasModID("spireTogether:") || Settings.isTrial)) {
                try {
                    Field screenNumField = NeowEvent.class.getDeclaredField("screenNum");
                    //false有初始捏奥选项，true没有
                    screenNumField.setAccessible(false);

                    test();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void test() {
        AbstractDungeon.effectList.add(new AbstractGameEffect() {
            @Override
            public void update() {
                isDone = true;
                RoomEventDialog.optionList.clear();
                MapRoomNode currNode = AbstractDungeon.getCurrMapNode();
                MapRoomNode node = new MapRoomNode(currNode.x, currNode.y);
                node.setRoom(new EventRoom() {
                    @Override
                    public void onPlayerEntry() {
                        AbstractDungeon.overlayMenu.proceedButton.hide();
                        event = new StelleAwakeEvent();
                        event.onEnterRoom();
                    }
                });
                for (MapEdge e : currNode.getEdges()) {
                    node.addEdge(e);
                }
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.setCurrMapNode(node);
                AbstractDungeon.getCurrRoom().onPlayerEntry();
                AbstractDungeon.scene.nextRoom(node.room);
                AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;

                //关闭气泡
                for (int i = AbstractDungeon.effectList.size() - 1; i >= 0; i--) {
                    if (AbstractDungeon.effectList.get(i) instanceof InfiniteSpeechBubble) {
                        ((InfiniteSpeechBubble) AbstractDungeon.effectList.get(i)).dismiss();
                        // AbstractDungeon.effectList.remove(i);
                    }
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        });
    }
}
