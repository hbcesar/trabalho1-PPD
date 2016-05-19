import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ImplementacaoEscravo implements InterfaceEscravo {

	public int id;

	@Override
        public int getId() throws RemoteException {
		return id;
	}

        @Override
	public void setId(int id) throws RemoteException {
		this.id = id;
	}

	/* Metodo que ordena o pedaco do vetor do cliente recebido pelo escravo. */
        @Override
	public Byte somar(List<Byte> vetor) throws RemoteException {
		byte sum = 0;

		for(Byte number : vetor)
			sum = (byte) (sum ^ number); 
		
		return sum;
	}

	// Desregistra o escravo da lista do Mestre em caso termino.
	public void attachShutDownHook(final InterfaceMestre mestre) {
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

		InterfaceMestre mestre;

		if (args.length > 0) {
			System.setProperty("java.rmi.server.hostname", args[0]);
		}

		try {
                    /* Procura Mestre no Registry. */
                    Registry registry = LocateRegistry.getRegistry(host);
                    mestre = (InterfaceMestre) registry.lookup("ReferenciaMestre");

                    ImplementacaoEscravo escravo = new ImplementacaoEscravo();
                    

                    //http://www.javapractices.com/topic/TopicAction.do?Id=56
//                    escravo.setId(UUID.randomUUID().toString());

                    InterfaceEscravo stub = (InterfaceEscravo) UnicastRemoteObject.exportObject(escravo, 0);

                    //EscravoService stub = (EscravoService) UnicastRemoteObject.exportObject(escravo, 0);

                    //De acordo com especificação, escravo deve se registrar no menino mestre
                    mestre.incluirFilaEscravos(stub);
                    escravo.attachShutDownHook(mestre);

		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}
