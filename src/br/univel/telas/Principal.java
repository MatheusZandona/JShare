package br.univel.telas;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import br.univel.classes.LeituraEscritaDeArquivos;
import br.univel.classes.LerIp;
import br.univel.classes.ListarDiretoriosArquivos;
import br.univel.classes.ModelArquivos;
import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class Principal extends JFrame implements IServer{
	
	public static IServer server ;
	public List<Cliente> clientes = new ArrayList<>();
	public static HashMap<Cliente, List<Arquivo>> mapArquivos = new HashMap<>();
	public static IServer serverCli ;
	public Cliente cLocal;
	
	private boolean servidorOnline = false;
	
	private JTextField txtPorta;
	private JTextField txtIp;
	private JTable tableArquivos;
	JTextPane txtPaneLogServidor;
	JTextPane txtPaneLogCliente;
	private JTextField txtPesq;
	JRadioButton rbExtensao;
	JRadioButton rbNome;
	JRadioButton rbTamMax;
	JRadioButton rbTamMin;
	JButton btnConectaServidor;
	JButton btnDesconectar;
	JButton btnPesq;
	JButton btnIniciarServidor;
	
	public Principal() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{88, 0, 0};
		gridBagLayout.rowHeights = new int[]{227, 221, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel panelCliente = new JPanel();
		GridBagConstraints gbc_panelCliente = new GridBagConstraints();
		gbc_panelCliente.insets = new Insets(0, 0, 5, 5);
		gbc_panelCliente.fill = GridBagConstraints.BOTH;
		gbc_panelCliente.gridx = 0;
		gbc_panelCliente.gridy = 0;
		getContentPane().add(panelCliente, gbc_panelCliente);
		GridBagLayout gbl_panelCliente = new GridBagLayout();
		gbl_panelCliente.columnWidths = new int[]{0, 0, 0};
		gbl_panelCliente.rowHeights = new int[]{0, 0, 194, 24, 0};
		gbl_panelCliente.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelCliente.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelCliente.setLayout(gbl_panelCliente);
		
		JLabel lblIp = new JLabel("IP: ");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.insets = new Insets(0, 5, 5, 5);
		gbc_lblIp.anchor = GridBagConstraints.WEST;
		gbc_lblIp.gridx = 0;
		gbc_lblIp.gridy = 0;
		panelCliente.add(lblIp, gbc_lblIp);
		
		txtIp = new JTextField();
		txtIp.setText(LerIp.ler());
		GridBagConstraints gbc_txtIp = new GridBagConstraints();
		gbc_txtIp.insets = new Insets(0, 0, 5, 5);
		gbc_txtIp.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIp.gridx = 1;
		gbc_txtIp.gridy = 0;
		panelCliente.add(txtIp, gbc_txtIp);
		txtIp.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("PORTA: ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 5, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		panelCliente.add(lblNewLabel, gbc_lblNewLabel);
		
		txtPorta = new JTextField();
		txtPorta.setText("1818");
		GridBagConstraints gbc_txtPorta = new GridBagConstraints();
		gbc_txtPorta.insets = new Insets(0, 0, 5, 5);
		gbc_txtPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPorta.gridx = 1;
		gbc_txtPorta.gridy = 1;
		panelCliente.add(txtPorta, gbc_txtPorta);
		txtPorta.setColumns(10);
		
		btnConectaServidor = new JButton("Conectar Servidor");
		btnConectaServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(!servidorOnline){
						iniciarServidor();
					}
					conectarServidor(txtIp.getText(), Integer.parseInt(txtPorta.getText()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridwidth = 2;
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_2.gridx = 0;
		gbc_scrollPane_2.gridy = 2;
		panelCliente.add(scrollPane_2, gbc_scrollPane_2);
		
		txtPaneLogCliente = new JTextPane();
		scrollPane_2.setViewportView(txtPaneLogCliente);
		txtPaneLogCliente.setEditable(false);
		GridBagConstraints gbc_btnConectaServidor = new GridBagConstraints();
		gbc_btnConectaServidor.insets = new Insets(0, 0, 0, 5);
		gbc_btnConectaServidor.gridx = 0;
		gbc_btnConectaServidor.gridy = 3;
		panelCliente.add(btnConectaServidor, gbc_btnConectaServidor);
		
		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setEnabled(false);
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					desconectar(cLocal);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.gridx = 1;
		gbc_btnDesconectar.gridy = 3;
		panelCliente.add(btnDesconectar, gbc_btnDesconectar);
		
		JPanel panelServidor = new JPanel();
		GridBagConstraints gbc_panelServidor = new GridBagConstraints();
		gbc_panelServidor.insets = new Insets(0, 0, 5, 0);
		gbc_panelServidor.fill = GridBagConstraints.BOTH;
		gbc_panelServidor.gridx = 1;
		gbc_panelServidor.gridy = 0;
		getContentPane().add(panelServidor, gbc_panelServidor);
		GridBagLayout gbl_panelServidor = new GridBagLayout();
		gbl_panelServidor.columnWidths = new int[]{0, 0};
		gbl_panelServidor.rowHeights = new int[]{51, 167, 15, 0};
		gbl_panelServidor.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelServidor.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelServidor.setLayout(gbl_panelServidor);
		
		btnIniciarServidor = new JButton("Iniciar Servidor");
		btnIniciarServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				iniciarServidor();
			}
		});
		GridBagConstraints gbc_btnIniciarServidor = new GridBagConstraints();
		gbc_btnIniciarServidor.insets = new Insets(0, 0, 5, 0);
		gbc_btnIniciarServidor.gridx = 0;
		gbc_btnIniciarServidor.gridy = 0;
		panelServidor.add(btnIniciarServidor, gbc_btnIniciarServidor);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		panelServidor.add(scrollPane_1, gbc_scrollPane_1);
		
		txtPaneLogServidor = new JTextPane();
		txtPaneLogServidor.setFont(new Font("Arial", Font.BOLD, 14));
		txtPaneLogServidor.setMargin(new Insets(3, 3, 0, 3));
		scrollPane_1.setViewportView(txtPaneLogServidor);
		txtPaneLogServidor.setEditable(false);
		
		JPanel panelTabela = new JPanel();
		GridBagConstraints gbc_panelTabela = new GridBagConstraints();
		gbc_panelTabela.gridwidth = 2;
		gbc_panelTabela.insets = new Insets(0, 0, 0, 5);
		gbc_panelTabela.fill = GridBagConstraints.BOTH;
		gbc_panelTabela.gridx = 0;
		gbc_panelTabela.gridy = 1;
		getContentPane().add(panelTabela, gbc_panelTabela);
		GridBagLayout gbl_panelTabela = new GridBagLayout();
		gbl_panelTabela.columnWidths = new int[]{0, 0, 0};
		gbl_panelTabela.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelTabela.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panelTabela.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelTabela.setLayout(gbl_panelTabela);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		panelTabela.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		rbNome = new JRadioButton("Nome");
		rbNome.setSelected(true);
		GridBagConstraints gbc_rbNome = new GridBagConstraints();
		gbc_rbNome.insets = new Insets(0, 0, 0, 5);
		gbc_rbNome.gridx = 0;
		gbc_rbNome.gridy = 0;
		panel.add(rbNome, gbc_rbNome);
		
		rbExtensao = new JRadioButton("Extensão");
		GridBagConstraints gbc_rbExtensao = new GridBagConstraints();
		gbc_rbExtensao.insets = new Insets(0, 0, 0, 5);
		gbc_rbExtensao.gridx = 1;
		gbc_rbExtensao.gridy = 0;
		panel.add(rbExtensao, gbc_rbExtensao);
		
		rbTamMin = new JRadioButton("Tam. Min.");
		GridBagConstraints gbc_rbTamMin = new GridBagConstraints();
		gbc_rbTamMin.insets = new Insets(0, 0, 0, 5);
		gbc_rbTamMin.gridx = 2;
		gbc_rbTamMin.gridy = 0;
		panel.add(rbTamMin, gbc_rbTamMin);
		
		rbTamMax = new JRadioButton("Tam. Max.");
		GridBagConstraints gbc_rbTamMax = new GridBagConstraints();
		gbc_rbTamMax.gridx = 3;
		gbc_rbTamMax.gridy = 0;
		panel.add(rbTamMax, gbc_rbTamMax);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbNome);
		buttonGroup.add(rbExtensao);
		buttonGroup.add(rbTamMin);
		buttonGroup.add(rbTamMax);
		
		JLabel lblArquivosDisponveis = new JLabel("Arquivos Disponíveis");
		lblArquivosDisponveis.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblArquivosDisponveis = new GridBagConstraints();
		gbc_lblArquivosDisponveis.gridwidth = 2;
		gbc_lblArquivosDisponveis.insets = new Insets(0, 0, 5, 0);
		gbc_lblArquivosDisponveis.gridx = 0;
		gbc_lblArquivosDisponveis.gridy = 1;
		panelTabela.add(lblArquivosDisponveis, gbc_lblArquivosDisponveis);
		
		txtPesq = new JTextField();
		txtPesq.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				HashMap<Cliente, List<Arquivo>> mapPesq = new HashMap<>();
				try {
					if(rbNome.isSelected()){
						mapPesq = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo(txtPesq.getText(), TipoFiltro.NOME, "");
					}else{
						if(rbExtensao.isSelected()){
							mapPesq = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo("", TipoFiltro.EXTENSAO, txtPesq.getText());
						}else{
							if(rbTamMax.isSelected()){
								mapPesq = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo("", TipoFiltro.TAMANHO_MAX, txtPesq.getText());
							}else{
								mapPesq = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo("", TipoFiltro.TAMANHO_MIN, txtPesq.getText());
							}
						}
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				atualizaTable(mapPesq);
			}
		});
		GridBagConstraints gbc_txtPesq = new GridBagConstraints();
		gbc_txtPesq.insets = new Insets(0, 0, 5, 5);
		gbc_txtPesq.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPesq.gridx = 0;
		gbc_txtPesq.gridy = 2;
		panelTabela.add(txtPesq, gbc_txtPesq);
		txtPesq.setColumns(10);
		
		btnPesq = new JButton("Pesq");
		btnPesq.setEnabled(false);
		btnPesq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				atualizarMapArquivos();
//				atualizaTable();
				HashMap<Cliente, List<Arquivo>> mapPesq = new HashMap<>();
				try {
					if(rbNome.isSelected()){
						mapPesq = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo(txtPesq.getText(), TipoFiltro.NOME, "");
					}else{
						if(rbExtensao.isSelected()){
							mapPesq = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo("", TipoFiltro.EXTENSAO, txtPesq.getText());
						}else{
							if(rbTamMax.isSelected()){
								mapPesq = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo("", TipoFiltro.TAMANHO_MAX, txtPesq.getText());
							}else{
								mapPesq = (HashMap<Cliente, List<Arquivo>>) server.procurarArquivo("", TipoFiltro.TAMANHO_MIN, txtPesq.getText());
							}
						}
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				atualizaTable(mapPesq);
			}
		});
		GridBagConstraints gbc_btnPesq = new GridBagConstraints();
		gbc_btnPesq.insets = new Insets(0, 0, 5, 0);
		gbc_btnPesq.gridx = 1;
		gbc_btnPesq.gridy = 2;
		panelTabela.add(btnPesq, gbc_btnPesq);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		panelTabela.add(scrollPane, gbc_scrollPane);
		
		tableArquivos = new JTable();
		scrollPane.setColumnHeaderView(tableArquivos);
		
		scrollPane.setViewportView(tableArquivos);
		
		JButton btnDownload = new JButton("Efetuar Download");
		btnDownload.setMinimumSize(new Dimension(400, 23));
		btnDownload.setMaximumSize(new Dimension(400, 23));
		btnDownload.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDownload.setBackground(Color.green);  
		btnDownload.setForeground(Color.BLACK); 
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				efetuarDownload();
			}
		});
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.insets = new Insets(0, 0, 0, 5);
		gbc_btnDownload.gridx = 0;
		gbc_btnDownload.gridy = 4;
		panelTabela.add(btnDownload, gbc_btnDownload);
	}
	
	
	public static void main(String[] args) {
		
		Principal p = new Principal();
		p.setSize(1500,700);
		p.setVisible(true);
		p.setLocationRelativeTo(null);
		
	}


	public void executar() {
		atualizaTable(mapArquivos);
		
	}

	public void iniciarServidor() {
		// Servidor =====================================================================
		mostrarMensagemServidor("Iniciando servidor...");
//		ExecutaServidorCliente servidor = new ExecutaServidorCliente();
		
		IServer servico;
		try {
			System.setProperty("java.rmi.server.hostname",LerIp.ler());
			
			servico = (IServer) UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.createRegistry(1818);
			
			registry.rebind(IServer.NOME_SERVICO, servico);
			mostrarMensagemServidor("Aguardando Cliente se conectar...");
			
			servidorOnline = true;
			
			btnIniciarServidor.setEnabled(false);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void conectarServidor(String ip, int porta) throws Exception {
		cLocal = new Cliente();
		cLocal.setId(1);
		cLocal.setIp(LerIp.ler());
		cLocal.setNome("Zandoná");
		cLocal.setPorta(1818);
		
		List<Arquivo> arquivos = new ArrayList<>();
		arquivos = ListarDiretoriosArquivos.listar(new File("Share\\Uploads"));
		
		Registry registry;
		mostrarMensagemCliente("Conectando no servidor...");
		registry =  LocateRegistry.getRegistry(ip, porta);
		server = (IServer) registry.lookup(IServer.NOME_SERVICO);
		mostrarMensagemCliente("Você esta conectado.");
			
			
		server.registrarCliente(cLocal);
		server.publicarListaArquivos(cLocal, arquivos);
		
		btnConectaServidor.setEnabled(false);
		btnDesconectar.setEnabled(true);
		btnPesq.setEnabled(true);
		atualizaTable(mapArquivos);
	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		clientes.add(c);
		mostrarMensagemServidor("Cliente "+c.getIp() + " - " + c.getNome() + " se registrou no servidor.");
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		mapArquivos.put(c, lista);
		mostrarMensagemServidor("Cliente "+c.getIp() + " - " + c.getNome() + " publicou arquivo(s).");
		atualizaTable(mapArquivos);
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro) throws RemoteException {
		query = query.toUpperCase();
		filtro = filtro.toUpperCase();
		
		HashMap<Cliente, List<Arquivo>> mapa = new HashMap<>();
		
		Set<Cliente> chaves = mapArquivos.keySet();
		
		for (Cliente cliente : chaves) {
			if(cliente != null){
				List<Arquivo> arquivos = (ArrayList<Arquivo>) mapArquivos.get(cliente);
				List<Arquivo> arquivosEncontrados = new ArrayList<>();
						
				for (Arquivo arquivo : arquivos) {
//					if(arquivo.getNome().contains(query)){
//						mostrarMensagemServidor(cliente.getIp()+" - "+cliente.getNome() +" - "+ arquivo.getNome());
//						arquivosEncontrados.add(arquivo);
//					}
					
					if(TipoFiltro.NOME == tipoFiltro){
						if(arquivo.getNome().toUpperCase().contains(query) || query.isEmpty()){
							arquivosEncontrados.add(arquivo);
						}
					}else{
						if(TipoFiltro.EXTENSAO == tipoFiltro){
							if(arquivo.getExtensao().toUpperCase().contains(filtro) || filtro.isEmpty()){
								arquivosEncontrados.add(arquivo);
							}
						}else{
							if(TipoFiltro.TAMANHO_MAX == tipoFiltro){
								int tamMax = 0;
								tamMax = Integer.parseInt(filtro);
								if (arquivo.getTamanho() <= tamMax || filtro.isEmpty()) {
									arquivosEncontrados.add(arquivo);
								}
							}else{
								if(tipoFiltro == TipoFiltro.TAMANHO_MIN){
									int tamMin = 0;
									tamMin = Integer.parseInt(filtro);
									if (arquivo.getTamanho() >= tamMin || filtro.isEmpty()) {
										arquivosEncontrados.add(arquivo);
									}
								}
							}
						}
					}
				}
				if(arquivosEncontrados.size() > 0){
					mapa.put(cliente, arquivosEncontrados);
				}
			}			
		}
		
		return mapa;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
		
		byte[] dados = null;
		
		LeituraEscritaDeArquivos le = new LeituraEscritaDeArquivos();
		dados = le.leia(new File(arq.getPath()));
		
		mostrarMensagemServidor("Cliente "+cli.getIp() + " - " + cli.getNome() + " fez download do arquivo " 
				+ arq.getNome() + "." + arq.getExtensao());
		
		return dados;
	}
  
	@Override
	public void desconectar(Cliente c) throws RemoteException {
		if(c != null){
			mapArquivos.remove(c);
			mostrarMensagemServidor("Cliente "+c.getIp() + " - " + c.getNome() + " se desconectou do servidor.");
			mostrarMensagemCliente("Você se desconectou.");
			btnDesconectar.setEnabled(false);
			btnConectaServidor.setEnabled(true);
			btnPesq.setEnabled(false);
			atualizaTable(mapArquivos);
			
			servidorOnline = false;
		}else{
			mostrarMensagemServidor("Impossivel desconctar cliente null.");
			mostrarMensagemCliente("Não foi possivel se desconectar.");
		}
	}
	
	protected void atualizaTable(Map<Cliente, List<Arquivo>> dados) {
//		Map<Cliente, List<Arquivo>> dados = gerarDados();
		ModelArquivos modelo ;
		modelo = new ModelArquivos(dados);
		tableArquivos.setModel(modelo);
		
	}
	
	public void mostrarMensagemServidor(String msg){
		txtPaneLogServidor.setText(txtPaneLogServidor.getText() +"\n" + msg);
	}
	
	public void mostrarMensagemCliente(String msg){
		txtPaneLogCliente.setText(txtPaneLogCliente.getText() +"\n" + msg);
	}
	
	public void efetuarDownload(){
		
		int linha = tableArquivos.getSelectedRow();
		
		Arquivo arq = new Arquivo();
		arq = ((ModelArquivos) tableArquivos.getModel()).getArquivo(linha);
		
		Cliente c = new Cliente();
		c = ((ModelArquivos) tableArquivos.getModel()).getCliente(linha);
		
		byte[] dados = null;
		
		try {
			Registry registry = LocateRegistry.getRegistry(c.getIp(),c.getPorta());
			IServer servico = (IServer) registry.lookup(IServer.NOME_SERVICO);
			dados = servico.baixarArquivo(cLocal, arq);
			
			LeituraEscritaDeArquivos le = new LeituraEscritaDeArquivos();
			le.escreva(new File("Share\\Downloads\\"
								+arq.getNome() + "." + arq.getExtensao()), dados);
			
			mostrarMensagemCliente("Download efetuado com sucesso.");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}

}
