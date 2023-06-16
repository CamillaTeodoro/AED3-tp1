
public class Cipher{

	//Atributos
	String viKey;//Chave para a cifra vigenere
	//Construtor
	public Cipher(){
		viKey = "AEDSIII";
	}
	//Metodos
	/**<p>setKey-> seta a chave para a cifra de vigenere<\p>
	 * @param s chave a setar
	 */
	public void setKey(String key){
		viKey = key;
	}
	/**<p>getKey-> pega a chave para a cifra de vigenere<\p>
	 * @return chave atual de vigenere
	 */
	public String getKey(){
		return viKey;
	}
	/**<p>createCipherDB-> cria um segundo db,a partir do original,
	 * com o campo director criptografado<\p>
	 * @param db db normal
	 * @param db2 db criptografado
	 */
	public void createCipherDB(DatabaseAccess db,DatabaseAccess db2){
		try{
		db.setRafPos((long)4);
		db.resetPosition();
		db2.clearDb();
		while(db.getRAFPosition()<db.length()){
			Film nextFilm = db.next();
			String director = nextFilm.getDirector();
			String encrypt = batmanCipher(director);
			nextFilm.setDirector(encrypt);
			db2.create(nextFilm);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**<p>batmanCipher->encripta a string argumento,com base numa cifra
	 * usada em mensagens secretas do espantalho no jogo batman arkham city<\p>
	 * @param og texto original
	 * @return texto criptografado
	 */
	public String batmanCipher(String og){
		String resp = "";
		int tam = og.length();
		int tamc = viKey.length();
		for(int v = 0,s = 0;v<tam;v++,s = (s + 1)%tamc){
			int code =  og.charAt(v);//Primeiro,pegamos o código ascii do char
			//Agora,aplicar a cifra atbash(O primeiro -> ultimo,segundo -> penultimo,etc..)
			//E depois a de vinigere com chave inicializada no construtor	
				code = 255 - code;
				code = code + viKey.charAt(s);
				if(code >255){
					code = code%256;
				}
			resp+= String.valueOf((char)code);
		}
		return resp;
	}
	/**<p>batmanDecipher->inverte a cifra usada neste tp,para a string argumento<\p>
	 * @param s texto criptografado
	 * @return texto descriptografado
	 */
	public String batmanDecipher(String s){
		String resp = "";
		int tam = s.length();
		int tamc = viKey.length();
		for(int v = 0,i = 0;v<tam;v++,i = (i + 1)%tamc){
			int code = s.charAt(v);
			code = code -viKey.charAt(i);
			if(code<0){
				code = 256 +code;
			}
			code = 255 - code;
			resp+= String.valueOf((char)code);
		}
		return resp;
	}
}