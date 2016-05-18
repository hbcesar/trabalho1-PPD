import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ImplementacaoCliente {
//	private static byte checksum_resultado;
//	private final byte[] checksum_vetor;
//        
//        public ImplementacaoCliente() {
//            Gerador g = new Gerador();
//            this.checksum_vetor = g.gerarVetor();
//        }

	@SuppressWarnings("unused")
	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {
            
            byte checksum_resultado;
            byte[] checksum_vetor = null;

		String host = null, situacao = null;

		if (args.length == 1) {
		 	situacao = args[0];
		}
		if (args.length == 2) {
		 	situacao = args[0];
		 	host = args[1];
		}

		Registry registry;

		try {
			registry = LocateRegistry.getRegistry(host);
			ImplementacaoMestre stub = (ImplementacaoMestre) registry.lookup("ReferenciaMestre");
		
			checksum_resultado = stub.somar(checksum_vetor);
                        System.out.println("Resultado da soma: " + checksum_resultado);
				
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

    
}
