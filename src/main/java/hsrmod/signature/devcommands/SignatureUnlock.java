package hsrmod.signature.devcommands;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.signature.utils.internal.SignatureHelperInternal;

import java.util.ArrayList;
import java.util.Map;

public class SignatureUnlock extends SignatureManipulator {
    public SignatureUnlock() {
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (tokens[2].equals("all")) {
            for (AbstractCard card : CardLibrary.getAllCards())
                if (SignatureHelperInternal.hasSignature(card) && !SignatureHelper.isUnlocked(card.cardID))
                    SignatureHelper.unlock(card.cardID, true);
        } else
            try {
                SignatureHelper.unlock(this.getCardID(tokens), true);
            } catch (Exception e) {
                errorMsg();
            }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> kvp : CardLibrary.cards.entrySet())
            if (SignatureHelperInternal.hasSignature(kvp.getValue()) && !SignatureHelper.isUnlocked(kvp.getKey()))
                result.add(kvp.getKey().replace(' ', '_'));
        result.add("all");
        return result;
    }
}
