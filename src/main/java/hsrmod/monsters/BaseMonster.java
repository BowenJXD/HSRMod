package hsrmod.monsters;

import basemod.BaseMod;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import hsrmod.effects.MoveToEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.HSRModConfig;
import hsrmod.powers.enemyOnly.SummonedPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.DataManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.MonsterDataCol;
import hsrmod.utils.PathDefine;
import spireTogether.network.P2P.P2PManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    public List<MoveInfo> moveInfos = new ArrayList<>();
    public EnemyMoveInfo currMove;
    public boolean moreDamageAs, moreHPAs, specialAs;
    public float floatIndex = 0;
    public Consumer<BaseMonster> preBattleAction;

    public BaseMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float x, float y) {
        super(name, HSRMod.makePath(id), maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, x, y);
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
            try {
                tv += tv * P2PManager.GetPlayerCountWithoutSelf() * 50 / 100;
            } catch (Exception ignored) {}
        }
        if (HSRModConfig.getActiveTPCount() > 0) {
            tv += (int) (tv * HSRModConfig.getTVInc());
        }

        p = AbstractDungeon.player;
        moreDamageAs = ModHelper.moreDamageAscension(type);
        moreHPAs = ModHelper.moreHPAscension(type);
        specialAs = ModHelper.specialAscension(type);
    }
    
    public BaseMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float x, float y, boolean ignoreBlights) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, x, y, ignoreBlights);
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

    public BaseMonster process(Consumer<BaseMonster> consumer) {
        consumer.accept(this);
        return this;
    }

    public BaseMonster modifyHpByPercent(float percent) {
        setHp((int) (maxHealth * percent));
        return this;
    }

    public BaseMonster modifyHp(int modifyAmount) {
        setHp(maxHealth + modifyAmount);
        return this;
    }

    public BaseMonster modifyToughnessByPercent(float percent) {
        tv = (int) (tv * percent);
        return this;
    }

    public BaseMonster setPreBattleAction(Consumer<BaseMonster> action) {
        preBattleAction = action;
        return this;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (tv > 0) {
            addToBot(new ApplyPowerAction(this, this, new ToughnessPower(this, tv, tv), 0));
        }
        if (preBattleAction != null) {
            preBattleAction.accept(this);
        }
    }

    @Override
    public void takeTurn() {
        if (nextMove >= 0 && nextMove < moveInfos.size()) {
            moveInfos.get(nextMove).move();
        } else {
            HSRMod.logger.info("Monster {} has no {} move!", NAME, nextMove);
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        if (floatIndex != 0)
            this.animY = floatIndex * MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }

    public void setDamagesWithAscension(int... damages) {
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

    public MonsterSlot getEmptySlot(boolean inOrder) {
        if (slots.isEmpty()) return null;
        int startIndex = 0;
        if (!inOrder) {
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
        spawnMonsters(count, SpawnType.MINION, false);
    }

    public void spawnMonsters(int count, SpawnType spawnType, boolean inOrder) {
        count = Math.min(count, getEmptySlotCount());
        if (count > 0 && monFunc != null)
            for (int i = 0; i < count; i++) {
                ModHelper.addToBotAbstract(() -> {
                    if (isDead || isDying) return;
                    MonsterSlot slot = getEmptySlot(inOrder);
                    if (slot == null) return;
                    AbstractMonster monster = monFunc.apply(slot);
                    if (monster == null) return;
                    addToTop(new SpawnMonsterAction(monster, spawnType == SpawnType.MINION));
                    monster.usePreBattleAction();
                    if (spawnType == SpawnType.SUMMONED) {
                        addToTop(new ApplyPowerAction(monster, monster, new SummonedPower(this)));
                    }
                    slot.setMonster(monster);
                });
            }
    }

    public void respawn() {
        if (MathUtils.randomBoolean()) {
            addToBot(new SFXAction("DARKLING_REGROW_2", MathUtils.random(-0.1F, 0.1F)));
        } else {
            addToBot(new SFXAction("DARKLING_REGROW_1", MathUtils.random(-0.1F, 0.1F)));
        }

        addToBot(new HealAction(this, this, this.maxHealth / 2));
        this.halfDead = false;
        Iterator var1 = AbstractDungeon.player.relics.iterator();

        while (var1.hasNext()) {
            AbstractRelic r = (AbstractRelic) var1.next();
            r.onSpawnMonster(this);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
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

    public void addMove(Intent intent, int dmg, Supplier<Integer> dmgTimeSupplier, Consumer<MoveInfo> takeMove) {
        int index = moveInfos.size();
        this.damage.add(new DamageInfo(this, dmg));
        moveInfos.add(new MoveInfo(index, intent, () -> damage.get(index).base, dmgTimeSupplier, takeMove));
    }

    public void addMoveA(Intent intent, int dmg, Supplier<Integer> dmgTime, Consumer<MoveInfo> takeMove) {
        if (ModHelper.moreDamageAscension(type)) dmg = Math.round(dmg * 1.1f);
        addMove(intent, dmg, dmgTime, takeMove);
    }

    public void addMove(Intent intent, int dmg, int dmgTime, Consumer<MoveInfo> takeMove) {
        addMove(intent, dmg, () -> dmgTime, takeMove);
    }

    public void addMoveA(Intent intent, int dmg, int dmgTime, Consumer<MoveInfo> takeMove) {
        if (ModHelper.moreDamageAscension(type)) dmg = Math.round(dmg * 1.1f);
        addMove(intent, dmg, () -> dmgTime, takeMove);
    }

    public void addMove(Intent intent, int dmg, Consumer<MoveInfo> takeMove) {
        addMove(intent, dmg, () -> 1, takeMove);
    }

    public void addMoveA(Intent intent, int dmg, Consumer<MoveInfo> takeMove) {
        if (ModHelper.moreDamageAscension(type)) dmg = Math.round(dmg * 1.1f);
        addMove(intent, dmg, () -> 1, takeMove);
    }

    public void addMove(Intent intent, Consumer<MoveInfo> takeMove) {
        addMove(intent, 0, () -> 0, takeMove);
    }

    public void setMove(int i) {
        MoveInfo info = moveInfos.get(i);
        int dmgTime = info.damageTimeSupplier.get();
        if (dmgTime <= 0) {
            setMove(MOVES[i], (byte) i, info.intent);
        } else if (dmgTime == 1) {
            setMove(MOVES[i], (byte) i, info.intent, damage.get(i).base);
        } else {
            setMove(MOVES[i], (byte) i, info.intent, damage.get(i).base, dmgTime, true);
        }
    }

    public void attack(MoveInfo info, AbstractGameAction.AttackEffect effect, AttackAnim anim) {
        if (anim != null)
            switch (anim) {
                case FAST:
                    addToBot(new AnimateFastAttackAction(this));
                    break;
                case SLOW:
                    addToBot(new AnimateSlowAttackAction(this));
                    break;
                case HOP:
                    addToBot(new AnimateHopAction(this));
                    break;
                case JUMP:
                    addToBot(new AnimateJumpAction(this));
                    break;
                case MOVE:
                    addToBot(new VFXAction(new MoveToEffect(this, p.hb.cX + p.hb.width / 2 - hb.cX, p.hb.cY - hb.cY, true, 0.4f)));
                    break;
            }
        for (int i = 0; i < info.damageTimeSupplier.get(); i++) {
            addToBot(new DamageAction(p, this.damage.get(info.index), effect));
        }
    }

    public void attack(MoveInfo info, AbstractGameAction.AttackEffect effect) {
        attack(info, effect, null);
    }

    public String getLastMove() {
        return MOVES[MOVES.length - 1];
    }

    public void shout(int index, float volume) {
        if (index >= DIALOG.length) return;
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName() + "_" + index, volume));
        addToBot(new ShoutAction(this, DIALOG[index]));
    }

    public void shout(int index) {
        shout(index, 1.0F);
    }

    public void shout(int start, int end, float volume) {
        shout(AbstractDungeon.miscRng.random(start, end), volume);
    }

    public void shout(int start, int end) {
        shout(AbstractDungeon.miscRng.random(start, end), 3.0F);
    }

    public void shoutIf(int index) {
        if (AbstractDungeon.miscRng.randomBoolean()) {
            shout(index);
        }
    }

    public void shoutIf(int index, int chance) {
        if (chance < AbstractDungeon.miscRng.random(100)) {
            shout(index);
        }
    }
    
    public static class MoveInfo {
        public int index;
        public AbstractMonster.Intent intent;
        public Consumer<MoveInfo> takeMove;
        public Supplier<Integer> damageSupplier;
        public Supplier<Integer> damageTimeSupplier;

        public MoveInfo(int index, Intent intent, Supplier<Integer> damageSupplier, Supplier<Integer> damageTimeSupplier, Consumer<MoveInfo> takeMove) {
            this.index = index;
            this.intent = intent;
            this.takeMove = takeMove;
            this.damageSupplier = damageSupplier;
            this.damageTimeSupplier = damageTimeSupplier;
        }

        public void move() {
            takeMove.accept(this);
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

    public static enum AttackAnim {
        FAST,
        SLOW,
        HOP,
        JUMP,
        MOVE,
    }

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {String.class, String.class, int.class, float.class, float.class, float.class, float.class, String.class, float.class, float.class, boolean.class})
    public static class MonsterMoveInfoPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster __instance, String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights, EnemyMoveInfo ___move) {
            if (__instance instanceof BaseMonster) {
                ((BaseMonster) __instance).currMove = ___move;
            }
        }
    }
}
