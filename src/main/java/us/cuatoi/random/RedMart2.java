package us.cuatoi.random;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class RedMart2 {
    public static void main(String[] args) throws IOException {
        String productUrl = "https://s3-ap-southeast-1.amazonaws.com/geeks.redmart.com/coding-problems/products.csv";
        int maxVol = 45 * 30 * 35;
        int count = 20000;
        try (Scanner scanner = new Scanner(new URL(productUrl).openStream())) {
            scanner.useDelimiter("\\D");
            int[] max = new int[maxVol + 1];
            int[] maxWeight = new int[maxVol + 1];
            List<HashSet<Integer>> ids = new ArrayList<>();
            int[] id = new int[count];
            int[] price = new int[count];
            int[] vol = new int[count];
            int[] weight = new int[count];
            for (int i = 0; i < count; i++) {
                id[i] = scanner.nextInt();
                price[i] = scanner.nextInt();
                vol[i] = scanner.nextInt() * scanner.nextInt() * scanner.nextInt();
                weight[i] = scanner.nextInt();
            }
            for (int i = 0; i < max.length; i++) {
                max[i] = 0;
                maxWeight[i] = 0;
                ids.add(new HashSet<>());
            }
            for (int i = 0; i < count; i++) {
                for (int j = vol[i] + 1; j < max.length; j++) {
                    int checkedVol = j - vol[i];
                    if (ids.get(checkedVol).contains(id[i])) continue;
                    int newMax = price[i] + max[checkedVol];
                    if (newMax < max[j]) continue;
                    int newWeight = weight[i] + maxWeight[checkedVol];
                    if (newMax == max[j] && newWeight > maxWeight[j]) continue;
                    max[j] = newMax;
                    maxWeight[j] = newWeight;
                    ids.set(j, new HashSet<>(ids.get(checkedVol)));
                    ids.get(j).add(id[i]);
                }
            }
            int found = 0;
            int globalMax = 0;
            int globalWeight = Integer.MAX_VALUE;
            for (int j = 0; j < max.length; j++) {
                if (globalMax > max[j]) continue;
                if (globalMax == max[j] && globalWeight < maxWeight[j]) continue;
                globalMax = max[j];
                globalWeight = maxWeight[j];
                found = j;
            }
            System.out.println("found=" + found);
            System.out.println("globalMax=" + globalMax);
            System.out.println("globalWeight=" + globalWeight);
            System.out.println("ids=" + ids.get(found));
            System.out.println("sum=" + ids.get(found).stream().reduce(Integer::sum).orElse(0));
        }
    }
}
