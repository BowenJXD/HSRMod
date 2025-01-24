/*
package hsrmod.patches;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import hsrmod.characters.StellaCharacter;
import hsrmod.events.StelleAwakeEvent;

import java.lang.reflect.Field;

public interface StelleAwakeWithNeow {
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
        public static SpireReturn<Void> Prefix(AbstractEvent e, int buttonPressed) {
            if (AbstractDungeon.player.chosenClass == StellaCharacter.PlayerColorEnum.STELLA_CHARACTER
                    && (BaseMod.hasModID("spireTogether:") || Settings.isTrial)) {
                try {
                    Field screenNumField = NeowEvent.class.getDeclaredField("screenNum");
                    //false有初始捏奥选项，true没有
                    screenNumField.setAccessible(true);
                    int screenNum = screenNumField.getInt(e);
                    if (screenNum == 2 || screenNum == 99) {
                        test();
                        return SpireReturn.Return();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return SpireReturn.Continue();
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
                node.setRoom(new PathSelectEventRoom());
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

    class PathSelectEventRoom extends EventRoom {
        @Override
        public void onPlayerEntry() {
            AbstractDungeon.overlayMenu.proceedButton.hide();
            event = new StelleAwakeEvent();
            event.onEnterRoom();
        }
    }
}
*/
