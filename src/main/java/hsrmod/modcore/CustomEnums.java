package hsrmod.modcore;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CustomEnums
{
    // Paths
    public static AbstractCard.CardTags TRAILBLAZE = AbstractCard.CardTags.TRAILBLAZE;
    public static AbstractCard.CardTags ELATION = AbstractCard.CardTags.ELATION;
    public static AbstractCard.CardTags DESTRUCTION = AbstractCard.CardTags.DESTRUCTION;
    public static AbstractCard.CardTags NIHILITY = AbstractCard.CardTags.NIHILITY;
    public static AbstractCard.CardTags PROPAGATION = AbstractCard.CardTags.PROPAGATION;
    public static AbstractCard.CardTags PRESERVATION = AbstractCard.CardTags.PRESERVATION;
    public static AbstractCard.CardTags THE_HUNT = AbstractCard.CardTags.THE_HUNT;
    public static AbstractCard.CardTags ERUDITION = AbstractCard.CardTags.ERUDITION;
    public static AbstractCard.CardTags ABUNDANCE = AbstractCard.CardTags.ABUNDANCE;
    public static AbstractCard.CardTags REMEMBRANCE = AbstractCard.CardTags.REMEMBRANCE;
    
    public static AbstractCard.CardTags FOLLOW_UP = AbstractCard.CardTags.FOLLOW_UP;
    public static AbstractCard.CardTags ENERGY_COSTING = AbstractCard.CardTags.ENERGY_COSTING;
    public static AbstractCard.CardTags DEBUFF = AbstractCard.CardTags.DEBUFF;
    public static AbstractCard.CardTags ENTANGLE;
    
    public static AbstractCard.CardTags REVIVE;
    public static AbstractCard.CardTags RUAN_MEI;
    
    public static AbstractPower.PowerType STATUS;
    
    public static AbstractCard.CardTags[] getPathTags() {
        return new AbstractCard.CardTags[] {TRAILBLAZE, ELATION, NIHILITY, DESTRUCTION, PROPAGATION, PRESERVATION, THE_HUNT, ERUDITION, ABUNDANCE, /*REMEMBRANCE*/};
    }
}