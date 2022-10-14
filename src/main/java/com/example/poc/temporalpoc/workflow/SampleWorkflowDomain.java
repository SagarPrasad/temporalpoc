package com.example.poc.temporalpoc.workflow;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleWorkflowDomain {
  private String xml;
  private Date date;
}
