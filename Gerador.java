import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gerador {
	public List<Byte> gerarVetor(int tamanho_vetor){
		
		Random gerador = new Random();
               
		List<Byte> vetor = new ArrayList<>();
		
		for(int i=0; i < tamanho_vetor; i++){
			vetor.add((byte)gerador.nextInt());
		}
		
		return vetor;
	}
}
