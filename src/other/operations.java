package other;

/**
 * any operations needed in any of the stages
 */
public class operations {

    /**
     *
     * @param Bin 32 bit binary string
     * @return the 2's complement of Bin as an integer
     */
    public static int Complement(String Bin){
        StringBuilder res = new StringBuilder();

        if (Bin.length() > 31 && Bin.charAt(0) == '1'){
            boolean flip = false;

            for (int i = Bin.length() - 1; i >= 0; i--) {
                if (flip){
                    if (Bin.charAt(i) == '0') res.append(1);
                    else res.append(0);
                }
                else {
                    res.append(Bin.charAt(i));
                    if (Bin.charAt(i) == '1') flip = true;
                }
            }

        }
        else return Integer.parseInt(Bin,2);

        return Integer.parseInt(res.reverse().toString(),2) * -1;
    }

}
