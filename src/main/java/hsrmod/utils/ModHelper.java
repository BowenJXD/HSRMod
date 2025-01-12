package hsrmod.utils;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.SummonedPower;

import java.util.*;
import java.util.function.Predicate;

public class ModHelper {
    public static void addToBotAbstract(Lambda func) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }

    public static void addToTopAbstract(Lambda func) {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }

    public interface Lambda extends Runnable {}
    
    public static List<AbstractGameAction> mostBotList = new ArrayList<>();
    
    public static void addToMostBot(Lambda func){
        AbstractGameAction action = new AbstractGameAction() {
            @Override
            public void update() {
                isDone = !isDone;
                if (!isDone) return;
                if (new HashSet<>(mostBotList).containsAll(AbstractDungeon.actionManager.actions)) {
                    func.run();
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(this);
                }
            }
        };
        mostBotList.add(action);
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static <T extends Enum<T>> T getRandomEnumValue(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        int randomIndex = new Random().random(values.length - 1);
        return values[randomIndex];
    }
    
    public static int getPowerCount(AbstractCreature creature, String powerID) {
        return creature.hasPower(powerID) ? creature.getPower(powerID).amount : 0;
    }
    
    public static List<FindResult> findCardsInGroup(Predicate<AbstractCard> predicate, CardGroup group) {
        List<FindResult> result = new ArrayList<>();
        for (AbstractCard card : group.group) {
            if (predicate.test(card)) {
                FindResult findResult = new FindResult();
                findResult.card = card;
                findResult.group = group;
                result.add(findResult);
            }
        }
        return result;
    }
    
    public static List<FindResult> findCards(Predicate<AbstractCard> predicate, boolean hand, boolean discard, boolean draw, boolean exhaust, boolean limbo) {
        List<FindResult> result = new ArrayList<>();
        if (hand) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.hand));
        if (discard) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.discardPile));
        if (draw) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.drawPile));
        if (exhaust) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.exhaustPile));
        if (limbo) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.limbo));
        return result;
    }
    
    public static List<FindResult> findCards(Predicate<AbstractCard> predicate, boolean shuffle) {
        List<FindResult> result = findCards(predicate);
        if (shuffle) {
            Collections.shuffle(result, AbstractDungeon.cardRandomRng.random);
        }
        return result;
    }
    
    public static List<FindResult> findCards(Predicate<AbstractCard> predicate) {
        return findCards(predicate, true, true, true, true, false);
    }
    
    public static class FindResult {
        public AbstractCard card;
        public CardGroup group;
    }
    
    public static <T> T getRandomElement(List<T> list, Random rand) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(rand.random(list.size() - 1));
    }
    
    public static <T> T getRandomElement(List<T> list, Random random, Predicate<T> predicate) {
        List<T> filtered = new ArrayList<>();
        for (T element : list) {
            if (predicate.test(element)) {
                filtered.add(element);
            }
        }
        if (filtered.isEmpty()) {
            return null;
        }
        return getRandomElement(filtered, random);
    }
    
    public static <T> List<T> getRandomElements(List<T> list,  Random random, int count) {
        count = Math.min(count, list.size());

        List<T> shuffledList = new ArrayList<>(list);
        Collections.shuffle(shuffledList, random.random);  // Randomly shuffle the list
        return shuffledList.subList(0, count);  // Return the first x elements
    }

    public static AbstractMonster betterGetRandomMonster() {
        return getRandomMonster(m -> !(m.isDying || m.isEscaping || m.halfDead || m.currentHealth <= 0), true);
    }
    
    public static boolean check(AbstractCreature m){
        return !(m.isDying || m.isEscaping || m.halfDead || m.currentHealth <= 0);
    }
    
    public static AbstractMonster getRandomMonster(Predicate<AbstractMonster> predicate, boolean aliveOnly) {
        MonsterGroup group = AbstractDungeon.getCurrRoom().monsters;
        Random rng = AbstractDungeon.cardRandomRng;
        if (group.areMonstersBasicallyDead()) {
            return null;
        } else {
            ArrayList tmp;
            Iterator var5;
            AbstractMonster m;
            if (predicate == null) {
                if (aliveOnly) {
                    tmp = new ArrayList();
                    var5 = group.monsters.iterator();

                    while(var5.hasNext()) {
                        m = (AbstractMonster)var5.next();
                        if (!m.halfDead && !m.isDying && !m.isEscaping) {
                            tmp.add(m);
                        }
                    }

                    if (tmp.size() <= 0) {
                        return null;
                    } else {
                        return (AbstractMonster)tmp.get(rng.random(0, tmp.size() - 1));
                    }
                } else {
                    return (AbstractMonster)group.monsters.get(rng.random(0, group.monsters.size() - 1));
                }
            } else if (group.monsters.size() == 1) {
                if (predicate.test((AbstractMonster)group.monsters.get(0))) {
                    return (AbstractMonster)group.monsters.get(0);
                } else {
                    return null;
                }
            } else if (aliveOnly) {
                tmp = new ArrayList();
                var5 = group.monsters.iterator();

                while(var5.hasNext()) {
                    m = (AbstractMonster)var5.next();
                    if (!m.halfDead && !m.isDying && !m.isEscaping && predicate.test(m)) {
                        tmp.add(m);
                    }
                }

                if (tmp.size() == 0) {
                    return null;
                } else {
                    return (AbstractMonster)tmp.get(rng.random(0, tmp.size() - 1));
                }
            } else {
                tmp = new ArrayList();
                var5 = group.monsters.iterator();

                while(var5.hasNext()) {
                    m = (AbstractMonster)var5.next();
                    if (predicate.test(m)) {
                        tmp.add(m);
                    }
                }

                return (AbstractMonster)tmp.get(rng.random(0, tmp.size() - 1));
            }
        }
    }
    
    public static boolean hasRelic(String relicID) {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(HSRMod.makePath(relicID));
    }
    
    public static void applyEnemyPowersOnly(DamageInfo info, AbstractCreature target, boolean reset) {
        if (reset) {
            info.output = info.base;
            info.isModified = false;
        }
        float tmp = (float)info.output;
        Iterator var3 = target.powers.iterator();

        AbstractPower p;
        while(var3.hasNext()) {
            p = (AbstractPower)var3.next();
            tmp = p.atDamageReceive(tmp, info.type);
            if (info.base != info.output) {
                info.isModified = true;
            }
        }

        var3 = target.powers.iterator();

        while(var3.hasNext()) {
            p = (AbstractPower)var3.next();
            tmp = p.atDamageFinalReceive(tmp, info.type);
            if (info.base != info.output) {
                info.isModified = true;
            }
        }

        if (tmp < 0.0F) {
            tmp = 0.0F;
        }

        info.output = MathUtils.floor(tmp);
    }
    
    public static void killAllMinions(){
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if (!m.isDead && !m.isDying && (m.hasPower(MinionPower.POWER_ID) || m.hasPower(SummonedPower.POWER_ID) || m.halfDead)) {
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
            }
        }
    }
    
    public static boolean moreDamageAscension(AbstractMonster.EnemyType type) {
        int level = 2;
        switch (type) {
            case NORMAL:
                level = 2;
                break;
            case ELITE:
                level = 3;
                break;
            case BOSS:
                level = 4;
                break;
        }
        return AbstractDungeon.ascensionLevel >= level;
    }
    
    public static boolean moreHPAscension(AbstractMonster.EnemyType type) {
        int level = 7;
        switch (type) {
            case NORMAL:
                level = 7;
                break;
            case ELITE:
                level = 8;
                break;
            case BOSS:
                level = 9;
                break;
        }
        return AbstractDungeon.ascensionLevel >= level;
    }
    
    public static boolean specialAscension(AbstractMonster.EnemyType type) {
        int level = 17;
        switch (type) {
            case NORMAL:
                level = 17;
                break;
            case ELITE:
                level = 18;
                break;
            case BOSS:
                level = 19;
                break;
        }
        return AbstractDungeon.ascensionLevel >= level;
    }
}
