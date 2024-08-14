package hsrmod.utils;

import java.io.*;
import java.util.*;

/*
 * 数据管理类Singleton，能通过CSV读取技能信息
 */
public class DataManager {
    private static DataManager instance = null;

    // set the charset to be able to read chinese
    static final String charset = "GBK";
    
    public Map<String, String[]> cardData;
    public static final String CSV_FILE = "C:\\Games\\SlayTheSpireMod\\SlayTheSpireMod\\src\\main\\resources\\HSRModResources\\data\\cardData.csv";
    
    private DataManager() {
        cardData = parseCSV(CSV_FILE);
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public static Map<String, String[]> parseCSV(String filePath) {
        Map<String, String[]> resultMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset))) {
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
            case UpgradeCost:
            case UpgradeDamage:
            case UpgradeBlock:
            case UpgradeMagicNumber:
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
    
    public static final int NULL_INT = -9;
}
