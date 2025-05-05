package androidTestMod.modcore;

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
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.RestartForChangesEffect;
import androidTestMod.characters.StellaCharacter;
import androidTestMod.effects.TopWarningEffect;
import androidTestMod.powers.misc.ShuffleStatePower;
import androidTestMod.utils.PathDefine;
import org.scannotation.AnnotationDB;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HSRModConfig implements OnStartBattleSubscriber, PostBattleSubscriber, RenderSubscriber, PreUpdateSubscriber {
    
    private static HSRModConfig instance;
    
    static int[] HP_INCS = {5, 10, 20, 40};
    static int[] TV_INCS = {5, 10, 15, 20};
    static int GOLD_INC = 400;
    
    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(HSRModConfig.class.getSimpleName());
    public static String[] TEXT = uiStrings.TEXT;
    public static SpireConfig config;
    public static ModInfo info;
    public static Texture tpIcon;
    
    public static boolean addRelic = true;
    public static boolean addEvent = true;
    public static boolean addEnemy = true;
    public static boolean useSpine = true;

    public static int tpLimit = 0;
    public static boolean tpThorn = false;
    public static boolean tpMalleable = false;
    public static boolean tpRitual = false;
    public static boolean tpSafeguard = false;
    public static boolean tpCurse = false;
    
    public static final int TP_LIMIT_LIMIT = 5;

    public ArrayList<AbstractGameEffect> effects = new ArrayList<>();

    private HSRModConfig() {
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
            defaults.setProperty("useSpine", Boolean.toString(true));
            
            defaults.setProperty("tpCount", Integer.toString(0));
            defaults.setProperty("tpLimit", Integer.toString(0));
            defaults.setProperty("tpThorn", Boolean.toString(false));
            defaults.setProperty("tpMalleable", Boolean.toString(false));
            defaults.setProperty("tpRitual", Boolean.toString(false));
            defaults.setProperty("tpSafeguard", Boolean.toString(false));
            defaults.setProperty("tpCurse", Boolean.toString(false));
            
            config = new SpireConfig(AndroidTestMod.MOD_NAME, AndroidTestMod.makePath("Config"), defaults);
            config.load();
            
            addRelic = config.getBool("addRelic");
            addEvent = config.getBool("addEvent");
            addEnemy = config.getBool("addEnemy");
            useSpine = config.getBool("useSpine");
            
            tpLimit = config.getInt("tpLimit");
            tpThorn = config.getBool("tpThorn");
            tpMalleable = config.getBool("tpMalleable");
            tpRitual = config.getBool("tpRitual");
            tpSafeguard = config.getBool("tpSafeguard");
            tpCurse = config.getBool("tpCurse");
            tpIcon = new Texture(PathDefine.POWER_PATH + "ChargingPower48.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContents(ModPanel panel) {
        float x = 1100.0F;
        
        panel.addUIElement(new ModLabel(getText(TextContent.BASIC_SETTINGS), x, 800.0F, Color.YELLOW, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel me) {
            }
        }));
        panel.addUIElement(new ModLabel(getText(TextContent.RESTART_NOTICE), x, 705.0F, Color.GRAY, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel me) {
            }
        }));
        ModLabeledToggleButton addRelicButton = new ModLabeledToggleButton(getText(TextContent.ADD_RELIC), x, 650.0F, Color.WHITE, FontHelper.buttonLabelFont, addRelic, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel label) {
            }
        }, new Consumer<ModToggleButton>() {
            @Override
            public void accept(ModToggleButton button) {
                addRelic = button.enabled;
                HSRModConfig.this.displayRestartRequiredText();
                try {
                    config.setBool("addRelic", addRelic);
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.addUIElement(addRelicButton);

        ModLabeledToggleButton addEventButton = new ModLabeledToggleButton(getText(TextContent.ADD_EVENT), x, 600.0F, Color.WHITE, FontHelper.buttonLabelFont, addEvent, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel label) {
            }
        }, new Consumer<ModToggleButton>() {
            @Override
            public void accept(ModToggleButton button) {
                addEvent = button.enabled;
                HSRModConfig.this.displayRestartRequiredText();
                try {
                    config.setBool("addEvent", addEvent);
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.addUIElement(addEventButton);

        ModLabeledToggleButton addEnemyButton = new ModLabeledToggleButton(getText(TextContent.ADD_ENEMY), x, 550.0F, Color.WHITE, FontHelper.buttonLabelFont, addEnemy, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel label) {
            }
        }, new Consumer<ModToggleButton>() {
            @Override
            public void accept(ModToggleButton button) {
                addEnemy = button.enabled;
                HSRModConfig.this.displayRestartRequiredText();
                try {
                    config.setBool("addEnemy", addEnemy);
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.addUIElement(addEnemyButton);
        
        ModLabeledToggleButton useSpineButton = new ModLabeledToggleButton(getText(TextContent.USE_SPINE), x, 500.0F, Color.WHITE, FontHelper.buttonLabelFont, useSpine, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel label) {
            }
        }, new Consumer<ModToggleButton>() {
            @Override
            public void accept(ModToggleButton button) {
                useSpine = button.enabled;
                try {
                    config.setBool("useSpine", useSpine);
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.addUIElement(useSpineButton);
    }

    public void setThresholdProtocols(ModPanel panel) {
        float x = 400.0F;

        panel.addUIElement(new ModImage(390, 785, PathDefine.POWER_PATH + "ChargingPower48.png"));
        panel.addUIElement(new ModLabel(getText(TextContent.THRERHOLD_PROTOCOLS), x, 800.0F, Color.RED, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel me) {
            }
        }));
        panel.addUIElement(new ModLabel(getText(TextContent.TP_DESCRIPTION), x, 200.0F, Color.GRAY, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel me) {
            }
        }));
        panel.addUIElement(new ModLabel(String.format(getText(TextContent.TP_LEVEL), getActiveTPCount(), tpLimit), x, 755.0F, Color.ORANGE, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel me) {
                me.text = String.format(getText(TextContent.TP_LEVEL), getActiveTPCount(), tpLimit);
            }
        }));

        ModLabeledToggleButton tpTHornButton = new ModLabeledToggleButton(getText(TextContent.TP_THORN), x, 700.0F, Color.WHITE, FontHelper.buttonLabelFont, tpThorn, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel label) {
            }
        }, new Consumer<ModToggleButton>() {
            @Override
            public void accept(ModToggleButton button) {
                if (!HSRModConfig.this.checkButton(button, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) {
                        tpThorn = b;
                    }
                })) return;
                try {
                    config.setBool("tpThorn", tpThorn);
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.addUIElement(tpTHornButton);

        ModLabeledToggleButton tpMalleableButton = new ModLabeledToggleButton(getText(TextContent.TP_MALLEABLE), x, 650.0F, Color.WHITE, FontHelper.buttonLabelFont, tpMalleable, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel label) {
            }
        }, new Consumer<ModToggleButton>() {
            @Override
            public void accept(ModToggleButton button) {
                if (!HSRModConfig.this.checkButton(button, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) {
                        tpMalleable = b;
                    }
                })) return;
                try {
                    config.setBool("tpMalleable", tpMalleable);
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.addUIElement(tpMalleableButton);

        ModLabeledToggleButton tpRitualButton = new ModLabeledToggleButton(getText(TextContent.TP_RITUAL), x, 600.0F, Color.WHITE, FontHelper.buttonLabelFont, tpRitual, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel label) {
            }
        }, new Consumer<ModToggleButton>() {
            @Override
            public void accept(ModToggleButton button) {
                if (!HSRModConfig.this.checkButton(button, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) {
                        tpRitual = b;
                    }
                })) return;
                try {
                    config.setBool("tpRitual", tpRitual);
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.addUIElement(tpRitualButton);

        ModLabeledToggleButton tpSafeguardButton = new ModLabeledToggleButton(getText(TextContent.TP_SAFEGUARD), x, 550.0F, Color.WHITE, FontHelper.buttonLabelFont, tpSafeguard, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel label) {
            }
        }, new Consumer<ModToggleButton>() {
            @Override
            public void accept(ModToggleButton button) {
                if (!HSRModConfig.this.checkButton(button, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) {
                        tpSafeguard = b;
                    }
                })) return;
                try {
                    config.setBool("tpSafeguard", tpSafeguard);
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.addUIElement(tpSafeguardButton);

        ModLabeledToggleButton tpStatusButton = new ModLabeledToggleButton(getText(TextContent.TP_CURSE), x, 500.0F, Color.WHITE, FontHelper.buttonLabelFont, tpCurse, panel, new Consumer<ModLabel>() {
            @Override
            public void accept(ModLabel label) {
            }
        }, new Consumer<ModToggleButton>() {
            @Override
            public void accept(ModToggleButton button) {
                if (!HSRModConfig.this.checkButton(button, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) {
                        tpCurse = b;
                    }
                })) return;
                try {
                    config.setBool("tpCurse", tpCurse);
                    config.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.addUIElement(tpStatusButton);
    }
    
    public boolean checkButton(ModToggleButton button, Consumer<Boolean> setter) {
        // button.enabled is the new updated state
        if (getActiveTPCount() == tpLimit && button.enabled) {
            setter.accept(false);
            button.enabled = false;
            displayText(getText(TextContent.TP_LIMIT_REACHED_NOTICE));
            return false;
        }
        boolean result = false;
        for (MenuButton b : CardCrawlGame.mainMenuScreen.buttons) {
            if (b.result == MenuButton.ClickResult.ABANDON_RUN || b.result == MenuButton.ClickResult.RESUME_GAME) {
                result = true;
                break;
            }
        }
        if (result) {
            setter.accept(false);
            button.enabled = !button.enabled;
            displayText(getText(TextContent.TP_CANT_MODIFY_NOTICE));
            return false;
        }
        setter.accept(button.enabled);
        if (button.enabled) {
            CardCrawlGame.sound.playA("STANCE_ENTER_WRATH", MathUtils.random(0.0F, 0.6F));
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
        Texture badgeTexture = ImageMaster.loadImage("img/char/badge.png");
        String found = "";
        for (String Author : info.Authors) {
            found = Author;
            break;
        }
        BaseMod.registerModBadge(badgeTexture, AndroidTestMod.MOD_NAME, found, info.Description, panel);
    }

    public static void loadSettings() {
        try {
            config = new SpireConfig(AndroidTestMod.MOD_NAME, AndroidTestMod.makePath("Config"));
            config.load();
        } catch (Exception ex) {
            AndroidTestMod.logger.catching(ex);
        }
    }

    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter(new Predicate<ModInfo>() {
            @Override
            public boolean test(ModInfo modInfo) {
                AnnotationDB annotationDB = (AnnotationDB) Patcher.annotationDBMap.get(modInfo.jarURL);
                if (annotationDB == null) {
                    return false;
                } else {
                    Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
                    return initializers.contains(AndroidTestMod.class.getName());
                }
            }
        }).findFirst();
        if (infos.isPresent()) {
            info = (ModInfo) infos.get();
            AndroidTestMod.MOD_NAME = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }
    
    public static int getActiveTPCount() {
        int result = 0;
        if (AbstractDungeon.ascensionLevel < 20) return result;
        if (tpThorn) result++;
        if (tpMalleable) result++;
        if (tpRitual) result++;
        if (tpSafeguard) result++;
        if (tpCurse) result++;
        return result;
    }

    public static float getHPInc() {
        int count = getActiveTPCount();
        int actIndex = AbstractDungeon.actNum - 1;
        actIndex = Math.min(actIndex, HP_INCS.length - 1);
        actIndex = Math.max(actIndex, 0);
        return HP_INCS[actIndex] * count / 100f;
    }

    public static float getTVInc() {
        int count = getActiveTPCount();
        int actIndex = AbstractDungeon.actNum - 1;
        actIndex = Math.min(actIndex, TV_INCS.length - 1);
        actIndex = Math.max(actIndex, 0);
        return TV_INCS[actIndex] * count / 100f;
    }
    
    public static int getGoldInc() {
        int count = getActiveTPCount();
        return GOLD_INC * count;
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
    
    public static String getHeader() {
        return getText(TextContent.THRERHOLD_PROTOCOLS);
    }
    
    public static String getTip() {
        StringBuilder sb = new StringBuilder();
        if (tpThorn) sb.append(getText(TextContent.TP_THORN)).append(" NL ");
        if (tpMalleable) sb.append(getText(TextContent.TP_MALLEABLE)).append(" NL ");
        if (tpRitual) sb.append(getText(TextContent.TP_RITUAL)).append(" NL ");
        if (tpSafeguard) sb.append(getText(TextContent.TP_SAFEGUARD)).append(" NL ");
        if (tpCurse) sb.append(getText(TextContent.TP_CURSE)).append(" NL ");
        return sb.toString();
    }
    
    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if (tpCurse && AbstractDungeon.ascensionLevel >= 20) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ShuffleStatePower(AbstractDungeon.player)));
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
    
    public static String getText(TextContent content) {
        return TEXT[content.ordinal()];
    }
    
    public enum TextContent {
        BASIC_SETTINGS,
        RESTART_NOTICE,
        ADD_RELIC,
        ADD_EVENT,
        ADD_ENEMY,
        USE_SPINE,
        THRERHOLD_PROTOCOLS,
        TP_DESCRIPTION,
        TP_THORN,
        TP_MALLEABLE,
        TP_RITUAL,
        TP_SAFEGUARD,
        TP_CURSE,
        TP_LEVEL,
        TP_LIMIT_REACHED_NOTICE,
        TP_CANT_MODIFY_NOTICE,
    }

    static {
        loadModInfo();
    }
}
