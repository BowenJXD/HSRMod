package hsrmod.signature.devcommands;

import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.signature.utils.internal.SignatureHelperInternal;

import java.util.ArrayList;

public class SignatureLock extends SignatureManipulator {
    public SignatureLock() {
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (tokens[2].equals("all")) {
            for (AbstractCard card : CardLibrary.getAllCards())
                if (SignatureHelper.isUnlocked(card.cardID))
                    SignatureHelper.unlock(card.cardID, false);
        } else
            try {
                SignatureHelper.unlock(this.getCardID(tokens), false);
            } catch (Exception e) {
                errorMsg();
            }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        for(String key : CardLibrary.cards.keySet()) {
            if (SignatureHelper.isUnlocked(key)) {
                result.add(key.replace(' ', '_'));
            }
        }
        result.add("all");
        return result;
    }
}
