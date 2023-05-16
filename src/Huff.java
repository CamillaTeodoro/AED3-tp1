import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.text.ParseException;

public class Huff
{   
    //Atributos
    HuffDict ht;
    HashMap<Character, String> hm;
    //Construtor
    public Huff() {
        this.ht = new HuffDict();
    }
    /**<p>paraArvoreFinal->transforma a arvore binaria inicial na arvore do metodo
     * huffman<\p>
     */
    public void paraArvoreFinal() {
         int dictSize = ht.dictSize;
         LinkedList<HuffDictNode> list = new LinkedList<HuffDictNode>();
        for (int i = 0; i < dictSize; ++i) {
            list.add(ht.removeMenorNode());
        }
        while (ht.raiz == null || ht.raiz.quant != ht.freqTotal) {
            HuffDictNode h1 = list.removeFirst();
            HuffDictNode h2 = list.removeFirst();
            HuffDictNode h3 = ht.adicionaNodeFinal(h1,h2);
            procuraInsereLL(h3, list);
        }
    }
    /**<p>constroiDictInicial->Constroi a arvore inicial a partir do arquivo fonte<\p>
     * @param s arquivo fonte convertido para string
     */
    public void constroiDictInicial(String s) {
        for (int length = s.length(), i = 0; i < length; ++i) {
            ht.addNode(s.charAt(i));
        }
    }
    /**<p>comprimir-> Começa o processo de compressão do arquivo fonte pelo metodo huffman<\p>
     * @param s arquivo fonte convertido em string
     * @param s2 caminho para o arquivo comprimido
     */
    public void comprimir( String s, String s2) {
        long tempoInic = System.currentTimeMillis();
        constroiDictInicial(s);
        paraArvoreFinal();
        criaHashMap(); 
        compress(s, s2);
        long tempoFin = System.currentTimeMillis();
        System.out.println("Tempo total para compactação pelo algoritmo Huffman foi de " + (tempoFin - tempoInic) + " milessegundos");
        
    }
    /**<p>procuraInsereLL->Insere um elemento novo na linked list argumento de uma forma ordenada<\p>
     * @param element elemento a inserir
     * @param list Linked list onde vamos inserir o elemento
     */
    public void procuraInsereLL(HuffDictNode element,LinkedList<HuffDictNode> list) {
        boolean found = false;
        for (int size = list.size(),  i = 0; !found && i < size; ++i) {
            if (element.quant < list.get(i).quant || (element.quant == list.get(i).quant && element.conteudo != null && list.get(i).conteudo == null)) {
                found = true;
                list.add(i, element);
            }else if(i == size-1){
                found = true;
                list.add(element);
            }
        }
    }
    /**<p>compress->Faz a compressão huffman propriamente dita,escrevendo ela em um arquivo<\p>
     * @param s arquivo fonte como string
     * @param name caminho até o arquivo de compressão
     */
    public void compress(String s, String name) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(name, "rw");
            escreveHuffTree(randomAccessFile, ht.raiz);
            randomAccessFile.writeInt(-1);
            String value = "";
            int length = s.length();
            for (int k = 0; k < length; ++k) {
                Character c = Character.valueOf(s.charAt(k));
                String set2 = hm.get(c);
                value+=set2;
                while (value.length()>=8) {
                    byte resp = converteBitByte(value);
                    System.out.println(resp);
                    randomAccessFile.writeByte(resp);
                    if(value.length()>8){
                    value = value.substring(8);
                    }else{
                        value = "";
                    }
                }
            }
            randomAccessFile.close();
        }
        catch (Exception ex) {
            System.out.println("Erro ao comprimir!");
            ex.printStackTrace();
        }
    }
    /**<p>criaHashMap->cria o hashmap com base na arvore de huffman<\p>
     */
    public void criaHashMap() {
        String set = "0";
        String set2 = "1";
        hm = new HashMap<Character, String>();
        procuraCodigo(ht.raiz.esq, set);
        procuraCodigo(ht.raiz.dir, set2);
    }
    /**<p>procuraCodigo->desce a arvore huffman final,descobrindo os códigos para escrever no hashmap
     * e os guarda<\p>
     * @param huffDictNode node ataul
     * @param value valor atual do valor do hashmap
     */
    public void procuraCodigo(HuffDictNode huffDictNode,String value) {
        if (huffDictNode.dir == null && huffDictNode.esq == null) {
            hm.put(huffDictNode.conteudo,value);
        }
        else if (huffDictNode.dir == null) {
            String value2 = value+ "0";
            procuraCodigo(huffDictNode.esq, value2);
        }
        else if(huffDictNode.esq == null){
            String value2 = value+ "1";
            procuraCodigo(huffDictNode.dir, value2);
        }else{
            String value2 = value+ "0";
            procuraCodigo(huffDictNode.esq, value2);
            value2 = value+ "1";
            procuraCodigo(huffDictNode.dir, value2);
        }
    }
    /**<p>escreveHuffTree->escreve a arvore huffman no começo do arquivo comprimido<\p>
     * @param randomAccessFile arquivo comprimido
     * @param huffDictNode node que vamos escrever
     */
    public void escreveHuffTree(RandomAccessFile randomAccessFile,HuffDictNode huffDictNode) throws IOException {
        if (huffDictNode == null) {
            randomAccessFile.writeInt(0);
        }
        else {
            randomAccessFile.writeInt(huffDictNode.quant);
            if(huffDictNode.conteudo != null){
            randomAccessFile.writeChar(huffDictNode.conteudo);
            }else{
            randomAccessFile.writeChar(0);    
            }
            escreveHuffTree(randomAccessFile,huffDictNode.esq);
            escreveHuffTree(randomAccessFile,huffDictNode.dir);
        }
    }
    public void mostraArvore(HuffDictNode hdc){
        if(hdc != null){
            mostraArvore(hdc.esq);
            if(hdc.conteudo == null){
                System.out.println("00");
            }else{
                System.out.println(hdc.conteudo.charValue());
            }
            mostraArvore(hdc.dir);
        }
    }
    public byte converteBitByte(String bits){
        int resp = 0;
        for(int v = 0;v<8;v++){
            if(bits.charAt(v)=='1'){
                int s = 0;
                int pow = 1;
                while(s<v){
                    pow*=2;
                    s++;
                }
                resp+= pow;
            }
        }
        return (byte) resp;
    }
    /**<p>descomprimir->Começa o processo de descompressão huffman<\p>
     * @param s caminho ate o arquivo de onde vamos descomprimir
     * @param s2 caminho ate o arquivo descomprimido
     */
    public void descomprimir(String s,String s2){
        try{
        long tempoInic = System.currentTimeMillis();
        RandomAccessFile raf = new RandomAccessFile(s,"r");
        ht.raiz = constroiArvoreArquivo(raf,ht.raiz);
        criaHashMap();
        decompress(raf,s2);
        long tempoFin = System.currentTimeMillis();
        System.out.println("Tempo total para descompactação pelo algoritmo Huffman foi de " + (tempoFin - tempoInic) + " milessegundos");
        raf.close();
        }catch(Exception e){
            System.out.println("Erro ao descomprimir!");
            e.printStackTrace();
        }
    }
    /**<p>constroiArvoreArquivo->constroi a arvore huffman a partir do arquivo
     * já comprimido<\p>
     * @param raf arquivo já comprimido
     * @param atual node atual
     * @return no final,a arvore reconstruida
     */
    public HuffDictNode constroiArvoreArquivo(RandomAccessFile raf,HuffDictNode atual) throws IOException{
        int start = raf.readInt();
        atual = null;
        if(start > 0){
            char c = raf.readChar();
            if(c == 0){
                Character c2 = null;
                atual  = new HuffDictNode(c2,start);
            }else{
                atual = new HuffDictNode(c,start);
            }
            atual.esq = constroiArvoreArquivo(raf,atual.esq);
            atual.dir = constroiArvoreArquivo(raf,atual.dir);
        }
        return atual;
    }
    /**<p>decompress->Faz a descompressão propriamente dita do arquivo argumento,para o bd equivalente<\p>
     * @param raf arquivo comprimido
     * @param s caminho para onde o bd ficara
     */
    public void decompress(RandomAccessFile raf,String s) throws IOException,ParseException{
        HashMap<String,Character> hm2 = inverteHashMap();
        long tam = raf.length();
        String dbAsString = "";
        String temp = "";
        while(raf.getFilePointer()<tam){
            int input = raf.read();
            String temp2 = byteToBits(input);
            temp+=temp2;
            int count2 = 1;
            while(count2<=temp.length()){
                String temp3 = temp.substring(0,count2);
                Character resp = hm2.get(temp3);
                if(resp != null){
                    dbAsString+= Character.toString(resp.charValue());
                    if(count2< temp.length()){
                        temp = temp.substring(count2);
                    }else{
                        temp = "";
                    }
                    
                    count2 = 0;
                }
                count2++;
            }
        }
        DatabaseAccess dbNew = new DatabaseAccess(s);
        dbNew.dbFromString(dbAsString);
    }
    /**<p>byteToBits->adiciona os bits equivalentes do byte argumento a String
     * retornada<\p>
     * @param data byte de onde vamos achar os bits
     */
    public String byteToBits(int data){
        String resp = "";
        for(int v = 7;v>=0;v--){
            int s = 0;
            int pow =1;
            while(s<v){
                pow*=2;
                s++;
            }
            if(data - pow>=0){
                data = data - pow;
                resp= "1"+resp;
            }else{
                resp = "0"+resp;
            }
        }
        return resp;
    }
    /**<p>inverteHashMap->inverte o hashmap atributo desta classe,para realizar a
     * descompressão eficientemente<\p>
     * @return hashMap invertido
     */
    public HashMap<String,Character> inverteHashMap(){
        HashMap<String,Character> resp = new HashMap<String,Character>();
        Set<Map.Entry<Character,String>> s = hm.entrySet();
        for(Map.Entry<Character,String> entry : s){
            resp.put(entry.getValue(),entry.getKey());
            }
        return resp;
    }
}


