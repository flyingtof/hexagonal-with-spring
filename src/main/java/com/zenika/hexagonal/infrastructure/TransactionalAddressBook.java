package com.zenika.hexagonal.infrastructure;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zenika.hexagonal.domain.DomainAddressBook;
import com.zenika.hexagonal.domain.AddressBook;
import lombok.experimental.Delegate;

@Service
@Primary
@Transactional
public class TransactionalAddressBook implements AddressBook {
	
	@Delegate
	private final DomainAddressBook domainPersonManager;
	
	public TransactionalAddressBook(DomainAddressBook domainPersonManager) {
		this.domainPersonManager = domainPersonManager;
	}
}
