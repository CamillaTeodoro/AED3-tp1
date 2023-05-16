public class HuffDict
{
    //Atributos
    HuffDictNode raiz;
    int dictSize;
    int freqTotal;
    //Construtor
    public HuffDict() {
        this.raiz = null;
        this.dictSize = 0;
        this.freqTotal = 0;
    }
    //Metodos
    /**<p>addNode->Começa o processo de adicionar nodes na arvore inicial<\p>
     * @param c char achado no arquivo fonte
     */
    public void addNode(char c) {
        raiz = adicionaNode(raiz, c);
    }
    /**<p>adicionaNode->Metodo que adiciona o node ou atualiza um na arvore inicial<\p>
     * @param huffDictNode node atual na arvore
     * @param c char encontrado no arquivo fonte
     * @return construção da arvore
     */
    public HuffDictNode adicionaNode(HuffDictNode huffDictNode, char c) {
        if (huffDictNode == null) {
            huffDictNode = new HuffDictNode(c);
            ++this.dictSize;
            ++this.freqTotal;
            return huffDictNode;
        }
        else if (c == huffDictNode.conteudo.charValue()) {
            huffDictNode.increQuant();
            ++this.freqTotal;
        }
        else if (c < huffDictNode.conteudo.charValue()) {
            huffDictNode.esq = this.adicionaNode(huffDictNode.esq, c);

        }
        else {
           huffDictNode.dir = this.adicionaNode(huffDictNode.dir, c);
        }
        return huffDictNode;
    }
    /**<p>removeMenorNode->acha,remove o menor node da arvore inicial e retorna uma cópia dele\p>
     * 
     */
    public HuffDictNode removeMenorNode() {
        HuffDictNode huffDictNode = null;
        if (raiz != null) {
            HuffDictNode percorreArvore = percorreArvore(raiz,raiz);
            huffDictNode = new HuffDictNode(percorreArvore.conteudo.charValue(), percorreArvore.quant);
            deletaNode(percorreArvore.conteudo.charValue());
        }
        return huffDictNode;
    }
    /**<p>percorreArvore->percorre a arvore inicial,procurando o node com menor quant<\p>
     * @param huffDictNode node atual
     * @param huffDictNode2 menor node achado ate entao
     * @return no final,menor node na arvore
     */
    public HuffDictNode percorreArvore(HuffDictNode huffDictNode, HuffDictNode huffDictNode2) {
        if (huffDictNode == null) {
            return huffDictNode2;
        }
        if (huffDictNode.quant < huffDictNode2.quant) {
            huffDictNode2 = huffDictNode;
        }
        huffDictNode2 = percorreArvore(huffDictNode.esq, huffDictNode2);
        huffDictNode2 = percorreArvore(huffDictNode.dir, huffDictNode2);
        return huffDictNode2;
    }
    /**<p>deletaNode->começa o processo de deletar um node da arvore inicial<\p>
     * @param c char do node a deletar
     */
    public void deletaNode( char c) {
        raiz = deletaNodeRec(c, raiz);
    }
    /**<p>deletaNodeRec->função recursiva para deletar um node da arvore inicial<\p>
     * @param c char do node a deletar
     * @param huffDictNode node atual da arvore
     * @return no final,uma arvore com o node escolhido deletado
     */
    public HuffDictNode deletaNodeRec(char c,HuffDictNode huffDictNode) {
        if (huffDictNode == null) {
            return huffDictNode;
        }
        if (c < huffDictNode.conteudo.charValue()) {
            huffDictNode.esq = deletaNodeRec(c, huffDictNode.esq);
        }
        else if (c > huffDictNode.conteudo.charValue()) {
            huffDictNode.dir = deletaNodeRec(c, huffDictNode.dir);
        }
        else {
            if (huffDictNode.esq == null) {
                return huffDictNode.dir;
            }
            if (huffDictNode.dir == null) {
                return huffDictNode.esq;
            }
            HuffDictNode minArvoreDireita = minArvoreDireita(huffDictNode.dir);
            huffDictNode.conteudo = minArvoreDireita.conteudo;
            huffDictNode.quant = minArvoreDireita.quant;
            huffDictNode.dir = deletaNodeRec(huffDictNode.conteudo.charValue(), huffDictNode.dir);
        }
        return huffDictNode;
    }
    /**<p>minArvoreDireita->Acha o menor node,na subarvore iniciada pelo node argumento,
     * e o retorna<\p>
     * @param esq node inicial
     * @return menor node encontrado
     */
    public HuffDictNode minArvoreDireita(HuffDictNode esq) {
        int n = esq.quant;
        char c = esq.conteudo;
        while (esq.esq != null) {
            n = esq.esq.quant;
            c = esq.esq.conteudo.charValue();
            esq = esq.esq;
        }
        return new HuffDictNode(c, n);
    }
    /**<p>adicionaNodeFinal->adiciona node a arvore que segue o metodo huffman<\p>
     * @param esq node a esquerda do node que vamos criar
     * @param dir node a direita do node que vamos criar
     * @return node criado
     */
    public HuffDictNode adicionaNodeFinal(HuffDictNode esq,HuffDictNode dir) {
        int n = esq.quant + dir.quant;
        HuffDictNode rai = new HuffDictNode(null, n);
        if (raiz == null || n > raiz.quant) {
            raiz = rai;
        }
        rai.esq = esq;
        rai.dir = dir;
        return rai;
    }
}
