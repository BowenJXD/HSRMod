package hsrmod.modcore;

import basemod.*;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PreUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.RestartForChangesEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.characters.StellaCharacter;
import hsrmod.effects.TopWarningEffect;
import hsrmod.misc.PathDefine;
import org.scannotation.AnnotationDB;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static com.megacrit.cardcrawl.core.Settings.language;

public class HSRModConfig implements OnStartBattleSubscriber, PostBattleSubscriber, RenderSubscriber, PreUpdateSubscriber {
    private static HSRModConfig instance;

    public static SpireConfig config;
    public static ModInfo info;
    public static boolean addRelic = true;
    public static boolean addEvent = true;
    public static boolean addEnemy = true;

    public static int tpLimit = 0;
    public static boolean tpThorn = false;
    public static boolean tpMalleable = false;
    public static boolean tpRitual = false;
    public static boolean tpSafeguard = false;
    public static boolean tpCurse = false;
    
    public static final int TP_LIMIT_LIMIT = 5;

    public static String[] buttonLanguage;
    public ArrayList<AbstractGameEffect> effects = new ArrayList();

    private HSRModConfig() {
        if (language == Settings.GameLanguage.ZHS || language == Settings.GameLanguage.ZHT)
            buttonLanguage = new String[]{
                    "加入遗物",
                    "加入事件",
                    "加入敌人",
                    "基础设定",
                    "部分设定需要重启并\n重开游戏才能生效。",
                    "  阈值协议⚠", // 5
                    "⚠阈值协议为高难模式，A20以上解锁。\n星满阈值通关时，阈值上限+1。\n勾选阈值协议条目，以激活阈值效果。\n每项条目额外增加怪物20%的生命和10%的韧性。",
                    "怪物初始拥有1层【荆棘】。",
                    "怪物初始拥有1层【柔韧】。",
                    "怪物初始拥有1层【仪式】。",
                    "精英和首领初始拥有1层【守备】。", // 10
                    "精英战开始后，摸牌堆加入1张状态牌；\n首领战开始时，主卡组加入1张诅咒牌。",
                    "⚠当前阈值协议等级：%d / %d 。",
                    "阈值协议已达上限。",
                    "游戏中无法修改阈值协议。",
            };
        else
            buttonLanguage = new String[]{
                    "Add Relic",
                    "Add Event",
                    "Add Enemy",
                    "Basic Settings",
                    "Some settings need to restart \nand reopen the game to apply.",
                    "    Threshold Protocol", // 5
                    "Threshold Protocol is a hard mode that is effective above A20. \nWhen Stelle passing the game with full threshold, increase the level by 1. \nTick the threshold protocol item to activate the threshold effect. \nEach item adds an extra 20% HP and 10% toughness to monsters.",
                    "Monsters start with 1 layer of Thorns.",
                    "Monsters start with 1 layer of Malleable.",
                    "Monsters start with 1 layer of Ritual.",
                    "Elites and Bosses start with Safeguard.", // 10
                    "When Elite battle starts, add a status card. \nBefore Boss battle, add a curse card.",
                    "Current Threshold Protocol Level: %d / %d.",
                    "Threshold Protocol has reached the limit.",
                    "Cannot modify Threshold Protocol during a run.",
            };
        BaseMod.subscribe(this);
    }

    public static HSRModConfig getInstance() {
        if (instance == null) {
            instance = new HSRModConfig();
        }
        return instance;
    }

    public void setProperties() {
        try {
            Properties defaults = new Properties();
            
            defaults.setProperty("addRelic", Boolean.toString(true));
            defaults.setProperty("addEvent", Boolean.toString(true));
            defaults.setProperty("addEnemy", Boolean.toString(true));
            
            defaults.setProperty("tpCount", Integer.toString(0));
            defaults.setProperty("tpLimit", Integer.toString(0));
            defaults.setProperty("tpThorn", Boolean.toString(false));
            defaults.setProperty("tpMalleable", Boolean.toString(false));
            defaults.setProperty("tpRitual", Boolean.toString(false));
            defaults.setProperty("tpSafeguard", Boolean.toString(false));
            defaults.setProperty("tpCurse", Boolean.toString(false));
            
            config = new SpireConfig(HSRMod.MOD_NAME, HSRMod.makePath("Config"), defaults);
            config.load();
            
            addRelic = config.getBool("addRelic");
            addEvent = config.getBool("addEvent");
            addEnemy = config.getBool("addEnemy");
            
            tpLimit = config.getInt("tpLimit");
            tpThorn = config.getBool("tpThorn");
            tpMalleable = config.getBool("tpMalleable");
            tpRitual = config.getBool("tpRitual");
            tpSafeguard = config.getBool("tpSafeguard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContents(ModPanel panel) {
        float x = 1100.0F;
        
        panel.addUIElement(new ModLabel(buttonLanguage[3], x, 800.0F, Color.YELLOW, panel, (me) -> {
        }));
        panel.addUIElement(new ModLabel(buttonLanguage[4], x, 705.0F, Color.GRAY, panel, (me) -> {
        }));
        ModLabeledToggleButton addRelicButton = new ModLabeledToggleButton(buttonLanguage[0], x, 650.0F, Color.WHITE, FontHelper.buttonLabelFont, addRelic, panel, (label) -> {
        }, (button) -> {
            addRelic = button.enabled;
            displayRestartRequiredText();
            try {
                config.setBool("addRelic", addRelic);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        panel.addUIElement(addRelicButton);

        ModLabeledToggleButton addEventButton = new ModLabeledToggleButton(buttonLanguage[1], x, 600.0F, Color.WHITE, FontHelper.buttonLabelFont, addEvent, panel, (label) -> {
        }, (button) -> {
            addEvent = button.enabled;
            displayRestartRequiredText();
            try {
                config.setBool("addEvent", addEvent);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        panel.addUIElement(addEventButton);

        ModLabeledToggleButton addEnemyButton = new ModLabeledToggleButton(buttonLanguage[2], x, 550.0F, Color.WHITE, FontHelper.buttonLabelFont, addEnemy, panel, (label) -> {
        }, (button) -> {
            addEnemy = button.enabled;
            displayRestartRequiredText();
            try {
                config.setBool("addEnemy", addEnemy);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        panel.addUIElement(addEnemyButton);

        /*ModLabeledToggleButton removeOtherEnemiesButton = new ModLabeledToggleButton(buttonLanguage[3], 400.0F, 400.0F, Color.WHITE, FontHelper.buttonLabelFont, removeOtherEnemies, panel, (label) -> {
        }, (button) -> {
            removeOtherEnemies = button.enabled;
            try {
                config.setBool("removeOtherEnemies", removeOtherEnemies);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        panel.addUIElement(removeOtherEnemiesButton);*/
    }

    public void setThresholdProtocols(ModPanel panel) {
        float x = 400.0F;

        panel.addUIElement(new ModImage(390, 785, PathDefine.POWER_PATH + "ChargingPower48.png"));
        panel.addUIElement(new ModLabel(buttonLanguage[5], x, 800.0F, Color.RED, panel, (me) -> {
        }));
        panel.addUIElement(new ModLabel(buttonLanguage[6], x, 200.0F, Color.GRAY, panel, (me) -> {
        }));
        panel.addUIElement(new ModLabel(String.format(buttonLanguage[12], getActiveTPCount(), tpLimit), x, 755.0F, Color.ORANGE, panel, (me) -> {
            me.text = String.format(buttonLanguage[12], getActiveTPCount(), tpLimit);
        }));

        ModLabeledToggleButton tpTHornButton = new ModLabeledToggleButton(buttonLanguage[7], x, 700.0F, Color.WHITE, FontHelper.buttonLabelFont, tpThorn, panel, (label) -> {
        }, (button) -> {
            if (!checkButton(button, (b) -> tpThorn = b)) return;
            try {
                config.setBool("tpThorn", tpThorn);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        panel.addUIElement(tpTHornButton);

        ModLabeledToggleButton tpMalleableButton = new ModLabeledToggleButton(buttonLanguage[8], x, 650.0F, Color.WHITE, FontHelper.buttonLabelFont, tpMalleable, panel, (label) -> {
        }, (button) -> {
            if (!checkButton(button, (b) -> tpMalleable = b)) return;
            try {
                config.setBool("tpMalleable", tpMalleable);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        panel.addUIElement(tpMalleableButton);

        ModLabeledToggleButton tpRitualButton = new ModLabeledToggleButton(buttonLanguage[9], x, 600.0F, Color.WHITE, FontHelper.buttonLabelFont, tpRitual, panel, (label) -> {
        }, (button) -> {
            if (!checkButton(button, (b) -> tpRitual = b)) return;
            try {
                config.setBool("tpRitual", tpRitual);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        panel.addUIElement(tpRitualButton);

        ModLabeledToggleButton tpSafeguardButton = new ModLabeledToggleButton(buttonLanguage[10], x, 550.0F, Color.WHITE, FontHelper.buttonLabelFont, tpSafeguard, panel, (label) -> {
        }, (button) -> {
            if (!checkButton(button, (b) -> tpSafeguard = b)) return;
            try {
                config.setBool("tpSafeguard", tpSafeguard);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        panel.addUIElement(tpSafeguardButton);

        ModLabeledToggleButton tpStatusButton = new ModLabeledToggleButton(buttonLanguage[11], x, 450.0F, Color.WHITE, FontHelper.buttonLabelFont, tpCurse, panel, (label) -> {
        }, (button) -> {
            if (!checkButton(button, (b) -> tpCurse = b)) return;
            try {
                config.setBool("tpCurse", tpCurse);
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        panel.addUIElement(tpStatusButton);
    }
    
    public boolean checkButton(ModToggleButton button, Consumer<Boolean> setter) {
        // button.enabled is the new updated state
        if (getActiveTPCount() == tpLimit && button.enabled) {
            setter.accept(false);
            button.enabled = false;
            displayText(buttonLanguage[13]);
            return false;
        }
        if (CardCrawlGame.mainMenuScreen.buttons.stream()
                .anyMatch(b -> b.result == MenuButton.ClickResult.ABANDON_RUN || b.result == MenuButton.ClickResult.RESUME_GAME)) {
            setter.accept(false);
            button.enabled = !button.enabled;
            displayText(buttonLanguage[14]);
            return false;
        }
        setter.accept(button.enabled);
        if (button.enabled) {
            CardCrawlGame.sound.playA("HEART_BEAT", MathUtils.random(0.0F, 0.6F));
        }
        return true;
    }

    public void displayRestartRequiredText() {
        if (CardCrawlGame.mode == CardCrawlGame.GameMode.CHAR_SELECT) {
            if (CardCrawlGame.mainMenuScreen != null) {
                effects.clear();
                effects.add(new RestartForChangesEffect());
            }
        } else {
            AbstractDungeon.topLevelEffects.add(new RestartForChangesEffect());
        }
    }
    
    public void displayText(String text) {
        if (CardCrawlGame.mode == CardCrawlGame.GameMode.CHAR_SELECT) {
            if (CardCrawlGame.mainMenuScreen != null) {
                effects.clear();
                effects.add(new TopWarningEffect(text));
            }
        } else {
            AbstractDungeon.topLevelEffects.add(new TopWarningEffect(text));
        }
    }
    
    public void addConfigPanel() {
        loadSettings();
        ModPanel panel = new ModPanel();
        setContents(panel);
        setThresholdProtocols(panel);
        Texture badgeTexture = ImageMaster.loadImage("HSRModResources/img/char/badge.png");
        BaseMod.registerModBadge(badgeTexture, HSRMod.MOD_NAME, Arrays.stream(info.Authors).findFirst().orElse(""), info.Description, panel);
    }

    public static void loadSettings() {
        try {
            config = new SpireConfig(HSRMod.MOD_NAME, HSRMod.makePath("Config"));
            config.load();
        } catch (Exception ex) {
            HSRMod.logger.catching(ex);
        }
    }

    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = (AnnotationDB) Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null) {
                return false;
            } else {
                Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
                return initializers.contains(HSRMod.class.getName());
            }
        }).findFirst();
        if (infos.isPresent()) {
            info = (ModInfo) infos.get();
            HSRMod.MOD_NAME = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }
    
    public static int getActiveTPCount() {
        int result = 0;
        if (tpThorn) result++;
        if (tpMalleable) result++;
        if (tpRitual) result++;
        if (tpSafeguard) result++;
        if (tpCurse) result++;
        return result;
    }
    
    public void addTPLimit() {
        if (tpLimit == TP_LIMIT_LIMIT) return;
        tpLimit++;
        try {
            config.setInt("tpLimit", tpLimit);
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        loadModInfo();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if (tpCurse && abstractRoom instanceof MonsterRoomBoss) {
            AbstractCard curse = AbstractDungeon.getCardFromPool(AbstractCard.CardRarity.CURSE, AbstractCard.CardType.CURSE, true);
            if (curse != null) {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(curse.makeCopy(), 1, true, true));
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse.makeCopy(), (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            }
        }
        if (tpCurse && abstractRoom.eliteTrigger) {
            CardLibrary.getAllCards().stream().filter((c) -> c.type == AbstractCard.CardType.STATUS).findAny().ifPresent(
                    state -> AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(state.makeCopy(), 1, true, true))
            );
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        if (CardCrawlGame.stopClock 
                && AbstractDungeon.ascensionLevel >= 20
                && AbstractDungeon.player instanceof StellaCharacter) {   
            if (tpLimit == 0 || getActiveTPCount() == tpLimit) {
                addTPLimit();
            }
        }
    }

    @Override
    public void receivePreUpdate() {
        Iterator<AbstractGameEffect> c = this.effects.iterator();

        while(c.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)c.next();
            e.update();
            if (e.isDone) {
                c.remove();
            }
        }
    }

    @Override
    public void receiveRender(SpriteBatch spriteBatch) {
        spriteBatch.setColor(Color.WHITE);
        Iterator var2 = this.effects.iterator();

        while(var2.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)var2.next();
            e.render(spriteBatch);
        }
    }
}
