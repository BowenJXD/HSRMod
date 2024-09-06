package hsrmod.modcore;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.orbs.Lightning;

public enum ElementType {
    None,
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
            default:
                break;
        }
        return result;
    }
    
    public Color getColor() {
        Color result = Color.WHITE;
        switch (this) {
            case Ice:
                result = Color.CYAN;
                break;
            case Physical:
                result = Color.GRAY;
                break;
            case Fire:
                result = Color.RED;
                break;
            case Lightning:
                result = Color.PURPLE;
                break;
            case Wind:
                result = Color.GREEN;
                break;
            case Quantum:
                result = Color.BLUE;
                break;
            case Imaginary:
                result = Color.YELLOW;
                break;
            default:
                break;
        }
        return result;
    }
}
