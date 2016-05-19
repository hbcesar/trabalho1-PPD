import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceEscravo extends java.rmi.Remote {

	//retorna id do escravo atribuida pelo mestre (usando UUID)
	public int getId() throws RemoteException;

	//atribui ID ao escravo
	public void setId(int id) throws RemoteException;

	//Fucao usada pelo mestre para solicitar ao escravo que faca a soma do vetor
	public Byte somar(List<Byte> vetor) throws RemoteException;

}
