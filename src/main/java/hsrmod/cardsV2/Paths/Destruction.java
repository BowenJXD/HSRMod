package hsrmod.cardsV2.Paths;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.*;
import hsrmod.actions.ForceWaitAction;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

public class Destruction extends BaseCard {
    public static final String ID = Destruction.class.getSimpleName();

    int effectCount = 40;

    public Destruction() {
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
                        effect = new AdrenalineEffect();                                                                        // 心电图的线
                        break;
                    case 2:
                        effect = new BiteEffect(m.hb.cX, m.hb.cY, Color.WHITE.cpy());                                           // 咬一口
                        break;
                    case 3:
                        effect = new BlizzardEffect(3, false);                                                 // 砸冰块
                        break;
                    case 4:
                        effect = new BloodShotEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY, 1);                              // 血液喷射，类似心脏
                        break;
                    case 5:
                        effect = new BorderFlashEffect(Color.RED);                                                              // 红色边框闪烁
                        break;
                    case 6:
                        effect = new BlurWaveChaoticEffect(p.hb.cX, p.hb.cY, Color.WHITE.cpy(), 1);                 // 抖动的模糊波冲击（单个30°）
                        break;
                    case 7:
                        effect = new BlurWaveNormalEffect(p.hb.cX, p.hb.cY, Color.WHITE.cpy(), 1);                  // 模糊波冲击（单个30°），类似钟表老头
                        break;
                    case 8:
                        effect = new BossCrystalImpactEffect(m.hb.cX, m.hb.cY);                                                 // 淡淡的紫色光晕从身上展开，扩大后散去
                        break;
                    case 9:
                        effect = new ClawEffect(m.hb.cX, m.hb.cY, Color.WHITE.cpy(), Color.RED.cpy());                          // 金刚狼爪击
                        break;
                    case 10:
                        effect = new DaggerSprayEffect(false);                                                         // 多个飞刀飞出去（带音效）
                        break;
                    case 11:
                        effect = new DevotionEffect();                                                                          // 寥寥几个火焰在地上从左到右闪过
                        break;
                    case 12:
                        effect = new DieDieDieEffect();                                                                         // 很多刀光（AOE）
                        break;
                    case 13:
                        effect = new EmpowerCircleEffect(p.hb.cX, p.hb.cY);                                                     // 小的白色圆圈从身体飞出，然后停止。
                        break;
                    case 14:
                        effect = new EmpowerEffect(p.hb.cX, p.hb.cY);                                                           // 很多白色圆圈从身体飞出，然后停止。
                        break;
                    case 15:
                        effect = new EntangleEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY);                                        // 扔一个蜘蛛网
                        break;
                    case 16:
                        effect = new FallingIceEffect(1, false);                                               // 砸冰块
                        break;
                    case 17:
                        effect = new FastingEffect(p.hb.cX, p.hb.cY, Color.WHITE);                                              // 较厚的圆圈包裹自身，然后收缩消散
                        break;
                    case 18:
                        effect = new FireballEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY);                                        // 绿色火球横着砸向敌人
                        break;
                    case 19:
                        effect = new FlickCoinEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY);                                       // 金币抛物线砸向敌人
                        break;
                    case 20:
                        effect = new FlyingDaggerEffect(m.hb.cX, m.hb.cY, 0, false);                            // 单个飞刀横着飞（可自定义角度）
                        break;
                    case 21:
                        effect = new FlyingSpikeEffect(p.hb.cX, p.hb.cY, 0, m.hb.cX, m.hb.cY, Color.WHITE);       // 单个刺直着飞（可自定义角度）
                        break;
                    case 22:
                        effect = new GhostIgniteEffect(m.hb.cX, m.hb.cY);                                                       // 绿色火焰从身上展开，往上消散
                        break;
                    case 23:
                        effect = new GiantEyeEffect(m.hb.cX, m.hb.cY, Color.WHITE.cpy());                                       // 神格眼睛
                        break;
                    case 24:
                        effect = new GiantFireEffect();                                                                         // 单个火焰从屏幕下方冒（高层级）
                        break;
                    case 25:
                        effect = new GiantTextEffect(m.hb.cX, m.hb.cY);                                                         // 大字体JUDGE抖动扩大消散
                        break;
                    case 26:
                        effect = new GoldenSlashEffect(m.hb.cX, m.hb.cY, false);                                       // 金色斜斩击劈下
                        break;
                    case 27:
                        effect = new GrandFinalEffect();                                                                        // 金光从屏幕上方照耀下来，樱花飘落，神圣音效
                        break;
                    case 28:
                        effect = new HeartBuffEffect(p.hb.cX, p.hb.cY);                                                         // 心脏的buff效果（好几个紫色球绕着圈飞）
                        break;
                    case 29:
                        effect = new HeartMegaDebuffEffect();                                                                   // 心脏的debuff 紫色vignette效果
                        break;
                    case 30:
                        effect = new IceShatterEffect(m.hb.cX, m.hb.cY);
                        break;
                    case 31:
                        effect = new ImpactSparkEffect(m.hb.cX, m.hb.cY);                                                       // 特别小的碰撞火花
                        break;
                    case 32:
                        effect = new InflameEffect(m);                                                                          // 自杀效果
                        break;
                    case 33:
                        effect = new IntenseZoomEffect(m.hb.cX, m.hb.cY, false);                                         // 闪亮登场，附加金色vignette效果，第一层boss变换形态的效果
                        break;
                    case 34:
                        effect = new IntimidateEffect(m.hb.cX, m.hb.cY);                                                        // 蛇的混乱特效（很多蛇从身体出来）
                        break;
                    case 35:
                        effect = new InversionBeamEffect(m.hb.cX);                                                              // 反转颜色的光束
                        break;
                    case 36:
                        effect = new LightBulbEffect(p.hb);                                                                     // 电灯泡从头上冒出来（三月七）
                        break;
                    case 37:
                        effect = new LightningEffect(m.hb.cX, m.hb.cY);                                                         // 闪电从天上打下来
                        break;
                    case 38:
                        effect = new LightRayFlyOutEffect(p.hb.cX, p.hb.cY, Color.YELLOW);                                      // 单个光线从身上飞出
                        break;
                    case 39:
                        effect = new MindblastEffect(p.hb.cX, p.hb.cY, false);                                      // 激光效果+天蓝色vignette
                        break;
                    case 40:
                        effect = new MiracleEffect();                                                                           // 角色周围橙色光晕，神圣音效
                        break;
                    default:
                        break;
                }
                if (effect == null) return;
                Destruction.this.addToTop(new VFXAction(effect));
                Destruction.this.addToTop(new TalkAction(true, i + ": " + effect, 1.0F, 2.0F));
            }
        });
    }
}
