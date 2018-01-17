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
        int maxVol = 45 * 30 * 35 + 1;
        int count = 20000;
        try (Scanner scanner = new Scanner(new URL(productUrl).openStream())) {
            scanner.useDelimiter("\\D");
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

            int max[] = new int[maxVol];
            int maxWeight[] = new int[maxVol];
            int maxSum[] = new int[maxVol];
            int lastMax[] = new int[maxVol];
            int lastWeight[] = new int[maxVol];
            int lastSum[] = new int[maxVol];
            for (int j = 0; j < maxVol; j++) {
                max[j] = 0;
                maxSum[j] = 0;
                maxWeight[j] = 0;
                lastMax[j] = 0;
                lastWeight[j] = 0;
                lastSum[j] = 0;
            }
            for (int i = 1; i < count; i++) {
                System.out.println("Checking " + i);
                for (int j = 0; j < maxVol; j++) {
                    max[j] = lastMax[j];
                    maxWeight[j] = lastWeight[j];
                    maxSum[j] = lastSum[j];
                    if (vol[i] > j) continue;

                    int prevVol = j - vol[i];
                    int newPrice = lastMax[prevVol] + price[i];
                    if (newPrice < max[j]) continue;
                    int newWeight = lastWeight[prevVol] + weight[i];
                    if (newPrice == max[j] && maxWeight[j] < newWeight) continue;
                    max[j] = newPrice;
                    maxWeight[j] = newWeight;
                    maxSum[j] = lastSum[prevVol] + id[i];
                }
            }
            int globalMax = 0;
            int globalMaxWeight = Integer.MAX_VALUE;
            int globalMaxSum = 0;
            int found = 0;
            for (int j = 0; j < maxVol; j++) {
                int curMax = max[j];
                if (globalMax > curMax) continue;
                int curWeight = maxWeight[j];
                if (globalMax == curMax && globalMaxWeight < curWeight) continue;
                globalMax = curMax;
                globalMaxWeight = curWeight;
                globalMaxSum = maxSum[j];
                found = j;
            }
            System.out.println("found=" + found);
            System.out.println("globalMax=" + globalMax);
            System.out.println("globalMaxWeight=" + globalMaxWeight);
            System.out.println("globalMaxSum=" + globalMaxSum);
        }
    }
}
