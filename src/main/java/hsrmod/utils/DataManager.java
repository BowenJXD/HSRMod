package hsrmod.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
    public static final String CARD_CSV = "HSRModResources/data/cardData.csv";
    public Map<String, String[]> relicData;
    public static final String RELIC_CSV = "HSRModResources/data/relicData.csv";
    
    public static final String RELIC_JSON = "HSRModResources/localization/ZHS/relics.json";
    
    private DataManager() {
        cardData = parseCSV(CARD_CSV);
        relicData = parseCSV(RELIC_CSV);
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public static Map<String, String[]> parseCSV(String filePath) {
        Map<String, String[]> resultMap = new HashMap<>();
        FileHandle fileHandle = Gdx.files.internal(filePath);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileHandle.read(), charset))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length > 1) { // 确保至少有两列数据
                    String primaryKey = columns[0];
                    if (primaryKey.isEmpty()) continue;
                    String[] values = new String[columns.length - 1];
                    System.arraycopy(columns, 1, values, 0, columns.length - 1);
                    resultMap.put(primaryKey, values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    
    public String getCardData(String key, CardDataCol col) {
        String result = cardData.get(key)[col.ordinal()];
        switch (col) {
            case Damage:
            case Block:
            case MagicNumber:
                if (Objects.equals(result, "")) {
                    return 0 + "";
                }
                break;
            case UpgradeDamage:
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
    
    public static final int NULL_INT = -9;

    public static class FileTextReplacer {

        public static void main(String[] args) {

            // 替换规则
            Map<String, String> replacements = new HashMap<>();
            replacements.put("【", " hsrmod:");
            replacements.put("】", " ");
            replacements.put("能量", " [E] ");
            replacements.put("追击", " hsrmod:追击 ");
            replacements.put("充能", " hsrmod:充能 ");
            replacements.put("若击破", "若 hsrmod:击破 ");
            replacements.put("击破后", " hsrmod:击破 后");
            replacements.put("击破伤害", " hsrmod:击破伤害 ");
            replacements.put("持续伤害", " hsrmod:持续伤害 ");
            replacements.put("〖", " ");
            replacements.put("〗", " ");
            replacements.put("消耗。", " 消耗 。");
            replacements.put("虚无。", " 虚无 。");
            replacements.put("固有。", " 固有 。");
            replacements.put("保留。", " 保留 。");

            // 读取文件并替换文本
            replaceTextInFile(CARD_CSV, replacements);
            
            replacements.clear();
            replacements.put("乘胜 hsrmod:追击 ", "乘胜追击");
            replacements.put("  ", " ");
            
            replaceTextInFile(CARD_CSV, replacements);
        }

        private static void replaceTextInFile(String filePath, Map<String, String> replacements) {
            filePath = "src/main/resources/" + filePath;
            
            // 读取文件内容，使用 UTF-8 编码
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charset.forName(charset))) {
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
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), Charset.forName(charset))) {
                writer.write(text);
            } catch (IOException e) {
                System.out.println("写入文件时出错: " + e.getMessage());
            }

            System.out.println("文本替换完成并写入文件: " + filePath);
        }
    }
}
