package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.monsters.BaseMonster;
import hsrmod.monsters.Exordium.*;
import hsrmod.monsters.TheCity.*;
import hsrmod.utils.GeneralUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SomethingInTheMirror extends BaseMonster {
    public static final String ID = SomethingInTheMirror.class.getSimpleName();
    
    public static List<Class<? extends BaseMonster>> normalClasses;
    public static List<Class<? extends BaseMonster>> strongerClasses;
    
    public SomethingInTheMirror(float x, float y){
        super(ID, 200, 200, x, y);
        
        addMove(Intent.UNKNOWN, mi->{
            addToBot(new SuicideAction(this));
            BaseMonster monster = generateEnemy(x, y);
            monster.usePreBattleAction();
            monster.maxHealth = this.maxHealth;
            monster.currentHealth = this.currentHealth;
            monster.healthBarUpdatedEvent();
            addToBot(new SpawnMonsterAction(monster, false));
        });
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }
    
    BaseMonster generateEnemy(float x, float y) {
        BaseMonster result;
        Class<? extends BaseMonster> selectedClass = GeneralUtil.getRandomElement(specialAs ? strongerClasses : normalClasses, AbstractDungeon.aiRng);
        try {
            assert selectedClass != null;
            Constructor<? extends BaseMonster> constructor = selectedClass.getDeclaredConstructor(float.class, float.class);
            constructor.setAccessible(true);
            result = constructor.newInstance(x, y);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class " + selectedClass.getSimpleName() +
                    " missing required two-float constructor", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot instantiate abstract class: " +
                    selectedClass.getSimpleName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Constructor access violation in: " +
                    selectedClass.getSimpleName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Constructor exception in: " +
                    selectedClass.getSimpleName(), e.getCause());
        }
        return result;
    }
    
    static {
        normalClasses = new ArrayList<>(Arrays.asList(
                Hound.class,
                Spider.class,
                CloudKnightsPatroller.class,
                Ballistarius.class,
                Birdskull.class,
                BubbleHound.class,
                MrDomescreen.class,
                SpringLoader.class,
                IlluminationDragonfish.class,
                FieldPersonnel.class,
                MaskOfNoThought.class,
                SilvermaneSoldier.class,
                WraithWarden.class
                ));
        strongerClasses = new ArrayList<>(Arrays.asList(
                MaleficApe.class,
                AurumatonSpectralEnvoy.class,
                Direwolf.class,
                BeyondOvercooked.class,
                GuardianShadow.class,
                Stormbringer.class,
                TheAscended.class
        ));
    }
}
