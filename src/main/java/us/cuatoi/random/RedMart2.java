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
            int max[][] = new int[count][maxVol];
            int maxWeight[][] = new int[count][maxVol];
            int maxSum[][] = new int[count][maxVol];
            for (int i = 0; i < count; i++) {
                id[i] = scanner.nextInt();
                price[i] = scanner.nextInt();
                vol[i] = scanner.nextInt() * scanner.nextInt() * scanner.nextInt();
                weight[i] = scanner.nextInt();
            }
            for (int j = 0; j < maxVol; j++) {
                max[0][j] = 0;
                maxSum[0][j] = 0;
                maxWeight[0][j] = 0;
            }
            for (int i = 1; i < count; i++) {
                for (int j = 0; j < maxVol; j++) {
                    max[i][j] = max[i - 1][j];
                    maxWeight[i][j] = maxWeight[i - 1][j];
                    maxSum[i][j] = maxSum[i - 1][j];
                    if (vol[i] <= j) continue;

                    int newPrice = max[i - 1][j - vol[i]] + price[i];
                    if (newPrice < max[i][j]) continue;
                    int newWeight = maxWeight[i - 1][j - vol[i]] + weight[i];
                    if (newPrice == max[i][j] && maxWeight[i][j] < newWeight) continue;
                    max[i][j] = newPrice;
                    maxWeight[i][j] = newWeight;
                    maxSum[i][j] = maxSum[i - 1][j - vol[i]] + id[i];
                }
            }
            int globalMax = 0;
            int globalMaxWeight = Integer.MAX_VALUE;
            int globalMaxSum = 0;
            for (int j = 0; j < maxVol; j++) {
                int curMax = max[count - 1][j];
                if (globalMax > curMax) continue;
                int curWeight = maxWeight[count - 1][j];
                if (globalMax == curMax && globalMaxWeight < curWeight) continue;
                globalMax = curMax;
                globalMaxWeight = curWeight;
                globalMaxSum = maxSum[count - 1][j];
            }
            System.out.println("globalMax=" + globalMax);
            System.out.println("globalMaxWeight=" + globalMaxWeight);
            System.out.println("globalMaxSum=" + globalMaxSum);
        }
    }
}
