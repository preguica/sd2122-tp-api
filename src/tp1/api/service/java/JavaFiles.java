package tp1.api.service.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;

public class JavaFiles implements Files {
	
	public JavaFiles() {}

	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		try {
			File file = new File(fileId);
			FileOutputStream files = new FileOutputStream(file);
			files.write(data);
			files.flush();
			files.close();
		} catch (IOException e) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

	}

	@Override
	public void deleteFile(String fileId, String token) {
		File file = new File(fileId);
		if (!file.exists())
			throw new WebApplicationException(Status.NOT_FOUND);
		if (!file.delete())
			throw new WebApplicationException(Status.BAD_REQUEST);

	}

	@Override
	public Result<byte[]> getFile(String fileId, String token) {
		try {
			File file = new File(fileId);
			if (!file.exists())
				return Result.error(ErrorCode.NOT_FOUND);
			FileInputStream files = new FileInputStream(file);
			byte[] data = files.readAllBytes();
			files.close();
			return Result.ok(data);
		} catch (IOException e) {
			return Result.error(ErrorCode.BAD_REQUEST);
		}
	}

}
