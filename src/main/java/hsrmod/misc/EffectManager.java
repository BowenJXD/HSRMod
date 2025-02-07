package hsrmod.misc;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.StarBounceEffect;
import com.megacrit.cardcrawl.vfx.combat.*;

public class EffectManager {
    
    public EffectManager() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
        Hitbox hb = m.hb;

        ReaperEffect reaper = new ReaperEffect(); // 雷劈
        WeightyImpactEffect weighty = new WeightyImpactEffect(p.hb.cX, p.hb.cY); // 圣光降下大锤
        OfferingEffect offering = new OfferingEffect(); // 消耗血量，血效果，红vignette
        ViolentAttackEffect violent = new ViolentAttackEffect(p.hb.cX, p.hb.cY, Color.RED); // 红色闪光特效划拉几下
        StarBounceEffect star = new StarBounceEffect(p.hb.cX, p.hb.cY); // 星星弹跳特效
        HemokinesisEffect hemokinesis = new HemokinesisEffect(hb.cX, hb.cY, p.hb.cX, p.hb.cY); // 多个血球发射如同魔法轰炸
        FlameBarrierEffect flameBarrier = new FlameBarrierEffect(p.hb.cX, p.hb.cY); // 火焰屏障，放一圈火在角色旁边
        SearingBlowEffect searingBlow = new SearingBlowEffect(p.hb.cX, p.hb.cY, 1); // 横排火焰
        // Animate Jump for opponent
        WhirlwindEffect whirlwind = new WhirlwindEffect(); // 一股风从一头吹向另一头，可自定义颜色
        CleaveEffect cleave = new CleaveEffect(); // 一道长刀光
        ClashEffect clash = new ClashEffect(p.hb.cX, p.hb.cY); // 交叉刀光（X）
        VerticalImpactEffect vertical = new VerticalImpactEffect(p.hb.cX, p.hb.cY); // 斜着的冲击，类似萨姆Rider Kick
        IronWaveEffect ironWave = new IronWaveEffect(hb.cX, hb.cY, p.hb.cX); // 一段段如同波浪横向砸过去的特效
        // Animate flip
    }
}
