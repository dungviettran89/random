package us.cuatoi.random;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class RedMart2 {
    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) throws IOException {
        String productUrl = "https://s3-ap-southeast-1.amazonaws.com/geeks.redmart.com/coding-problems/products.csv";
        int maxVol = 45 * 30 * 35 + 1;
        int count = 20000;
        try (Scanner scanner = new Scanner(new URL(productUrl).openStream())) {
            scanner.useDelimiter("\\D");
            try (DB db = DBMaker.tempFileDB().fileMmapEnableIfSupported().fileDeleteAfterClose().make()) {
                int[] id = new int[count];
                int[] price = new int[count];
                int[] vol = new int[count];
                int[] weight = new int[count];
                Map<String, Integer> max = db.treeMap("max")
                        .keySerializer(Serializer.STRING)
                        .valueSerializer(Serializer.INTEGER_PACKED)
                        .create();
                for (int i = 0; i < count; i++) {
                    id[i] = scanner.nextInt();
                    price[i] = scanner.nextInt();
                    vol[i] = scanner.nextInt() * scanner.nextInt() * scanner.nextInt();
                    weight[i] = scanner.nextInt();
                }
                for (int j = 0; j < maxVol; j++) {
                    max.put(key("max", 0, j), 0);
                    max.put(key("maxSum", 0, j), 0);
                    max.put(key("maxWeight", 0, j), 0);
                }
                for (int i = 1; i < count; i++) {
                    System.out.printf("Processing %s %s %s %s %s\n",i,id[i],price[i],vol[i],weight[i]);
                    for (int j = 0; j < maxVol; j++) {
                        int iMinus1 = i - 1;
                        max.put(key("max", i, j), getSafe(max, iMinus1, j, "max"));
                        max.put(key("maxWeight", i, j), getSafe(max, iMinus1, j, "maxWeight"));
                        max.put(key("maxSum", i, j), getSafe(max, iMinus1, j, "maxSum"));
                        if (vol[i] <= j) continue;

                        int jMinusVoli = j - vol[i];
                        int newPrice = getSafe(max, iMinus1, jMinusVoli, "max") + price[i];
                        if (newPrice < getSafe(max, i, j, "max")) continue;
                        int newWeight = getSafe(max, iMinus1, jMinusVoli, "maxWeight") + weight[i];
                        if (newPrice == getSafe(max, i, j, "max") && getSafe(max, i, j, "maxWeight") < newWeight)
                            continue;
                        max.put(key("max", i, j), newPrice);
                        max.put(key("maxWeight", i, j), newWeight);
                        max.put(key("maxSum", i, j), getSafe(max, iMinus1, jMinusVoli, "maxSum") + id[i]);
                    }
                }
                int globalMax = 0;
                int globalMaxWeight = Integer.MAX_VALUE;
                int globalMaxSum = 0;
                for (int j = 0; j < maxVol; j++) {
                    int curMax = getSafe(max, count - 1, 1, "max");
                    if (globalMax > curMax) continue;
                    int curWeight = getSafe(max, count - 1, 1, "maxWeight");
                    if (globalMax == curMax && globalMaxWeight < curWeight) continue;
                    globalMax = curMax;
                    globalMaxWeight = curWeight;
                    globalMaxSum = getSafe(max, count - 1, 1, "maxSum");
                }
                System.out.println("globalMax=" + globalMax);
                System.out.println("globalMaxWeight=" + globalMaxWeight);
                System.out.println("globalMaxSum=" + globalMaxSum);
            }
        }
    }

    private static Integer getSafe(Map<String, Integer> max, int i, int j, String name) {
        Integer value = max.get(key(name, i, j));
        return value != null ? value : 0;
    }

    @NotNull
    private static String key(String name, int i, int j) {
        return name + i + "-" + j;
    }
}
