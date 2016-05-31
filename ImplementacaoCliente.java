
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ImplementacaoCliente {

    //metodo usado pelo cliente para executar soma serial (para comparação)
    public static Byte somar(List<Byte> vetor) {
        byte sum = 0;

        for (Byte number : vetor) {
            sum = (byte) (sum ^ number);
        }

        return sum;
    }

    public static void main(String[] args) {

        //recebe nome que foi associado ao mestre (para buscar no Registry)
        String host = null;
        if (args.length > 0) {
            host = args[0];
        }

        //cria classe que gera vetor randomico
        Gerador g = new Gerador();

        //variavel que recebera o vetor inicial
        List<Byte> vetorInicial = new ArrayList<>();

        /**
         * ******************************************
         * Variaveis que armazenam informacoes para comparação.
                ********************************************
         */
        //Calculo Serial
        long tempoInicialEstatico = 0;
        long tempoFinalEstatico = 0;
        byte resultado_estatico = 0;

        //Calculo Paralelo (e Distribuido)
        long tempoInicial = 0;
        long tempoFinal = 0;
        byte resultado = 0;



        //Calcula o tempo de execucao para ambos os casos
        double tempoExecucaoEstatico = 0;
        double tempoExecucao = 0;

        try {
            //faz registro do mestre com o nome dado
            Registry registry = LocateRegistry.getRegistry(host);

            //objeto remoto que o qual executara os metodos
            final InterfaceMestre stub = (InterfaceMestre) registry.lookup("ReferenciaMestre");

            //Imprime o header do CSV
            System.out.println("Tamanho do Vetor;Tempo de Execução Estático;Tempo de Execução Distribuido");

            //testa vetores (tamanho de 500 até 10ˆ6 com intervalos de 500)
            for (int i = 500; i <= 100000; i += 500) {
                vetorInicial = g.gerarVetor(i);

                //Executa Calculo Serial Não paralelizado
//                tempoInicialEstatico = System.nanoTime();
//                resultado_estatico = somar(vetorInicial);
//                tempoFinalEstatico = System.nanoTime();

                //Executa calculo Paralelo
                tempoInicial = System.nanoTime();
                resultado = stub.somar(vetorInicial);
                tempoFinal = System.nanoTime();

                //Calcula tempo gasto em ambos os casos
                tempoExecucaoEstatico = 0; //(tempoFinalEstatico - tempoInicialEstatico);
                tempoExecucao = (tempoFinal - tempoInicial);

                System.out.println(vetorInicial.size() + ";" + tempoExecucaoEstatico + ";" + tempoExecucao);
            }

        } catch (Exception e) {
            System.err.println("Erro encontrado (cliente): " + e.toString());
            e.printStackTrace();
        }
    }
}
