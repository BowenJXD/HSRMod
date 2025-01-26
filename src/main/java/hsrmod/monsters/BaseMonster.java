package hsrmod.monsters;

import basemod.BaseMod;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.SummonedPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.DataManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.MonsterDataCol;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class BaseMonster extends CustomMonster {
    public MonsterStrings monsterStrings;
    public String NAME;
    public String[] MOVES;
    public String[] DIALOG;
    public int turnCount;
    public int[] damages;
    public int tv = 0;
    public List<MonsterSlot> slots = new ArrayList<>();
    public Function<MonsterSlot, AbstractMonster> monFunc;
    public String bgm;
    public AbstractPlayer p = AbstractDungeon.player;
    
    public BaseMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String atlas, float x, float y) {
        super(name, HSRMod.makePath(id), maxHealth, hb_x, hb_y, hb_w, hb_h, atlas, x, y);
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(id));
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        
        dialogX = -150.0F * Settings.scale;
        dialogY = 70.0F * Settings.scale;
        
        type = EnemyType.valueOf(DataManager.getInstance().getMonsterData(id, MonsterDataCol.Type));
        
        int hp;
        try {
            hp = DataManager.getInstance().getMonsterDataInt(id + AbstractDungeon.actNum, ModHelper.moreHPAscension(type) ? MonsterDataCol.HP2 : MonsterDataCol.HP1);
        } catch (Exception e) {
            hp = DataManager.getInstance().getMonsterDataInt(id, ModHelper.moreHPAscension(type) ? MonsterDataCol.HP2 : MonsterDataCol.HP1);
        }
        
        switch (type) {
            case NORMAL:
                hp += AbstractDungeon.monsterHpRng.random(-2, 2);
                break;
            case ELITE:
                hp += AbstractDungeon.monsterHpRng.random(-3, 3);
                break;
        }
        setHp(hp);
        
        try {
            tv = DataManager.getInstance().getMonsterDataInt(id + AbstractDungeon.actNum, ModHelper.moreHPAscension(type) ? MonsterDataCol.TV2 : MonsterDataCol.TV1);
        } catch (Exception e) {
            tv = DataManager.getInstance().getMonsterDataInt(id, ModHelper.moreHPAscension(type) ? MonsterDataCol.TV2 : MonsterDataCol.TV1);
        }
        if (BaseMod.hasModID("spireTogether:")) {
            tv += (int) (tv * 0.5);
        }
        
        p = AbstractDungeon.player;
    }

    public BaseMonster(String id, String imgUrl, float hb_x, float hb_y, float hb_w, float hb_h, float x, float y) {
        this(CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(id)).NAME,
                id,
                1, 
                hb_x, hb_y, 
                hb_w, hb_h,
                imgUrl, 
                x, y);
    }
    
    public BaseMonster(String id, float hb_x, float hb_y, float hb_w, float hb_h, float x, float y) {
        this(CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(id)).NAME, 
                id,
                1, 
                hb_x, hb_y, 
                hb_w, hb_h,
                PathDefine.MONSTER_PATH + id + ".png", 
                x, y);
    }

    public BaseMonster(String id, float hb_w, float hb_h, float x, float y) {
        this(CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(id)).NAME,
                id,
                1, 
                0, -15F, 
                hb_w, hb_h,
                PathDefine.MONSTER_PATH + id + ".png", 
                x, y);
    }

    public BaseMonster(String id, float hb_w, float hb_h) {
        this(CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(id)).NAME,
                id,
                1,
                0, -15F,
                hb_w, hb_h,
                PathDefine.MONSTER_PATH + id + ".png",
                0, 0);
    }
    
    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (tv > 0) {
            addToBot(new ApplyPowerAction(this, this, new ToughnessPower(this, tv, tv), 0));
        }
        if (bgm != null) {
            AbstractDungeon.scene.fadeOutAmbiance();
            CardCrawlGame.music.silenceTempBgmInstantly();
            AbstractDungeon.getCurrRoom().playBgmInstantly(bgm);
        }
    }
    
    public void setDamagesWithAscension(int... damages){
        this.damage.clear();
        this.damages = damages;
        for (int i = 0; i < damages.length; i++) {
            damages[i] = Math.round(damages[i] * (ModHelper.moreDamageAscension(type) ? 1.1f : 1));
            this.damage.add(new DamageInfo(this, damages[i]));
        }
    }
    
    public void setDamages(int... damages) {
        this.damage.clear();
        this.damages = damages;
        for (int j : damages) {
            this.damage.add(new DamageInfo(this, j));
        }
    }
    
    public void addDamageActions(AbstractCreature target, int index, int numTimes, AbstractGameAction.AttackEffect effect) {
        for (int i = 0; i < numTimes; i++) {
            addToBot(new DamageAction(target, this.damage.get(index), effect));
        }
    }
    
    public void addSlot(float x, float y) {
        slots.add(new MonsterSlot(x, y));
    }
    
    public MonsterSlot getEmptySlot(boolean randomSlot) {
        if (slots.isEmpty()) return null;
        int startIndex = 0;
        if (randomSlot) {
            startIndex = AbstractDungeon.monsterHpRng.random(slots.size() - 1);
        }
        for (int i = 0; i < slots.size(); i++) {
            MonsterSlot slot = slots.get((startIndex + i) % slots.size());
            if (slot.isEmpty()) {
                return slot;
            }
        }
        return null;
    }
    
    public MonsterSlot getEmptySlot() {
        return getEmptySlot(true);
    }
    
    public int getEmptySlotCount() {
        int count = 0;
        for (MonsterSlot slot : slots) {
            if (slot.isEmpty()) {
                count++;
            }
        }
        return count;
    }
    
    public MonsterSlot getOccupiedSlot() {
        for (MonsterSlot slot : slots) {
            if (!slot.isEmpty()) {
                return slot;
            }
        }
        return null;
    }
    
    public void spawnMonsters() {
        spawnMonsters(getEmptySlotCount());
    }

    public void spawnMonsters(int count) {
        count = Math.min(count, getEmptySlotCount());
        if (count > 0 && monFunc != null)
            for (int i = 0; i < count; i++) {
                MonsterSlot slot = getEmptySlot();
                if (slot != null) {
                    AbstractMonster monster = monFunc.apply(slot);
                    if (monster != null) {
                        monster.usePreBattleAction();
                        addToBot(new SpawnMonsterAction(monster, true));
                        slot.setMonster(monster);
                    }
                }
            }
    }

    @Override
    public void die() {
        super.die();
        int shakeTime = 0;
        switch (type) {
            case NORMAL:
                shakeTime = 2;
                break;
            case ELITE:
                shakeTime = 3;
                break;
            case BOSS:
                shakeTime = 5;
                break;
        }
        this.useShakeAnimation(shakeTime);
        for (MonsterSlot slot : slots) {
            if (!slot.isEmpty()) {
                AbstractMonster m = slot.monster;
                if (!m.isDead && !m.isDying && (m.hasPower(MinionPower.POWER_ID) || m.hasPower(SummonedPower.POWER_ID) || m.halfDead)) {
                    AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                    AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                    AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
                }
            }
        }
    }

    public static class MonsterSlot {
        public float x;
        public float y;
        public AbstractMonster monster;
        
        public MonsterSlot(float x, float y) {
            this.x = x;
            this.y = y;
        }
        
        public boolean isEmpty() {
            return (monster == null || monster.isDeadOrEscaped());
        }
        
        public void setMonster(AbstractMonster monster) {
            this.monster = monster;
        }
    }
    
    public static enum SpawnType {
        NONE,
        MINION,
        SUMMONED
    }
}
