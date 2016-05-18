import java.util.Random;

public class Gerador {
	public byte[] gerarVetor(){
		Random gerador = new Random();
		int tamanho_vetor;
		
		tamanho_vetor = gerador.nextInt() + 100;
		
		byte[] vetor = new byte[tamanho_vetor];
		
		for(int i=0; i < tamanho_vetor; i++){
			vetor[i] = (byte)gerador.nextInt();
		}
		
		return vetor;
	}
}
