import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ThreadMestreEscravo extends Thread {

    //Referencia para escravo que receberá o trabalho sujo a ser feito
    //(tá final prq o NetBeans falou que isso era bacana)
    public final InterfaceEscravo escravo;
    
    //armazena resultato parcial do checksum
    public byte soma;
    
    //Subvetor que será enviado ao escravo
    List<Byte> subvetor = new ArrayList<>();

    //Construtor da classe
    public ThreadMestreEscravo(InterfaceEscravo escravo, List<Byte> subvetor) {
        this.escravo = escravo;
        this.subvetor = subvetor;
    }

    //Quando a thread é iniciada, pega o vetorzinho e manda pro escravo
    @Override
    public void run() {

        try {
            soma = escravo.somar(subvetor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
