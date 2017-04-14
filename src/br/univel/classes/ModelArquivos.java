package br.univel.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;

public class ModelArquivos extends AbstractTableModel{

	private Object[][] matrix;

	/**
	 * Código precisa ser revisado caso haja um cliente (chave) com lista nula
	 * ou vazia, pois assume-se que todas as chaves tenham no mínimo um arquivo.
	 * 
	 * @param dados
	 */
	public ModelArquivos(Map<Cliente, List<Arquivo>> dados) {

		int tempCli = 0;
		for (Entry<Cliente, List<Arquivo>> e : dados.entrySet()) {
			if (e.getValue() != null) {
				tempCli += e.getValue().size();
			}
		}

		matrix = new Object[tempCli][11];
		
		List<Cliente> list = new ArrayList<>(dados.keySet());
		
		list.sort((o1, o2) -> o1.getNome().compareTo(o2.getNome()));
		
		int cont = 0;
		for (Cliente cli : list) {
			for (Arquivo arq : dados.get(cli)) {
				matrix[cont][0] = cli.getId();
				matrix[cont][1] = cli.getNome();
				matrix[cont][2] = cli.getIp();
				matrix[cont][3] = cli.getPorta();
				matrix[cont][4] = arq.getId();
				matrix[cont][5] = arq.getNome();
				matrix[cont][6] = arq.getExtensao();
				matrix[cont][7] = arq.getMd5();
				matrix[cont][8] = arq.getPath();
				matrix[cont][9] = arq.getTamanho();
				matrix[cont][10] = arq.getDataHoraModificacao();
				
				cont++;
			}
		}
	}

	@Override
	public int getColumnCount() {
		return 11;
	}

	@Override
	public int getRowCount() {
		return matrix.length;
	}

	@Override
	public String getColumnName(int column) {
		switch( column) {
		case 0:
			return "Id Cliente";
		case 1:
			return "Cliente";
		case 2:
			return "Ip Cliente";
		case 3:
			return "Porta Cli";
		case 4:
			return "Id Arq";
		case 5:
			return "Nome Arq";
		case 6:
			return "Extensao";
		case 7:
			return "Md5";
		case 8:
			return "Path";
		case 9:
			return "Tamanho";
		case 10:
			return "Data Hr Mod";
		default:
			return super.getColumnName(column);
		}
	}
	
	@Override
	public Object getValueAt(int arg0, int arg1) {
		return matrix[arg0][arg1];
	}
	
	public Arquivo getArquivo(int linha){
		Arquivo arq = new Arquivo();
		arq.setId((long) matrix[linha][4]);
		arq.setNome((String) matrix[linha][5]);
		arq.setExtensao((String) matrix[linha][6]);
		arq.setMd5((String) matrix[linha][7]);
		arq.setPath((String) matrix[linha][8]);
		arq.setTamanho((long) matrix[linha][9]);
		arq.setDataHoraModificacao( (Date) matrix[linha][10]);
		
//		matrix[cont][4] = arq.getId();
//		matrix[cont][5] = arq.getNome();
//		matrix[cont][6] = arq.getExtensao();
//		matrix[cont][7] = arq.getMd5();
//		matrix[cont][8] = arq.getPath();
//		matrix[cont][9] = arq.getTamanho();
//		matrix[cont][4] = arq.getDataHoraModificacao();
		
		
		return arq;
	}
	
	public Cliente getCliente(int linha){
		Cliente c = new Cliente();
		c.setId((long) matrix[linha][0]);
		c.setNome((String) matrix[linha][1]);
		c.setIp((String) matrix[linha][2]);
		c.setPorta((int) matrix[linha][3]);
		
//		matrix[cont][0] = cli.getId();
//		matrix[cont][1] = cli.getNome();
//		matrix[cont][2] = cli.getIp();
//		matrix[cont][3] = cli.getPorta();
		return c;
	}

}
