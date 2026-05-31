package com.courseenrollment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CourseRequest {
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("instructor")
    private String instructor;
    
    @JsonProperty("courseCode")
    private String courseCode;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("totalCapacity")
    private Integer totalCapacity;
    
    @JsonProperty("startDate")
    private String startDate;
    
    @JsonProperty("endDate")
    private String endDate;
    
    public CourseRequest() {
    }
    
    public CourseRequest(String titleValue, String instructorValue, String courseCodeValue, String categoryValue, Integer totalCapacityValue, String startDateValue, String endDateValue) {
        this.title = titleValue;
        this.instructor = instructorValue;
        this.courseCode = courseCodeValue;
        this.category = categoryValue;
        this.totalCapacity = totalCapacityValue;
        this.startDate = startDateValue;
        this.endDate = endDateValue;
    }
    
    public CourseRequest(String titleValue, String instructorValue, String courseCodeValue, String categoryValue, Integer totalCapacityValue) {
        this.title = titleValue;
        this.instructor = instructorValue;
        this.courseCode = courseCodeValue;
        this.category = categoryValue;
        this.totalCapacity = totalCapacityValue;
        this.startDate = generateStartDate();
        this.endDate = generateEndDate();
    }
    
    private String generateStartDate() {
        LocalDate startDateValue = LocalDate.now().plusDays(30);
        return startDateValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    private String generateEndDate() {
        LocalDate endDateValue = LocalDate.now().plusDays(90);
        return endDateValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getInstructor() {
        return instructor;
    }
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getTotalCapacity() {
        return totalCapacity;
    }
    
    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}