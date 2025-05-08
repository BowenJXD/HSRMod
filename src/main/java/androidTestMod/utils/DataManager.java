package androidTestMod.utils;

import androidTestMod.AndroidTestMod;
import com.megacrit.cardcrawl.android.mods.AssetLoader;
import com.megacrit.cardcrawl.core.Settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/*
 * 数据管理类Singleton，能通过CSV读取技能信息
 */
public class DataManager {
    private static DataManager instance = null;

    // set the charset to be able to read chinese
    public static final String charset = "GBK";
    
    public Map<String, String[]> cardData;
    public static final String CARD_CSV_ZHS = "localization/ZHS/cardData.csv";
    public static final String CARD_CSV_ENG = "localization/ENG/cardData.csv";
    public Map<String, String[]> relicData;
    public static final String RELIC_CSV = "localization/ZHS/relicData.csv";
    public Map<String, String[]> monsterData;
    public static final String MONSTER_CSV = "localization/ZHS/monsterData.csv";
    
    public static final String RELIC_JSON = "localization/ZHS/relics.json";
    
    private DataManager() {
        if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
            cardData = parseCSV(CARD_CSV_ZHS);
        } else {
            cardData = parseCSV(CARD_CSV_ENG);
        }
        relicData = parseCSV(RELIC_CSV);
        monsterData = parseCSV(MONSTER_CSV);
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public static Map<String, String[]> parseCSV(String filePath) {
        Map<String, String[]> resultMap = new HashMap<>();
        // FileHandle fileHandle = Gdx.files.internal(filePath);
        String file = AssetLoader.getString(AndroidTestMod.MOD_NAME, filePath);
        try {
            String[] lines = file.split("\n");
            for (String line : lines) {
                // 使用自定义的解析方法
                String[] columns = parseCSVLine(line);
                if (columns.length > 1) { // 确保至少有两列数据
                    String primaryKey = columns[0];
                    if (primaryKey.isEmpty()) continue;
                    String[] values = new String[columns.length - 1];
                    System.arraycopy(columns, 1, values, 0, columns.length - 1);
                    System.out.println("key: " + primaryKey + " values " + Arrays.toString(values));
                    resultMap.put(primaryKey, values);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return resultMap;
    }

    private static String[] parseCSVLine(String line) {
        // 使用 StringBuilder 来构建当前字段
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        List<String> fields = new ArrayList<>();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes; // 切换引号状态
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString()); // 字段结束
                field.setLength(0); // 清空字段构建器
            } else {
                field.append(c); // 继续构建当前字段
            }
        }
        // 添加最后一个字段
        fields.add(field.toString());

        return fields.toArray(new String[0]);
    }
    
    public String getCardData(String key, CardDataCol col) {
        String result = cardData.get(key)[col.ordinal()];
        switch (col) {
            case Damage:
            case ToughnessReduction:
            case Block:
            case MagicNumber:
                if (Objects.equals(result, "")) {
                    return 0 + "";
                }
                break;
            case UpgradeDamage:
            case UpgradeToughnessReduction:
            case UpgradeBlock:
            case UpgradeMagicNumber:
            case UpgradeCost:
                if (Objects.equals(result, "")) {
                    return NULL_INT + "";
                }
                break;
        }
        return result;
    }
    
    public int getCardDataInt(String key, CardDataCol col) {
        return Integer.parseInt(getCardData(key, col));
    }
    
    public String getRelicData(String key, RelicDataCol col) {
        String result = relicData.get(key)[col.ordinal()];
        switch (col) {
            case MagicNumber:
                if (Objects.equals(result, "")) {
                    return 0 + "";
                }
                break;
        }
        return result;
    }
    
    public int getRelicDataInt(String key, RelicDataCol col) {
        return Integer.parseInt(getRelicData(key, col));
    }
    
    public boolean getRelicDataBoolean(String key, RelicDataCol col) {
        return Boolean.parseBoolean(getRelicData(key, col));
    }
    
    public String getMonsterData(String key, MonsterDataCol col) {
        String result = monsterData.get(key)[col.ordinal()];
        switch (col) {
            case HP1:
            case HP2:
            case TV1:
            case TV2:
                if (Objects.equals(result, "")) {
                    return 0 + "";
                }
                break;
        }
        return result;
    }
    
    public int getMonsterDataInt(String key, MonsterDataCol col) {
        return Integer.parseInt(getMonsterData(key, col));
    }
    
    public static final int NULL_INT = -9;

    public static class EnglishReplacer {
        public static void main (String[] args) {
            FileTextReplacer.replaceENG();
        }
    }
    
    public static class ChineseReplacer {
        public static void main (String[] args) {
            FileTextReplacer.replaceZHS();
        }
    }
    
    public static class FileTextReplacer {
        private static void replaceZHS() {
            // 替换规则
            Map<String, String> replacements = new HashMap<>();
            // replacements.put(" D ", " !D! ");
            // replacements.put(" B ", " !B! ");
            // replacements.put(" M ", " !M! ");
            // replacements.put(" E ", " [E] ");
            replacements.put("【", " hsrmod:");
            replacements.put("】", " ");
            replacements.put("〖", " ");
            replacements.put("〗", " ");
            replacements.put("『", " *");
            replacements.put("』", " ");
            replacements.put("「", " *「");
            replacements.put("」", "」 ");
            // replacements.put("能量", " [E] ");
            replacements.put("追击", " hsrmod:追击 ");
            replacements.put("充能", " [hsrmod:ChargeIcon] ");
            replacements.put("若击破", "若 hsrmod:击破 ");
            replacements.put("击破后", " hsrmod:击破 后");
            replacements.put("击破伤害", " hsrmod:击破伤害 ");
            replacements.put("持续伤害", " hsrmod:持续伤害 ");
            replacements.put("临时生命", " 临时生命 ");
            replacements.put("弹射", " hsrmod:弹射 ");
            replacements.put("耗能", " hsrmod:耗能 ");
            replacements.put("消耗。", " 消耗 。");
            replacements.put("虚无。", " 虚无 。");
            replacements.put("固有。", " 固有 。");
            replacements.put("保留。", " 保留 。");

            // 读取文件并替换文本
            replaceTextInFile(CARD_CSV_ZHS, replacements);

            replacements.clear();
            replacements.put("乘胜 hsrmod:追击 ", "乘胜追击");
            replacements.put("hsrmod:超 hsrmod:击破伤害 ", "hsrmod:超击破伤害 ");
            replacements.put("  ", " ");

            replaceTextInFile(CARD_CSV_ZHS, replacements);
        }

        private static void replaceENG() {
            // 替换规则
            Map<String, String> replacements = new HashMap<>();
            replacements.put("`", "hsrmod:");
            //replacements.put("能量", " [E] ");
            replacements.put("Follow Up", " hsrmod:Follow-Up ");
            replacements.put("Charge", " [hsrmod:ChargeIcon] ");
            replacements.put("Break Damage", " hsrmod:Break-Damage ");
            replacements.put("Break Effect", "Break-Effect ");
            replacements.put("Break Efficiency", "Break-Efficiency ");
            replacements.put("DoT", " hsrmod:DoT ");
            replacements.put("Wind Shear", "Wind-Shear");
            replacements.put("Bounce", " hsrmod:Bounce ");
            replacements.put("temporary HP", "temporary_hp");

            // 读取文件并替换文本
            replaceTextInFile(CARD_CSV_ENG, replacements);

            replacements.clear();
            replacements.put(" break ", " hsrmod:break ");
            replacements.put("  ", " ");

            replaceTextInFile(CARD_CSV_ENG, replacements);
        }
        
        private static void replaceTextInFile(String filePath, Map<String, String> replacements) {
            filePath = "src/main/resources/" + filePath;
            
            // 读取文件内容，使用 UTF-8 编码
            StringBuilder content = new StringBuilder();
            Path path = Paths.get(filePath);
            try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName(charset))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                System.out.println("读取文件时出错: " + e.getMessage());
                return;
            }

            // 进行文本替换
            String text = content.toString();
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                text = text.replaceAll(entry.getKey(), entry.getValue());
            }

            // 将替换后的内容写回文件，使用 UTF-8 编码
            try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName(charset))) {
                writer.write(text);
            } catch (IOException e) {
                System.out.println("写入文件时出错: " + e.getMessage());
            }

            System.out.println("文本替换完成并写入文件: " + filePath);
        }
    }
}
