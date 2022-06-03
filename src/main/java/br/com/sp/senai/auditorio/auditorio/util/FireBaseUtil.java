package br.com.sp.senai.auditorio.auditorio.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

	//para ele reconhecer uma logica service, para ser usada no spring 
	@Service

	public class FireBaseUtil {
		// variavel para guardar as credenciais de acesso
		private Credentials credentials;
		// variavel para acessar e manipular o storage
		private Storage storage;
		// constante para o nome do bucket
		private final String BUCKET_NAME = "auditoSenai.appspot.com";
		// constante para o prefixo d url
		private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/";
		// CNOSTANTE PARA O SUFIXO DA URL
		private final String SUFFIX = "?alt=media";
		// constante para a URL
		private final String DODWNLOAD_URL = PREFIX + "%s" + SUFFIX;

		public FireBaseUtil() {
			// acessar o arquivo com a chave privada
			Resource resource = new ClassPathResource("auditosenai.json");
			// gera uma credencial no FireBase através da chave do arquivo
			try {
				credentials = GoogleCredentials.fromStream(resource.getInputStream());
				// cria o storage para manipular os dados no Firebase
				storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}

		private String getExtensao(String nomeArquivo) {
			return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
		}

		// metodo que faz o upload
		public String upload(MultipartFile arquivo) throws IOException {
				// gera um nome aleatorio para o arquivo
			String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
			
			//criar um blobId atraves do nome gerado do arquivo
			
			BlobId blobId = BlobId.of(BUCKET_NAME, nomeArquivo);
			//criando um blobInfor atraves do blodId
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
			//gravar o blobInfo no storage passando os bytes do arquivo 
			storage.create(blobInfo, arquivo.getBytes());
			//retorna a url do arquivo gerado no Storage 
			return String.format(DODWNLOAD_URL, nomeArquivo);
		}
		//metodo que excluir o arquivo do storage
		public void deletar(String nomeaArquivo) {
			//retira prefixo e sufixo da string
			nomeaArquivo = nomeaArquivo.replace(PREFIX, "").replace(SUFFIX, "");
			
			//Obter um Blob através do nome
		com.google.cloud.storage.Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeaArquivo));
			//deletar atraves do blob 
			storage.delete(blob.getBlobId());
			
			
		}

	}

	

