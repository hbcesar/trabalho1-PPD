
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
    private Map<Integer, InterfaceEscravo> listaEscravos = new HashMap<>();

    //Lista de threads (de escravos executando)s
    private List<Thread> threads = new ArrayList<>();

    //Lista de "sub-somas" dos escravos
    private List<Byte> subVetor = new ArrayList<>();

    private int idEscravo = 1;

    // Captura o CTRL+C
    //http://stackoverflow.com/questions/1611931/catching-ctrlc-in-java
    public void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {

                // Remove todos escravos da lista caso o mestre caia
                for (Map.Entry<Integer, InterfaceEscravo> escravo : listaEscravos
                        .entrySet()) {
                    listaEscravos.remove(escravo);
                }
                System.out.println(" Mestre Caiu! :(");
            }
        });
    }

    //Metodo usado pelo mestre para fazer a soma
    @Override
    public byte somar(List<Byte> vetor) throws RemoteException {
        int i = 0;
        List<Byte> resultados = new ArrayList<>();
        int tamVetor = vetor.size();
        int tamVetorEscravos = tamVetor / listaEscravos.size();
        int resto = tamVetor % listaEscravos.size();
        int range = 0;
        int inicio = 0;
        int fim = tamVetorEscravos;

        //Lista com objetos que gerenciam as threads de cada escravo, usada para obter os valores das somas dos vetores parciais
        List<ThreadMestreEscravo> meninxs = new ArrayList<>();

        //Define o tamanho do subvetor que será enviado a cada escravo
        //http://stackoverflow.com/questions/46898/how-to-efficiently-iterate-over-each-entry-in-a-map
        for (Map.Entry<Integer, InterfaceEscravo> e : listaEscravos.entrySet()) {
            //se a divisao de vetores nao for exata, vai atribuindo +1 campo do vetor a cada escravo
            if (resto > 0) {
                fim++;
                resto--;
            }

            //Cria copia do vetor com tamanho desejado
            https://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html#subList(int,%20int)
            List<Byte> subVetor = new ArrayList<>();
            subVetor = new ArrayList<Byte>(vetor.subList(inicio, fim));
            range += tamVetorEscravos;

            inicio = fim;
            fim += tamVetorEscravos;

            //Cria as threads para executar os meninos escravos
            ThreadMestreEscravo exec = new ThreadMestreEscravo(e.getValue(), subVetor);
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
        for (ThreadMestreEscravo w : meninxs) {
            resultados.add(w.soma);
            i++;
        }

        return somarSubVetor(resultados);
    }

    //Registra Escravo na listinha do mestre
    @Override
    public void incluirFilaEscravos(InterfaceEscravo e)
            throws RemoteException {
        listaEscravos.put(idEscravo, e);
        e.setId(idEscravo);
        idEscravo++;

        System.out.println("Escravo " + e.getId() + " adicionado.");
    }

    //Remove escravo da listinha do mestre
    @Override
    public void removerFilaEscravos(InterfaceEscravo e) throws RemoteException {
        listaEscravos.remove(e.getId());
    }

    @Override
    public void removerFilaEscravos(int id) throws RemoteException {
        listaEscravos.remove(id);

        System.out.println("Escravo " + id + " removido.");
    }

    private byte somarSubVetor(List<Byte> vetor) {
        byte sum = 0;

        for (byte number : vetor) {
            sum = (byte) (sum ^ number);
        }

        return sum;
    }

    public static void main(String[] args) {
        //recebe nome que foi associado ao mestre (para buscar no Registry)
        String host = "localhost";

        if (args.length >= 1) {
            host = args[0];
        }

        //
        if (args.length > 0) {
            System.setProperty("java.rmi.server.hostname", args[0]);
        }

        System.out.println("Mestre conectando-se ao host: " + host);

        try {
            //Referencia para o mestre (pra fazer o bind)
            ImplementacaoMestre mestre = new ImplementacaoMestre();
            InterfaceMestre refMestre = (InterfaceMestre) UnicastRemoteObject.exportObject(mestre, 2001);

            //Pega referencia ao mestre no registro de nomes, e faz o bind (no middleware)
            Registry registry = LocateRegistry.getRegistry(host);
            registry.rebind("ReferenciaMestre", refMestre);

            //"Attach" acao para caso o mestre caia
            mestre.attachShutDownHook();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
