package tp1.server.resources.rest;

import java.util.logging.Logger;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.java.JavaFiles;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.util.Result.ErrorCode;

public class FilesResource implements RestFiles{

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());
	
	private final JavaFiles impl = new JavaFiles(); 

	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		Log.info("writeFile : " + fileId);
		try {
			impl.writeFile(fileId, data, token);
		} catch (WebApplicationException e) {
			throw e;
		}
	}

	@Override
	public void deleteFile(String fileId, String token) {
		Log.info("deleteFile : " + fileId);
		try {
			impl.deleteFile(fileId, token);
		} catch (WebApplicationException e) {
			throw e;
		}
	}

	@Override
	public byte[] getFile(String fileId, String token) {
		Log.info("getFile : " + fileId);
		var result = impl.getFile(fileId, token);
		if (result.isOK()) {
			return result.value();
		}
		else {
			throw new WebApplicationException(convertToStatus(result.error()));
		}
	}
	
	private Status convertToStatus(ErrorCode error) {
		
		switch (error) {
			case OK:
				return Status.OK;
			case BAD_REQUEST:
				return Status.BAD_REQUEST;
			case FORBIDDEN:
				return Status.FORBIDDEN;
			case CONFLICT:
				return Status.CONFLICT;
			case NOT_FOUND:
				return Status.NOT_FOUND;
			case INTERNAL_ERROR:
				return Status.INTERNAL_SERVER_ERROR;
			case NOT_IMPLEMENTED:
				return Status.NOT_IMPLEMENTED;
			default:
				break;
			}
			return null;
	}

}
