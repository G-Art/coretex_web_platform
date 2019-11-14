package com.coretex.newpost.api.actions.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentPriceValue {

	@JsonProperty(value = "AssessedCost")
	private Integer assessedCost;

	@JsonProperty(value = "Cost")
	private Integer cost;

	@JsonProperty(value = "CostPack")
	private Integer costPack;

	@JsonProperty(value = "CostRedelivery")
	private Integer costRedelivery = 0;

	public Integer getCostRedelivery() {
		return costRedelivery;
	}

	public void setCostRedelivery(Integer costRedelivery) {
		this.costRedelivery = costRedelivery;
	}

	public Integer getAssessedCost() {
		return assessedCost;
	}

	public void setAssessedCost(Integer assessedCost) {
		this.assessedCost = assessedCost;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Integer getCostPack() {
		return costPack;
	}

	public void setCostPack(Integer costPack) {
		this.costPack = costPack;
	}
}
