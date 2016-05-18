import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplementacaoMestre implements InterfaceMestre {
        //Lista de escravos guardada pelo mestre
	private Map<String, InterfaceEscravo> listaEscravos = new HashMap<>();
        
        //Lista de threads (de escravos executando)s
	private List<Thread> threads = new ArrayList<>();
        
        //Lista de "sub-somas" dos escravos
        private byte[] subVetor;

	// Captura o CTRL+C
	//http://stackoverflow.com/questions/1611931/catching-ctrlc-in-java
	public void attachShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {

				/* Remove todos escravos da lista em caso o mestre caia */
				for (Map.Entry<String, InterfaceEscravo> escravo : listaEscravos
						.entrySet()) {
					listaEscravos.remove(escravo);
				}
				System.out.println("Mestre Caiu :(");
			}
		});
	}


	//Metodo usado pelo cliente para fazer a soma
	@SuppressWarnings("unused")
	@Override
	public byte somar(byte[] vetor) throws RemoteException {
		int i = 0;
		byte[] resultados;
		int tamVetor = vetor.length;
		int tamVetorEscravos = tamVetor / this.threads.size();
		int resto = 0;
		int range = 0;
		int inicio = 0;
		int fim = tamVetorEscravos;

		//Lista com objetos que gerenciam as threads de cada escravo, usada para obter os valores das somas dos vetores parciais
		List<ThreadAC> meninxs = new ArrayList<>();
		
		//Define o tamanho do subvetor que será enviado a cada escravo
		if(tamVetorEscravos * listaEscravos.size() != tamVetor){
			resto = tamVetor % tamVetorEscravos;
		}
		
                //http://stackoverflow.com/questions/46898/how-to-efficiently-iterate-over-each-entry-in-a-map
		for(Map.Entry<String, InterfaceEscravo> e: listaEscravos.entrySet()){
			//se a divisao de vetores nao for exata, vai atribuindo +1 campo do vetor a cada menino escravo
			if(resto > 0){
				fim++;
				resto--;
			}

			//Cria copia do vetor com tamanho desejado
			//http://www.tutorialspoint.com/java/util/arrays_copyofrange_short.htm
			byte[] subVetor = Arrays.copyOfRange(vetor, inicio, fim);
			range += tamVetorEscravos;
			
			inicio = ++fim;
			fim += tamVetorEscravos;
			
                        //Cria as threads para executar os meninos escravos
			ThreadAC exec = new ThreadAC(e.getValue(), subVetor);
			meninxs.add(exec);
			Thread t = new Thread(exec);
			threads.add(t);

			t.start();
		}

		//Checa se todos escravos terminaram
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
                
		//Recebe a soma de cada escravo
		for (ThreadAC w : meninxs) {
			subVetor[i] = w.soma;
			i++;
		}

                //TODO:
                //mestre precisa somar esse menino antes de retornar
		return subVetor;
	}

	//Registra Escravo na listinha do mestre
	public void incluirFilaEscravos(InterfaceEscravo e, String id)
			throws RemoteException {
		listaEscravos.put(id, e);
		// System.out.println("Slave reporting for duty!");
	}

	//Remove escravo da listinha do mestre
        @Override
	public void removerFilaEscravos(String id) throws RemoteException {
		listaEscravos.remove(listaEscravos.get(id));
	}

	public static void main(String[] args) {
		String host = (args.length < 1) ? "" : args[0];

		if (args.length > 0) {
			System.setProperty("java.rmi.server.hostname", args[0]);
		}

		System.out.println("Connection try at host: " + host);

		try {
			ImplementacaoMestre obj = new ImplementacaoMestre();

			InterfaceMestre ref = (InterfaceMestre) UnicastRemoteObject
					.exportObject(obj, 2001);

			/* Comentado para executar remoto. */
			// MestreService ref = (MestreService)
			// UnicastRemoteObject.exportObject(obj, 0);

			Registry registry = LocateRegistry.getRegistry(host);
			registry.rebind("ReferenciaMestre", ref);
			obj.attachShutDownHook();
			// System.out.println("Master reporting!");
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

    @Override
    public void incluirFilaEscravos(InterfaceEscravo escravo) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	//Thread Mestre
	public class ThreadAC extends Thread {

		public final InterfaceEscravo escravo;
		public byte soma;
		public byte[] subvetor;

		public ThreadAC (InterfaceEscravo escravo, byte[] subvetor) {
			this.escravo = escravo;
			this.subvetor = subvetor;
		}

		/* Executa quando a thread e iniciada. */
		@Override
		public void run() {

			try {
				soma = escravo.somar(subvetor);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
