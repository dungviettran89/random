package us.cuatoi.random;

import java.net.URL;
import java.util.Scanner;
import java.util.TreeSet;

public class RedMart {
    public static void main(String[] args) throws Exception {
        String mapUrl = "http://s3-ap-southeast-1.amazonaws.com/geeks.redmart.com/coding-problems/map.txt";
        try (Scanner scanner = new Scanner(new URL(mapUrl).openStream())) {
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            int[][] data = new int[row][col];
            int[][] path = new int[row][col];
            int[][] origin = new int[row][col];
            int[] difX = new int[]{1, 0, -1, 0};
            int[] difY = new int[]{0, 1, 0, -1};
            TreeSet<Integer> heights = new TreeSet<>();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    data[i][j] = scanner.nextInt();
                    path[i][j] = 1;
                    origin[i][j] = data[i][j];
                    heights.add(data[i][j]);
                }
            }
            System.out.printf("Bounds: [%s,%s]\n", heights.first(), heights.last());
            int maxPath = -1;
            int maxDifferent = -1;
            for (Integer h : heights) {
                //#System.out.println("Checking " + h);
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        if (data[i][j] == h) {
                            //check 4 direction
                            for (int d = 0; d < difX.length; d++) {
                                int di = i + difX[d];
                                int dj = j + difY[d];
                                //check bound and is higher
                                if (0 <= di && di < row && 0 <= dj && dj < col && data[i][j] > data[di][dj]) {
                                    if (path[di][dj] + 1 > path[i][j]) {
                                        path[i][j] = path[di][dj] + 1;
                                        origin[i][j] = origin[di][dj];
                                    }
                                }
                            }
                        }
                        if (path[i][j] >= maxPath) {
                            if (data[i][j] - origin[i][j] > maxDifferent) {
                                maxPath = path[i][j];
                                maxDifferent = data[i][j] - origin[i][j];
                                System.out.println("i=" + i);
                                System.out.println("j=" + j);
                                System.out.println("maxPath=" + maxPath);
                                System.out.println("maxDifferent=" + maxDifferent);
                            }
                        }
                    }
                }
            }
            System.out.println("maxPath=" + maxPath);
            System.out.println("maxDifferent=" + maxDifferent);
        }
    }
}
