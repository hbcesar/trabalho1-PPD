import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

@SuppressWarnings("unused")
public class ImplementacaoEscravo implements InterfaceEscravo {

	public String id;

	@Override
        public String getId() throws RemoteException {
		return id;
	}

        @Override
	public void setId(String id) throws RemoteException {
		this.id = id;
	}

	/* Metodo que ordena o pedaco do vetor do cliente recebido pelo escravo. */
        @Override
	public byte somar(byte[] vetor) throws RemoteException {
		byte sum = 0;

		for(byte number : vetor)
			sum += number; 
		
		return sum;
	}

	/* Desregistra o escravo da lista do Mestre em caso termino. */
	public void attachShutDownHook(final ImplementacaoMestre mestre) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					mestre.removerFilaEscravos(id);
					// System.out.println("Slave free!");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {

		String host = (args.length < 1) ? null : args[1];

		ImplementacaoMestre mestre;

		if (args.length > 0) {
			System.setProperty("java.rmi.server.hostname", args[0]);
		}

		try {
			/* Procura Mestre no Registry. */
			Registry registry = LocateRegistry.getRegistry(host);
			mestre = (ImplementacaoMestre) registry.lookup("ReferenciaMestre");

			ImplementacaoEscravo escravo = new ImplementacaoEscravo();

			//http://www.javapractices.com/topic/TopicAction.do?Id=56
			escravo.setId(UUID.randomUUID().toString());

			ImplementacaoEscravo stub = (ImplementacaoEscravo) UnicastRemoteObject.exportObject(escravo, 2001);

			//EscravoService stub = (EscravoService) UnicastRemoteObject.exportObject(escravo, 0);

			//De acordo com especificação, escravo deve se registrar no menino mestre
			mestre.incluirFilaEscravos(stub, escravo.getId());
			escravo.attachShutDownHook(mestre);

		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}