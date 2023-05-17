public class HuffDictNode
{
    //Atributos
    HuffDictNode esq;
    HuffDictNode dir;
    Character conteudo;
    int quant;
    //Construtores
    //Construtor 1,usado na arvore inicial tirada do arquivo fonte
    public HuffDictNode(char c) {
        this.esq = null;
        this.dir = null;
        this.conteudo = Character.valueOf(c);
        this.quant = 1;
    }
    //Construtor 2,usado nos nodes folha da arvore final
    public HuffDictNode(char c,int quant) {
        this.esq = null;
        this.dir = null;
        this.conteudo = Character.valueOf(c);
        this.quant = quant;
    }
    //Construtor 3,usado nos nodes não folha da arvore final
    public HuffDictNode(Character conteudo,int quant) {
        this.esq = null;
        this.dir = null;
        this.conteudo = conteudo;
        this.quant = quant;
    }
    /**<p>increQuant->Aumenta quant em 1<\p>
     * 
     */
    public void increQuant() {
        this.quant++;
    }
}
