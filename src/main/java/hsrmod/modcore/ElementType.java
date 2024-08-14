package hsrmod.modcore;

public enum ElementType {
    Ice,
    Physical,
    Fire,
    Lightning,
    Wind,
    Quantum,
    Imaginary;
    
    public int getBreakDamage() {
        int result = 0;
        switch (this) {
            case Quantum:
            case Imaginary:
                result = 1;
                break;
            case Ice:
            case Lightning:
                result = 2;
                break;
            case Wind:
                result = 3;
                break;
            case Physical:
            case Fire:
                result = 4;
                break;
        }
        return result;
    }
}
