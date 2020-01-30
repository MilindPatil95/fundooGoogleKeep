package com.bridgelabz.googlekeep.CustomException;

public class CustomException {

	

	public static class UserNotExistException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public UserNotExistException(String message) {
			super(message);
		}
	}

	@SuppressWarnings("serial")
	public static class EmptyUserListException extends RuntimeException {
		/**
		 * 
		 */		
		public EmptyUserListException(String message) {
			super(message);
		}
	}

	public static class InvalidLabelIdException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public InvalidLabelIdException(String message) {

			super(message);
		}
	}

	public static class InvalidLabelListException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public InvalidLabelListException(String message) {

			super(message);
		}
	}

	public static class InvalidNoteException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvalidNoteException(String message) {
			super(message);
		}

	}

	public static class EmptyNoteListException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EmptyNoteListException(String message) {
			super(message);
		}

	}

	public static class TokenExpiredException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TokenExpiredException(String message) {
			super(message);
		}
	}

	public static class InvalidTokenException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public InvalidTokenException(String message) {
			super(message);
		}
	}

	public static class InvaidLabelException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public InvaidLabelException(String message) {
			super(message);
		}
	}

	public static class EmptyLabelListException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public EmptyLabelListException(String message) {
			super(message);
		}
	}
	@SuppressWarnings("serial")
	public static class ProfileNotSave extends RuntimeException {
		public ProfileNotSave(String message) {
			super(message);
		}

	}

}