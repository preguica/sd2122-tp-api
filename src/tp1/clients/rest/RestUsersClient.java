package tp1.clients.rest;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Users;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;

public class RestUsersClient extends RestClient implements Users {

	final WebTarget target;
	
	public RestUsersClient( URI serverURI ) {
		super( serverURI );
		target = client.target( serverURI ).path( RestUsers.PATH );
	}
	
	@Override
	public Result<String> createUser(User user) {
		return super.reTry( () -> {
			return clt_createUser( user );
		});
	}

	@Override
	public Result<User> getUser(String userId, String password) {
		return super.reTry( () -> {
			return clt_getUser(userId, password);
		});
	}

	@Override
	public Result<User> updateUser(String userId, String password, User user) {
		return super.reTry( () -> {
			return clt_updateUser(userId, password, user);
		});
	}

	@Override
	public Result<User> deleteUser(String userId, String password) {
			return super.reTry( () -> {
		return clt_deleteUser(userId, password);
		});
	}

	@Override
	public Result<List<User>> searchUsers(String pattern) {
		return super.reTry( () -> clt_searchUsers( pattern ));
	}
	
	@Override
	public Result<Boolean> hasUser(String userId) {
		return super.reTry(() -> {
			return clt_hasUser(userId);
		});
	}

	private Result<String> clt_createUser( User user) {
		
		Response r = target.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON));

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
			return Result.ok(r.readEntity(String.class));
		else {
			System.out.println("Error, HTTP error status: " + r.getStatus() );
			return Result.error(convertToError(r.getStatus()));
		}
	}
	
	private Result<User> clt_getUser(String userId, String password) {
		Response r = target.path( userId )
				.queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			User u = r.readEntity(User.class);
			System.out.println( "User : " + u);
			return Result.ok(u);
		} else {
			System.out.println("Error, HTTP error status: " + r.getStatus() );
			return Result.error(convertToError(r.getStatus()));
		}
	}
	
	private Result<User> clt_updateUser(String userId, String password, User user) {
		Response r = target.path( userId )
				.queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(user, MediaType.APPLICATION_JSON));

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
			return Result.ok(r.readEntity(User.class));
		else {
			System.out.println("Error, HTTP error status: " + r.getStatus() );
			return Result.error(convertToError(r.getStatus()));
		}
	}
	
	private Result<User> clt_deleteUser(String userId, String password) {
		Response r = target.path( userId )
				.queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_JSON)
				.delete();
		
		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
			return Result.ok(r.readEntity(User.class));
		else {
			System.out.println("Error, HTTP error status: " + r.getStatus() );
			return Result.error(convertToError(r.getStatus()));
		}
	}
	
	private Result<List<User>> clt_searchUsers(String pattern) {
		Response r = target
				.queryParam(RestUsers.QUERY, pattern)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
			return Result.ok(r.readEntity(new GenericType<List<User>>() {}));
		else  {
			System.out.println("Error, HTTP error status: " + r.getStatus() );
			return Result.error(convertToError(r.getStatus()));
		}
	}

	private Result<Boolean> clt_hasUser(String userId) {
		Response r = target.path("/hasUser").path( userId ).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			boolean result = r.readEntity(Boolean.class);
			System.out.println( "User exists: " + result);
			return Result.ok(result);
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
