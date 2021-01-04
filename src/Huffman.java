
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class
Huffman {

    /**
     Code
     provided from previous version and modified for 2020.
     */
    public static void encode()throws IOException{
        // initialize Scanner to capture user input
        Scanner sc = new Scanner(System.in);

        ArrayList<Pair> pairs = new ArrayList<>();

        // capture file information from user and read file
        System.out.print("Enter the filename to read from/encode: ");
        String f = sc.nextLine();

        // create File object and build text String
        File file = new File(f);
        Scanner input = new Scanner(file).useDelimiter("\\z");
        String text = input.next();

        // close input file
        input.close();

        // initialize Array to hold frequencies (indices correspond to
        // ASCII values)
        int[] freq = new int[256];
        // concatenate/sanitize text String and create character Array
        // nice that \\s also consumes \n and \r
        // we can add the whitespace back in during the encoding phase

        char[] chars = text.replaceAll("\\s", "").toCharArray();

        // count character frequencies
        for(char c: chars)
            freq[c]++;

        //adding the pairs to the arraylist
        for(int i = 0; i<256; i++){
            if(freq[i]!=0){
                // this method of rounding is good enough
                Pair p = new Pair((char)i, Math.round(freq[i]*10000d/chars.length)/10000d);
                pairs.add(p);
            }
        }

        //sorting the arraylist
        Collections.sort(pairs);

        //Queues for storing Pairs and Pairtree
        Queue<BinaryTree<Pair>> S = new Queue<BinaryTree<Pair>>();
        Queue<BinaryTree<Pair>> T = new Queue<BinaryTree<Pair>>();

        //adding the created pair in the pair queue
        BinaryTree<Pair>temp;
        for(Pair p : pairs){
            temp = new BinaryTree<Pair>();
            temp.makeRoot(p);
            S.enqueue(temp);
        }

        //creating two smallest BinaryTree for the Queue
        BinaryTree<Pair> A = null;
        BinaryTree<Pair> B = null;
        BinaryTree<Pair> P;
        double probSum;
        int Atree, Btree;

        //iterating loop until the pair queue is empty
        while(!S.isEmpty()) {
            if (T.isEmpty()) {
                //dequeue the first 2 pairs
                A = S.dequeue();
                B = S.dequeue();

                //add the dequeued pair
                P = new BinaryTree<Pair>();
                probSum = A.getData().getProb()+B.getData().getProb();

                Pair pair = new Pair('⁂' ,probSum);
                P.makeRoot(pair);

            }else{
                //adding the pair for which the first element is smallest from both the queue
                Atree = T.peek().getData().compareTo(S.peek().getData());

                //dequeue the first element from the pair queue or the pairtree queue
                if(Atree<0){
                    A = S.dequeue();
                }else{
                    A = T.dequeue();
                }

                Btree = T.peek().getData().compareTo(S.peek().getData());
                if (Btree < 0 ) {
                    B = S.dequeue();
                }else{
                    B = T.dequeue();
                }

                P = new BinaryTree<>();
                probSum = A.getData().getProb()+B.getData().getProb();
                Pair pair = new Pair('⁂' ,probSum);
                P.makeRoot(pair);

            }

            //attach the left and right root
            P.attachLeft(A);
            P.attachRight(B);
            T.enqueue(P);
        }

        //if the size of the pairtree is still greater than 1 then proceed with this while loop
        while(T.size()>1){
            probSum = 0.0;
            P = new BinaryTree<>();

            A = T.dequeue();
            B = T.dequeue();

            Pair pair = new Pair('⁂' , probSum);
            P.makeRoot(pair);
            P.attachLeft(A);
            P.attachRight(B);
            T.enqueue(P);
        }


        //can be used to get the codes
        String[] codes = findEncoding(T.dequeue());

        //printing the output of the huffman in "Huffman" file
        PrintWriter output = new PrintWriter("Huffman.txt");
        output.println("Symbol" + "\t" + "Prob." + "\t" + "Huffman Code" + "\n");
        for(Pair p: pairs){
            output.println(p.getValue() + "\t" + p.getProb() + "\t" + codes[p.getValue()]);
        }
        output.close();

        //printing the output of the encoded version of the text in "Encoded" file
        String encodedValue = "";
        char[] stringCh = chars;
        for(char c: stringCh){
            encodedValue += codes[c];
        }
        output = new PrintWriter("Encoded.txt");
        output.println(encodedValue);
        output.close();

    }


    //method for decoding the text
    public static void decode()throws IOException{
        // initialize Scanner to capture user input
        Scanner sc = new Scanner(System.in);

        // capture file information from user and read file
        System.out.print("Enter the filename to read from/decode: ");
        String f = sc.nextLine();

        // create File object and build text String
        File file = new File(f);
        Scanner input = new Scanner(file).useDelimiter("\\Z");
        String text = input.next();
        // ensure all text is consumed, avoiding false positive end of
        // input String
        input.useDelimiter("\\z");
        text += input.next();


        // close input file
        input.close();

        // capture file information from user and read file
        System.out.print("Enter the filename of document containing Huffman codes: ");
        f = sc.nextLine();

        // create File object and build text String
        file = new File(f);
        input = new Scanner(file).useDelimiter("\\Z");
        String codes = input.next();

        // close input file
        input.close();

        //arraylist for string the character and the string
        ArrayList<Character> ch = new ArrayList<>();
        ArrayList<String> code = new ArrayList<>();

        /**
         *code provided in the PDF
         */
        Scanner ls = new Scanner(codes);

        ls.nextLine();
        ls.nextLine();

        while (ls.hasNextLine()) {
            char c = ls.next().charAt(0);
            ls.next();
            String s = ls.next();
            ch.add(c);
            code.add(s);
        }
        ////////////////////////////////////

        //Getting and writing out decoded text to the "Decoded" file.
        PrintWriter output = new PrintWriter("Decoded.txt");
        String str = "";
        for (char t : text.toCharArray()) {
            str += t;
            if (code.contains(str)) {
                output.print(ch.get(code.indexOf(str)));
                str = "";
            }
        }
        output.close();

    }

    // the findEncoding helper method returns a String Array containing
    // Huffman codes for all characters in the Huffman Tree (characters not
    // present are represented by nulls)
    // this method was provided by Srini (Dr. Srini Sampalli). Two versions are below, one for Pairtree and one for BinaryTree

    private static String[] findEncoding(BinaryTree<Pair> bt){
        String[] result = new String[256];
        findEncoding(bt, result, "");
        return result;
    }


    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix){
        // test is node/tree is a leaf
        if (bt.getLeft()==null && bt.getRight()==null){
            a[bt.getData().getValue()] = prefix;
        }
        // recursive calls
        else{
            findEncoding(bt.getLeft(), a, prefix+"0");
            findEncoding(bt.getRight(), a, prefix+"1");
        }
    }

}
