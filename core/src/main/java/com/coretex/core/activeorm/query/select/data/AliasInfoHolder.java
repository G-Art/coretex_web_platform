package com.coretex.core.activeorm.query.select.data;

import com.coretex.core.activeorm.query.select.scanners.FromItemScanner;
import com.coretex.core.activeorm.query.select.scanners.SubSelectScanner;

public class AliasInfoHolder<T> {
	private String alias;
	private T owner;

	private boolean generated = false;

	public AliasInfoHolder(String alias, T owner) {
		this.alias = alias;
		this.owner = owner;
	}

	public AliasInfoHolder(String alias, T owner, boolean generated) {
		this.alias = alias;
		this.owner = owner;
		this.generated = generated;
	}

	public boolean isTable(){
		return owner instanceof FromItemScanner && ((FromItemScanner) owner).isTable();
	}
	public boolean isSubSelect(){
		return owner instanceof SubSelectScanner;
	}

	public String getAlias() {
		return alias;
	}

	public T getOwner() {
		return owner;
	}

	public boolean isGenerated() {
		return generated;
	}

}
