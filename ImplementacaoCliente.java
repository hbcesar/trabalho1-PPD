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
		String nome = "localhost";

		Gerador g = new Gerador();
		
		List<Byte> vetorInicial = new ArrayList<>();
        vetorInicial = g.gerarVetor();

		byte resultado_estatico = somar(vetorInicial);
		

		try {
			//faz registro do mestre com o nome dado
			Registry registry = LocateRegistry.getRegistry(nome);
                        
			//objeto remoto que o qual executara os metodos			
			final InterfaceMestre stub = (InterfaceMestre)registry.lookup("ReferenciaMestre");

			//inicio do tempo de execucao do mestre
			long tempoInicial = System.nanoTime();
			
			//CLiente passa para o mestre o vetor de bytes
			byte resultado = stub.somar(vetorInicial);

			long tempoFinal = System.nanoTime();

			double tempoExecucao = (tempoFinal - tempoInicial)/Math.pow(10,9);

			System.out.println("Resultado: "+resultado);			
			System.out.println("Resultado estatico: "+(resultado_estatico));
			System.out.println("Tempo de execucao: "+tempoExecucao+" s");

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
