import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ImplementacaoCliente {	 
	
	public static Byte somar(List<Byte> vetor) {
		byte sum = 0;

		for(Byte number : vetor)
			sum = (byte) (sum ^ number); 
		
		return sum;
	}

	public static void main(String[] args) {
		//nome que sera associado ao mestre
		String host = (args.length < 1) ? null : args[0];

		Gerador g = new Gerador();
		
		List<Byte> vetorInicial = new ArrayList<>();


		//variaveis que armazenam informacoes para comparação
		long tempoInicialEstatico = 0;
		byte resultado_estatico = 0;
		long tempoFinalEstatico = 0;
		//inicio do tempo de execucao do mestre
		long tempoInicial = 0;
		//CLiente passa para o mestre o vetor de bytes
		byte resultado = 0;
		//fim do tempo de execucao do mestre
		long tempoFinal = 0;

		double tempoExecucaoEstatico = 0;
				double tempoExecucao = 0;
       

		try {
			//faz registro do mestre com o nome dado
			Registry registry = LocateRegistry.getRegistry(host);
                        
			//objeto remoto que o qual executara os metodos			
			final InterfaceMestre stub = (InterfaceMestre)registry.lookup("ReferenciaMestre");


			System.out.println("Tamanho do Vetor;Tempo de Execução Estático;Tempo de Execução Distribuido");			
			for(int i=500; i<=1000000; i += 500){
				 vetorInicial = g.gerarVetor(i);

				//Executa Calculo Serial Não paralelizado e calcula o tempo gasto
				tempoInicialEstatico = System.nanoTime();
				resultado_estatico = somar(vetorInicial);
				tempoFinalEstatico = System.nanoTime();


				//inicio do tempo de execucao do mestre
				tempoInicial = System.nanoTime();
				//CLiente passa para o mestre o vetor de bytes
				resultado = stub.somar(vetorInicial);
				//fim do tempo de execucao do mestre
				tempoFinal = System.nanoTime();


				tempoExecucaoEstatico = (tempoFinalEstatico - tempoInicialEstatico);
				tempoExecucao = (tempoFinal - tempoInicial);

				System.out.println(vetorInicial.size() + ";" + tempoExecucaoEstatico + ";" + tempoExecucao);
			}
			
			

		} catch (Exception e) {
			System.err.println("Erro encontrado (cliente): " + e.toString());
			e.printStackTrace();
		}
	}
}

//import java.io.FileNotFoundException;
//import java.io.UnsupportedEncodingException;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//
//
//public class ImplementacaoCliente {
////	private static byte checksum_resultado;
////	private final byte[] checksum_vetor;
////        
////        public ImplementacaoCliente() {
////            Gerador g = new Gerador();
////            this.checksum_vetor = g.gerarVetor();
////        }
//
//	@SuppressWarnings("unused")
//	public static void main(String[] args) {
//            
//            byte checksum_resultado;
//            byte[] checksum_vetor = null;
//
//		String host = null, situacao = null;
//
//		if (args.length == 1) {
//		 	situacao = args[0];
//		}
//		if (args.length == 2) {
//		 	situacao = args[0];
//		 	host = args[1];
//		}
//
//		Registry registry;
//
//		try {
//			registry = LocateRegistry.getRegistry(host);
//			ImplementacaoMestre stub = (ImplementacaoMestre) registry.lookup("ReferenciaMestre");
//		
//			checksum_resultado = stub.somar(checksum_vetor);
//                        System.out.println("Resultado da soma: " + checksum_resultado);
//				
//		} catch (RemoteException | NotBoundException e) {
//			e.printStackTrace();
//		}
//	}
//}
