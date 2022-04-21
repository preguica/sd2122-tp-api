package tp1.server.resources.rest;

import java.util.List;
import java.util.logging.Logger;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.service.java.JavaDirectory;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Result.ErrorCode;

@Singleton
public class DirectoryResource implements RestDirectory{
	
	private static Logger Log = Logger.getLogger(UsersResource.class.getName());
	
	private JavaDirectory impl = new JavaDirectory();
	
	public DirectoryResource() {}

	@Override
	public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
		Log.info("writeFile : filename = " + filename + "; data = " + data + "; user = " + userId + "; pwd = " + password);
		try {
			return impl.writeFile(filename, data, userId, password);
		} catch (WebApplicationException e) {
			throw e;
		}
	}

	@Override
	public void deleteFile(String filename, String userId, String password) {
		Log.info("deleteFile : filename = " + filename + "; user = " + userId + "; pwd = " + password);
		try {
			impl.deleteFile(filename, userId, password);
		} catch (WebApplicationException e) {
			throw e;
		}
	}

	@Override
	public void shareFile(String filename, String userId, String userIdShare, String password) {
		Log.info("shareFile : filename = " + filename + "; user = " + userId 
							+ "; userIdShare= " + userIdShare +"; pwd = " + password);
		var result = impl.shareFile(filename, userId, userIdShare, password);
		if (!result.isOK()) {
			throw new WebApplicationException(convertToStatus(result.error()));
		}
	}

	@Override
	public void unshareFile(String filename, String userId, String userIdShare, String password) {
		Log.info("unshareFile : filename = " + filename + "; user = " + userId 
								+"; userIdShare= " + userIdShare +"; pwd = " + password);
		var result = impl.unshareFile(filename, userId, userIdShare, password);
		if (!result.isOK()) {
			throw new WebApplicationException(convertToStatus(result.error()));
		}
		
	}

	@Override
	public byte[] getFile(String filename, String userId, String accUserId, String password) {
		Log.info("getFile : filename = " + filename + "; user = " + userId 
				+" accUserId= " + accUserId +"; pwd = " + password);
		try {
			var result = impl.getFile(filename, userId, accUserId, password);
			if (!result.isOK()) {
				throw new WebApplicationException(convertToStatus(result.error()));
			}
		} catch (WebApplicationException e) {
			throw e;
		}
		return null;
	}

	@Override
	public List<FileInfo> lsFile(String userId, String password) {
		Log.info("lsFile : user = " + userId +"; pwd = " + password);
		var result = impl.lsFile(userId, password);
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
