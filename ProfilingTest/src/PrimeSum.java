public class PrimeSum {
    final static int MAX_INT = 2147483647;

    public static int find_the_square_root() {
        int res = -1;
        for (int i = 2; i <= MAX_INT; i++) {
            if (i * i < MAX_INT && i * i > res) {
                res = i * i;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int result = find_the_square_root();
        System.out.println(result);
    }
}