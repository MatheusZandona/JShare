package br.univel.classes;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.channels.ShutdownChannelGroupException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.swing.plaf.synth.SynthScrollPaneUI;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;

public class ExecutaServidorCliente implements IServer{

	public static IServer server ;
	public List<Cliente> clientes = new ArrayList<>();
	public HashMap<Cliente, List<Arquivo>> mapArquivos = new HashMap<>();
	public static IServer serverCli ;
	
	
	public static void main(String[] args) {
		
		iniciarServidor();
		
		
		// Cliente =========================================================================
		conectarServidor("127.0.0.1",1818);
		
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
		
		//Arquivo para baixar // simulando o selecionar na grid para buscar o arquivo
		Arquivo a1 = new Arquivo();
		a1.setNome("texto");
		a1.setExtensao("txt");
		a1.setTamanho(1);
		a1.setId(1);
		a1.setPath("C:\\eclipse neon\\Workspace\\Projeto Compartilha Arquivos\\Share\\Uploads");
		
		List<Arquivo> arquivos = new ArrayList<>();
		arquivos = ListarDiretoriosArquivos.listar(new File("Share\\Uploads"));
		
		try {
			server.registrarCliente(c1);
//			server.registrarCliente(c2);
//			server.desconectar(c1);
			server.publicarListaArquivos(c1, arquivos);
			HashMap<Cliente, List<Arquivo>> mapa = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo("texto", TipoFiltro.NOME, "txt");
//			server.procurarArquivo("texto", TipoFiltro.NOME, "txt");
			
			Set<Cliente> chaves = mapa.keySet();
			
			for (Cliente cliente : chaves) {
				if(cliente != null){
					List<Arquivo> arquivos1 = (ArrayList<Arquivo>) mapa.get(cliente);
					for (Arquivo arquivo : arquivos1) {
						byte[] dados = null; 
						dados = server.baixarArquivo(cliente, arquivo);
						
						LeituraEscritaDeArquivos le = new LeituraEscritaDeArquivos();
						le.escreva(new File("C:\\eclipse neon\\Workspace\\Projeto Compartilha Arquivos\\Share\\Downloads\\"
											+arquivo.getNome()), dados);
					}
					mapa.put(cliente, arquivos1);
				}			
			}
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void iniciarServidor() {
		// Servidor =====================================================================
		System.out.println("Iniciando servidor");
		ExecutaServidorCliente servidor = new ExecutaServidorCliente();
		
		IServer servico;
		try {
			
			servico = (IServer) UnicastRemoteObject.exportObject(servidor, 0);
			Registry registry = LocateRegistry.createRegistry(1818);
			registry.rebind(IServer.NOME_SERVICO, servico);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private static void conectarServidor(String ip, int porta) {
		Registry registry;
		try {
			registry =  LocateRegistry.getRegistry(ip, porta);
			server = (IServer) registry.lookup(IServer.NOME_SERVICO);
			
		} catch (Exception e) {
			
		}
	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		clientes.add(c);
		System.out.println("Cliente "+c.getIp() + " - " + c.getNome() + " se registrou no servidor.");		
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		mapArquivos.put(c, lista);
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro) throws RemoteException {
		HashMap<Cliente, List<Arquivo>> mapa = new HashMap<>();
		
		Set<Cliente> chaves = mapArquivos.keySet();
		
		for (Cliente cliente : chaves) {
			if(cliente != null){
				List<Arquivo> arquivos = (ArrayList<Arquivo>) mapArquivos.get(cliente);
				List<Arquivo> arquivosEncontrados = new ArrayList<>();
						
				for (Arquivo arquivo : arquivos) {
					if(arquivo.getNome().contains(query)){
						System.out.println(cliente.getIp()+" - "+cliente.getNome() +" - "+ arquivo.getNome());
						arquivosEncontrados.add(arquivo);
					}
				}
				mapa.put(cliente, arquivosEncontrados);
			}			
		}
		
		return mapa;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
		
		byte[] dados = null;
		
		LeituraEscritaDeArquivos le = new LeituraEscritaDeArquivos();
		dados = le.leia(new File(arq.getPath()));
		
		return dados;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {
		clientes.remove(c);
		System.out.println("Cliente "+c.getIp() + " - " + c.getNome() + " se desconectou do servidor.");	
	}

	
}
