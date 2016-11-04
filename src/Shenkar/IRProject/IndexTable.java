package Shenkar.IRProject;


import java.io.*;
import java.util.*;

public class IndexTable implements Serializable{

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

    private class Tuple {
        private int fileno;
        private int position;

        public Tuple(int fileno, int position) {
            this.fileno = fileno;
            this.position = position;
        }
    }

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

    public String search(String word) {
        stemmer=new Stemmer();
        word=word.toLowerCase();
        stemmer.add(word);
        stemmer.stem();
        List<Tuple> idx = index.get(stemmer.toString());
        String res="<html>";
        if (idx != null)
        {
            for (Tuple t : idx)
            {
                Document d=lib.docs.get(t.fileno);
                if(d.data.get(t.position-1).equals(word))
                {
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


}
