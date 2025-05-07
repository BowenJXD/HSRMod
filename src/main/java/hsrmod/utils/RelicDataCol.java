package hsrmod.utils;

/**
 * 详细对应的数据表格请参考 relicData.csv
 */
public enum RelicDataCol {
    /**
     * @deprecated 该字段已废弃，使用relics.json获取遗物名称
     */
    @Deprecated
    Name,
    Tier,
    /**
     * @deprecated 该字段已废弃，使用relics.json获取遗物描述
     */
    @Deprecated
    Description,
    MagicNumber,
    /**
     * @deprecated 该字段已废弃，使用relics.json获取遗物风味文本
     */
    @Deprecated
    Flavor,
    Sound,
    Destructible,
    Subtle,
    Special,
    Economic,
}
