package gov.samhsa.c2s.iexhubpixpdq.service.dto;

import lombok.Data;

@Data
public class EmpiPatientDto {

    private String patientId;
    private String identifier;
    private String identifierType;
}
