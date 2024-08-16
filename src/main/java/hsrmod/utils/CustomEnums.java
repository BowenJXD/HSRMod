package hsrmod.utils;

import basemod.helpers.RelicType;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class CustomEnums
{
    @SpireEnum public static AbstractCard.CardTags FOLLOW_UP;
    @SpireEnum public static AbstractCard.CardTags ULTIMATE;
    @SpireEnum public static AbstractCard.CardTags DEBUFF;
    
    @SpireEnum public static DamageInfo.DamageType PHYSICAL;
    
    @SpireEnum public static RelicType HSR;
}