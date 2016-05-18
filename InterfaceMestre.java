import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceMestre extends java.rmi.Remote  {

	//Funcao para incluir escravo recem criado na fila de escravos registrados do mestre
	public void incluirFilaEscravos(InterfaceEscravo escravo) throws RemoteException;

	//Funcao usada pelo escravo para se desregistrar do mestre quando esse cai por algum motivo
	public void removerFilaEscravos(String id) throws RemoteException;

	//Funcao usada pelo cliente para solicitar soma do vetor ao mestre
	public byte somar(byte[] vetor) throws RemoteException, Exception;
}
