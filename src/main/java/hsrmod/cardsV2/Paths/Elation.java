package hsrmod.cardsV2.Paths;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import com.megacrit.cardcrawl.vfx.combat.*;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import hsrmod.actions.ForceWaitAction;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

public class Elation extends BaseCard {
    public static final String ID = Elation.class.getSimpleName();

    int effectCount = 40;

    public Elation() {
        super(ID);
        target = CardTarget.ENEMY;
        returnToHand = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int h = p.currentHealth;
        if (h > effectCount) {
            for (int i = 0; i < effectCount; i++) {
                castEffect(i, p, m);
                addToBot(new ForceWaitAction(2f));
            }
        } else {
            castEffect(h, p, m);
        }
    }

    void castEffect(int i, AbstractPlayer p, AbstractMonster m) {
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                AbstractGameEffect effect = null;
                switch (i) {
                    case 1:
                        // effect = new AdditiveSlashImpactEffect(0.0F, 0.0F, Color.WHITE);
                        break;
                    case 2:
                        effect = new OmegaFlashEffect(p.hb.cX, p.hb.cY);                                                    // omega标志显示
                        break;
                    case 3:
                        effect = new PingHpEffect(p.hb.cX);
                        break;
                    case 4:
                        effect = new RipAndTearEffect(p.hb.cX, p.hb.cY, Color.YELLOW, Color.RED);                           // 金刚狼爪击
                        break;
                    case 5:
                        effect = new PressurePointEffect(p.hb.cX, p.hb.cY);                                                 // 细的紫色流星闪光
                        break;
                    case 6:
                        effect = new ReaperEffect();                                                                        // 雷劈
                        break;
                    case 7:
                        effect = new RedFireballEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY, 3);                 // 一排火过去
                        break;
                    case 8:
                        effect = new RedFireBurstParticleEffect(p.hb.cX, p.hb.cY, 10);                         // 单个火效果
                        break;
                    case 9:
                        effect = new RoomTintEffect(Color.CYAN, 0.5F);                                        // 房间滤镜
                        break;
                    case 10:
                        effect = new SanctityEffect(p.hb.cX, p.hb.cY);                                                      // 一圈细长光粒子扩散出去
                        break;
                    case 11:
                        effect = new ScrapeEffect(p.hb.cX, p.hb.cY);                                                        // 两个爪子交叉爪击
                        break;
                    case 12:
                        effect = new ScreenOnFireEffect();                                                                  // 屏幕燃烧
                        break;
                    case 13:
                        effect = new SmallLaserEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY);                                  // 小激光
                        break;
                    case 14:
                        effect = new SmokeBlurEffect(m.hb.cX, m.hb.cY);                                                     // 单个烟雾
                        break;
                    case 15:
                        effect = new SmokeBombEffect(m.hb.cX, m.hb.cY);                                                     // 烟雾弹
                        break;
                    case 16:
                        effect = new SmokingEmberEffect(p.hb.cX, p.hb.cY);                                                  // 爆炸的单个小火花
                        break;
                    case 17:
                        effect = new StrikeEffect(m, m.hb.cX, m.hb.cY, 3);                                          // 多个红色黄色橙色闪光散开
                        break;
                    case 18:
                        effect = new StunStarEffect(m.hb.cX, m.hb.cY);                                                      // 眩晕意图的星星特效
                        break;
                    case 19:
                        effect = new SweepingBeamEffect(p.hb.cX, p.hb.cY, false);                                  // 扫射激光
                        break;
                    case 20:
                        effect = new SwirlyBloodEffect(p.hb.cX, p.hb.cY);                                                   // 单个心脏buff特效
                        break;
                    case 21:
                        effect = new ThirdEyeEffect(p.hb.cX, p.hb.cY);                                                      // 十字的紫色火焰散开的效果
                        break;
                    case 22:
                        effect = new ThrowDaggerEffect(m.hb.cX, m.hb.cY);                                                   // 绿色闪光+飞刀音效
                        break;
                    case 23:
                        effect = new ThrowShivEffect(m.hb.cX, m.hb.cY);                                                     // 白色闪光+飞刀音效
                        break;
                    case 24:
                        effect = new TimeWarpTurnEndEffect();                                                               // 时间扭曲（无音效）
                        break;
                    case 25:
                        effect = new VerticalAuraEffect(Color.RED, p.hb.cX, p.hb.cY);                                       // aura从身上竖向散开
                        break;
                    case 26:
                        effect = new VerticalImpactEffect(p.hb.cX, p.hb.cY);                                                // 萨姆踢
                        break;
                    case 27:
                        effect = new ViceCrushEffect(m.hb.cX, m.hb.cY);                                                     // 心脏单伤攻击特效（两个紫色的][撞击）
                        break;
                    case 28:
                        effect = new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.RED);                                      // 红色闪光特效划拉几下
                        break;
                    case 29:
                        effect = new WaterDropEffect(p.hb.cX, p.hb.cY);                                                     // 血滴（覆盖人物）
                        break;
                    case 30:
                        effect = new WebEffect(p, p.hb.cX, p.hb.cY);                                                        // 多个蜘蛛网
                        break;
                    case 31:
                        effect = new WebLineEffect(p.hb.cX, p.hb.cY, false);                                       // 单个不断伸缩的线
                        break;
                    case 32:
                        effect = new WobblyLineEffect(p.hb.cX, p.hb.cY, Color.RED);                                         // 单个蛇出去的效果
                        break;
                    case 33:
                        effect = new FlyingOrbEffect(p.hb.cX, p.hb.cY);                                                     // 单个红色小球旋转出去
                        break;
                    case 34:
                        effect = new DarkOrbActivateEffect(p.hb.cX, p.hb.cY);                                               // 紫色线条》《》《横着散开
                        break;
                    case 35:
                        effect = new LightningOrbActivateEffect(p.hb.cX, p.hb.cY);                                          // 闪电横着散开
                        break;
                    case 36:
                        effect = new FrostOrbActivateEffect(p.hb.cX, p.hb.cY);                                              // 冰块在人物身上显示一下
                        break;
                    case 37:
                        effect = new PlasmaOrbActivateEffect(p.hb.cX, p.hb.cY);                                             // 彩色光球在人物身上显示一下
                        break;
                    case 38:
                        effect = new WallopEffect(5, m.hb.cX, m.hb.cY);                                             // 几个星星抛物线四散
                        break;
                    case 39:
                        effect = new DivinityParticleEffect();                                                              // GiantEye没那么大
                        break;
                    case 40:
                        effect = new RainbowCardEffect();
                        break;
                }
                if (effect == null) return;
                Elation.this.addToTop(new VFXAction(effect));
                Elation.this.addToTop(new TalkAction(true, i + ": " + effect, 1.0F, 2.0F));
            }
        });
    }
}
