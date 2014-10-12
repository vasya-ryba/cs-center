package Graphs;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Noli nocere!
 */
public class NonOrientAvailable {

	static void dfs(ArrayList<Integer>[] a, int v) {
		used[v] = true;
		for(int i = 0; i < a[v].size(); i++) {
			if(!used[a[v].get(i)]) {
				dfs(a, a[v].get(i));
			}
		}
	}

	static Scanner in;
	static boolean[] used;

	public static void main(String[] args) {
		in = new Scanner(System.in);
		int n = in.nextInt() + 1;
		int m = in.nextInt();
		ArrayList<Integer>[] a = new ArrayList[n];
		for(int i = 0; i < n; i++) {
			a[i] = new ArrayList<Integer>();
		}
		used = new boolean[n];
		int x, y;
		for(int i = 0; i < m; i++) {
			x = in.nextInt();
			y = in.nextInt();
			a[x].add(y);
			a[y].add(x);
		}
		int start = in.nextInt();
		int fin = in.nextInt();
		dfs(a, start);
		System.out.print(used[fin] ? "True" : "False");
		System.out.close();
	}
}
