package tp1.clients.rest;

import java.net.URI;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;

public class RestFilesClient extends RestClient implements Files{

	private final WebTarget target;
	
	public RestFilesClient(URI serverURI) {
		super(serverURI);
		target = client.target( serverURI ).path( RestFiles.PATH );

	}

	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		super.reTry( () -> {
			clt_writeFile(fileId, data, token);
			return null;
		});
		
	}

	@Override
	public void deleteFile(String fileId, String token) {
		super.reTry( () -> {
			clt_deleteFile(fileId, token);
			return null;
		});
		
	}

	@Override
	public Result<byte[]> getFile(String fileId, String token) {
		return super.reTry(() -> {
			return clt_getFile(fileId, token);
		});
	}

	private void clt_writeFile(String fileId, byte[] data, String token) {
		Response r = target.path(fileId).request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
		
		int status = r.getStatus();
		
		if(status == Status.NO_CONTENT.getStatusCode())
			System.out.println("Write File Success");
		else
			System.out.println("Error, HTTP error status: " + status);
	}
	


	private void clt_deleteFile(String fileId, String token) {
		Response r = target.path( fileId ).request()
				.accept(MediaType.APPLICATION_JSON)
				.delete();
		
		int status = r.getStatus();
		
		if(status == Status.NO_CONTENT.getStatusCode())
			System.out.println("Delete File Success");
		else
			System.out.println("Error, HTTP error status: " + status);
		
	}
	
	private Result<byte[]> clt_getFile(String fileId, String token) {
		Response r = target.path( fileId ).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			byte[] f = r.readEntity(byte[].class);
			System.out.println( "File : " + f);
			return Result.ok(f);
		} else {
			System.out.println("Error, HTTP error status: " + r.getStatus() );
			return Result.error(convertToError(r.getStatus()));
		}
	}
	
	private ErrorCode convertToError(int error) {
		
		switch (error) {
			case 200:
				return ErrorCode.OK;
			case 400:
				return ErrorCode.BAD_REQUEST;
			case 403:
				return ErrorCode.FORBIDDEN;
			case 409:
				return ErrorCode.CONFLICT;
			case 404:
				return ErrorCode.NOT_FOUND;
			case 500:
				return ErrorCode.INTERNAL_ERROR;
			case 501:
				return ErrorCode.NOT_IMPLEMENTED;
			default:
				break;
			}
			return null;
}

}
