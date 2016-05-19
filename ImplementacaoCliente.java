import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ImplementacaoCliente {	 
	public static void main(String[] args) {
		//nome que sera associado ao mestre
		String nome = "localhost";

		Gerador g = new Gerador();
		List<Byte> vetorInicial = new ArrayList<>();
                vetorInicial = g.gerarVetor();

		try {
			//faz registro do mestre com o nome dado
			Registry registry = LocateRegistry.getRegistry(nome);
                        
			//objeto remoto que o qual executara os metodos			
			final InterfaceMestre stub = (InterfaceMestre)registry.lookup(ImplementacaoMestre.class.getSimpleName());

			//CLiente passa para o mestre o vetor de bytes
			byte resultado = stub.somar(vetorInicial);

			//System.out.println("response: " + Arrays.toString(vetor));	

		} catch (Exception e) {
			System.err.println("Erro encontrado (cliente): " + e.toString());
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
