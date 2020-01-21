package com.bridgelabz.googlekeep.CustomException;

public class CustomException {

	public static class UserNotExistException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UserNotExistException(String message) {
			super(message);
		}
	}

	public static class EmptyNote extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EmptyNote(String message) {
			super(message);
		}

	}

	public static class EmptyNoteList extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EmptyNoteList(String message) {
			super(message);
		}

	}

	public static class TokenExpired extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TokenExpired(String message) {
			super(message);
		}
	}

	public static class InvalidToken extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvalidToken(String message) {
			super(message);
		}
	}

}
