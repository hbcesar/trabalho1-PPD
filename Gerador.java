import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gerador {
	public List<Byte> gerarVetor(int tamanho_vetor){
		
		Random gerador = new Random();
		// int tamanho_vetor;

		// //gerador.nextInt() + 100;
		// tamanho_vetor = 10000000;
               
		List<Byte> vetor = new ArrayList<>();
		
		for(int i=0; i < tamanho_vetor; i++){
			vetor.add((byte)gerador.nextInt());
		}
		
		return vetor;
	}
}
