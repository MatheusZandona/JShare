package br.univel.classes;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;

public class ExecutaCliente {

	public static IServer server ;
	
	public static void main(String[] args) {
		conectar();
		
		Cliente c1 = new Cliente();
		c1.setId(1);
		c1.setIp(LerIp.ler());
		c1.setNome("Matheus");
		c1.setPorta(1818);
//		
//		Cliente c2 = new Cliente();
//		c2.setId(1);
//		c2.setIp("192.168.1.2");
//		c2.setNome("Maria");
//		c2.setPorta(1818);
		
		List<Arquivo> arquivos = new ArrayList<>();
		arquivos = ListarDiretoriosArquivos.listar(new File("Share\\Uploads"));
		
		//Arquivo para baixar
		Arquivo a1 = new Arquivo();
		a1.setNome("texto");
		a1.setExtensao("txt");
		a1.setTamanho(1);
		a1.setId(1);
		a1.setPath("C:\\eclipse neon\\Workspace\\Projeto Compartilha Arquivos\\Share\\Uploads");
		
		try {
			server.registrarCliente(c1);
//			server.registrarCliente(c2);
//			server.desconectar(c1);
			server.publicarListaArquivos(c1, arquivos);
//			HashMap<Cliente, List<Arquivo>> mapa = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo("texto", TipoFiltro.NOME, "txt");
			server.procurarArquivo("texto", TipoFiltro.NOME, "txt");
			server.baixarArquivo(c1, a1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void conectar() {
		Registry registry;
		try {
			registry =  LocateRegistry.getRegistry("127.0.0.1", 1818);
			server = (IServer) registry.lookup(IServer.NOME_SERVICO);
			
		} catch (Exception e) {
			
		}
	}

}
