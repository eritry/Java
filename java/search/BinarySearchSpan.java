package search;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import javafx.util.*;

public class BinarySearchSpan {
	static int a[];
	
	// pre: for all i >= -1 && i <= a.length : a[i] >= a[i + 1]
	// && type a[i], x = int
	static int iterativeSearch(int x) {
		//l = -1 >= -1 && r = a.length <= a.length
		int l = -1, r = a.length;
		//r - l >= 1
		//a[r] && a[l] exists
		//a[r] <= a[l] && a[r] <= x
		//r' - l' < r - l
 	   	while (r - l > 1) {
 	   		int m = (r + l) / 2;
			//l < m < r && m >= 0 && m < a.length => a[m] exists
			if (a[m] <= x) {
				//a[r] && a[l] exists
				//a[r] < a[l] && a[r] < x
 	   			r = m;
 	   			//r = m
				//r' < r && l = l' < r' => r' - l' < r - l
 	   		} else {
				//a[r] && a[l] exists
				//a[r] < a[l] && a[r] < x
				l = m;
				//l = m
				//l < l' && r = r' > l' => r' - l' < r - l
			}
 	   	}
 	   	return r;
	}
	//post: a[r] <= x && a[i]' = a[i] && a[r - 1] > x


	//pre: l >= -1 && r <= a.length && l <= r && i >= -1 && i <= a.length
	//&& for all i >= -1 && i <= a.length : a[i] > a[i + 1]
	//&& Inv = a[l] < x && a[r] <= x
	//type a[i], l, r, x = int
	//r' - l' < r - l
	static int recursiveSearch(int x, int l, int r) {
		if (r - l <= 1) {
			//a[r] >= finder && a[l] = a[r - 1] > x
			//Inv
			return l;
		}
		int m = (r + l) / 2;
		//l < m < r && m >= 0 && m < a.length => a[m] exists
		if (a[m] < x) {
			//a[r] && a[l] exists
			//a[r] < a[l] && a[r] <= x => Inv
			return recursiveSearch(x, l, m);
			//r' = m && l = l' && r' < r => r' - l' < r - l
		} else {
			//a[r] && a[l] exists
			//a[l] > a[r] && a[r] <= x => Inv
			return recursiveSearch(x, m, r);
			//l' = m && l < l' && r' = r => r' - l' < r - l
		}
	}
	//post: a[l] < x && a[r] <= x (Inv)
	//&& a[i]' = a[i] && a[l] >= x && a[l + 1] = a[r]< x

	//l >= -1 && r <= a.length
	static int recursiveSearch(int x) {
		return recursiveSearch(x, -1, a.length);
	}
	
    public static void main(String[] args) {
 		int x = Integer.parseInt(args[0]);
 		int n = args.length - 1;

 		a = new int[args.length - 1];
 		for (int i = 1; i < args.length; i++) {
			a[i - 1] = Integer.parseInt(args[i]);
 	   	}

 	   	// l = -1 >= -1, r = a.length - 1 <= a.length
 	   	int left = iterativeSearch(x);
 	   	int right = recursiveSearch(x);
 	   	if (right >= left) {
 	   		System.out.println(left + " " + (right - left + 1));
 	   	} else {
 	   		System.out.println(left + " " + 0);
 	   	}
 	   	
    }	
}