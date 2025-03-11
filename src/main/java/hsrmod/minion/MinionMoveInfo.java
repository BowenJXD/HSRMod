package hsrmod.minion;

public class MinionMoveInfo {
    public byte nextMove;
    public AbstractPlayerMinion.MinionIntent intent;
    public int baseDamage;
    public int multiplier;
    public boolean isMultiDamage;

    public MinionMoveInfo(byte nextMove, AbstractPlayerMinion.MinionIntent intent, int intentBaseDmg, int multiplier, boolean isMultiDamage) {
        this.nextMove = nextMove;
        this.intent = intent;
        this.baseDamage = intentBaseDmg;
        this.multiplier = multiplier;
        this.isMultiDamage = isMultiDamage;
    }
}
