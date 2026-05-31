package com.courseenrollment.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseUpdateRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("totalCapacity")
    private Integer totalCapacity;

    @JsonProperty("availableSlots")
    private Integer availableSlots;

    @JsonProperty("endDate")
    private String endDate;

    public CourseUpdateRequest() {
    }

    public CourseUpdateRequest(String title, Integer totalCapacity, Integer availableSlots, String endDate) {
        this.title = title;
        this.totalCapacity = totalCapacity;
        this.availableSlots = availableSlots;
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}