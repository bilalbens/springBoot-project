package com.bbenslimane.app.ws.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="users")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = -6180220990563365202L;
	
	@Id				// primary key
	@GeneratedValue   // auto increment in db
	private long id;
	
	@Column(nullable=false)
	private String userId;	
	@Column(nullable=false, length=50)
	private String firstName;
	@Column(nullable=false, length=50)
	private String secondName;
	@Column(nullable=false, length=120, unique=true)
	private String email;
	@Column(nullable=false)
	private String encryptedPassword;
	@Column(nullable=true)
	private String emailVerificationToken;
	//@Column(columnDefinition="boolean default false")
	@Column(nullable=false)
	private Boolean emailVerificationStatus=false;
	
	@OneToMany(mappedBy="user", fetch = FetchType.EAGER ,cascade=CascadeType.ALL)
	private List<AddressEntity> addresses;
	
	@OneToOne(mappedBy="user", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private ContactEntity  contact;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="users")
	private Set<GroupEntity> groups = new HashSet<>();
	
	
	
	
	public List<AddressEntity> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressEntity> addresses) {
		this.addresses = addresses;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}
	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}
	public Boolean getEmailVerificationStatus() {
		return emailVerificationStatus;
	}
	public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}
	public ContactEntity getContact() {
		return contact;
	}
	public void setContact(ContactEntity contact) {
		this.contact = contact;
	}

}
