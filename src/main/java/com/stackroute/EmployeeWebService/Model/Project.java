package com.stackroute.EmployeeWebService.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private int projectId;
    private String projectName;
    private String projectType;
    private String clientName;
    private Date startDate;
    private Date endDate;
}
