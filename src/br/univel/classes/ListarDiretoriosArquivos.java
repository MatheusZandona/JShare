package br.univel.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.univel.jshare.comum.Arquivo;

public class ListarDiretoriosArquivos {

	public static List<Arquivo> listar(File dirStart) {

		//File dirStart = new File("." + File.separatorChar);

		List<Arquivo> listaArquivos = new ArrayList<>();
		 
		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {
				Arquivo arq = new Arquivo();
				arq.setNome(file.getName());
				arq.setTamanho(file.length());
				arq.setPath(file.getPath());
				arq.setId(1);
				arq.setExtensao(file.getName().substring((file.getName().lastIndexOf(".") + 1)));
				arq.setNome(arq.getNome().replace("." + arq.getExtensao(), ""));
				arq.setMd5(new Md5Util().getMD5Checksum(arq.getPath()));
				listaArquivos.add(arq);
			} 
		}


//		
//		System.out.println("Arquivos");
//		for (Arquivo arq : listaArquivos) {
//			System.out.println("\t" + arq.getTamanho() + "\t" + arq.getNome());
//		}

		return listaArquivos;
	}
}