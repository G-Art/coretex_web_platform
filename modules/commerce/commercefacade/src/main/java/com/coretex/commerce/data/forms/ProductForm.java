package com.coretex.commerce.data.forms;

import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.SizeVariantProductItem;
import com.coretex.items.cx_core.StyleVariantProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductForm {
	private UUID uuid;
	private String code;
	private Map<String, String> name;
	private Map<String, String> title;
	private Map<String, String> metaKeywords;
	private Map<String, String> metaDescription;
	private Map<String, String> description;
	private UUID manufacturer;
	private boolean available;
	private UUID store;
	private UUID category;

	private Map<String, String> size;
	private String colorCode;
	private Map<String, String> colorName;

	private String variantType;

	public ProductForm() {
	}

	public ProductForm(ProductItem product) {
		this.uuid = product.getUuid();
		this.code = product.getCode();
		this.name = convertLocale(product.allName());
		this.category = Objects.nonNull(product.getCategory()) ? product.getCategory().getUuid() : null;
		this.title= convertLocale(product.allTitle());
		this.description = convertLocale(product.allDescription());
		this.metaKeywords = convertLocale(product.allMetaKeywords());
		this.metaDescription = convertLocale(product.allMetaDescription());
		this.available = product.getAvailable();
		this.manufacturer = product.getManufacturer() == null ? null : product.getManufacturer().getUuid();
		this.store = product.getStore() == null ? null : product.getStore().getUuid();

		if(product instanceof VariantProductItem){
			this.variantType = product.getMetaType().getTypeCode();
		}

		if(product.getMetaType().getTypeCode().equals(SizeVariantProductItem.ITEM_TYPE)){
			this.size = convertLocale(((SizeVariantProductItem) product).allSize());
		}

		if (product.getMetaType().getTypeCode().equals(StyleVariantProductItem.ITEM_TYPE)){
			var style = ((StyleVariantProductItem) product).getStyle();
			if(Objects.nonNull(style)){
				this.colorCode = style.getCssColorCode();
				this.colorName = convertLocale(style.allStyleName());
			}

		}

	}

	private Map<String, String> convertLocale(Map<Locale, String> localized) {
		if(localized == null){
			return Maps.newHashMap();
		}
		return localized.entrySet()
				.stream()
				.collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, String> getName() {
		return name;
	}

	public UUID getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(UUID manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setName(Map<String, String> name) {
		this.name = name;
	}

	public Map<String, String> getDescription() {
		return description;
	}

	public void setDescription(Map<String, String> description) {
		this.description = description;
	}

	public Map<String, String> getTitle() {
		return title;
	}

	public void setTitle(Map<String, String> title) {
		this.title = title;
	}

	public Map<String, String> getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(Map<String, String> metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public Map<String, String> getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(Map<String, String> metaDescription) {
		this.metaDescription = metaDescription;
	}

	public boolean getAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public UUID getStore() {
		return store;
	}

	public void setStore(UUID store) {
		this.store = store;
	}

	public UUID getCategory() {
		return category;
	}

	public void setCategory(UUID category) {
		this.category = category;
	}

	public String getVariantType() {
		return variantType;
	}

	public void setVariantType(String variantType) {
		this.variantType = variantType;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public Map<String, String> getSize() {
		return size;
	}

	public void setSize(Map<String, String> size) {
		this.size = size;
	}

	public Map<String, String> getColorName() {
		return colorName;
	}

	public void setColorName(Map<String, String> colorName) {
		this.colorName = colorName;
	}
}
