package com.coretex.shop.admin.forms;

import com.coretex.shop.admin.data.LanguageDto;
import com.coretex.shop.admin.data.MerchantStoreDto;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class UserForm implements Serializable {

	private UUID uuid;

	private String login;
	private String password;
	@Email(message = "{messages.invalid.email}")
	private String email;
	private String firstName;
	private String lastName;
	private String adminName;

	private LanguageDto language;
	private Boolean active = true;

	private MerchantStoreDto merchantStore;

	private List<UUID> groups;


	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LanguageDto getLanguage() {
		return language;
	}

	public void setLanguage(LanguageDto language) {
		this.language = language;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public MerchantStoreDto getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(MerchantStoreDto merchantStore) {
		this.merchantStore = merchantStore;
	}

	public List<UUID> getGroups() {
		return groups;
	}

	public void setGroups(List<UUID> groups) {
		this.groups = groups;
	}
}
