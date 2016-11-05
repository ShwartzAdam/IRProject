package Shenkar.IRProject;


import java.io.*;
import java.lang.reflect.Array;
import java.util.*;


public class IndexTable implements Serializable{

    private class Tuple {
        private int fileno;
        private int position;

        public Tuple(int fileno, int position) {
            this.fileno = fileno;
            this.position = position;
        }
        private int getFileNum(){return fileno;}
        private int getPos(){return position;}
    }


    Map<String, List<Tuple>> index = new HashMap<String, List<Tuple>>();
    List<String> files = new ArrayList<String>(); // the original list files

    Stemmer stemmer;
    Library lib= new Library();
    List<String> stopwords = Arrays.asList("a", "able", "about",
            "across", "after", "all", "almost", "also", "am", "among", "an",
            "and", "any", "are", "as", "at", "be", "because", "been", "but",
            "by", "can", "cannot", "could", "dear", "did", "do", "does",
            "either", "else", "ever", "every", "for", "from", "get", "got",
            "had", "has", "have", "he", "her", "hers", "him", "his", "how",
            "however", "i", "if", "in", "into", "is", "it", "its", "just",
            "least", "let", "like", "likely", "may", "me", "might", "most",
            "must", "my", "neither", "no", "nor", "not", "of", "off", "often",
            "on", "only", "or", "other", "our", "own", "rather", "said", "say",
            "says", "she", "should", "since", "so", "some", "than", "that",
            "the", "their", "them", "then", "there", "these", "they", "this",
            "tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
            "what", "when", "where", "which", "while", "who", "whom", "why",
            "will", "with", "would", "yet", "you", "your");



    public void indexFile(File file) throws IOException {
        stemmer=new Stemmer();
        Vector<String> words= new Vector<>();
        int fileno = files.indexOf(file.getPath());
        Document doc= new Document(file,fileno);
        if (fileno == -1) {
            files.add(file.getPath());
            fileno = files.size() - 1;
        }
        int pos = 0;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null; line = reader
                .readLine())
        {
            for (String _word : line.split("\\W+"))
            {
                if(_word.equals("")) continue;
                words.add(_word);
                _word=_word.toLowerCase();
                stemmer.add(_word);
                pos++;
                if (stopwords.contains(_word))
                    continue;
                stemmer.stem();
                _word=stemmer.toString();
                List<Tuple> idx = index.get(_word);
                if (idx == null) {
                    idx = new LinkedList<Tuple>();
                    index.put(_word, idx);
                }
                idx.add(new Tuple(fileno, pos));
            }
        }
        doc.data=words;
        lib.docs.add(doc);
        System.out.println("indexed " + file.getPath() + " " + pos + " words");
    }

    public void indexLib(File dir) throws IOException {
        File files[];
        files=dir.listFiles();
        for(int i=0;i< files.length;i++)
        {
            indexFile(files[i]);
        }

    }

    public String searchSingle(String word) {
        // stem the word to its stemming form.
        stemmer=new Stemmer();
        // convert to lowercase the word
        word=word.toLowerCase();
        // prepare the stem class for a word and change it.
        stemmer.add(word);
        stemmer.stem();
        //
        List<Integer> results = new ArrayList<>();
        List<Tuple> idx = index.get(stemmer.toString());
        String res="<html>";
        if (idx != null)
        {
            for (Tuple t : idx)
            {
                Document d=lib.docs.get(t.fileno);
                if(d.data.get(t.position-1).equals(word))
                {
                    //results.add(d.getId(),t.position-1);
                    System.out.println(d.data.get(t.position-1).toString());

                    res+=(d.printContext(t.position-1))+"<br><br>";
                }

            }
            res+="</html>";
        }
        else
        {
            res="No resutls";
        }
        return res;
    }

    public ArrayList search(String word) {
        // stem the word to its stemming form.
        stemmer=new Stemmer();
        // convert to lowercase the word
        word=word.toLowerCase();
        // prepare the stem class for a word and change it.
        stemmer.add(word);
        stemmer.stem();
        //return where is word
        List<Tuple> idx = index.get(stemmer.toString());
        ArrayList res = new ArrayList(lib.docs.size());
        int len = 0;
        while (len != lib.docs.size())
        {
            res.add(len,0);
            len++;
        }
        // adding all apperance of the word to Arraylist of interger

        if (idx != null) {
            for (Tuple t : idx) {
                //Document d = lib.docs.get(t.fileno);
               // if (d.data.get(t.position - 1).equals(word)) {
                        res.set(t.getFileNum(),1);
               // }
            }
        }

        return res;

    }

    public String analyseQuery(List<String> parsed, String query)
    {
        Map<Integer,ArrayList> result = new HashMap<Integer,ArrayList>();
        String[] splitted = query.split(" ");
        int loc = 0;
        for(String token : splitted)
        {
            result.put(loc,new ArrayList());
            loc ++;
        }

        // the results will held in VECTOR of WORD -> 0 - 1 (true)
        //                                            1 - 1 (true)
        //                                            2 - 0 (true)

        // take the first three if the third is NOT take also the forth.
        
        // NOT then AND and then OR

        int sizeofDocs = lib.docs.size();
        int indexOfOper = 0;
        int indexOfWord = 0;
        for(String str:parsed)
        {
            // if one of the parsed list has '3' meaning it NOT sign and we need to get result on it
            // first and then address all the others
            if(str.equals("3"))
            {
                ArrayList docApperance = new ArrayList();
                String[] tmp = query.split(" ");
                docApperance = search(tmp[indexOfWord + 1]);
                docApperance = notOper(docApperance); // NOT Oper
                result.put(indexOfWord + 1,docApperance);
                parsed.remove(indexOfOper);
                result.remove(indexOfOper);
                query.replace("NOT","");
                // remove the NOT from query
                // and from the parsed

                // take the word right to it , meanning we need the index of the NOT and index of 
                // the word and add it the result Vector , the result Vector will manage the words 
                // apper in text by DOCS. 
            }
            indexOfOper++;
            indexOfWord++;
        }
        indexOfOper = 0;
        indexOfWord = 0;
        for(String str : parsed)
        {
            if(str.equals("4"))
            {
                ArrayList docApperance = new ArrayList();
                String[] tmp = query.split(" ");
                docApperance = search(tmp[indexOfWord + 1]);
                docApperance = notOper(docApperance);

                // qoution of the word -- need to take the Index of 4 , and the Index of the word
                // and add it to the Vector of result 
                // all of the is dont before we bulid the HTML structure
            }
            indexOfOper++;
            indexOfWord++;
        }
        ArrayList left = new ArrayList();
        ArrayList right = new ArrayList();

        left.add(parsed.get(0));
        right.add(parsed.get(2));

        int size = parsed.size();
        /*
        while ( size != 0)
        {


        }
        */
        //System.out.println(parsed.get(1));
        //System.out.println(parsed.get(2));
        //System.out.println(parsed.get(3));

        left = orOper(left,right);

        /*
        for (int i=0 ; i< parsed.size() ; i++)
        {
            // if there is NOT Oper we will use it first on the word it is left to .. cat OR NOT dog
            // NOT dog will be first to short it to group of three




        }
        */

        // the String that will return will hold the HTML structure includeing the required
        // search terms


        return null;
    }
    public ArrayList orOper(ArrayList left , ArrayList right)
    {
        // add right to left and return the left with no duplicates
        left.removeAll(right);
        left.addAll(right);
        Collections.sort(left);
        return left;
    }
    public ArrayList andOper(ArrayList left , ArrayList right)
    {
        int loc = 0;
        while (left.size() > loc) {
            Integer leftVal = (Integer)left.get(loc);
            Integer rightVal = (Integer)right.get(loc);
            if( left.equals(0) || right.equals(0)  )
            {
                left.set(loc,0);
            }
            else {
                left.set(loc,1);
            }
            loc++;
        }
        return left;
    }
    public ArrayList notOper(ArrayList notIt)
    {
        int loc = 0;
        while (notIt.size() > loc) {
           Integer tmp = (Integer)notIt.get(loc);
            if( tmp.equals(0) )
            {
                notIt.set(loc,1);
            }
            else{
                notIt.set(loc,0);
            }
            loc++;
        }
        return notIt;
    }

}
