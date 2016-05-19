import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gerador {
	public List<Byte> gerarVetor(){
		Random gerador = new Random();
		int tamanho_vetor;
		
		tamanho_vetor = 500;//gerador.nextInt() + 100;
               
		List<Byte> vetor = new ArrayList<>();
		
		for(int i=0; i < tamanho_vetor; i++){
			vetor.add((byte)gerador.nextInt());
		}
		
		return vetor;
	}
}
