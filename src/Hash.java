import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Scanner;

class Hash{
    //Atributos

    private short profGlobal;
    private RandomAccessFile arqDiretorio;
    private RandomAccessFile arqIndices;

    //Getters
    
    public short getProfGlobal(){
        return profGlobal;
    }

    //Setters

    public void setProfGlobal(short novaProf){
        this.profGlobal = novaProf;
    }

    //Construtores

    public Hash(){
        criarArqDir();
        criarArqInd();
        try{
        arqDiretorio.seek(0);
        setProfGlobal(arqDiretorio.readShort());
        }catch(Exception e){
        setProfGlobal((short)0);
        }
    }
    
    //Metodos
    /** <p> reset-> reseta os arquivos de indice,de diretorio e a profundidade global </p>
     */
    public void reset()throws IOException{
        arqDiretorio.setLength(0);
        arqIndices.setLength(0);
        setProfGlobal((short)0);
        criarArqDir();
        criarArqInd();
    }

    /** <p>criarArqDir -> Cria o arquivo de diretório Hash,e inicializa o cabeçalho e o apontamento
    * para o primeiro bucket</p>
    */
    private void criarArqDir(){
        try{
        this.arqDiretorio = new RandomAccessFile("../db/HashDir.db","rw");
        if(arqDiretorio.length()==0){
        arqDiretorio.writeShort(getProfGlobal());
        arqDiretorio.writeLong((long)0);
        }
        }catch(Exception e){
            System.out.println("Ocorreu um erro na criação do arquivo de Diretório!");
            e.printStackTrace();
        }
    }

    /** <p>criarArqInd -> Cria o arquivo de Indices(Buckets) do Hash,e escreve algumas informações
    * no primeiro bucket</p>
    */
   private void criarArqInd(){
    try{
        this.arqIndices = new RandomAccessFile("../db/HashInd.db","rw");
        if(arqIndices.length()==0){
        arqIndices.writeShort(0);
        arqIndices.writeByte(0);
        }
    }catch(Exception e){
        System.out.println("Ocorreu um erro na criação do arquivo de Indices!");
        e.printStackTrace();
    }
   }

    /** <p>fechar -> fecha a stream dos arquivos usados por esta classe</p>
    */
   public void fechar() throws IOException{
    this.arqDiretorio.close();
    this.arqIndices.close();
   }
   /** <p>calcPow2 -> calcula a potencia de dois com o expoente argumento dado</p>
    * @param exp expoente da potencia
    * @return resultado do calculo
    */
   public int calcPow2(short exp){
    int resp = 1;
    for(short v = 0;v<exp;v++){
            resp*=2;
        }
    return resp; 
   }

    /**  <p>hashFunc -> calcula a função de hash = id mod 2^profGlobal para o id indicado</p>
    * @param idCalc id de um registro para calcular o hash
    * @return  resultado da função
    */
   public int hashFunc(int idCalc){
        short prof = getProfGlobal();
        int pow = calcPow2(prof);
    return idCalc%pow;
   }

   /** <p>readBucketPos -> Le a posição de um bucket,baseado no arquivo de diretórios</p>
   * @param res resultado de uma aplicação da função de hash
   * @return  posição de inicio do bucket desejado
   */
  public long readBucketPos(int res) throws IOException{
    arqDiretorio.seek(2+ 8*(res));
    return arqDiretorio.readLong();
  }
    /* aumentarProfGlobal -> aumenta a profundidade local,alocando ponteiros novos no arquivo de
    diretorios
    */
   public void aumentarProfGlobal() throws IOException{
    setProfGlobal((short)(getProfGlobal()+1));
    arqDiretorio.seek(0);
    short prof = getProfGlobal();
    arqDiretorio.writeShort(prof);
    int end = calcPow2(prof);
    int v = calcPow2((short)(prof-1));
    arqDiretorio.seek(2);
    long arrEndereco[] = new long[v];
    for(int s = 0;s<v;s++){
        arrEndereco[s] = arqDiretorio.readLong();
    }
    for(int s = 0;v<end;v++,s++){
        arqDiretorio.writeLong(arrEndereco[s]);
    }
   }

    /** <p>achaBucketEscreveAddr -> acha para qual bucket a proxima entrada do arquivo de
     * diretorios deve apontar,e escreve o endereço dele</p>
     * @param h2 hash que indica qual diretorio devemos mudar o apontamento
    */
    public void achaBucketEscreveAddr(int h2) throws IOException{
        arqDiretorio.seek(2 + h2*8);
        long addr = achaAddrBucketVazio();
        if(addr == -1){
            System.out.println("Erro ao escrever endereço de bucket no arquivo de diretorio!Cod:1");
        }else{
       arqDiretorio.writeLong(addr);
        }
    }

    /** <p> achaAddrBucketVazio ->Acha o endereço de byte do primeiro bucket com 0 elementos</p>
     * no arquivo de indices</p>
     */
    public long achaAddrBucketVazio() throws IOException{
        long resp = -1;
        long tam = arqIndices.length();
        if(tam == calcPow2((short)(getProfGlobal()-1))*75){
            tam--;
        }
        resp = ((tam/75)+1)*75;
        return resp;
    }

   /**  <p>inserir -> Insere um registro novo no bucket adequado
    * @param idShow id do show,a ser usada na função de hashing para achar o bucket
    * @param endereco endereço do filme no arquivo .db que os contem
    */
    public void inserir(int idShow,long endereco) throws IOException{
        int bucket = hashFunc(idShow);
        long pos = readBucketPos(bucket);
        arqIndices.seek(pos);
        short profLocal = arqIndices.readShort();
        byte numbRes = arqIndices.readByte();
        if(6>numbRes){
            arqIndices.seek(arqIndices.getFilePointer()+ numbRes*12);
            arqIndices.writeInt(idShow);
            arqIndices.writeLong(endereco);
            arqIndices.seek(pos+2);
            arqIndices.writeByte(numbRes+1);
        }else{
            if(profLocal == getProfGlobal()){
                aumentarProfGlobal();
            }
            profLocal++;
            arqIndices.seek(pos+3);
            byte numb1 = 0;
            byte numb2 = 0;
            int idFilm1[] = new int[6];
            long enderecoFilm1[] = new long[6];
            int idFilm2[] = new int[6];
            long enderecoFilm2[] = new long[6];
            for(int v = 0;v<numbRes;v++){
                int idFilm = arqIndices.readInt();
                long posFilm = arqIndices.readLong();
                if(hashFunc(idFilm) == bucket){
                    idFilm1[numb1] = idFilm;
                    enderecoFilm1[numb1] = posFilm;
                    numb1++;
                }else{
                    idFilm2[numb2] = idFilm;
                    enderecoFilm2[numb2] = posFilm;
                    numb2++;
                }
            }
           
            if(hashFunc(idFilm1[0])<hashFunc(idFilm2[0])){
            arqIndices.seek(pos);
            arqIndices.writeShort(profLocal);
            arqIndices.writeByte(numb1);
            for(int v = 0;v<numb1;v++){
                arqIndices.writeInt(idFilm1[v]);
                arqIndices.writeLong(enderecoFilm1[v]);
            }
            int hash2 = hashFunc(idFilm2[0]);
            achaBucketEscreveAddr(hash2);
            arqIndices.seek(readBucketPos(hash2));
            arqIndices.writeShort(profLocal);
            arqIndices.writeByte(numb2);
            for(int v = 0;v<numb2;v++){
                arqIndices.writeInt(idFilm2[v]);
                arqIndices.writeLong(enderecoFilm2[v]);
            }
            }else{
            arqIndices.seek(pos);
            arqIndices.writeShort(profLocal);
            arqIndices.writeByte(numb2);
            for(int v = 0;v<numb2;v++){
                arqIndices.writeInt(idFilm2[v]);
                arqIndices.writeLong(enderecoFilm2[v]);
            }
            int hash1 = hashFunc(idFilm1[0]);
            achaBucketEscreveAddr(hash1);
            arqIndices.seek(readBucketPos(hash1));
            arqIndices.writeShort(profLocal);
            arqIndices.writeByte(numb1);
            for(int v = 0;v<numb1;v++){
                arqIndices.writeInt(idFilm1[v]);
                arqIndices.writeLong(enderecoFilm1[v]);
            }
            }
            
        }
    }
    /** <p>ler -> Devolve o endereço,no arquivo de banco de dados,
     * do filme cujo id é o argumento dado</p>
     * @param id id do filme a procurar
     * @return posição do filme no banco de dados
     */
    public long ler(int id)throws IOException{
        long resp = -1;
        int buck = hashFunc(id);
        long bucketPos = readBucketPos(buck);
        arqIndices.seek(bucketPos+2);
        byte numbReg = arqIndices.readByte();
        boolean achou = false;
        for(byte v = 0;!achou && v<numbReg;v++){
            int idFilm = arqIndices.readInt();
            long addrFilm = arqIndices.readLong();
            if(idFilm == id ){
                resp = addrFilm;
                achou = true;
            }
        }
        return resp;
    }
    /** <p>mostrarInd -> mostra os filmes que estão no arquivo de indices,bucket a bucket,sequencialmente.
     * Serve mais para debugging</p>
     */
    public void mostrarInd(Scanner sca)throws IOException{
        arqIndices.seek(0);
        long tam = arqIndices.length();
        int buck = 0;
        while(arqIndices.getFilePointer()<tam){
            long atual = arqIndices.getFilePointer();
            arqIndices.readShort();
            byte numbRegs = arqIndices.readByte();
            System.out.println("Numero de registros no bucket "+ buck + " :"+ numbRegs);
            for(byte v = 0;v < numbRegs;v++){
                int id = arqIndices.readInt();
                long addr = arqIndices.readLong();
                System.out.println("id: "+ id+" addr: "+ addr);
                System.out.println("Aperte Enter:");
                sca.nextLine();
            }
            buck++;
            arqIndices.seek(atual + 75);
        }
    }
    /**<p>mostrarDir -> mostra os filmes que estão no arquivo de diretorios,bucket a bucket,sequencialmente.
     * Serve mais para debugging</p>
     */
    public void mostrarDir(Scanner sca) throws IOException{
        arqDiretorio.seek(0);
        System.out.println(arqDiretorio.readShort());
        long tam = arqDiretorio.length();
        int buck = 0;
        while(arqDiretorio.getFilePointer()<tam){
            System.out.println("Endereco do bucket "+ buck +" :"+ arqDiretorio.readLong());
            buck++;
            System.out.println("Aperte Enter:");
            sca.nextLine();
        }
    }
    /**<p>deletar -> apaga um registro que exista do arquivo de indices</p>
     * @param id id a apagar
     * @return se apagamos ou não o registro
     */
    public boolean deletar(int id) throws IOException{
        boolean resp = false;
        int bucket = hashFunc(id);
        long bucketPos = readBucketPos(bucket);
        arqIndices.seek(bucketPos+2);
        byte numbReg = arqIndices.readByte();
        int []arrI = new int[numbReg];
        long []arrL = new long[numbReg];
        for(byte v = 0;v<numbReg;v++){
            arrI[v] = arqIndices.readInt();
            arrL[v] = arqIndices.readLong();
            if(arrI[v] == id){
                resp = true;
                arrI[v] = -1;
            }
        }
        if(resp){
            arqIndices.seek(bucketPos +2);
            arqIndices.writeByte((byte)(numbReg-1));
            for(byte v = 0;v<numbReg;v++){
                if(arrI[v]!=-1){
                    arqIndices.writeInt(arrI[v]);
                    arqIndices.writeLong(arrL[v]);
                }
            }
        }
        return resp;
    }
    /**<p> atualizar -> atualiza um registro no arquivo de indices do hash </p>
     * @param id id do filme a atualizar o endereço
     * @param addr endereço novo,para sobrescrever o antigo
     * @return se a atualização foi bem sucedida ou não
     */
    public boolean atualizar(int id,long addr) throws IOException{
        boolean resp = false;
        int bucket = hashFunc(id);
        long bucketPos = readBucketPos(bucket);
        arqIndices.seek(bucketPos+2);
        byte numbReg = arqIndices.readByte();
        for(byte v = 0;!resp && v<numbReg;v++){
            if(arqIndices.readInt()==id){
                resp = true;
                arqIndices.writeLong(addr);
            }else{
                arqIndices.readLong();
            }
        }
        return resp;
    }
}