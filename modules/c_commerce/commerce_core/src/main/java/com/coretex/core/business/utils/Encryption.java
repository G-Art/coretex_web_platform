package com.coretex.core.business.utils;


public interface Encryption {


	/**
	 * Encrypts a string value
	 *
	 * @param value VALUE
	 * @return String encrypted string
	 * @throws Exception cannot encrypt
	 */
	String encrypt(String value) throws Exception;

	/**
	 * Decrypts a string value
	 *
	 * @param value VLUE
	 * @return String encrypted string
	 * @throws Exception cannot encrypt
	 */
	String decrypt(String value) throws Exception;

}
