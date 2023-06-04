
public class RB{
	//Atributos
	int padraoHash;//Valor hash do padrao escolhido
	String padrao;//O padrao procurado
    //Metodos
    /**<p>changePadrao-> muda os atributos desta classe relacionados ao padrao
     * a ser pesquisado<\p>
     * @param String novo padrao a ser pesquisado no texto
     */
    public void changePadrao(String pad){
        if(pad.length()>9){
            System.out.println("Padrao maior que o permitido.Máximo 9 caracteres.");
            pad = pad.substring(0,9);
            System.out.println("Reduzindo padrao a "+ pad);
        }
        padrao = pad;
        padraoHash = hashing1(pad);
    }
    /**<p>hashing1->Metodo chamado para calcular o valor de hash do padrao
     *de iniciarmos as buscas<\p>
     * @param String padrao que queremos achar no texto
     * @return valor hash do padrão
     */
    public int hashing1(String padrao){
        int resp = 0;
        int tam = padrao.length();
        int pow = 1;
        for(int v = 1;v<tam;v++){
            pow*=10;
        }
        for(int v = 0;v<tam;v++,pow/=10){
            resp += (padrao.charAt(v)%10)*pow;
        }
        return resp;
    }
    /**
     * <p>procurar->Procura no db fornecido como string argumento,o padrao atributo desta classe,
     * e no final escreve uma frase com o total de vezes o padrao foi encontrado,e em quais registros ele foi encontrado<\p>
     * @param String db como uma string
     */
    public void procurar(String db){
        long tempoInicial = System.currentTimeMillis();
        int quant = 0;
        int hashMatch = 0;
        int tamP = padrao.length();
        String p1 = db.substring(0,tamP);
        int hashInic = hashing1(p1);
        int tam = db.length() - tamP+1;
        if(hashInic == padraoHash){
            hashMatch++;
            if(checkP(p1)){
            quant++;
            }
        }
        char lastChar = db.charAt(0);
        for(int v = 1;v<tam;v++){
            hashInic = rollHash(hashInic,lastChar,db.charAt(v+tamP-1));
             if(hashInic == padraoHash){
                hashMatch++;
                  p1 = db.substring(v,v+ tamP);
                 if(checkP(p1)){
                 quant++;
                  }
             }
            lastChar = db.charAt(v);
        }
    long tempoFinal = System.currentTimeMillis();
    long total = tempoFinal - tempoInicial;
    System.out.println("Tempo total para busca pelo algoritmo Rabin-Karp foi de " + total + " milessegundos");
    if(quant == 0){
        System.out.println("Padrão não encontrado no texto.");
    }else{
         System.out.println("Padrão encontrado " + quant + " vezes,hash deu match "+ hashMatch + " vezes.");
    }
    }
    /** <p>rollHash->faz o hash a partir da proxima posição do texto rapidamente,
     * usando a tecnica sugerida no metodo rabin-karp<\p>
     * @param int valor hash da janela anterior no texto
     * @param char char a sair do hash
     * @param char char a entrar no hash
     * @return novo valor do hash na nova janela
     */
    public int rollHash(int hashAnt,char out,char in){
        int pow = 1;
        int tam = padrao.length();
        for(int v = 1;v<tam;v++){
            pow*=10;
        }
        return ((hashAnt - (out%10)*pow)*10 + in%10);
    }
    /**<p>checkP->checa se a string argumento é igual ao padrao<\p>
     * @param String texto a comparar ao padrao
     * @return true se eh igual,false caso contrario
     */
    public boolean checkP(String text){
        boolean resp = true;
        int tam = text.length();
        for(int v = 0;resp && v<tam;v++){
            if(text.charAt(v)!= padrao.charAt(v)){
                resp = false;
            }
        }
        return resp;
    }
}