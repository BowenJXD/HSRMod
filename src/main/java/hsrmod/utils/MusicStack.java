package hsrmod.utils;

import basemod.BaseMod;
import basemod.interfaces.OnStartBattleSubscriber;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.monsters.BaseMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MusicStack {
    private static MusicStack instance;
    
    List<String> stack;
    
    private MusicStack() {
        stack = new ArrayList<>();
    }
    
    public static MusicStack getInstance() {
        if (instance == null) {
            instance = new MusicStack();
        }
        return instance;
    }
    
    public void justPush(String musicKey) {
        stack.add(musicKey);
    }
    
    public void push(String musicKey) {
        stack.add(musicKey);

        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.silenceBGM();
        CardCrawlGame.music.justFadeOutTempBGM();
        CardCrawlGame.music.playTempBgmInstantly(musicKey);
    }
    
    public void remove(String musicKey) {
        // stack.remove(musicKey);
        
        if (!stack.isEmpty() && Objects.equals(musicKey, stack.get(stack.size()-1))) {
            stack.remove(musicKey);
            if (!stack.isEmpty()) {
                CardCrawlGame.music.justFadeOutTempBGM();
                CardCrawlGame.music.playTempBGM(stack.get(stack.size()-1));
            } else {
                CardCrawlGame.music.fadeOutTempBGM();
                AbstractDungeon.scene.fadeInAmbiance();
            }
        } else {
            stack.remove(musicKey);
        }
    }
    
    public void clear() {
        stack.clear();
        CardCrawlGame.music.fadeOutTempBGM();
        AbstractDungeon.scene.fadeInAmbiance();
    }
}
