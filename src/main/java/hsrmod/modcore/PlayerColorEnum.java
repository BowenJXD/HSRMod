package hsrmod.modcore;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

// 以下为原版人物枚举、卡牌颜色枚举扩展的枚举，需要写，接下来要用
// 注意此处是在 MyCharacter 类内部的静态嵌套类中定义的新枚举值
// 不可将该定义放在外部的 MyCharacter 类中，具体原因见《高级技巧 / 01 - Patch / SpireEnum》
public class PlayerColorEnum {
    public static AbstractPlayer.PlayerClass STELLA_CHARACTER = AbstractPlayer.PlayerClass.add("STELLA");

    // ***将CardColor和LibraryType的变量名改为你的角色的颜色名称，确保不会与其他mod冲突***
    // ***并且名称需要一致！***
    public static final AbstractCard.CardColor HSR_PINK = AbstractCard.CardColor.add("HSRPINK");
}
